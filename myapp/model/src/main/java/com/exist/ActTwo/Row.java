package com.exist.ActTwo;

import java.util.Map;
import java.util.LinkedHashMap;

public class Row {
	private Map<String, String> row;
									
	public Row(Map row) {				
		this.row = row;	
	}									
	
	public Map getRow() {
		return row;
	}

	public void setRow(Map row) {
		this.row = row;
	}

	public String toString() {
		return row.toString();
	}
}
