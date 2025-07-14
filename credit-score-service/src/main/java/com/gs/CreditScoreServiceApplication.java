package com.gs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class CreditScoreServiceApplication {

	@Value("${spring.redis.host}")
    private String redisHost;

	public static void main(String[] args) {
		SpringApplication.run(CreditScoreServiceApplication.class, args);
	}
	@PostConstruct
    public void debugRedisHost() {
        System.out.println("🧪 Redis Host loaded from config: " + redisHost);
    }

}
