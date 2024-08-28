package com.ericsson.oss.eniq.dataingress.service.listners;

import java.lang.reflect.InvocationTargetException;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;
//import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;

import com.ericsson.oss.eniq.dataingress.service.routing.RouteMessageIface;

@Service
public class LoadfileBuilderKafkaController {
	
	private static final Logger LOG = LogManager.getLogger(LoadfileBuilderKafkaController.class);

	@Autowired
	private RouteMessageIface routeMessageIface;
    //@Transactional
    @KafkaListener(id = "PM_E_ERBS_DATA_GROUP", topics = "PM_E_ERBS_DATA", containerFactory = "batchFactory")
     void listen(ConsumerRecords<String, GenericRecord> consumerRecords, Acknowledgment acknowledgment) {
    		try{
    			LOG.info("__________listner method called");
			routeMessageIface.routeToFile(consumerRecords);
    		}
    		catch(Exception e)
    		{
    			LOG.error("error in listner method "+e.toString());
    		}
			acknowledgment.acknowledge();
	}
    
}
