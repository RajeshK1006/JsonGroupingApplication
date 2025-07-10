package com.example.JsonGroubByApplication.controller;


import com.example.JsonGroubByApplication.dto.GroupByQueryResponseDto;
import com.example.JsonGroubByApplication.dto.InsertResponseDto;
import com.example.JsonGroubByApplication.dto.SortedQueryResponseDto;
import com.example.JsonGroubByApplication.service.Impl.DatasetRecordServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DatasetRecordController.class)
class DatasetRecordControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private DatasetRecordServiceImpl service;

    @Autowired
    private ObjectMapper objectMapper;

    //1. Insert Record Test
    @Test
    void testInsertRecord_success() throws Exception {
        Map<String, Object> request = Map.of("id", 1, "name", "Alice", "age", 30, "department", "HR");

        InsertResponseDto response = new InsertResponseDto("Record added successfully", "employees", 1);
        when(service.insertRecord(eq("employees"), eq(request))).thenReturn(response);

        mockMvc.perform(post("/api/dataset/employees/record")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Record added successfully"))
                .andExpect(jsonPath("$.dataset").value("employees"))
                .andExpect(jsonPath("$.recordId").value(1));
    }

    //2. GroupBy Department
    @Test
    void testGroupBy_department_success() throws Exception {
        Map<String, List<Map<String, Object>>> grouped = Map.of(
                "HR", List.of(Map.of("id", 1, "name", "Alice", "age", 30, "department", "HR")),
                "Engineering", List.of(Map.of("id", 2, "name", "Bob", "age", 25, "department", "Engineering"))
        );

        when(service.getGroupedRecords("employees", "department"))
                .thenReturn(new GroupByQueryResponseDto(grouped));

        mockMvc.perform(get("/api/dataset/employees/query")
                        .param("groupBy", "department"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.groupedRecords.HR[0].name").value("Alice"))
                .andExpect(jsonPath("$.groupedRecords.Engineering[0].name").value("Bob"));
    }

    //3. SortBy Age Ascending
    @Test
    void testSortBy_age_ascending_success() throws Exception {
        List<Map<String, Object>> sortedList = List.of(
                Map.of("id", 2, "name", "Bob", "age", 25, "department", "Engineering"),
                Map.of("id", 1, "name", "Alice", "age", 30, "department", "HR")
        );

        when(service.getSortedRecords("employees", "age", "asc"))
                .thenReturn(new SortedQueryResponseDto(sortedList));

        mockMvc.perform(get("/api/dataset/employees/query")
                        .param("sortBy", "age")
                        .param("order", "asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sortedRecords[0].name").value("Bob"))
                .andExpect(jsonPath("$.sortedRecords[1].name").value("Alice"));
    }

    //4. SortBy Age Descending
    @Test
    void testSortBy_age_descending_success() throws Exception {
        List<Map<String, Object>> sortedList = List.of(
                Map.of("id", 1, "name", "Alice", "age", 30, "department", "HR"),
                Map.of("id", 2, "name", "Bob", "age", 25, "department", "Engineering")
        );

        when(service.getSortedRecords("employees", "age", "desc"))
                .thenReturn(new SortedQueryResponseDto(sortedList));

        mockMvc.perform(get("/api/dataset/employees/query")
                        .param("sortBy", "age")
                        .param("order", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sortedRecords[0].name").value("Alice"))
                .andExpect(jsonPath("$.sortedRecords[1].name").value("Bob"));
    }

    //5. Bad Request - No Params
    @Test
    void testQueryWithoutGroupByOrSortBy_returnsBadRequest() throws Exception {
        mockMvc.perform(get("/api/dataset/employees/query"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Please provide either 'groupBy' or 'sortBy' query parameter."));
    }
}
