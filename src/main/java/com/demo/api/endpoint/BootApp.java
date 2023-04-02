package com.demo.api.endpoint;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

/**
 * @author avinash
 */
@SpringBootApplication
public class BootApp {

	public static void main(String[] args) {
		SpringApplication.run(Arrays.asList(BootApp.class, EndpointApplication.class).toArray(new Class[]{}), args);
	}

}
