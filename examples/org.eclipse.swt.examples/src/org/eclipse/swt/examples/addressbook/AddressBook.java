/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.addressbook;


/* Imports */
import java.io.*;
import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

/**
 * AddressBookExample is an example that uses <code>org.eclipse.swt</code> 
 * libraries to implement a simple address book.  This application has 
 * save, load, sorting, and searching functions common
 * to basic address books.
 */
public class AddressBook {

	private static ResourceBundle resAddressBook = ResourceBundle.getBundle("examples_addressbook");
	private Shell shell;
	
	private Table table;
	private SearchDialog searchDialog;
	
	private File file;
	private boolean isModified;
	
	private String[] copyBuffer;

	private int lastSortColumn= -1;
	
	private static final String DELIMITER = "\t";
	private static final String[] columnNames = {resAddressBook.getString("Last_name"),
												 resAddressBook.getString("First_name"),
												 resAddressBook.getString("Business_phone"),
												 resAddressBook.getString("Home_phone"),
												 resAddressBook.getString("Email"),
												 resAddressBook.getString("Fax")};
	
public static void main(String[] args) {
	Display display = new Display();
	AddressBook application = new AddressBook();
	Shell shell = application.open(display);
	while(!shell.isDisposed()){
		if(!display.readAndDispatch())
			display.sleep();
	}
	display.dispose();
}
public Shell open(Display display) {
	shell = new Shell(display);
	shell.setLayout(new FillLayout());
	shell.addShellListener(new ShellAdapter() {
		public void shellClosed(ShellEvent e) {
			e.doit = closeAddressBook();
		}
	});
	
	createMenuBar();

	searchDialog = new SearchDialog(shell);
	searchDialog.setSearchAreaNames(columnNames);
	searchDialog.setSearchAreaLabel(resAddressBook.getString("Column"));
	searchDialog.addFindListener(new FindListener () {
		public boolean find() {
			return findEntry();
		}
	});
					
	table = new Table(shell, SWT.SINGLE | SWT.BORDER | SWT.FULL_SELECTION);
	table.setHeaderVisible(true);	
	table.setMenu(createPopUpMenu());	
	table.addSelectionListener(new SelectionAdapter() {
		public void widgetDefaultSelected(SelectionEvent e) {
			TableItem[] items = table.getSelection();
			if (items.length > 0) editEntry(items[0]);
		}
	});
	for(int i = 0; i < columnNames.length; i++) {
		TableColumn column = new TableColumn(table, SWT.NONE);
		column.setText(columnNames[i]);
		column.setWidth(150);
		final int columnIndex = i;
		column.addSelectionListener(new SelectionAdapter() {		
			public void widgetSelected(SelectionEvent e) {
				sort(columnIndex);
			}
		});
	}

	newAddressBook();

	shell.setSize(table.computeSize(SWT.DEFAULT, SWT.DEFAULT).x, 300);
	shell.open();
	return shell;
}

private boolean closeAddressBook() {
	if(isModified) {
		//ask user if they want to save current address book
		MessageBox box = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO | SWT.CANCEL);
		box.setText(shell.getText());
		box.setMessage(resAddressBook.getString("Close_save"));
	
		int choice = box.open();
		if(choice == SWT.CANCEL) {
			return false;
		} else if(choice == SWT.YES) {
			if (!save()) return false;
		}
	}
		
	TableItem[] items = table.getItems();
	for (int i = 0; i < items.length; i ++) {
		items[i].dispose();
	}
	
	return true;
}
/**
 * Creates the menu at the top of the shell where most
 * of the programs functionality is accessed.
 *
 * @return		The <code>Menu</code> widget that was created
 */
private Menu createMenuBar() {
	Menu menuBar = new Menu(shell, SWT.BAR);
	shell.setMenuBar(menuBar);
	
	//create each header and subMenu for the menuBar
	createFileMenu(menuBar);
	createEditMenu(menuBar);
	createSearchMenu(menuBar);
	createHelpMenu(menuBar);
	
	return menuBar;
}

/**
 * Converts an encoded <code>String</code> to a String array representing a table entry.
 */
