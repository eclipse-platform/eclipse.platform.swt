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

	/* Important UI elements */ 	  
 	/* package */ Display display;
	private Shell   shell;
	private ToolBar toolBar;
	private TreeView treeView;
	private TableView tableView;
	private ComboView comboView;

	private Label numObjectsLabel;
	private Label diskSpaceLabel;
	
	private File currentDirectory = null;

	private static final DateFormat dateFormat = DateFormat.getDateTimeInstance(
		DateFormat.MEDIUM, DateFormat.MEDIUM);
	/* Simulate only flag */
	// when true, disables actual filesystem manipulations and outputs results to standard out
	private static boolean simulateOnly = false;

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
	/* package */ void open() {		
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
		tableView.dispose();
		IconCache.freeResources();
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
		comboView = new ComboView(this, shell, gridData);
		gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData.horizontalSpan = 2;
		createToolBar(shell, gridData);

		SashForm sashForm = new SashForm(shell, SWT.NONE);
		sashForm.setOrientation(SWT.HORIZONTAL);
		gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 3;
		sashForm.setLayoutData(gridData);
		treeView = new TreeView(this, sashForm, null);
		tableView = new TableView(this, sashForm, null);
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

		MenuItem item = new MenuItem(menu, SWT.PUSH);
		item.setText(getResourceString("menu.Close.text"));
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
		item.setText(getResourceString("menu.About.text"));		
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
	 * Notifies the application components that a new current directory has been selected
	 * 
	 * @param dir the directory that was selected, null is ignored
	 */
	/* package */ void notifySelectedDirectory(File dir) {
		if (dir == null) return;
		if (currentDirectory != null && dir.equals(currentDirectory)) return;
		currentDirectory = dir;
		shell.setText(getResourceString("Title", new Object[] { currentDirectory.getPath() }));
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
		if (files == null) files = new File[0];
		if (files.length != 0) {
			final File file = files[0];	
			notifySelectedDirectory(file.getParentFile());
		}
		tableView.selectedFiles(files);
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
	/* package */ void doParent() {
		if (currentDirectory == null) return;
		File parentDirectory = currentDirectory.getParentFile();
		notifySelectedDirectory(parentDirectory);
	}
	 
	/**
	 * Performs a refresh
	 */
	/* package */ void doRefresh() {
		notifyRefreshFiles(null);
	}
	/**
	 * Sets the details appropriately for a file
	 * 
	 * @param file the file in question
	 */
	/* package */ void setFileDetails(File file) {
		diskSpaceLabel.setText(getResourceString("details.FileSize.text",
			new Object[] { new Long(file.length()) }));
		numObjectsLabel.setText("");
	}

	/**
	 * Sets the details appropriately for a folder
	 * 
	 * @param folder the folder in question
	 * @param files the directory listing
	 */
	/* package */ void setFolderDetails(File folder, File[] files) {
		diskSpaceLabel.setText("");
		numObjectsLabel.setText(getResourceString("details.NumberOfObjects.text",
			new Object[] { new Integer(files.length) }));
	}

	/**
	 * Sets the details appropriately for a selection with > 1 item
	 * 
	 * @param file the file in question
	 */
	/* package */ void setSelectionDetails(File[] file) {
		// not implemented
		clearDetails();
	}

	/**
	 * Blanks out the details
	 */
	/* package */ void clearDetails() {
		diskSpaceLabel.setText("");
		numObjectsLabel.setText("");
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
	/* package */ static File[] getDirectoryList(File file) {
		File[] list = file.listFiles();
		if (list == null) return new File[0];
		sortFiles(list);
		return list;
	}
	
	/**
	 * Gets file information for display purposes
	 * 
	 * @param file the file to query
	 * @return the requested information or null if not available
	 */
	/* package */ FileDisplayInfo getFileDisplayInfo(File file) {
		final String nameString = file.getName();
		final String dateString = dateFormat.format(new Date(file.lastModified()));
		final String sizeString;
		final String typeString;
		final Image icon;
		
		if (file.isDirectory()) {
			typeString = getResourceString("filetype.Folder");
			sizeString = "";
			icon = IconCache.stockImages[IconCache.iconClosedFolder];
		} else {
			sizeString = getResourceString("filesize.KB",
				new Object[] { new Long((file.length() + 512) / 1024) });
			
			int dot = nameString.lastIndexOf('.');
			if (dot != -1) {
				String extension = nameString.substring(dot);
				Program program = Program.findProgram(extension);
				if (program != null) {
					typeString = program.getName();
					icon = IconCache.getIconFromProgram(program, extension);
				} else {
					typeString = getResourceString("filetype.Unknown", new Object[] { extension.toUpperCase() });
					icon = IconCache.stockImages[IconCache.iconFile];
				}
			} else {
				typeString = getResourceString("filetype.None");
				icon = IconCache.stockImages[IconCache.iconFile];
			}
		}
		return new FileDisplayInfo(nameString, typeString, sizeString, dateString, icon);
	}
	/**
	 * Moves a file or entire directory structure.
	 * [Note only works within a specific volume on some platforms for now]
	 *
	 * @param oldFile the location of the old file or directory
	 * @param newFile the location of the new file or directory
	 * @return true iff the operation succeeds without errors
	 */
	/* package */ static boolean moveFileStructure(File oldFile, File newFile) {
		if (oldFile == null || newFile == null) return false;
		if (! oldFile.exists());
		if (newFile.exists());
		if (simulateOnly) {
			System.out.println(getResourceString("simulate.MoveFromTo.text",
				new Object[] { oldFile.getPath(), newFile.getPath() }));
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
	/* package */ static boolean copyFileStructure(File oldFile, File newFile) {
		if (oldFile == null || newFile == null) return false;		
		if (! oldFile.exists());
		if (newFile.exists());
		
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
				System.out.println(getResourceString("simulate.IgnoringUnknownResource.text",
					new Object[] { oldFile.getPath() }));
			}
			return true; // ignore it
		}
	}
	
	/**
	 * Sorts files lexicographically by name.
	 * 
	 * @param files the array of Files to be sorted
	 */
	/* package */ static void sortFiles(File[] files) {
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
}
