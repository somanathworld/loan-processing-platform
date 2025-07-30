package com.example.rest_consumer;

import java.io.File;
import java.util.Map;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RestConsumer {

    private static final String BASE_URL = "http://localhost:8080/auth";
    private final RestTemplate restTemplate = new RestTemplate();

    public void register(String username, String password) {
        String url = BASE_URL + "/register";
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"username\":\"%s\", \"password\":\"%s\", \"role\": \"ADMIN\"}", username,
                password);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println("Register response: " + response.getBody());
    }

    public String login(String username, String password) {
        String url = BASE_URL + "/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        System.out.println("Login response: " + response.getBody());
        return response.getBody() != null ? (String) response.getBody().get("token") : null;
    }

    public String getProtectedResource(String token) {
        String url = BASE_URL + "/status"; // Change to your protected endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, request,
                String.class);
        System.out.println("Protected resource response: " + response.getBody());
        return response.getBody();
    }

    public String getProtectedResource2(String token) {
        String url = "http://localhost:8080/users/info"; // Change to your protected endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, request,
                String.class);
        System.out.println("Protected resource response: " + response.getBody());
        return response.getBody();
    }

    public String getProtectedResource3(String token) {
        String url = "http://localhost:8080/users/health/admin"; // Change to your protected endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, request,
                String.class);
        System.out.println("Protected resource response: " + response.getBody());
        return response.getBody();
    }

    

    public String registerCustomer(String token) {
        String url = "http://localhost:8080/users/register";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"

        String body = "{\"name\":\"John Doe\", \"email\":\"john.doe@example.com\", \"phone\":\"1234567890\"}";
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println("Register customer response: " + response.getBody());
        return response.getBody();
    }

    public String uploadKYC(String token, String customerId, String filePath) {
        String url = "http://localhost:8080/users/upload-kyc/" + customerId;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"

        // Create a multipart request body
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", new FileSystemResource(filePath));

        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println("Upload KYC response: " + response.getBody());
        return response.getBody();
    }

    public String loanStatus(String token) {
        String url = "http://localhost:8080/loans/status/fail"; // Change to your loan status endpoint
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        System.out.println("Loan status response: " + response.getBody());
        return response.getBody();
    }

    public String applyForLoan(String token, String customerId, double amount) {
        String url = "http://localhost:8080/loans/apply";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"

        String body = String.format("{\"customerId\":\"%s\", \"amount\":%.2f}", customerId,  amount);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, request, String.class);
        System.out.println("Apply for loan response: " + response.getBody());
        return response.getBody();
    }

    public String approveLoan(String token, String loanId) {
        String url = "http://localhost:8080/loans/approve/" + loanId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        System.out.println("Approve loan response: " + response.getBody());
        return response.getBody();
    }

    public String getCreditScore(String token, String customerId) {
        String url = "http://localhost:8080/scores/" + customerId;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"
        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
        System.out.println("Credit score response: " + response.getBody());
        return response.getBody();
    }

    public static void main(String[] args) {

        RestConsumer client = new RestConsumer();
        //client.register("john1", "12345");
        String token = client.login("john1", "12345");
        if (token != null) {
            System.out.println("Login successful, token: " + token);
            client.getProtectedResource3(token);
            client.registerCustomer(token);
            client.uploadKYC(token, "68714419089e5f073d531f41",
                    "/workspaces/loan-processing-platform/docker-compose.yml");
            client.loanStatus(token);
            client.applyForLoan(token, "68714419089e5f073d531f41" , 30000.00);
            try{
                Thread.sleep(7000);
            }catch(Exception ex){
                ex.printStackTrace();
            }
            client.getCreditScore(token, "687b29e94e36203261e8194d");
            client.approveLoan(token, "6836d5dd-38e9-4686-a7a0-878761827ce9");
           
        } else {
            System.out.println("Login failed");
        }

    }
}
