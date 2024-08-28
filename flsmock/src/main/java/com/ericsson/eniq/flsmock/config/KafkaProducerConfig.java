package com.ericsson.eniq.flsmock.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.RoundRobinPartitioner;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;


@Configuration
public class KafkaProducerConfig {
	private static final Logger log = LogManager.getLogger(KafkaProducerConfig.class);
	@Autowired 
	private SpringProperties springProperties;
	
	@Autowired
	Environment env;
	
	@Bean
	public ProducerFactory<String, String> producerFactory() {
		Map<String, Object> configProps = new HashMap<>();
		configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, springProperties.getKafka().getBootstrapServers());
		configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
		configProps.put(ProducerConfig.PARTITIONER_CLASS_CONFIG, RoundRobinPartitioner.class);
		configProps.putAll(tlsSecurityCOnfigs());
		log.log(Level.INFO, "Config props : "+configProps);
		return new DefaultKafkaProducerFactory<>(configProps);
	}
	
	public Map<String, Object> tlsSecurityCOnfigs() {
		Map<String, Object> props = new HashMap<>();
        // TLS Properties
        props.put("security.protocol", "SSL");
        props.put("ssl.truststore.location", env.getProperty("truststore_jks"));
        props.put("ssl.truststore.password", env.getProperty("password"));
        props.put("ssl.key.password", env.getProperty("password"));
        props.put("ssl.keystore.password", env.getProperty("password"));
        props.put("ssl.keystore.location", env.getProperty("keystore_jks"));
        return props;
	}

	@Bean
	public KafkaTemplate<String, String> kafkaTemplate() {
		return new KafkaTemplate<>(producerFactory());
	}

}
