package com.example.rest_consumer;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class RestConsumer {

    private static final String BASE_URL = "http://localhost:8080/auth";
    private final RestTemplate restTemplate = new RestTemplate();

    public void register(String username, String password) {
        String url = BASE_URL + "/register";
        HttpHeaders headers = new org.springframework.http.HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"username\":\"%s\", \"password\":\"%s\", \"role\": \"ADMIN\"}", username, password);
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

    ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, request, String.class);
    System.out.println("Protected resource response: " + response.getBody());
    return response.getBody();
    }

    public String getProtectedResource2(String token) {
    String url = "http://localhost:8080/users/status"; // Change to your protected endpoint
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"
    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, request, String.class);
    System.out.println("Protected resource response: " + response.getBody());
    return response.getBody();
    }    

    public String getProtectedResource3(String token) {
    String url = "http://localhost:8080/users/status/admin"; // Change to your protected endpoint
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(token); // Sets "Authorization: Bearer <token>"
    HttpEntity<String> request = new HttpEntity<>(headers);

    ResponseEntity<String> response = restTemplate.exchange(url, org.springframework.http.HttpMethod.GET, request, String.class);
    System.out.println("Protected resource response: " + response.getBody());
    return response.getBody();
    }    

    public void healthCheck() {
        String url = "http://localhost:8080/users/status";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        System.out.println("Health check response: " + response.getBody());
    }

    public static void main(String[] args) {
        RestConsumer client = new RestConsumer();
        client.register("john", "1234");
        String token = client.login("john", "1234");
        if (token != null) {
            System.out.println("Login successful, token: " + token);
            client.getProtectedResource(token);
            client.getProtectedResource2(token);
            client.getProtectedResource3(token);
            client.healthCheck();
        } else {
            System.out.println("Login failed");
        }

    }
}

