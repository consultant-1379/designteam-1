package com.ericsson.eniq.flsmock.util;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import com.ericsson.eniq.flsmock.pojo.ParserInput;
import com.ericsson.eniq.flsmock.pojo.ProducerInput;
import com.ericsson.eniq.flsmock.service.Producer;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

@Component
public class FLSUtil {
	private static final Logger log = LogManager.getLogger(FLSUtil.class);
	
	@Autowired
	Producer producer;
	@Autowired
	private KafkaTemplate<String, String> kafkaTemplate;
	@Autowired
	private Gson gson;
	
	public ParserInput getParserInputTemplate(Resource resourceFile) {
		try (InputStream ris = resourceFile.getInputStream();
				JsonReader reader = gson.newJsonReader(new InputStreamReader(ris))) {
			return gson.fromJson(reader, ParserInput.class);
		} catch (Exception e) {
			log.log(Level.ERROR, "Exception while reading json", e);
		}

		return null;
	}

	public void produceBatch(int batchId, ProducerInput producerInput, ParserInput parserInputTemplate,String topicName) {
		boolean repeat = producerInput.getRepeat();
		log.log(Level.INFO, "repeat is set to : " + repeat);
		if (repeat) {
			produceDuplicates(batchId, producerInput, parserInputTemplate,topicName);
		}
		// TODO -read from directory and feed the batch
	}

	public void produceDuplicates(int batchId, ProducerInput producerInput, ParserInput parserInputTemplate,String topicName) {
		log.log(Level.INFO, "producing duplicates for batchId : " + batchId);
		int batchSize = producerInput.getMessagesPerBatch();
		parserInputTemplate.setFilePath(producerInput.getInDir());
		parserInputTemplate.setFileName(producerInput.getRepeatFile());
		//parserInputTemplate.getActionContents().put("outDir",
				//parserInputTemplate.getActionContents().get("outDir") + File.separator + batchId);
		for (int i = 0; i < batchSize; i++) {
			this.produceMessages( topicName, gson.toJson(parserInputTemplate, ParserInput.class));
		}
	}
	
	public void produceMessages(String topic, String message) {
		log.log(Level.INFO, "topic  "+ topic);
		log.log(Level.INFO, "sending messages to kafka topic message  "+ message);
		kafkaTemplate.send(topic, message);
		log.log(Level.INFO, "producing message successfully "+ message);
	}
}
