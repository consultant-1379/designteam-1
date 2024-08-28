package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.service;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.cache.LoadDetailsCache;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.DatabaseType;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.DbConnections;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.LoaderDAO;
//import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.LoaderDAOImpl;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model.Metadata;

@Service
public class MetaDataService {
	
	@Autowired
	DbConnections dbConnection;
	
	@Autowired
	LoaderDAO loaderDAO;
	
	String foldername;
	
	Map<String,Metadata> cacheMap;
	
	@Autowired
	LoadDetailsCache loadDetailsCache;
	
	public MetaDataService(){
		
	}
	
	public Metadata fetchMetadata(String foldername) {
		
		Metadata metadata;
		metadata = loadDetailsCache.getLoadCommand(foldername);
		
		if(metadata==null) {
			metadata = fetchDataFromDB(foldername);
			//metadata = fetchDataFromRestService(foldername);
		}
		
		return metadata;
		
	}

	private Metadata fetchDataFromCache(String foldername) {
		// TODO 
		return null;
	}

	private Metadata fetchDataFromDB(String foldername) {
		
		final String QueryMetaCollection = "select * from META_COLLECTIONS" + " where COLLECTION_NAME LIKE '%" + foldername + "%' and SETTYPE LIKE 'Loader'";
		final Long COLLECTION_ID;
		final String VERSION_NUMBER;
		final Long COLLECTION_SET_ID;
		
		ResultSet metaCollectionRS, metaTransferActionsRS;
		Metadata metadata = null;
		try {
			Connection conn = dbConnection.getDBConnection(DatabaseType.ETLREP);
			metaCollectionRS = loaderDAO.getResultSetFromDB(conn, QueryMetaCollection);
			
			if (metaCollectionRS.next()) {
				COLLECTION_ID = metaCollectionRS.getLong(1);
				VERSION_NUMBER = metaCollectionRS.getString(13);
				COLLECTION_SET_ID = metaCollectionRS.getLong(14);
				
				System.out.println("Current DATA from Sybase is : " + COLLECTION_ID + " " + VERSION_NUMBER + " "
						+ COLLECTION_SET_ID);
			
			
				final String QueryMetaTransferActions = "select * from META_TRANSFER_ACTIONS" + " where COLLECTION_ID = " + COLLECTION_ID + " and VERSION_NUMBER LIKE '" + VERSION_NUMBER + "' and COLLECTION_SET_ID = " + COLLECTION_SET_ID + " and ACTION_TYPE LIKE 'Loader'";
				metaTransferActionsRS = loaderDAO.getResultSetFromDB(conn, QueryMetaTransferActions);
				
				if (metaTransferActionsRS.next()) {
					metadata = new Metadata();
					
					metadata.setVERSION_NUMBER(metaTransferActionsRS.getString(1));
					metadata.setTRANSFER_ACTION_ID(metaTransferActionsRS.getLong(2));
					metadata.setCOLLECTION_ID(metaTransferActionsRS.getLong(3));
					metadata.setCOLLECTION_SET_ID(metaTransferActionsRS.getLong(4));
					metadata.setACTION_TYPE(metaTransferActionsRS.getString(5));
					metadata.setTRANSFER_ACTION_NAME(metaTransferActionsRS.getString(6));
					metadata.setORDER_BY_NO(metaTransferActionsRS.getLong(7));
					metadata.setDESCRIPTION(metaTransferActionsRS.getString(8));
					metadata.setWHERE_CLAUSE_01(metaTransferActionsRS.getString(9));
					metadata.setACTION_CONTENTS_01(metaTransferActionsRS.getString(10));
					metadata.setENABLED_FLAG(metaTransferActionsRS.getString(11));
					metadata.setCONNECTION_ID(metaTransferActionsRS.getLong(12));
					metadata.setWHERE_CLAUSE_02(metaTransferActionsRS.getString(13));
					metadata.setWHERE_CLAUSE_03(metaTransferActionsRS.getString(14));
					metadata.setACTION_CONTENTS_02(metaTransferActionsRS.getString(15));
					metadata.setACTION_CONTENTS_03(metaTransferActionsRS.getString(16));
					
					/*
					 * TODO
					 * Add data to cache
					 * */
					
					loadDetailsCache.addLoadCommand(foldername, metadata);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}		
		
		return metadata;
	}
	
	private Metadata fetchDataFromRestService(String foldername) {
		return null;
	}

}