private String[] decodeLine(String line) {
	if(line == null) return null;
	
	String[] parsedLine = new String[table.getColumnCount()];
	for(int i = 0; i < parsedLine.length - 1; i++) {
		int index = line.indexOf(DELIMITER);
		if (index > -1) {
			parsedLine[i] = line.substring(0, index);
			line = line.substring(index + DELIMITER.length(), line.length());
		} else {
			return null;
		}
	}
	
	if (line.indexOf(DELIMITER) != -1) return null;
	
	parsedLine[parsedLine.length - 1] = line;

	return parsedLine;
}
private void displayError(String msg) {
	MessageBox box = new MessageBox(shell, SWT.ICON_ERROR);
	box.setMessage(msg);
	box.open();
}
private void editEntry(TableItem item) {
	DataEntryDialog dialog = new DataEntryDialog(shell);
	dialog.setLabels(columnNames);
	String[] values = new String[table.getColumnCount()];
	for (int i = 0; i < values.length; i++) {
		values[i] = item.getText(i);
	}
	dialog.setValues(values);
	values = dialog.open();
	if (values != null) {
		item.setText(values);
		isModified = true;
	}
}
private String encodeLine(String[] tableItems) {
	String line = "";
	for (int i = 0; i < tableItems.length - 1; i++) {
		line += tableItems[i] + DELIMITER;
	}
	line += tableItems[tableItems.length - 1] + "\n";
	
	return line;
}
private boolean findEntry() {
	Cursor waitCursor = shell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
	shell.setCursor(waitCursor);
	
	boolean matchCase = searchDialog.getMatchCase();
	boolean matchWord = searchDialog.getMatchWord();
	String searchString = searchDialog.getSearchString();
	int column = searchDialog.getSelectedSearchArea();
	
	searchString = matchCase ? searchString : searchString.toLowerCase();
	
	boolean found = false;
	if (searchDialog.getSearchDown()) {
		for(int i = table.getSelectionIndex() + 1; i < table.getItemCount(); i++) {
			if (found = findMatch(searchString, table.getItem(i), column, matchWord, matchCase)){
				table.setSelection(i);
				break;
			}
		}
	} else {
		for(int i = table.getSelectionIndex() - 1; i > -1; i--) {
			if (found = findMatch(searchString, table.getItem(i), column, matchWord, matchCase)){
				table.setSelection(i);
				break;
			}
		}
	}
	
	shell.setCursor(null);
		
	return found;
}
private boolean findMatch(String searchString, TableItem item, int column, boolean matchWord, boolean matchCase) {
	
	String tableText = matchCase ? item.getText(column) : item.getText(column).toLowerCase();
	if (matchWord) {
		if (tableText != null && tableText.equals(searchString)) {
			return true;
		}
		
	} else {
		if(tableText!= null && tableText.indexOf(searchString) != -1) {
			return true;
		}
	}
	return false;
}
private void newAddressBook() {	
	shell.setText(resAddressBook.getString("Title_bar") + resAddressBook.getString("New_title"));
	file = null;
	isModified = false;
}
private void newEntry() {
	DataEntryDialog dialog = new DataEntryDialog(shell);
	dialog.setLabels(columnNames);
	String[] data = dialog.open();
	if (data != null) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(data);
		isModified = true;
	}
}

