package org.eclipse.swt.examples.explorer;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.io.*;import java.text.*;import java.util.*;import org.eclipse.swt.*;import org.eclipse.swt.dnd.*;import org.eclipse.swt.events.*;import org.eclipse.swt.graphics.*;import org.eclipse.swt.program.*;import org.eclipse.swt.widgets.*;

/**
 * File Viewer example
 */
public class FileViewer { 
 	private static ResourceBundle resourceBundle;
	private static final String
		COMBODATA_PREVIOUS_INDEX = "Combo.previousIndex", // Integer: last selected combo index so we can revoke a selection
		COMBODATA_ROOTS          = "Combo.roots",         // File[]: filesystem root entries
		TREEDATA_PREVIOUS_ITEM   = "Tree.previousItem",   // TreeItem: last selected tree item so we can revoke a selection
		TREEITEMDATA_FILE        = "TreeItem.file",       // File: File associated with tree item
		TREEITEMDATA_SUBFILES    = "TreeItem.subfiles",   // File[]: cached directory listing
		TREEITEMDATA_IMAGEEXPANDED  = "TreeItem.imageExpanded",  // Image: shown when item is expanded
		TREEITEMDATA_IMAGECOLLAPSED = "TreeItem.imageCollapsed", // Image: shown when item is collapsed
		TABLEDATA_FILE           = "Table.file",          // File: dir currently being shown in details table
		TABLEITEMDATA_FILE       = "TableItem.file";      // File: File associated with table row

	private final static String DRIVE_A = "a:"+File.separator;
	private final static String DRIVE_B = "b:"+File.separator;

	/* Important UI elements */ 	  
 	private Display display;
	private Shell   shell;
	private Combo   combo;
	private ToolBar toolBar;
	private Tree    tree;
	private Table   table;
	private Sash    sash;

	private Label numObjectsLabel;
	private Label diskSpaceLabel;
	private Label allFoldersLabel;
	private Label contentLabel;

	/* Worker thread control */
	private UpdateWorker tableUpdateWorker;
	
	/* Simulate only flag */
	// when true, disables actual filesystem manipulations and outputs results to standard out
	private static boolean simulateOnly = false;

	/**
	 * Runs main program.
	 */
	public static void main (String [] args) {
		resourceBundle = ResourceBundle.getBundle("examples_explorer");
		new FileViewer().open();
	}
	
	/**
	 * Opens the main program.
	 */
	public void open() {		
		// Create the window
		display = new Display();
		Images.loadAll(display);
		shell = new Shell();
		createShellContents();
		shell.open();

		// Event loop
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
		// Cleanup
		tableUpdateWorker.syncStop();
		Images.freeAll();
		display.dispose();
	}

	/**
	 * Closes the main program.
	 */
	public void close() {
		shell.close();
	}
	
	/**
	 * Returns a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	public static String getResourceString(String key) {
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
	public static String getResourceString(String key, Object[] args) {
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
	public void createShellContents() {
		shell.setText(getResourceString("Title", new Object[] { "" }));	
		shell.setImage(Images.ShellIcon);
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		createFileMenu(bar);
		createHelpMenu(bar);
		
		createToolBar(shell);
		
		createStatusLine(shell);

		createDetailsTable(shell);
		createTree(shell); // creating Tree depends on Table already existing
		createSash(shell);
	
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle rect = shell.getClientArea ();
				Point comboSize = combo.computeSize(combo.getSize().x, SWT.DEFAULT);
				Point toolBarSize = toolBar.computeSize (rect.width, SWT.DEFAULT);
				int toolHeight = Math.max(comboSize.y, toolBarSize.y) + 2;
				toolBar.setBounds (0, 0, rect.width, toolHeight);		
			
				Rectangle sashBounds = sash.getBounds();
				int sashX = sashBounds.x, sashWidth = sashBounds.width;
			
				Point size = numObjectsLabel.getSize();
				allFoldersLabel.setBounds(0, toolHeight, sashX, size.y);
				contentLabel.setBounds(sashX + sashWidth, toolHeight, rect.width - (sashX + sashWidth), size.y);
			
				sash.setBounds(sashX, toolHeight, sashWidth, rect.height - toolHeight - size.y);	
				tree.setBounds(0, toolHeight + size.y, sashX, rect.height - toolHeight - (size.y * 2));
				table.setBounds(sashX + sashWidth, toolHeight + size.y, rect.width - (sashX + sashWidth), rect.height - toolHeight - (size.y * 2));
				
				numObjectsLabel.setBounds(0, rect.height - size.y, size.x, size.y);
				diskSpaceLabel.setBounds(size.x, rect.height - size.y, rect.width - size.x, size.y);
			}
		});
		refreshAll();
	}

	/**
	 * Refresh the entire tree.
	 */
	public void refreshAll() {
		File[] roots = listRoots();
		
		for (int i = 0; i < roots.length; ++i) {
			final File file = roots[i];
			combo.add(file.getPath());
			
			TreeItem item = new TreeItem(tree, SWT.NULL);
			initTreeItemVolume(item, file);

			// add a placeholder child item so we get the "expand" button
			TreeItem placeholderItem = new TreeItem(item, SWT.NULL);
		}
		combo.setData(COMBODATA_ROOTS, roots);
		refreshTreeStructure(tree.getItems());
	}
	public void refresh() {
	}
	public void markDirtyFile(File file) {
	}
	
