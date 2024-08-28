package com.ericsson.eniq.flsmock;

import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import com.ericsson.eniq.flsmock.pojo.ParserInput;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@SpringBootTest

class FlsmockApplicationTests {
	
	@Test
	void contextLoads() {
	}
	
	/*@Test
	void testResourceRead() throws Exception {
		Gson gson = new Gson();
		InputStream resource = new ClassPathResource(
			      "/ParserInput.json").getInputStream();
			    try (JsonReader reader = gson.newJsonReader(new InputStreamReader(resource))) {
			    	ParserInput pInput = gson.fromJson(reader, ParserInput.class);
			    	System.out.println(" result : "+ pInput.toString());
			    }
	}*/

}
