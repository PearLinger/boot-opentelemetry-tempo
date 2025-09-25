package io.opentelemetry.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients(basePackages = {"io.opentelemetry.example.feign.*","io.opentelemetry.example.feign"})
public class BootOtelTempoApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(BootOtelTempoApiApplication.class, args);
	}
}
