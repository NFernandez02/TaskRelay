package com.taskrelay;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class TaskrelayApplication {

	public static void main(String[] args) {
		SpringApplication.run(TaskrelayApplication.class, args);
	}

}
