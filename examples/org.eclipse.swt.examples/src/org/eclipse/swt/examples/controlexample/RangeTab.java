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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

abstract class RangeTab extends Tab {
	/* Style widgets added to the "Style" group */
	Button horizontalButton, verticalButton;
	boolean orientationButtons = true;

	/* Scale widgets added to the "Control" group */
	Spinner minimumSpinner, selectionSpinner, maximumSpinner;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	RangeTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets () {
		/* Create controls specific to this example */
		createMinimumGroup ();
		createMaximumGroup ();
		createSelectionGroup ();
	}
	
	/**
	 * Create a group of widgets to control the maximum
	 * attribute of the example widget.
	 */
	void createMaximumGroup() {
	
		/* Create the group */
		Group maximumGroup = new Group (controlGroup, SWT.NONE);
		maximumGroup.setLayout (new GridLayout ());
		maximumGroup.setText (ControlExample.getResourceString("Maximum"));
		maximumGroup.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	
		/* Create a Spinner widget */
		maximumSpinner = new Spinner (maximumGroup, SWT.BORDER);
		maximumSpinner.setMaximum (100000);
		maximumSpinner.setSelection (getDefaultMaximum());
		maximumSpinner.setPageIncrement (100);
		maximumSpinner.setIncrement (1);
		maximumSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
	
		/* Add the listeners */
		maximumSpinner.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetMaximum ();
			}
		});
	}
	
	/**
	 * Create a group of widgets to control the minimum
	 * attribute of the example widget.
	 */
	void createMinimumGroup() {
	
		/* Create the group */
		Group minimumGroup = new Group (controlGroup, SWT.NONE);
		minimumGroup.setLayout (new GridLayout ());
		minimumGroup.setText (ControlExample.getResourceString("Minimum"));
		minimumGroup.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
	
		/* Create a Spinner widget */
		minimumSpinner = new Spinner (minimumGroup, SWT.BORDER);
		minimumSpinner.setMaximum (100000);
		minimumSpinner.setSelection(getDefaultMinimum());
		minimumSpinner.setPageIncrement (100);
		minimumSpinner.setIncrement (1);
		minimumSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		/* Add the listeners */
		minimumSpinner.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetMinimum ();
			}
		});
	
	}
	
	/**
	 * Create a group of widgets to control the selection
	 * attribute of the example widget.
	 */
	void createSelectionGroup() {
	
		/* Create the group */
		Group selectionGroup = new Group(controlGroup, SWT.NONE);
		selectionGroup.setLayout(new GridLayout());
		GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, false, false);
		selectionGroup.setLayoutData(gridData);
		selectionGroup.setText(ControlExample.getResourceString("Selection"));
	
		/* Create a Spinner widget */
		selectionSpinner = new Spinner (selectionGroup, SWT.BORDER);
		selectionSpinner.setMaximum (100000);
		selectionSpinner.setSelection (getDefaultSelection());
		selectionSpinner.setPageIncrement (100);
		selectionSpinner.setIncrement (1);
		selectionSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		/* Add the listeners */
		selectionSpinner.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setWidgetSelection ();
			}
		});
		
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();
	
		/* Create the extra widgets */
		if (orientationButtons) {
			horizontalButton = new Button (styleGroup, SWT.RADIO);
			horizontalButton.setText ("SWT.HORIZONTAL");
			verticalButton = new Button (styleGroup, SWT.RADIO);
			verticalButton.setText ("SWT.VERTICAL");
		}
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		if (!instance.startup) {
			setWidgetMinimum ();
			setWidgetMaximum ();
			setWidgetSelection ();
		}
		Widget [] widgets = getExampleWidgets ();
		if (widgets.length != 0) {
			if (orientationButtons) {
				horizontalButton.setSelection ((widgets [0].getStyle () & SWT.HORIZONTAL) != 0);
				verticalButton.setSelection ((widgets [0].getStyle () & SWT.VERTICAL) != 0);
			}
			borderButton.setSelection ((widgets [0].getStyle () & SWT.BORDER) != 0);
		}
	}
	
	/**
	 * Gets the default maximum of the "Example" widgets.
	 */
	abstract int getDefaultMaximum ();
	
	/**
	 * Gets the default minimim of the "Example" widgets.
	 */
	abstract int getDefaultMinimum ();
	
	/**
	 * Gets the default selection of the "Example" widgets.
	 */
	abstract int getDefaultSelection ();

	/**
	 * Sets the maximum of the "Example" widgets.
	 */
	abstract void setWidgetMaximum ();
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	abstract void setWidgetMinimum ();
	
	/**
	 * Sets the selection of the "Example" widgets.
	 */
	abstract void setWidgetSelection ();
}
