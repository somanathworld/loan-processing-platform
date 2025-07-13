package com.gs.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gs.document.Customer;
import com.gs.repository.ICustomerRepository;

@Service
public class CustomerMgmtServiceImpl implements ICustomerMgmtService{

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(String id) {
        return customerRepository.findById(id).orElse(null);
    }

    @Override
    public Customer updateCustomer(String id, Customer customer) {
        customer.setId(id);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(String id) {
        customerRepository.deleteById(id);
    }
}
