package org.eclipse.swt.examples.controlexample;
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class CoolBarTab extends Tab {
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
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		coolBarGroup = new Group (exampleGroup, SWT.NULL);
		coolBarGroup.setLayout (new GridLayout ());
		coolBarGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		coolBarGroup.setText (ControlExample.getResourceString("CoolBar"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		int style = 0;

		/* Compute the widget style */
		int toolBarStyle = SWT.FLAT;
		if (borderButton.getSelection()) style |= SWT.BORDER;
	
		/*
		* Create the example widgets.
		*/
		coolBar = new CoolBar (coolBarGroup, style);

		/* create the push button toolbar */
		ToolBar toolBar = new ToolBar (coolBar, toolBarStyle);
		ToolItem item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_PUSH"));
		item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_PUSH"));
		item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setToolTipText (ControlExample.getResourceString("SWT_PUSH"));
		item = new ToolItem (toolBar, SWT.SEPARATOR);
		item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_PUSH"));
		item = new ToolItem (toolBar, SWT.PUSH);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_PUSH"));		
		pushItem = new CoolItem (coolBar, SWT.NULL);
		pushItem.setControl (toolBar);
		Point pushSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		pushSize = pushItem.computeSize(pushSize.x, pushSize.y);
		pushItem.setSize(pushSize);
				
		/* create the dropdown toolbar */
		toolBar = new ToolBar (coolBar, toolBarStyle);
		item = new ToolItem (toolBar, SWT.DROP_DOWN);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_DROP_DOWN"));
		item.addSelectionListener (new DropDownSelectionListener());
		item = new ToolItem (toolBar, SWT.DROP_DOWN);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_DROP_DOWN"));
		item.addSelectionListener (new DropDownSelectionListener());
		dropDownItem = new CoolItem (coolBar, SWT.NULL);
		dropDownItem.setControl (toolBar);
		Point dropSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		dropSize = dropDownItem.computeSize(dropSize.x, dropSize.y);
		dropDownItem.setSize(dropSize);
				
		/* create the radio button toolbar */
		toolBar = new ToolBar (coolBar, toolBarStyle);
		item = new ToolItem (toolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_RADIO"));
		item = new ToolItem (toolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_RADIO"));
		item = new ToolItem (toolBar, SWT.RADIO);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_RADIO"));
		radioItem = new CoolItem (coolBar, SWT.NULL);
		radioItem.setControl (toolBar);
		Point radioSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		radioSize = radioItem.computeSize(radioSize.x, radioSize.y);
		radioItem.setSize(radioSize);
		
		/* create the check button toolbar */
		toolBar = new ToolBar (coolBar, toolBarStyle);
		item = new ToolItem (toolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciClosedFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_CHECK"));
		item = new ToolItem (toolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setToolTipText (ControlExample.getResourceString("SWT_CHECK"));
		item = new ToolItem (toolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciOpenFolder]);
		item.setToolTipText (ControlExample.getResourceString("SWT_CHECK"));
		item = new ToolItem (toolBar, SWT.CHECK);
		item.setImage (instance.images[ControlExample.ciTarget]);
		item.setToolTipText (ControlExample.getResourceString("SWT_CHECK"));
		checkItem = new CoolItem (coolBar, SWT.NULL);
		checkItem.setControl (toolBar);
		Point checkSize = toolBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		checkSize = checkItem.computeSize(checkSize.x, checkSize.y);
		checkItem.setSize(checkSize);
		
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
		borderButton.setText (ControlExample.getResourceString("SWT_BORDER"));
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
		return ControlExample.getResourceString("CoolBar");
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		borderButton.setSelection ((coolBar.getStyle () & SWT.BORDER) != 0);
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
}
