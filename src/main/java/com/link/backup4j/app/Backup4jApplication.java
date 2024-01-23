package com.link.backup4j.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class Backup4jApplication {

	public static void main(String[] args) {
		SpringApplication.run(Backup4jApplication.class, args);
	}

}
