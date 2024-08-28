package com.ericsson.oss.eniq.dataingress.service.config;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.IsolationLevel;
import org.apache.kafka.common.serialization.StringDeserializer;
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
import org.springframework.kafka.transaction.KafkaTransactionManager;

import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.subject.TopicRecordNameStrategy;

@Configuration
@EnableKafka
public class KafkaConfiguration {

	@Value("${schema.registry.url}") 
	private String schemaRegistryUrl;
	
	@Value("${spring.kafka.bootstrap-servers}") 
	private String bootStrapServers;
	
	@Value("${spring.kafka.consumer.max-poll-records}")
	private int maxPollRecords;
	
//	/// adding new code///
//		@Value("${spring.kafka.consumer.topics}'.split(',')}")
//		private String[] topics;
//		
//	
//		@Value("${spring.kafka.consumer.group-id}'.split(',')}")
//		private String[] groupIds;


	@Bean
	public Map<String, Object> consumerConfigs() {
		Map<String, Object> props = new HashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootStrapServers);
		//props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "PM_E_ERBS_DATA_GROUP");
		
		//props.put(ConsumerConfig.GROUP_ID_CONFIG, "groupIds" + UUID.randomUUID().toString());// new code
		//props.put(ConsumerConfig.ALLOW_AUTO_CREATE_TOPICS_CONFIG, "topics");// new code
		
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		//props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
		props.put(KafkaAvroDeserializerConfig.SCHEMA_REGISTRY_URL_CONFIG, schemaRegistryUrl);
		props.put(KafkaAvroDeserializerConfig.VALUE_SUBJECT_NAME_STRATEGY,TopicRecordNameStrategy.class.getName());
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false);
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG,maxPollRecords);
		
		props.put(ConsumerConfig.ISOLATION_LEVEL_CONFIG, IsolationLevel.READ_COMMITTED.toString().toLowerCase());
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		System.out.println("props initialized = "+props);
		return props;
	}

	@Bean
	public ConsumerFactory<String, GenericRecord> consumerFactory() {
		return new DefaultKafkaConsumerFactory<>(consumerConfigs());
	}

	/*@Bean
	public KafkaTransactionManager<String, GenericRecord> kafkaTransactionManager() {
	  return new KafkaTransactionManager<String, GenericRecord>(consumerFactory());
	}*/
	
	@Bean
	public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, GenericRecord>> batchFactory() {
		ConcurrentKafkaListenerContainerFactory<String, GenericRecord> factory = new ConcurrentKafkaListenerContainerFactory<>();
		factory.setConsumerFactory(consumerFactory());
		factory.getContainerProperties().setAckMode(AckMode.MANUAL_IMMEDIATE);
		factory.setBatchListener(true);
		
		factory.getContainerProperties().setSyncCommits(true);   
		factory.getContainerProperties().setDeliveryAttemptHeader(true);
		       // factory.getContainerProperties().setTransactionManager(kafkaTransactionManager());
	    factory.setStatefulRetry(true);                                                                     // → Enable stateful Retry
        factory.setConcurrency(1);  
			   // factory.setRetryTemplate(retryTemplate());    
			  //  factory.setErrorHandler(seekToCurrentErrorHandler(deadLetterRecoverer()));  
		return factory;
	}
	
	

}
