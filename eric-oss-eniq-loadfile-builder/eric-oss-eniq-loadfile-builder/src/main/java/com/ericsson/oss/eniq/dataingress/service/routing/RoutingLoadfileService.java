/**
 * 
 */
package com.ericsson.oss.eniq.dataingress.service.routing;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.stereotype.Service;

/**
 * @author zmairvn
 *
 */
@Service
public interface RoutingLoadfileService {
	public boolean routingLogic(String topic,ConsumerRecords<String, GenericRecord> consumerRecords);

}
