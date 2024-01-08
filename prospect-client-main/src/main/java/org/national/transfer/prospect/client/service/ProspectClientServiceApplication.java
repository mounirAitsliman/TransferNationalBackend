package org.national.transfer.prospect.client.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableEurekaClient
public class ProspectClientServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ProspectClientServiceApplication.class, args);
    }
}
