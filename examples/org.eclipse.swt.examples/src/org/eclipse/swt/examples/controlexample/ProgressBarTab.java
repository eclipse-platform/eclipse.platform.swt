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

class ProgressBarTab extends RangeTab {
	/* Example widgets and groups that contain them */
	ProgressBar progressBar1;
	Group progressBarGroup;

	/* Style widgets added to the "Style" group */
	Button smoothButton;
	Button indeterminateButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ProgressBarTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup() {
		super.createExampleGroup ();

		/* Create a group for the progress bar */
		progressBarGroup = new Group (exampleGroup, SWT.NONE);
		progressBarGroup.setLayout (new GridLayout ());
		progressBarGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		progressBarGroup.setText ("ProgressBar");
	}

	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {

		/* Compute the widget style */
		int style = getDefaultStyle();
		if (horizontalButton.getSelection ()) style |= SWT.HORIZONTAL;
		if (verticalButton.getSelection ()) style |= SWT.VERTICAL;
		if (smoothButton.getSelection ()) style |= SWT.SMOOTH;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (indeterminateButton.getSelection ()) style |= SWT.INDETERMINATE;

		/* Create the example widgets */
		progressBar1 = new ProgressBar (progressBarGroup, style);
	}

	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();

		/* Create the extra widgets */
		smoothButton = new Button (styleGroup, SWT.CHECK);
		smoothButton.setText ("SWT.SMOOTH");
		indeterminateButton = new Button (styleGroup, SWT.CHECK);
		indeterminateButton.setText ("SWT.INDETERMINATE");
	}

	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {progressBar1};
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Selection", "State", "ToolTipText"};
	}

	/**
	 * Gets the short text for the tab folder item.
	 */
	String getShortTabText() {
		return "PB";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "ProgressBar";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		if (indeterminateButton.getSelection ()) {
			selectionSpinner.setEnabled (false);
			minimumSpinner.setEnabled (false);
			maximumSpinner.setEnabled (false);
		} else {
			selectionSpinner.setEnabled (true);
			minimumSpinner.setEnabled (true);
			maximumSpinner.setEnabled (true);
		}
		smoothButton.setSelection ((progressBar1.getStyle () & SWT.SMOOTH) != 0);
		indeterminateButton.setSelection ((progressBar1.getStyle () & SWT.INDETERMINATE) != 0);
	}

	/**
	 * Gets the default maximum of the "Example" widgets.
	 */
	int getDefaultMaximum () {
		return progressBar1.getMaximum();
	}
	
	/**
	 * Gets the default minimim of the "Example" widgets.
	 */
	int getDefaultMinimum () {
		return progressBar1.getMinimum();
	}
	
	/**
	 * Gets the default selection of the "Example" widgets.
	 */
	int getDefaultSelection () {
		return progressBar1.getSelection();
	}

	/**
	 * Sets the maximum of the "Example" widgets.
	 */
	void setWidgetMaximum () {
		progressBar1.setMaximum (maximumSpinner.getSelection ());
		updateSpinners ();
	}

	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMinimum () {
		progressBar1.setMinimum (minimumSpinner.getSelection ());
		updateSpinners ();
	}

	/**
	 * Sets the selection of the "Example" widgets.
	 */
	void setWidgetSelection () {
		progressBar1.setSelection (selectionSpinner.getSelection ());
		updateSpinners ();
	}

	/**
	 * Update the Spinner widgets to reflect the actual value set 
	 * on the "Example" widget.
	 */
	void updateSpinners () {
		minimumSpinner.setSelection (progressBar1.getMinimum ());
		selectionSpinner.setSelection (progressBar1.getSelection ());
		maximumSpinner.setSelection (progressBar1.getMaximum ());
	}
}
