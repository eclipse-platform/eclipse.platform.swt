package org.eclipse.swt.examples.launcher;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import java.net.*;import org.eclipse.jface.dialogs.*;import org.eclipse.swt.*;import org.eclipse.swt.events.*;import org.eclipse.swt.layout.*;import org.eclipse.swt.widgets.*;import org.eclipse.ui.part.*;

/**
 * Launcher uses <code>org.eclipse.swt</code> and <code>org.eclipse.core</code>
 * to launch the other registered examples.
 * 
 * @see ViewPart
 */
public class LauncherView extends ViewPart {
	private Shell workbenchShell;
	
	private Tree launchTree;
	private Text descriptionText;
	private Button importButton;
	private Button runButton;

	/**
	 * Constructs a LauncherView.
	 */
	public LauncherView() {
		LauncherPlugin.initResources();
	}

	/**
	 * Creates the example.
	 * 
	 * @see ViewPart#createPartControl
	 */
	public void createPartControl(Composite parent) {
		workbenchShell = getSite().getShell();
		parent.setLayout(new SplitLayout());
				
		Group launchGroup = new Group(parent, SWT.NULL);
		launchGroup.setText(LauncherPlugin.getResourceString("view.launchGroup.text"));
		
		GridLayout gridLayout = new GridLayout();
		gridLayout.numColumns = 2;
		launchGroup.setLayout(gridLayout);

		launchTree = new Tree(launchGroup, SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 2;
		launchTree.setLayoutData(gridData);
		launchTree.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				final ItemDescriptor item = getSelectedItem();
				setDescriptionByItem(item);
			}
			public void widgetDefaultSelected(SelectionEvent event) {
				final ItemDescriptor item = getSelectedItem();
				setDescriptionByItem(item);
				runItem(getSelectedItem());
			}
		});
		launchTree.addTreeListener(new TreeListener() {
			public void treeCollapsed(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				if (item == null) return;
				item.setImage(LauncherPlugin.images[LauncherPlugin.liClosedFolder]);
			}
			public void treeExpanded(TreeEvent event) {
				final TreeItem item = (TreeItem) event.item;
				if (item == null) return;
				item.setImage(LauncherPlugin.images[LauncherPlugin.liOpenFolder]);
			}
		});

		runButton = new Button(launchGroup, SWT.PUSH);
		runButton.setText(LauncherPlugin.getResourceString("view.launchButton.text"));
		runButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				runItem(getSelectedItem());
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		
		importButton = new Button(launchGroup, SWT.PUSH);
		importButton.setText(LauncherPlugin.getResourceString("view.importButton.text"));
		importButton.addSelectionListener(new SelectionListener() {
			public void widgetSelected(SelectionEvent event) {
				importItem(getSelectedItem());
			}
			public void widgetDefaultSelected(SelectionEvent event) {
			}
		});
		

		Group descriptionGroup = new Group(parent, SWT.NULL);
		descriptionGroup.setText(LauncherPlugin.getResourceString("view.descriptionGroup.text"));
		descriptionGroup.setLayout(new FillLayout());
		
		descriptionText = new Text(descriptionGroup, SWT.MULTI | SWT.BORDER |
			SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY);

		setDescriptionByItem(null);
		setItemDescriptors(LauncherPlugin.getLaunchItemTree());
	}

	/**
	 * Called when we must grab focus.
	 * 
	 * @see org.eclipse.ui.part.ViewPart#setFocus
	 */
	public void setFocus()  {
		launchTree.setFocus();
		runButton.getShell().setDefaultButton(runButton);
	}

	/**
	 * Called when the View is to be disposed
	 */	
	public void dispose() {
		workbenchShell = null;
		launchTree = null;
		descriptionText = null;
		importButton = null;
		runButton = null;		
		super.dispose();
	}

	/**
	 * Installs a new launch list.
	 * 
	 * @param newRoot the new tree of launch items for the UI
	 */
	public void setItemDescriptors(final ItemTreeNode newRoot) {
		if (workbenchShell == null) return;
		workbenchShell.getDisplay().syncExec(new Runnable() {
			public void run() {
				if ((launchTree == null) || (launchTree.isDisposed())) return;
				launchTree.removeAll();
			
				for (ItemTreeNode node = newRoot.getFirstChild(); node != null;
					node = node.getNextSibling()) {
					doNode(node, new TreeItem(launchTree, SWT.NONE)); // top-level TreeItem
				}
			}
			private void addGroup(TreeItem parent, ItemTreeNode node) {
				for (;node != null; node = node.getNextSibling()) {
					doNode(node, new TreeItem(parent, SWT.NONE)); // TreeItem at depth > 0
				}
			}
			private void doNode(ItemTreeNode node, TreeItem treeItem) {
				final ItemDescriptor item = node.getDescriptor();
				treeItem.setText(item.getName());
				treeItem.setData(item);
				if (node.getDescriptor().isFolder()) {
					treeItem.setExpanded(false);
					treeItem.setImage(LauncherPlugin.images[LauncherPlugin.liClosedFolder]);
				} else {
					treeItem.setImage(node.getDescriptor().getIcon());
				}
				addGroup(treeItem, node.getFirstChild());
			}
		});
	}

	/**
	 * Runs the specified launch item.
	 * 
	 * @param itemDescriptor the launch item to execute
	 */
	private void runItem(final ItemDescriptor itemDescriptor) {
		if ((workbenchShell == null) || (itemDescriptor == null)) return;
		final LaunchDelegate launchDelegate = itemDescriptor.getLaunchDelegate();
		if (launchDelegate == null) return;		
		
		boolean result = launchDelegate.launch(this);
		if (! result) {
			MessageDialog.openError(workbenchShell,
				LauncherPlugin.getResourceString("dialog.RunProgramProblems.title"),
				LauncherPlugin.getResourceString("dialog.RunProgramErrorCheckLog.message",
				new Object[] { itemDescriptor.getName() }));
		}					
	}

	/**
	 * Imports the specified launch item into the workspace.
	 * 
	 * @param itemDescriptor the launch item to import.
	 */
	private void importItem(final ItemDescriptor itemDescriptor) {
		if ((workbenchShell == null) || (itemDescriptor == null)) return;

		URL sourceUrl = itemDescriptor.getSourceCodePath();
		if (sourceUrl == null) return; // oops! can't do that!
		
		ImportProjectTask task = new ImportProjectTask(workbenchShell, itemDescriptor.getName(),
			sourceUrl);
		boolean success = task.execute();
		task.dispose();
	}
	/**
	 * Obtains the selected launch item.
	 * 
	 * @return the currently selected ItemDescriptor
	 */
	private ItemDescriptor getSelectedItem() {
		final TreeItem[] selections = launchTree.getSelection();
		if (selections.length == 0) return null;
		final ItemDescriptor itemDescriptor = (ItemDescriptor) selections[0].getData();
		return itemDescriptor;
	}
	
	/**
	 * Sets the currently visible description text to reflect that of a particular ItemDescriptor.
	 *
	 * @param itemDescriptor the launch item whose description is to be displayed, or null if none
	 */
	private void setDescriptionByItem(ItemDescriptor itemDescriptor) {
		String description;
		if (itemDescriptor == null) {
			description = LauncherPlugin.getResourceString("launchitem.Null.description");
			if (runButton != null) runButton.setEnabled(false);
			if (importButton != null) importButton.setEnabled(false);
		} else {
			description = itemDescriptor.getDescription();
			if (description == null)
				description = LauncherPlugin.getResourceString("launchitem.Missing.description");
				
			if (runButton != null) runButton.setEnabled(itemDescriptor.getLaunchDelegate() != null);
			if (importButton != null) importButton.setEnabled(itemDescriptor.getSourceCodePath() != null);
		}
		descriptionText.setText(description);
	}
}