private void openAddressBook() {	
	FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);

	fileDialog.setFilterExtensions(new String[] {"*.adr;", "*.*"});
	fileDialog.setFilterNames(new String[] {resAddressBook.getString("Book_filter_name") + " (*.adr)", 
											resAddressBook.getString("All_filter_name") + " (*.*)"});
	String name = fileDialog.open();

	if(name == null) return;
	File file = new File(name);
	if (!file.exists()) {
		displayError(resAddressBook.getString("File")+file.getName()+" "+resAddressBook.getString("Does_not_exist")); 
		return;
	}
	
	Cursor waitCursor = shell.getDisplay().getSystemCursor(SWT.CURSOR_WAIT);
	shell.setCursor(waitCursor);
	
	FileReader fileReader = null;
	BufferedReader bufferedReader = null;
	String[] data = new String[0];
	try {
		fileReader = new FileReader(file.getAbsolutePath());
		bufferedReader = new BufferedReader(fileReader);
		String nextLine = bufferedReader.readLine();
		while (nextLine != null){
			String[] newData = new String[data.length + 1];
			System.arraycopy(data, 0, newData, 0, data.length);
			newData[data.length] = nextLine;
			data = newData;
			nextLine = bufferedReader.readLine();
		}
	} catch(FileNotFoundException e) {
		displayError(resAddressBook.getString("File_not_found") + "\n" + file.getName());
		return;
	} catch (IOException e ) {
		displayError(resAddressBook.getString("IO_error_read") + "\n" + file.getName());
		return;
	} finally {	
		
		shell.setCursor(null);
	
		if(fileReader != null) {
			try {
				fileReader.close();
			} catch(IOException e) {
				displayError(resAddressBook.getString("IO_error_close") + "\n" + file.getName());
				return;
			}
		}
	}
	
	String[][] tableInfo = new String[data.length][table.getColumnCount()];
	int writeIndex = 0;
	for (int i = 0; i < data.length; i++) {
		String[] line = decodeLine(data[i]);
		if (line != null) tableInfo[writeIndex++] = line;
	}
	if (writeIndex != data.length) {
		String[][] result = new String[writeIndex][table.getColumnCount()];
		System.arraycopy(tableInfo, 0, result, 0, writeIndex);
		tableInfo = result;
	}
	Arrays.sort(tableInfo, new RowComparator(0));

	for (int i = 0; i < tableInfo.length; i++) {
		TableItem item = new TableItem(table, SWT.NONE);
		item.setText(tableInfo[i]);
	}
	shell.setText(resAddressBook.getString("Title_bar")+fileDialog.getFileName());
	isModified = false;
	this.file = file;
}
private boolean save() {
	if(file == null) return saveAs();
	
	Cursor waitCursor = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
	shell.setCursor(waitCursor);
	
	TableItem[] items = table.getItems();
	String[] lines = new String[items.length];
	for(int i = 0; i < items.length; i++) {
		String[] itemText = new String[table.getColumnCount()];
		for (int j = 0; j < itemText.length; j++) {
			itemText[j] = items[i].getText(j);
		}
		lines[i] = encodeLine(itemText);
	}
		
	FileWriter fileWriter = null;
	try { 
		fileWriter = new FileWriter(file.getAbsolutePath(), false);
		for (int i = 0; i < lines.length; i++) {
			fileWriter.write(lines[i]);
		}
	} catch(FileNotFoundException e) {
		displayError(resAddressBook.getString("File_not_found") + "\n" + file.getName());
		return false;
	} catch(IOException e ) {
		displayError(resAddressBook.getString("IO_error_write") + "\n" + file.getName());
		return false;
	} finally {
		shell.setCursor(null);
		
		if(fileWriter != null) {
			try {
				fileWriter.close();
			} catch(IOException e) {
				displayError(resAddressBook.getString("IO_error_close") + "\n" + file.getName());
				return false;
			}
		}
	}

	shell.setText(resAddressBook.getString("Title_bar")+file.getName());
	isModified = false;
	return true;
}
private boolean saveAs() {
		
	FileDialog saveDialog = new FileDialog(shell, SWT.SAVE);
	saveDialog.setFilterExtensions(new String[] {"*.adr;",  "*.*"});
	saveDialog.setFilterNames(new String[] {"Address Books (*.adr)", "All Files "});
	
	saveDialog.open();
	String name = saveDialog.getFileName();
		
	if(name.equals("")) return false;

	if(name.indexOf(".adr") != name.length() - 4) {
		name += ".adr";
	}

	File file = new File(saveDialog.getFilterPath(), name);
	if(file.exists()) {
		MessageBox box = new MessageBox(shell, SWT.ICON_WARNING | SWT.YES | SWT.NO);
		box.setText(resAddressBook.getString("Save_as_title"));
		box.setMessage(resAddressBook.getString("File") + file.getName()+" "+resAddressBook.getString("Query_overwrite"));
		if(box.open() != SWT.YES) {
			return false;
		}
	}
	this.file = file;
	return save();	
}
private void sort(int column) {
	if(table.getItemCount() <= 1) return;

	TableItem[] items = table.getItems();
	String[][] data = new String[items.length][table.getColumnCount()];
	for(int i = 0; i < items.length; i++) {
		for(int j = 0; j < table.getColumnCount(); j++) {
			data[i][j] = items[i].getText(j);
		}
	}
	
	Arrays.sort(data, new RowComparator(column));
	
	if (lastSortColumn != column) {
		table.setSortColumn(table.getColumn(column));
		table.setSortDirection(SWT.DOWN);
		for (int i = 0; i < data.length; i++) {
			items[i].setText(data[i]);
		}
		lastSortColumn = column;
	} else {
		// reverse order if the current column is selected again
		table.setSortDirection(SWT.UP);
		int j = data.length -1;
		for (int i = 0; i < data.length; i++) {
			items[i].setText(data[j--]);
		}
		lastSortColumn = -1;
	}
	
}
/**
 * Creates all the items located in the File submenu and
 * associate all the menu items with their appropriate
 * functions.
 *
 * @param	menuBar Menu
 *				the <code>Menu</code> that file contain
 *				the File submenu.
 */
