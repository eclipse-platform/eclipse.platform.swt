package org.eclipse.swt.examples.controlexample;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class CoolBarTab extends Tab {
	Button dropDownButton, lockedButton;

	/* Example widgets and group that contains them */
	CoolBar coolBar;
	CoolItem pushItem, dropDownItem, radioItem, checkItem;
	Group coolBarGroup;
	
	Point[] sizes;
	int[] wrapIndices;
	int[] order;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	CoolBarTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Display" group.
	 */
	void createDisplayGroup () {
		super.createDisplayGroup ();
	
		/* Create display controls specific to this example */
		lockedButton = new Button (displayGroup, SWT.CHECK);
		lockedButton.setText (ControlExample.getResourceString("Locked"));
	
		/* Add the listeners */
		lockedButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetLocked ();
			}
		});
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		coolBarGroup = new Group (exampleGroup, SWT.NULL);
		coolBarGroup.setLayout (new GridLayout ());
		coolBarGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		coolBarGroup.setText ("CoolBar");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		int style = 0, itemStyle = 0;

		/* Compute the widget style */
		int toolBarStyle = SWT.FLAT;
		if (borderButton.getSelection()) style |= SWT.BORDER;
		if (dropDownButton.getSelection()) itemStyle |= SWT.DROP_DOWN;
	
		/*
		* Create the example widgets.
		*/
		coolBar = new CoolBar (coolBarGroup, style);
		
		/* create the push button toolbar */
		ToolBar toolBar = new ToolBar (coolBar, toolBarStyle);
		ToolItem item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.PUSH");
		item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText ("SWT.PUSH");
		item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setToolTipText ("SWT.PUSH");
		item = new ToolItem (toolBar, SWT.SEPARATOR);
		item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.PUSH");
		item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText ("SWT.PUSH");		
		pushItem = new CoolItem (coolBar, itemStyle);
		pushItem.setControl (toolBar);
		Point pushSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pushSize = pushItem.computeSize(pushSize.x, pushSize.y);
		pushItem.setSize(pushSize);
		pushItem.setMinimumSize(item.getWidth(), pushSize.y);
        pushItem.addSelectionListener (new CoolItemSelectionListener());
				
		/* create the dropdown toolbar */
		toolBar = new ToolBar (coolBar, toolBarStyle);
		item = new ToolItem (toolBar, SWT.DROP_DOWN);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText ("SWT.DROP_DOWN");
		item.addSelectionListener (new DropDownSelectionListener());
		item = new ToolItem (toolBar, SWT.DROP_DOWN);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.DROP_DOWN");
		item.addSelectionListener (new DropDownSelectionListener());
		dropDownItem = new CoolItem (coolBar, itemStyle);
		dropDownItem.setControl (toolBar);
		Point dropSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		dropSize = dropDownItem.computeSize(dropSize.x, dropSize.y);
		dropDownItem.setSize(dropSize);
		dropDownItem.setMinimumSize(item.getWidth(), dropSize.y);
        dropDownItem.addSelectionListener (new CoolItemSelectionListener());
				
		/* create the radio button toolbar */
		toolBar = new ToolBar (coolBar, toolBarStyle);
		item = new ToolItem (toolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.RADIO");
		item = new ToolItem (toolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.RADIO");
		item = new ToolItem (toolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.RADIO");
		radioItem = new CoolItem (coolBar, itemStyle);
		radioItem.setControl (toolBar);
		Point radioSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		radioSize = radioItem.computeSize(radioSize.x, radioSize.y);
		radioItem.setSize(radioSize);
		radioItem.setMinimumSize(item.getWidth(), radioSize.y);
        radioItem.addSelectionListener (new CoolItemSelectionListener());
		
		/* create the check button toolbar */
		toolBar = new ToolBar (coolBar, toolBarStyle);
		item = new ToolItem (toolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText ("SWT.CHECK");
		item = new ToolItem (toolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setToolTipText ("SWT.CHECK");
		item = new ToolItem (toolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText ("SWT.CHECK");
		item = new ToolItem (toolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setToolTipText ("SWT.CHECK");
		checkItem = new CoolItem (coolBar, itemStyle);
		checkItem.setControl (toolBar);
		Point checkSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		checkSize = checkItem.computeSize(checkSize.x, checkSize.y);
		checkItem.setSize(checkSize);
		checkItem.setMinimumSize(item.getWidth(), checkSize.y);
        checkItem.addSelectionListener (new CoolItemSelectionListener());
		
		/* if we have saved state, restore it */
		if (order != null) {
			coolBar.setItemLayout(order, wrapIndices, sizes);
			/* 
			 * special case: because setItemLayout will restore the items
			 * to the sizes the user left them at, the preferred size may not
			 * be the same as the actual size. Thus we must explicitly set
			 * the preferred sizes.
			 */
			pushItem.setPreferredSize(pushSize);
			dropDownItem.setPreferredSize(dropSize);
			radioItem.setPreferredSize(radioSize);
			checkItem.setPreferredSize(checkSize);
		}
		else {
			coolBar.setWrapIndices(new int[] {1, 3});
		}
		
		/* add a listener to resize the group box to match the coolbar */
		coolBar.addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event event) {
				exampleGroup.layout();
			}
		});
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widget */
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
		Group itemGroup = new Group(styleGroup, SWT.NONE);
		itemGroup.setLayout (new GridLayout ());
		itemGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		itemGroup.setText(ControlExample.getResourceString("Item_Styles"));
		dropDownButton = new Button (itemGroup, SWT.CHECK);
		dropDownButton.setText ("SWT.DROP_DOWN");
		dropDownButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				recreateExampleWidgets ();
			}
		});
	}
	
	/**
	 * Disposes the "Example" widgets.
	 */
	void disposeExampleWidgets () {
		/* store the state of the toolbar if applicable */
		if (coolBar != null) {
			sizes = coolBar.getItemSizes();
			wrapIndices = coolBar.getWrapIndices();
			order = coolBar.getItemOrder();
		}
		super.disposeExampleWidgets();	
	}

	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {coolBar};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "CoolBar";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		borderButton.setSelection ((coolBar.getStyle () & SWT.BORDER) != 0);
		dropDownButton.setSelection ((coolBar.getItem(0).getStyle () & SWT.DROP_DOWN) != 0);
		setWidgetLocked ();
	}
	
	/**
	 * Sets the header visible state of the "Example" widgets.
	 */
	void setWidgetLocked () {
		coolBar.setLocked (lockedButton.getSelection ());
	}
	
	/**
	 * Listens to widgetSelected() events on SWT.DROP_DOWN type ToolItems
	 * and opens/closes a menu when appropriate.
	 */
	class DropDownSelectionListener extends SelectionAdapter {
		private Menu menu = null;
		private boolean visible = false;
		
		public void widgetSelected(SelectionEvent event) {
			// Create the menu if it has not already been created
			if (menu == null) {
				// Lazy create the menu.
				Shell shell = tabFolderPage.getShell();
				menu = new Menu(shell);
				for (int i = 0; i < 9; ++i) {
					final String text = ControlExample.getResourceString("DropDownData_" + i);
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

	/**
	 * Listens to widgetSelected() events on SWT.DROP_DOWN type CoolItems
	 * and opens/closes a menu when appropriate.
	 */
	class CoolItemSelectionListener extends SelectionAdapter {
		private Menu menu = null;
		
		public void widgetSelected(SelectionEvent event) {
			/**
			 * A selection event will be fired when the cool item
			 * is selected by its gripper or if the drop down arrow
			 * (or 'chevron') is selected. Examine the event detail
			 * to determine where the widget was selected.
			 */
			if (event.detail == SWT.ARROW) {
				/* If the popup menu is already up (i.e. user pressed arrow twice),
				 * then dispose it.
				 */
				if (menu != null) {
					menu.dispose();
					menu = null;
					return;
				}
				
				/* Get the cool item and convert its bounds to display coordinates. */
				CoolItem coolItem = (CoolItem) event.widget;
				Rectangle itemBounds = coolItem.getBounds ();
				Point pt = coolBar.toDisplay(new Point (itemBounds.x, itemBounds.y));
				itemBounds.x = pt.x;
				itemBounds.y = pt.y;
				
				/* Get the toolbar from the cool item. */
				ToolBar toolBar = (ToolBar) coolItem.getControl ();
				ToolItem[] tools = toolBar.getItems ();
				int toolCount = tools.length;
								
				/* Convert the bounds of each tool item to display coordinates,
				 * and determine which ones are past the bounds of the cool item.
				 */
				int i = 0;
				while (i < toolCount) {
					Rectangle toolBounds = tools[i].getBounds ();
					pt = toolBar.toDisplay(new Point(toolBounds.x, toolBounds.y));
					toolBounds.x = pt.x;
					toolBounds.y = pt.y;
			  		Rectangle intersection = itemBounds.intersection (toolBounds);
			  		if (!intersection.equals (toolBounds)) break;
			  		i++;
				}
				
				/* Create a pop-up menu with items for each of the hidden buttons. */
				menu = new Menu (coolBar);
				for (int j = i; j < toolCount; j++) {
					ToolItem tool = tools[j];
					Image image = tool.getImage();
					if (image == null) {
						new MenuItem (menu, SWT.SEPARATOR);
					} else {
						if ((tool.getStyle() & SWT.DROP_DOWN) != 0) {
							MenuItem menuItem = new MenuItem (menu, SWT.CASCADE);
							menuItem.setImage(image);
							Menu m = new Menu(menu);
							menuItem.setMenu(m);
							for (int k = 0; k < 9; ++k) {
								String text = ControlExample.getResourceString("DropDownData_" + k);
								if (text.length() != 0) {
									MenuItem mi = new MenuItem(m, SWT.NONE);
									mi.setText(text);
									/* Application code to perform the action for the submenu item would go here. */
								} else {
									new MenuItem(m, SWT.SEPARATOR);
								}
							}
						} else {
							MenuItem menuItem = new MenuItem (menu, SWT.NONE);
							menuItem.setImage(image);
						}
						/* Application code to perform the action for the menu item would go here. */

					}
				}
				
				/* Display the pop-up menu at the lower left corner of the arrow button.
				 * Dispose the menu when the user is done with it.
				 */
				pt = coolBar.toDisplay(new Point(event.x, event.y));
				menu.setLocation (pt.x, pt.y);
				menu.setVisible (true);
				Display display = coolBar.getDisplay ();
				while (menu.isVisible ()) {
					if (!display.readAndDispatch ()) display.sleep ();
				}
				if (menu != null) {
					menu.dispose ();
					menu = null;
				}
			}
		}
	}
}
