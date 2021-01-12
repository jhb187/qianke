package com.qianke;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ComponentScan(basePackages = {"com.qianke"})
@EnableScheduling
public class QiankeApplication {

	public static void main(String[] args) {
		SpringApplication.run(QiankeApplication.class, args);
	}

}