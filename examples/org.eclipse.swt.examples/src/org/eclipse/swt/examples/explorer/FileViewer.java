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

	private final static String DRIVE_A = "a:" + File.separator;
	private final static String DRIVE_B = "b:" + File.separator;

	/* Important UI elements */ 	  
 	/* package */ Display display;
	private Shell   shell;
	private ToolBar toolBar;
	private Sash    sash;

	private TreeView treeView;
	private TableView tableView;
	private ComboView comboView;

	private Label numObjectsLabel;
	private Label diskSpaceLabel;
	private Label allFoldersLabel;
	private Label contentLabel;
	
	private File currentDirectory = null;

	/* Simulate only flag */
	// when true, disables actual filesystem manipulations and outputs results to standard out
	private static boolean simulateOnly = true;

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
	/* package */ void open() {		
		// Create the window
		display = new Display();
		Images.loadAll(display);
		shell = new Shell();
		createShellContents();
		notifyRefreshFiles(null);
		shell.open();

		// Event loop
		while (! shell.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
		// Cleanup
		tableView.dispose();
		Images.freeAll();
		display.dispose();
	}

	/**
	 * Closes the main program.
	 */
	/* package */ void close() {
		shell.close();
	}
	
	/**
	 * Returns a string from the resource bundle.
	 * We don't want to crash because of a missing String.
	 * Returns the key if not found.
	 */
	/* package */ static String getResourceString(String key) {
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
	/* package */ static String getResourceString(String key, Object[] args) {
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
	/* package */ void createShellContents() {
		shell.setText(getResourceString("Title", new Object[] { "" }));	
		shell.setImage(Images.ShellIcon);
		Menu bar = new Menu(shell, SWT.BAR);
		shell.setMenuBar(bar);
		createFileMenu(bar);
		createHelpMenu(bar);
		
		createToolBar(shell);
		
		createStatusLine(shell);

		tableView = new TableView(this, shell);
		treeView = new TreeView(this, shell);
		createSash(shell);
	
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				Rectangle rect = shell.getClientArea ();
				Point comboSize = comboView.combo.computeSize(comboView.combo.getSize().x, SWT.DEFAULT);
				Point toolBarSize = toolBar.computeSize (rect.width, SWT.DEFAULT);
				int toolHeight = Math.max(comboSize.y, toolBarSize.y) + 2;
				toolBar.setBounds (0, 0, rect.width, toolHeight);		
			
				Rectangle sashBounds = sash.getBounds();
				int sashX = sashBounds.x, sashWidth = sashBounds.width;
			
				Point size = numObjectsLabel.getSize();
				allFoldersLabel.setBounds(0, toolHeight, sashX, size.y);
				contentLabel.setBounds(sashX + sashWidth, toolHeight, rect.width - (sashX + sashWidth), size.y);
			
				sash.setBounds(sashX, toolHeight, sashWidth, rect.height - toolHeight - size.y);	
				treeView.tree.setBounds(0, toolHeight + size.y, sashX, rect.height - toolHeight - (size.y * 2));
				tableView.table.setBounds(sashX + sashWidth, toolHeight + size.y, rect.width - (sashX + sashWidth), rect.height - toolHeight - (size.y * 2));
				
				numObjectsLabel.setBounds(0, rect.height - size.y, size.x, size.y);
				diskSpaceLabel.setBounds(size.x, rect.height - size.y, rect.width - size.x, size.y);
			}
		});
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
				MessageBox box = new MessageBox(shell, SWT.ICON_INFORMATION | SWT.OK);
				box.setText(getResourceString("About_title"));
				box.setMessage(getResourceString("About_description") + System.getProperty("os.name"));
				box.open();
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
		comboView = new ComboView(this, toolBar);
		item.setControl(comboView.combo);
		item.setWidth(185);

		item = new ToolItem(toolBar, SWT.SEPARATOR);
		item = new ToolItem(toolBar, SWT.PUSH);
		item.setImage(Images.UpDirectory);
//		item.addSelectionListener(new SelectionAdapter () {
//			public void widgetSelected(SelectionEvent e) {
//				TreeItem[] items = tree.getSelection();
//				if (items.length == 0) return;
//
//				if (items[0].getParentItem() != null) {
//					TreeItem parent = items[0].getParentItem();
//					if (parent == null) return;
//					
//					tree.setSelection(new TreeItem[] { parent });
//					selectFile((File) parent.getData(TREEITEMDATA_FILE), null);
//				}
//			}
//		});
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
			
			
					size = treeView.tree.getSize ();
					treeView.tree.setSize (event.x, size.y);
					Rectangle bounds = tableView.table.getBounds ();
					tableView.table.setBounds (event.x + event.width, bounds.y,
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
	 * Validate a drop target event
	 * <p>
	 * Note event.detail is modified by this method
	 * </p>
	 * @param event the DropTargetEvent we are validating
	 * @param targetFile the File representing the drop target location
	 *        under inspection, or null if none
	 * @return an array of files to be operated on, or null on error
	 */
	/* package */ File[] validateDrop(DropTargetEvent event, File targetFile) {
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
	/* package */ void performDrop(DropTargetEvent event, File targetFile) {
		Vector /* of File */ dirtyFiles = new Vector();
		try {
			final File[] sourceFiles = validateDrop(event, targetFile);
			final int dropMode = event.detail;

			event.detail = DND.DROP_NONE; // simplify error reporting
			if (sourceFiles == null) return;
		
			dirtyFiles.add(targetFile);
			for (int i = 0; i < sourceFiles.length; i++){
				final File source = sourceFiles[i];
				final File dest = new File(targetFile, source.getName());
	
				// Perform action on each file
				switch (dropMode) {
					case DND.DROP_COPY:
						if (! copyFileStructure(source, dest)) return;
						break;
					case DND.DROP_MOVE:
						dirtyFiles.add(source);
						if (! moveFileStructure(source, dest)) return;
						break;
					default:
						throw new IllegalArgumentException(getResourceString("exception.Invalid_DND_Mode",
							new Object[] { new Integer(dropMode) }));
				}
			}
			event.detail = dropMode;
		} finally {
			notifyRefreshFiles((File[]) dirtyFiles.toArray(new File[dirtyFiles.size()]));
		}
	}

	/**
	 * Notifies the application components that a new current directory has been selected
	 * 
	 * @param dir the directory that was selected, null clears the selection
	 */
	/* package */ void notifySelectedDirectory(File dir) {
		if (dir == null) return;
		if (currentDirectory != null && dir.equals(currentDirectory)) return;
		currentDirectory = dir;

		// Notify the other components
		comboView.selectedDirectory(dir);
		treeView.selectedDirectory(dir);
		tableView.selectedDirectory(dir);
	}
	
	/**
	 * Notifies the application components that files have been selected
	 * 
	 * @param files the files that were selected, null clears the selection
	 */
	/* package */ void notifySelectedFiles(File[] files) {
		if (files == null || files.length == 0) return;
		notifySelectedDirectory(getEnclosingDirectory(files[0]));
	}

	/**
	 * Notifies the application components that files must be refreshed
	 * 
	 * @param files the files that need refreshing, empty array is a no-op, null refreshes all
	 */
	/* package */ void notifyRefreshFiles(File[] files) {
		if (files != null && files.length == 0) return;
		
		// Notify the other components
		comboView.refreshFiles(files);
		treeView.refreshFiles(files);
		tableView.refreshFiles(files);
	}

	/**
	 * Performs the default action on a set of files.
	 * 
	 * @param files the array of files to process
	 */
	/* package */ void doDefaultFileAction(File[] files) {
		// only uses the 1st file (for now)
		if (files.length == 0) return;
		final File file = files[0];
		if (file.isDirectory()) {
			notifySelectedDirectory(file);
		}
		
		final String fileName = file.getAbsolutePath();
		if (! Program.launch(fileName)) {	
			MessageBox dialog = new MessageBox(shell, SWT.ICON_ERROR | SWT.OK);
			dialog.setMessage(getResourceString("Failed_launch", new Object[] { fileName }));
			dialog.setText(shell.getText ());
			dialog.open();
		}
	}

	/**
	 * Sets the diskspace label
	 * 
	 * @param text the new text
	 */
	/* package */ void setDiskSpaceText(String text) {
		diskSpaceLabel.setText(text);
	}

	/**
	 * Sets the number of objects label
	 * 
	 * @param text the new text
	 */
	/* package */ void setNumberOfObjectsText(String text) {
		numObjectsLabel.setText(text);
	}

	/**
	 * Sets the titlebar text
	 * 
	 * @param text the new text
	 */
	/* package */ void setTitleText(String text) {
		shell.setText(getResourceString("Title", new Object[] { text }));
	}

	/**
	 * Sets the contents-of label
	 * 
	 * @param text the new text
	 */
	/* package */ void setContentsOfText(String text) {
		contentLabel.setText(getResourceString("Content_of", new Object[] { text }));
	}

//	/**
//	 * Displays a RETRY/CANCEL dialog box
//	 * 
//	 * @param s the name of the resource that is not available
//	 * @return true iff RETRY is selected
//	 */
//	protected boolean messageBoxRetry(String s) {	
//		MessageBox box = new MessageBox(shell, SWT.RETRY | SWT.CANCEL | SWT.APPLICATION_MODAL | SWT.ICON_ERROR);
//		box.setMessage(getResourceString("Unavailable", new Object[] { s }));
//		box.setText(shell.getText ());
//		return box.open() == SWT.RETRY;
//	}

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
	 * Gets filesystem root entries
	 * 
	 * @return an array of Files corresponding to the root directories on the platform,
	 *         may be empty but not null
	 */
	/* package */ static File[] getRoots() {
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
			sort(roots);
			return roots;
		} else {
			return new File[] { new File(File.separator) };
		}
	}

	/**
	 * Gets the enclosing directory
	 * 
	 * @param file the file whose enclosing directory is to be locate, null is valid
	 * @return the original file if it was a directory, or its parent, null on failure
	 */
	/* package */ static File getEnclosingDirectory(File file) {
		if (file == null) return null;
		if (file.isDirectory()) return file;
		return file.getParentFile();
	}
	
	/**
	 * Gets a directory listing
	 * 
	 * @param file the directory to be listed
	 * @return an array of files this directory contains, may be empty but not null
	 */
	/* package */ static File[] getDirectoryList(File file) {
		File[] list = file.listFiles();
		if (list == null) return new File[0];
		sort(list);
		return list;
	}
}
