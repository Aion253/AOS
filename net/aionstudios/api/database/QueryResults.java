package net.aionstudios.api.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryResults {
	
	private List<Map<String, Object>> results = new ArrayList<Map<String, Object>>();
	private String table;
	
	public QueryResults(List<Map<String, Object>> resultset, String tableName) {
		results = resultset;
		table = tableName;
	}
	
	public List<Map<String, Object>> getResults() {
		return results;
	}

	public String getTableName() {
		return table;
	}

	public int getRowCount() {
		return results.size();
	}
	
	public int getColumnsPerRow() {
		if(results.size()>0) {
			return results.get(0).size();
		}
		return 0;
	}
	
	public int getTotalResults() {
		return getRowCount() * getColumnsPerRow();
	}
	
	public String getColumnName(int columnNumber) {
		if(results.size()>0&&results.get(0).size()>columnNumber-1) {
			return (String) results.get(0).keySet().toArray()[columnNumber];
		}
		return null;
	}
	
	public String getColumnType(int columnNumber) {
		if(results.size()>0&&results.get(0).size()>columnNumber-1) {
			return (String) results.get(0).get(results.get(0).keySet().toArray()[columnNumber]).getClass().getSimpleName();
		}
		return null;
	}
	
	public String getColumnType(String columnName) {
		if(results.size()>0&&results.get(0).size()>0) {
			return (String) results.get(0).get(columnName).getClass().getSimpleName();
		}
		return null;
	}

}
