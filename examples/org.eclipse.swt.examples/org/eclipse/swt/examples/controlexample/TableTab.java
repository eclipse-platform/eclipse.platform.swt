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
	
	static String [] columnTitles	= {ControlExample.getResourceString("TableTitle_0"),
									   ControlExample.getResourceString("TableTitle_1"),
									   ControlExample.getResourceString("TableTitle_2"),
									   ControlExample.getResourceString("TableTitle_3")};
									   
	static String [] stringLine0		= {ControlExample.getResourceString("TableLine0_0"),
										  ControlExample.getResourceString("TableLine0_1"),
										  ControlExample.getResourceString("TableLine0_2"),
										  ControlExample.getResourceString("TableLine0_3")};
									   
	static String [] stringLine1		= {ControlExample.getResourceString("TableLine1_0"),
										  ControlExample.getResourceString("TableLine1_1"),
										  ControlExample.getResourceString("TableLine1_2"),
										  ControlExample.getResourceString("TableLine1_3")};
									   
	static String [] stringLine2		= {ControlExample.getResourceString("TableLine2_0"),
										  ControlExample.getResourceString("TableLine2_1"),
										  ControlExample.getResourceString("TableLine2_2"),
										  ControlExample.getResourceString("TableLine2_3")};

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TableTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Display" group.
	 */
	void createDisplayGroup () {
		super.createDisplayGroup ();
	
		/* Create display controls specific to this example */
		headerVisibleButton = new Button (displayGroup, SWT.CHECK);
		headerVisibleButton.setText (ControlExample.getResourceString("Header_Visible"));
		linesVisibleButton = new Button (displayGroup, SWT.CHECK);
		linesVisibleButton.setText (ControlExample.getResourceString("Lines_Visible"));
	
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
		tableGroup.setText (ControlExample.getResourceString("Table"));
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
		int[] columnWidths = {150, 60, 75, 150};
		for (int i = 0; i < columnTitles.length; i++) {
			TableColumn tableColumn = new TableColumn(table1, SWT.NULL);
			tableColumn.setWidth(columnWidths[i]);
			tableColumn.setText(columnTitles[i]);
		}	
		for (int i=0; i<16; i++) {
			TableItem item = new TableItem (table1, SWT.NULL);
			item.setImage (instance.images [i % 3]);
			switch (i % 3) {
				case 0:
					stringLine0 [0] = ControlExample.getResourceString("Index") + i;
					item.setText(stringLine0);
					break;
				case 1:
					stringLine1 [0] = ControlExample.getResourceString("Index") + i;
					item.setText(stringLine1);
					break;
				case 2:
					stringLine2 [0] = ControlExample.getResourceString("Index") + i;
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
		fullSelectionButton.setText (ControlExample.getResourceString("SWT_FULL_SELECTION"));
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
		return ControlExample.getResourceString("Table");
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
