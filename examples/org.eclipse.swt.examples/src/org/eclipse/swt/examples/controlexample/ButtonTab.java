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
import org.eclipse.swt.events.*;

/**
 * <code>ButtonTab</code> is the class that
 * demonstrates SWT buttons.
 */
class ButtonTab extends AlignableTab {

	/* Example widgets and groups that contain them */
	Button button1, button2, button3, button4, button5, button6, button7, button8, button9;
	Group textButtonGroup, imageButtonGroup, imagetextButtonGroup;

	/* Alignment widgets added to the "Control" group */
	Button upButton, downButton;

	/* Style widgets added to the "Style" group */
	Button pushButton, checkButton, radioButton, toggleButton, arrowButton, flatButton, wrapButton;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ButtonTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Control" group. 
	 */
	void createControlGroup () {
		super.createControlGroup ();
	
		/* Create the controls */
		upButton = new Button (alignmentGroup, SWT.RADIO);
		upButton.setText (ControlExample.getResourceString("Up"));
		downButton = new Button (alignmentGroup, SWT.RADIO);
		downButton.setText (ControlExample.getResourceString("Down"));
	
		/* Add the listeners */
		SelectionListener selectionListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (!((Button) event.widget).getSelection()) return;
				setExampleWidgetAlignment ();
			}
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
		textButtonGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		textButtonGroup.setText (ControlExample.getResourceString("Text_Buttons"));
	
		/* Create a group for the image buttons */
		imageButtonGroup = new Group(exampleGroup, SWT.NONE);
		gridLayout = new GridLayout();
		imageButtonGroup.setLayout(gridLayout);
		gridLayout.numColumns = 3;
		imageButtonGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		imageButtonGroup.setText (ControlExample.getResourceString("Image_Buttons"));

		/* Create a group for the image and text buttons */
		imagetextButtonGroup = new Group(exampleGroup, SWT.NONE);
		gridLayout = new GridLayout();
		imagetextButtonGroup.setLayout(gridLayout);
		gridLayout.numColumns = 3;
		imagetextButtonGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		imagetextButtonGroup.setText (ControlExample.getResourceString("Image_Text_Buttons"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
	
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (pushButton.getSelection()) style |= SWT.PUSH;
		if (checkButton.getSelection()) style |= SWT.CHECK;
		if (radioButton.getSelection()) style |= SWT.RADIO;
		if (toggleButton.getSelection()) style |= SWT.TOGGLE;
		if (flatButton.getSelection()) style |= SWT.FLAT;
		if (wrapButton.getSelection()) style |= SWT.WRAP;
		if (borderButton.getSelection()) style |= SWT.BORDER;
		if (leftButton.getSelection()) style |= SWT.LEFT;
		if (rightButton.getSelection()) style |= SWT.RIGHT;
		if (arrowButton.getSelection()) {
			style |= SWT.ARROW; 
			if (upButton.getSelection()) style |= SWT.UP;
			if (downButton.getSelection()) style |= SWT.DOWN;
		} else {
			if (centerButton.getSelection()) style |= SWT.CENTER;			
		}
	
		/* Create the example widgets */
		button1 = new Button(textButtonGroup, style);
		button1.setText(ControlExample.getResourceString("One"));
		button2 = new Button(textButtonGroup, style);
		button2.setText(ControlExample.getResourceString("Two"));
		button3 = new Button(textButtonGroup, style);
		if (wrapButton.getSelection ()) {
			button3.setText (ControlExample.getResourceString("Wrap_Text"));
		} else {
			button3.setText (ControlExample.getResourceString("Three"));
		}
		button4 = new Button(imageButtonGroup, style);
		button4.setImage(instance.images[ControlExample.ciClosedFolder]);
		button5 = new Button(imageButtonGroup, style);
		button5.setImage(instance.images[ControlExample.ciOpenFolder]);
		button6 = new Button(imageButtonGroup, style);
		button6.setImage(instance.images[ControlExample.ciTarget]);
		button7 = new Button(imagetextButtonGroup, style);
		button7.setText(ControlExample.getResourceString("One"));
		button7.setImage(instance.images[ControlExample.ciClosedFolder]);
		button8 = new Button(imagetextButtonGroup, style);
		button8.setText(ControlExample.getResourceString("Two"));
		button8.setImage(instance.images[ControlExample.ciOpenFolder]);
		button9 = new Button(imagetextButtonGroup, style);
		if (wrapButton.getSelection ()) {
			button9.setText (ControlExample.getResourceString("Wrap_Text"));
		} else {
			button9.setText (ControlExample.getResourceString("Three"));
		}
		button9.setImage(instance.images[ControlExample.ciTarget]);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
	
		/* Create the extra widgets */
		pushButton = new Button (styleGroup, SWT.RADIO);
		pushButton.setText("SWT.PUSH");
		checkButton = new Button (styleGroup, SWT.RADIO);
		checkButton.setText ("SWT.CHECK");
		radioButton = new Button (styleGroup, SWT.RADIO);
		radioButton.setText ("SWT.RADIO");
		toggleButton = new Button (styleGroup, SWT.RADIO);
		toggleButton.setText ("SWT.TOGGLE");
		arrowButton = new Button (styleGroup, SWT.RADIO);
		arrowButton.setText ("SWT.ARROW");
		flatButton = new Button (styleGroup, SWT.CHECK);
		flatButton.setText ("SWT.FLAT");
		wrapButton = new Button (styleGroup, SWT.CHECK);
		wrapButton.setText ("SWT.WRAP");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {button1, button2, button3, button4, button5, button6, button7, button8, button9};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Grayed", "Selection", "Text", "ToolTipText"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Button";
	}
	
	/**
	 * Sets the alignment of the "Example" widgets.
	 */
	void setExampleWidgetAlignment () {
		int alignment = 0;
		if (leftButton.getSelection ()) alignment = SWT.LEFT;
		if (centerButton.getSelection ()) alignment = SWT.CENTER;
		if (rightButton.getSelection ()) alignment = SWT.RIGHT;
		if (upButton.getSelection ()) alignment = SWT.UP;
		if (downButton.getSelection ()) alignment = SWT.DOWN;
		button1.setAlignment (alignment);
		button2.setAlignment (alignment);
		button3.setAlignment (alignment);
		button4.setAlignment (alignment);
		button5.setAlignment (alignment);
		button6.setAlignment (alignment);
		button7.setAlignment (alignment);
		button8.setAlignment (alignment);
		button9.setAlignment (alignment);
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
		flatButton.setSelection ((button1.getStyle () & SWT.FLAT) != 0);
		wrapButton.setSelection ((button1.getStyle () & SWT.WRAP) != 0);
		borderButton.setSelection ((button1.getStyle () & SWT.BORDER) != 0);
	}
}
