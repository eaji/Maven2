package com.exist.ActTwo;

import com.exist.ActTwo.Row;
import com.exist.ActTwo.Table;

import java.util.Scanner;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.List;
import java.util.Map;
import java.io.FileWriter;
import java.util.TreeMap;
import org.apache.commons.lang.StringUtils;

public class TableService {

	private Table table = new Table();
	private String filePath;

	public void initializeTable(String filePath) throws IOException {
		this.filePath = filePath;
		Map<String, String> row = new LinkedHashMap<>();
		List<Row> tempList = new ArrayList<>();
		LineNumberReader lineReader = new LineNumberReader(new FileReader(filePath));
		String[] splitRow = new String[0];
		String[] splitColumn = new String[0];
		String lineText = null;

		for (lineText = lineReader.readLine(); lineText != null; lineText = lineReader.readLine()) {
			row = new LinkedHashMap<>();
			splitRow = lineText.split("\\s+");
			for (int i = 0; i < splitRow.length; i++) {
				splitColumn = splitRow[i].split("=");
				row.put(splitColumn[0], splitColumn[1]);
			}
			tempList.add(new Row(row));
		}
		table.setList(tempList);
	}
	
	public void add(Row row) {
		List<Row> tempList = table.getList();
		tempList.add(row);
		table.setList(tempList);
		fileWrite();
	}

	public void edit(int rowNumber, int columnNumber, String newKey, String newValue) {
		String cell = null;
		int index = 0;
		for (Map.Entry<String, String> entry : ((Map<String, String>) table.getList().get(rowNumber).getRow()).entrySet()) {
			if (index == columnNumber) {
				cell = entry.getKey();
				break;
			}
			index++;
		}
		table.getList().get(rowNumber).getRow().remove(cell);
		table.getList().get(rowNumber).getRow().put(newKey, newValue);
		fileWrite();
	}

	public List<Map<String,String>> search(String search) {
		List<Map<String,String>> listOfMatches = new ArrayList<>();
		for(Row row : table.getList()){
			int column = 0;
			Map<String,String> columnMatches = new LinkedHashMap<>();
			for(Map.Entry<String,String> entry : ((Map<String,String>)row.getRow()).entrySet()){
				int numberOfMatchesInKey = StringUtils.countMatches(entry.getKey(), search);
				int numberOfMatchesInValue = StringUtils.countMatches(entry.getValue(), search);
				if(numberOfMatchesInKey + numberOfMatchesInValue > 0){
					columnMatches.put("column" + column, Integer.toString(numberOfMatchesInKey + numberOfMatchesInValue));
				}
			column++;	
			}
			listOfMatches.add(columnMatches);
		}
		return listOfMatches;
	}

	public void sort() {
		List<Row> sortedList = new ArrayList<>();
		int counter = 0;
		for (Row row : table.getList()) {
			TreeMap treemap = new TreeMap(row.getRow());
			sortedList.add(new Row(treemap));
		}
		//System.out.println(sortedList);
		table.setList(sortedList);
		fileWrite();
	}

	public void fileWrite() {
		int counter = 1;
		try {
			FileWriter writer = new FileWriter(filePath, false);
			for (Row row : table.getList()) {
				for (Map.Entry<String, String> entry : ((Map<String, String>) row.getRow()).entrySet()) {
					writer.write(entry.getKey() + "=" + entry.getValue() + "\t");
					if (counter % getColumnSize() == 0) {
						writer.write("\n");
					}
					counter++;
				}
			}
			writer.close();
		} catch (IOException ex) {
			System.err.println(ex);
		}
	}

	public int getRowSize() {
		return table.getList().size();
	}

	public int getColumnSize() {
		return table.getList().get(0).getRow().size();
	}

	public boolean isValidChoice(String choice) {
		return choice.matches("[1-7]");
	}

	public boolean isValidRow(String row, int rowSize) {
		if (row.matches("[0-9]") && Integer.parseInt(row) < (rowSize)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidColumn(String column, int columnSize) {
		if (column.matches("[0-9]") && Integer.parseInt(column) < (columnSize)) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isValidNumber(String number) {
		return number.matches("[1-9]");
	}

	public boolean isValidKey(String newKey, List<Row> list) {
		for (int i = 0; i < (list.size()); i++) {
			if (list.get(i).getRow().containsKey(newKey) || newKey.contains("=") || newKey.contains(" ")
					|| newKey.isEmpty()) {
				i = -1;
				return false;
			}
		}
		return true;
	}

	public boolean isValidValue(String newValue) {
		while (newValue.contains("=") || newValue.contains(" ") || newValue.isEmpty()) {
			return false;
		}
		return true;
	}

	public Table getTable(){
		return table;
	}

	public static void errMsg() {
		System.out.println("Invalid input!");
		System.out.println("Enter again: ");
	}

}
