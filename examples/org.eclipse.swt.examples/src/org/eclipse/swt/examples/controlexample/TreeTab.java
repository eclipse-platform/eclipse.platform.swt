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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class TreeTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	Tree tree1, tree2;
	Group treeGroup, imageTreeGroup;
	
	/* Style widgets added to the "Style" group */
	Button checkButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TreeTab(ControlExample instance) {
		super(instance);
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
		treeGroup.setText ("Tree");
	
		/* Create a group for the image tree */
		imageTreeGroup = new Group (exampleGroup, SWT.NONE);
		imageTreeGroup.setLayout (new GridLayout ());
		imageTreeGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		imageTreeGroup.setText (ControlExample.getResourceString("Tree_With_Images"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (singleButton.getSelection()) style |= SWT.SINGLE;
		if (multiButton.getSelection()) style |= SWT.MULTI;
		if (checkButton.getSelection()) style |= SWT.CHECK;
		if (borderButton.getSelection()) style |= SWT.BORDER;
	
		/* Create the text tree */
		tree1 = new Tree (treeGroup, style);
		TreeItem node1 = new TreeItem (tree1, SWT.NONE);
		node1.setText (ControlExample.getResourceString("Node_1"));
		TreeItem node2 = new TreeItem (tree1, SWT.NONE);
		node2.setText (ControlExample.getResourceString("Node_2"));
		TreeItem node3 = new TreeItem (tree1, SWT.NONE);
		node3.setText (ControlExample.getResourceString("Node_3"));
		TreeItem node4 = new TreeItem (tree1, SWT.NONE);
		node4.setText (ControlExample.getResourceString("Node_4"));
		TreeItem node1_1 = new TreeItem (node1, SWT.NONE);
		node1_1.setText (ControlExample.getResourceString("Node_1_1"));
		TreeItem node2_1 = new TreeItem (node2, SWT.NONE);
		node2_1.setText (ControlExample.getResourceString("Node_2_1"));
		TreeItem node3_1 = new TreeItem (node3, SWT.NONE);
		node3_1.setText (ControlExample.getResourceString("Node_3_1"));
		TreeItem node2_2 = new TreeItem (node2, SWT.NONE);
		node2_2.setText (ControlExample.getResourceString("Node_2_2"));
		TreeItem node2_2_1 = new TreeItem (node2_2, SWT.NONE);
		node2_2_1.setText (ControlExample.getResourceString("Node_2_2_1"));
	
		/* Create the image tree */	
		tree2 = new Tree (imageTreeGroup, style);
		node1 = new TreeItem (tree2, SWT.NONE);
		node1.setText (ControlExample.getResourceString("Node_1"));
		node1.setImage (instance.images[ControlExample.ciClosedFolder]);
		node2 = new TreeItem (tree2, SWT.NONE);
		node2.setText (ControlExample.getResourceString("Node_2"));
		node2.setImage (instance.images[ControlExample.ciClosedFolder]);
		node3 = new TreeItem (tree2, SWT.NONE);
		node3.setText (ControlExample.getResourceString("Node_3"));
		node3.setImage (instance.images[ControlExample.ciClosedFolder]);
		node4 = new TreeItem (tree2, SWT.NONE);
		node4.setText (ControlExample.getResourceString("Node_4"));
		node4.setImage (instance.images[ControlExample.ciClosedFolder]);
		node1_1 = new TreeItem (node1, SWT.NONE);
		node1_1.setText (ControlExample.getResourceString("Node_1_1"));
		node1_1.setImage (instance.images[ControlExample.ciClosedFolder]);
		node2_1 = new TreeItem (node2, SWT.NONE);
		node2_1.setText (ControlExample.getResourceString("Node_2_1"));
		node2_1.setImage (instance.images[ControlExample.ciClosedFolder]);
		node3_1 = new TreeItem (node3, SWT.NONE);
		node3_1.setText (ControlExample.getResourceString("Node_3_1"));
		node3_1.setImage (instance.images[ControlExample.ciClosedFolder]);
		node2_2 = new TreeItem(node2, SWT.NONE);
		node2_2.setText (ControlExample.getResourceString("Node_2_2"));
		node2_2.setImage (instance.images[ControlExample.ciClosedFolder]);
		node2_2_1 = new TreeItem (node2_2, SWT.NONE);
		node2_2_1.setText (ControlExample.getResourceString("Node_2_2_1"));
		node2_2_1.setImage (instance.images[ControlExample.ciClosedFolder]);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
		
		/* Create the extra widgets */
		checkButton = new Button (styleGroup, SWT.CHECK);
		checkButton.setText ("SWT.CHECK");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {tree1, tree2};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Tree";
	}
}
