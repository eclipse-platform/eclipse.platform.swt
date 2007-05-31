/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;

class ListTab extends ScrollableTab {

	/* Example widgets and groups that contain them */
	List list1;
	Group listGroup;
	
	static String [] ListData1 = {ControlExample.getResourceString("ListData1_0"),
								  ControlExample.getResourceString("ListData1_1"),
								  ControlExample.getResourceString("ListData1_2"),
								  ControlExample.getResourceString("ListData1_3"),
								  ControlExample.getResourceString("ListData1_4"),
								  ControlExample.getResourceString("ListData1_5"),
								  ControlExample.getResourceString("ListData1_6"),
								  ControlExample.getResourceString("ListData1_7"),
								  ControlExample.getResourceString("ListData1_8")};

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ListTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the list */
		listGroup = new Group (exampleGroup, SWT.NONE);
		listGroup.setLayout (new GridLayout ());
		listGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		listGroup.setText ("List");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (singleButton.getSelection ()) style |= SWT.SINGLE;
		if (multiButton.getSelection ()) style |= SWT.MULTI;
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
	
		/* Create the example widgets */
		list1 = new List (listGroup, style);
		list1.setItems (ListData1);
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {list1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Items", "Selection", "ToolTipText", "TopIndex"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "List";
	}
}