private void createFileMenu(Menu menuBar) {
	//File menu.
	MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
	item.setText(resAddressBook.getString("File_menu_title"));
	Menu menu = new Menu(shell, SWT.DROP_DOWN);
	item.setMenu(menu);
	/** 
	 * Adds a listener to handle enabling and disabling 
	 * some items in the Edit submenu.
	 */
	menu.addMenuListener(new MenuAdapter() {
		public void menuShown(MenuEvent e) {
			Menu menu = (Menu)e.widget;
			MenuItem[] items = menu.getItems();
			items[1].setEnabled(table.getSelectionCount() != 0); // edit contact
			items[5].setEnabled((file != null) && isModified); // save
			items[6].setEnabled(table.getItemCount() != 0); // save as
		}
	});


	//File -> New Contact
	MenuItem subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("New_contact"));
	subItem.setAccelerator(SWT.MOD1 + 'N');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			newEntry();
		}
	});
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("Edit_contact"));
	subItem.setAccelerator(SWT.MOD1 + 'E');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TableItem[] items = table.getSelection();
			if (items.length == 0) return;
			editEntry(items[0]);
		}
	});

	
	new MenuItem(menu, SWT.SEPARATOR);
	
	//File -> New Address Book
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("New_address_book"));
	subItem.setAccelerator(SWT.MOD1 + 'B');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (closeAddressBook()) {
				newAddressBook();
			}
		}
	});

	//File -> Open
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("Open_address_book"));
	subItem.setAccelerator(SWT.MOD1 + 'O');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (closeAddressBook()) {
				openAddressBook();
			}
		}
	});

	//File -> Save.
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("Save_address_book"));
	subItem.setAccelerator(SWT.MOD1 + 'S');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			save();
		}
	});
	
	//File -> Save As.
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("Save_book_as"));
	subItem.setAccelerator(SWT.MOD1 + 'A');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			saveAs();
		}
	});

		
	new MenuItem(menu, SWT.SEPARATOR);
	
	//File -> Exit.
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("Exit"));
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			shell.close();
		}
	});
}

/**
 * Creates all the items located in the Edit submenu and
 * associate all the menu items with their appropriate
 * functions.
 *
 * @param	menuBar Menu
 *				the <code>Menu</code> that file contain
 *				the Edit submenu.
 *
 * @see	#createSortMenu()
 */
