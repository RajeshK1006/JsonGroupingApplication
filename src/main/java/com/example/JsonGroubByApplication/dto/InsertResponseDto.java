package com.example.JsonGroubByApplication.dto;
// DTO for returning the response after inserting a record into a dataset
public class InsertResponseDto {

    private String message;
    private String dataset;
    private Integer recordId;

    public InsertResponseDto() {}

    public InsertResponseDto(String message, String dataset, Integer recordId) {
        this.message = message;
        this.dataset = dataset;
        this.recordId = recordId;
    }

    public String getMessage() {
        return message;
    }

    public String getDataset() {
        return dataset;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDataset(String dataset) {
        this.dataset = dataset;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }
}
