package org.eclipse.swt.examples.fileviewer;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.dnd.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.program.*;
import org.eclipse.swt.widgets.*;

import java.io.*;
import java.text.*;
import java.util.*;
/**
 * File Viewer example
 */
public class FileViewer { 
 	private static ResourceBundle resourceBundle;
	private final static String DRIVE_A = "a:" + File.separator;
	private final static String DRIVE_B = "b:" + File.separator;

	/* UI elements */ 	  
 	private Display display;
	private Shell   shell;
	private ToolBar toolBar;

	private Label numObjectsLabel;
	private Label diskSpaceLabel;
	
	private File currentDirectory = null;

	/* Combo view */
	private static final String COMBODATA_ROOTS = "Combo.roots";
		// File[]: Array of files whose paths are currently displayed in the combo
	private static final String COMBODATA_LASTTEXT = "Combo.lastText";
		// String: Previous selection text string

	private Combo combo;

	/* Tree view */
	private static final String TREEITEMDATA_FILE = "TreeItem.file";
		// File: File associated with tree item
	private static final String TREEITEMDATA_IMAGEEXPANDED = "TreeItem.imageExpanded";
		// Image: shown when item is expanded
	private static final String TREEITEMDATA_IMAGECOLLAPSED = "TreeItem.imageCollapsed";
		// Image: shown when item is collapsed
	private static final String TREEITEMDATA_STUB = "TreeItem.stub";
		// Object: if not present or null then the item has not been populated

	private Tree tree;
	private Label treeScopeLabel;

	/* Table view */
	private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(
		DateFormat.MEDIUM, DateFormat.MEDIUM);
	private static final String TABLEITEMDATA_FILE = "TableItem.file";
		// File: File associated with table row
	private static final String TABLEDATA_DIR = "Table.dir";
		// File: Currently visible directory
	private static final int[] tableWidths = new int[] {150, 60, 75, 150};
	private final String[] tableTitles = new String [] {
		FileViewer.getResourceString("table.Name.title"),
		FileViewer.getResourceString("table.Size.title"),
		FileViewer.getResourceString("table.Type.title"),
		FileViewer.getResourceString("table.Modified.title")
	};
	private Table table;
	private Label tableContentsOfLabel;

	/* Table update worker */
	// Control data
	private final Object workerLock = new Object();
		// Lock for all worker control data and state
	private volatile Thread  workerThread = null;
		// The worker's thread
	private volatile boolean workerStopped = false;
		// True if the worker must exit on completion of the current cycle
	private volatile boolean workerCancelled = false;
		// True if the worker must cancel its operations prematurely perhaps due to a state update

	// Worker state information -- this is what gets synchronized by an update
	private volatile File workerStateDir = null;

	// State information to use for the next cycle
	private volatile File workerNextDir = null;

	/* Simulate only flag */
	// when true, disables actual filesystem manipulations and outputs results to standard out
	private boolean simulateOnly = true;

	/**
	 * Runs main program.
	 */
	public static void main (String [] args) {
		resourceBundle = ResourceBundle.getBundle("examples_fileviewer");
		new FileViewer().open();
	}
	
	/**
	 * Opens the main program.
	 */
	void open() {		
		// Create the window
		display = new Display();
		IconCache.initResources(display);
		shell = new Shell();
		createShellContents();
		notifyRefreshFiles(null);
		shell.open();
		// Event loop
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
		// Cleanup
		workerStop();
		IconCache.freeResources();
		display.dispose();
	}
	/**
	 * Closes the main program.
	 */
	void close() {
		shell.close();
	}
	
	/**
	 * Returns a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	static String getResourceString(String key) {
		try {
			return resourceBundle.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}			
	}

	/**
	 * Returns a string from the resource bundle and binds it
	 * with the given arguments. If the key is not found,
	 * return the key.
	 */
	static String getResourceString(String key, Object[] args) {
		try {
			return MessageFormat.format(getResourceString(key), args);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";
		}
	}

