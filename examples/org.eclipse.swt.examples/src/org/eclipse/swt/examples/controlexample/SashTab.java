/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.events.*;

class SashTab extends Tab {
	/* Example widgets and groups that contain them */
	Sash hSash, vSash;
	List list1, list2, list3;
	Text text;

	static String [] ListData0 = {ControlExample.getResourceString("ListData0_0"),
								  ControlExample.getResourceString("ListData0_1"),
								  ControlExample.getResourceString("ListData0_2"),
								  ControlExample.getResourceString("ListData0_3"),
								  ControlExample.getResourceString("ListData0_4"),
								  ControlExample.getResourceString("ListData0_5"),
								  ControlExample.getResourceString("ListData0_6"),
								  ControlExample.getResourceString("ListData0_7"),
								  ControlExample.getResourceString("ListData0_8")};
								  
	static String [] ListData1 = {ControlExample.getResourceString("ListData1_0"),
								  ControlExample.getResourceString("ListData1_1"),
								  ControlExample.getResourceString("ListData1_2"),
								  ControlExample.getResourceString("ListData1_3"),
								  ControlExample.getResourceString("ListData1_4"),
								  ControlExample.getResourceString("ListData1_5"),
								  ControlExample.getResourceString("ListData1_6"),
								  ControlExample.getResourceString("ListData1_7"),
								  ControlExample.getResourceString("ListData1_8")};

	/* Constants */
	static final int SASH_WIDTH = 3;
	static final int SASH_LIMIT = 20;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	SashTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the tab folder page.
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		/*
		 * Create the page.  This example does not use layouts.
		 */
		tabFolderPage = new Composite(tabFolder, SWT.BORDER);
	
		/* Create the list and text widgets */
		list1 = new List (tabFolderPage, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		list1.setItems (ListData0);
		list2 = new List (tabFolderPage, SWT.V_SCROLL | SWT.H_SCROLL | SWT.BORDER);
		list2.setItems (ListData1);
		text = new Text (tabFolderPage, SWT.MULTI | SWT.BORDER);
		text.setText (ControlExample.getResourceString("Multi_line"));
	
		/* Create the sashes */
		vSash = new Sash (tabFolderPage, SWT.VERTICAL);
		hSash = new Sash (tabFolderPage, SWT.HORIZONTAL);
		
		/* Add the listeners */
		hSash.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Rectangle rect = vSash.getParent().getClientArea();
				event.y = Math.min (Math.max (event.y, SASH_LIMIT), rect.height - SASH_LIMIT);
				if (event.detail != SWT.DRAG) {
					hSash.setBounds (event.x, event.y, event.width, event.height);
					layout ();
				}
			}
		});
		vSash.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Rectangle rect = vSash.getParent().getClientArea();
				event.x = Math.min (Math.max (event.x, SASH_LIMIT), rect.width - SASH_LIMIT);
				if (event.detail != SWT.DRAG) {
					vSash.setBounds (event.x, event.y, event.width, event.height);
					layout ();
				}
			}
		});
		tabFolderPage.addControlListener (new ControlAdapter () {
			public void controlResized (ControlEvent event) {
				shellResized ();
			}
		});
	
		/*
		* Do not set the bounds of the lists, text and sashes here 
		* because this method is run before the widget is opened 
		* so we do not know how big the tabComposite is going to be.
		* When the widget is opened a resize event will occur 
		* and the contained widgets can be sized accordingly.
		*/
		return tabFolderPage; 
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {hSash, vSash};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Sash";
	}
	
	/**
	 * Layout the list and text widgets according to the new
	 * positions of the sashes..events.SelectionEvent
	 */
	void layout () {
		
		Rectangle tabCompositeBounds = tabFolderPage.getClientArea ();
		Rectangle hSashBounds = hSash.getBounds ();
		Rectangle vSashBounds = vSash.getBounds ();
		
		list1.setBounds (0, 0, vSashBounds.x, hSashBounds.y);
		list2.setBounds (vSashBounds.x + vSashBounds.width, 0, tabCompositeBounds.width - (vSashBounds.x + vSashBounds.width), hSashBounds.y);
		text.setBounds (0, hSashBounds.y + hSashBounds.height, tabCompositeBounds.width, tabCompositeBounds.height - (hSashBounds.y + hSashBounds.height));
	
		/**
		* If the horizontal sash has been moved then the vertical
		* sash is either too long or too short and its size must
		* be adjusted.
		*/
		vSashBounds.height = hSashBounds.y;
		vSash.setBounds (vSashBounds);
	}
	
	/**
	 * Recreates the "Example" widgets.
	 */
	void recreateExampleWidgets () {
		// this tab does not use this framework mechanism
	}
	
	/**
	 * Handle the shell resized event.
	 */
	void shellResized () {
	
		/* Get the client area for the shell */
		Rectangle tabFolderPageBounds = tabFolderPage.getClientArea ();
		
		/*
		* Make list 1 half the width and half the height of the tab leaving room for the sash.
		* Place list 1 in the top left quadrant of the tab.
		*/
		Rectangle list1Bounds = new Rectangle (0, 0, (tabFolderPageBounds.width - SASH_WIDTH) / 2, (tabFolderPageBounds.height - SASH_WIDTH) / 2);
		list1.setBounds (list1Bounds);
	
		/*
		* Make list 2 half the width and half the height of the tab leaving room for the sash.
		* Place list 2 in the top right quadrant of the tab.
		*/
		list2.setBounds (list1Bounds.width + SASH_WIDTH, 0, tabFolderPageBounds.width - (list1Bounds.width + SASH_WIDTH), list1Bounds.height);
	
		/*
		* Make the text area the full width and half the height of the tab leaving room for the sash.
		* Place the text area in the bottom half of the tab.
		*/
		text.setBounds (0, list1Bounds.height + SASH_WIDTH, tabFolderPageBounds.width, tabFolderPageBounds.height - (list1Bounds.height + SASH_WIDTH));
	
		/* Position the sashes */
		vSash.setBounds (list1Bounds.width, 0, SASH_WIDTH, list1Bounds.height);
		hSash.setBounds (0, list1Bounds.height, tabFolderPageBounds.width, SASH_WIDTH);
	}
}
