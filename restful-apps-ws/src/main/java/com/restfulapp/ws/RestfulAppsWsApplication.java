package com.restfulapp.ws;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@EnableDiscoveryClient
@SpringBootApplication
public class RestfulAppsWsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestfulAppsWsApplication.class, args);
	}

}
