package com.gs.service;

import com.gs.document.Customer;

public interface ICustomerMgmtService {

    Customer createCustomer(Customer customer);
    Customer getCustomerById(String id);
    Customer updateCustomer(String id, Customer customer);
    void deleteCustomer(String id);

}
