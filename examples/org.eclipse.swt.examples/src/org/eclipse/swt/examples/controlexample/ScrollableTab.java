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

abstract class ScrollableTab extends Tab {
	/* Style widgets added to the "Style" group */	
	Button singleButton, multiButton, horizontalButton, verticalButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ScrollableTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();
	
		/* Create the extra widgets */
		singleButton = new Button (styleGroup, SWT.RADIO);
		singleButton.setText ("SWT.SINGLE");
		multiButton = new Button (styleGroup, SWT.RADIO);
		multiButton.setText ("SWT.MULTI");
		horizontalButton = new Button (styleGroup, SWT.CHECK);
		horizontalButton.setText ("SWT.H_SCROLL");
		horizontalButton.setSelection(true);
		verticalButton = new Button (styleGroup, SWT.CHECK);
		verticalButton.setText ("SWT.V_SCROLL");
		verticalButton.setSelection(true);
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		Control [] controls = getExampleWidgets ();
		if (controls.length != 0){
			singleButton.setSelection ((controls [0].getStyle () & SWT.SINGLE) != 0);
			multiButton.setSelection ((controls [0].getStyle () & SWT.MULTI) != 0);
			horizontalButton.setSelection ((controls [0].getStyle () & SWT.H_SCROLL) != 0);
			verticalButton.setSelection ((controls [0].getStyle () & SWT.V_SCROLL) != 0);
			borderButton.setSelection ((controls [0].getStyle () & SWT.BORDER) != 0);
		}
	}
}
