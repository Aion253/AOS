package net.aionstudios.api.util;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.aionstudios.api.database.QueryResults;
import net.aionstudios.api.external.DatabaseConnector;

public class DatabaseUtils {
	
	public static List<QueryResults> prepareAndExecute(String preparedStatement, boolean logError, Object... elements) {
		Connection connection = null;
		PreparedStatement statement = null;
		List<QueryResults> resultSet = null;
		try {
			connection = DatabaseConnector.getDatabase();
			statement = connection.prepareStatement(preparedStatement);
			for(int i = 0; i < elements.length; i++) {
				statement.setObject(i+1, elements[i]);
			}
			statement.execute();
			resultSet = new ArrayList<QueryResults>();
			ResultSet rs = statement.getResultSet();
			while(rs!=null) {
				List<Map<String, Object>> resultList = new ArrayList<Map<String, Object>>();
			    Map<String, Object> row = null;

			    ResultSetMetaData metaData = rs.getMetaData();
			    Integer columnCount = metaData.getColumnCount();
			    while (rs.next()) {
			        row = new HashMap<String, Object>();
			        for (int i = 1; i <= columnCount; i++) {
			            row.put(metaData.getColumnName(i), rs.getObject(i));
			        }
			        resultList.add(row);
			    }
			    if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
			    resultSet.add(new QueryResults(resultList, metaData.getTableName(1)));
			    rs=null;
			    if(statement.getMoreResults()) {
			    	rs=statement.getResultSet();
			    }
			}
			if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
			return resultSet;
		} catch (SQLException e) {
			if(logError) {
				System.err.println("Error executing SQL query.");
				e.printStackTrace();
			}
		} finally {
			if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
		}
		if (statement != null) try { statement.close(); } catch (SQLException ignore) {}
		return null;
	}

}