private MenuItem createEditMenu(Menu menuBar) {
	//Edit menu.
	MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
	item.setText(resAddressBook.getString("Edit_menu_title"));
	Menu menu = new Menu(shell, SWT.DROP_DOWN);
	item.setMenu(menu);
	
	/** 
	 * Add a listener to handle enabling and disabling 
	 * some items in the Edit submenu.
	 */
	menu.addMenuListener(new MenuAdapter() {
		public void menuShown(MenuEvent e) {
			Menu menu = (Menu)e.widget;
			MenuItem[] items = menu.getItems();
			int count = table.getSelectionCount();
			items[0].setEnabled(count != 0); // edit
			items[1].setEnabled(count != 0); // copy
			items[2].setEnabled(copyBuffer != null); // paste
			items[3].setEnabled(count != 0); // delete
			items[5].setEnabled(table.getItemCount() != 0); // sort
		}
	});
	
	//Edit -> Edit
	MenuItem subItem = new MenuItem(menu, SWT.PUSH);
	subItem.setText(resAddressBook.getString("Edit"));
	subItem.setAccelerator(SWT.MOD1 + 'E');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TableItem[] items = table.getSelection();
			if (items.length == 0) return;
			editEntry(items[0]);
		}
	});

	//Edit -> Copy
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("Copy"));
	subItem.setAccelerator(SWT.MOD1 + 'C');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TableItem[] items = table.getSelection();
			if (items.length == 0) return;
			copyBuffer = new String[table.getColumnCount()];
			for (int i = 0; i < copyBuffer.length; i++) {
				copyBuffer[i] = items[0].getText(i);
			}
		}
	});
	
	//Edit -> Paste
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("Paste"));
	subItem.setAccelerator(SWT.MOD1 + 'V');
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (copyBuffer == null) return;
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(copyBuffer);
			isModified = true;
		}
	});
	
	//Edit -> Delete
	subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("Delete"));
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TableItem[] items = table.getSelection();
			if (items.length == 0) return;
			items[0].dispose();
			isModified = true;		}
	});
	
	new MenuItem(menu, SWT.SEPARATOR);
	
	//Edit -> Sort(Cascade)
	subItem = new MenuItem(menu, SWT.CASCADE);
	subItem.setText(resAddressBook.getString("Sort"));
	Menu submenu = createSortMenu();
	subItem.setMenu(submenu);
	
	return item;
	
}

/**
 * Creates all the items located in the Sort cascading submenu and
 * associate all the menu items with their appropriate
 * functions.
 *
 * @return	Menu
 *			The cascading menu with all the sort menu items on it.
 */
private Menu createSortMenu() {
	Menu submenu = new Menu(shell, SWT.DROP_DOWN);
	MenuItem subitem;
	for(int i = 0; i < columnNames.length; i++) {
		subitem = new MenuItem (submenu, SWT.NONE);
		subitem.setText(columnNames [i]);
		final int column = i;
		subitem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				sort(column);
			}
		});

	}
	
	return submenu;
}

/**
 * Creates all the items located in the Search submenu and
 * associate all the menu items with their appropriate
 * functions.
 *
 * @param	menuBar	Menu
 *				the <code>Menu</code> that file contain
 *				the Search submenu.
 */
private void createSearchMenu(Menu menuBar) {
	//Search menu.
	MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
	item.setText(resAddressBook.getString("Search_menu_title"));
	Menu searchMenu = new Menu(shell, SWT.DROP_DOWN);
	item.setMenu(searchMenu);

	//Search -> Find...
	item = new MenuItem(searchMenu, SWT.NONE);
	item.setText(resAddressBook.getString("Find"));
	item.setAccelerator(SWT.MOD1 + 'F');
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			searchDialog.setMatchCase(false);
			searchDialog.setMatchWord(false);
			searchDialog.setSearchDown(true);
			searchDialog.setSearchString("");
			searchDialog.setSelectedSearchArea(0);
			searchDialog.open();
		}
	});

	//Search -> Find Next
	item = new MenuItem(searchMenu, SWT.NONE);
	item.setText(resAddressBook.getString("Find_next"));
	item.setAccelerator(SWT.F3);
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			searchDialog.open();
		}
	});
}

/** 
 * Creates all items located in the popup menu and associates
 * all the menu items with their appropriate functions.
 *
 * @return	Menu
 *			The created popup menu.
 */
