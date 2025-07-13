package com.gs.controller;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gs.document.Customer;
import com.gs.service.ICustomerMgmtService;

@RestController
@RequestMapping("/users")
public class CustomerController {

    @Autowired
    private ICustomerMgmtService customerService;

    @GetMapping("/public")
    public String anyoneCanAccess() {
        return "Public endpoint";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/status/admin")
    public String getUserStatus2() {
        return "User Service is running";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/status")
    public String getUserStatus() {
        return "User Service is running";
    }

    @PostMapping("/register")
    public ResponseEntity<Customer> register(@RequestBody Customer customer) {
        Customer saved = customerService.createCustomer(customer);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/upload-kyc/{id}")
    public ResponseEntity<String> uploadKYC(@PathVariable String id,
            @RequestParam("file") MultipartFile file) throws IOException {
        Customer cust = customerService.getCustomerById(id);
        if (cust == null)
            return ResponseEntity.notFound().build();

        String uploadDir = "/uploads";
        File target = new File(uploadDir + File.separator + file.getOriginalFilename());
        target.getParentFile().mkdirs();
        System.out.println("KYC file uploaded to: " + target.getAbsolutePath());
        file.transferTo(target);

        cust.setKycFilePath(target.getAbsolutePath());
        customerService.createCustomer(cust);

        return ResponseEntity.ok("KYC uploaded successfully.");
    }

}
