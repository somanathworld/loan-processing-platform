package com.gs.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document(collection = "customers")
@Data
public class Customer {
    @Id
    private String id;

    private String name;
    private String email;
    private String phone;
    private String status;
    private String kycFilePath; // file system path

    // Getters & Setters
}