private Menu createPopUpMenu() {
	Menu popUpMenu = new Menu(shell, SWT.POP_UP);

	/** 
	 * Adds a listener to handle enabling and disabling 
	 * some items in the Edit submenu.
	 */
	popUpMenu.addMenuListener(new MenuAdapter() {
		public void menuShown(MenuEvent e) {
			Menu menu = (Menu)e.widget;
			MenuItem[] items = menu.getItems();
			int count = table.getSelectionCount();
			items[2].setEnabled(count != 0); // edit
			items[3].setEnabled(count != 0); // copy
			items[4].setEnabled(copyBuffer != null); // paste
			items[5].setEnabled(count != 0); // delete
			items[7].setEnabled(table.getItemCount() != 0); // find
		}
	});

	//New
	MenuItem item = new MenuItem(popUpMenu, SWT.PUSH);
	item.setText(resAddressBook.getString("Pop_up_new"));
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			newEntry();
		}
	});
	
	new MenuItem(popUpMenu, SWT.SEPARATOR);	
	
	//Edit
	item = new MenuItem(popUpMenu, SWT.PUSH);
	item.setText(resAddressBook.getString("Pop_up_edit"));
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TableItem[] items = table.getSelection();
			if (items.length == 0) return;
			editEntry(items[0]);
		}
	});

	//Copy
	item = new MenuItem(popUpMenu, SWT.PUSH);
	item.setText(resAddressBook.getString("Pop_up_copy"));
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TableItem[] items = table.getSelection();
			if (items.length == 0) return;
			copyBuffer = new String[table.getColumnCount()];
			for (int i = 0; i < copyBuffer.length; i++) {
				copyBuffer[i] = items[0].getText(i);
			}
		}
	});
	
	//Paste
	item = new MenuItem(popUpMenu, SWT.PUSH);
	item.setText(resAddressBook.getString("Pop_up_paste"));
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			if (copyBuffer == null) return;
			TableItem item = new TableItem(table, SWT.NONE);
			item.setText(copyBuffer);
			isModified = true;
		}
	});
	
	//Delete
	item = new MenuItem(popUpMenu, SWT.PUSH);
	item.setText(resAddressBook.getString("Pop_up_delete"));
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			TableItem[] items = table.getSelection();
			if (items.length == 0) return;
			items[0].dispose();
			isModified = true;
		}
	});
	
	new MenuItem(popUpMenu, SWT.SEPARATOR);	
	
	//Find...
	item = new MenuItem(popUpMenu, SWT.PUSH);
	item.setText(resAddressBook.getString("Pop_up_find"));
	item.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			searchDialog.open();
		}
	});

	return popUpMenu;
}

/**
 * Creates all the items located in the Help submenu and
 * associate all the menu items with their appropriate
 * functions.
 *
 * @param	menuBar	Menu
 *				the <code>Menu</code> that file contain
 *				the Help submenu.
 */
private void createHelpMenu(Menu menuBar) {
	
	//Help Menu
	MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
	item.setText(resAddressBook.getString("Help_menu_title"));	
	Menu menu = new Menu(shell, SWT.DROP_DOWN);
	item.setMenu(menu);
	
	//Help -> About Text Editor
	MenuItem subItem = new MenuItem(menu, SWT.NONE);
	subItem.setText(resAddressBook.getString("About"));
	subItem.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			MessageBox box = new MessageBox(shell, SWT.NONE);
			box.setText(resAddressBook.getString("About_1") + shell.getText());
			box.setMessage(shell.getText() + resAddressBook.getString("About_2"));
			box.open();		
		}
	});
}

/**
 * To compare entries (rows) by the given column
 */
private class RowComparator implements Comparator {
	private int column;
	
	/**
	 * Constructs a RowComparator given the column index
	 * @param col The index (starting at zero) of the column
	 */
	public RowComparator(int col) {
		column = col;
	}
	
	/**
	 * Compares two rows (type String[]) using the specified
	 * column entry.
	 * @param obj1 First row to compare
	 * @param obj2 Second row to compare
	 * @return negative if obj1 less than obj2, positive if
	 * 			obj1 greater than obj2, and zero if equal.
	 */
	public int compare(Object obj1, Object obj2) {
		String[] row1 = (String[])obj1;
		String[] row2 = (String[])obj2;
		
		return row1[column].compareTo(row2[column]);
	}
}
}
