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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

class SashFormTab extends Tab {
	/* Example widgets and groups that contain them */
	Group sashFormGroup;
	SashForm form;
	List list1, list2;
	Text text;
	
	/* Style widgets added to the "Style" group */
	Button horizontalButton, verticalButton, smoothButton;

	static String [] ListData0 = {ControlExample.getResourceString("ListData0_0"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_1"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_2"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_3"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_4"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_5"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_6"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData0_7")}; //$NON-NLS-1$
								  
	static String [] ListData1 = {ControlExample.getResourceString("ListData1_0"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_1"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_2"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_3"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_4"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_5"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_6"), //$NON-NLS-1$
								  ControlExample.getResourceString("ListData1_7")}; //$NON-NLS-1$


	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	SashFormTab(ControlExample instance) {
		super(instance);
	}
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the sashform widget */
		sashFormGroup = new Group (exampleGroup, SWT.NONE);
		sashFormGroup.setLayout (new GridLayout ());
		sashFormGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		sashFormGroup.setText ("SashForm");
	}
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (smoothButton.getSelection ()) style |= SWT.SMOOTH;
		
		/* Create the example widgets */
		form = new SashForm (sashFormGroup, style);
		list1 = new List (form, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		list1.setItems (ListData0);
		list2 = new List (form, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		list2.setItems (ListData1);
		text = new Text (form, SWT.MULTI | SWT.BORDER);
		text.setText (ControlExample.getResourceString("Multi_line")); //$NON-NLS-1$
		form.setWeights(new int[] {1, 1, 1});
	}
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widgets */
		horizontalButton = new Button (styleGroup, SWT.RADIO);
		horizontalButton.setText ("SWT.HORIZONTAL");
		horizontalButton.setSelection(true);
		verticalButton = new Button (styleGroup, SWT.RADIO);
		verticalButton.setText ("SWT.VERTICAL");
		verticalButton.setSelection(false);
		smoothButton = new Button (styleGroup, SWT.CHECK);
		smoothButton.setText ("SWT.SMOOTH");
		smoothButton.setSelection(false);
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {form};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "SashForm"; //$NON-NLS-1$
	}
	
		/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		horizontalButton.setSelection ((form.getStyle () & SWT.H_SCROLL) != 0);
		verticalButton.setSelection ((form.getStyle () & SWT.V_SCROLL) != 0);
		smoothButton.setSelection ((form.getStyle () & SWT.SMOOTH) != 0);
	}
}
