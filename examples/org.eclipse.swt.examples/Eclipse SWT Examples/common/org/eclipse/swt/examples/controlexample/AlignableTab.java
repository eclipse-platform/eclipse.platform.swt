package org.eclipse.swt.examples.controlexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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

	/* Allignment Controls */
	Button leftButton, rightButton, centerButton;

	/* Alignment Group */
	Group allignmentGroup;
/**
* Creates the "Control" group. 
*/
void createControlGroup () {
	super.createControlGroup ();
	
	/* Create the group */
	allignmentGroup = new Group (controlGroup, SWT.NULL);
	allignmentGroup.setLayout (new GridLayout ());
	allignmentGroup.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	allignmentGroup.setText (resControls.getString("Alignment"));

	/* Create the controls */
	leftButton = new Button (allignmentGroup, SWT.RADIO);
	leftButton.setText (resControls.getString("Left"));
	centerButton = new Button (allignmentGroup, SWT.RADIO);
	centerButton.setText(resControls.getString("Center"));
	rightButton = new Button (allignmentGroup, SWT.RADIO);
	rightButton.setText (resControls.getString("Right"));

	/* Add the listeners */
	SelectionListener selectionListener = new SelectionAdapter () {
		public void widgetSelected(SelectionEvent event) {
			if (!((Button) event.widget).getSelection ()) return;
			setExampleWidgetAlignment ();
		};
	};
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
void setExampleWidgetState () {
	super.setExampleWidgetState ();
	Control [] controls = getExampleWidgets ();
	if (controls.length != 0) {
		leftButton.setSelection ((controls [0].getStyle () & SWT.LEFT) != 0);
		centerButton.setSelection ((controls [0].getStyle () & SWT.CENTER) != 0);
		rightButton.setSelection ((controls [0].getStyle () & SWT.RIGHT) != 0);
	}
}
}
