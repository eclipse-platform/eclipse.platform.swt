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

class ListTab extends ScrollableTab {

	/* Example widgets and groups that contain them */
	List list1;
	Group listGroup;
	
	static String [] ListData1 = {resControls.getString("ListData1_0"),
								  resControls.getString("ListData1_1"),
								  resControls.getString("ListData1_2"),
								  resControls.getString("ListData1_3"),
								  resControls.getString("ListData1_4"),
								  resControls.getString("ListData1_5"),
								  resControls.getString("ListData1_6"),
								  resControls.getString("ListData1_7"),
								  resControls.getString("ListData1_8")};

/**
* Creates the "Example" group.
*/
void createExampleGroup () {
	super.createExampleGroup ();
	
	/* Create a group for the list */
	listGroup = new Group (exampleGroup, SWT.NULL);
	listGroup.setLayout (new GridLayout ());
	listGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	listGroup.setText (resControls.getString("List"));
}
/**
* Creates the "Example" widgets.
*/
void createExampleWidgets () {
	
	/* Compute the widget style */
	int style = SWT.NONE;
	if (singleButton.getSelection ()) style |= SWT.SINGLE;
	if (multiButton.getSelection ()) style |= SWT.MULTI;
	if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
	if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
	if (borderButton.getSelection ()) style |= SWT.BORDER;

	/* Create the example widgets */
	list1 = new List (listGroup, style);
	list1.setItems (ListData1);
}
/**
* Gets the "Example" widget children.
*/
Control [] getExampleWidgets () {
	return new Control [] {list1};
}
/**
* Gets the text for the tab folder item.
*/
String getTabText () {
	return resControls.getString("List");
}
}
