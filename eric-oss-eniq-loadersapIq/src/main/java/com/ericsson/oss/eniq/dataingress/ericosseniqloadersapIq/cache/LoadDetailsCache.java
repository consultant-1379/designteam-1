package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.cache;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.DbConnections;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.LoaderDAO;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.LoaderDAOImpl;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.model.Metadata;

@Component
public class LoadDetailsCache {
	// Map to hold metadata details from rep db
	private static Map<String,Metadata> loadCommandMap = new ConcurrentHashMap<String,Metadata>();
	private static Map<String,Object> partitionMap = new ConcurrentHashMap<String,Object>();
	
	public void addLoadCommand(String tableName, Metadata o) {
		loadCommandMap.computeIfAbsent(tableName,k ->  o);
		
	}

	public void addPartionDetails(String tableName, Object o) {
		partitionMap.computeIfAbsent(tableName,k ->  o);
		
	}
	
	public Metadata getLoadCommand(String tableName) {
		return loadCommandMap.get(tableName);
	}
	
	public Object getPartionDetails(String tableName) {
		return partitionMap.get(tableName);
	}
	
	/*public static void main(String[] args) {
		
		LoadDetailsCache.loadCommandMap.put("t1", "t1 existing query");
		cache.addLoadCommand("t1", "t1 query");
		cache.addPartionDetails("t1", "t1 partion");
		
		
		System.out.println("op 1 : "+cache.getLoadCommand("t1"));
		System.out.println("op 2 : "+cache.getPartionDetails("t1"));
	//	LoadDetailsCache cache = new LoadDetailsCache();
		DbConnections cons = new DbConnections();
		LoaderDAO dao = new LoaderDAOImpl();
		try {
		ResultSet rs = 	dao.getResultSetFromDB(cons.getDBConnection(), "select * from Dataformat");
		if (rs.next()) {

			System.out.println("Current DATA from Sybase is : " + rs.getString(1) + " " + rs.getString(2) + " "
					+ rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " "
					);
		}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}*/
}
