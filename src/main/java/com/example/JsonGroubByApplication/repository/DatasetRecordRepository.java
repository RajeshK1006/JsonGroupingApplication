package com.example.JsonGroubByApplication.repository;

import com.example.JsonGroubByApplication.entity.DatasetRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


// Repository interface for DatasetRecord entity to perform CRUD and custom queries

public interface DatasetRecordRepository extends JpaRepository<DatasetRecord,Long> {

     List<DatasetRecord> findByDatasetName(String name);
}
