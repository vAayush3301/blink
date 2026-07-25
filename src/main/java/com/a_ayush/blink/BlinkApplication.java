package com.a_ayush.blink;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.Instant;
import java.time.temporal.Temporal;

@SpringBootApplication
public class BlinkApplication {

	public static final Instant START_TIME = Instant.now();

	public static void main(String[] args) {
		SpringApplication.run(BlinkApplication.class, args);
	}

}
