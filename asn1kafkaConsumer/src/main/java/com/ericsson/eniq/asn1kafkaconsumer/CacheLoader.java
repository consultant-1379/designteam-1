package com.ericsson.eniq.asn1kafkaconsumer;
/*package com.ericsson.eniq.sbkafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ericsson.eniq.parser.cache.DBLookupCache;
import com.ericsson.eniq.sbkafka.cache.DataFormatCacheImpl;
import com.ericsson.eniq.sbkafka.cache.TransformerCacheImpl;


@Component
public class CacheLoader implements ApplicationRunner{
	
	@Autowired
	private Environment env;
		
	String transId;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		initializeCache();
		//runTests();
		
	}
	
	@Bean
	private void initializeCache(KafkaListenerEndpointRegistry registry,
			 KafkaTemplate<String, String> template) {
		System.out.println("PAUSING THE CONSUMER");
		registry.getListenerContainer("mdcgroup").pause();
		String repdbUrl = env.getProperty("db.repdb.url");
		String dwhdbUrl = env.getProperty("db.dwhdb.url");
		String driver = env.getProperty("db.driver");
		String etlrepUser = env.getProperty("db.repdb.etlrep.user");
		String etlrepPass = env.getProperty("db.repdb.etlrep.pass");
		String dwhrepUser = env.getProperty("db.repdb.dwhrep.user");
		String dwhrepPass = env.getProperty("db.repdb.dwhrep.pass");
		String dwhdbUser = env.getProperty("db.dwhdb.user");
		String dwhdbPass = env.getProperty("db.dwhdb.pass");
    	DBLookupCache.initialize(driver, 
    			dwhdbUrl, dwhdbUser, dwhdbPass);
    	
    	TransformerCacheImpl dbread = new TransformerCacheImpl();
    	dbread.readDB(repdbUrl, 
    			dwhrepUser, dwhrepPass, driver,"dwhrep", "DC_E_ERBS");
    	
    	DataFormatCacheImpl dataformats = new DataFormatCacheImpl();
    	dataformats.readDB(repdbUrl, 
    			dwhrepUser, dwhrepPass, driver, "DC_E_ERBS");
    	
    	System.out.println("START UP SQUENCE COMPLETE!");
    	registry.getListenerContainer("mdcgroup").resume();
    	System.out.println("CONSUMER RESUMED");
	}
	
}*/
