package com.ericsson.oss.eniq.dataingress.service.producer;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ericsson.oss.eniq.dataingress.service.model.Notification;
import com.google.gson.Gson;

@Service
public class ProducerService {

	private static final Logger LOG = LogManager.getLogger(ProducerService.class);
	
	/*@Value("${spring.kafka.producer.topic}") 
	private String topic;*/
	@Autowired
    private Gson gson;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

	public void sendMessage(Notification notification) {
		LOG.log(Level.INFO, "#### -> Producing message -> " + notification);
        this.kafkaTemplate.send("loadfileTopic", gson.toJson(notification));
		
		/*ListenableFuture<SendResult<String, String>> future = this.kafkaTemplate.send(TOPIC, message);
		future.addCallback(new ListenableFutureCallback<>() {
		@Override
		public void onFailure(Throwable ex) {
		logger.info("Unable to send message=[ {} ] due to : {}", message, ex.getMessage());
		}
			@Override
			public void onSuccess(SendResult<String, String> result) {
			logger.info("Sent message=[ {} ] with offset=[ {} ]", message, result.getRecordMetadata().offset());
			}
		});*/
	}
}
