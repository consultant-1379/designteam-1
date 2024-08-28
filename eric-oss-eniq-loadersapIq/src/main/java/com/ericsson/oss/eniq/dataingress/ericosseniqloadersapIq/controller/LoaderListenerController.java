package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.controller;

import java.io.File;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model.Metadata;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model.Notification;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.service.DwhPartitionService;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.service.MetaDataService;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.sql.DwhPartitionSql;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.sql.Loader;
import com.google.gson.Gson;

@RestController
public class LoaderListenerController {
	
	private static final Logger LOG = LogManager.getLogger(LoaderListenerController.class);

    @Autowired
	MetaDataService metaDataService;
    
    @Autowired
    DwhPartitionSql dwhPartitionSql;
    
    @Autowired
    private Gson gson;
    
    @Autowired
    private Loader loader;
    
   
    private static DwhPartitionService dwhp;
	
    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${spring.kafka.consumer.topic}", containerFactory = "batchFactory")
    public void listen(@RequestBody String  message,Acknowledgment acknowledgment) {
	  
    	try {
    		if(message.split("},").length>0) {
    			String arr[]=message.split("},");
    			message = arr[0]+"}";
    			}
    		Notification notification=gson.fromJson(message, Notification.class);
    		LOG.info("Message Consumed ----"+notification.toString()+"------");
    		Metadata metadata = metaDataService.fetchMetadata(notification.getTableName());
        	dwhPartitionSql.readDB();
            if(dwhp ==null) {
                dwhp = DwhPartitionService.getData();
            }
            String tableraw=metadata.getTRANSFER_ACTION_NAME();
            tableraw = tableraw.replaceAll(Pattern.quote("Loader_"), "");
            List<String> activetable=dwhp.getActiveTables(tableraw+":RAW");

            String active1= activetable.get(0);
            String loadtemplate=metadata.getACTION_CONTENTS_01();
	        loader.loadTable(notification.getPath(),loadtemplate,active1);
    	}
    	catch (Exception e) {
    		LOG.error("error occur" + e.toString());
		}

	    acknowledgment.acknowledge();
	}

    
}
