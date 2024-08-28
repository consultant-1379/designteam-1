package com.ericsson.eniq.flsmock;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.google.gson.Gson;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

public class FLSSwaggerConfig {
	@Configuration
	public class SpringFoxConfig {                                    
	    @Bean
	    public Docket api() { 
	        return new Docket(DocumentationType.SWAGGER_2)
	        		.apiInfo(apiInfo())
	          .select()                                  
	          .apis(RequestHandlerSelectors.basePackage("com.ericsson.eniq.flsmock.controller"))         
	          .paths(PathSelectors.any())                          
	          .build();                                           
	    }
	}
	private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("PARSER APIs")
                .description("API for managing parser tasks")
                .version("1.0")
                .build();
    }
	@Bean
	public Gson gson() {
		return new Gson();
	}
}
