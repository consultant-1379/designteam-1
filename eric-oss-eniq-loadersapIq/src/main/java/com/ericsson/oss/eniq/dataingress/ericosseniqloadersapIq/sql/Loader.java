package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.sql;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.DatabaseType;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.DbConnections;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.LoaderDAO;

@Component
public class Loader {

	@Autowired
	private DbConnections dbconnection;
	
	@Autowired
	private LoaderDAO dao;
	
	private static final Logger LOG = LogManager.getLogger(Loader.class);
	
	public void loadTable(String filepath, String loadtemplate, String tablename)
	{
		
		
	 Connection conn=dbconnection.getDBConnection(DatabaseType.DWHDB);
	 
	
	 String loadParam= loadParameter();

		
		
		
//		String sql=loadtemplate;
//		sql = sql.replaceAll(Pattern.quote("$TABLE"), tablename);
//		sql = sql.replaceAll(Pattern.quote("$FILENAMES"), filepath);
//		sql = sql.replaceAll(Pattern.quote("$LOADERPARAMETERS"), loadParam);
	 String sql="LOAD TABLE $TABLE  (\r\n" + 
				"SN   , Subrack   , NESW   , BbProcessingResource   , ERBS   , Equipment   , DCVECTOR_INDEX   , AuxPlugInUnit   , PlugInUnit   , OSS_ID   , DeviceGroup   , Slot   , MOID   , DATE_ID   , YEAR_ID   , MONTH_ID   , DAY_ID   , HOUR_ID   , DATETIME_ID   , MIN_ID   , TIMELEVEL   , SESSION_ID   , BATCH_ID   , PERIOD_DURATION   , ROWSTATUS   , DC_RELEASE   , DC_SOURCE   , DC_TIMEZONE   , DC_SUSPECTFLAG   , UTC_DATETIME_ID   , pmBbmDlBbCapacityUtilization   , pmBbmDlPrbUtilization   , pmLicDlPrbCapDistr   , pmLicDlPrbUsedDistr   , pmBbmUlPrbUtilization   , pmBbmDlSeUtilization   , pmLicUlPrbUsedDistr   , pmLicDlCapDistr   , pmBbmUlBbCapacityUtilization   , pmBbmUlSeUtilization   , pmLicUlPrbCapDistr   , pmZtemporary54   , pmZtemporary53   , pmZtemporary151   , pmLicUlCapDistr   , pmZtemporary152   , pmBbmDlPrbPttReservation   , pmBbmDlBbCapacityPttReservation   , pmBbmDlSePttReservation   )\r\n" + 
				"FROM $FILENAMES \r\n" + 
				loadParam;
		sql = sql.replaceAll(Pattern.quote("$TABLE"), "DC_E_ERBS_BBPROCESSINGRESOURCE_V_RAW_02");
		sql = sql.replaceAll(Pattern.quote("$FILENAMES"), "'/eniq/database/dwh_main/DC_E_ERBS_BBPROCESSINGRESOURCE_V_loadfilebuilder-c56977768-kfm9k_1616669639030.txt'");
		sql = sql.replaceAll(Pattern.quote("$LOADERPARAMETERS"), loadParam);
		
		
		
		
		

		
		
		
		try {
			
			int row=dao.getResultSetFromDB1(conn, sql);
			if(row>=1)
			{
				
				LOG.info("data is inserted");
			}
			else
			{
				LOG.info("data is not inserted");
				
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}

	public static String loadParameter()
	{
		return "NOTIFY 100000 ESCAPES OFF QUOTES OFF DELIMITED BY '	'  IGNORE CONSTRAINT UNIQUE 2000000 IGNORE CONSTRAINT NULL 2000000 IGNORE CONSTRAINT DATA VALUE 2000000 WITH CHECKPOINT OFF";

	}
}
