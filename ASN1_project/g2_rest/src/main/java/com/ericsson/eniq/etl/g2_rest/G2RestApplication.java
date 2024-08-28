package com.ericsson.eniq.etl.g2_rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class G2RestApplication {

	public static void main(String[] args) {
		SpringApplication.run(G2RestApplication.class, args);
	}

}
