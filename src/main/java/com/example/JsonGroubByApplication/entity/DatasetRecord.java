package com.example.JsonGroubByApplication.entity;


import jakarta.persistence.*;
// Entity representing a single record in a named dataset stored as JSON

@Entity
@Table(name="dataset_records")
public class DatasetRecord {

    @Id
    @GeneratedValue(strategy =   GenerationType.IDENTITY)
    private Long id;

    private String datasetName;

    private Integer recordId;

    @Column(columnDefinition = "TEXT")
    private String jsonData;



    public DatasetRecord(String datasetName, Integer recordId,String jsonData){
        this.datasetName = datasetName;
        this.recordId = recordId;
        this.jsonData = jsonData;
    }

    public DatasetRecord(){

    }

    public Long getId() {
        return id;
    }

    public Integer getRecordId() {
        return recordId;
    }

    public String getDatasetName() {
        return datasetName;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setDatasetName(String datasetName) {
        this.datasetName = datasetName;
    }

    public void setRecordId(Integer recordId) {
        this.recordId = recordId;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
