package com.ericsson.oss.eniq.dataingress.ericosseniqloadersapIq.connections;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface LoaderDAO {
	
	ResultSet getResultSetFromDB(Connection con, String query) throws SQLException;

	int getResultSetFromDB1(Connection con, String query) throws SQLException;
}
