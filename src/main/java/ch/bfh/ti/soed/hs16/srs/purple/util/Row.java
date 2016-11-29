package ch.bfh.ti.soed.hs16.srs.purple.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Used to map sql types into java types.
 * */
public class Row {
	private List<Entry<Object, Class<?>>> row;
	public static Map<String, Class<?>> TYPE;
	
	static {
		TYPE = new HashMap<String,Class<?>>();
		
		TYPE.put("INT", Integer.class);
		TYPE.put("TINYINT", Boolean.class);
		TYPE.put("VARCHAR", String.class);
		TYPE.put("TIMESTAMP", Timestamp.class);
	}
	
	public Row(){
		row = new ArrayList<Entry<Object, Class<?>>>();
	}
	
	/**
	 * Adds data into the row with the associated java type.
	 * */
	private <T> void add(T data){
		row.add(new AbstractMap.SimpleImmutableEntry<Object,Class<?>>(data, data.getClass()));
	}
	
	/**
	 * Casts an sql-type into a java type and adds the data into the row.
	 * */
	private void add(Object data, String sqlType){
		if(data != null){
			add((Row.TYPE.get(sqlType)).cast(data));
		}else{
			row.add(new AbstractMap.SimpleImmutableEntry<Object,Class<?>>(null,null));
		}
	}
	
	/**
	 * Fetches the results of an sql-query into an ArrayList with rows.
	 * 
	 * @return A table containing the rows from the sql-query or null if the query was empty.
	 * */
	public static ArrayList<Row> formTable(ResultSet resultSet){
		ArrayList<Row> table = new ArrayList<Row>();
		
		if(resultSet == null) return null;
		
		try {
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();
			
			while(resultSet.next()){
				Row currentRow = new Row();
				
				for(int i=1; i<=columnCount; i++){
					Object resultObject = resultSet.getObject(i);
					String cloumnType = rsmd.getColumnTypeName(i);
					currentRow.add(resultObject,cloumnType);
				}
				table.add(currentRow);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return table;
	}

	public List<Entry<Object, Class<?>>> getRow() {
		return row;
	}
}
