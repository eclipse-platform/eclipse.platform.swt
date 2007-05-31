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

class ComboTab extends Tab {

	/* Example widgets and groups that contain them */
	Combo combo1;
	Group comboGroup;
	
	/* Style widgets added to the "Style" group */
	Button dropDownButton, readOnlyButton, simpleButton;
	
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
	ComboTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the combo box */
		comboGroup = new Group (exampleGroup, SWT.NONE);
		comboGroup.setLayout (new GridLayout ());
		comboGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		comboGroup.setText ("Combo");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (dropDownButton.getSelection ()) style |= SWT.DROP_DOWN;
		if (readOnlyButton.getSelection ()) style |= SWT.READ_ONLY;
		if (simpleButton.getSelection ()) style |= SWT.SIMPLE;
		
		/* Create the example widgets */
		combo1 = new Combo (comboGroup, style);
		combo1.setItems (ListData);
		if (ListData.length >= 3) {
			combo1.setText(ListData [2]);
		}
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
		dropDownButton = new Button (styleGroup, SWT.RADIO);
		dropDownButton.setText ("SWT.DROP_DOWN");
		simpleButton = new Button (styleGroup, SWT.RADIO);
		simpleButton.setText("SWT.SIMPLE");
		readOnlyButton = new Button (styleGroup, SWT.CHECK);
		readOnlyButton.setText ("SWT.READ_ONLY");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {combo1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Items", "Orientation", "Selection", "Text", "TextLimit", "ToolTipText", "VisibleItemCount"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Combo";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		dropDownButton.setSelection ((combo1.getStyle () & SWT.DROP_DOWN) != 0);
		simpleButton.setSelection ((combo1.getStyle () & SWT.SIMPLE) != 0);
		readOnlyButton.setSelection ((combo1.getStyle () & SWT.READ_ONLY) != 0);
		readOnlyButton.setEnabled(!simpleButton.getSelection());
	}
}
