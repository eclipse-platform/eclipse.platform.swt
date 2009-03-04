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

class DateTimeTab extends Tab {
	/* Example widgets and groups that contain them */
	DateTime dateTime1;
	Group dateTimeGroup;
	
	/* Style widgets added to the "Style" group */
	Button dateButton, timeButton, calendarButton, shortButton, mediumButton, longButton, dropDownButton;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	DateTimeTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the list */
		dateTimeGroup = new Group (exampleGroup, SWT.NONE);
		dateTimeGroup.setLayout (new GridLayout ());
		dateTimeGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		dateTimeGroup.setText ("DateTime");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (dateButton.getSelection ()) style |= SWT.DATE;
		if (timeButton.getSelection ()) style |= SWT.TIME;
		if (calendarButton.getSelection ()) style |= SWT.CALENDAR;
		if (shortButton.getSelection ()) style |= SWT.SHORT;
		if (mediumButton.getSelection ()) style |= SWT.MEDIUM;
		if (longButton.getSelection ()) style |= SWT.LONG;
		if (dropDownButton.getSelection ()) style |= SWT.DROP_DOWN;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
	
		/* Create the example widgets */		
		dateTime1 = new DateTime (dateTimeGroup, style);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		dateButton = new Button(styleGroup, SWT.RADIO);
		dateButton.setText("SWT.DATE");
		timeButton = new Button(styleGroup, SWT.RADIO);
		timeButton.setText("SWT.TIME");
		calendarButton = new Button(styleGroup, SWT.RADIO);
		calendarButton.setText("SWT.CALENDAR");
		Group formatGroup = new Group(styleGroup, SWT.NONE);
		formatGroup.setLayout(new GridLayout());
		shortButton = new Button(formatGroup, SWT.RADIO);
		shortButton.setText("SWT.SHORT");
		mediumButton = new Button(formatGroup, SWT.RADIO);
		mediumButton.setText("SWT.MEDIUM");
		longButton = new Button(formatGroup, SWT.RADIO);
		longButton.setText("SWT.LONG");
		dropDownButton = new Button(styleGroup, SWT.CHECK);
		dropDownButton.setText("SWT.DROP_DOWN");
		borderButton = new Button(styleGroup, SWT.CHECK);
		borderButton.setText("SWT.BORDER");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {dateTime1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Day", "Hours", "Minutes", "Month", "Seconds", "Year"};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "DateTime";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		dateButton.setSelection ((dateTime1.getStyle () & SWT.DATE) != 0);
		timeButton.setSelection ((dateTime1.getStyle () & SWT.TIME) != 0);
		calendarButton.setSelection ((dateTime1.getStyle () & SWT.CALENDAR) != 0);
		shortButton.setSelection ((dateTime1.getStyle () & SWT.SHORT) != 0);
		mediumButton.setSelection ((dateTime1.getStyle () & SWT.MEDIUM) != 0);
		longButton.setSelection ((dateTime1.getStyle () & SWT.LONG) != 0);
		dropDownButton.setSelection ((dateTime1.getStyle () & SWT.DROP_DOWN) != 0);
		borderButton.setSelection ((dateTime1.getStyle () & SWT.BORDER) != 0);
	}
}
