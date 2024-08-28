package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.DatabaseType;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections.DbConnections;
import com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.service.DwhPartitionService;

@Service
public class DwhPartitionSql {
	@Autowired
	DbConnections dbConnection;
	
	public void readDB() {
		
		Connection conn = dbConnection.getDBConnection(DatabaseType.DWHREP);
		DwhPartitionService partitionData = DwhPartitionService.getData();
		try {
			loadDataFormats(conn,partitionData);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public void loadDataFormats(Connection con, DwhPartitionService prtData) throws Exception {
		String sql ="SELECT dwhp.storageid, dwhp.tablename, dwhp.starttime, dwhp.endtime, dwhp.status, dwht.partitionsize, plan.defaultpartitionsize, dwhp.loadorder "
	            + "FROM DWHPartition dwhp, DWHType dwht, TPActivation tpa, TypeActivation ta, PartitionPlan plan "
	            + "WHERE dwht.storageid = dwhp.storageid AND dwht.techpack_name = tpa.techpack_name AND dwht.techpack_name = ta.techpack_name AND "
	            + "dwht.typename = ta.typename AND dwht.tablelevel = ta.tablelevel AND dwht.partitionplan = plan.partitionplan AND "
	            + "tpa.status = 'ACTIVE' AND ta.status='ACTIVE'";
		
		try(PreparedStatement ps = con.prepareStatement(sql);
		ResultSet rs = ps.executeQuery();) {
			while (rs.next()) {
				prtData.addDataFormat(rs.getString(1), rs.getString(2), rs.getTimestamp(3), rs.getTimestamp(4), rs.getString(5), rs.getLong(6), rs.getLong(7), rs.getInt(8));	
			}
		}
	}

}
