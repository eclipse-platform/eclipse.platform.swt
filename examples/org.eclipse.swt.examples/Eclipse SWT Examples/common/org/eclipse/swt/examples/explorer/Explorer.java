package org.eclipse.swt.examples.explorer;

/*
* Licensed Materials - Property of IBM,
* SWT - The Simple Widget Toolkit,
* (c) Copyright IBM Corp 1998, 1999.
*/

import org.eclipse.swt.events.*;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.program.*;

import java.util.*;
import java.io.*;

/**
*/
public class Explorer {  

 	ResourceBundle resExplorer = ResourceBundle.getBundle("examples_explorer");
 	  
 	Display display;
	Shell   shell;
	Combo   combo;
	ToolBar toolBar;
	Tree    tree;
	Table   table;
	Sash    sash;

	Label numObjectsLabel;
	Label diskSpaceLabel;
	Label allFoldersLabel;
	Label contentLabel;
	
	TreeItem [] comboContents;
	TreeItem previousSelectedTreeItem;
	
	static final String DRIVE_A = "a:"+File.separator;
	static final String DRIVE_B = "b:"+File.separator;

	boolean cancel = false;
	Thread tableThread;
	
	DragSource tableDragSource;
	DropTarget tableDropTarget;
	DragSource treeDragSource;
	DropTarget treeDropTarget;
	TreeItem[] oldTreeSelection;
	TableItem[] oldTableSelection;
	TreeItem[] treeDragItems;
	TableItem[] tableDragItems;

	
public void close () {
	if (shell != null && !shell.isDisposed ()) { 
		shell.dispose ();
	}
	Images.freeAll ();
	display.dispose ();
}
boolean copyFile(File oldFile, File newFile) {
	if (oldFile == null || !oldFile.exists() || newFile == null || newFile.exists())
		return false;
	
	// copy directory	
	if (oldFile.isDirectory()) {
		boolean result = newFile.mkdirs();
		if (result) {
			String[] subFiles = oldFile.list();
			if (subFiles != null) {
				for (int i = 0; i < subFiles.length; i++) {
					File oldSubFile = new File(subFiles[i]);
					File newSubFile = new File(newFile, oldSubFile.getName());
					copyFile(oldSubFile, newSubFile);
				}
			}
		}
		return result;
	}
	
	// copy file
	FileReader in;
	try {
		in = new FileReader(oldFile);
	} catch (FileNotFoundException e) {
		return false;
	}
	FileWriter out;
	try {
		out = new FileWriter(newFile);
	} catch (FileNotFoundException e) {
		try {	in.close(); } catch (IOException e2) {}
		return false;
	}catch (IOException e) {
		try {	in.close(); } catch (IOException e2) {}
		return false;
	}

	try {	
		int count;
		while ((count = in.read()) != -1)
			out.write(count);
	} catch (IOException e) {
		return false;
	} finally {
		try {	in.close(); } catch (IOException e) {}
		try { out.close();} catch (IOException e) {}
	}
	
	return true;
}
void createCombo () {
	combo = new Combo (toolBar, SWT.READ_ONLY);
	ToolItem comboItem = toolBar.getItems () [0];
	comboItem.setControl (combo);
	comboItem.setWidth (185);
	/*
	* It would be nice to use File.listRoots but this
	* feature is only available in JDK 1.22.
	*/
//	File[] roots = File.listRoots();
//	if (roots != null) {
//		for (int i = 0, length = roots.length; i < length; i++) {
//			combo.add(roots[i].getAbsolutePath());
//		}
//	}
	if (System.getProperty ("os.name").indexOf ("Windows") != -1) {
		combo.add (DRIVE_A);
		combo.add (DRIVE_B);	
		for (int i='c'; i<='z'; i++) {
			File drive = new File ((char)i + ":" + File.separator);
			if (drive.isDirectory ()) {
				combo.add (drive.getPath());
			}
		}
	} else {
		combo.add (File.separator);
	}
	combo.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			selectCombo ();
		}
	});	
}
void createDetailsItems (Menu menu) {
	String [] strings = {resExplorer.getString("Large_icons_menuitem"),
						 resExplorer.getString("Small_icons_menuitem"),
						 resExplorer.getString("List_menuitem"),
						 resExplorer.getString("Details_menuitem")};
	final MenuItem [] items = new MenuItem [strings.length];
	SelectionListener l = new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			for (int i=0; i<items.length; i++) {
				items [i].setSelection (items [i] == e.widget);
			}
		}
	};
	for (int i=0; i<strings.length; i++) {
		items [i] = new MenuItem (menu, SWT.RADIO);
		items [i].setText (strings [i]);
		items [i].addSelectionListener (l);
	}
	items [3].setSelection (true);
}
Menu createEditMenu() {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	
	MenuItem item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Undo_menuitem"));
	item.setAccelerator(SWT.CONTROL + 'Z');
	
	new MenuItem (menu, SWT.SEPARATOR);
	
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Cut_menuitem"));
	item.setAccelerator(SWT.CONTROL + 'X');

	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Copy_menuitem"));
	item.setAccelerator(SWT.CONTROL + 'C');

	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Paste_menuitem"));
	item.setAccelerator(SWT.CONTROL + 'V');


	return menu;
}
Menu createFileMenu() {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	MenuItem item;

	// New with submenu
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText (resExplorer.getString("New_menuitem"));
	Menu submenu = new Menu (shell, SWT.DROP_DOWN);
	createNewMenu (submenu);
	item.setMenu (submenu);

	// -------------
	new MenuItem (menu, SWT.SEPARATOR);

	// Create shortcut
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Shortcut_menuitem"));

	// Delete
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Delete_menuitem"));

	// Rename
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Rename_menuitem"));

	// Properties
	item = new MenuItem(menu, SWT.PUSH);
	item.setText (resExplorer.getString("Properties_menuitem"));
	
	// ------------
	new MenuItem (menu, SWT.SEPARATOR);

	// Close
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Close_menuitem"));
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			menuFileExit ();
		}
	});
	return menu;
}
Menu createHelpMenu () {
	// Help topics
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);
	MenuItem item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Help_menuitem"));

	// ----------
	new MenuItem (menu, SWT.SEPARATOR);

	// About the OS
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("About_menuitem") + " " + System.getProperty ("os.name"));	
	
	return menu;
}
void createMenuBar () {
	Menu bar = new Menu (shell, SWT.BAR);
	shell.setMenuBar (bar);

	MenuItem fileItem = new MenuItem (bar, SWT.CASCADE);
	fileItem.setText (resExplorer.getString("File_menutitle"));
	fileItem.setMenu (createFileMenu ());

	MenuItem editItem = new MenuItem (bar, SWT.CASCADE);
	editItem.setText (resExplorer.getString("Edit_menutitle"));
	editItem.setMenu (createEditMenu ());

	MenuItem viewItem = new MenuItem (bar, SWT.CASCADE);
	viewItem.setText (resExplorer.getString("View_menutitle"));
	viewItem.setMenu (createViewMenu ());

	MenuItem toolsItem = new MenuItem (bar, SWT.CASCADE);
	toolsItem.setText (resExplorer.getString("Tools_menutitle"));
	toolsItem.setMenu (createToolsMenu ());

	MenuItem helpItem = new MenuItem (bar, SWT.CASCADE);
	helpItem.setText (resExplorer.getString("Help_menutitle"));
	helpItem.setMenu (createHelpMenu ());
}
void createNewMenu(Menu submenu) {
	MenuItem subitem1 = new MenuItem (submenu, SWT.NULL);
	subitem1.setText (resExplorer.getString("Folder"));

	subitem1 = new MenuItem (submenu, SWT.NULL);
	subitem1.setText (resExplorer.getString("Shortcut"));
}
void createSash () {
	sash = new Sash (shell, SWT.VERTICAL);
	sash.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			selectSash(e);
		}
	});
	Point size = sash.computeSize (SWT.DEFAULT, SWT.DEFAULT);
	sash.setBounds (300, 0, size.x, 0);
}
void createShell () {
	display = new Display ();
	shell = new Shell (display);
	shell.setText (resExplorer.getString("Title"));	
	Images.loadAll (display);
	shell.setImage (Images.ShellIcon);
}
void createSortByMenu(Menu menu) {
	String [] strings = {resExplorer.getString("By_name"),
						 resExplorer.getString("By_type"),
						 resExplorer.getString("By_size"),
						 resExplorer.getString("By_date")};
	MenuItem subitem;
	for (int i=0; i<strings.length; i++) {
		subitem = new MenuItem (menu, SWT.PUSH);
		subitem.setText (strings [i]);
	}
	subitem = new MenuItem (menu, SWT.SEPARATOR);
	subitem = new MenuItem (menu, SWT.PUSH);
	subitem.setText (resExplorer.getString("Auto_arrange"));
}
void createStatusLine () {
	numObjectsLabel = new Label (shell, SWT.BORDER);
	Point size = numObjectsLabel.computeSize (145, SWT.DEFAULT);
	numObjectsLabel.setSize (size.x, size.y + 2);
	diskSpaceLabel = new Label (shell, SWT.BORDER);
	allFoldersLabel = new Label (shell, SWT.BORDER);
	allFoldersLabel.setText(resExplorer.getString("All_folders"));
	contentLabel = new Label (shell, SWT.BORDER);
}
void createTable () {
	table = new Table (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	String [] titles = new String [] {resExplorer.getString("Name"),
									  resExplorer.getString("Size"),
									  resExplorer.getString("Type"),
									  resExplorer.getString("Modified")};
	int [] widths = new int [] {150, 60, 75, 150};
	for (int i=0; i<titles.length; i++) {
		TableColumn column = new TableColumn (table, SWT.NONE);
		column.setText (titles [i]);
		column.setWidth (widths [i]);
	}
	table.setHeaderVisible (true);
	table.setMenu (createTableMenu ());
	table.addSelectionListener(new SelectionAdapter() {
		public void widgetDefaultSelected(SelectionEvent e) {
			defaultSelectTable (e);
		}
	});

	createTableDragSource();
	
	createTableDropTarget();

}
void createTableDragSource(){
	// create a drag source for the table
	int operations = DND.DROP_MOVE | DND.DROP_COPY | DND.DROP_LINK;
	tableDragSource = new DragSource(table, operations);
	tableDragSource.setTransfer(new Transfer[]{FileTransfer.getInstance()});			
	DragSourceListener listener2 = new DragSourceListener() {
		public void dragStart(DragSourceEvent e){
			TableItem[] selection = table.getSelection();
			if (selection == null || selection.length == 0) {
				e.doit = false;
			}
		};
		public void dragSetData(DragSourceEvent event){
			if (!FileTransfer.getInstance().isSupportedType(event.dataType)) return;
			tableDragItems = table.getSelection();
			if (tableDragItems == null || tableDragItems.length == 0) return;
			String[] data = new String[tableDragItems.length];
			for (int i = 0; i < tableDragItems.length; i++) {
				File file = (File)tableDragItems[i].getData();
				data[i] = file.getAbsolutePath();
			}
			event.data = data;
		}
		public void dragFinished(DragSourceEvent event){
			if (event.detail != DND.DROP_NONE)
				refresh();
			tableDragItems = null;
		}
	};
	tableDragSource.addDragListener(listener2);
}
void createTableDropTarget(){
	// create a drop target for the Table
	int operations = DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK;
	tableDropTarget = new DropTarget(table, operations);
	tableDropTarget.setTransfer(new Transfer[] {FileTransfer.getInstance()});
	DropTargetListener listener = new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event) {
			oldTableSelection = table.getSelection();
		};
		public void dragOver(DropTargetEvent event) {
			Point p = table.toControl(new Point(event.x, event.y));
			TableItem item = table.getItem (p);
			if (item != null) {
				File file = (File)item.getData();
				if (file.isDirectory()) {
					//table.setDropSelection (item);
					table.setSelection (new TableItem[] {item});
					return;
				}
			}
			table.setSelection(new TableItem[0]);
		};
		public void dragLeave(DropTargetEvent event) {
			table.setSelection(oldTableSelection);
			oldTableSelection = null;
			//table.setDropSelection(null);
		};
		public void drop(DropTargetEvent event) {
			// Get dropped data
			Object value = event.data;
			if (value == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			String[] names = (String[]) value;
				
			// Find parent file
			File parentFile = null;
			Point p = table.toControl(new Point(event.x, event.y));
			TableItem item = table.getItem(p);
			// was the file dropped on a directory?
			if (item != null) {
				File file = (File)item.getData();
				if (file != null && file.isDirectory()){
					parentFile = file;
				}
			}
			if (parentFile == null) {
				// use the same parent as all other entries in the table
				parentFile = (File)table.getData();
			}
			if (parentFile == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
				
			// Perform drop
			for (int i = 0; i < names.length; i++){
				
				// manipulate underlying file
				File source = new File(names[i]);
				File dest = new File(parentFile, source.getName());
				if (event.detail == DND.DROP_COPY) {
					if (!copyFile(source, dest)) {
						event.detail = DND.DROP_NONE;
						return;
					}
				} else if (event.detail == DND.DROP_MOVE) {
					if (!moveFile(source, dest)) {
						event.detail = DND.DROP_NONE;
						return;
					}
				}
			}
				
			refresh();
		}
	};
	tableDropTarget.addDropListener(listener);
}
Menu createTableMenu () {
	Menu menu = new Menu (shell, SWT.POP_UP);
	MenuItem menuItem;
	
	// View, with submenu
	menuItem = new MenuItem (menu, SWT.CASCADE);
	menuItem.setText (resExplorer.getString("Popup_view"));

	Menu submenu = new Menu (shell, SWT.DROP_DOWN);
	createDetailsItems (submenu);
	menuItem.setMenu (submenu);
	
	menuItem = new MenuItem (menu, SWT.SEPARATOR);
	
	// Arrange Icons with submenu
	menuItem = new MenuItem (menu, SWT.CASCADE);
	menuItem.setText (resExplorer.getString("Popup_arrange"));

	submenu = new Menu (shell, SWT.DROP_DOWN);
	createSortByMenu(submenu);
	menuItem.setMenu(submenu);
	
	// Line up Icons
	menuItem = new MenuItem (menu, SWT.PUSH);
	menuItem.setText (resExplorer.getString("Popup_lineup"));
	menuItem.setEnabled(false);
	
	// ----------------
	menuItem = new MenuItem (menu, SWT.SEPARATOR);	

	// Paste
	menuItem = new MenuItem (menu, SWT.PUSH);
	menuItem.setText (resExplorer.getString("Popup_paste"));
	menuItem.setEnabled(false);

	// Paste shortcut
	menuItem = new MenuItem (menu, SWT.PUSH);
	menuItem.setText (resExplorer.getString("Popup_shortcut"));
	menuItem.setEnabled(false);
	
	// ---------------
	menuItem = new MenuItem (menu, SWT.SEPARATOR);	

	// New with submenu
	menuItem = new MenuItem (menu, SWT.CASCADE);
	menuItem.setText (resExplorer.getString("Popup_new"));

	submenu = new Menu (shell, SWT.DROP_DOWN);
	createNewMenu(submenu);
	menuItem.setMenu(submenu);
	
	// ------------------
	menuItem = new MenuItem (menu, SWT.SEPARATOR);	

	// Properties
	menuItem = new MenuItem (menu, SWT.PUSH);
	menuItem.setText (resExplorer.getString("Popup_properties"));
	
	return menu;
}
void createToolBar() {
	toolBar = new ToolBar(shell, SWT.NULL);
	ToolItem item = new ToolItem(toolBar, SWT.SEPARATOR);
	item = new ToolItem(toolBar, SWT.SEPARATOR);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.UpDirectory);
	item.addSelectionListener(new SelectionAdapter () {
		public void widgetSelected(SelectionEvent e) {
			selectParentTreeItem();
		}
	});
	item = new ToolItem(toolBar, SWT.SEPARATOR);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.MapNetwork);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.Disconnect);
	item = new ToolItem(toolBar, SWT.SEPARATOR);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.Cut);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.Copy);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.Paste);
	item = new ToolItem(toolBar, SWT.SEPARATOR);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.Undo);
	item = new ToolItem(toolBar, SWT.SEPARATOR);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.Delete);
	item = new ToolItem(toolBar, SWT.PUSH);
	item.setImage(Images.Properties);
	item = new ToolItem(toolBar, SWT.SEPARATOR);
	item = new ToolItem(toolBar, SWT.RADIO);
	item.setImage(Images.Large);
	item = new ToolItem(toolBar, SWT.RADIO);
	item.setImage(Images.Small);
	item = new ToolItem(toolBar, SWT.RADIO);
	item.setImage(Images.List);
	item = new ToolItem(toolBar, SWT.RADIO);
	item.setImage(Images.Details);
	item.setSelection(true);
}
Menu createToolsMenu () {
	Menu bar = shell.getMenuBar ();
	Menu menu = new Menu (bar);

	MenuItem item = new MenuItem (menu, SWT.CASCADE);
	item.setText (resExplorer.getString("Find_menuitem"));	

	String [] strings = {
		resExplorer.getString("File_find"),
		resExplorer.getString("Computer_find"),
		resExplorer.getString("Internet_find")
	};
	
	Menu submenu = new Menu (shell, SWT.DROP_DOWN);
	MenuItem subitem;
	for (int i=0; i<strings.length; i++){
		subitem = new MenuItem (submenu, SWT.PUSH);
		subitem.setText (strings[i]);
	}
	subitem = new MenuItem (submenu, SWT.SEPARATOR);
	subitem = new MenuItem (submenu, SWT.PUSH);
	subitem.setText(resExplorer.getString("Autoarrange_menuitem"));

	item.setMenu(submenu);

	
	new MenuItem (menu, SWT.SEPARATOR);
	
	item = new MenuItem (menu, SWT.CHECK);
	item.setText (resExplorer.getString("Mapdrive_menuitem"));	

	item = new MenuItem (menu, SWT.CHECK);
	item.setText (resExplorer.getString("Disconnect_menuitem"));	

	new MenuItem (menu, SWT.SEPARATOR);

	item = new MenuItem (menu, SWT.CHECK);
	item.setText (resExplorer.getString("Goto_menuitem"));	

		
	return menu;
}
void createTree () {
	tree = new Tree (shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
	tree.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			selectTree();
		}
	});
	tree.addTreeListener(new TreeAdapter() {
		public void treeExpanded(TreeEvent e) {
			expandTree(e);
		}
		public void treeCollapsed(TreeEvent e) {
			expandTree(e);
		}
	});
	int length = combo.getItems().length;
	comboContents = new TreeItem [length];
	boolean first = false;
	for (int i=0; i<length; i++){
		TreeItem newItem = new TreeItem (tree, SWT.NULL);
		File file = new File(combo.getItem(i));
		newItem.setText (file.getPath());
		newItem.setImage(Images.Drive);
		newItem.setData(file);
		comboContents [i] = newItem;
		setExpandable(newItem);
	}
	
	createTreeDragSource();
	
	createTreeDropTarget();	
}
void createTreeDragSource(){
	// create a drag source for the tree
	treeDragSource = new DragSource(tree,  DND.DROP_COPY | DND.DROP_LINK | DND.DROP_MOVE);
	treeDragSource.setTransfer(new Transfer[]{FileTransfer.getInstance()});				

	DragSourceListener listener = new DragSourceListener() {
		public void dragStart(DragSourceEvent e){
			treeDragItems = tree.getSelection();
			if (treeDragItems == null || treeDragItems.length == 0) {
				e.doit = false;
			}
		};
		public void dragSetData(DragSourceEvent event){
			if (treeDragItems == null || treeDragItems.length == 0) return;

			if (FileTransfer.getInstance().isSupportedType(event.dataType)) {
				// build an array of the absolute file names
				String[] fileData = new String[treeDragItems.length];
				for (int i = 0; i < treeDragItems.length; i++){
					File file = (File)treeDragItems[i].getData();
					fileData[i] = file.getAbsolutePath();
				}
				event.data = fileData;
			}
		}
		public void dragFinished(DragSourceEvent event) {
			if (event.detail != DND.DROP_NONE) {
				refresh();
			}
			treeDragItems = null;
			
		}
	};
	treeDragSource.addDragListener(listener);
}
void createTreeDropTarget() {
	// create a drop target for the tree
	treeDropTarget = new DropTarget(tree, DND.DROP_COPY | DND.DROP_MOVE);
	treeDropTarget.setTransfer(new Transfer[] {FileTransfer.getInstance()});
	
	DropTargetListener listener2 = new DropTargetAdapter() {
		public void dragEnter(DropTargetEvent event) {
			oldTreeSelection = tree.getSelection();
		};
		public void dragOver(DropTargetEvent event) {
			Point p = tree.toControl(new Point(event.x, event.y));
			TreeItem item = tree.getItem (p);
			if (item != null) {
				//tree.setDropSelection (item);
				tree.setSelection(new TreeItem[] {item});
			}
		};
		public void dragLeave(DropTargetEvent event) {
			tree.setSelection(oldTreeSelection);
			oldTreeSelection = null;
			//tree.setDropSelection (null);
		};
		public void drop(DropTargetEvent event) {
			// Find the item this was dropped on
			Point p = tree.toControl(new Point(event.x, event.y));
			TreeItem parent = tree.getItem (p);
			if (parent == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
				
			// Get the dropped files
			Object value = event.data;
			if (value == null) {
				event.detail = DND.DROP_NONE;
				return;
			}
			String[] fileNames =  (String[]) value;
			File[] files  = new File[fileNames.length];
			for (int i = 0; i < fileNames.length; i++){
				File file = new File(fileNames[i]);
				if (!file.exists()) {
					files = null;
					break;
				}
				files[i] = file;
			}
			if (files == null || files.length == 0) {
				event.detail = DND.DROP_NONE;
				return;
			}
				
			// Make sure we are not dropping on ourselves - we will not add
			// a file to itself
			File parentFile = (File)parent.getData();
			for (int i = 0; i < files.length; i++){						
				if (files[i].getAbsolutePath().equals(parentFile.getAbsolutePath())){
					event.detail = DND.DROP_NONE;
					return;
				}
			}
				
			// Perform drop
			for (int i = 0; i < files.length; i++){
				// manipulate the underlying file
				File source = files[i];
				File dest = new File(parentFile, source.getName());
				if (event.detail == DND.DROP_COPY){
					if (!copyFile(source, dest)){
						event.detail = DND.DROP_NONE;
						return;
					}
				} else if (event.detail == DND.DROP_MOVE) {
					if (!moveFile(source, dest)){
						event.detail = DND.DROP_NONE;
						return;
					}
				}				
			}
			
			refresh();
		}
	};
	treeDropTarget.addDropListener(listener2);
	
}
Menu createViewMenu() {
	Menu bar = shell.getMenuBar();
	Menu menu = new Menu(bar);
	MenuItem item = new MenuItem(menu, SWT.CHECK);
	item.setText(resExplorer.getString("Toolbar_menuitem"));
	item.setSelection (true);
	item.addSelectionListener (new SelectionAdapter() {
		public void widgetSelected (SelectionEvent e) {
			setToolBarVisible (((MenuItem) e.widget).getSelection ());
		}
	});
	item = new MenuItem (menu, SWT.CHECK);
	item.setText (resExplorer.getString("Statusbar_menuitem"));
	item.setSelection (true);
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {
			setStatusBarVisible (((MenuItem) e.widget).getSelection ());
		}
	});
	new MenuItem (menu, SWT.SEPARATOR);

	// Radio items in the menu
	createDetailsItems (menu);

	// menuItems that come after the radio section

	new MenuItem (menu, SWT.SEPARATOR);
	item = new MenuItem (menu, SWT.CASCADE);
	item.setText (resExplorer.getString("Arrange_menuitem"));
	Menu submenu = new Menu (shell, SWT.DROP_DOWN);
	createSortByMenu (submenu);
	item.setMenu (submenu);
	item = new MenuItem (menu, SWT.PUSH);
	item.setText (resExplorer.getString("Lineup_menuitem"));
	item = new MenuItem (menu, SWT.SEPARATOR);
	String[] strings = {resExplorer.getString("Refresh_menuitem"),
						resExplorer.getString("Options_menuitem")};
	for (int i = 0; i < strings.length; i++) {
		item = new MenuItem(menu, SWT.PUSH);
		item.setText (strings [i]);
	}
	return menu;
}
void defaultSelectTable (SelectionEvent event) {
	TableItem [] items = table.getSelection ();
	if (items.length == 0) return;
	TreeItem item = previousSelectedTreeItem;
	if (item == null) return;
	String path = ((File)(item.getData ())).getAbsolutePath ();
	String fileName = path + File.separator + items [0].getText ();
	if (!Program.launch (fileName)) {	
		MessageBox dialog = new MessageBox (shell, SWT.ICON_ERROR | SWT.OK);
		dialog.setMessage (resExplorer.getString("Failed_launch") + " " + fileName);
		dialog.setText (shell.getText ());
		dialog.open ();
	}
}
void expandTree (TreeEvent event) {
	TreeItem item = (TreeItem) event.item;
	Shell shell = tree.getShell ();
	shell.setCursor (Images.CursorWait);

	// if this is the a or b drive, check if the drive is expandable
	
	// if directory hasn't been viewed yet, remove dummy treeItem then populate the tree
	TreeItem[] items = item.getItems ();
	if (items[0].getText().equals ("?????")) {
		// dispose of dummy items
		for (int i=0; i<items.length; i++) {
			items [i].dispose ();
		}
		// expand the tree item
		expandTree (item);
	} 

	shell.setCursor(null);
}
void expandTree (final TreeItem item) {
	
	File file = (File)item.getData();
	// Get a list of subdirectories
	File[] subFiles = file.listFiles();
	
	
	// Special case: if drive a or b is selected and does not contain a disk
	// then prompt user to retry
	String path = file.getAbsolutePath();
	if (subFiles==null && ( path.equals(DRIVE_A) ||  path.equals(DRIVE_B))) {
		int index = -1;
		if (messageBoxRetry (path) == SWT.RETRY) {
			expandTree(item);
		} else {
			tree.setSelection (new TreeItem [] {previousSelectedTreeItem});
			index = -1;
			for (int i = 0; i < comboContents.length; i++) {
				if (comboContents[i].equals(previousSelectedTreeItem)){
					index = i;
					break;
				}
			}
			if (index != -1)
				combo.select (index);
				
			display.asyncExec(new Runnable() {
				public void run() {
					item.setExpanded(false);
					setExpandable(item);
				}
			});	
						
		}
		return;
	}

	
	sort(subFiles);
		
	// for each of this directory's subdirectories (and files)
	for (int i=0; i<subFiles.length; i++) {
		if (!subFiles[i].isDirectory()) continue;
		
		// add the directory to the tree
		TreeItem newItem = new TreeItem (item, SWT.NULL);
		newItem.setText (subFiles[i].getName());
		newItem.setImage(Images.Folder);
		newItem.setData(subFiles[i]);
			
		// make the directory "expandable"
		setExpandable(newItem);
	}

}

