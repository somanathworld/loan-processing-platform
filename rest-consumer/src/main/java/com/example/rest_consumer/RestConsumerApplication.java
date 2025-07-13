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
            System.out.println(client.registerCustomer(token));
            System.out.println(
                    client.uploadKYC(token, "68714419089e5f073d531f41",
                            "/workspaces/loan-processing-platform/docker-compose.yml"));
            System.out.println(client.loanStatus(token));
            System.out.println(client.applyForLoan(token, "12344", "2344L", 30000.00));

        } else {
            System.out.println("Login failed");
        }

	}

}
