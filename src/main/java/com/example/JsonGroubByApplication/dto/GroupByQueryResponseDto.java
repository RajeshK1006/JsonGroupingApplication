package com.example.JsonGroubByApplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

// DTO for returning grouped dataset records based on a specific field

public class GroupByQueryResponseDto {

    @NotNull(message = "'groupedRecords' map cannot be null")
    @NotEmpty(message = "'groupedRecords' map cannot be empty")
    private Map<String, List<Map<String, Object>>> groupedRecords;

    public GroupByQueryResponseDto() {
    }

    public GroupByQueryResponseDto(Map<String, List<Map<String, Object>>> groupedRecords) {
        this.groupedRecords = groupedRecords;
    }

    public Map<String, List<Map<String, Object>>> getGroupedRecords() {
        return groupedRecords;
    }

    public void setGroupedRecords(Map<String, List<Map<String, Object>>> groupedRecords) {
        this.groupedRecords = groupedRecords;
    }
}
