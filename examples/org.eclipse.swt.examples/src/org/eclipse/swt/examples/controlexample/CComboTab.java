package org.eclipse.swt.examples.controlexample;

/*
 * (c) Copyright IBM Corp. 2000, 2002.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;

class CComboTab extends Tab {

	/* Example widgets and groups that contain them */
	CCombo combo1;
	Group comboGroup;
	
	/* Style widgets added to the "Style" group */
	Button flatButton, readOnlyButton;
	
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
	CComboTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the combo box */
		comboGroup = new Group (exampleGroup, SWT.NULL);
		comboGroup.setLayout (new GridLayout ());
		comboGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		comboGroup.setText (ControlExample.getResourceString("Custom_Combo"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = SWT.NONE;
		if (flatButton.getSelection ()) style |= SWT.FLAT;
		if (readOnlyButton.getSelection ()) style |= SWT.READ_ONLY;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		
		/* Create the example widgets */
		combo1 = new CCombo (comboGroup, style);
		combo1.setItems (ListData);
		if (ListData.length >= 3) {
			combo1.setText(ListData [2]);
		}
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();
	
		/* Create the extra widgets */
		readOnlyButton = new Button (styleGroup, SWT.CHECK);
		readOnlyButton.setText ("SWT.READ_ONLY");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
		flatButton = new Button (styleGroup, SWT.CHECK);
		flatButton.setText ("SWT.FLAT");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {combo1};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "CCombo";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		flatButton.setSelection ((combo1.getStyle () & SWT.FLAT) != 0);
		readOnlyButton.setSelection ((combo1.getStyle () & SWT.READ_ONLY) != 0);
		borderButton.setSelection ((combo1.getStyle () & SWT.BORDER) != 0);
	}
}
