/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

class CoolBarTab extends Tab {
	/* Example widgets and group that contains them */
	CoolBar coolBar;
	CoolItem pushItem, dropDownItem, radioItem, checkItem, textItem;
	Group coolBarGroup;
	
	/* Style widgets added to the "Style" group */
	Button horizontalButton, verticalButton;
	Button dropDownButton, flatButton;

	/* Other widgets added to the "Other" group */
	Button lockedButton;
	
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
	 * Creates the "Other" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
	
		/* Create display controls specific to this example */
		lockedButton = new Button (otherGroup, SWT.CHECK);
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
		coolBarGroup = new Group (exampleGroup, SWT.NONE);
		coolBarGroup.setLayout (new GridLayout ());
		coolBarGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		coolBarGroup.setText ("CoolBar");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		int style = getDefaultStyle(), itemStyle = 0;

		/* Compute the widget, item, and item toolBar styles */
		int toolBarStyle = SWT.FLAT;
		boolean vertical = false;
		if (horizontalButton.getSelection ()) {
			style |= SWT.HORIZONTAL;
			toolBarStyle |= SWT.HORIZONTAL;
		}
		if (verticalButton.getSelection ()) {
			style |= SWT.VERTICAL;
			toolBarStyle |= SWT.VERTICAL;
			vertical = true;
		}
		if (borderButton.getSelection()) style |= SWT.BORDER;
		if (flatButton.getSelection()) style |= SWT.FLAT;
		if (dropDownButton.getSelection()) itemStyle |= SWT.DROP_DOWN;
	
		/*
		* Create the example widgets.
		*/
		coolBar = new CoolBar (coolBarGroup, style);
		
		/* Create the push button toolbar cool item */
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
		pushItem.addSelectionListener (new CoolItemSelectionListener());
				
		/* Create the dropdown toolbar cool item */
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
		dropDownItem.addSelectionListener (new CoolItemSelectionListener());

		/* Create the radio button toolbar cool item */
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
		radioItem.addSelectionListener (new CoolItemSelectionListener());
		
		/* Create the check button toolbar cool item */
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
		checkItem.addSelectionListener (new CoolItemSelectionListener());
		
		/* Create the text cool item */
		if (!vertical) {
			Text text = new Text (coolBar, SWT.BORDER | SWT.SINGLE);
			textItem = new CoolItem (coolBar, itemStyle);
			textItem.setControl (text);
			textItem.addSelectionListener (new CoolItemSelectionListener());
			Point textSize = text.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			textSize = textItem.computeSize(textSize.x, textSize.y);
			textItem.setMinimumSize(textSize);
			textItem.setPreferredSize(textSize);
			textItem.setSize(textSize);
		}

		/* Set the sizes after adding all cool items */
		CoolItem[] coolItems = coolBar.getItems();
		for (int i = 0; i < coolItems.length; i++) {
			CoolItem coolItem = coolItems[i];
			Control control = coolItem.getControl();
			Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			Point coolSize = coolItem.computeSize(size.x, size.y);
			if (control instanceof ToolBar) {
				ToolBar bar = (ToolBar)control;
				if (bar.getItemCount() > 0) {
					if (vertical) {
						size.y = bar.getItem(0).getBounds().height;
					} else {
						size.x = bar.getItem(0).getWidth();
					}
				}
			}
			coolItem.setMinimumSize(size);
			coolItem.setPreferredSize(coolSize);
			coolItem.setSize(coolSize);
		}
		
		/* If we have saved state, restore it */
		if (order != null && order.length == coolBar.getItemCount()) {
			coolBar.setItemLayout(order, wrapIndices, sizes);
		} else {
			coolBar.setWrapIndices(new int[] {1, 3});
		}
		
		/* Add a listener to resize the group box to match the coolbar */
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
	
		/* Create the extra widgets */
		horizontalButton = new Button (styleGroup, SWT.RADIO);
		horizontalButton.setText ("SWT.HORIZONTAL");
		verticalButton = new Button (styleGroup, SWT.RADIO);
		verticalButton.setText ("SWT.VERTICAL");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
		flatButton = new Button (styleGroup, SWT.CHECK);
		flatButton.setText ("SWT.FLAT");
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
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		return coolBar.getItems();
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {coolBar};
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
	public String getShortTabText() {
		return "CB";
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
		horizontalButton.setSelection ((coolBar.getStyle () & SWT.HORIZONTAL) != 0);
		verticalButton.setSelection ((coolBar.getStyle () & SWT.VERTICAL) != 0);
		borderButton.setSelection ((coolBar.getStyle () & SWT.BORDER) != 0);
		flatButton.setSelection ((coolBar.getStyle () & SWT.FLAT) != 0);
		dropDownButton.setSelection ((coolBar.getItem(0).getStyle () & SWT.DROP_DOWN) != 0);
		if (!instance.startup) setWidgetLocked ();
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
				menu = new Menu(shell);
				menu.addMenuListener(new MenuAdapter() {
					public void menuHidden(MenuEvent e) {
						visible = false;
					}
				});
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
				itemBounds.width = event.x - itemBounds.x;
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
							String text = tool.getToolTipText();
							if (text != null) menuItem.setText(text);
							Menu m = new Menu(menu);
							menuItem.setMenu(m);
							for (int k = 0; k < 9; ++k) {
								text = ControlExample.getResourceString("DropDownData_" + k);
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
							String text = tool.getToolTipText();
							if (text != null) menuItem.setText(text);
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
				while (menu != null && !menu.isDisposed() && menu.isVisible ()) {
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
