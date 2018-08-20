/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
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
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.Widget;

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
	@Override
	void createControlWidgets () {
		super.createControlWidgets ();
		createIncrementGroup ();
		createPageIncrementGroup ();
		createDigitsGroup ();
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
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
	@Override
	void createExampleWidgets () {

		/* Compute the widget style */
		int style = getDefaultStyle();
		if (readOnlyButton.getSelection ()) style |= SWT.READ_ONLY;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (wrapButton.getSelection ()) style |= SWT.WRAP;

		/* Create the example widgets */
		spinner1 = new Spinner (spinnerGroup, style);
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
		incrementSpinner.setMaximum (100000);
		incrementSpinner.setSelection (getDefaultIncrement());
		incrementSpinner.setPageIncrement (100);
		incrementSpinner.setIncrement (1);
		incrementSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		/* Add the listeners */
		incrementSpinner.addSelectionListener (widgetSelectedAdapter(e -> setWidgetIncrement ()));
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
		pageIncrementSpinner.setMaximum (100000);
		pageIncrementSpinner.setSelection (getDefaultPageIncrement());
		pageIncrementSpinner.setPageIncrement (100);
		pageIncrementSpinner.setIncrement (1);
		pageIncrementSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		/* Add the listeners */
		pageIncrementSpinner.addSelectionListener (widgetSelectedAdapter(event -> setWidgetPageIncrement ()));
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
		digitsSpinner.setMaximum (100000);
		digitsSpinner.setSelection (getDefaultDigits());
		digitsSpinner.setPageIncrement (100);
		digitsSpinner.setIncrement (1);
		digitsSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		/* Add the listeners */
		digitsSpinner.addSelectionListener (widgetSelectedAdapter(e -> setWidgetDigits ()));
	}

	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	@Override
	Composite createTabFolderPage (TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(ControlListener.controlResizedAdapter(e ->	setExampleWidgetSize ()));

		return tabFolderPage;
	}

	/**
	 * Creates the "Style" group.
	 */
	@Override
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
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [] {spinner1};
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	@Override
	String[] getMethodNames() {
		return new String[] {"Selection", "TextLimit", "ToolTipText"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "Spinner";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	@Override
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
	 * Gets the default maximum of the "Example" widgets.
	 */
	@Override
	int getDefaultMaximum () {
		return spinner1.getMaximum();
	}

	/**
	 * Gets the default minimim of the "Example" widgets.
	 */
	@Override
	int getDefaultMinimum () {
		return spinner1.getMinimum();
	}

	/**
	 * Gets the default selection of the "Example" widgets.
	 */
	@Override
	int getDefaultSelection () {
		return spinner1.getSelection();
	}

	/**
	 * Gets the default increment of the "Example" widgets.
	 */
	int getDefaultIncrement () {
		return spinner1.getIncrement();
	}

	/**
	 * Gets the default page increment of the "Example" widgets.
	 */
	int getDefaultPageIncrement () {
		return spinner1.getPageIncrement();
	}

	/**
	 * Gets the default digits of the "Example" widgets.
	 */
	int getDefaultDigits () {
		return spinner1.getDigits();
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
	@Override
	void setWidgetMaximum () {
		spinner1.setMaximum (maximumSpinner.getSelection ());
	}

	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	@Override
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
	@Override
	void setWidgetSelection () {
		spinner1.setSelection (selectionSpinner.getSelection ());
	}
}
