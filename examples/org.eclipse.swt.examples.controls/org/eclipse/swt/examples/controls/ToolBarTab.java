package org.eclipse.swt.examples.controls;

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
	Menu dropDownMenu;
	Group imageToolBarGroup, textToolBarGroup;
	
	/* Style widgets added to the "Style" group */
	Button flatButton, wrapButton;

	static String [] MenuData0 = {ControlPlugin.getResourceString("ListData0_0"),
								  ControlPlugin.getResourceString("ListData0_1"),
								  ControlPlugin.getResourceString("ListData0_2"),
								  ControlPlugin.getResourceString("ListData0_3"),
								  ControlPlugin.getResourceString("ListData0_4"),
								  ControlPlugin.getResourceString("ListData0_5"),
								  ControlPlugin.getResourceString("ListData0_6"),
								  ControlPlugin.getResourceString("ListData0_7"),
								  ControlPlugin.getResourceString("ListData0_8")};

	/**
	 * Create the drop down menu widget used by the 
	 * drop down style tool bar item.
	 */
	void createDropDownMenu() {
		/* Don't create more than one menu */
		if (dropDownMenu != null) return;
	
		/* Create the menu */
		Shell shell = tabFolderPage.getShell ();
		dropDownMenu = new Menu(shell);
		for (int i = 0; i < MenuData0.length; ++i) {
			if (i != 5) {
				MenuItem menuItem = new MenuItem(dropDownMenu, SWT.NONE);
				menuItem.setText(MenuData0[i]);
				/*
				* Add a menu selection listener so that the menu is hidden
				* when the user selects an item from the drop down menu.
				*/
				menuItem.addSelectionListener(new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						disposeDropDownMenu ();
					}
				});
			} else {
				MenuItem menuItem = new MenuItem(dropDownMenu, SWT.SEPARATOR);
			}
		}
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
		imageToolBarGroup.setText (ControlPlugin.getResourceString("Image_ToolBar"));
	
		/* Create a group for the text tool bar */
		textToolBarGroup = new Group (exampleGroup, SWT.NULL);
		textToolBarGroup.setLayout (new GridLayout ());
		textToolBarGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		textToolBarGroup.setText (ControlPlugin.getResourceString("Text_ToolBar"));
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
		item.setImage (ControlPlugin.images[ControlPlugin.ciClosedFolder]);
		item.setToolTipText(ControlPlugin.getResourceString("SWT_PUSH"));
		item = new ToolItem (imageToolBar, SWT.PUSH);
		item.setImage (ControlPlugin.images[ControlPlugin.ciClosedFolder]);
		item.setToolTipText (ControlPlugin.getResourceString("SWT_PUSH"));
		item = new ToolItem (imageToolBar, SWT.RADIO);
		item.setImage (ControlPlugin.images[ControlPlugin.ciOpenFolder]);
		item.setToolTipText (ControlPlugin.getResourceString("SWT_RADIO"));
		item = new ToolItem (imageToolBar, SWT.RADIO);
		item.setImage (ControlPlugin.images[ControlPlugin.ciOpenFolder]);
		item.setToolTipText (ControlPlugin.getResourceString("SWT_RADIO"));
		item = new ToolItem (imageToolBar, SWT.CHECK);
		item.setImage (ControlPlugin.images[ControlPlugin.ciTarget]);
		item.setToolTipText (ControlPlugin.getResourceString("SWT_CHECK"));
		item = new ToolItem (imageToolBar, SWT.RADIO);
		item.setImage (ControlPlugin.images[ControlPlugin.ciClosedFolder]);
		item.setToolTipText (ControlPlugin.getResourceString("SWT_RADIO"));
		item = new ToolItem (imageToolBar, SWT.RADIO);
		item.setImage (ControlPlugin.images[ControlPlugin.ciClosedFolder]);
		item.setToolTipText (ControlPlugin.getResourceString("SWT_RADIO"));
		item = new ToolItem (imageToolBar, SWT.SEPARATOR);
		item.setToolTipText(ControlPlugin.getResourceString("SWT_SEPARATOR"));
		item = new ToolItem (imageToolBar, SWT.DROP_DOWN);
		item.setImage (ControlPlugin.images[ControlPlugin.ciTarget]);
		item.setToolTipText (ControlPlugin.getResourceString("SWT_DROP_DOWN"));
	
		/*
		* Add a selection listener to the drop down tool item
		* so that we can show the menu when the drop down area
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
		item.setText (ControlPlugin.getResourceString("Push"));
		item.setToolTipText(ControlPlugin.getResourceString("SWT_PUSH"));
		item = new ToolItem (textToolBar, SWT.PUSH);
		item.setText (ControlPlugin.getResourceString("Push"));
		item.setToolTipText(ControlPlugin.getResourceString("SWT_PUSH"));
		item = new ToolItem (textToolBar, SWT.RADIO);
		item.setText (ControlPlugin.getResourceString("Radio"));
		item.setToolTipText(ControlPlugin.getResourceString("SWT_RADIO"));
		item = new ToolItem (textToolBar, SWT.RADIO);
		item.setText (ControlPlugin.getResourceString("Radio"));
		item.setToolTipText(ControlPlugin.getResourceString("SWT_RADIO"));
		item = new ToolItem (textToolBar, SWT.CHECK);
		item.setText (ControlPlugin.getResourceString("Check"));
		item.setToolTipText(ControlPlugin.getResourceString("SWT_CHECK"));
		item = new ToolItem (textToolBar, SWT.RADIO);
		item.setText (ControlPlugin.getResourceString("Radio"));
		item.setToolTipText(ControlPlugin.getResourceString("SWT_RADIO"));
		item = new ToolItem (textToolBar, SWT.RADIO);
		item.setText (ControlPlugin.getResourceString("Radio"));
		item.setToolTipText(ControlPlugin.getResourceString("SWT_RADIO"));
		item = new ToolItem (textToolBar, SWT.SEPARATOR);
		item.setToolTipText(ControlPlugin.getResourceString("SWT_SEPARATOR"));
		item = new ToolItem (textToolBar, SWT.DROP_DOWN);
		item.setText (ControlPlugin.getResourceString("Drop_Down"));
		item.setToolTipText(ControlPlugin.getResourceString("SWT_DROP_DOWN"));
	
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
		flatButton.setText (ControlPlugin.getResourceString("SWT_FLAT"));
		wrapButton = new Button (styleGroup, SWT.CHECK);
		wrapButton.setText (ControlPlugin.getResourceString("SWT_WRAP"));
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText (ControlPlugin.getResourceString("SWT_BORDER"));
	}
	
	void disposeDropDownMenu () {
		if (dropDownMenu == null) return;
		dropDownMenu.setVisible(false);
		dropDownMenu.dispose();
		dropDownMenu = null;
	}
	
	void disposeExampleWidgets () {
		disposeDropDownMenu ();
		super.disposeExampleWidgets ();
	}
	
	/**
	 * Handle the drop down tool item selection event.
	 *
	 * @param event the selection event
	 */
	void dropDownToolItemSelected (SelectionEvent event) {
	
		/*
		 * If menu was already dropped down then close it.
		 * We would do this regardless of where the tool
		 * item was selected.
		 */
		 if (dropDownMenu != null) {
			disposeDropDownMenu ();
			return;
		}
		createDropDownMenu ();
		
		/**
		 * A selection event will be fired when a drop down tool
		 * item is selected in the main area and in the drop
		 * down arrow.  Examine the event detail to determine
		 * where the widget was selected.
		 */		
		if (event.detail == SWT.ARROW) {
			/*
			 * The drop down arrow was selected.
			 * Position the menu below and vertically 
			 * alligned with the the drop down tool button.
			 */
			ToolItem item = (ToolItem) event.widget;
			Rectangle toolItemBounds = item.getBounds ();
			Point point1 = imageToolBar.toDisplay(new Point (toolItemBounds.x, toolItemBounds.y));
			dropDownMenu.setLocation(point1.x, point1.y + toolItemBounds.height);
			dropDownMenu.setVisible(true);
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
		return ControlPlugin.getResourceString("ToolBar");
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
