package com.ericsson.eniq.etl.utils;

public class Field {
	
	private String name;
	private String type;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Field [name=" + name + ", type=" + type + "]";
	}
	
	

}
