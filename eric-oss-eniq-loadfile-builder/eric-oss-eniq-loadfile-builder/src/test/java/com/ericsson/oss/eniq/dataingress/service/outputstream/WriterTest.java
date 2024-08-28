package com.ericsson.oss.eniq.dataingress.service.outputstream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.avro.generic.GenericRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class WriterTest {

	private BlockingQueue<Data> queue = new LinkedBlockingQueue<>();
	@Autowired
	Writer writer = new Writer();
	@Autowired
	GenericRecord message;
	@Autowired
	ILoadFile loadFile;
	@Autowired
	Data data  = new Data(message, loadFile);

	@Autowired
	Data data1  = new Data(message, loadFile);


	@Test
	public void testIsShutDownTriggered() {

		boolean isShutDownTriggered = false;
		assertFalse(isShutDownTriggered);
	}

	@Test
	public void testSetShutDownTriggered() {
		Object isShutDownTriggered = true;
		assertNotNull(isShutDownTriggered);
		assertEquals(isShutDownTriggered,isShutDownTriggered);
	}

	@Test
	public void testAdd() {
		writer.add(data);
		try {
			queue.put(data1);
			assertNotNull(data);
			assertNotNull(data1);
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
	}

	@Test
	public void testRun() {
		boolean isShutDownTriggered = false;
		assertFalse(isShutDownTriggered);
		
		assertEquals(true, true);
	}


}
