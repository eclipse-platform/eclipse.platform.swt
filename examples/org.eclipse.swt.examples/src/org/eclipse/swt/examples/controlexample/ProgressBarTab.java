/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
		progressBarGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
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
		progressBar1.setMaximum (100);
		progressBar1.setSelection (50);
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
	Control [] getExampleWidgets () {
		return new Control [] {progressBar1};
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"ToolTipText"};
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
			selectionScale.setEnabled (false);
			minimumScale.setEnabled (false);
			maximumScale.setEnabled (false);
		} else {
			selectionScale.setEnabled (true);
			minimumScale.setEnabled (true);
			maximumScale.setEnabled (true);
		}
		maximumScale.setMaximum (progressBar1.getMaximum ());
		smoothButton.setSelection ((progressBar1.getStyle () & SWT.SMOOTH) != 0);
		indeterminateButton.setSelection ((progressBar1.getStyle () & SWT.INDETERMINATE) != 0);
	}

	/**
	 * Sets the maximum of the "Example" widgets.
	 */
	void setWidgetMaximum () {
		progressBar1.setMaximum (maximumScale.getSelection ());
		updateScales ();
	}

	/**
	 * Sets the minimim of the "Example" widgets.
	 */
	void setWidgetMinimum () {
		progressBar1.setMinimum (minimumScale.getSelection ());
		updateScales ();
	}

	/**
	 * Sets the selection of the "Example" widgets.
	 */
	void setWidgetSelection () {
		progressBar1.setSelection (selectionScale.getSelection ());
		updateScales ();
	}

	/**
	 * Update the scale widgets to reflect the actual value set 
	 * on the "Example" widget.
	 */
	void updateScales () {
		minimumScale.setSelection (progressBar1.getMinimum ());
		selectionScale.setSelection (progressBar1.getSelection ());
		maximumScale.setSelection (progressBar1.getMaximum ());
	}
}
