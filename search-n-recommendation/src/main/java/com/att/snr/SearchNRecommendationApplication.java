package com.att.snr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAutoConfiguration
@SpringBootApplication
public class SearchNRecommendationApplication {

	public static void main(String[] args) {
		SpringApplication.run(SearchNRecommendationApplication.class, args);
	}
}
