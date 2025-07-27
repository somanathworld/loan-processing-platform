package com.example.rest_consumer;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

public class K8s {

    RestTemplate restTemplate = new RestTemplate();
    public static final String URL = "http://192.168.49.2:32189";

    public String AuthRegister(String username, String password) {
        // This method is intended to handle registration logic.
        String registerUrl = URL + "/auth/register";
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        String requestBody = "{\"username\":\"" + username + "\",\"password\":\"" + password + "\"}";
        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);
        String result = restTemplate.postForObject(registerUrl, requestEntity, String.class);
        return result;
    }

    public String login(String username, String password) {
        String url = URL + "/auth/login";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(url, request, Map.class);
        System.out.println("Login response: " + response.getBody());
        return response.getBody() != null ? (String) response.getBody().get("token") : null;
    }

    
    public static void main(String[] args) {
        K8s k8s = new K8s();
        System.out.println(k8s.AuthRegister("john1", "12345"));
        String token = k8s.login("john1", "12345");
        if (token != null) {
            System.out.println("Login successful, token: " + token);
        } else {
            System.out.println("Login failed");
        }
    }
}
