package com.exist.ActTwo;

import org.apache.commons.lang.StringUtils;
import com.exist.ActTwo.Table;
import com.exist.ActTwo.TableService;
import com.exist.ActTwo.Row;

import java.util.Scanner;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;

public class App {
	private Scanner in = new Scanner(System.in);
	private TableService tableService = new TableService();

	public static void main(String[] args) {
		App app = new App();
		String filePath =  "/home/joel/Activities/Maven2/myapp/textfile.txt";
		boolean continueProcess = true;

		try {
			app.tableService.initializeTable(filePath);
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Textfile Data:");
		app.print();

		while (continueProcess) {
			System.out.println();
			System.out.println("Commands:");
			System.out.println("1. Print");
			System.out.println("2. Edit");
			System.out.println("3. Search");
			System.out.println("4. Add");
			System.out.println("5. Sort");
			System.out.println("6. Reset");
			System.out.println("7. Exit");
			System.out.print(StringUtils.swapCase("Enter Equivalent Number of Command: "));

			int choice = app.getChoice();
			switch (choice) {
			case 1: {
				app.print();
				break;
			}
			
			case 2: {
				app.tableService.edit(app.getRow(), app.getColumn(), app.getKey(app.tableService.getTable().getList()), app.getValue());
				System.out.println("Successfully Updated!");
				break;
			}

			case 3: {
				app.search();
				break;
			}
			case 4: {
				app.addRow();
				System.out.println("Successfully Added!");
				break;
			}
			case 5: {
				app.tableService.sort();
				System.out.println("Successfully Sorted!");
				break;
			}
			case 6: {
				app.reset();
				System.out.println("Reset Successfully Done!");
				break;
			}

			case 7: {
				System.out.println("End!");
				continueProcess = false;
				break;
			}
			default: {
				System.out.println("Command not found!");
			}

			}
		}
	}

	private int getChoice() {
		String choice = in.nextLine();
		while (!tableService.isValidChoice(choice)) {
			System.out.print("Please select a valid choice: ");
			choice = in.nextLine();
		}
		return Integer.parseInt(choice);
	}

	private void print() {
		for (Row row : tableService.getTable().getList()) {
			System.out.println(row.toString());
		}
	}

	private int getRow() {
		System.out.print("Row to Edit: ");
		String row = in.nextLine();
		while (!tableService.isValidRow(row, tableService.getRowSize())) {
			System.out.print("Please select a valid row: ");
			row = in.nextLine();
		}
		return Integer.parseInt(row);
	}

	private int getColumn() {
		System.out.print("Column to Edit: ");
		String column = in.nextLine();
		while (!tableService.isValidColumn(column, tableService.getColumnSize())) {
			System.out.print("Please select a valid column: ");
			column = in.nextLine();
		}
		return Integer.parseInt(column);
	}

	private String getKey(List<Row> list) {
		System.out.print("Enter New key: ");
		String key = in.nextLine();
		while (!tableService.isValidKey(key, list)) {
			System.out.print("Please input a valid and non-existing key: ");
			key = in.nextLine();
		}
		return key;
	}

	private String getValue() {
		System.out.print("Enter New value: ");
		String value = in.nextLine();
		while (!tableService.isValidValue(value)) {
			System.out.print("Please input a valid value: ");
			value = in.nextLine();
		}
		return value;
	}

	private String getSearch() {
		System.out.print("Enter search string: ");
		return in.nextLine();
	}

	private void search() {
		String search = getSearch();
		List<Map<String,String>> listOfMatches = tableService.search(search);
		boolean hasOccurrence = false;
		int row = 0;
		for (Map<String,String> map : listOfMatches) {
			for (Map.Entry<String, String> entry : map.entrySet()) {
				System.out.println("@row" + row + "-" + entry.getKey() + " : " + entry.getValue() + " occurence(s)");
				hasOccurrence = true;
			}
			row++;
		}
		if(!hasOccurrence){
			System.out.println("No matches found.");
		}
	}

	private void addRow() {
		tableService.add(createRow());
	}

	private Row createRow() {
		Row row = new Row(new LinkedHashMap<String, String>());
		for (int i = 0; i < tableService.getColumnSize(); i++) {
			System.out.println("@Column" + i + ":");
			row.getRow().put(getKey(tableService.getTable().getList()), getValue());
		}
		System.out.println();
		return row;
	}

	private void reset() {
		List<Row> newList = new ArrayList<>();

		System.out.print("Enter number of rows: ");
		String rowLength = in.nextLine();
		while (!tableService.isValidNumber(rowLength)) {
			System.out.print("Please input a valid number of rows: ");
			rowLength = in.nextLine();
		}

		System.out.print("Enter number of columns: ");
		String columnLength = in.nextLine();
		while (!tableService.isValidNumber(columnLength)) {
			System.out.print("Please input a valid number of columns: ");
			columnLength = in.nextLine();
		}

		for (int i = 0; i < Integer.parseInt(columnLength); i++) {
			Row row = new Row(new LinkedHashMap<String, String>());
			for (int j = 0; j < Integer.parseInt(rowLength); j++) {
				row.getRow().put(getKey(newList), getValue());
			}
			newList.add(row);
		}
		//System.out.println(newList);
		tableService.getTable().setList(newList);
		tableService.fileWrite();
	}

}
