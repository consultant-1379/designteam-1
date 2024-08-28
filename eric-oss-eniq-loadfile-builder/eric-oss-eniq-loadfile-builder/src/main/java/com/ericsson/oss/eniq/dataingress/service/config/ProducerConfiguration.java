package com.ericsson.oss.eniq.dataingress.service.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

@Configuration
@EnableKafka
public class ProducerConfiguration {
	@Value("${spring.kafka.bootstrap-servers}") 
	private String bootStrapServers;
	
	@Bean
    public ProducerFactory<String, String> 
    producerFactory() 
    {  
        Map<String, Object> config = new HashMap<>(); 
  
        config.put( ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers); 
        config.put( ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class); 
        config.put( ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class); 
  
        return new DefaultKafkaProducerFactory<>(config); 
    } 
  
    @Bean
    public KafkaTemplate<String, String> 
    kafkaTemplate() 
    { 
        return new KafkaTemplate<>(producerFactory()); 
    } 
}
