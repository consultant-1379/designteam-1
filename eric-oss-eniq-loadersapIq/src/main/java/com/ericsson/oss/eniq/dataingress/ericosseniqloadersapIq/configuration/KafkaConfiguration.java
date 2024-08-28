package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.configuration;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties.AckMode;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model.Notification;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

@Configuration
@EnableKafka
public class KafkaConfiguration {
	
	private static final Logger LOG = LogManager.getLogger(KafkaConfiguration.class);
	
	@Value("${spring.kafka.bootstrap-servers}") 
	private String bootStrapServers;
	
	@Value("${spring.kafka.consumer.auto-offset-reset}")
	private String offsetReset;
	
	@Value("${spring.kafka.consumer.group-id}")
	private String groupId;
	
	
	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
	    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, offsetReset);
	    props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		
		LOG.log(Level.INFO, "props initialized = " + props);
		return props;
	}

	@Bean
	public ConsumerFactory<String, String> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	
	}

	
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> batchFactory() {
		ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
		factory.setBatchListener(true);  
		return factory;
	}
	 @Bean
	    public Gson gsonJsonConverter() {
		 Gson gson = new GsonBuilder()
			        .setLenient()
			        .create();
	        return gson;
	    }
	

}
