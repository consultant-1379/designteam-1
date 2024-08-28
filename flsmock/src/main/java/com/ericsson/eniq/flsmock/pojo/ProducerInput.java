package com.ericsson.eniq.flsmock.pojo;

import lombok.ToString;

@ToString
public class ProducerInput {
	
	Integer totalBatches;
	Integer messagesPerBatch;
	String inDir;
	Boolean repeat;
	String repeatFile;
	
	
	public Integer getTotalBatches() {
		return totalBatches;
	}
	public void setTotalBatches(Integer totalBatches) {
		this.totalBatches = totalBatches;
	}
	public Integer getMessagesPerBatch() {
		return messagesPerBatch;
	}
	public void setMessagesPerBatch(Integer messagesPerBatch) {
		this.messagesPerBatch = messagesPerBatch;
	}
	public String getInDir() {
		return inDir;
	}
	public void setInDir(String inDir) {
		this.inDir = inDir;
	}
	public Boolean getRepeat() {
		return repeat;
	}
	public void setRepeat(Boolean repeat) {
		this.repeat = repeat;
	}
	public String getRepeatFile() {
		return repeatFile;
	}
	public void setRepeatFile(String repeatFile) {
		this.repeatFile = repeatFile;
	}
	
	

}
