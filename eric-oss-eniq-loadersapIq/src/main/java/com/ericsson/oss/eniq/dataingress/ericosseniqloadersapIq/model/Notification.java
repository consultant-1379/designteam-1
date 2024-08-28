package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model;

import java.io.Serializable;

import org.springframework.stereotype.Component;

@Component
public class Notification implements Serializable{
   
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long id;
	String path;
	String tableName;
	long timestamp;
	
	public long getId() {
		return id;
	}
	public void setId(long l) {
		this.id = l;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public long getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return "Notification [id=" + id + ", path=" + path + ", tableName=" + tableName + ", timestamp=" + timestamp
				+ "]";
	}
	
	
	
}
