package com.ericsson.eniq.ebskafkaconsumer.controller;

import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.core.KafkaTemplate;

import com.ericsson.eniq.ebskafkaconsumer.cache.DataFormatCacheImpl;
import com.ericsson.eniq.parser.sink.ISink;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

public class EbsKafkaProducer implements ISink {

	private KafkaTemplate<String, GenericRecord> kafkaTemplate;

	private static final Logger LOG = LogManager.getLogger(EbsKafkaProducer.class);
	
	String topic;

	public EbsKafkaProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate, String topic) {
		this.kafkaTemplate = kafkaTemplate;
		this.topic = topic;
	}

	@Override
	public void pushMessage(String tagId, Map<String, String> data) {
		long convStartTime = System.nanoTime();
		System.out.println("Data :"+data);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String mapToJson = null;
        try {
            mapToJson = objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(mapToJson);
		Schema schema = DataFormatCacheImpl.getSchema(tagId);
		if (schema != null) {
			GenericRecord record = new GenericData.Record(schema);
			String colName;
			String value;
			for (Field field : record.getSchema().getFields()) {
				colName = field.name();
				value = data.get(colName);
				if (value == null) {
					value = "";
				}
				record.put(colName, value);
			}
			long convEndTime = System.nanoTime();
			//LOG.log(Level.INFO, "Time taken for conv in nanos: "+(convEndTime-convStartTime));
			long pushStartTime = System.nanoTime();
			if (tagId != null) {
				kafkaTemplate.send("PM_E_ERBS_DATA", tagId, record);
			} else {
				LOG.log(Level.WARN, "tagId is null for : " + tagId);
			}
			long pushEndTime = System.nanoTime();
			//LOG.log(Level.INFO, "Time taken for push in nanos: "+(pushEndTime-pushStartTime));
			//LOG.log(Level.INFO, "Time taken for conv + push in nanos: "+(pushEndTime-convStartTime));
		} else {
			LOG.log(Level.WARN, "No schema found for foldername : " + tagId);
		}
		
	}

	@Override
	public void pushMessage(String tagId, String record) {
		throw new UnsupportedOperationException();
		
	}

}
