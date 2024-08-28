//package com.ericsson.eniq.loader;
//
//import java.util.HashMap;
//
//import org.springframework.stereotype.Component;
//
//@Component
//public class DatabaseDetails {
//
//	private static HashMap<String, String> dbDetails = new HashMap<String, String>();
//
//	public DatabaseDetails() {
//		init();
//	}
//
//	public HashMap<String, String> getDbDetails() {
//		return dbDetails;
//	}
//
//	public void setDbDetails(HashMap<String, String> dbDetails) {
//		DatabaseDetails.dbDetails = dbDetails;
//	}
//
//	private void init() {
//		dbDetails.put("repdbURL", "jdbc:sybase:Tds:ieatrcxb6510:2641");
//		dbDetails.put("dwhdbURL", "jdbc:sybase:Tds:ieatrcxb6510:2640");
//		dbDetails.put("driver", "com.sybase.jdbc4.jdbc.SybDriver");
//		dbDetails.put("etlrepUser", "etlrep");
//		dbDetails.put("etlrepPass", "etlrep");
//		dbDetails.put("dwhrepUser", "dwhrep");
//		dbDetails.put("dwhrepPass", "dwhrep");
//		dbDetails.put("dwhdbUser", "dc");
//		dbDetails.put("dwhdbPass", "DC@dc1");
//	}
//
//}
