/*******************************************************************************
 * Copyright (c) 2000, 2013 IBM Corporation and others.
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
	@Override
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
	@Override
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
	@Override
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
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [] {progressBar1};
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	@Override
	String[] getMethodNames() {
		return new String[] {"Selection", "State", "ToolTipText"};
	}

	/**
	 * Gets the short text for the tab folder item.
	 */
	@Override
	String getShortTabText() {
		return "PB";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "ProgressBar";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	@Override
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
	@Override
	int getDefaultMaximum () {
		return progressBar1.getMaximum();
	}

	/**
	 * Gets the default minimum of the "Example" widgets.
	 */
	@Override
	int getDefaultMinimum () {
		return progressBar1.getMinimum();
	}

	/**
	 * Gets the default selection of the "Example" widgets.
	 */
	@Override
	int getDefaultSelection () {
		return progressBar1.getSelection();
	}

	/**
	 * Sets the maximum of the "Example" widgets.
	 */
	@Override
	void setWidgetMaximum () {
		progressBar1.setMaximum (maximumSpinner.getSelection ());
		updateSpinners ();
	}

	/**
	 * Sets the minimum of the "Example" widgets.
	 */
	@Override
	void setWidgetMinimum () {
		progressBar1.setMinimum (minimumSpinner.getSelection ());
		updateSpinners ();
	}

	/**
	 * Sets the selection of the "Example" widgets.
	 */
	@Override
	void setWidgetSelection () {
		progressBar1.setSelection (selectionSpinner.getSelection ());
		updateSpinners ();
	}

	/**
	 * Update the Spinner widgets to reflect the actual value set
	 * on the "Example" widget.
	 */
	void updateSpinners () {
		updateSpinner (minimumSpinner, progressBar1.getMinimum ());
		updateSpinner (selectionSpinner, progressBar1.getSelection ());
		updateSpinner (maximumSpinner, progressBar1.getMaximum ());
	}

	void updateSpinner(Spinner spinner, int selection) {
		if (spinner.getSelection() != selection) spinner.setSelection (selection);
	}
}
