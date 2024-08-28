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
public interface RouteMessageIface {
	public void routeToFile(ConsumerRecords<String, GenericRecord> consumerRecords);

}
