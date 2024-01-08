package org.national.transfer.gatewaye.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GatewayeServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayeServiceApplication.class, args);
	}

}
