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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class SashFormTab extends Tab {
	/* Example widgets and groups that contain them */
	SashForm fullForm, topForm;
	List list1, list2, list3;
	Text text;

	static String [] ListData0 = {ControlExample.getResourceString("ListData0_0"),
								  ControlExample.getResourceString("ListData0_1"),
								  ControlExample.getResourceString("ListData0_2"),
								  ControlExample.getResourceString("ListData0_3"),
								  ControlExample.getResourceString("ListData0_4"),
								  ControlExample.getResourceString("ListData0_5"),
								  ControlExample.getResourceString("ListData0_6"),
								  ControlExample.getResourceString("ListData0_7"),
								  ControlExample.getResourceString("ListData0_8")};
								  
	static String [] ListData1 = {ControlExample.getResourceString("ListData1_0"),
								  ControlExample.getResourceString("ListData1_1"),
								  ControlExample.getResourceString("ListData1_2"),
								  ControlExample.getResourceString("ListData1_3"),
								  ControlExample.getResourceString("ListData1_4"),
								  ControlExample.getResourceString("ListData1_5"),
								  ControlExample.getResourceString("ListData1_6"),
								  ControlExample.getResourceString("ListData1_7"),
								  ControlExample.getResourceString("ListData1_8")};

	/* Constants */
	static final int SASH_WIDTH = 3;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	SashFormTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the tab folder page.
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		tabFolderPage = new Composite(tabFolder, SWT.BORDER);
		tabFolderPage.setLayout(new FillLayout());
		
		fullForm = new SashForm (tabFolderPage, SWT.HORIZONTAL);
		fullForm.setLayout(new FillLayout());
		topForm = new SashForm (fullForm, SWT.VERTICAL);
		topForm.setLayout(new FillLayout());
		
		list1 = new List (topForm, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		list1.setItems (ListData0);
		list2 = new List (topForm, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		list2.setItems (ListData1);
		text = new Text (fullForm, SWT.MULTI | SWT.BORDER);
		text.setText (ControlExample.getResourceString("Multi_line"));

		fullForm.setWeights(new int[] {50, 50});
		topForm.setWeights(new int[] {50, 50});
		
		return tabFolderPage; 
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {fullForm, topForm};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "SashForm";
	}
	
	/**
	 * Recreates the "Example" widgets.
	 */
	void recreateExampleWidgets () {
		// this tab does not use this framework mechanism
	}
}
