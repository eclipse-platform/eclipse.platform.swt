/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

class SpinnerTab extends RangeTab {

	/* Example widgets and groups that contain them */
	Spinner spinner1;
	Group spinnerGroup;
	
	/* Style widgets added to the "Style" group */
	Button readOnlyButton;
	
	/* Scale widgets added to the "Control" group */
	Scale incrementScale, pageIncrementScale;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	SpinnerTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets () {
		super.createControlWidgets ();
		createIncrementGroup ();
		createPageIncrementGroup ();
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
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		
		/* Create the example widgets */
		spinner1 = new Spinner (spinnerGroup, style);
	}
	
	/**
	 * Create a group of widgets to control the maximum
	 * attribute of the example widget.
	 */
	void createMaximumGroup() {
		super.createMaximumGroup();
		maximumScale.setMaximum (1000);
		maximumScale.setPageIncrement (100);
	}
	
	/**
	 * Create a group of widgets to control the selection
	 * attribute of the example widget.
	 */
	void createSelectionGroup() {
		super.createSelectionGroup();
		selectionScale.setMaximum (1000);
		selectionScale.setPageIncrement (100);
	}
	
	/**
	 * Create a group of widgets to control the increment
	 * attribute of the example widget.
	 */
	void createIncrementGroup() {
	
		/* Create the group */
		Group incrementGroup = new Group (controlGroup, SWT.NONE);
		incrementGroup.setLayout (new GridLayout ());
		incrementGroup.setText (ControlExample.getResourceString("Increment"));
		incrementGroup.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	
		/* Create the scale widget */
		incrementScale = new Scale (incrementGroup, SWT.NONE);
		incrementScale.setMaximum (100);
		incrementScale.setSelection (1);
		incrementScale.setPageIncrement (10);
		incrementScale.setIncrement (5);

		GridData data = new GridData (GridData.FILL_HORIZONTAL);
		data.widthHint = 100;
		incrementScale.setLayoutData (data);
	
		/* Add the listeners */
		incrementScale.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {		
				setWidgetIncrement ();
			};
		});
	}
	
	/**
	 * Create a group of widgets to control the page increment
	 * attribute of the example widget.
	 */
	void createPageIncrementGroup() {
	
		/* Create the group */
		Group pageIncrementGroup = new Group (controlGroup, SWT.NONE);
		pageIncrementGroup.setLayout (new GridLayout ());
		pageIncrementGroup.setText (ControlExample.getResourceString("Page_Increment"));
		pageIncrementGroup.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
			
		/* Create the scale widget */
		pageIncrementScale = new Scale (pageIncrementGroup, SWT.NONE);
		pageIncrementScale.setMaximum (100);
		pageIncrementScale.setSelection (10);
		pageIncrementScale.setPageIncrement (10);
		pageIncrementScale.setIncrement (5);
	
		GridData data = new GridData (GridData.FILL_HORIZONTAL);
		data.widthHint = 100;
		pageIncrementScale.setLayoutData (data);

		/* Add the listeners */
		pageIncrementScale.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetPageIncrement ();
			}
		});
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
		orientationButtons = false;
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
		setWidgetIncrement ();
		setWidgetPageIncrement ();
	}

	/**
	 * Sets the increment of the "Example" widgets.
	 */
	void setWidgetIncrement () {
		spinner1.setIncrement (incrementScale.getSelection ());
	}
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMaximum () {
		spinner1.setMaximum (maximumScale.getSelection ());
	}
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMinimum () {
		spinner1.setMinimum (minimumScale.getSelection ());
	}
	
	/**
	 * Sets the page increment of the "Example" widgets.
	 */
	void setWidgetPageIncrement () {
		spinner1.setPageIncrement (pageIncrementScale.getSelection ());
	}
	
	/**
	 * Sets the selection of the "Example" widgets.
	 */
	void setWidgetSelection () {
		spinner1.setSelection (selectionScale.getSelection ());
	}
}
