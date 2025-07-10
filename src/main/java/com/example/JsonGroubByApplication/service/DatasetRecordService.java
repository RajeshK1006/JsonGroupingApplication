package com.example.JsonGroubByApplication.service;

import com.example.JsonGroubByApplication.dto.GroupByQueryResponseDto;
import com.example.JsonGroubByApplication.dto.InsertResponseDto;
import com.example.JsonGroubByApplication.dto.SortedQueryResponseDto;

import java.util.Map;


// Service interface for dataset record operations: insert, group by, and sort

public interface DatasetRecordService {

    InsertResponseDto insertRecord(String datasetName , Map<String, Object> recordData);

    GroupByQueryResponseDto getGroupedRecords(String datasetName, String groupByField);

    SortedQueryResponseDto getSortedRecords(String datasetName, String sortByField, String order);
}
