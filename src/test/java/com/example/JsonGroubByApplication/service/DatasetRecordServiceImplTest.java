package com.example.JsonGroubByApplication.service;

import com.example.JsonGroubByApplication.dto.GroupByQueryResponseDto;
import com.example.JsonGroubByApplication.dto.InsertResponseDto;
import com.example.JsonGroubByApplication.dto.SortedQueryResponseDto;
import com.example.JsonGroubByApplication.entity.DatasetRecord;
import com.example.JsonGroubByApplication.repository.DatasetRecordRepository;
import com.example.JsonGroubByApplication.service.Impl.DatasetRecordServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.fasterxml.jackson.core.type.TypeReference;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class DatasetRecordServiceImplTest {

    @Mock
    private DatasetRecordRepository repository;

    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private DatasetRecordServiceImpl service;


    private Map<String,Object> sampleRecord;


    //initializes the mock and inject mocks
    @BeforeEach
    void setUp(){
        MockitoAnnotations.openMocks(this);
        sampleRecord = new HashMap<>();
        sampleRecord.put("id", 1);
        sampleRecord.put("name", "John");
        sampleRecord.put("age" ,25);
        sampleRecord.put("department", "Engineering");
    }

    @Test
    void testInsertRecord_success() throws Exception{
        when(mapper.writeValueAsString(sampleRecord)).thenReturn(
                "{\"id\":1,\"name\":\"John\",\"department\":\"Engineering\",\"age\":25}"
        );

        InsertResponseDto response = service.insertRecord("employees",sampleRecord);

        assertEquals("employees", response.getDataset());
        assertEquals(1,response.getRecordId());
        assertEquals("Record added successfully",response.getMessage());

        verify(repository, times(1)).save(any(DatasetRecord.class));

    }

    @Test
    void testInsertRecordJsonConversionFails() throws Exception{
        when(mapper.writeValueAsString(sampleRecord)).thenThrow(new RuntimeException("JSON conversion error"));

        RuntimeException exception = assertThrows(RuntimeException.class, ()-> service.insertRecord("employees",sampleRecord));

        assertEquals("JSON conversion error",exception.getMessage());
        verify(repository,never()).save(any());

    }

    @Test
    void testGroupBy_withMissingFieldInRecord() throws Exception {
        DatasetRecord record1 = new DatasetRecord("employees", 1, "{\"id\":1,\"name\":\"Alice\"}");
        List<DatasetRecord> records = List.of(record1);

        when(repository.findByDatasetName("employees")).thenReturn(records);
        when(mapper.readValue(eq(record1.getJsonData()), ArgumentMatchers.<TypeReference<Map<String, Object>>>any()))
                .thenReturn(Map.of("id", 1, "name", "Alice"));

        GroupByQueryResponseDto response = service.getGroupedRecords("employees", "department");

        assertEquals(1, response.getGroupedRecords().size());
        assertTrue(response.getGroupedRecords().containsKey("UNKNOWN"));
    }

    @Test
    void testSortBy_AgeCorrectly() throws Exception{
        DatasetRecord record1 = new DatasetRecord("employees", 1,
                "{\"id\":1,\"name\":\"Alice\",\"age\":30,\"department\":\"HR\"}");
        DatasetRecord record2 = new DatasetRecord("employees", 2,
                "{\"id\":2,\"name\":\"Bob\",\"age\":25,\"department\":\"Engineering\"}");
        DatasetRecord record3 = new DatasetRecord("employees", 3,
                "{\"id\":3,\"name\":\"Charlie\",\"age\":28,\"department\":\"Finance\"}");

        List<DatasetRecord> allRecords = List.of(record1, record2, record3);

        TypeReference<Map<String, Object>> mapType = new TypeReference<Map<String, Object>>() {};
        when(repository.findByDatasetName("employees")).thenReturn(allRecords);

        when(mapper.readValue(eq(record1.getJsonData()),ArgumentMatchers.<TypeReference<Map<String,Object>>>any())).thenReturn(Map.of("id", 1, "name", "Alice", "age", 30, "department", "HR"));
        when(mapper.readValue(eq(record2.getJsonData()), ArgumentMatchers.<TypeReference<Map<String,Object>>>any())).thenReturn(Map.of("id", 2, "name", "Bob", "age", 25, "department", "Engineering"));
        when(mapper.readValue(eq(record3.getJsonData()),ArgumentMatchers.<TypeReference<Map<String,Object>>>any())).thenReturn(Map.of("id", 3, "name", "Charlie", "age", 28, "department", "Finance"));



        SortedQueryResponseDto response = service.getSortedRecords("employees","age","asc");

        List<Map<String, Object>> sorted = response.getSortedRecords();


        assertEquals("Bob", sorted.get(0).get("name"));      // age 25
        assertEquals("Charlie", sorted.get(1).get("name"));  // age 28
        assertEquals("Alice", sorted.get(2).get("name"));    // age 30
    }

    @Test
    void testGroupBy_validField() throws Exception {
        DatasetRecord record1 = new DatasetRecord("employees", 1, "{\"id\":1,\"name\":\"Alice\",\"dept\":\"HR\"}");
        DatasetRecord record2 = new DatasetRecord("employees", 2, "{\"id\":2,\"name\":\"Bob\",\"dept\":\"HR\"}");
        DatasetRecord record3 = new DatasetRecord("employees", 3, "{\"id\":3,\"name\":\"Charlie\",\"dept\":\"IT\"}");

        List<DatasetRecord> allRecords = List.of(record1, record2, record3);

        when(repository.findByDatasetName("employees")).thenReturn(allRecords);
        when(mapper.readValue(eq(record1.getJsonData()), any(TypeReference.class)))
                .thenReturn(Map.of("id", 1, "name", "Alice", "dept", "HR"));
        when(mapper.readValue(eq(record2.getJsonData()), any(TypeReference.class)))
                .thenReturn(Map.of("id", 2, "name", "Bob", "dept", "HR"));
        when(mapper.readValue(eq(record3.getJsonData()), any(TypeReference.class)))
                .thenReturn(Map.of("id", 3, "name", "Charlie", "dept", "IT"));

        GroupByQueryResponseDto response = service.getGroupedRecords("employees", "dept");

        assertEquals(2, response.getGroupedRecords().size());
        assertTrue(response.getGroupedRecords().containsKey("HR"));
        assertTrue(response.getGroupedRecords().containsKey("IT"));
    }






}
