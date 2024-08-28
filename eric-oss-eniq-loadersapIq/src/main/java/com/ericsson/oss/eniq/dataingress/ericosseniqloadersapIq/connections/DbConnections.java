package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class DbConnections implements ApplicationRunner{

	
	
	private Connection etlCon;
	private Connection dwhCon;
	private Connection dwhdbcon;
	
	@Override
	public void run(ApplicationArguments args) throws Exception {
		initDWHRepConnection();
		initETLRepConnection();
		initDWHbdConnection();
	}
	public DbConnections(){
		/*initDWHRepConnection();
		initETLRepConnection();*/
	}
	private void initDWHbdConnection() {
		String drivername="com.sybase.jdbc4.jdbc.SybDriver";
		String uid = "dc";
		String pwd = "Dc12#";
		String url = "jdbc:sybase:Tds:10.36.255.109:2640";
		try {
			DriverManager.registerDriver((Driver) Class.forName(drivername).newInstance());
			dwhdbcon = DriverManager.getConnection(url, uid, pwd);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}
	}
	private void initDWHRepConnection() {
		String drivername="com.sybase.jdbc4.jdbc.SybDriver";
		String uid = "dwhrep";
		String pwd = "dwhrep";
		String url = "jdbc:sybase:Tds:10.36.255.109:2641";
		try {
			DriverManager.registerDriver((Driver) Class.forName(drivername).newInstance());
			dwhCon = DriverManager.getConnection(url, uid, pwd);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
			e.printStackTrace();
		}/*finally {
			if (dwhCon != null) {
				try {
					dwhCon.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/
	}
	
	private void initETLRepConnection() {
		String drivername="com.sybase.jdbc4.jdbc.SybDriver";
        String uid = "etlrep";
        String pwd = "etlrep";
        String url = "jdbc:sybase:Tds:ieatrcxb6510:2641";
        try {
            DriverManager.registerDriver((Driver) Class.forName(drivername).newInstance());
            etlCon = DriverManager.getConnection(url, uid, pwd);
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException | SQLException e) {
            
            e.printStackTrace();
        }/*finally {
			if (etlCon != null) {
				try {
					etlCon.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}*/

	}

	public Connection getDBConnection(DatabaseType dbType) {
		if(dbType.equals(DatabaseType.DWHREP))
			return dwhCon;
		else if(dbType.equals(DatabaseType.ETLREP))
			return etlCon;
		else if(dbType.equals(DatabaseType.DWHDB))
			return dwhdbcon;
		return null;
	}
	
	/*// String uid = "dwhrep";
		// String pwd = "dwhrep";

		// String uid = "dc";
		// String pwd = "Dc12#";
		// String url = "jdbc:sybase:Tds:ieatrcxb6510:2641";
		// String url="jdbc:sybase:Tds:10.36.255.109:2640"; //VAPP DWHDB
		 // VAPP REPDB
		ResultSet rs = null;
		Statement statement = null;


		 // ERLREP
																				// Query

			// rs = statement.executeQuery("select * from Dataformat ");
			// //DWHREP Query

			// rs = statement.executeQuery("select * from
			// DC_E_ERBS_SECTORCARRIER_DAYBH_CALC");//DWHDB Query
		}

		if (rs.next()) {

			System.out.println("Current DATA from Sybase is : " + rs.getString(1) + " " + rs.getString(2) + " "
					+ rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5) + " " + rs.getString(6) + " "
					+ rs.getString(7) + " " + rs.getString(8) + " " + rs.getString(9) + " " + rs.getString(10) + " "
					+ rs.getString(11) + " " + rs.getString(12) + " " + rs.getString(13) + " " + rs.getString(14) + " "
					+ rs.getString(15) + " " + rs.getString(16));
		}
		// DWHREP Execution
		// if (rs.next()) {
		//
		// System.out.println("Current DATA from Sybase is : " + rs.getString(1)
		// + " " + rs.getString(2) + " "
		// + rs.getString(3) + " " + rs.getString(4) + " " + rs.getString(5) + "
		// " + rs.getString(6));
		// }


	}*/
}
