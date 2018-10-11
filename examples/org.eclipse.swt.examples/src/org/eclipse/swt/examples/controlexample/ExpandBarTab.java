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


import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Widget;

class ExpandBarTab extends Tab {
	/* Example widgets and groups that contain them */
	ExpandBar expandBar1;
	Group expandBarGroup;

	/* Style widgets added to the "Style" group */
	Button verticalButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ExpandBarTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
	void createExampleGroup () {
		super.createExampleGroup ();

		/* Create a group for the list */
		expandBarGroup = new Group (exampleGroup, SWT.NONE);
		expandBarGroup.setLayout (new GridLayout ());
		expandBarGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		expandBarGroup.setText ("ExpandBar");
	}

	/**
	 * Creates the "Example" widgets.
	 */
	@Override
	void createExampleWidgets () {

		/* Compute the widget style */
		int style = getDefaultStyle();
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (verticalButton.getSelection()) style |= SWT.V_SCROLL;

		/* Create the example widgets */
		expandBar1 = new ExpandBar (expandBarGroup, style);

		// First item
		Composite composite = new Composite (expandBar1, SWT.NONE);
		composite.setLayout(new GridLayout ());
		new Button (composite, SWT.PUSH).setText("SWT.PUSH");
		new Button (composite, SWT.RADIO).setText("SWT.RADIO");
		new Button (composite, SWT.CHECK).setText("SWT.CHECK");
		new Button (composite, SWT.TOGGLE).setText("SWT.TOGGLE");
		ExpandItem item = new ExpandItem (expandBar1, SWT.NONE, 0);
		item.setText(ControlExample.getResourceString("Item1_Text"));
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);
		item.setImage(instance.images[ControlExample.ciClosedFolder]);

		// Second item
		composite = new Composite (expandBar1, SWT.NONE);
		composite.setLayout(new GridLayout (2, false));
		new Label (composite, SWT.NONE).setImage(display.getSystemImage(SWT.ICON_ERROR));
		new Label (composite, SWT.NONE).setText("SWT.ICON_ERROR");
		new Label (composite, SWT.NONE).setImage(display.getSystemImage(SWT.ICON_INFORMATION));
		new Label (composite, SWT.NONE).setText("SWT.ICON_INFORMATION");
		new Label (composite, SWT.NONE).setImage(display.getSystemImage(SWT.ICON_WARNING));
		new Label (composite, SWT.NONE).setText("SWT.ICON_WARNING");
		new Label (composite, SWT.NONE).setImage(display.getSystemImage(SWT.ICON_QUESTION));
		new Label (composite, SWT.NONE).setText("SWT.ICON_QUESTION");
		item = new ExpandItem (expandBar1, SWT.NONE, 1);
		item.setText(ControlExample.getResourceString("Item2_Text"));
		item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		item.setControl(composite);
		item.setImage(instance.images[ControlExample.ciOpenFolder]);
		item.setExpanded(true);
	}

	/**
	 * Creates the "Style" group.
	 */
	@Override
	void createStyleGroup() {
		super.createStyleGroup ();

		/* Create the extra widgets */
		verticalButton = new Button (styleGroup, SWT.CHECK);
		verticalButton.setText ("SWT.V_SCROLL");
		verticalButton.setSelection(true);
		borderButton = new Button(styleGroup, SWT.CHECK);
		borderButton.setText("SWT.BORDER");
	}

	/**
	 * Gets the "Example" widget children.
	 */
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [] {expandBar1};
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	@Override
	String[] getMethodNames() {
		return new String[] {"Spacing"};
	}

	/**
	 * Gets the short text for the tab folder item.
	 */
	@Override
	String getShortTabText() {
		return "EB";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "ExpandBar";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	@Override
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		Widget [] widgets = getExampleWidgets ();
		if (widgets.length != 0){
			verticalButton.setSelection ((widgets [0].getStyle () & SWT.V_SCROLL) != 0);
			borderButton.setSelection ((widgets [0].getStyle () & SWT.BORDER) != 0);
		}
	}
}
