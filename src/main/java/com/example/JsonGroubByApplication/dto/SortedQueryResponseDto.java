package com.example.JsonGroubByApplication.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Map;

// DTO for returning sorted dataset records based on a specific field

public class SortedQueryResponseDto {
    @NotNull(message = "'sortedRecords' list cannot be null")
    @NotEmpty(message = "'sortedRecords' list cannot be empty")
    private List<Map<String, Object>> sortedRecords;

    public SortedQueryResponseDto() {
    }

    public SortedQueryResponseDto(List<Map<String, Object>> sortedRecords) {
        this.sortedRecords = sortedRecords;
    }

    public List<Map<String, Object>> getSortedRecords() {
        return sortedRecords;
    }

    public void setSortedRecords(List<Map<String, Object>> sortedRecords) {
        this.sortedRecords = sortedRecords;
    }
}