	/**
	 * Construct the UI
	 * 
	 * @param container the ShellContainer managing the Shell we are rendering inside
	 */
	private void createShellContents() {
		shell.setText(getResourceString("Title", new Object[] { "" }));	
		shell.setImage(IconCache.stockImages[IconCache.shellIcon]);
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		createFileMenu(bar);
		createHelpMenu(bar);

		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 3;
		gridLayout.marginHeight = gridLayout.marginWidth = 0;
		shell.setLayout(gridLayout);

		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.widthHint = 185;
		createComboView(shell, gridData);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		createToolBar(shell, gridData);

		SashForm sashForm = new SashForm(shell, SWT.NONE);
		sashForm.setOrientation(SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 3;
		sashForm.setLayoutData(gridData);
		createTreeView(sashForm);
		createTableView(sashForm);
		sashForm.setWeights(new int[] { 2, 5 });

		numObjectsLabel = new Label(shell, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		gridData.widthHint = 185;
		numObjectsLabel.setLayoutData(gridData);
		
		diskSpaceLabel = new Label(shell, SWT.BORDER);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		diskSpaceLabel.setLayoutData(gridData);
	}
	
	/**
	 * Creates the File Menu.
	 * 
	 * @param parent the parent menu
	 */
	private void createFileMenu(Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(getResourceString("menu.File.text"));
		header.setMenu(menu);

		final MenuItem simulateItem = new MenuItem(menu, SWT.CHECK);
		simulateItem.setText(getResourceString("menu.File.SimulateOnly.text"));
		simulateItem.setSelection(simulateOnly);
		simulateItem.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				simulateOnly = simulateItem.getSelection();
			}
		});

		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(getResourceString("menu.File.Close.text"));
		item.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				close();
			}
		});
	}
	
	/**
	 * Creates the Help Menu.
	 * 
	 * @param parent the parent menu
	 */
	private void createHelpMenu(Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(getResourceString("menu.Help.text"));
		header.setMenu(menu);

		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(getResourceString("menu.Help.About.text"));		
		item.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				box.setText(getResourceString("dialog.About.title"));
				box.setMessage(getResourceString("dialog.About.description",
					new Object[] { System.getProperty("os.name") }));
				box.open();
			}
		});
	}

	/**
	 * Creates the toolbar
	 * 
	 * @param shell the shell on which to attach the toolbar
	 * @param layoutData the layout data
	 */
	private void createToolBar(final Shell shell, Object layoutData) {
		toolBar = new ToolBar(shell, SWT.NULL);
		toolBar.setLayoutData(layoutData);
		ToolItem item = new ToolItem(toolBar, SWT.SEPARATOR);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdParent]);
		item.setToolTipText(getResourceString("tool.Parent.tiptext"));
		item.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				doParent();
			}
		});
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdRefresh]);
		item.setToolTipText(getResourceString("tool.Refresh.tiptext"));
		item.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				doRefresh();
			}
		});
		SelectionAdapter unimplementedListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				box.setText(getResourceString("dialog.NotImplemented.title"));
				box.setMessage(getResourceString("dialog.ActionNotImplemented.description"));
				box.open();
			}
		};

		item = new ToolItem(toolBar, SWT.SEPARATOR);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdCut]);
		item.setToolTipText(getResourceString("tool.Cut.tiptext"));
		item.addSelectionListener(unimplementedListener);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdCopy]);
		item.setToolTipText(getResourceString("tool.Copy.tiptext"));
		item.addSelectionListener(unimplementedListener);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdPaste]);
		item.setToolTipText(getResourceString("tool.Paste.tiptext"));		item.addSelectionListener(unimplementedListener);

		item = new ToolItem(toolBar, SWT.SEPARATOR);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdDelete]);
		item.setToolTipText(getResourceString("tool.Delete.tiptext"));
		item.addSelectionListener(unimplementedListener);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdRename]);
		item.setToolTipText(getResourceString("tool.Rename.tiptext"));
		item.addSelectionListener(unimplementedListener);

		item = new ToolItem(toolBar, SWT.SEPARATOR);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdSearch]);
		item.setToolTipText(getResourceString("tool.Search.tiptext"));
		item.addSelectionListener(unimplementedListener);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(IconCache.stockImages[IconCache.cmdPrint]);
		item.setToolTipText(getResourceString("tool.Print.tiptext"));
		item.addSelectionListener(unimplementedListener);
	}

	/**
	 * Creates the combo box view.
	 * 
	 * @param parent the parent control
	 */
	private void createComboView(Composite parent, Object layoutData) {
		combo = new Combo(parent, SWT.NONE);
		combo.setLayoutData(layoutData);
		combo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				final File[] roots = (File[]) combo.getData(COMBODATA_ROOTS);
				if (roots == null) return;
				int selection = combo.getSelectionIndex();
				if (selection >= 0 && selection < roots.length) {
					notifySelectedDirectory(roots[selection]);
				}
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				final String lastText = (String) combo.getData(COMBODATA_LASTTEXT);
				String text = combo.getText();
				if (text == null) return;
				if (lastText != null && lastText.equals(text)) return;
				combo.setData(COMBODATA_LASTTEXT, text);
				notifySelectedDirectory(new File(text));
			}
		});
	}

	/**
	 * Creates the file tree view.
	 * 
	 * @param parent the parent control
	 */
	private void createTreeView(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 2;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);

		treeScopeLabel = new Label(composite, SWT.BORDER);
		treeScopeLabel.setText(FileViewer.getResourceString("details.AllFolders.text"));
		treeScopeLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));

		tree = new Tree(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		tree.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

		tree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				final TreeItem[] selection = tree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
					File file = (File) item.getData(TREEITEMDATA_FILE);
				
					notifySelectedDirectory(file);
				}
			}
			public void widgetDefaultSelected(SelectionEvent event) {
				final TreeItem[] selection = tree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
					item.setExpanded(true);
					treeExpandItem(item);
				}
			}
		});
		tree.addTreeListener(new TreeAdapter() {
			public void treeExpanded(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				final Image image = (Image) item.getData(TREEITEMDATA_IMAGEEXPANDED);
				if (image != null) item.setImage(image);
				treeExpandItem(item);
			}
			public void treeCollapsed(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				final Image image = (Image) item.getData(TREEITEMDATA_IMAGECOLLAPSED);
				if (image != null) item.setImage(image);
			}
		});
		createTreeDragSource(tree);
		createTreeDropTarget(tree);
	}

	/**
	 * Creates the Drag & Drop DragSource for items being dragged from the tree.
	 * 
	 * @return the DragSource for the tree
	 */
	private DragSource createTreeDragSource(final Tree tree){
		DragSource dragSource = new DragSource(tree, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceListener() {
			TreeItem[] dndSelection = null;
			String[] sourceNames = null;
			public void dragStart(DragSourceEvent event){
				dndSelection = tree.getSelection();
				sourceNames = null;
				event.doit = dndSelection.length > 0;
			}
			public void dragFinished(DragSourceEvent event){
				dragSourceHandleDragFinished(event, sourceNames);
				dndSelection = null;
				sourceNames = null;
			}
			public void dragSetData(DragSourceEvent event){
				if (dndSelection == null || dndSelection.length == 0) return;
				if (! FileTransfer.getInstance().isSupportedType(event.dataType)) return;
				
				sourceNames  = new String[dndSelection.length];
				for (int i = 0; i < dndSelection.length; i++) {
					File file = (File) dndSelection[i].getData(TREEITEMDATA_FILE);
					sourceNames[i] = file.getAbsolutePath();
				}
				event.data = sourceNames;
			}
		});
		return dragSource;
	}

	/**
	 * Creates the Drag & Drop DropTarget for items being dropped onto the tree.
	 * 
	 * @return the DropTarget for the tree
	 */
	private DropTarget createTreeDropTarget(final Tree tree) {
		DropTarget dropTarget = new DropTarget(tree, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dropTarget.addDropListener(new TreeDropFeedbackListener(tree));
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void dragOver(DropTargetEvent event) {
				dropTargetValidate(event, getTargetFile(event));
			}
			public void dropAccept(DropTargetEvent event) {
				dropTargetValidate(event, getTargetFile(event));
			}
			public void drop(DropTargetEvent event) {
				if (dropTargetValidate(event, getTargetFile(event)))
					dropTargetHandleDrop(event, getTargetFile(event));
			}				
			private File getTargetFile(DropTargetEvent event) {
				// Determine the target File for the drop 
				final TreeItem item = tree.getItem(tree.toControl(new Point(event.x, event.y)));
				final File targetFile;
				if (item == null) {
					// We dropped on an unoccupied area of the tree, we have no recourse. Quit.
					targetFile = null;
				} else {
					// We dropped on a particular item in the tree, use the item's file
					targetFile = (File) item.getData(TREEITEMDATA_FILE);
				}
				return targetFile;
			}
		});
		return dropTarget;	
	}

	/**
	 * Handles expand events on a tree item.
	 * 
	 * @param item the TreeItem to fill in
	 */
	private void treeExpandItem(TreeItem item) {
		final File file = (File) item.getData(TREEITEMDATA_FILE);
		final Object stub = item.getData(TREEITEMDATA_STUB);
		if (stub == null) treeRebuildItem(item, file);
	}
	
	/**
	 * Populates an item in the tree with a complete directory listing.
	 * 
	 * @param item the TreeItem to fill in
	 * @param file the directory to use
	 */
	private void treeRebuildItem(TreeItem item, File file) {
		/* Get directory listing */
		shell.setCursor(IconCache.stockCursors[IconCache.cursorWait]);
		File[] subFiles = null;
		if (file != null) {
			subFiles = FileViewer.getDirectoryList(file);
		}
		
		/* Eliminate any existing (possibly placeholder) children */
		final TreeItem[] oldChildren = item.getItems();
		for (int i = 0; i < oldChildren.length; ++i) {
			oldChildren[i].dispose();
		}

		if (subFiles != null && subFiles.length > 0) {
			/* Add subdirectory entries */
			for (int i = 0; i < subFiles.length; ++i) {
				final File folder = subFiles[i];
				if (! folder.isDirectory()) continue;
				
				// add the directory to the tree
				TreeItem newItem = new TreeItem(item, SWT.NULL);
				treeInitFolder(newItem, folder);
				
				// add a placeholder child item so we get the "expand" button
				TreeItem placeholderItem = new TreeItem(newItem, SWT.NULL);
			}
		} else {
			/* Error or nothing found -- collapse the item */
			item.setExpanded(false);
		}		

		// Clear stub flag
		item.setData(TREEITEMDATA_STUB, this);
		shell.setCursor(IconCache.stockCursors[IconCache.cursorDefault]);
	}

	/**
	 * Initializes a folder item.
	 * 
	 * @param item the TreeItem to initialize
	 * @param folder the File associated with this TreeItem
	 */
	private void treeInitFolder(TreeItem item, File folder) {
		item.setText(folder.getName());
		item.setImage(IconCache.stockImages[IconCache.iconClosedFolder]);
		item.setData(TREEITEMDATA_FILE, folder);
		item.setData(TREEITEMDATA_IMAGEEXPANDED, IconCache.stockImages[IconCache.iconOpenFolder]);
		item.setData(TREEITEMDATA_IMAGECOLLAPSED, IconCache.stockImages[IconCache.iconClosedFolder]);
	}

	/**
	 * Initializes a volume item.
	 * 
	 * @param item the TreeItem to initialize
	 * @param volume the File associated with this TreeItem
	 */
	private void treeInitVolume(TreeItem item, File volume) {
		item.setText(volume.getPath());
		item.setImage(IconCache.stockImages[IconCache.iconClosedDrive]);
		item.setData(TREEITEMDATA_FILE, volume);
		item.setData(TREEITEMDATA_IMAGEEXPANDED, IconCache.stockImages[IconCache.iconOpenDrive]);
		item.setData(TREEITEMDATA_IMAGECOLLAPSED, IconCache.stockImages[IconCache.iconClosedDrive]);
	}

	/**
	 * Creates the file details table.
	 * 
	 * @param parent the parent control
	 */
	private void createTableView(Composite parent) {
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 1;
		gridLayout.marginHeight = gridLayout.marginWidth = 2;
		gridLayout.horizontalSpacing = gridLayout.verticalSpacing = 0;
		composite.setLayout(gridLayout);
		tableContentsOfLabel = new Label(composite, SWT.BORDER);
		tableContentsOfLabel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_FILL));

		table = new Table(composite, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.MULTI | SWT.FULL_SELECTION);
		table.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL));

		for (int i = 0; i < tableTitles.length; ++i) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(tableTitles[i]);
			column.setWidth(tableWidths[i]);
		}
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				notifySelectedFiles(getSelectedFiles());
			}
			public void widgetDefaultSelected(SelectionEvent event) {
				doDefaultFileAction(getSelectedFiles());
			}
			private File[] getSelectedFiles() {
				final TableItem[] items = table.getSelection();
				final File[] files = new File[items.length];
				
				for (int i = 0; i < items.length; ++i) {
					files[i] = (File) items[i].getData(TABLEITEMDATA_FILE);
				}
				return files;
			}
		});

		createTableDragSource(table);
		createTableDropTarget(table);
	}

	/**
	 * Creates the Drag & Drop DragSource for items being dragged from the table.
	 * 
	 * @return the DragSource for the table
	 */
	private DragSource createTableDragSource(final Table table) {
		DragSource dragSource = new DragSource(table, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceListener() {
			TableItem[] dndSelection = null;
			String[] sourceNames = null;
			public void dragStart(DragSourceEvent event){
				dndSelection = table.getSelection();
				sourceNames = null;
				event.doit = dndSelection.length > 0;
			}
			public void dragFinished(DragSourceEvent event){
				dragSourceHandleDragFinished(event, sourceNames);
				dndSelection = null;
				sourceNames = null;
			}
			public void dragSetData(DragSourceEvent event){
				if (dndSelection == null || dndSelection.length == 0) return;
				if (! FileTransfer.getInstance().isSupportedType(event.dataType)) return;
				
				sourceNames  = new String[dndSelection.length];
				for (int i = 0; i < dndSelection.length; i++) {
					File file = (File) dndSelection[i].getData(TABLEITEMDATA_FILE);
					sourceNames[i] = file.getAbsolutePath();
				}
				event.data = sourceNames;
			}
		});
		return dragSource;
	}

	/**
	 * Creates the Drag & Drop DropTarget for items being dropped onto the table.
	 * 
	 * @return the DropTarget for the table
	 */
	private DropTarget createTableDropTarget(final Table table){
		DropTarget dropTarget = new DropTarget(table, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void dragOver(DropTargetEvent event) {
				dropTargetValidate(event, getTargetFile(event));
			}
			public void dropAccept(DropTargetEvent event) {
				dropTargetValidate(event, getTargetFile(event));
			}
			public void drop(DropTargetEvent event) {
				if (dropTargetValidate(event, getTargetFile(event)))
					dropTargetHandleDrop(event, getTargetFile(event));
			}				
			private File getTargetFile(DropTargetEvent event) {
				// Determine the target File for the drop 
				final TableItem item = table.getItem(table.toControl(new Point(event.x, event.y)));
				final File targetFile;
				if (item == null) {
					// We dropped on an unoccupied area of the table, use the table's root file
					targetFile = (File) table.getData(TABLEDATA_DIR);
				} else {
					// We dropped on a particular item in the table, use the item's file
					targetFile = (File) item.getData(TABLEITEMDATA_FILE);
				}
				return targetFile;
			}
		});
		return dropTarget;
	}

	/**
	 * Notifies the application components that a new current directory has been selected
	 * 
	 * @param dir the directory that was selected, null is ignored
	 */
	void notifySelectedDirectory(File dir) {
		if (dir == null) return;
		if (currentDirectory != null && dir.equals(currentDirectory)) return;
		currentDirectory = dir;
		notifySelectedFiles(null);
		
		/* Shell:
		 * Sets the title to indicate the selected directory
		 */
		shell.setText(getResourceString("Title", new Object[] { currentDirectory.getPath() }));
		/* Table view:
		 * Displays the contents of the selected directory.
		 */
		workerUpdate(dir, false);

		/* Combo view:
		 * Sets the combo box to point to the selected directory.
		 */
		final File[] comboRoots = (File[]) combo.getData(COMBODATA_ROOTS);
		int comboEntry = -1;
		if (comboRoots != null) {		
			for (int i = 0; i < comboRoots.length; ++i) {
				if (dir.equals(comboRoots[i])) {
					comboEntry = i;
					break;
				}
			}
		}
		if (comboEntry == -1) combo.setText(dir.getPath());
		else combo.select(comboEntry);

		/* Tree view:
		 * If not already expanded, recursively expands the parents of the specified
		 * directory until it is visible.
		 */
		Vector /* of File */ path = new Vector();
		// Build a stack of paths from the root of the tree
		while (dir != null) {
			path.add(dir);
			dir = dir.getParentFile();
		}
		// Recursively expand the tree to get to the specified directory
		TreeItem[] items = tree.getItems();
		TreeItem lastItem = null;
		for (int i = path.size() - 1; i >= 0; --i) {
			final File pathElement = (File) path.elementAt(i);

			// Search for a particular File in the array of tree items
			// No guarantee that the items are sorted in any recognizable fashion, so we'll
			// just sequential scan.  There shouldn't be more than a few thousand entries.
			TreeItem item = null;
			for (int k = 0; k < items.length; ++k) {
				item = items[k];
				if (item.isDisposed()) continue;
				final File itemFile = (File) item.getData(TREEITEMDATA_FILE);
				if (itemFile != null && itemFile.equals(pathElement)) break;
			}
			if (item == null) break;
			lastItem = item;
			if (i != 0 && !item.getExpanded()) {
				treeExpandItem(item);
				item.setExpanded(true);
			}
			items = item.getItems();
		}
		tree.setSelection((lastItem != null) ? new TreeItem[] { lastItem } : new TreeItem[0]);
	}
	
	/**
	 * Notifies the application components that files have been selected
	 * 
	 * @param files the files that were selected, null or empty array indicates no active selection
	 */
	void notifySelectedFiles(File[] files) {
		/* Details:
		 * Update the details that are visible on screen.
		 */
		if ((files != null) && (files.length != 0)) {
			numObjectsLabel.setText(getResourceString("details.NumberOfSelectedFiles.text",
				new Object[] { new Integer(files.length) }));
			long fileSize = 0L;
			for (int i = 0; i < files.length; ++i) {
				fileSize += files[i].length();
			}
			diskSpaceLabel.setText(getResourceString("details.FileSize.text",
				new Object[] { new Long(fileSize) }));
		} else {
			// No files selected
			diskSpaceLabel.setText("");
			if (currentDirectory != null) {
				int numObjects = getDirectoryList(currentDirectory).length;
				numObjectsLabel.setText(getResourceString("details.DirNumberOfObjects.text",
					new Object[] { new Integer(numObjects) }));
			} else {
				numObjectsLabel.setText("");
			}
		}
	}

	/**
	 * Notifies the application components that files must be refreshed
	 * 
	 * @param files the files that need refreshing, empty array is a no-op, null refreshes all
	 */
	void notifyRefreshFiles(File[] files) {
		if (files != null && files.length == 0) return;

		/* Table view:
		 * Refreshes information about any files in the list and their children.
		 */
		workerUpdate(currentDirectory, true);

		final File[] roots = getRoots();

		/* Combo view:
		 * Refreshes the list of roots
		 */
		combo.removeAll();
		combo.setData(COMBODATA_ROOTS, roots);
		for (int i = 0; i < roots.length; ++i) {
			final File file = roots[i];
			combo.add(file.getPath());
		}

		/* Tree view:
		 * Refreshes information about any files in the list and their children.
		 */
		tree.removeAll();
		for (int i = 0; i < roots.length; ++i) {
			final File file = roots[i];
			TreeItem item = new TreeItem(tree, SWT.NULL);
			treeInitVolume(item, file);

			// add a placeholder child item so we get the "expand" button
			TreeItem placeholderItem = new TreeItem(item, SWT.NULL);
		}
		
		// Remind everyone where we are in the filesystem
		final File dir = currentDirectory;
		currentDirectory = null;
		notifySelectedDirectory(dir);
	}

	/**
	 * Performs the default action on a set of files.
	 * 
	 * @param files the array of files to process
	 */
	void doDefaultFileAction(File[] files) {
		// only uses the 1st file (for now)
		if (files.length == 0) return;
		final File file = files[0];

		if (file.isDirectory()) {
			notifySelectedDirectory(file);
		} else {
			final String fileName = file.getAbsolutePath();
			if (! Program.launch(fileName)) {	
				MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				dialog.setMessage(getResourceString("error.FailedLaunch.message", new Object[] { fileName }));
				dialog.setText(shell.getText ());
				dialog.open();
			}
		}
	}

	/**
	 * Navigates to the parent directory
	 */
	void doParent() {
		if (currentDirectory == null) return;
		File parentDirectory = currentDirectory.getParentFile();
		notifySelectedDirectory(parentDirectory);
	}
	 
	/**
	 * Performs a refresh
	 */
	void doRefresh() {
		notifyRefreshFiles(null);
	}

	/**
	 * Validates a drop target as a candidate for a drop operation.
	 * <p>
	 * Used in dragOver() and dropAccept().<br>
	 * Note event.detail is set to DND.DROP_NONE by this method if the target is not valid.
	 * </p>
	 * @param event the DropTargetEvent to validate
	 * @param targetFile the File representing the drop target location
	 *        under inspection, or null if none
	 */
	private boolean dropTargetValidate(DropTargetEvent event, File targetFile) {
		if (targetFile != null && targetFile.isDirectory()) {
			if (event.detail != DND.DROP_COPY && event.detail != DND.DROP_MOVE) {
				event.detail = DND.DROP_MOVE;
			}
		} else {
			event.detail = DND.DROP_NONE;
		}
		return event.detail != DND.DROP_NONE;
	}

	/**
	 * Handles a drop on a dropTarget.
	 * <p>
	 * Used in drop().<br>
	 * Note event.detail is modified by this method
	 * </p>
	 * @param event the DropTargetEvent passed as parameter to the drop() method
	 * @param targetFile the File representing the drop target location
	 *        under inspection, or null if none
	 */
	private void dropTargetHandleDrop(DropTargetEvent event, File targetFile) {
		// Get dropped data (an array of filenames)
		if (! dropTargetValidate(event, targetFile)) return;
		final String[] sourceNames = (String[]) event.data;
		if (sourceNames == null) event.detail = DND.DROP_NONE;
		if (event.detail == DND.DROP_NONE) return;

		for (int i = 0; i < sourceNames.length; i++){
			final File source = new File(sourceNames[i]);
			final File dest = new File(targetFile, source.getName());
	
			// Copy each file
			if (! copyFileStructure(source, dest)) {
				event.detail = DND.DROP_NONE; // forbid the source from deleting files on us
				MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				box.setText(getResourceString("dialog.FailedCopy.title"));
				box.setMessage(getResourceString("dialog.FailedCopy.description",
					new Object[] { source, dest }));
				box.open();
				break;
			}
		}
		notifyRefreshFiles(new File[] { targetFile });
	}

	/**
	 * Handles the completion of a drag on a dragSource.
	 * <p>
	 * Used in dragFinished().<br>
	 * </p>
	 * @param event the DragSourceEvent passed as parameter to the dragFinished() method
	 * @param sourceNames the names of the files that were dragged (event.data is inadequate)
	 */
	private void dragSourceHandleDragFinished(DragSourceEvent event, String[] sourceNames) {
		if (sourceNames == null) return;
		if (event.detail != DND.DROP_MOVE) return;

		Vector /* of File */ dirtyFiles = new Vector();
		for (int i = 0; i < sourceNames.length; i++){
			final File source = new File(sourceNames[i]);
			dirtyFiles.add(source);
	
			// Delete each file
			if (! deleteFileStructure(source)) {
				MessageBox box = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
				box.setText(getResourceString("dialog.FailedDelete.title"));
				box.setMessage(getResourceString("dialog.FailedDelete.description",
					new Object[] { source }));
				box.open();
				break;
			}
		}
		notifyRefreshFiles((File[]) dirtyFiles.toArray(new File[dirtyFiles.size()]));
	}

	/**
	 * Gets filesystem root entries
	 * 
	 * @return an array of Files corresponding to the root directories on the platform,
	 *         may be empty but not null
	 */
	static File[] getRoots() {
		/*
		 * On JDK 1.22 only...
		 */
		// return File.listRoots();

		/*
		 * On JDK 1.1.7 and beyond...
		 * -- PORTABILITY ISSUES HERE --
		 */
		if (System.getProperty ("os.name").indexOf ("Windows") != -1) {
			Vector /* of File */ list = new Vector();
			list.add(new File(DRIVE_A));
			list.add(new File(DRIVE_B));
			for (char i = 'c'; i <= 'z'; ++i) {
				File drive = new File(i + ":" + File.separator);
				if (drive.isDirectory() && drive.exists()) {
					list.add(drive);
				}
			}
			File[] roots = (File[]) list.toArray(new File[list.size()]);
			sortFiles(roots);
			return roots;
		} else {
			return new File[] { new File(File.separator) };
		}
	}

	/**
	 * Gets a directory listing
	 * 
	 * @param file the directory to be listed
	 * @return an array of files this directory contains, may be empty but not null
	 */
	static File[] getDirectoryList(File file) {
		File[] list = file.listFiles();
		if (list == null) return new File[0];
		sortFiles(list);
		return list;
	}
	
	/**
	 * Copies a file or entire directory structure.
	 * 
	 * @param oldFile the location of the old file or directory
	 * @param newFile the location of the new file or directory
	 * @return true iff the operation succeeds without errors
	 */
	boolean copyFileStructure(File oldFile, File newFile) {
		if (oldFile == null || newFile == null) return false;
		
		// ensure that newFile is not a child of oldFile
		File searchFile = newFile;
		do {
			if (oldFile.equals(searchFile)) return false;
			searchFile = searchFile.getParentFile();
		} while (searchFile != null);
		
		if (oldFile.isFile()) {
			/*
			 * Copy a file
			 */
			if (simulateOnly) {
				System.out.println(getResourceString("simulate.CopyFromTo.text",
					new Object[] { oldFile.getPath(), newFile.getPath() }));
			} else {
				FileReader in = null;
				FileWriter out = null;
				try {
					in = new FileReader(oldFile);
					out = new FileWriter(newFile);
			
					int count;
					while ((count = in.read()) != -1) out.write(count);
				} catch (FileNotFoundException e) {
					return false;
				} catch (IOException e) {
					return false;
				} finally {
					try {
						if (in != null) in.close();
						if (out != null) out.close();
					} catch (IOException e) {
						return false;
					}
				}
			}
			return true;
		} else if (oldFile.isDirectory()) {
			/*
			 * Copy a directory
			 */
			if (simulateOnly) {
				System.out.println(getResourceString("simulate.DirectoriesCreated.text",
					new Object[] { newFile.getPath() }));
			} else {
				if (! newFile.mkdirs()) return false;
			}
			File[] subFiles = oldFile.listFiles();
			if (subFiles != null) {
				for (int i = 0; i < subFiles.length; i++) {
					File oldSubFile = subFiles[i];
					File newSubFile = new File(newFile, oldSubFile.getName());
					if (! copyFileStructure(oldSubFile, newSubFile)) return false;
				}
			}
			return true;
		} else {
			/*
			 * Unknown type
			 */
			if (simulateOnly) {
				System.out.println(getResourceString("simulate.UnknownResource.text",
					new Object[] { oldFile.getPath() }));
			}
			return false; // error we don't know how to copy this
		}
	}

	/**
	 * Deletes a file or entire directory structure.
	 * 
	 * @param oldFile the location of the old file or directory
	 * @return true iff the operation succeeds without errors
	 */
	boolean deleteFileStructure(File oldFile) {
		if (oldFile == null) return false;		
		if (oldFile.isDirectory()) {
			/*
			 * Delete a directory
			 */
			File[] subFiles = oldFile.listFiles();
			if (subFiles != null) {
				for (int i = 0; i < subFiles.length; i++) {
					File oldSubFile = subFiles[i];
					if (! deleteFileStructure(oldSubFile)) return false;
				}
			}
		}
		if (simulateOnly) {
			System.out.println(getResourceString("simulate.Delete.text",
				new Object[] { oldFile.getPath(), oldFile.getPath() }));
			return true;
		} else {
			return oldFile.delete();
		}
	}
	
	/**
	 * Sorts files lexicographically by name.
	 * 
	 * @param files the array of Files to be sorted
	 */
	static void sortFiles(File[] files) {
		/* Very lazy merge sort algorithm */
		sortBlock(files, 0, files.length - 1, new File[files.length]);
	}
	private static void sortBlock(File[] files, int start, int end, File[] mergeTemp) {
		final int length = end - start + 1;
		if (length < 8) {
			for (int i = end; i > start; --i) {
				for (int j = end; j > start; --j)  {
					if (compareFiles(files[j - 1], files[j]) > 0) {
					    final File temp = files[j]; 
					    files[j] = files[j-1]; 
					    files[j-1] = temp;
					}
			    }
			}
			return;
		}
		final int mid = (start + end) / 2;
		sortBlock(files, start, mid, mergeTemp);
		sortBlock(files, mid + 1, end, mergeTemp);
		int x = start;
		int y = mid + 1;
		for (int i = 0; i < length; ++i) {
			if ((x > mid) || ((y <= end) && compareFiles(files[x], files[y]) > 0)) {
				mergeTemp[i] = files[y++];
			} else {
				mergeTemp[i] = files[x++];
			}
		}
		for (int i = 0; i < length; ++i) files[i + start] = mergeTemp[i];
	}
	private static int compareFiles(File a, File b) {
//		boolean aIsDir = a.isDirectory();
//		boolean bIsDir = b.isDirectory();
//		if (aIsDir && ! bIsDir) return -1;
//		if (bIsDir && ! aIsDir) return 1;
		return a.getName().compareToIgnoreCase(b.getName());
	}
	
	/*
	 * This worker updates the table with file information in the background.
	 * <p>
	 * Implementation notes:
	 * <ul>
	 * <li> It is designed such that it can be interrupted cleanly.
	 * <li> It uses asyncExec() in some places to ensure that SWT Widgets are manipulated in the
	 *      right thread.  Exclusive use of syncExec() would be inappropriate as it would require a pair
	 *      of context switches between each table update operation.
	 * </ul>
	 * </p>
	 */

	/**
	 * Stops the worker and waits for it to terminate.
	 */
	public void workerStop() {
		if (workerThread == null) return;
		synchronized(workerLock) {
			workerCancelled = true;
			workerStopped = true;
			workerLock.notifyAll();
		}
		while (workerThread != null) {
			if (! display.readAndDispatch()) display.sleep();
		}
	}

	/**
	 * Notifies the worker that it should update itself with new data.
	 * Cancels any previous operation and begins a new one.
	 * 
	 * @param dir the new base directory for the table, null is ignored
	 * @param force if true causes a refresh even if the data is the same
	 */
	public void workerUpdate(File dir, boolean force) {
		if (dir == null) return;
		if ((!force) && (workerNextDir != null) && (workerNextDir.equals(dir))) return;

		synchronized(workerLock) {
			workerNextDir = dir;
			workerStopped = false;
			workerCancelled = true;
			workerLock.notifyAll();
		}
		if (workerThread == null) {
			workerThread = new Thread(workerRunnable);
			workerThread.start();
		}
	}

	/**
	 * Manages the worker's thread
	 */
	private final Runnable workerRunnable = new Runnable() {
		public void run() {
			while (! workerStopped) {
				synchronized(workerLock) {
					workerCancelled = false;
					workerStateDir = workerNextDir;
				}
				workerExecute();
				synchronized(workerLock) {
					try {
						if ((!workerCancelled) && (workerStateDir == workerNextDir)) workerLock.wait();
					} catch (InterruptedException e) {
					}
				}
			}
			workerThread = null;
		}
	};
	
	/**
	 * Updates the table's contents
	 */
	protected void workerExecute() {
		File[] dirList;
		
		// Clear existing information
		display.syncExec(new Runnable() {
			public void run() {
				tableContentsOfLabel.setText(FileViewer.getResourceString("details.ContentsOf.text",
					new Object[] { workerStateDir.getPath() }));
				table.removeAll();
				table.setRedraw(false);
				table.setData(TABLEDATA_DIR, workerStateDir);
			}
		});
		dirList = getDirectoryList(workerStateDir);

		for (int i = 0; (! workerCancelled) && (i < dirList.length); i++) {
			final File theFile = dirList[i];
			workerAddFileDetails(dirList[i]);

			final boolean doIncrementalRefresh = ((i & 127) == 127);
			if (doIncrementalRefresh) display.syncExec(new Runnable() {
				public void run () {
					// table.redraw();
					table.setRedraw(true);
					table.setRedraw(false);
				}
			});
		}

		// Allow the table to refresh itself
		display.asyncExec(new Runnable() {
			public void run() {
				table.setRedraw(true);
			}
		});
	}
		
	/**
	 * Adds a file's detail information to the directory list
	 */
	private void workerAddFileDetails(final File file) {
		final String nameString = file.getName();
		final String dateString = dateFormat.format(new Date(file.lastModified()));
		final String sizeString;
		final String typeString;
		final Image iconImage;
		
		if (file.isDirectory()) {
			typeString = getResourceString("filetype.Folder");
			sizeString = "";
			iconImage = IconCache.stockImages[IconCache.iconClosedFolder];
		} else {
			sizeString = getResourceString("filesize.KB",
				new Object[] { new Long((file.length() + 512) / 1024) });
			
			int dot = nameString.lastIndexOf('.');
			if (dot != -1) {
				String extension = nameString.substring(dot);
				Program program = Program.findProgram(extension);
				if (program != null) {
					typeString = program.getName();
					iconImage = IconCache.getIconFromProgram(program, extension);
				} else {
					typeString = getResourceString("filetype.Unknown", new Object[] { extension.toUpperCase() });
					iconImage = IconCache.stockImages[IconCache.iconFile];
				}
			} else {
				typeString = getResourceString("filetype.None");
				iconImage = IconCache.stockImages[IconCache.iconFile];
			}
		}
		final String[] strings = new String[] { nameString, sizeString, typeString, dateString };

		display.asyncExec(new Runnable() {
			public void run () {
				TableItem tableItem = new TableItem(table, 0);
				tableItem.setText(strings);
				tableItem.setImage(iconImage);
				tableItem.setData(TABLEITEMDATA_FILE, file);
			}
		});
	}	
}
