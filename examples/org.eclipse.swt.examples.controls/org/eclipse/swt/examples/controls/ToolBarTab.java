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
	Group imageToolBarGroup, textToolBarGroup;
	
	/* Style widgets added to the "Style" group */
	Button flatButton, wrapButton;

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
		item.addSelectionListener(new DropDownSelectionListener());
	
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
		item.addSelectionListener(new DropDownSelectionListener());
	
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
	
	void disposeExampleWidgets () {
		super.disposeExampleWidgets ();
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
	
	/**
	 * Listens to widgetSelected() events on SWT.DROP_DOWN type ToolItems
	 * and opens/closes a menu when appropriate.
	 */
	class DropDownSelectionListener extends SelectionAdapter {
		private Menu    menu = null;
		private boolean visible = false;
		
		public void widgetSelected(SelectionEvent event) {
			// Create the menu if it has not already been created
			if (menu == null) {
				// Lazy create the menu.
				Shell shell = tabFolderPage.getShell();
				menu = new Menu(shell);
				for (int i = 0; i < 9; ++i) {
					final String text = ControlPlugin.getResourceString("DropDownData_" + i);
					if (text.length() != 0) {
						MenuItem menuItem = new MenuItem(menu, SWT.NONE);
						menuItem.setText(text);
						/*
						 * Add a menu selection listener so that the menu is hidden
						 * when the user selects an item from the drop down menu.
						 */
						menuItem.addSelectionListener(new SelectionAdapter() {
							public void widgetSelected(SelectionEvent e) {
								setMenuVisible(false);
							}
						});
					} else {
						MenuItem menuItem = new MenuItem(menu, SWT.SEPARATOR);
					}
				}
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
				 */
				if (visible) {
					// Hide the menu to give the Arrow the appearance of being a toggle button.
					setMenuVisible(false);
				} else {	
					// Position the menu below and vertically aligned with the the drop down tool button.
					final ToolItem toolItem = (ToolItem) event.widget;
					final ToolBar  toolBar = toolItem.getParent();
					
					Rectangle toolItemBounds = toolItem.getBounds();
					Point point = toolBar.toDisplay(new Point(toolItemBounds.x, toolItemBounds.y));
					menu.setLocation(point.x, point.y + toolItemBounds.height);
					setMenuVisible(true);
				}
			} else {
				/*
				 * Main area of drop down tool item selected.
				 * An application would invoke the code to perform the action for the tool item.
				 */
			}
		}
		private void setMenuVisible(boolean visible) {
			menu.setVisible(visible);
			this.visible = visible;
		}
	}
}
