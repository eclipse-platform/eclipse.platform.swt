/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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

class TabFolderTab extends Tab {
	/* Example widgets and groups that contain them */
	TabFolder tabFolder1;
	Group tabFolderGroup;
	
	/* Style widgets added to the "Style" group */
	Button topButton, bottomButton;

	static String [] TabItems1 = {ControlExample.getResourceString("TabItem1_0"),
								  ControlExample.getResourceString("TabItem1_1"),
								  ControlExample.getResourceString("TabItem1_2")};

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TabFolderTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the TabFolder */
		tabFolderGroup = new Group (exampleGroup, SWT.NONE);
		tabFolderGroup.setLayout (new GridLayout ());
		tabFolderGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		tabFolderGroup.setText ("TabFolder");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (topButton.getSelection ()) style |= SWT.TOP;
		if (bottomButton.getSelection ()) style |= SWT.BOTTOM;
		if (borderButton.getSelection ()) style |= SWT.BORDER;

		/* Create the example widgets */
		tabFolder1 = new TabFolder (tabFolderGroup, style);
		for (int i = 0; i < TabItems1.length; i++) {
			TabItem item = new TabItem(tabFolder1, SWT.NONE);
			item.setText(TabItems1[i]);
			item.setToolTipText(ControlExample.getResourceString("Tooltip", new String [] {TabItems1[i]}));
			Text content = new Text(tabFolder1, SWT.WRAP | SWT.MULTI);
			content.setText(ControlExample.getResourceString("TabItem_content") + ": " + i);
			item.setControl(content);
		}
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		topButton = new Button (styleGroup, SWT.RADIO);
		topButton.setText ("SWT.TOP");
		topButton.setSelection(true);
		bottomButton = new Button (styleGroup, SWT.RADIO);
		bottomButton.setText ("SWT.BOTTOM");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	
		/* Add the listeners */
		SelectionListener selectionListener = new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				if (!((Button) event.widget).getSelection ()) return;
				recreateExampleWidgets ();
			}
		};
		topButton.addSelectionListener (selectionListener);
		bottomButton.addSelectionListener (selectionListener);
	}
	
	/**
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		return tabFolder1.getItems();
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {tabFolder1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Selection", "SelectionIndex"};
	}

	String setMethodName(String methodRoot) {
		/* Override to handle special case of int getSelectionIndex()/setSelection(int) */
		return (methodRoot.equals("SelectionIndex")) ? "setSelection" : "set" + methodRoot;
	}

	Object[] parameterForType(String typeName, String value, Control control) {
		if (value.equals("")) return new Object[] {new TabItem[0]};
		if (typeName.equals("org.eclipse.swt.widgets.TabItem")) {
			TabItem item = findItem(value, ((TabFolder) control).getItems());
			if (item != null) return new Object[] {item};
		}
		if (typeName.equals("[Lorg.eclipse.swt.widgets.TabItem;")) {
			String[] values = value.split(",");
			TabItem[] items = new TabItem[values.length];
			for (int i = 0; i < values.length; i++) {
				items[i] = findItem(values[i], ((TabFolder) control).getItems());
			}
			return new Object[] {items};
		}
		return super.parameterForType(typeName, value, control);
	}

	TabItem findItem(String value, TabItem[] items) {
		for (int i = 0; i < items.length; i++) {
			TabItem item = items[i];
			if (item.getText().equals(value)) return item;
		}
		return null;
	}

	/**
	 * Gets the short text for the tab folder item.
	 */
	public String getShortTabText() {
		return "TF";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "TabFolder";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		topButton.setSelection ((tabFolder1.getStyle () & SWT.TOP) != 0);
		bottomButton.setSelection ((tabFolder1.getStyle () & SWT.BOTTOM) != 0);
		borderButton.setSelection ((tabFolder1.getStyle () & SWT.BORDER) != 0);
	}
}
