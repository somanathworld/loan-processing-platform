package com.gs.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.gs.document.Customer;

public interface ICustomerRepository extends MongoRepository<Customer, String> {
    // This interface will automatically provide CRUD operations for Customer documents
    // No additional methods are needed unless custom queries are required


}
