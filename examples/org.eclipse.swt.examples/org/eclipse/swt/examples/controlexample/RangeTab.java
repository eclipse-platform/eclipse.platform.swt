package org.eclipse.swt.examples.controlexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

abstract class RangeTab extends Tab {

	/* Style widgets added to the "Style" group */
	Button horizontalButton, verticalButton;

	/* Scale widgets added to the "Control" group */
	Scale minimumScale, selectionScale, maximumScale;

/**
* Creates the "Control" widget children.
*/
void createControlWidgets () {

	/* Leave an empty cell */
	new Composite (controlGroup, SWT.NULL);
	
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
	Group maximumGroup = new Group (controlGroup, SWT.NULL);
	maximumGroup.setLayout (new GridLayout ());
	maximumGroup.setText (resControls.getString("Maximum"));

	/* Create a scale widget */
	maximumScale = new Scale (maximumGroup, SWT.NULL);
	maximumScale.setMaximum (100);
	maximumScale.setSelection (100);
	maximumScale.setPageIncrement (10);
	maximumScale.setIncrement (5);

	/* Add the listeners */
	maximumScale.addSelectionListener(new SelectionAdapter () {
		public void widgetSelected (SelectionEvent event) {
			setWidgetMaximum ();
		};
	});
}
/**
* Create a group of widgets to control the minimum
* attribute of the example widget.
*/
void createMinimumGroup() {

	/* Create the group */
	Group minimumGroup = new Group (controlGroup, SWT.NULL);
	minimumGroup.setLayout (new GridLayout ());
	minimumGroup.setText (resControls.getString("Minimum"));

	/* Create a scale widget */
	minimumScale = new Scale (minimumGroup, SWT.NULL);
	minimumScale.setMaximum (100);
	minimumScale.setPageIncrement (10);
	minimumScale.setIncrement (5);

	/* Add the listeners */
	minimumScale.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent event) {
			setWidgetMinimum ();
		};
	});

}
/**
* Create a group of widgets to control the selection
* attribute of the example widget.
*/
void createSelectionGroup() {

	/* Create the group */
	Group selectionGroup = new Group(controlGroup, SWT.NULL);
	selectionGroup.setLayout(new GridLayout());
	GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_CENTER);
	gridData.horizontalSpan = 2;
	selectionGroup.setLayoutData(gridData);
	selectionGroup.setText(resControls.getString("Selection"));

	/* Create a scale widget */
	selectionScale = new Scale (selectionGroup, SWT.NULL);
	selectionScale.setMaximum (100);
	selectionScale.setSelection (50);
	selectionScale.setPageIncrement (10);
	selectionScale.setIncrement (5);

	/* Add the listeners */
	selectionScale.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			setWidgetSelection ();
		};
	});
	
}
/**
* Creates the "Style" group.
*/
void createStyleGroup () {
	super.createStyleGroup ();

	/* Create the extra widgets */
	horizontalButton = new Button (styleGroup, SWT.RADIO);
	horizontalButton.setText (resControls.getString("SWT_HORIZONTAL"));
	verticalButton = new Button (styleGroup, SWT.RADIO);
	verticalButton.setText (resControls.getString("SWT_VERTICAL"));
	borderButton = new Button (styleGroup, SWT.CHECK);
	borderButton.setText (resControls.getString("SWT_BORDER"));
}
/**
* Sets the state of the "Example" widgets.
*/
void setExampleWidgetState () {
	super.setExampleWidgetState ();
	setWidgetMinimum ();
	setWidgetMaximum ();
	setWidgetSelection ();
	Control [] controls = getExampleWidgets ();
	if (controls.length != 0){
		horizontalButton.setSelection ((controls [0].getStyle () & SWT.HORIZONTAL) != 0);
		verticalButton.setSelection ((controls [0].getStyle () & SWT.VERTICAL) != 0);
		borderButton.setSelection ((controls [0].getStyle () & SWT.BORDER) != 0);
	}
}
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
