package com.ericsson.oss.eniq.dataingress.service.routing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import com.ericsson.oss.eniq.dataingress.service.cache.TableNameCache;
import com.ericsson.oss.eniq.dataingress.service.outputstream.Data;
import com.ericsson.oss.eniq.dataingress.service.outputstream.ILoadFile;
import com.ericsson.oss.eniq.dataingress.service.outputstream.LoadFileRepo;
import com.ericsson.oss.eniq.dataingress.service.outputstream.Writer;

//**
// * @author zmairvn

// */
public class RoutingLoadfileServiceImpl implements RoutingLoadfileService {
	private static final Logger LOG = LogManager.getLogger(RoutingLoadfileServiceImpl.class);
	private static final Map<Integer, Writer> writers = new HashMap<>();

	@Value("${out.dir}")
	private String outDir;

	@Value("${batch.size}")
	private String batchSize;

	@Value("${num.of.writers}")
	private int numOfWriters;

	public boolean routingLogic(String topic, ConsumerRecords<String, GenericRecord> consumerRecords) {

		boolean rtnStatus=false;
		if(topic!=null && consumerRecords!=null ){
			switch (topic) {
			case "PM_E_BSS_DATA":
				displayConsumerRecord(consumerRecords);
			case "PM_E_EBS_DATA":
				displayConsumerRecord(consumerRecords);
			case "CT_DATA":
				displayConsumerRecord(consumerRecords);
			case "PM_E_ERBS_DATA":
				displayConsumerRecord(consumerRecords);
				rtnStatus=true;
			}}else{
				rtnStatus=false;
			}
		return rtnStatus;

	}

	private Writer getWriteThread(String folderName) {
		int index = Math.abs(folderName.hashCode() % numOfWriters);
		return writers.get(index);
	}

	public ConsumerRecords<String, GenericRecord> displayConsumerRecord(
			ConsumerRecords<String, GenericRecord> consumerRecords) {

		for (ConsumerRecord<String, GenericRecord> consumerRecord : consumerRecords) {
			String key = consumerRecord.key();
			GenericRecord message = consumerRecord.value();

			LOG.log(Level.INFO, "KafkaMOConsumer {} : key = " + key + " ### message = " + message);
			ILoadFile loadFile = LoadFileRepo.getLoadFile(outDir + File.separator + TableNameCache.getTpName(key),
					TableNameCache.getFolderName(key));
			Data data = new Data(message, loadFile);
			getWriteThread(key).add(data);

		}
		return consumerRecords;

	}

}
