package org.eclipse.swt.examples.controlexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class SliderTab extends RangeTab {

	/* Example widgets and groups that contain them */
	Scale scale1;
	Slider slider1;
	Group sliderGroup, scaleGroup;

	/* Scale widgets added to the "Control" group */
	Scale incrementScale, pageIncrementScale, thumbScale;
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
	sliderGroup = new Group (exampleGroup, SWT.NULL);
	sliderGroup.setLayout (new GridLayout ());
	sliderGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	sliderGroup.setText (resControls.getString("Slider"));

	/* Create a group for the scale */
	scaleGroup = new Group (exampleGroup, SWT.NULL);
	scaleGroup.setLayout (new GridLayout ());
	scaleGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	scaleGroup.setText (resControls.getString("Scale"));

}
/**
* Creates the "Example" widgets.
*/
void createExampleWidgets () {
	
	/* Compute the widget style */
	int style = SWT.NONE;
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
	Group incrementGroup = new Group (controlGroup, SWT.NULL);
	incrementGroup.setLayout (new GridLayout ());
	incrementGroup.setText (resControls.getString("Increment"));

	/* Create the scale widget */
	incrementScale = new Scale (incrementGroup, SWT.NULL);
	incrementScale.setMaximum (100);
	incrementScale.setSelection (5);
	incrementScale.setPageIncrement (10);
	incrementScale.setIncrement (5);

	/* Add the listeners */
	incrementScale.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent e) {		
			setWidgetIncrement ();
		};
	});
}
/**
* Create a group of widgets to control the page increment
* attribute of the example widget.
*/
void createPageIncrementGroup() {

	/* Create the group */
	Group pageIncrementGroup = new Group (controlGroup, SWT.NULL);
	pageIncrementGroup.setLayout (new GridLayout ());
	pageIncrementGroup.setText (resControls.getString("Page_Increment"));

	/* Create the scale widget */
	pageIncrementScale = new Scale (pageIncrementGroup, SWT.NULL);
	pageIncrementScale.setMaximum (100);
	pageIncrementScale.setSelection (10);
	pageIncrementScale.setPageIncrement (10);
	pageIncrementScale.setIncrement (5);

	/* Add the listeners */
	pageIncrementScale.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent event) {
			setWidgetIncrement ();
		}
	});
}
/**
* Create a group of widgets to control the thumb
* attribute of the example widget.
*/
void createThumbGroup() {

	/* Create the group */
	Group thumbGroup = new Group (controlGroup, SWT.NULL);
	thumbGroup.setLayout (new GridLayout ());
	thumbGroup.setText (resControls.getString("Thumb"));

	/* Create the scale widget */
	thumbScale = new Scale (thumbGroup, SWT.NULL);
	thumbScale.setMaximum (100);
	thumbScale.setSelection (10);
	thumbScale.setPageIncrement (10);
	thumbScale.setIncrement (5);

	/* Add the listeners */
	thumbScale.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent event) {
			setWidgetThumb ();
		};
	});
}
/**
* Gets the "Example" widget children.
*/
Control [] getExampleWidgets () {
	return new Control [] {scale1, slider1};
}
/**
* Gets the text for the tab folder item.
*/
String getTabText () {
	return resControls.getString("Slider_and_Scale");
}
/**
* Sets the state of the "Example" widgets.
*/
void setExampleWidgetState () {
	super.setExampleWidgetState ();
	setWidgetIncrement ();
	setWidgetPageIncrement ();
	setWidgetThumb ();
}
/**
* Sets the increment of the "Example" widgets.
*/
void setWidgetIncrement () {
	slider1.setIncrement (incrementScale.getSelection ());
	scale1.setIncrement (incrementScale.getSelection ());
}
/**
* Sets the minimim of the "Example" widgets.
*/
void setWidgetMaximum () {
	slider1.setMaximum (maximumScale.getSelection ());
	scale1.setMaximum (maximumScale.getSelection ());
}
/**
* Sets the minimim of the "Example" widgets.
*/
void setWidgetMinimum () {
	slider1.setMinimum (minimumScale.getSelection ());
	scale1.setMinimum (minimumScale.getSelection ());
}
/**
* Sets the page increment of the "Example" widgets.
*/
void setWidgetPageIncrement () {
	slider1.setPageIncrement (pageIncrementScale.getSelection ());
	scale1.setPageIncrement (pageIncrementScale.getSelection ());
}
/**
* Sets the selection of the "Example" widgets.
*/
void setWidgetSelection () {
	slider1.setSelection (selectionScale.getSelection ());
	scale1.setSelection (selectionScale.getSelection ());
}
/**
* Sets the thumb of the "Example" widgets.
*/
void setWidgetThumb () {
	slider1.setThumb (thumbScale.getSelection ());
}
}
