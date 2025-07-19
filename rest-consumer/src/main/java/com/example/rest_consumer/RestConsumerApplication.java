package com.example.rest_consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class RestConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestConsumerApplication.class, args);
        RestConsumer client = new RestConsumer();   
        client.register("john1", "12345");
        String token = client.login("john1", "12345");
        if (token != null) {
            System.out.println("Login successful, token: " + token);
            client.getProtectedResource(token);
            client.getProtectedResource2(token);
            client.getProtectedResource3(token);
            client.registerCustomer(token);
            client.uploadKYC(token, "68714419089e5f073d531f41",
                    "/workspaces/loan-processing-platform/docker-compose.yml");
            client.loanStatus(token);
            client.applyForLoan(token, "687b29e94e36203261e8194d" , 30000.00);
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
