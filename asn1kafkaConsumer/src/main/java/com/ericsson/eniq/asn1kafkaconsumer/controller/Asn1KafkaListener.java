package com.ericsson.eniq.asn1kafkaconsumer.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import com.ericsson.eniq.asn1kafkaconsumer.controller.pojo.ParserInput;
import com.ericsson.eniq.parser.ExecutionManager;
import com.ericsson.eniq.parser.ParseSession;
import com.ericsson.eniq.parser.SourceFile;
import com.ericsson.eniq.parser.ASN1Parser.ASN1Parser;
import com.google.gson.Gson;

@Component
public class Asn1KafkaListener {

	private static final Logger logger = LogManager.getLogger(Asn1KafkaListener.class);

	private List<Callable<Boolean>> workers = new ArrayList<>();

	@Value("${parser.batch.size}")
	private String batchSize;
	
	private int consumptionBatchId = 100;
	
	private String hostName;

	@KafkaListener(id = "#{'${spring.kafka.consumer.group-id}'}", topics = "#{'${spring.kafka.consumer.topic}'}", containerFactory = "batchFactory")
	void listen(ConsumerRecords<String, String> consumerRecords, Acknowledgment acknowledgment) {
		consumptionBatchId++;
		for (ConsumerRecord<String, String> consumerRecord : consumerRecords) {
			String message = consumerRecord.value();
			logger.log(Level.INFO, "Record details : partitiion = " + consumerRecord.partition() + " ,offset = "
					+ consumerRecord.offset());
			logger.log(Level.INFO, "KafkaHandler[String] {}" + message);
			logger.log(Level.INFO, "Current number of workers : " + workers + " ,batch size : " + batchSize);
			//List<Callable<Boolean>> tempWorkers = new ArrayList<>();
			//if (workers.size() < Integer.valueOf(batchSize)) {
				//logger.log(Level.INFO, "Adding to list");
				createWorker(message);
			//} else {
				//logger.log(Level.INFO, "triggering the parsers");
				//tempWorkers.addAll(workers);
				//ExecutionManager.getInstance().addParserToExecution(tempWorkers);
				//acknowledgment.acknowledge();
				//workers.clear();
			//}
		}
		ExecutionManager.getInstance().addParserToExecution(workers);
		workers.clear();
		acknowledgment.acknowledge();
	}

	private void createWorker(String message) {
		Gson gson = new Gson();
		ParserInput input = gson.fromJson(message, ParserInput.class);
		File inputFile = new File(input.getInputFile());
		String setName = input.getSetName();
		String setType = input.getSetType();
		String techpack = input.getTp();
		input.getActionContents().put("outDir",input.getActionContents().get("outDir")+File.separator+"_"+getHostName() + "_"+consumptionBatchId);
		Properties conf = new Properties();
		conf.putAll(input.getActionContents());
		ParseSession session = new ParseSession(8888, conf);
		SourceFile sf = new SourceFile(inputFile, conf, session, conf.getProperty("useZip", "gzip"), logger);
		ASN1Parser parser = new ASN1Parser(sf, techpack, setType, setName, "eniqasn1_worker");
		workers.add(parser);
	}
	
	private String getHostName() {
		if (hostName == null) {
			hostName = System.getenv("HOSTNAME");
		}
		return hostName;
	}
}
