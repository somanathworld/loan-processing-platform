package com.gs.controller;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.gs.dto.CustomerDTO;
import com.gs.service.ICustomerMgmtService;

@RestController
@RequestMapping("/users")
public class CustomerController {

    @Autowired
    private ICustomerMgmtService customerService;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    @GetMapping("/public")
    public String anyoneCanAccess() {
        return "Public endpoint";
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/health/admin")
    public String getHelth() {
        return "User Service is running";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @GetMapping("/info")
    public String getInfo() {

        logger.info("🔍 [UserService] Handling /users/status - TraceID: {}");
        return "User Service is running";
    }

    @GetMapping("/status/{userId}")
    public ResponseEntity<String> getUserStatus(@PathVariable("userId") String userId){
        Customer cust = customerService.getCustomerById(userId);
        if(cust != null){
            return ResponseEntity.ok(cust.getStatus() == null ? "ACTIVE": cust.getStatus());
        }else{
            return ResponseEntity.status(HttpStatus.NOT_EXTENDED).body("User not found.");
        }
    }

    @GetMapping("/info/{id}")
    public ResponseEntity<?> getUserInfo(@PathVariable String id) {
        Customer cust = customerService.getCustomerById(id);
        if(cust != null){
            CustomerDTO dto = new CustomerDTO();
            dto.setCustomerId(cust.getId());
            dto.setEmail(cust.getEmail());
            dto.setName(cust.getName());
            dto.setStatus(cust.getStatus());

            return ResponseEntity.ok(dto);
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
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
