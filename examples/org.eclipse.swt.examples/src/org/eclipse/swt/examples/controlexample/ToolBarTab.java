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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class ToolBarTab extends Tab {
	/* Example widgets and groups that contain them */
	ToolBar imageToolBar, textToolBar, imageTextToolBar;
	Group imageToolBarGroup, textToolBarGroup, imageTextToolBarGroup;
	
	/* Style widgets added to the "Style" group */
	Button horizontalButton, verticalButton, flatButton, shadowOutButton, wrapButton, rightButton;

	/* Other widgets added to the "Other" group */
	Button comboChildButton;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ToolBarTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the image tool bar */
		imageToolBarGroup = new Group (exampleGroup, SWT.NONE);
		imageToolBarGroup.setLayout (new GridLayout ());
		imageToolBarGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		imageToolBarGroup.setText (ControlExample.getResourceString("Image_ToolBar"));
	
		/* Create a group for the text tool bar */
		textToolBarGroup = new Group (exampleGroup, SWT.NONE);
		textToolBarGroup.setLayout (new GridLayout ());
		textToolBarGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		textToolBarGroup.setText (ControlExample.getResourceString("Text_ToolBar"));
		
		/* Create a group for the image and text tool bar */
		imageTextToolBarGroup = new Group (exampleGroup, SWT.NONE);
		imageTextToolBarGroup.setLayout (new GridLayout ());
		imageTextToolBarGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		imageTextToolBarGroup.setText (ControlExample.getResourceString("ImageText_ToolBar"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
	
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (horizontalButton.getSelection()) style |= SWT.HORIZONTAL;
		if (verticalButton.getSelection()) style |= SWT.VERTICAL;
		if (flatButton.getSelection()) style |= SWT.FLAT;
		if (wrapButton.getSelection()) style |= SWT.WRAP;
		if (borderButton.getSelection()) style |= SWT.BORDER;
		if (shadowOutButton.getSelection()) style |= SWT.SHADOW_OUT;
		if (rightButton.getSelection()) style |= SWT.RIGHT;
	
		/*
		* Create the example widgets.
		*
		* A tool bar must consist of all image tool
		* items or all text tool items but not both.
		*/
	
		/* Create the image tool bar */
		imageToolBar = new ToolBar (imageToolBarGroup, style);
		ToolItem item = new ToolItem (imageToolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText("SWT.PUSH");
		item = new ToolItem (imageToolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.PUSH");
		item = new ToolItem (imageToolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText ("SWT.RADIO");
		item = new ToolItem (imageToolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText ("SWT.RADIO");
		item = new ToolItem (imageToolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setToolTipText ("SWT.CHECK");
		item = new ToolItem (imageToolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.RADIO");
		item = new ToolItem (imageToolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.RADIO");
		item = new ToolItem (imageToolBar, SWT.SEPARATOR);
		item.setToolTipText("SWT.SEPARATOR");
		if (comboChildButton.getSelection ()) {
			Combo combo = new Combo (imageToolBar, SWT.NONE);
			combo.setItems (new String [] {"250", "500", "750"});
			combo.setText (combo.getItem (0));
			combo.pack ();
			item.setWidth (combo.getSize ().x);
			item.setControl (combo);
		}
		item = new ToolItem (imageToolBar, SWT.DROP_DOWN);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setToolTipText ("SWT.DROP_DOWN");
		item.addSelectionListener(new DropDownSelectionListener());
	
		/* Create the text tool bar */
		textToolBar = new ToolBar (textToolBarGroup, style);
		item = new ToolItem (textToolBar, SWT.PUSH);
		item.setText (ControlExample.getResourceString("Push"));
		item.setToolTipText("SWT.PUSH");
		item = new ToolItem (textToolBar, SWT.PUSH);
		item.setText (ControlExample.getResourceString("Push"));
		item.setToolTipText("SWT.PUSH");
		item = new ToolItem (textToolBar, SWT.RADIO);
		item.setText (ControlExample.getResourceString("Radio"));
		item.setToolTipText("SWT.RADIO");
		item = new ToolItem (textToolBar, SWT.RADIO);
		item.setText (ControlExample.getResourceString("Radio"));
		item.setToolTipText("SWT.RADIO");
		item = new ToolItem (textToolBar, SWT.CHECK);
		item.setText (ControlExample.getResourceString("Check"));
		item.setToolTipText("SWT.CHECK");
		item = new ToolItem (textToolBar, SWT.RADIO);
		item.setText (ControlExample.getResourceString("Radio"));
		item.setToolTipText("SWT.RADIO");
		item = new ToolItem (textToolBar, SWT.RADIO);
		item.setText (ControlExample.getResourceString("Radio"));
		item.setToolTipText("SWT.RADIO");
		item = new ToolItem (textToolBar, SWT.SEPARATOR);
		item.setToolTipText("SWT.SEPARATOR");
		if (comboChildButton.getSelection ()) {
			Combo combo = new Combo (textToolBar, SWT.NONE);
			combo.setItems (new String [] {"250", "500", "750"});
			combo.setText (combo.getItem (0));
			combo.pack ();
			item.setWidth (combo.getSize ().x);
			item.setControl (combo);
		}
		item = new ToolItem (textToolBar, SWT.DROP_DOWN);
		item.setText (ControlExample.getResourceString("Drop_Down"));
		item.setToolTipText("SWT.DROP_DOWN");
		item.addSelectionListener(new DropDownSelectionListener());

		/* Create the image and text tool bar */
		imageTextToolBar = new ToolBar (imageTextToolBarGroup, style);
		item = new ToolItem (imageTextToolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setText (ControlExample.getResourceString("Push"));
		item.setToolTipText("SWT.PUSH");
		item = new ToolItem (imageTextToolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setText (ControlExample.getResourceString("Push"));
		item.setToolTipText("SWT.PUSH");
		item = new ToolItem (imageTextToolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setText (ControlExample.getResourceString("Radio"));
		item.setToolTipText("SWT.RADIO");
		item = new ToolItem (imageTextToolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setText (ControlExample.getResourceString("Radio"));
		item.setToolTipText("SWT.RADIO");
		item = new ToolItem (imageTextToolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setText (ControlExample.getResourceString("Check"));
		item.setToolTipText("SWT.CHECK");
		item = new ToolItem (imageTextToolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setText (ControlExample.getResourceString("Radio"));
		item.setToolTipText("SWT.RADIO");
		item = new ToolItem (imageTextToolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setText (ControlExample.getResourceString("Radio"));
		item.setToolTipText("SWT.RADIO");
		item = new ToolItem (imageTextToolBar, SWT.SEPARATOR);
		item.setToolTipText("SWT.SEPARATOR");
		if (comboChildButton.getSelection ()) {
			Combo combo = new Combo (imageTextToolBar, SWT.NONE);
			combo.setItems (new String [] {"250", "500", "750"});
			combo.setText (combo.getItem (0));
			combo.pack ();
			item.setWidth (combo.getSize ().x);
			item.setControl (combo);
		}
		item = new ToolItem (imageTextToolBar, SWT.DROP_DOWN);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setText (ControlExample.getResourceString("Drop_Down"));
		item.setToolTipText("SWT.DROP_DOWN");
		item.addSelectionListener(new DropDownSelectionListener());

		/*
		* Do not add the selection event for this drop down
		* tool item.  Without hooking the event, the drop down
		* widget does nothing special when the drop down area
		* is selected.
		*/
	}
	
	/**
	 * Creates the "Other" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
	
		/* Create display controls specific to this example */
		comboChildButton = new Button (otherGroup, SWT.CHECK);
		comboChildButton.setText (ControlExample.getResourceString("Combo_child"));
	
		/* Add the listeners */
		comboChildButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				recreateExampleWidgets ();
			}
		});
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widgets */
		horizontalButton = new Button (styleGroup, SWT.RADIO);
		horizontalButton.setText ("SWT.HORIZONTAL");
		verticalButton = new Button (styleGroup, SWT.RADIO);
		verticalButton.setText ("SWT.VERTICAL");
		flatButton = new Button (styleGroup, SWT.CHECK);
		flatButton.setText ("SWT.FLAT");
		shadowOutButton = new Button (styleGroup, SWT.CHECK);
		shadowOutButton.setText ("SWT.SHADOW_OUT");
		wrapButton = new Button (styleGroup, SWT.CHECK);
		wrapButton.setText ("SWT.WRAP");
		rightButton = new Button (styleGroup, SWT.CHECK);
		rightButton.setText ("SWT.RIGHT");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
	}
	
	void disposeExampleWidgets () {
		super.disposeExampleWidgets ();
	}
	
	/**
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		Item [] imageToolBarItems = imageToolBar.getItems();
		Item [] textToolBarItems = textToolBar.getItems();
		Item [] imageTextToolBarItems = imageTextToolBar.getItems();
		Item [] allItems = new Item [imageToolBarItems.length + textToolBarItems.length + imageTextToolBarItems.length];
		System.arraycopy(imageToolBarItems, 0, allItems, 0, imageToolBarItems.length);
		System.arraycopy(textToolBarItems, 0, allItems, imageToolBarItems.length, textToolBarItems.length);
		System.arraycopy(imageTextToolBarItems, 0, allItems, imageToolBarItems.length + textToolBarItems.length, imageTextToolBarItems.length);
		return allItems;
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {imageToolBar, textToolBar, imageTextToolBar};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"ToolTipText"};
	}

	/**
	 * Gets the short text for the tab folder item.
	 */
	String getShortTabText() {
		return "TB";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "ToolBar";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		horizontalButton.setSelection ((imageToolBar.getStyle () & SWT.HORIZONTAL) != 0);
		verticalButton.setSelection ((imageToolBar.getStyle () & SWT.VERTICAL) != 0);
		flatButton.setSelection ((imageToolBar.getStyle () & SWT.FLAT) != 0);
		wrapButton.setSelection ((imageToolBar.getStyle () & SWT.WRAP) != 0);
		shadowOutButton.setSelection ((imageToolBar.getStyle () & SWT.SHADOW_OUT) != 0);
		borderButton.setSelection ((imageToolBar.getStyle () & SWT.BORDER) != 0);
		rightButton.setSelection ((imageToolBar.getStyle () & SWT.RIGHT) != 0);
	}
	
	/**
	 * Listens to widgetSelected() events on SWT.DROP_DOWN type ToolItems
	 * and opens/closes a menu when appropriate.
	 */
	class DropDownSelectionListener extends SelectionAdapter {
		private Menu    menu = null;
		
		public void widgetSelected(SelectionEvent event) {
			// Create the menu if it has not already been created
			if (menu == null) {
				// Lazy create the menu.
				ToolBar toolbar = ((ToolItem) event.widget).getParent();
				int style = toolbar.getStyle() & (SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT);
				menu = new Menu(shell, style | SWT.POP_UP);
				for (int i = 0; i < 9; ++i) {
					final String text = ControlExample.getResourceString("DropDownData_" + i);
					if (text.length() != 0) {
						MenuItem menuItem = new MenuItem(menu, SWT.NONE);
						menuItem.setText(text);
					} else {
						new MenuItem(menu, SWT.SEPARATOR);
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
				// Position the menu below and vertically aligned with the the drop down tool button.
				final ToolItem toolItem = (ToolItem) event.widget;
				final ToolBar  toolBar = toolItem.getParent();
				
				Point point = toolBar.toDisplay(new Point(event.x, event.y));
				menu.setLocation(point.x, point.y);
				menu.setVisible(true);
			} 
		}
	}
}
