package com.ericsson.eniq.sbkafka;

import java.sql.DriverManager;

import org.apache.avro.generic.GenericRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.event.KafkaEvent;

import com.ericsson.eniq.parser.cache.DBLookupCache;
import com.ericsson.eniq.sbkafka.cache.DataFormatCacheImpl;
import com.ericsson.eniq.sbkafka.cache.TransformerCacheImpl;

@SpringBootApplication(scanBasePackages={"com.ericsson.eniq.sbkafka","com.ericsson.eniq.sbkafka.config","com.ericsson.eniq.sbkafka.controller"})

public class SbkafkaApplication implements ApplicationListener<KafkaEvent> {
	
	@Autowired
	private Environment env;
	
	@Autowired
	private DataFormatCacheImpl dataformats;

	public static void main(String[] args) {
		SpringApplication.run(SbkafkaApplication.class, args);
	}

	@Override
	public void onApplicationEvent(KafkaEvent event) {
		System.out.println(event);
		
	}
	
	@Bean
	public ApplicationRunner runner(KafkaListenerEndpointRegistry registry ) {
		return args -> {
			System.out.println("PAUSING THE CONSUMER");
			registry.getListenerContainer("PM_E_ERBS_FILES_GROUP").pause();
			String repdbUrl = env.getProperty("db.repdb.url");
			String dwhdbUrl = env.getProperty("db.dwhdb.url");
			String repDbDriver = env.getProperty("db.repdb.driver");
			String dwhDbDriver = env.getProperty("db.dwhdb.driver");
			String etlrepUser = env.getProperty("db.repdb.etlrep.user");
			String etlrepPass = env.getProperty("db.repdb.etlrep.pass");
			String dwhrepUser = env.getProperty("db.repdb.dwhrep.user");
			String dwhrepPass = env.getProperty("db.repdb.dwhrep.pass");
			String dwhdbUser = env.getProperty("db.dwhdb.user");
			String dwhdbPass = env.getProperty("db.dwhdb.pass");
			
			DriverManager.registerDriver(new org.postgresql.Driver());
			DriverManager.registerDriver(new com.sybase.jdbc4.jdbc.SybDriver());
			DBLookupCache.initialize(dwhDbDriver, 
	    			dwhdbUrl, dwhdbUser, dwhdbPass);
			
			TransformerCacheImpl dbread = new TransformerCacheImpl();
	    	dbread.readDB(repdbUrl, 
	    			dwhrepUser, dwhrepPass, repDbDriver,"dwhrep", "DC_E_ERBS");
	    	
			//DataFormatCacheImpl dataformats = new DataFormatCacheImpl();
	    	dataformats.readDB(repdbUrl, 
	    			dwhrepUser, dwhrepPass, repDbDriver, "DC_E_ERBS");
	    	  	
	    	
	    	
	    	System.out.println("START UP SQUENCE COMPLETE!");
	    	registry.getListenerContainer("PM_E_ERBS_FILES_GROUP").resume();
	    	System.out.println("CONSUMER RESUMED");
		};
		
	}

}
