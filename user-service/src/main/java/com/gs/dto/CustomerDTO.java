package com.gs.dto;

import lombok.Data;

@Data
public class CustomerDTO {
    private String customerId;
    private String name;
    private String email;
    private String status;
    // getters/setters
}
