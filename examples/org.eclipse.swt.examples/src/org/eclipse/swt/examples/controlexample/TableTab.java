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

class TableTab extends ScrollableTab {

	/* Example widgets and groups that contain them */
	Table table1;
	Group tableGroup;

	/* Style widgets added to the "Style" group */
	Button fullSelectionButton;

	/* Display widgets added to the "Display" group */
	Button headerVisibleButton, linesVisibleButton;
	
	static String [] columnTitles	= {resControls.getString("TableTitle_0"),
									   resControls.getString("TableTitle_1"),
									   resControls.getString("TableTitle_2"),
									   resControls.getString("TableTitle_3")};
									   
	static String [] stringLine0		= {resControls.getString("TableLine0_0"),
										  resControls.getString("TableLine0_1"),
										  resControls.getString("TableLine0_2"),
										  resControls.getString("TableLine0_3")};
									   
	static String [] stringLine1		= {resControls.getString("TableLine1_0"),
										  resControls.getString("TableLine1_1"),
										  resControls.getString("TableLine1_2"),
										  resControls.getString("TableLine1_3")};
									   
	static String [] stringLine2		= {resControls.getString("TableLine2_0"),
										  resControls.getString("TableLine2_1"),
										  resControls.getString("TableLine2_2"),
										  resControls.getString("TableLine2_3")};

/**
* Creates the "Display" group.
*/
void createDisplayGroup () {
	super.createDisplayGroup ();

	/* Create display controls specific to this example */
	headerVisibleButton = new Button (displayGroup, SWT.CHECK);
	headerVisibleButton.setText (resControls.getString("Header_Visible"));
	linesVisibleButton = new Button (displayGroup, SWT.CHECK);
	linesVisibleButton.setText (resControls.getString("Lines_Visible"));

	/* Add the listeners */
	headerVisibleButton.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent event) {
			setWidgetHeaderVisible ();
		}
	});
	linesVisibleButton.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent event) {
			setWidgetLinesVisible ();
		};
	});
}
/**
* Creates the "Example" group.
*/
void createExampleGroup () {
	super.createExampleGroup ();
	
	/* Create a group for the table */
	tableGroup = new Group (exampleGroup, SWT.NULL);
	tableGroup.setLayout (new GridLayout ());
	tableGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.GRAB_VERTICAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	tableGroup.setText (resControls.getString("Table"));
}
/**
* Creates the "Example" widgets.
*/
void createExampleWidgets () {
	
	/* Compute the widget style */
	int style = SWT.NONE;
	if (singleButton.getSelection ()) style |= SWT.SINGLE;
	if (multiButton.getSelection ()) style |= SWT.MULTI;
	if (fullSelectionButton.getSelection ()) style |= SWT.FULL_SELECTION;
	if (borderButton.getSelection ()) style |= SWT.BORDER;

	/* Create the table widget */
	table1 = new Table (tableGroup, style);

	/* Fill the table with data */
	Image [] images = new Image [] {
		Images.CLOSED_FOLDER_IMAGE,
		Images.OPEN_FOLDER_IMAGE,
		Images.TARGET_IMAGE,
	};
	int[] columnWidths = {150, 60, 75, 150};
	for (int i = 0; i < columnTitles.length; i++) {
		TableColumn tableColumn = new TableColumn(table1, SWT.NULL);
		tableColumn.setWidth(columnWidths[i]);
		tableColumn.setText(columnTitles[i]);
	}	
	for (int i=0; i<16; i++) {
		TableItem item = new TableItem (table1, SWT.NULL);
		item.setImage (images [i % 3]);
		switch (i % 3) {
			case 0:
				stringLine0 [0] = resControls.getString("Index") + i;
				item.setText(stringLine0);
				break;
			case 1:
				stringLine1 [0] = resControls.getString("Index") + i;
				item.setText(stringLine1);
				break;
			case 2:
				stringLine2 [0] = resControls.getString("Index") + i;
				item.setText(stringLine2);
				break;
		}
	}
}
/**
* Creates the "Style" group.
*/
void createStyleGroup () {
	super.createStyleGroup ();
	
	/* Create the extra widgets */
	fullSelectionButton = new Button (styleGroup, SWT.CHECK);
	fullSelectionButton.setText (resControls.getString("SWT_FULL_SELECTION"));
}
/**
* Gets the "Example" widget children.
*/
Control [] getExampleWidgets () {
	return new Control [] {table1};
}
/**
* Gets the text for the tab folder item.
*/
String getTabText () {
	return resControls.getString("Table");
}
/**
* Sets the state of the "Example" widgets.
*/
void setExampleWidgetState () {
	super.setExampleWidgetState ();
	setWidgetHeaderVisible ();
	setWidgetLinesVisible ();
	fullSelectionButton.setSelection ((table1.getStyle () & SWT.FULL_SELECTION) != 0);
}

/**
* Sets the header visible state of the "Example" widgets.
*/
void setWidgetHeaderVisible () {
	table1.setHeaderVisible (headerVisibleButton.getSelection ());
}
/**
* Sets the lines visible state of the "Example" widgets.
*/
void setWidgetLinesVisible () {
	table1.setLinesVisible (linesVisibleButton.getSelection ());
}
}
