package com.ericsson.eniq.asn1kafkaconsumer.controller;

import java.util.List;
import java.util.Map;

import org.apache.avro.Schema;
import org.apache.avro.Schema.Field;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.ericsson.eniq.asn1kafkaconsumer.cache.DataFormatCacheImpl;
import com.ericsson.eniq.parser.sink.ISink;



@Service
public class SbKafkaProducer implements ISink {

	
	private KafkaTemplate<String, GenericRecord> kafkaTemplate;

	private static final Logger LOG = LogManager.getLogger(SbKafkaProducer.class);
	
	String topic;

	@Autowired 
	public SbKafkaProducer(KafkaTemplate<String, GenericRecord> kafkaTemplate, @Value("#{'${producer.topic}'}") String topic) {
		this.kafkaTemplate = kafkaTemplate;
		this.topic = topic;
	}

	@Override
	public void pushMessage(String tagId, Map<String, String> data) {
		long convStartTime = System.nanoTime();
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
			//String pushValue = record.toString();
			long pushStartTime = System.nanoTime();
			if (tagId != null) {
				kafkaTemplate.send("PM_E_BSS_DATA", tagId, record);
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
