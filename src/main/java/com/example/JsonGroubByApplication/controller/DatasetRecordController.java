package com.example.JsonGroubByApplication.controller;

import com.example.JsonGroubByApplication.dto.GroupByQueryResponseDto;
import com.example.JsonGroubByApplication.dto.InsertResponseDto;
import com.example.JsonGroubByApplication.dto.SortedQueryResponseDto;
import com.example.JsonGroubByApplication.service.Impl.DatasetRecordServiceImpl;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


// REST controller for handling dataset record insertions, grouping, and sorting operations
@RestController
@RequestMapping("api/dataset/{datasetName}")
public class DatasetRecordController {

    private static final Logger logger = LoggerFactory.getLogger(DatasetRecordController.class);

    @Autowired
    public DatasetRecordServiceImpl service;

    @PostMapping(value = "/record", consumes = "application/json", produces = "application/json")
    public ResponseEntity<InsertResponseDto> insertRecord(
            @PathVariable String datasetName,
            @Valid @RequestBody Map<String, Object> record
    ) {
        logger.info("POST /record | Dataset: {} | Payload: {}", datasetName, record);
        InsertResponseDto response = service.insertRecord(datasetName, record);
        logger.info("Record inserted successfully for dataset '{}' with ID: {}", datasetName, response.getRecordId());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/query")
    public ResponseEntity<?> queryRecords(
            @PathVariable String datasetName,
            @RequestParam(required = false) String groupBy,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String order
    ) {
        logger.info("GET /query | Dataset: {} | groupBy: {} | sortBy: {} | order: {}", datasetName, groupBy, sortBy, order);

        if (groupBy != null) {
            GroupByQueryResponseDto grouped = service.getGroupedRecords(datasetName, groupBy);
            logger.info("GroupBy '{}' returned {} groups", groupBy, grouped.getGroupedRecords().size());
            return ResponseEntity.ok(grouped);
        } else if (sortBy != null) {
            SortedQueryResponseDto sorted = service.getSortedRecords(datasetName, sortBy, order);
            logger.info("SortBy '{}' returned {} records in {} order", sortBy, sorted.getSortedRecords().size(), order);
            return ResponseEntity.ok(sorted);
        } else {
            logger.warn("Bad request: Neither 'groupBy' nor 'sortBy' parameter provided.");
            return ResponseEntity.badRequest().body("Please provide either 'groupBy' or 'sortBy' query parameter.");
        }
    }
}
