package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model;

import java.sql.Timestamp; // NOPMD : eeipca : may not be used

public class Dwhpartition implements Comparable<Dwhpartition>{

    private String storageID;
    private String tableName;
    private Timestamp startTime;
    private Timestamp endTime;
    private String status;
    private Long partitionSize;
    private Long defaultPartitionSize;
    private Integer loadOrder;
    
    public Dwhpartition() {
    	
    }
    
    public Dwhpartition(String storageID, String tableName, Timestamp startTime, Timestamp endTime, String status,
			Long partitionSize, Long defaultPartitionSize, Integer loadOrder) {
		super();
		this.storageID = storageID;
		this.tableName = tableName;
		this.startTime = startTime;
		this.endTime = endTime;
		this.status = status;
		this.partitionSize = partitionSize;
		this.defaultPartitionSize = defaultPartitionSize;
		this.loadOrder = loadOrder;
	}

	public String getStorageID() {
		return storageID;
	}

	public void setStorageID(String storageID) {
		this.storageID = storageID;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Long getPartitionSize() {
		return partitionSize;
	}

	public void setPartitionSize(Long partitionSize) {
		this.partitionSize = partitionSize;
	}

	public Long getDefaultPartitionSize() {
		return defaultPartitionSize;
	}

	public void setDefaultPartitionSize(Long defaultPartitionSize) {
		this.defaultPartitionSize = defaultPartitionSize;
	}

	public Integer getLoadOrder() {
		return loadOrder;
	}

	public void setLoadOrder(Integer loadOrder) {
		this.loadOrder = loadOrder;
	}

	@Override
	public String toString() {
		return "Dwhpartition [storageID=" + storageID + ", tableName=" + tableName + ", startTime=" + startTime
				+ ", endTime=" + endTime + ", status=" + status + ", partitionSize=" + partitionSize
				+ ", defaultPartitionSize=" + defaultPartitionSize + ", loadOrder=" + loadOrder + "]";
	}

	@Override
	public int compareTo(Dwhpartition dwhp) {

		int comp = dwhp.loadOrder.compareTo(this.loadOrder);
		if(comp ==0) {
			comp = dwhp.tableName.compareTo(this.tableName);
		}
		return comp;
	}

}
