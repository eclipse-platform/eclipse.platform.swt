package org.eclipse.swt.examples.controlexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;

abstract class ScrollableTab extends Tab {

	/* Style widgets added to the "Style" group */	
	Button singleButton, multiButton, horizontalButton, verticalButton, borderButton;
/**
* Creates the "Style" group.
*/
void createStyleGroup () {
	super.createStyleGroup ();

	/* Create the extra widgets */
	singleButton = new Button (styleGroup, SWT.RADIO);
	singleButton.setText (resControls.getString("SWT_SINGLE"));
	multiButton = new Button (styleGroup, SWT.RADIO);
	multiButton.setText (resControls.getString("SWT_MULTI"));
	horizontalButton = new Button (styleGroup, SWT.CHECK);
	horizontalButton.setText (resControls.getString("SWT_H_SCROLL"));
	verticalButton = new Button (styleGroup, SWT.CHECK);
	verticalButton.setText (resControls.getString("SWT_V_SCROLL"));
	borderButton = new Button (styleGroup, SWT.CHECK);
	borderButton.setText (resControls.getString("SWT_BORDER"));
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
