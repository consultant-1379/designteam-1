package com.ericsson.eniq.flsmock.pojo;

public final class ParserInput implements Cloneable {

	private String dataFlowName;
	private String fdn;
	private String sourceId;
	private String bucketName;
	private String dataType;
	private String nodeType;
	private String filePath;
	private String fileName;

	public String getDataFlowName() {
		return dataFlowName;
	}

	public void setDataFlowName(String dataFlowName) {
		this.dataFlowName = dataFlowName;
	}

	public String getFdn() {
		return fdn;
	}

	public void setFdn(String fdn) {
		this.fdn = fdn;
	}

	public String getSourceId() {
		return sourceId;
	}

	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	public String getNodeType() {
		return nodeType;
	}

	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	public ParserInput clone() {

		ParserInput parserInput = null;
		try {
			parserInput = (ParserInput) super.clone();
		} catch (CloneNotSupportedException e) {
			// ignore this exception
		}
		return parserInput;
	}

}
