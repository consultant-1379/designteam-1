package com.ericsson.oss.eniq.dataingress.service.routing;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ericsson.oss.eniq.dataingress.service.cache.TableNameCache;
import com.ericsson.oss.eniq.dataingress.service.outputstream.Data;
import com.ericsson.oss.eniq.dataingress.service.outputstream.ILoadFile;
import com.ericsson.oss.eniq.dataingress.service.outputstream.LoadFileRepo;
import com.ericsson.oss.eniq.dataingress.service.outputstream.Writer;

//**
// * @author zmairvn

// */
@Service
public class RouteMessageToFile implements RouteMessageIface {
	private static final Logger LOG = LogManager.getLogger(RouteMessageToFile.class);
	private static final Map<Integer, Writer> writers = new HashMap<>();

	@Value("${out.dir}")
	private String outDir;

	@Value("${batch.size}")
	private String batchSize;

	@Value("${num.of.writers}")
	private int numOfWriters;

	@Value("${threshold.number}")
	private int thresholdNumber;

	private Writer getWriteThread(String folderName) {
		int index = Math.abs(folderName.hashCode() % numOfWriters);
		return writers.get(index);
	}

	@Override
	public void routeToFile(ConsumerRecords<String, GenericRecord> consumerRecords) {
		
		if (writers.isEmpty()) {
			ExecutorService execService = Executors.newFixedThreadPool(numOfWriters);
			Writer writer;
			LOG.log(Level.INFO, "number of writers to be created = " + numOfWriters);
			LOG.log(Level.INFO, "outDir = " + outDir);
			LOG.log(Level.INFO, "batchSize = " + batchSize);
			for (int i = 0; i < numOfWriters; i++) {
				writer = new Writer();
				writers.put(i, writer);
				execService.execute(writer);
				LOG.log(Level.INFO, "Writer created, index = " + i);
			}
		}

		for (ConsumerRecord<String, GenericRecord> consumerRecord : consumerRecords) {
			String key = consumerRecord.key();
			GenericRecord message = consumerRecord.value();
    
			LOG.log(Level.INFO, "KafkaMOConsumer {} : key = " + key + " ### message = " + message);
			ILoadFile loadFile = LoadFileRepo.getLoadFile(outDir + File.separator + TableNameCache.getTpName(key),
					TableNameCache.getFolderName(key), thresholdNumber);
			Data data = new Data(message, loadFile);
			getWriteThread(key).add(data);

		}

	}

}
