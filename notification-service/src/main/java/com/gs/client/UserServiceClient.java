package com.gs.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gs.dto.CustomerDTO;

@FeignClient(name = "user-service")
public interface UserServiceClient {

    @GetMapping("/users/info/{id}")
    CustomerDTO getUserInfo(@PathVariable("id") String customerId);
}