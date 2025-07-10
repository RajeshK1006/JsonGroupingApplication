package com.example.JsonGroubByApplication.service.Impl;

import com.example.JsonGroubByApplication.dto.GroupByQueryResponseDto;
import com.example.JsonGroubByApplication.dto.InsertResponseDto;
import com.example.JsonGroubByApplication.dto.SortedQueryResponseDto;
import com.example.JsonGroubByApplication.entity.DatasetRecord;
import com.example.JsonGroubByApplication.repository.DatasetRecordRepository;
import com.example.JsonGroubByApplication.service.DatasetRecordService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


// Service implementation for handling insert, group, and sort operations on dataset records

@Service
public class DatasetRecordServiceImpl implements DatasetRecordService {

    private static final Logger logger = LoggerFactory.getLogger(DatasetRecordServiceImpl.class);

    @Autowired
    public DatasetRecordRepository repository;

    @Autowired
    public ObjectMapper mapper;

    @Override
    public InsertResponseDto insertRecord(String datasetName, Map<String, Object> recordData) {
        logger.info("Inserting record into dataset '{}'", datasetName);

        try {
            Object idObj = recordData.get("id");
            if (idObj == null) {
                logger.error("'id' field is missing in the input record: {}", recordData);
                throw new IllegalArgumentException("'id' field is required in the JSON");
            }

            Integer recordId = (idObj instanceof Integer) ? (Integer) idObj : Integer.parseInt(idObj.toString());

            String jsonData = mapper.writeValueAsString(recordData);

            DatasetRecord record = new DatasetRecord(datasetName, recordId, jsonData);
            repository.save(record);

            logger.info("Successfully inserted record with ID {} into dataset '{}'", recordId, datasetName);
            return new InsertResponseDto("Record added successfully", datasetName, recordId);
        } catch (JsonProcessingException e) {
            logger.error("JSON parsing error while inserting record: {}", e.getMessage());
            throw new IllegalArgumentException("Failed to parse JSON: " + e.getMessage());
        } catch (NumberFormatException e) {
            logger.error("Invalid 'id' type in record: {}", recordData.get("id"));
            throw new IllegalArgumentException("'id' must be a number");
        }
    }

    @Override
    public GroupByQueryResponseDto getGroupedRecords(String datasetName, String groupByField) {
        logger.info("Grouping records in dataset '{}' by field '{}'", datasetName, groupByField);
        List<DatasetRecord> records = repository.findByDatasetName(datasetName);
        Map<String, List<Map<String, Object>>> grouped = new HashMap<>();

        for (DatasetRecord record : records) {
            try {
                Map<String, Object> map = mapper.readValue(record.getJsonData(), new TypeReference<>() {});
                String key = String.valueOf(map.getOrDefault(groupByField, "UNKNOWN"));

                grouped.computeIfAbsent(key, k -> new ArrayList<>()).add(map);
            } catch (JsonProcessingException e) {
                logger.error("Error converting JSON to map for record ID {}: {}", record.getId(), e.getMessage());
                throw new IllegalArgumentException("Error converting JSON to map: " + e.getMessage());
            }
        }

        logger.info("Completed grouping. Total groups: {}", grouped.size());
        return new GroupByQueryResponseDto(grouped);
    }

    @Override
    public SortedQueryResponseDto getSortedRecords(String datasetName, String sortByField, String order) {
        logger.info("Sorting records in dataset '{}' by field '{}' with order '{}'", datasetName, sortByField, order);
        List<DatasetRecord> records = repository.findByDatasetName(datasetName);
        List<Map<String, Object>> recordList = new ArrayList<>();

        for (DatasetRecord record : records) {
            try {
                Map<String, Object> map = mapper.readValue(record.getJsonData(), new TypeReference<>() {});
                recordList.add(map);
            } catch (JsonProcessingException e) {
                logger.error("Invalid JSON format in stored record ID {}: {}", record.getId(), e.getMessage());
                throw new IllegalArgumentException("Invalid JSON format in stored record", e);
            }
        }

        Comparator<Map<String, Object>> comparator = (map1, map2) -> {
            Object val1 = map1.get(sortByField);
            Object val2 = map2.get(sortByField);

            if (val1 == null || val2 == null) return 0;

            if (val1 instanceof Number && val2 instanceof Number) {
                return Double.compare(((Number) val1).doubleValue(), ((Number) val2).doubleValue());
            }

            return val1.toString().compareTo(val2.toString());
        };

        if ("desc".equalsIgnoreCase(order)) {
            comparator = comparator.reversed();
        }

        recordList.sort(comparator);
        logger.info("Completed sorting. Total records sorted: {}", recordList.size());

        return new SortedQueryResponseDto(recordList);
    }
}
