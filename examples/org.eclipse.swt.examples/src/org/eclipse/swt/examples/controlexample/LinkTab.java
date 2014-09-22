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

class LinkTab extends Tab {
	/* Example widgets and groups that contain them */
	Link link1;
	Group linkGroup;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	LinkTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	@Override
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the list */
		linkGroup = new Group (exampleGroup, SWT.NONE);
		linkGroup.setLayout (new GridLayout ());
		linkGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		linkGroup.setText ("Link");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	@Override
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (borderButton.getSelection ()) style |= SWT.BORDER;
	
		/* Create the example widgets */		
		link1 = new Link (linkGroup, style);
		link1.setText (ControlExample.getResourceString("LinkText"));
	}
	
	/**
	 * Creates the "Style" group.
	 */
	@Override
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		borderButton = new Button(styleGroup, SWT.CHECK);
		borderButton.setText("SWT.BORDER");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [] {link1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	@Override
	String[] getMethodNames() {
		return new String[] {"Text", "ToolTipText"};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "Link";
	}

}
