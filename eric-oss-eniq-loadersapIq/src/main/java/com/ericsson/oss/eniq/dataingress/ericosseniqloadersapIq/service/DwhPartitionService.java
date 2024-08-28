package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model.Dwhpartition;

@Service
public class DwhPartitionService {
	
	public DwhPartitionService() {
		
	}
	
	final Map<String, List<Dwhpartition>> tableMap = new HashMap<String, List<Dwhpartition>>();
	final Map<String, Dwhpartition> dwhpartitions = new ConcurrentHashMap<>();
	final Map<String, Dwhpartition> dwhpartitionLookUpMap = new ConcurrentHashMap<>();
	final Map<String, Map<String, Dwhpartition>> interfaceDwhpartitionLookUpMap = new ConcurrentHashMap<>();
	
	private static class InstanceHolder {
		static final DwhPartitionService dpc = new DwhPartitionService();
	}
	
	public static DwhPartitionService getData() {
		return InstanceHolder.dpc;
	}
	
	public void addDataFormat(String storageID, String tableName, Timestamp startTime, Timestamp endTime,
			String status, Long partitionSize, Long defaultPartitionSize, Integer loadOrder) {
		Dwhpartition dwhpartition;

		if (dwhpartitions.containsKey(storageID)) {
			dwhpartition = dwhpartitions.get(storageID);
			dwhpartition.setStorageID(storageID);
			dwhpartition.setTableName(tableName);
			dwhpartition.setStartTime(startTime);
			dwhpartition.setEndTime(endTime);
			dwhpartition.setStatus(status);
			dwhpartition.setPartitionSize(partitionSize);
			dwhpartition.setDefaultPartitionSize(defaultPartitionSize);
			dwhpartition.setLoadOrder(loadOrder);
		} else {
			dwhpartition = new Dwhpartition(storageID,tableName, startTime, endTime, status,partitionSize,defaultPartitionSize,loadOrder);
		}

		dwhpartitions.put(storageID, dwhpartition);
		dwhpartitionLookUpMap.put(storageID, dwhpartition);
		Map<String, Dwhpartition> tagIdMap = interfaceDwhpartitionLookUpMap.get(storageID);
		if (tagIdMap == null) {
			tagIdMap = new HashMap<>();
			interfaceDwhpartitionLookUpMap.put(storageID, tagIdMap);
		}
		tagIdMap.put(storageID, dwhpartition);
		
		List<Dwhpartition> entries = tableMap.get(storageID);
        if (entries == null) {
            entries = new ArrayList<Dwhpartition>();
            tableMap.put(storageID, entries);
        }
        entries.add(dwhpartition);
       Collections.sort(entries);
	}

	public Dwhpartition getPartitionBasedOnStorageId(String storageID) {
		return dwhpartitionLookUpMap.get(storageID);
	}
	
	public List<String> getActiveTables(final String storageID) {
		final List<String> ret = new ArrayList<String>();

        final List<Dwhpartition> ptes = tableMap.get(storageID);

        if (ptes != null) {
            for (Dwhpartition pte : ptes) {
                if (pte.getStatus().equalsIgnoreCase("ACTIVE")) {
                    ret.add(pte.getTableName());
                }
            }
        }
        return ret;
    }

}
