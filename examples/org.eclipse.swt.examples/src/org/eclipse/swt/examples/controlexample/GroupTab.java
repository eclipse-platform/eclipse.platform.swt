/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Widget;

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
	@Override
	void createOtherGroup () {
		super.createOtherGroup ();

		/* Create display controls specific to this example */
		titleButton = new Button (otherGroup, SWT.CHECK);
		titleButton.setText (ControlExample.getResourceString("Title_Text"));

		/* Add the listeners */
		titleButton.addSelectionListener (widgetSelectedAdapter(event -> setTitleText ()));
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
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
	@Override
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
	@Override
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
	}

	/**
	 * Gets the "Example" widget children.
	 */
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [] {group1};
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
	@Override
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
