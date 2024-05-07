package com.booking.trainticket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableNeo4jRepositories
@EnableWebSecurity
public class TrainticketApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrainticketApplication.class, args);
	}

}
