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
	Button readOnlyButton, wrapButton;
	
	/* Spinner widgets added to the "Control" group */
	Spinner incrementSpinner, pageIncrementSpinner, digitsSpinner;

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
		createDigitsGroup ();
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the spinner */
		spinnerGroup = new Group (exampleGroup, SWT.NONE);
		spinnerGroup.setLayout (new GridLayout ());
		spinnerGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
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
		if (wrapButton.getSelection ()) style |= SWT.WRAP;
		
		/* Create the example widgets */
		spinner1 = new Spinner (spinnerGroup, style);
		spinner1.setMaximum(100);
		spinner1.setSelection(50);
	}
	
	/**
	 * Create a group of widgets to control the maximum
	 * attribute of the example widget.
	 */
	void createMaximumGroup() {
		super.createMaximumGroup();
		maximumSpinner.setMaximum (1000);
		maximumSpinner.setPageIncrement (100);
	}
	
	/**
	 * Create a group of widgets to control the selection
	 * attribute of the example widget.
	 */
	void createSelectionGroup() {
		super.createSelectionGroup();
		selectionSpinner.setMaximum (1000);
		selectionSpinner.setPageIncrement (100);
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
	
		/* Create the Spinner widget */
		incrementSpinner = new Spinner (incrementGroup, SWT.BORDER);
		incrementSpinner.setMaximum (100);
		incrementSpinner.setSelection (1);
		incrementSpinner.setPageIncrement (10);
		incrementSpinner.setIncrement (5);
		incrementSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
	
		/* Add the listeners */
		incrementSpinner.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {		
				setWidgetIncrement ();
			}
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
			
		/* Create the Spinner widget */
		pageIncrementSpinner = new Spinner (pageIncrementGroup, SWT.BORDER);
		pageIncrementSpinner.setMaximum (100);
		pageIncrementSpinner.setSelection (10);
		pageIncrementSpinner.setPageIncrement (10);
		pageIncrementSpinner.setIncrement (5);
		pageIncrementSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		/* Add the listeners */
		pageIncrementSpinner.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetPageIncrement ();
			}
		});
	}
	
	/**
	 * Create a group of widgets to control the digits
	 * attribute of the example widget.
	 */
	void createDigitsGroup() {
	
		/* Create the group */
		Group digitsGroup = new Group (controlGroup, SWT.NONE);
		digitsGroup.setLayout (new GridLayout ());
		digitsGroup.setText (ControlExample.getResourceString("Digits"));
		digitsGroup.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	
		/* Create the Spinner widget */
		digitsSpinner = new Spinner (digitsGroup, SWT.BORDER);
		digitsSpinner.setMaximum (8);
		digitsSpinner.setSelection (0);
		digitsSpinner.setPageIncrement (10);
		digitsSpinner.setIncrement (1);
		digitsSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
	
		/* Add the listeners */
		digitsSpinner.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {		
				setWidgetDigits ();
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
		wrapButton = new Button (styleGroup, SWT.CHECK);
		wrapButton.setText ("SWT.WRAP");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {spinner1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Selection", "ToolTipText"};
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
		wrapButton.setSelection ((spinner1.getStyle () & SWT.WRAP) != 0);
		if (!instance.startup) {
			setWidgetIncrement ();
			setWidgetPageIncrement ();
			setWidgetDigits ();
		}
	}

	/**
	 * Sets the increment of the "Example" widgets.
	 */
	void setWidgetIncrement () {
		spinner1.setIncrement (incrementSpinner.getSelection ());
	}
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMaximum () {
		spinner1.setMaximum (maximumSpinner.getSelection ());
	}
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMinimum () {
		spinner1.setMinimum (minimumSpinner.getSelection ());
	}
	
	/**
	 * Sets the page increment of the "Example" widgets.
	 */
	void setWidgetPageIncrement () {
		spinner1.setPageIncrement (pageIncrementSpinner.getSelection ());
	}
	
	/**
	 * Sets the digits of the "Example" widgets.
	 */
	void setWidgetDigits () {
		spinner1.setDigits (digitsSpinner.getSelection ());
	}
	
	/**
	 * Sets the selection of the "Example" widgets.
	 */
	void setWidgetSelection () {
		spinner1.setSelection (selectionSpinner.getSelection ());
	}
}