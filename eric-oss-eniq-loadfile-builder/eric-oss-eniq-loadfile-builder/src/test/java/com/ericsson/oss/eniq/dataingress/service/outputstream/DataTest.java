package com.ericsson.oss.eniq.dataingress.service.outputstream;

import static org.junit.Assert.assertEquals;

import org.apache.avro.generic.GenericRecord;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DataTest {
	@Autowired
	GenericRecord message, message1 = null;

	@Autowired
	ILoadFile loadFile,loadFile1;

	@Autowired
	Data data = new Data(message, loadFile);

	Object one;

	@Test
	public void testData() {

		assertEquals(data, data);

	}

	@Test
	public void testGetMessage() {

		message = data.getMessage();

		assertEquals(message, message1);

	}

	@Test
	public void testGetLoadFile() {


		assertEquals(loadFile, loadFile1);
	}

}
