package org.eclipse.swt.examples.explorer;
/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.*;import org.eclipse.swt.dnd.*;import org.eclipse.swt.events.*;import org.eclipse.swt.graphics.*;import org.eclipse.swt.widgets.*;import java.io.*;import java.util.*;

class TreeView {
	private static final String
		TREEITEMDATA_FILE           = "TreeItem.file",
			// File: File associated with tree item
		TREEITEMDATA_IMAGEEXPANDED  = "TreeItem.imageExpanded",
			// Image: shown when item is expanded
		TREEITEMDATA_IMAGECOLLAPSED = "TreeItem.imageCollapsed";
			// Image: shown when item is collapsed

	/* package */ final Tree tree;
	private final Shell shell;
	private final Display display;
	private final FileViewer viewer;

	/**
	 * Creates the file tree table.
	 * 
	 * @param theViewer the viewer to attach to
	 * @param parent the parent control
	 */
	public TreeView(FileViewer theViewer, Composite parent) {
		this.viewer = theViewer;
		shell = parent.getShell();
		display = shell.getDisplay();

		tree = new Tree(parent, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL | SWT.SINGLE);
		tree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				final TreeItem[] selection = tree.getSelection();
				if (selection != null && selection.length != 0) {
					TreeItem item = selection[0];
					File file = (File) item.getData(TREEITEMDATA_FILE);
				
					viewer.notifySelectedDirectory(file);
				}
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
	 * Creates the Drag & Drop DragSource for items being dragged from the tree.
	 * 
	 * @return the DragSource for the tree
	 */
	private DragSource createTreeDragSource(final Tree tree){
		DragSource dragSource = new DragSource(tree, DND.DROP_MOVE | DND.DROP_COPY);
		dragSource.setTransfer(new Transfer[] { FileTransfer.getInstance() });
		dragSource.addDragListener(new DragSourceListener() {
			public void dragStart(DragSourceEvent event){
				event.doit = tree.getSelectionCount() > 0;
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
				viewer.validateDrop(event, getTargetFile(event));
			}
			public void drop(DropTargetEvent event) {
				viewer.performDrop(event, getTargetFile(event));
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
	 * Populate an item in the tree with a complete directory listing.
	 * 
	 * @param item the TreeItem to fill in
	 */
	private void expandTreeItem(TreeItem item) {
		final File file = (File) item.getData(TREEITEMDATA_FILE);

		/* Get directory listing */
		shell.setCursor(Images.CursorWait);
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
	 * Listens to selectedDirectory events.
	 * <p>
	 * If not already expanded, recursively expands the parents of the specified
	 * directory until it is visible.
	 * </p>
	 * 
	 * @param dir the directory that was selected, null is ignored
	 */
	/* package */ void selectedDirectory(File dir) {
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
			TreeItem item = searchItems(items, pathElement);
			if (item == null) break;
			lastItem = item;
			if (! item.getExpanded()) expandTreeItem(item);
			items = item.getItems();
		}
		tree.setSelection((lastItem != null) ? new TreeItem[] { lastItem } : new TreeItem[0]);
	}
	
	private TreeItem searchItems(TreeItem[] items, File element) {
		// No guarantee that the items are sorted, so we'll just sequential scan
		// shouldn't be more than a couple hundred entries, anyway.
		for (int i = 0; i < items.length; ++i) {
			final TreeItem item = items[i];
			if (item.isDisposed()) continue;
			final File itemFile = (File) item.getData(TREEITEMDATA_FILE);
			if (itemFile != null && itemFile.equals(element)) return item;
		}
		return null;
	}
	
	/**
	 * Listens to refreshFiles events.
	 * <p>
	 * Refreshes information about any files in the list and their children.
	 * </p>
	 * 
	 * @param files the list of files to be refreshed, null refreshes everything
	 */
	/* package */ void refreshFiles(File[] files) {
		if (files == null) {
			File[] roots = viewer.getRoots();
	
			for (int i = 0; i < roots.length; ++i) {
				final File file = roots[i];
				TreeItem item = new TreeItem(tree, SWT.NULL);
				initTreeItemVolume(item, file);
	
				// add a placeholder child item so we get the "expand" button
				TreeItem placeholderItem = new TreeItem(item, SWT.NULL);
			}
		}
	}

	/**
	 * Initializes a volume item.
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
	 * Initializes a folder item.
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
}
