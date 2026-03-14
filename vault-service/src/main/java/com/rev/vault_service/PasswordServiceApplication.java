package com.rev.vault_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class PasswordServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(PasswordServiceApplication.class, args);
	}
}
