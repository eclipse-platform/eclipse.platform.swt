/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import org.eclipse.swt.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class TableTreeTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	TableTree tree1;
	Group treeGroup;
	
	/* Style widgets added to the "Style" group */
	Button checkButton, fullSelectionButton;
	
	/* Display widgets added to the "Display" group */
	Button headerVisibleButton, linesVisibleButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TableTreeTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Display" group.
	 */
	void createDisplayGroup () {
		super.createDisplayGroup ();
	
		/* Create display controls specific to this example */
		headerVisibleButton = new Button (displayGroup, SWT.CHECK);
		headerVisibleButton.setText (ControlExample.getResourceString("Header_Visible"));
		linesVisibleButton = new Button (displayGroup, SWT.CHECK);
		linesVisibleButton.setText (ControlExample.getResourceString("Lines_Visible"));
	
		/* Add the listeners */
		headerVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetHeaderVisible();
			}
		});
		linesVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetLinesVisible();
			};
		});
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the text tree */
		treeGroup = new Group (exampleGroup, SWT.NONE);
		treeGroup.setLayout (new GridLayout ());
		treeGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		treeGroup.setText ("TableTree");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		/* Compute the widget style */
		int style = SWT.NONE;
		if (singleButton.getSelection()) style |= SWT.SINGLE;
		if (multiButton.getSelection()) style |= SWT.MULTI;
		if (checkButton.getSelection()) style |= SWT.CHECK;
		if (borderButton.getSelection()) style |= SWT.BORDER;
		if (fullSelectionButton.getSelection()) style |= SWT.FULL_SELECTION;
	
		/* Create the text tree */
		tree1 = new TableTree (treeGroup, style);
		Table table = tree1.getTable();
		for (int i = 0; i < 3; i++) {
			TableColumn column = new TableColumn (table,SWT.NONE);
			column.setWidth(100);
			column.setText(ControlExample.getResourceString("TableTree_column") + ": " + i);
		}
		TableTreeItem node1 = new TableTreeItem (tree1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node1.setText (i, ControlExample.getResourceString("Node_1") + "-" + i);
		}
		node1.setImage (instance.images[ControlExample.ciOpenFolder]);
		TableTreeItem node2 = new TableTreeItem (tree1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node2.setText (i, ControlExample.getResourceString("Node_2") + "-" + i);
		}
		node2.setImage (instance.images[ControlExample.ciClosedFolder]);
		TableTreeItem node3 = new TableTreeItem (tree1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node3.setText (i, ControlExample.getResourceString("Node_3") + "-" + i);
		}
		node3.setImage (instance.images[ControlExample.ciTarget]);
		TableTreeItem node4 = new TableTreeItem (tree1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node4.setText (i, ControlExample.getResourceString("Node_4") + "-" + i);
		}
		node4.setImage (instance.images[ControlExample.ciOpenFolder]);
		TableTreeItem node1_1 = new TableTreeItem (node1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node1_1.setText (i, ControlExample.getResourceString("Node_1_1") + "-" + i);
		}
		node1_1.setImage (instance.images[ControlExample.ciClosedFolder]);
		TableTreeItem node2_1 = new TableTreeItem (node2, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node2_1.setText (i, ControlExample.getResourceString("Node_2_1") + "-" + i);
		}
		node2_1.setImage (instance.images[ControlExample.ciTarget]);
		TableTreeItem node3_1 = new TableTreeItem (node3, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node3_1.setText (i, ControlExample.getResourceString("Node_3_1") + "-" + i);
		}
		node3_1.setImage (instance.images[ControlExample.ciOpenFolder]);
		TableTreeItem node2_2 = new TableTreeItem (node2, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node2_2.setText (i, ControlExample.getResourceString("Node_2_2") + "-" + i);
		}
		node2_2.setImage (instance.images[ControlExample.ciClosedFolder]);
		TableTreeItem node2_2_1 = new TableTreeItem (node2_2, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node2_2_1.setText (i, ControlExample.getResourceString("Node_2_2_1") + "-" + i);
		}
		node2_2_1.setImage (instance.images[ControlExample.ciTarget]);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
		
		/* Create the extra widgets */
		checkButton = new Button (styleGroup, SWT.CHECK);
		checkButton.setText ("SWT.CHECK");
		fullSelectionButton = new Button (styleGroup, SWT.CHECK);
		fullSelectionButton.setText ("SWT.FULL_SELECTION");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {tree1};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "TableTree";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		setWidgetHeaderVisible ();
		setWidgetLinesVisible ();
	}
	
	/**
	 * Sets the header visible state of the "Example" widgets.
	 */
	void setWidgetHeaderVisible () {
		tree1.getTable().setHeaderVisible (headerVisibleButton.getSelection ());
	}
	
	/**
	 * Sets the lines visible state of the "Example" widgets.
	 */
	void setWidgetLinesVisible () {
		tree1.getTable().setLinesVisible (linesVisibleButton.getSelection ());
	}
}
