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

/**
* <code>ButtonTab</code> is the class that
* demonstrates SWT buttons.
*/

class ButtonTab extends AlignableTab {

	/* Example widgets and groups that contain them */
	Button button1, button2, button3, button4, button5, button6;
	Group textButtonGroup, imageButtonGroup;

	/* Allignment widgets added to the "Control" group */
	Button upButton, downButton;

	/* Style widgets added to the "Style" group */
	Button pushButton, checkButton, radioButton, toggleButton, arrowButton;
/**
* Creates the "Control" group. 
*/
void createControlGroup () {
	super.createControlGroup ();

	/* Create the controls */
	upButton = new Button (allignmentGroup, SWT.RADIO);
	upButton.setText (resControls.getString("Up"));
	downButton = new Button (allignmentGroup, SWT.RADIO);
	downButton.setText (resControls.getString("Down"));

	/* Add the listeners */
	SelectionListener selectionListener = new SelectionAdapter() {
		public void widgetSelected(SelectionEvent event) {
			if (!((Button) event.widget).getSelection()) return;
			setExampleWidgetAlignment ();
		};
	};
	upButton.addSelectionListener(selectionListener);
	downButton.addSelectionListener(selectionListener);
}
/**
* Creates the "Example" group.
*/
void createExampleGroup () {
	super.createExampleGroup ();
	
	/* Create a group for text buttons */
	textButtonGroup = new Group(exampleGroup, SWT.NONE);
	GridLayout gridLayout = new GridLayout ();
	textButtonGroup.setLayout(gridLayout);
	gridLayout.numColumns = 3;
	textButtonGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	textButtonGroup.setText (resControls.getString("Text_Buttons"));

	/* Create a group for the image buttons */
	imageButtonGroup = new Group(exampleGroup, SWT.NONE);
	gridLayout = new GridLayout();
	imageButtonGroup.setLayout(gridLayout);
	gridLayout.numColumns = 3;
	imageButtonGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	imageButtonGroup.setText (resControls.getString("Image_Buttons"));

}
/**
* Creates the "Example" widgets.
*/
void createExampleWidgets () {

	/* Compute the widget style */
	int style = SWT.NONE;
	if (pushButton.getSelection()) style |= SWT.PUSH;
	if (checkButton.getSelection()) style |= SWT.CHECK;
	if (radioButton.getSelection()) style |= SWT.RADIO;
	if (toggleButton.getSelection()) style |= SWT.TOGGLE;
	if (arrowButton.getSelection()) style |= SWT.ARROW;
	if (borderButton.getSelection()) style |= SWT.BORDER;

	/* Create the example widgets */
	button1 = new Button(textButtonGroup, style);
	button1.setText(resControls.getString("One"));
	button2 = new Button(textButtonGroup, style);
	button2.setText(resControls.getString("Two"));
	button3 = new Button(textButtonGroup, style);
	button3.setText(resControls.getString("Three"));
	button4 = new Button(imageButtonGroup, style);
	button4.setImage(Images.CLOSED_FOLDER_IMAGE);
	button5 = new Button(imageButtonGroup, style);
	button5.setImage(Images.OPEN_FOLDER_IMAGE);
	button6 = new Button(imageButtonGroup, style);
	button6.setImage(Images.TARGET_IMAGE);
}
/**
* Creates the "Style" group.
*/
void createStyleGroup() {
	super.createStyleGroup ();

	/* Create the extra widgets */
	pushButton = new Button (styleGroup, SWT.RADIO);
	pushButton.setText(resControls.getString("SWT_PUSH"));
	checkButton = new Button (styleGroup, SWT.RADIO);
	checkButton.setText (resControls.getString("SWT_CHECK"));
	radioButton = new Button (styleGroup, SWT.RADIO);
	radioButton.setText (resControls.getString("SWT_RADIO"));
	toggleButton = new Button (styleGroup, SWT.RADIO);
	toggleButton.setText (resControls.getString("SWT_TOGGLE"));
	arrowButton = new Button (styleGroup, SWT.RADIO);
	arrowButton.setText (resControls.getString("SWT_ARROW"));
	borderButton = new Button (styleGroup, SWT.CHECK);
	borderButton.setText (resControls.getString("SWT_BORDER"));
}
/**
* Gets the "Example" widget children.
*/
Control [] getExampleWidgets () {
	return new Control [] {button1, button2, button3, button4, button5, button6};
}
/**
* Gets the text for the tab folder item.
*/
String getTabText () {
	return resControls.getString("Button");
}
/**
* Sets the alignment of the "Example" widgets.
*/
void setExampleWidgetAlignment () {
	int allignment = 0;
	if (leftButton.getSelection ()) allignment = SWT.LEFT;
	if (centerButton.getSelection ()) allignment = SWT.CENTER;
	if (rightButton.getSelection ()) allignment = SWT.RIGHT;
	if (upButton.getSelection ()) allignment = SWT.UP;
	if (downButton.getSelection ()) allignment = SWT.DOWN;
	button1.setAlignment (allignment);
	button2.setAlignment (allignment);
	button3.setAlignment (allignment);
	button4.setAlignment (allignment);
	button5.setAlignment (allignment);
	button6.setAlignment (allignment);
}
/**
* Sets the state of the "Example" widgets.
*/
void setExampleWidgetState () {
	super.setExampleWidgetState ();
	if (arrowButton.getSelection ()) {
		upButton.setEnabled (true);
		centerButton.setEnabled (false);
		downButton.setEnabled (true);
	} else {
		upButton.setEnabled (false);
		centerButton.setEnabled (true);
		downButton.setEnabled (false);
	}
	upButton.setSelection ((button1.getStyle () & SWT.UP) != 0);
	downButton.setSelection ((button1.getStyle () & SWT.DOWN) != 0);
	pushButton.setSelection ((button1.getStyle () & SWT.PUSH) != 0);
	checkButton.setSelection ((button1.getStyle () & SWT.CHECK) != 0);
	radioButton.setSelection ((button1.getStyle () & SWT.RADIO) != 0);
	toggleButton.setSelection ((button1.getStyle () & SWT.TOGGLE) != 0);
	arrowButton.setSelection ((button1.getStyle () & SWT.ARROW) != 0);
	borderButton.setSelection ((button1.getStyle () & SWT.BORDER) != 0);
}
}