public static void main (String [] args) {
	Explorer example = new Explorer ();
	example.open ();
	example.run ();
	example.close ();
}
void menuFileExit () {
	shell.close ();
}
int messageBoxRetry (String s) {	
	MessageBox box = new MessageBox (shell, SWT.RETRY | SWT.CANCEL | SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
	box.setMessage (s + " " + resExplorer.getString("Unavailable"));
	box.setText (shell.getText ());
	return box.open ();
}
boolean moveFile(File oldFile, File newFile) {
	if (oldFile == null || !oldFile.exists() || newFile.exists())
		return false;
	return oldFile.renameTo(newFile);
}
public void open () {
	createShell ();
	createMenuBar ();
	createToolBar ();
	createStatusLine ();
	createCombo ();
	createTable ();
	createTree (); // creating Tree depends on Table already existing
	createSash ();
	int last = combo.getItemCount() - 1;
	combo.select (last);
	selectCombo();

	shell.addControlListener(new ControlAdapter () {
		public void controlResized (ControlEvent e) {
			shellResized (e);
		}
	});
	shell.open ();
}
void refresh() {
	// update the tree
	TreeItem[] items = tree.getItems();
	for (int i = 0; i < items.length; i++) {
		refresh(items[i]);
	}
	
	// update the table
	TreeItem[] selection = tree.getSelection();
	if (selection.length == 1){
		updateTable((File)selection[0].getData());
	}
}
void refresh(TreeItem item) {

	if (item.getText().equals("?????")) return;
	
	TreeItem[] subItems = item.getItems();
	for (int i = 0; i < subItems.length; i++) {		
		refresh (subItems[i]);
	}
	
	// is this item is collapsed, replace subitems with a dummy item
	// and refresh subitems next time it is expanded
	if (!item.getExpanded()) {	
		for (int i = 0; i < subItems.length; i++) {
			if (subItems[i].equals(previousSelectedTreeItem)) {
				previousSelectedTreeItem = null;
			}
			subItems[i].dispose();
		}
		setExpandable(item);
	} else {
		// compare subitems with real ones
		File file = (File)item.getData();
		File[] subFiles = file.listFiles();
		if (subFiles == null) {
			setExpandable(item);
			item.setExpanded(false);
			return;
		}
		
		sort(subFiles);
		
		// for each of this directory's subdirectories (and files)
		for (int i=0; i<subFiles.length; i++) {
			if (!subFiles[i].isDirectory()) continue;
	
			// is the file in the tree?
			boolean found = false;
			for (int j = 0; j < subItems.length; j++) {		
				Object temp = subItems[j].getData();
				if (subFiles[i].equals(temp)){
					found = true;
					break;
				}
			}

			if (!found) {
				// add the directory to the tree
				TreeItem newItem = new TreeItem (item, SWT.NULL);
				newItem.setText (subFiles[i].getName());
				newItem.setImage(Images.Folder);
				newItem.setData(subFiles[i]);
			
				// make the directory "expandable"
				setExpandable(newItem);

			}
		}
		
		// are there extra files in the tree
		for (int i = 0; i < subItems.length; i++) {
			boolean found = false;
			Object temp = subItems[i].getData();
			for (int j = 0; j < subFiles.length; j++) {		
				if (temp.equals(subFiles[j])){
					found = true;
					break;
				}
			}

			if (!found) {
				if (subItems[i].equals(previousSelectedTreeItem)) {
					previousSelectedTreeItem = null;
				}
				subItems[i].dispose();
			}
		}
	}
		
}
public void run () {
	while (!shell.isDisposed ())
		if (!display.readAndDispatch ()) display.sleep ();
}
//	Called when a drive is selected from the combo box
//  Behavior is supposed to be as close as possible to that of Win 95 Explorer
void selectCombo () {
	// update what the widgets display
	String comboText = combo.getText();

	TreeItem ti = comboContents [combo.getSelectionIndex ()];
	tree.setSelection (new TreeItem [] {ti});
	File file = (File)ti.getData();

	// Special case: if drive a or b is selected and does not contain a disk
	// then prompt user to retry
	String path = file.getAbsolutePath();
	if (! file.exists () && ( path.equals(DRIVE_A) ||  path.equals(DRIVE_B))) {
		if (messageBoxRetry (path) == SWT.RETRY) {
			selectCombo();
		} else {
			tree.setSelection (new TreeItem [] {previousSelectedTreeItem});
			int index = -1;
			for (int i = 0; i < comboContents.length; i++) {
				if (comboContents[i].equals(previousSelectedTreeItem)){
					index = i;
					break;
				}
			}
			if (index != -1)
				combo.select (index);
		}
		return;
	}
		
	setText(file);

	// change tree item images
	if (previousSelectedTreeItem != null && previousSelectedTreeItem.getImage() == Images.FolderOpen) 
		previousSelectedTreeItem.setImage(Images.Folder);	
	
	// update the table and other widgets
	updateTable(file);
	updateNumObjects(file);

	// Update previousSelectedTreeItem
	previousSelectedTreeItem = ti;
}

/**
 * This method selects the parent item in the tree (of the currently selected item)
 * @param e org.eclipse.swt.widgets.Event
 */
void selectParentTreeItem() {
	TreeItem [] items = tree.getSelection();
	if (items.length == 0) return;

	if (items [0].getParentItem() != null ){
		tree.setSelection (new TreeItem [] {items [0].getParentItem()});
	}
}
void selectSash(SelectionEvent event) {
	if (event.detail != SWT.DRAG) {
		sash.setBounds (event.x, event.y, event.width, event.height);
		Rectangle rect = shell.getClientArea ();
		
		Rectangle toolRect = toolBar.getBounds ();
		Point size = allFoldersLabel.getSize ();
		allFoldersLabel.setBounds (0, toolRect.height, event.x, size.y);
		size = contentLabel.getSize ();
		contentLabel.setBounds (event.x + event.width, toolRect.height, rect.width - (event.x + event.width), size.y);

		size = tree.getSize ();
		tree.setSize (event.x, size.y);
		Rectangle bounds = table.getBounds ();
		table.setBounds (event.x + event.width, bounds.y, rect.width - (event.x + event.width), bounds.height);
		return;
	}
	Rectangle rect = shell.getClientArea ();
	event.x = Math.min (Math.max (30, event.x), rect.width - 30);
}
// Update the appropriate widgets when a tree item is selected
void selectTree () {
	TreeItem[] selection = tree.getSelection();
	if (selection.length == 0) return;
	
	TreeItem item =  selection[0];
	File file = (File)item.getData();

	// Special case: if drive a or b is selected and does not contain a disk
	// then prompt user to retry
	String path = file.getAbsolutePath();
	if (!file.exists () && ( path.equals(DRIVE_A) ||  path.equals(DRIVE_B))) {
		if (messageBoxRetry (path) == SWT.RETRY) {
			selectTree ();
		} else {
			tree.setSelection (new TreeItem [] {previousSelectedTreeItem});
			int index = -1;
			for (int i = 0; i < comboContents.length; i++) {
				if (comboContents[i].equals(previousSelectedTreeItem)){
					index = i;
					break;
				}
			}
			if (index != -1)
				combo.select (index);
		}
		return;
	}

	// Update the widgets
	setText (file);
			
	// Update the combo box if a new drive is selected
	String drive = file.getAbsolutePath();
	int end = drive.indexOf(File.separator);
	drive = drive.substring(0, end + 1);
	if (!combo.getText().equals(drive)){
		combo.select (combo.indexOf(drive));
	}

	// Update the contents of the table
	updateTable(file);
	updateNumObjects(file);

	// Update the images of the tree items
	if (item.getImage() == Images.Folder) item.setImage(Images.FolderOpen);
	if (previousSelectedTreeItem != null && previousSelectedTreeItem.getImage() == Images.FolderOpen) previousSelectedTreeItem.setImage(Images.Folder);
		
	// Update previousSelectedTreeItem
	previousSelectedTreeItem = item;
}
/**
 * Insert the method's description here.
 * @param file java.io.File
 */
void setExpandable(TreeItem treeItem) {
	File file = (File)treeItem.getData();
	String path = file.getAbsolutePath();
	if (path.equals(DRIVE_A) || path.equals(DRIVE_B) || (file.exists() && file.list() != null) ) {
		// if the tree item doesn't have a "+", then add a dummy item
		if (treeItem.getItems().length == 0) {
			TreeItem newItem2 = new TreeItem (treeItem, SWT.NULL);
			newItem2.setText ("?????");
		}
	}
	
}

/**
 * Insert the method's description here.
 * @param s java.lang.String
 */
void setText(File file) {
	String name = null;
	if (file != null) {
		name = file.getName();
		if (name.length() == 0) {
			name = file.getPath();
		}
	}
	shell.setText (resExplorer.getString("Title") + " " + name);	

	name = file.getPath();
	if (!file.isDirectory()) name = file.getParent();
	contentLabel.setText(resExplorer.getString("Content_of")+" '"+name+"'");	
	
	long folderSize = file.length();
	long diskFreeSize = 0;
	diskSpaceLabel.setText(folderSize+" "+resExplorer.getString("Filesize_1")+" "+diskFreeSize+resExplorer.getString("Filesize_2"));
	
}
void setStatusBarVisible(boolean show) {
}
/**
 * Insert method's description here.
 * @param show boolean
 */
void setToolBarVisible(boolean show) {
}
void shellResized (ControlEvent event) {
	Rectangle rect = shell.getClientArea ();
	Point comboSize = combo.computeSize (combo.getSize ().x, SWT.DEFAULT);
	Point toolBarSize = toolBar.computeSize (rect.width, SWT.DEFAULT);
	int toolHeight = Math.max (comboSize.y, toolBarSize.y) + 2;
	toolBar.setBounds (0, 0, rect.width, toolHeight);

	Rectangle sashBounds = sash.getBounds ();
	int sashX = sashBounds.x, sashWidth = sashBounds.width;

	Point size = numObjectsLabel.getSize ();
	allFoldersLabel.setBounds (0, toolHeight, sashX, size.y);
	contentLabel.setBounds (sashX + sashWidth, toolHeight, rect.width - (sashX + sashWidth), size.y);

	sash.setBounds (sashX, toolHeight, sashWidth, rect.height - toolHeight - size.y);	
	tree.setBounds (0, toolHeight + size.y, sashX, rect.height - toolHeight - (size.y * 2));
	table.setBounds (sashX + sashWidth, toolHeight + size.y, rect.width - (sashX + sashWidth), rect.height - toolHeight - (size.y * 2));
	
	numObjectsLabel.setBounds (0, rect.height - size.y, size.x, size.y);
	diskSpaceLabel.setBounds (size.x, rect.height - size.y, rect.width - size.x, size.y);
}
void sort (File[] files) {
	boolean done;
	do {
	    done = true;
	    for (int i=files.length-1; i>0; i--) {
			if (cancel) return;
			if (files [i-1].getName().toUpperCase ().compareTo (files [i].getName().toUpperCase ()) > 0) {
			    File temp = files [i]; 
			    files [i] = files [i-1]; 
			    files [i-1] = temp;
			    done = false;
			}
	    }
	} while (!done);
	return;
}
/**
 * This method adjusts the contents of the label
 * @param numElements int
 */
void updateNumObjects(File file) {
	String[] items = file.list();
	int count = 0;
	if (items != null) {
		count = items.length;
	}
	numObjectsLabel.setText (count+" "+resExplorer.getString("Objects"));
}

void updateTable (final File root) {
	if (tableThread != null) {
		cancel = true;
		while (cancel) {
			while (display.readAndDispatch ());
		}
	}
	if (!root.exists ()) return;
		
	cancel = false;
	tableThread = new Thread (new Runnable () {
		public void run () {
			display.syncExec (new Runnable () {
				public void run () {
					table.setRedraw (false);
					table.removeAll ();
					table.setData(root);
				}
			});
			File [] list = root.listFiles ();
			if (list == null) list = new File [0];
			sort (list);
			for (int i=0; i<list.length; i++) {
				if (cancel) break;
				final File file = list[i];
				/*
				* (the format of the date should be modified, using something similar to:
				* parsing the date: g.MONTH+1+"/"+g.DAY_OF_MONTH+"/"+g.YEAR+" ||| "+
				* Note: Deprecated: date.toLocaleString()
				*/
				Image image = null;
				String [] strings = null;
				String date = new Date(file.lastModified()).toLocaleString();
				if (list[i].isDirectory ()) {
					strings = new String [] {file.getName(), "", resExplorer.getString("File_folder"), date};
					image = Images.Folder;
				} else {
					strings = new String [] {file.getName(),
											 file.length() / 1024 + 1 + " " + resExplorer.getString("KB"),
											 resExplorer.getString("System_file"),
											 date};
					image = Images.File;
				}
				final Image finalImage = image;
				final String [] finalStrings = strings;
				display.syncExec (new Runnable () {
					public void run () {
						TableItem tableItem = new TableItem(table, 0);
						tableItem.setText(finalStrings);
						tableItem.setImage(finalImage);
						tableItem.setData(file);
					}
				});
			}
			display.syncExec (new Runnable () {
				public void run () {
					table.setRedraw (true);
				}
			});
			tableThread = null;
			cancel = false;
		}
	});
	tableThread.start ();
}

}
