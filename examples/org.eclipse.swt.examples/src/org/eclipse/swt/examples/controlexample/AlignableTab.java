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


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

/**
 * <code>AlignableTab</code> is the abstract
 * superclass of example controls that can be
 * aligned.
 */
abstract class AlignableTab extends Tab {

	/* Alignment Controls */
	Button leftButton, rightButton, centerButton;

	/* Alignment Group */
	Group alignmentGroup;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	AlignableTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Other" group.
	 */
	@Override
	void createOtherGroup () {
		super.createOtherGroup ();

		/* Create the group */
		alignmentGroup = new Group (otherGroup, SWT.NONE);
		alignmentGroup.setLayout (new GridLayout ());
		alignmentGroup.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_FILL |
			GridData.VERTICAL_ALIGN_FILL));
		alignmentGroup.setText (ControlExample.getResourceString("Alignment"));

		/* Create the controls */
		leftButton = new Button (alignmentGroup, SWT.RADIO);
		leftButton.setText (ControlExample.getResourceString("Left"));
		centerButton = new Button (alignmentGroup, SWT.RADIO);
		centerButton.setText(ControlExample.getResourceString("Center"));
		rightButton = new Button (alignmentGroup, SWT.RADIO);
		rightButton.setText (ControlExample.getResourceString("Right"));

		/* Add the listeners */
		SelectionListener selectionListener = widgetSelectedAdapter(event -> {
			if (!((Button) event.widget).getSelection ()) return;
			setExampleWidgetAlignment ();
		});
		leftButton.addSelectionListener (selectionListener);
		centerButton.addSelectionListener (selectionListener);
		rightButton.addSelectionListener (selectionListener);
	}

	/**
	 * Sets the alignment of the "Example" widgets.
	 */
	abstract void setExampleWidgetAlignment ();

	/**
	 * Sets the state of the "Example" widgets.
	 */
	@Override
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		Widget [] widgets = getExampleWidgets ();
		if (widgets.length != 0) {
			leftButton.setSelection ((widgets [0].getStyle () & SWT.LEFT) != 0);
			centerButton.setSelection ((widgets [0].getStyle () & SWT.CENTER) != 0);
			rightButton.setSelection ((widgets [0].getStyle () & SWT.RIGHT) != 0);
		}
	}
}
