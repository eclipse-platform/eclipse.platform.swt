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

class ToolBarTab extends Tab {

	/* Example widgets and groups that contain them */
	ToolBar imageToolBar, textToolBar;
	Shell dropDownShell;
	List dropDownList;
	Group imageToolBarGroup, textToolBarGroup;
	
	/* Style widgets added to the "Style" group */
	Button flatButton, wrapButton;

	static String [] ListData0 = {resControls.getString("ListData0_0"),
								  resControls.getString("ListData0_1"),
								  resControls.getString("ListData0_2"),
								  resControls.getString("ListData0_3"),
								  resControls.getString("ListData0_4"),
								  resControls.getString("ListData0_5"),
								  resControls.getString("ListData0_6"),
								  resControls.getString("ListData0_7"),
								  resControls.getString("ListData0_8")};

/**
* Create the drop down list widget used by the 
* drop down style tool bar item.
*/
void createDropDownList() {

	/* Don't create more than one list */
	if (dropDownList != null) return;

	/* Create the list */
	Shell shell = tabFolderPage.getShell ();
	dropDownShell = new Shell (shell, SWT.NO_TRIM);
	dropDownList = new List(dropDownShell, SWT.VERTICAL);
	dropDownShell.setLayout (new FillLayout ());
	dropDownList.setItems (ListData0);

	/*
	* Add a list selection listener so that the list is hidden
	* when the user selects an item from the drop down list.
	*/
	dropDownList.addSelectionListener(new SelectionAdapter() {
		public void widgetSelected(SelectionEvent e) {
			disposeDropDownList ();
		}
	});
}
/**
* Creates the "Example" group.
*/
void createExampleGroup () {
	super.createExampleGroup ();
	
	/* Create a group for the image tool bar */
	imageToolBarGroup = new Group (exampleGroup, SWT.NULL);
	imageToolBarGroup.setLayout (new GridLayout ());
	imageToolBarGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	imageToolBarGroup.setText (resControls.getString("Image_ToolBar"));

	/* Create a group for the text tool bar */
	textToolBarGroup = new Group (exampleGroup, SWT.NULL);
	textToolBarGroup.setLayout (new GridLayout ());
	textToolBarGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
	textToolBarGroup.setText (resControls.getString("Text_ToolBar"));
}
/**
* Creates the "Example" widgets.
*/
void createExampleWidgets () {

	/* Compute the widget style */
	int style = SWT.NONE;
	if (flatButton.getSelection()) style |= SWT.FLAT;
	if (wrapButton.getSelection()) style |= SWT.WRAP;
	if (borderButton.getSelection()) style |= SWT.BORDER;

	/*
	* Create the example widgets.
	*
	* A tool bar must consist of all image tool
	* items or all text tool items but not both.
	*/

	/* Create the image tool bar */
	imageToolBar = new ToolBar (imageToolBarGroup, style);
	ToolItem item = new ToolItem (imageToolBar, SWT.PUSH);
	item.setImage (Images.CLOSED_FOLDER_IMAGE);
	item.setToolTipText(resControls.getString("SWT_PUSH"));
	item = new ToolItem (imageToolBar, SWT.PUSH);
	item.setImage (Images.CLOSED_FOLDER_IMAGE);
	item.setToolTipText (resControls.getString("SWT_PUSH"));
	item = new ToolItem (imageToolBar, SWT.RADIO);
	item.setImage (Images.OPEN_FOLDER_IMAGE);
	item.setToolTipText (resControls.getString("SWT_RADIO"));
	item = new ToolItem (imageToolBar, SWT.RADIO);
	item.setImage (Images.OPEN_FOLDER_IMAGE);
	item.setToolTipText (resControls.getString("SWT_RADIO"));
	item = new ToolItem (imageToolBar, SWT.CHECK);
	item.setImage (Images.TARGET_IMAGE);
	item.setToolTipText (resControls.getString("SWT_CHECK"));
	item = new ToolItem (imageToolBar, SWT.RADIO);
	item.setImage (Images.CLOSED_FOLDER_IMAGE);
	item.setToolTipText (resControls.getString("SWT_RADIO"));
	item = new ToolItem (imageToolBar, SWT.RADIO);
	item.setImage (Images.CLOSED_FOLDER_IMAGE);
	item.setToolTipText (resControls.getString("SWT_RADIO"));
	item = new ToolItem (imageToolBar, SWT.SEPARATOR);
	item.setToolTipText(resControls.getString("SWT_SEPARATOR"));
	item = new ToolItem (imageToolBar, SWT.DROP_DOWN);
	item.setImage (Images.TARGET_IMAGE);
	item.setToolTipText (resControls.getString("SWT_DROP_DOWN"));

	/*
	* Add a selection listener to the drop down tool item
	* so that we can show the list when the drop down area
	* is pressed.
	*/
	item.addSelectionListener (new SelectionAdapter () {
		public void widgetSelected (SelectionEvent event) {
			dropDownToolItemSelected (event);
		}
	});

	/* Create the text tool bar */
	textToolBar = new ToolBar (textToolBarGroup, style);
	item = new ToolItem (textToolBar, SWT.PUSH);
	item.setText (resControls.getString("Push"));
	item.setToolTipText(resControls.getString("SWT_PUSH"));
	item = new ToolItem (textToolBar, SWT.PUSH);
	item.setText (resControls.getString("Push"));
	item.setToolTipText(resControls.getString("SWT_PUSH"));
	item = new ToolItem (textToolBar, SWT.RADIO);
	item.setText (resControls.getString("Radio"));
	item.setToolTipText(resControls.getString("SWT_RADIO"));
	item = new ToolItem (textToolBar, SWT.RADIO);
	item.setText (resControls.getString("Radio"));
	item.setToolTipText(resControls.getString("SWT_RADIO"));
	item = new ToolItem (textToolBar, SWT.CHECK);
	item.setText (resControls.getString("Check"));
	item.setToolTipText(resControls.getString("SWT_CHECK"));
	item = new ToolItem (textToolBar, SWT.RADIO);
	item.setText (resControls.getString("Radio"));
	item.setToolTipText(resControls.getString("SWT_RADIO"));
	item = new ToolItem (textToolBar, SWT.RADIO);
	item.setText (resControls.getString("Radio"));
	item.setToolTipText(resControls.getString("SWT_RADIO"));
	item = new ToolItem (textToolBar, SWT.SEPARATOR);
	item.setToolTipText(resControls.getString("SWT_SEPARATOR"));
	item = new ToolItem (textToolBar, SWT.DROP_DOWN);
	item.setText (resControls.getString("Drop_Down"));
	item.setToolTipText(resControls.getString("SWT_DROP_DOWN"));

	/*
	* Do not add the selection event for this drop down
	* tool item.  Without hooking the event, the drop down
	* widget does nothing special when the drop down area
	* is selected.
	*/
}
/**
* Creates the "Style" group.
*/
void createStyleGroup() {
	super.createStyleGroup();

	/* Create the extra widgets */
	flatButton = new Button (styleGroup, SWT.CHECK);
	flatButton.setText (resControls.getString("SWT_FLAT"));
	wrapButton = new Button (styleGroup, SWT.CHECK);
	wrapButton.setText (resControls.getString("SWT_WRAP"));
	borderButton = new Button (styleGroup, SWT.CHECK);
	borderButton.setText (resControls.getString("SWT_BORDER"));
}
void disposeDropDownList () {
	if (dropDownShell != null) dropDownShell.dispose ();
	dropDownShell = null; dropDownList = null;
}
void disposeExampleWidgets () {
	super.disposeExampleWidgets ();
	disposeDropDownList ();
}
/**
* Handle the drop down tool item selection event.
*
* @param event the selection event
*/
void dropDownToolItemSelected (SelectionEvent event) {

	/*
	* If list was already dropped down then close it.
	* We would do this regardless of where the tool
	* item was selected.
	*/
	createDropDownList ();
	if (dropDownShell.getVisible ()) {
		disposeDropDownList ();
		return;
	}
	
	/**
	* A selection event will be fired when a drop down tool
	* item is selected in the main area and in the drop
	* down arrow.  Examine the event detail to determine
	* where the widget was selected.
	*/		
	if (event.detail == SWT.ARROW) {
		/*
		* The drop down arrow was selected.
		* Position the list below and vertically 
		* alligned with the the drop down tool button.
		*/
		ToolItem item = (ToolItem) event.widget;
		Rectangle toolItemBounds = item.getBounds ();
		Point point1 = imageToolBar.toDisplay (new Point (toolItemBounds.x, toolItemBounds.y));
		dropDownShell.setBounds (point1.x, point1.y + toolItemBounds.height, 100, 100);
		dropDownShell.setVisible (true);
	} else {
		/*
		* Main area of drop down tool item selected.
		* An application would invoke the code was
		* required to perform the action for the tool
		* item.
		*/
	}
}
/**
* Gets the "Example" widget children.
*/
Control [] getExampleWidgets () {
	return new Control [] {imageToolBar, textToolBar};
}
/**
* Gets the text for the tab folder item.
*/
String getTabText () {
	return resControls.getString("ToolBar");
}
/**
* Sets the state of the "Example" widgets.
*/
void setExampleWidgetState () {
	super.setExampleWidgetState ();
	flatButton.setSelection ((imageToolBar.getStyle () & SWT.FLAT) != 0);
	wrapButton.setSelection ((imageToolBar.getStyle () & SWT.WRAP) != 0);
	borderButton.setSelection ((imageToolBar.getStyle () & SWT.BORDER) != 0);
}

}
