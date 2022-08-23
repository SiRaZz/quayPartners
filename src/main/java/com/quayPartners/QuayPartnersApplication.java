package com.quayPartners;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class QuayPartnersApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuayPartnersApplication.class, args);
	}

}
