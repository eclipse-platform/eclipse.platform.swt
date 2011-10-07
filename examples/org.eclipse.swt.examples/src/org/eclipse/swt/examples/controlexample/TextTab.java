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
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;

class TextTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	Text text;
	Group textGroup;

	/* Style widgets added to the "Style" group */
	Button wrapButton, readOnlyButton, passwordButton, searchButton, iconCancelButton, iconSearchButton;
	Button leftButton, centerButton, rightButton;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TextTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the text widget */
		textGroup = new Group (exampleGroup, SWT.NONE);
		textGroup.setLayout (new GridLayout ());
		textGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		textGroup.setText ("Text");
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
		if (wrapButton.getSelection ()) style |= SWT.WRAP;
		if (readOnlyButton.getSelection ()) style |= SWT.READ_ONLY;
		if (passwordButton.getSelection ()) style |= SWT.PASSWORD;
		if (searchButton.getSelection ()) style |= SWT.SEARCH;
		if (iconCancelButton.getSelection ()) style |= SWT.ICON_CANCEL;
		if (iconSearchButton.getSelection ()) style |= SWT.ICON_SEARCH;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (leftButton.getSelection ()) style |= SWT.LEFT;
		if (centerButton.getSelection ()) style |= SWT.CENTER;
		if (rightButton.getSelection ()) style |= SWT.RIGHT;
	
		/* Create the example widgets */
		text = new Text (textGroup, style);
		text.setText (ControlExample.getResourceString("Example_string") + Text.DELIMITER + ControlExample.getResourceString("One_Two_Three"));
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widgets */
		wrapButton = new Button (styleGroup, SWT.CHECK);
		wrapButton.setText ("SWT.WRAP");
		readOnlyButton = new Button (styleGroup, SWT.CHECK);
		readOnlyButton.setText ("SWT.READ_ONLY");
		passwordButton = new Button (styleGroup, SWT.CHECK);
		passwordButton.setText ("SWT.PASSWORD");
		searchButton = new Button (styleGroup, SWT.CHECK);
		searchButton.setText ("SWT.SEARCH");
		iconCancelButton = new Button (styleGroup, SWT.CHECK);
		iconCancelButton.setText ("SWT.ICON_CANCEL");
		iconSearchButton = new Button (styleGroup, SWT.CHECK);
		iconSearchButton.setText ("SWT.ICON_SEARCH");

		Composite alignmentGroup = new Composite (styleGroup, SWT.NONE);
		GridLayout layout = new GridLayout ();
		layout.marginWidth = layout.marginHeight = 0;
		alignmentGroup.setLayout (layout);
		alignmentGroup.setLayoutData (new GridData (GridData.FILL_BOTH));
		leftButton = new Button (alignmentGroup, SWT.RADIO);
		leftButton.setText ("SWT.LEFT");
		centerButton = new Button (alignmentGroup, SWT.RADIO);
		centerButton.setText ("SWT.CENTER");
		rightButton = new Button (alignmentGroup, SWT.RADIO);
		rightButton.setText ("SWT.RIGHT");
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
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {text};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"DoubleClickEnabled", "EchoChar", "Editable", "Message", "Orientation", "Selection", "Tabs", "Text", "TextLimit", "ToolTipText", "TopIndex"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Text";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		wrapButton.setSelection ((text.getStyle () & SWT.WRAP) != 0);
		readOnlyButton.setSelection ((text.getStyle () & SWT.READ_ONLY) != 0);
		passwordButton.setSelection ((text.getStyle () & SWT.PASSWORD) != 0);
		searchButton.setSelection ((text.getStyle () & SWT.SEARCH) != 0);
		leftButton.setSelection ((text.getStyle () & SWT.LEFT) != 0);
		centerButton.setSelection ((text.getStyle () & SWT.CENTER) != 0);
		rightButton.setSelection ((text.getStyle () & SWT.RIGHT) != 0);
		
		/* Special case: ICON_CANCEL and H_SCROLL have the same value,
		 * and ICON_SEARCH and V_SCROLL have the same value,
		 * so to avoid confusion, only set CANCEL if SEARCH is set. */
		if ((text.getStyle () & SWT.SEARCH) != 0) {
			iconCancelButton.setSelection ((text.getStyle () & SWT.ICON_CANCEL) != 0);
			iconSearchButton.setSelection ((text.getStyle () & SWT.ICON_SEARCH) != 0);
			horizontalButton.setSelection (false);
			verticalButton.setSelection (false);
		} else {
			iconCancelButton.setSelection (false);
			iconSearchButton.setSelection (false);
			horizontalButton.setSelection ((text.getStyle () & SWT.H_SCROLL) != 0);
			verticalButton.setSelection ((text.getStyle () & SWT.V_SCROLL) != 0);
		}

		passwordButton.setEnabled ((text.getStyle () & SWT.SINGLE) != 0);
		searchButton.setEnabled ((text.getStyle () & SWT.SINGLE) != 0);
		iconCancelButton.setEnabled ((text.getStyle () & SWT.SEARCH) != 0);
		iconSearchButton.setEnabled ((text.getStyle () & SWT.SEARCH) != 0);
		wrapButton.setEnabled ((text.getStyle () & SWT.MULTI) != 0);
		horizontalButton.setEnabled ((text.getStyle () & SWT.MULTI) != 0);
		verticalButton.setEnabled ((text.getStyle () & SWT.MULTI) != 0);
	}
}
