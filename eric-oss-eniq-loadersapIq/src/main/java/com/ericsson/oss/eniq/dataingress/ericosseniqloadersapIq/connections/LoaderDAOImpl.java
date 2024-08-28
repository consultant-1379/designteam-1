package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.stereotype.Service;

@Service
public class LoaderDAOImpl implements LoaderDAO{

	@Override
	public ResultSet getResultSetFromDB(Connection con, String query) throws SQLException {
		// TODO Auto-generated method stub

		if (con != null) {
			System.out.println("Connection is sucessfully");
			Statement statement = con.createStatement();
			return statement.executeQuery(query);

		}
		return null;



	}

	@Override
	public int getResultSetFromDB1(Connection con, String query) throws SQLException {
		Statement statement;
		int data1;
		if (con != null) {
			System.out.println("Connection is sucessfully");
			
			
				statement = con.createStatement();
			

			
			try {
				data1= statement.executeUpdate(query);
				return data1;
				
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return 0;
	}

}
