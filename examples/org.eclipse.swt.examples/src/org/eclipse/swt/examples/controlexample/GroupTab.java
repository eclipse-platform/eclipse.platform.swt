/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class GroupTab extends Tab {
	Button titleButton;
	
	/* Example widgets and groups that contain them */
	Group group1;
	Group groupGroup;
	
	/* Style widgets added to the "Style" group */
	Button shadowEtchedInButton, shadowEtchedOutButton, shadowInButton, shadowOutButton, shadowNoneButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	GroupTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Other" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
	
		/* Create display controls specific to this example */
		titleButton = new Button (otherGroup, SWT.CHECK);
		titleButton.setText (ControlExample.getResourceString("Title_Text"));
	
		/* Add the listeners */
		titleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setTitleText ();
			}
		});
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the Group */
		groupGroup = new Group (exampleGroup, SWT.NONE);
		groupGroup.setLayout (new GridLayout ());
		groupGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		groupGroup.setText ("Group");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (shadowEtchedInButton.getSelection ()) style |= SWT.SHADOW_ETCHED_IN;
		if (shadowEtchedOutButton.getSelection ()) style |= SWT.SHADOW_ETCHED_OUT;
		if (shadowInButton.getSelection ()) style |= SWT.SHADOW_IN;
		if (shadowOutButton.getSelection ()) style |= SWT.SHADOW_OUT;
		if (shadowNoneButton.getSelection ()) style |= SWT.SHADOW_NONE;
		if (borderButton.getSelection ()) style |= SWT.BORDER;

		/* Create the example widgets */
		group1 = new Group (groupGroup, style);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		shadowEtchedInButton = new Button (styleGroup, SWT.RADIO);
		shadowEtchedInButton.setText ("SWT.SHADOW_ETCHED_IN");
		shadowEtchedInButton.setSelection(true);
		shadowEtchedOutButton = new Button (styleGroup, SWT.RADIO);
		shadowEtchedOutButton.setText ("SWT.SHADOW_ETCHED_OUT");
		shadowInButton = new Button (styleGroup, SWT.RADIO);
		shadowInButton.setText ("SWT.SHADOW_IN");
		shadowOutButton = new Button (styleGroup, SWT.RADIO);
		shadowOutButton.setText ("SWT.SHADOW_OUT");
		shadowNoneButton = new Button (styleGroup, SWT.RADIO);
		shadowNoneButton.setText ("SWT.SHADOW_NONE");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	
		/* Add the listeners */
		SelectionListener selectionListener = new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				if (!((Button) event.widget).getSelection ()) return;
				recreateExampleWidgets ();
			}
		};
		shadowEtchedInButton.addSelectionListener (selectionListener);
		shadowEtchedOutButton.addSelectionListener (selectionListener);
		shadowInButton.addSelectionListener (selectionListener);
		shadowOutButton.addSelectionListener (selectionListener);
		shadowNoneButton.addSelectionListener (selectionListener);
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {group1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"ToolTipText"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Group";
	}

	/**
	 * Sets the title text of the "Example" widgets.
	 */
	void setTitleText () {
		if (titleButton.getSelection ()) {
			group1.setText (ControlExample.getResourceString("Title_Text"));
		} else {
			group1.setText ("");
		}
		setExampleWidgetSize ();
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		shadowEtchedInButton.setSelection ((group1.getStyle () & SWT.SHADOW_ETCHED_IN) != 0);
		shadowEtchedOutButton.setSelection ((group1.getStyle () & SWT.SHADOW_ETCHED_OUT) != 0);
		shadowInButton.setSelection ((group1.getStyle () & SWT.SHADOW_IN) != 0);
		shadowOutButton.setSelection ((group1.getStyle () & SWT.SHADOW_OUT) != 0);
		shadowNoneButton.setSelection ((group1.getStyle () & SWT.SHADOW_NONE) != 0);
		borderButton.setSelection ((group1.getStyle () & SWT.BORDER) != 0);
		if (!instance.startup) setTitleText ();
	}
}
