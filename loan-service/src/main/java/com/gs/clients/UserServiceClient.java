package com.gs.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.gs.config.FeignClientInterceptor;

@FeignClient(name = "user-service", configuration = FeignClientInterceptor.class) // Use registered service name in Eureka
public interface UserServiceClient {

    @GetMapping("/users/status/{userId}")
    String getUserStatus(@PathVariable("userId") String userId);
}
