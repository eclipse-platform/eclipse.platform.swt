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

class ScaleTab extends RangeTab {
	/* Example widgets and groups that contain them */
	Scale scale1;
	Group scaleGroup;

	/* Spinner widgets added to the "Control" group */
	Spinner incrementSpinner, pageIncrementSpinner;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ScaleTab(ControlExample instance) {
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
		
		/* Create a group for the scale */
		scaleGroup = new Group (exampleGroup, SWT.NONE);
		scaleGroup.setLayout (new GridLayout ());
		scaleGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		scaleGroup.setText ("Scale");
	
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (horizontalButton.getSelection ()) style |= SWT.HORIZONTAL;
		if (verticalButton.getSelection ()) style |= SWT.VERTICAL;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
	
		/* Create the example widgets */
		scale1 = new Scale (scaleGroup, style);
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
		pageIncrementSpinner.setMaximum (100000);
		pageIncrementSpinner.setSelection (getDefaultPageIncrement());
		pageIncrementSpinner.setPageIncrement (100);
		pageIncrementSpinner.setIncrement (1);
		pageIncrementSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		/* Add the listeners */
		pageIncrementSpinner.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetPageIncrement ();
			}
		});
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {scale1};
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
		return "Scale";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		if (!instance.startup) {
			setWidgetIncrement ();
			setWidgetPageIncrement ();
		}
	}
	
	/**
	 * Gets the default maximum of the "Example" widgets.
	 */
	int getDefaultMaximum () {
		return scale1.getMaximum();
	}
	
	/**
	 * Gets the default minimim of the "Example" widgets.
	 */
	int getDefaultMinimum () {
		return scale1.getMinimum();
	}
	
	/**
	 * Gets the default selection of the "Example" widgets.
	 */
	int getDefaultSelection () {
		return scale1.getSelection();
	}

	/**
	 * Gets the default increment of the "Example" widgets.
	 */
	int getDefaultIncrement () {
		return scale1.getIncrement();
	}
	
	/**
	 * Gets the default page increment of the "Example" widgets.
	 */
	int getDefaultPageIncrement () {
		return scale1.getPageIncrement();
	}
	
	/**
	 * Sets the increment of the "Example" widgets.
	 */
	void setWidgetIncrement () {
		scale1.setIncrement (incrementSpinner.getSelection ());
	}
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMaximum () {
		scale1.setMaximum (maximumSpinner.getSelection ());
	}
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMinimum () {
		scale1.setMinimum (minimumSpinner.getSelection ());
	}
	
	/**
	 * Sets the page increment of the "Example" widgets.
	 */
	void setWidgetPageIncrement () {
		scale1.setPageIncrement (pageIncrementSpinner.getSelection ());
	}
	
	/**
	 * Sets the selection of the "Example" widgets.
	 */
	void setWidgetSelection () {
		scale1.setSelection (selectionSpinner.getSelection ());
	}
}
