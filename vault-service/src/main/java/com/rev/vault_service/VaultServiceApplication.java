package com.rev.vault_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableFeignClients
@EnableScheduling
@SpringBootApplication
public class VaultServiceApplication {
	public static void main(String[] args) {
		SpringApplication.run(VaultServiceApplication.class, args);
	}
}
