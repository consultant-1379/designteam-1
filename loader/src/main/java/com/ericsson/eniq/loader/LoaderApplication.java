package com.ericsson.eniq.loader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = { "com.ericsson.eniq.loader.common" })
public class LoaderApplication {

	Logger logger = LogManager.getLogger(LoaderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(LoaderApplication.class, args);
	}
	
	
}