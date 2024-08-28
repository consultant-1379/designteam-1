package com.ericsson.oss.eniq.dataingress.service.outputstream;

import org.apache.avro.generic.GenericRecord;

public class Data {
	GenericRecord message;
	//String message;
	ILoadFile loadFile;
	
	public Data(GenericRecord message, ILoadFile loadFile) {
		this.message = message;
		this.loadFile = loadFile;
	}

	public GenericRecord getMessage() {
		return message;
	}

	public ILoadFile getLoadFile() {
		return loadFile;
	}
}
