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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class SliderTab extends RangeTab {
	/* Example widgets and groups that contain them */
	Scale scale1;
	Slider slider1;
	Group sliderGroup, scaleGroup;

	/* Spinner widgets added to the "Control" group */
	Spinner incrementSpinner, pageIncrementSpinner, thumbSpinner;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	SliderTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets () {
		super.createControlWidgets ();
		createThumbGroup ();
		createIncrementGroup ();
		createPageIncrementGroup ();
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the slider */
		sliderGroup = new Group (exampleGroup, SWT.NONE);
		sliderGroup.setLayout (new GridLayout ());
		sliderGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		sliderGroup.setText ("Slider");
	
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
		scale1.setMaximum (100);
		scale1.setSelection (50);
		scale1.setIncrement (5);
		scale1.setPageIncrement (10);
		slider1 = new Slider(sliderGroup, style);
		slider1.setMaximum (100);
		slider1.setSelection (50);
		slider1.setIncrement(5);
		slider1.setPageIncrement (10);
		slider1.setThumb (10);
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
		incrementSpinner.setSelection (5);
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
	 * Create a group of widgets to control the thumb
	 * attribute of the example widget.
	 */
	void createThumbGroup() {
	
		/* Create the group */
		Group thumbGroup = new Group (controlGroup, SWT.NONE);
		thumbGroup.setLayout (new GridLayout ());
		thumbGroup.setText (ControlExample.getResourceString("Thumb"));
		thumbGroup.setLayoutData (new GridData (GridData.FILL_HORIZONTAL));
		
		/* Create the Spinner widget */
		thumbSpinner = new Spinner (thumbGroup, SWT.BORDER);
		thumbSpinner.setMaximum (100);
		thumbSpinner.setSelection (10);
		thumbSpinner.setPageIncrement (10);
		thumbSpinner.setIncrement (5);
		thumbSpinner.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));

		/* Add the listeners */
		thumbSpinner.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetThumb ();
			}
		});
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {scale1, slider1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Selection", "ToolTipText"};
	}

	/**
	 * Gets the short text for the tab folder item.
	 */
	public String getShortTabText() {
		return "S/S";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Slider/Scale";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		if (!instance.startup) {
			setWidgetIncrement ();
			setWidgetPageIncrement ();
			setWidgetThumb ();
		}
	}
	
	/**
	 * Sets the increment of the "Example" widgets.
	 */
	void setWidgetIncrement () {
		slider1.setIncrement (incrementSpinner.getSelection ());
		scale1.setIncrement (incrementSpinner.getSelection ());
	}
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMaximum () {
		slider1.setMaximum (maximumSpinner.getSelection ());
		scale1.setMaximum (maximumSpinner.getSelection ());
	}
	
	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMinimum () {
		slider1.setMinimum (minimumSpinner.getSelection ());
		scale1.setMinimum (minimumSpinner.getSelection ());
	}
	
	/**
	 * Sets the page increment of the "Example" widgets.
	 */
	void setWidgetPageIncrement () {
		slider1.setPageIncrement (pageIncrementSpinner.getSelection ());
		scale1.setPageIncrement (pageIncrementSpinner.getSelection ());
	}
	
	/**
	 * Sets the selection of the "Example" widgets.
	 */
	void setWidgetSelection () {
		slider1.setSelection (selectionSpinner.getSelection ());
		scale1.setSelection (selectionSpinner.getSelection ());
	}
	
	/**
	 * Sets the thumb of the "Example" widgets.
	 */
	void setWidgetThumb () {
		slider1.setThumb (thumbSpinner.getSelection ());
	}
}
