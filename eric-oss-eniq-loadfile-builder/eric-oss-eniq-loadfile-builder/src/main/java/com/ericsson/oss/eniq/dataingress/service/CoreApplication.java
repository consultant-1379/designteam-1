package com.ericsson.oss.eniq.dataingress.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
//import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.ericsson.oss.eniq.dataingress.service.cache.TableNameCache;

//import io.micrometer.core.instrument.MeterRegistry;

@SpringBootApplication
public class CoreApplication implements ApplicationRunner {
	@Autowired
	TableNameCache cache;
	public static void main(String[] args) {
		SpringApplication.run(CoreApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
         cache.initCache();		
	}
	
//	@Bean
//    MeterRegistryCustomizer<MeterRegistry> metricsCommonTags() {
//        return registry -> registry.config().commonTags("application", "your-service-name").commonTags("instance",System.getenv("HOSTNAME"));
//    }

}