	private void refreshTreeStructure(TreeItem[] items) {
		for (int i = 0; i < items.length; i++) {
			final TreeItem item = items[i];
			refreshTreeStructure(item.getItems());
		}
	}
	
	/**
	 * Creates the File Menu.
	 * 
	 * @param parent the parent menu
	 */
	private void createFileMenu(Menu parent) {
		Menu menu = new Menu(parent);
		MenuItem header = new MenuItem(parent, SWT.CASCADE);
		header.setText(getResourceString("File_menutitle"));
		header.setMenu(menu);

		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(getResourceString("Close_menuitem"));
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
		header.setText(getResourceString("Help_menutitle"));
		header.setMenu(menu);

		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(getResourceString("About_menuitem"));		
		item.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				/*  + " " + System.getProperty ("os.name") */
			}
		});
	}

	/**
	 * Creates the toolbar
	 * 
	 * @param shell the shell on which to attach the toolbar
	 */
	private void createToolBar(Shell shell) {
		toolBar = new ToolBar(shell, SWT.NULL);
		ToolItem item = new ToolItem(toolBar, SWT.SEPARATOR);
		final Combo combo = new Combo(toolBar, SWT.READ_ONLY);
		this.combo = combo;
		item.setControl(combo);
		item.setWidth(185);

		combo.setData(COMBODATA_ROOTS, null);
		combo.setData(COMBODATA_PREVIOUS_INDEX, null);
		combo.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				final File[] roots = (File[]) combo.getData(COMBODATA_ROOTS);
				int selection = combo.getSelectionIndex();
				if (selection >= 0 && selection < roots.length) {
					selectFile(roots[selection], null);
				}
			}
		});
		
		item = new ToolItem(toolBar, SWT.SEPARATOR);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(Images.UpDirectory);
		item.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				TreeItem[] items = tree.getSelection();
				if (items.length == 0) return;

				if (items[0].getParentItem() != null) {
					TreeItem parent = items[0].getParentItem();
					if (parent == null) return;
					
					tree.setSelection(new TreeItem[] { parent });
					selectFile((File) parent.getData(TREEITEMDATA_FILE), null);
				}
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

	/**
	 * Creates the status bar.
	 * 
	 * @param shell the shell on which to attach the status bar
	 */
	private void createStatusLine(Shell shell) {
		numObjectsLabel = new Label(shell, SWT.BORDER);
		Point size = numObjectsLabel.computeSize(145, SWT.DEFAULT);
		numObjectsLabel.setSize(size.x, size.y + 2);
		diskSpaceLabel = new Label(shell, SWT.BORDER);
		allFoldersLabel = new Label(shell, SWT.BORDER);
		allFoldersLabel.setText(getResourceString("All_folders"));
		contentLabel = new Label(shell, SWT.BORDER);
	}

	/**
	 * Creates the file details table.
	 * 
	 * @param shell the shell on which to attach the details table
	 */
	private void createDetailsTable(Shell shell) {
		table = new Table(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		String[] titles = new String [] {
			getResourceString("Name"),
			getResourceString("Size"),
			getResourceString("Type"),
			getResourceString("Modified")
		};
		int[] widths = new int[] {150, 60, 75, 150};
		for (int i = 0; i < titles.length; i++) {
			TableColumn column = new TableColumn(table, SWT.NONE);
			column.setText(titles [i]);
			column.setWidth(widths [i]);
		}
		table.setHeaderVisible(true);
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent event) {
				final TableItem[] items = table.getSelection();
				final File[] files = new File[items.length];
				
				for (int i = 0; i < items.length; ++i) {
					files[i] = (File) items[i].getData(TABLEITEMDATA_FILE);
				}
				doDefaultFileAction(files);
			}
		});
		createTableDragSource(table);
		createTableDropTarget(table);
		
		tableUpdateWorker = new TableUpdateWorker();
	}

	/**
	 * Updates the table with file information in the background.
	 * <p>
	 * It is designed such that it can be interrupted cleanly.  Rather than put all the code
	 * to manage this here, the balance has been pushed out to UpdateWorker so it can be re-used.
	 * </p>
	 */
	private class TableUpdateWorker extends UpdateWorker {
		public TableUpdateWorker() {
			super(display);
		}
		/**
		 * Updates the table's contents
		 * 
		 * @param data arguments in an Object[]
		 * <ul>
		 * <li>File data[0] the file/dir to detail in the table
		 * <li>File[] data[1] the cached directory listing, or null if not cached or not appropriate
		 * </ul>
		 */
		protected void execute(Object data) {
			/*
			 * Note the use of asyncExec() below to ensure that SWT Widgets are manipulated in the
			 * right thread.  syncExec() would be inappropriate here as it would require a full pair
			 * of context switches between each operation -- which is inefficient.
			 */
			// Clear existing information
			display.asyncExec(new Runnable() {
				public void run() {
					diskSpaceLabel.setText("");
					numObjectsLabel.setText("");
					table.setRedraw(false);
					table.removeAll();
				}
			});

			// Get arguments
			if (data == null) return;
			final Object[] args = (Object[]) data;
			final File file = (File) args[0];
			final File[] cache = (File[]) args[1];

			// Set current file
			display.asyncExec(new Runnable() {
				public void run() {
					table.setData(TABLEDATA_FILE, file);
				}
			});
			if (! file.exists()) return;
			
			// Show disk usage
			final long folderSize = file.length();
			final long diskFreeSize = 0;
			display.asyncExec(new Runnable() {
				public void run() {
					diskSpaceLabel.setText(getResourceString("Filesize",
						new Object[] { new Long(folderSize), new Long(diskFreeSize) }));
				}
			});

			// Show directory listing
			if (!file.isDirectory()) return;
			
			final DateFormat dateFormat = DateFormat.getDateTimeInstance(
				DateFormat.MEDIUM, DateFormat.MEDIUM);
			final File[] list = (cache != null) ? cache : file.listFiles();

			// Show object count
			display.asyncExec(new Runnable() {
				public void run() {
					numObjectsLabel.setText(getResourceString("Objects", new Object[] {
						new Integer((list != null) ? list.length : 0) }));
				}
			});

			// Show directory listing
			if (list != null) {
				if (list != cache) sort(list);
				for (int i = 0; i < list.length; i++) {
					final File theFile = list[i];
					if (isCancelled()) break;

					final String date = dateFormat.format(new Date(theFile.lastModified()));
					final Image image;
					final String[] strings;
					if (list[i].isDirectory ()) {
						strings = new String [] {
							theFile.getName(),
							"",
							getResourceString("File_folder"),
							date
						};
						image = Images.Folder;
					} else {
						strings = new String [] {
							theFile.getName(),
							getResourceString("KB", new Object[] { new Long((theFile.length() + 512) / 1024) }),
							getResourceString("System_file"),
							date
						};
						image = Images.File;
					}
					final boolean doIncrementalRefresh = ((i & 127) == 127);
					display.asyncExec(new Runnable() {
						public void run () {
							TableItem tableItem = new TableItem(table, 0);
							tableItem.setText(strings);
							tableItem.setImage(image);
							tableItem.setData(TABLEITEMDATA_FILE, theFile);
							if (doIncrementalRefresh) {
								table.setRedraw(true);
								table.setRedraw(false);
							}
						}
					});
				}
			}
			// Allow the table to refresh itself
			display.asyncExec(new Runnable() {
				public void run() {
					table.setRedraw(true);
				}
			});
		}
	}

	/** Creates the directory tree.
	 * 
	 * @param shell the shell on which to attach the tree
	 */
	private void createTree(Shell shell) {
		tree = new Tree(shell, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		tree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				final TreeItem[] selection = tree.getSelection();
				if (selection == null || selection.length == 0) return;
	
				TreeItem item = selection[0];
				File file = (File) item.getData(TREEITEMDATA_FILE);
				selectFile(file, (File[]) item.getData(TREEITEMDATA_SUBFILES));
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		tree.addTreeListener(new TreeAdapter() {
			public void treeExpanded(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				final Image image = (Image) item.getData(TREEITEMDATA_IMAGEEXPANDED);
				if (image != null) item.setImage(image);
				expandTreeItem(item);
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
	 * Creates the sash.
	 * 
	 * @param shell the shell to 
	 */
	private void createSash(final Shell shell) {
		sash = new Sash(shell, SWT.VERTICAL);
		sash.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail != SWT.DRAG) {
					sash.setBounds (event.x, event.y, event.width, event.height);
					Rectangle rect = shell.getClientArea ();
					
					Rectangle toolRect = toolBar.getBounds ();
					Point size = allFoldersLabel.getSize ();
					allFoldersLabel.setBounds (0, toolRect.height, event.x, size.y);
					size = contentLabel.getSize ();
					contentLabel.setBounds (event.x + event.width, toolRect.height,
						rect.width - (event.x + event.width), size.y);
			
			
					size = tree.getSize ();
					tree.setSize (event.x, size.y);
					Rectangle bounds = table.getBounds ();
					table.setBounds (event.x + event.width, bounds.y,
						rect.width - (event.x + event.width), bounds.height);
				} else {
					Rectangle rect = shell.getClientArea ();
					event.x = Math.min (Math.max (30, event.x), rect.width - 30);
				}
			}
		});
		Point size = sash.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		sash.setBounds (300, 0, size.x, 0);
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
			public void dragStart(DragSourceEvent e) {
				e.doit = table.getSelectionCount() > 0;
			}
			public void dragSetData(DragSourceEvent event){
				if (! FileTransfer.getInstance().isSupportedType(event.dataType)) return;
				
				final TableItem[] tableDragItems = table.getSelection();
				if (tableDragItems == null || tableDragItems.length == 0) return;
				
				final String[] data = new String[tableDragItems.length];
				for (int i = 0; i < tableDragItems.length; i++) {
					File file = (File) tableDragItems[i].getData(TABLEITEMDATA_FILE);
					data[i] = file.getAbsolutePath();
				}
				event.data = data;
			}
			public void dragFinished(DragSourceEvent event) {
			}
		});
		return dragSource;
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
			public void dragStart(DragSourceEvent event){
				event.doit = table.getSelectionCount() > 0;
			}
			public void dragFinished(DragSourceEvent event){
			}
			public void dragSetData(DragSourceEvent event){
				if (! FileTransfer.getInstance().isSupportedType(event.dataType)) return;
				
				final TreeItem[] treeDragItems = tree.getSelection();
				if (treeDragItems == null || treeDragItems.length == 0) return;
				
				final String[] data = new String[treeDragItems.length];
				for (int i = 0; i < treeDragItems.length; i++) {
					File file = (File) treeDragItems[i].getData(TREEITEMDATA_FILE);
					data[i] = file.getAbsolutePath();
				}
				event.data = data;
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
				validateDrop(event, getTargetFile(event));
			}
			public void drop(DropTargetEvent event) {
				performDrop(event, getTargetFile(event));
			}				
			private File getTargetFile(DropTargetEvent event) {
				// Determine the target File for the drop 
				final TableItem item = table.getItem(table.toControl(new Point(event.x, event.y)));
				final File targetFile;
				if (item == null) {
					// We dropped on an unoccupied area of the table, use the table's root file
					targetFile = (File) table.getData(TABLEDATA_FILE);
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
	 * Creates the Drag & Drop DropTarget for items being dropped onto the tree.
	 * 
	 * @return the DropTarget for the tree
	 */
	private DropTarget createTreeDropTarget(final Tree tree) {
		DropTarget dropTarget = new DropTarget(tree, DND.DROP_MOVE | DND.DROP_COPY);
		dropTarget.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dropTarget.addDropListener(new TreeScrollDropListener(tree));
		dropTarget.addDropListener(new TreeExpandDropListener(tree));
		dropTarget.addDropListener(new DropTargetAdapter() {
			public void dragOver(DropTargetEvent event) {
				validateDrop(event, getTargetFile(event));
			}
			public void drop(DropTargetEvent event) {
				performDrop(event, getTargetFile(event));
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
	 * Validate a drop target event
	 * <p>
	 * Note event.detail is modified by this method
	 * </p>
	 * @param event the DropTargetEvent we are validating
	 * @param targetFile the File representing the drop target location
	 *        under inspection, or null if none
	 * @return an array of files to be operated on, or null on error
	 */
	private File[] validateDrop(DropTargetEvent event, File targetFile) {
		final int dropMode = (event.detail != DND.DROP_NONE) ? event.detail : DND.DROP_MOVE;
		event.detail = DND.DROP_NONE; // simplify error reporting

		// Validate the target
		if (targetFile == null) return null;
		if (! targetFile.isDirectory()) return null;

		// Get dropped data (an array of filenames)
		final String[] names = (String[]) event.data;
		final File[] files;
		if (names != null) {			
			// Validate the source
			files = new File[names.length];
			for (int i = 0; i < names.length; ++i) {
				files[i] = new File(names[i]);
				if (files[i].equals(targetFile)) return null;
			}
		} else {
			// Files not available yet
			files = null;
		}
		event.detail = dropMode;
		return files;
	}

	/**
	 * Perform a drop on a target
	 * <p>
	 * Note event.detail is modified by this method
	 * </p>
	 * @param event the DropTargetEvent passed as parameter to the drop() method
	 * @param targetFile the File representing the drop target location
	 *        under inspection, or null if none
	 */
	private void performDrop(DropTargetEvent event, File targetFile) {
		try {
			final File[] sourceFiles = validateDrop(event, targetFile);
			final int dropMode = event.detail;

			event.detail = DND.DROP_NONE; // simplify error reporting
			if (sourceFiles == null) return;
		
			markDirtyFile(targetFile);
			for (int i = 0; i < sourceFiles.length; i++){
				final File source = sourceFiles[i];
				final File dest = new File(targetFile, source.getName());
	
				// Perform action on each file
				switch (dropMode) {
					case DND.DROP_COPY:
						if (! copyFileStructure(source, dest)) return;
						break;
					case DND.DROP_MOVE:
						markDirtyFile(source);
						if (! moveFileStructure(source, dest)) return;
						break;
					default:
						throw new IllegalArgumentException(getResourceString("exception.Invalid_DND_Mode",
							new Object[] { new Integer(dropMode) }));
				}
			}
			event.detail = dropMode;
		} finally {
			refresh();
		}
	}

	/**
	 * Select a File
	 * 
	 * @param file the File to select
	 * @param cachedChildren the children of the File, or null if not known
	 */
	private void selectFile(final File file, final File[] cachedChildren) {
		if (file == null) return;
		
		/*
		 * Loop until the file is selected appropriately, or we abort
		 */
		for(;;) {
			// Special case: if drive a or b is selected and does not contain a disk
			// then prompt user to retry
			String path = file.getAbsolutePath();
			if (! file.exists () && (path.equals(DRIVE_A) ||  path.equals(DRIVE_B))) {
				if (messageBoxRetry(path)) {
					continue;
				} else {
					Integer index = (Integer) combo.getData(COMBODATA_PREVIOUS_INDEX);
					if (index != null) {
						combo.select(index.intValue());
					}
					TreeItem[] items = (TreeItem[]) tree.getData(TREEDATA_PREVIOUS_ITEM);
					if (items != null) {
						tree.setSelection(items);
					}
					return;
				}
			}
			break;
		}

		/* Clean up some UI artifacts */

	// Update the combo box if a new drive is selected
//	String drive = file.getAbsolutePath();
//	int end = drive.indexOf(File.separator);
//	drive = drive.substring(0, end + 1);
//	if (!combo.getText().equals(drive)){
//		combo.select (combo.indexOf(drive));
//	}

		/* Store this as our previous selection */
		combo.setData(COMBODATA_PREVIOUS_INDEX, new Integer(combo.getSelectionIndex()));
		tree.setData(TREEDATA_PREVIOUS_ITEM, tree.getSelection());

		/*
		 * Update file details
		 */

		// Update title bar
		String name = file.getName();
		if (name.length() == 0) name = file.getPath();
		shell.setText(getResourceString("Title", new Object[] { name }));

		// Update file details content-of label
		name = file.isDirectory() ? file.getPath() : file.getParent();
		contentLabel.setText(getResourceString("Content_of", new Object[] { name }));

		/*** Spawn a thread to update more time-consuming information ***/
		tableUpdateWorker.asyncUpdate(new Object[] { file, cachedChildren });
	}

	/**
	 * Performs the default action on a set of files.
	 * 
	 * @param files the array of files to process
	 */
	private void doDefaultFileAction(File[] files) {
		// only uses the 1st file (for now)
		if (files.length == 0) return;
		final File file = files[0];
		final String fileName = file.getAbsolutePath();
		if (! Program.launch(fileName)) {	
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setMessage(getResourceString("Failed_launch", new Object[] { fileName }));
			dialog.setText(shell.getText ());
			dialog.open();
		}
	}

	/**
	 * Populate an item in the tree with a complete directory listing.
	 * 
	 * @param item the TreeItem to fill in
	 */
	private void expandTreeItem(TreeItem item) {
		final File file = (File) item.getData(TREEITEMDATA_FILE);
		File[] subFiles = (File[]) item.getData(TREEITEMDATA_SUBFILES);

		/* Get directory listing */
		shell.setCursor(Images.CursorWait);
		if (subFiles == null) {
			if (file != null) {
				subFiles = file.listFiles();
				item.setData(TREEITEMDATA_SUBFILES, subFiles);
			}
		}
		
		/* Eliminate any existing (possibly placeholder) children */
		final TreeItem[] oldChildren = item.getItems();
		for (int i = 0; i < oldChildren.length; ++i) {
			oldChildren[i].dispose();
		}

		if (subFiles != null && subFiles.length > 0) {
			/* Add subdirectory entries */
			sort(subFiles);			
			for (int i = 0; i < subFiles.length; ++i) {
				final File folder = subFiles[i];
				if (! folder.isDirectory()) continue;
				
				// add the directory to the tree
				TreeItem newItem = new TreeItem(item, SWT.NULL);
				initTreeItemFolder(newItem, folder);
				
				// add a placeholder child item so we get the "expand" button
				TreeItem placeholderItem = new TreeItem(newItem, SWT.NULL);
			}
		} else {
			/* Error or nothing found -- collapse the item */
			item.setExpanded(false);
		}			
		shell.setCursor(null);
	}

	/**
	 * Initialize a volume item
	 * 
	 * @param item the TreeItem to initialize
	 * @param volume the File associated with this TreeItem
	 */
	private void initTreeItemVolume(TreeItem item, File volume) {
		item.setText(volume.getPath());
		item.setImage(Images.Drive);
		item.setData(TREEITEMDATA_FILE, volume);
	}

	/**
	 * Initialize a folder item
	 * 
	 * @param item the TreeItem to initialize
	 * @param folder the File associated with this TreeItem
	 */
	private void initTreeItemFolder(TreeItem item, File folder) {
		item.setText(folder.getName());
		item.setImage(Images.Folder);
		item.setData(TREEITEMDATA_FILE, folder);
		item.setData(TREEITEMDATA_IMAGEEXPANDED, Images.FolderOpen);
		item.setData(TREEITEMDATA_IMAGECOLLAPSED, Images.Folder);
	}

	/**
	 * Displays a RETRY/CANCEL dialog box
	 * 
	 * @param s the name of the resource that is not available
	 * @return true iff RETRY is selected
	 */
	protected boolean messageBoxRetry(String s) {	
		MessageBox box = new MessageBox(shell, SWT.RETRY | SWT.CANCEL | SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
		box.setMessage(getResourceString("Unavailable", new Object[] { s }));
		box.setText(shell.getText ());
		return box.open() == SWT.RETRY;
	}

	/**
	 * Moves a file or entire directory structure.
	 * [Note only works within a specific volume on some platforms for now]
	 *
	 * @param oldFile the location of the old file or directory
	 * @param newFile the location of the new file or directory
	 * @return true iff the operation succeeds without errors
	 */
	protected static boolean moveFileStructure(File oldFile, File newFile) {
		if (oldFile == null || newFile == null) return false;
		if (! oldFile.exists());
		if (newFile.exists());
		if (simulateOnly) {
			System.out.println("Simulated move from " + oldFile.getPath() + " to " + newFile.getPath());
			return true;
		} else {
			return oldFile.renameTo(newFile);
		}
	}
	
	/**
	 * Copies a file or entire directory structure.
	 * 
	 * @param oldFile the location of the old file or directory
	 * @param newFile the location of the new file or directory
	 * @return true iff the operation succeeds without errors
	 */
	protected static boolean copyFileStructure(File oldFile, File newFile) {
		if (oldFile == null || newFile == null) return false;		
		if (! oldFile.exists());
		if (newFile.exists());
		
		if (oldFile.isFile()) {
			/*
			 * Copy a file
			 */
			if (simulateOnly) {
				System.out.println("Simulated copy from " + oldFile.getPath() + " to " + newFile.getPath());
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
				System.out.println("Simulated directories created for " + newFile.getPath());
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
				System.out.println("Ignoring " + oldFile.getPath());
			}
			return true; // ignore it
		}
	}
	
	/**
	 * Sorts files lexicographically by name.
	 * 
	 * @param files the array of Files to be sorted
	 */
	protected static void sort(File[] files) {
		/* Very lazy merge sort algorithm */
		sortBlock(files, 0, files.length - 1, new File[files.length]);
	}
	private static void sortBlock(File[] files, int start, int end, File[] mergeTemp) {
		final int length = end - start + 1;
		if (length < 8) {
			for (int i = end; i > start; --i) {
				for (int j = end; j > start; --j)  {
					if (files[j-1].getName().toUpperCase().compareToIgnoreCase(
							files[j].getName().toUpperCase()) > 0) {
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
			if ((x > mid) || ((y <= end) &&
				files[x].getName().compareToIgnoreCase(files[y].getName()) > 0)) {
				mergeTemp[i] = files[y++];
			} else {
				mergeTemp[i] = files[x++];
			}
		}
		for (int i = 0; i < length; ++i) files[i + start] = mergeTemp[i];
	}

	/**
	 * List file roots
	 * 
	 * @return an array of Files corresponding to the root directories on the platform
	 */
	protected File[] listRoots() {
		/*
		 * On JDK 1.22 only...
		 */
		// return File.listRoots();

		/*
		 * On JDK 1.1.7 and beyond...
		 * -- PORTABILITY ISSUES HERE --
		 */
		if (System.getProperty ("os.name").indexOf ("Windows") != -1) {
			File[] roots = null;
			roots = (File[]) addToFileArray(roots, new File(DRIVE_A));
			roots = (File[]) addToFileArray(roots, new File(DRIVE_B));
			for (char i = 'c'; i <= 'z'; ++i) {
				File drive = new File(i + ":" + File.separator);
				if (drive.isDirectory() && drive.exists()) {
					roots = (File[]) addToFileArray(roots, drive);
				}
			}
			return roots;
		} else {
			return new File[] { new File(File.separator) };
		}
	}

	/**
	 * Extends a File array and adds an element to its tail
	 *
	 * @param array the array, null if the array should be created from scratch
	 * @param object the object to add
	 * @return the new array
	 */
	private static File[] addToFileArray(File[] array, File object) {
		if (array == null) {
			return new File[] { object };
		}
		File[] newArray = new File[array.length + 1];
		System.arraycopy(array, 0, newArray, 0, array.length);
		newArray[array.length] = object;
		return newArray;
	}
}
