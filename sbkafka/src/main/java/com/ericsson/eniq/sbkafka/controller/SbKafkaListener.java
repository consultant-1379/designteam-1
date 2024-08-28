package com.ericsson.eniq.sbkafka.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import com.ericsson.eniq.parser.ExecutionManager;
import com.ericsson.eniq.parser.ParseSession;
import com.ericsson.eniq.parser.Parser;
import com.ericsson.eniq.parser.SourceFile;
import com.ericsson.eniq.parser.sink.ISink;
import com.ericsson.eniq.sbkafka.controller.pojo.ParserInput;
import com.google.gson.Gson;


@Service
public class SbKafkaListener {

	private static final Logger LOG = LogManager.getLogger(SbKafkaListener.class);

	private List<Callable<Boolean>> workers = new ArrayList<>();

	@Value("${parser.batch.size}")
	private String batchSize;
	
	@Value("${producer.topic}") 
	private String producerTopic;

	private int consumptionBatchId = 100;

	private String hostName;

	private Map<Integer, ISink> sinks = new HashMap<>();

	@KafkaListener(id = "PM_E_ERBS_FILES_GROUP", topics = "PM_E_ERBS_FILES", containerFactory = "batchFactory")
	void listen(ConsumerRecords<String, String> consumerRecords, Acknowledgment acknowledgment) {
		consumptionBatchId++;
		int count = 0;
		for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			String message = consumerRecord.value();
			LOG.log(Level.INFO, "Record details : partition = " + consumerRecord.partition() + " ,offset = "
					+ consumerRecord.offset());
			LOG.log(Level.INFO, "KafkaHandler[String] {}" + message);
			LOG.log(Level.INFO, "Current number of workers : " + workers + " ,batch size : " + batchSize);
			createWorker(message, ++count);
		}
		ExecutionManager.getInstance().addParserToExecution(workers);
		workers.clear();
		acknowledgment.acknowledge();
	}
	
	@Autowired
	public void createKafkaProducers(ApplicationContext context) {
		int bs = Integer.parseInt(batchSize);
		for (int i=1 ; i <= bs ; i++) {
			sinks.put(i, new SbKafkaProducer((KafkaTemplate<String, GenericRecord>)context.getBean(KafkaTemplate.class), producerTopic));
			//sinks.put(i, new SbKafkaTextProducer((KafkaTemplate<String, String>)context.getBean(KafkaTemplate.class)));
		}
		LOG.log(Level.INFO, "Sinks = " + sinks);
	}
	
	

	private void createWorker(String message, int producerIndex) {
		Gson gson = new Gson();
		ParserInput input = gson.fromJson(message, ParserInput.class);
		File inputFile = new File(input.getInputFile());
		String setName = input.getSetName();
		String setType = input.getSetType();
		String techpack = input.getTp();
		input.getActionContents().put("outDir", input.getActionContents().get("outDir") + File.separator + "_"
				+ getHostName() + "_" + consumptionBatchId);
		Properties conf = new Properties();
		conf.putAll(input.getActionContents());
		ParseSession session = new ParseSession(8888, conf);
		SourceFile sf = new SourceFile(inputFile, conf, session, conf.getProperty("useZip", "gzip"), LOG);
		Parser parser = new Parser(sf, techpack, setType, setName, "mdc_worker", sinks.get(producerIndex));
		workers.add(parser);
	}

	private String getHostName() {
		if (hostName == null) {
			hostName = System.getenv("HOSTNAME");
		}
		return hostName;
	}
}
