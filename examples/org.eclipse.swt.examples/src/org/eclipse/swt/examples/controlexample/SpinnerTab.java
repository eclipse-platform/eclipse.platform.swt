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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

class SpinnerTab extends Tab {

	/* Example widgets and groups that contain them */
	Spinner spinner1;
	Group spinnerGroup;
	
	/* Style widgets added to the "Style" group */
	Button readOnlyButton;
	
	static String [] ListData = {ControlExample.getResourceString("ListData0_0"),
								 ControlExample.getResourceString("ListData0_1"),
								 ControlExample.getResourceString("ListData0_2"),
								 ControlExample.getResourceString("ListData0_3"),
								 ControlExample.getResourceString("ListData0_4"),
								 ControlExample.getResourceString("ListData0_5"),
								 ControlExample.getResourceString("ListData0_6"),
								 ControlExample.getResourceString("ListData0_7"),
								 ControlExample.getResourceString("ListData0_8")};

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	SpinnerTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the spinner */
		spinnerGroup = new Group (exampleGroup, SWT.NONE);
		spinnerGroup.setLayout (new GridLayout ());
		spinnerGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		spinnerGroup.setText ("Spinner");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (readOnlyButton.getSelection ()) style |= SWT.READ_ONLY;
		
		/* Create the example widgets */
		spinner1 = new Spinner (spinnerGroup, style);
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				setExampleWidgetSize ();
			}
		});
		
		return tabFolderPage;
	}

	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();
	
		/* Create the extra widgets */
		readOnlyButton = new Button (styleGroup, SWT.CHECK);
		readOnlyButton.setText ("SWT.READ_ONLY");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {spinner1};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Spinner";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		readOnlyButton.setSelection ((spinner1.getStyle () & SWT.READ_ONLY) != 0);
	}
}
