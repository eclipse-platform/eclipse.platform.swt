/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
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
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class MenuTab extends Tab {	
	/* Widgets added to the "Menu Style", "MenuItem Style" and "Other" groups */
	Button barButton, dropDownButton, popUpButton, noRadioGroupButton, leftToRightButton, rightToLeftButton;
	Button checkButton, cascadeButton, pushButton, radioButton, separatorButton;
	Button imagesButton, acceleratorsButton, mnemonicsButton, subMenuButton, subSubMenuButton;
	Button createButton, closeAllButton;
	Group menuItemStyleGroup;

	/* Variables used to track the open shells */
	int shellCount = 0;
	Shell [] shells = new Shell [4];
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	MenuTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Close all the example shells.
	 */
	void closeAllShells() {
		for (int i = 0; i<shellCount; i++) {
			if (shells[i] != null & !shells [i].isDisposed ()) {
				shells [i].dispose();
			}
		}
		shellCount = 0;
	}
	
	/**
	 * Handle the Create button selection event.
	 *
	 * @param event org.eclipse.swt.events.SelectionEvent
	 */
	public void createButtonSelected(SelectionEvent event) {
	
		/*
		 * Remember the example shells so they
		 * can be disposed by the user.
		 */
		if (shellCount >= shells.length) {
			Shell [] newShells = new Shell [shells.length + 4];
			System.arraycopy (shells, 0, newShells, 0, shells.length);
			shells = newShells;
		}
	
		int orientation = 0;
		if (leftToRightButton.getSelection()) orientation |= SWT.LEFT_TO_RIGHT;
		if (rightToLeftButton.getSelection()) orientation |= SWT.RIGHT_TO_LEFT;
		int radioBehavior = 0;
		if (noRadioGroupButton.getSelection()) radioBehavior |= SWT.NO_RADIO_GROUP;
		
		/* Create the shell and menu(s) */
		Shell shell = new Shell (SWT.SHELL_TRIM | orientation);
		shells [shellCount] = shell;
		if (barButton.getSelection ()) {
			/* Create menu bar. */
			Menu menuBar = new Menu(shell, SWT.BAR | radioBehavior);
			shell.setMenuBar(menuBar);
			hookListeners(menuBar);

			if (dropDownButton.getSelection() && cascadeButton.getSelection()) {
				/* Create cascade button and drop-down menu in menu bar. */
				MenuItem item = new MenuItem(menuBar, SWT.CASCADE);
				item.setText(getMenuItemText("Cascade"));
				if (imagesButton.getSelection()) item.setImage(instance.images[ControlExample.ciOpenFolder]);
				hookListeners(item);
				Menu dropDownMenu = new Menu(shell, SWT.DROP_DOWN | radioBehavior);
				item.setMenu(dropDownMenu);
				hookListeners(dropDownMenu);
	
				/* Create various menu items, depending on selections. */
				createMenuItems(dropDownMenu, subMenuButton.getSelection(), subSubMenuButton.getSelection());
			}
		}
		
		if (popUpButton.getSelection()) {
			/* Create pop-up menu. */
			Menu popUpMenu = new Menu(shell, SWT.POP_UP | radioBehavior);
			shell.setMenu(popUpMenu);
			hookListeners(popUpMenu);

			/* Create various menu items, depending on selections. */
			createMenuItems(popUpMenu, subMenuButton.getSelection(), subSubMenuButton.getSelection());
		}
		
		/* Set the size, title and open the shell. */
		shell.setSize (300, 100);
		shell.setText (ControlExample.getResourceString("Title") + shellCount);
		shell.addPaintListener(new PaintListener() {
			public void paintControl(PaintEvent e) {
				e.gc.drawString(ControlExample.getResourceString("PopupMenuHere"), 20, 20);
			}
		});
		shell.open ();
		shellCount++;
	}
	
	/**
	 * Creates the "Control" group. 
	 */
	void createControlGroup () {
		/*
		 * Create the "Control" group.  This is the group on the
		 * right half of each example tab.  For MenuTab, it consists of
		 * the Menu style group, the MenuItem style group and the 'other' group.
		 */		
		controlGroup = new Group (tabFolderPage, SWT.NONE);
		controlGroup.setLayout (new GridLayout (2, true));
		controlGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		controlGroup.setText (ControlExample.getResourceString("Parameters"));
	
		/* Create a group for the menu style controls */
		styleGroup = new Group (controlGroup, SWT.NONE);
		styleGroup.setLayout (new GridLayout ());
		styleGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		styleGroup.setText (ControlExample.getResourceString("Menu_Styles"));
	
		/* Create a group for the menu item style controls */
		menuItemStyleGroup = new Group (controlGroup, SWT.NONE);
		menuItemStyleGroup.setLayout (new GridLayout ());
		menuItemStyleGroup.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		menuItemStyleGroup.setText (ControlExample.getResourceString("MenuItem_Styles"));

		/* Create a group for the 'other' controls */
		otherGroup = new Group (controlGroup, SWT.NONE);
		otherGroup.setLayout (new GridLayout ());
		otherGroup.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		otherGroup.setText (ControlExample.getResourceString("Other"));
	}
	
	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets () {
	
		/* Create the menu style buttons */
		barButton = new Button (styleGroup, SWT.CHECK);
		barButton.setText ("SWT.BAR");
		dropDownButton = new Button (styleGroup, SWT.CHECK);
		dropDownButton.setText ("SWT.DROP_DOWN");
		popUpButton = new Button (styleGroup, SWT.CHECK);
		popUpButton.setText ("SWT.POP_UP");
		noRadioGroupButton = new Button (styleGroup, SWT.CHECK);
		noRadioGroupButton.setText ("SWT.NO_RADIO_GROUP");
		leftToRightButton = new Button (styleGroup, SWT.RADIO);
		leftToRightButton.setText ("SWT.LEFT_TO_RIGHT");
		leftToRightButton.setSelection(true);
		rightToLeftButton = new Button (styleGroup, SWT.RADIO);
		rightToLeftButton.setText ("SWT.RIGHT_TO_LEFT");
	
		/* Create the menu item style buttons */
		cascadeButton = new Button (menuItemStyleGroup, SWT.CHECK);
		cascadeButton.setText ("SWT.CASCADE");
		checkButton = new Button (menuItemStyleGroup, SWT.CHECK);
		checkButton.setText ("SWT.CHECK");
		pushButton = new Button (menuItemStyleGroup, SWT.CHECK);
		pushButton.setText ("SWT.PUSH");
		radioButton = new Button (menuItemStyleGroup, SWT.CHECK);
		radioButton.setText ("SWT.RADIO");
		separatorButton = new Button (menuItemStyleGroup, SWT.CHECK);
		separatorButton.setText ("SWT.SEPARATOR");
		
		/* Create the 'other' buttons */
		enabledButton = new Button(otherGroup, SWT.CHECK);
		enabledButton.setText(ControlExample.getResourceString("Enabled"));
		enabledButton.setSelection(true);
		imagesButton = new Button (otherGroup, SWT.CHECK);
		imagesButton.setText (ControlExample.getResourceString("Images"));
		acceleratorsButton = new Button (otherGroup, SWT.CHECK);
		acceleratorsButton.setText (ControlExample.getResourceString("Accelerators"));
		mnemonicsButton = new Button (otherGroup, SWT.CHECK);
		mnemonicsButton.setText (ControlExample.getResourceString("Mnemonics"));
		subMenuButton = new Button (otherGroup, SWT.CHECK);
		subMenuButton.setText (ControlExample.getResourceString("SubMenu"));
		subSubMenuButton = new Button (otherGroup, SWT.CHECK);
		subSubMenuButton.setText (ControlExample.getResourceString("SubSubMenu"));
		
		/* Create the "create" and "closeAll" buttons (and a 'filler' label to place them) */
		new Label(controlGroup, SWT.NONE);
		createButton = new Button (controlGroup, SWT.NONE);
		createButton.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_END));
		createButton.setText (ControlExample.getResourceString("Create_Shell"));
		closeAllButton = new Button (controlGroup, SWT.NONE);
		closeAllButton.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING));
		closeAllButton.setText (ControlExample.getResourceString("Close_All_Shells"));
	
		/* Add the listeners */
		createButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				createButtonSelected(e);
			}
		});
		closeAllButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				closeAllShells ();
			}
		});
		subMenuButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				subSubMenuButton.setEnabled (subMenuButton.getSelection ());
			}
		});
	
		/* Set the default state */
		barButton.setSelection (true);
		dropDownButton.setSelection (true);
		popUpButton.setSelection (true);
		cascadeButton.setSelection (true);
		checkButton.setSelection (true);
		pushButton.setSelection (true);
		radioButton.setSelection (true);
		separatorButton.setSelection (true);
		subSubMenuButton.setEnabled (subMenuButton.getSelection ());
	}
	
	/* Create various menu items, depending on selections. */
	void createMenuItems(Menu menu, boolean createSubMenu, boolean createSubSubMenu) {
		MenuItem item;
		if (pushButton.getSelection()) {
			item = new MenuItem(menu, SWT.PUSH);
			item.setText(getMenuItemText("Push"));
			if (acceleratorsButton.getSelection()) item.setAccelerator(SWT.MOD1 + SWT.MOD2 + 'P');
			if (imagesButton.getSelection()) item.setImage(instance.images[ControlExample.ciClosedFolder]);
			item.setEnabled(enabledButton.getSelection());
			hookListeners(item);
		}
		
		if (separatorButton.getSelection()) {
			new MenuItem(menu, SWT.SEPARATOR);
		}
		
		if (checkButton.getSelection()) {
			item = new MenuItem(menu, SWT.CHECK);
			item.setText(getMenuItemText("Check"));
			if (acceleratorsButton.getSelection()) item.setAccelerator(SWT.MOD1 + SWT.MOD2 + 'C');
			if (imagesButton.getSelection()) item.setImage(instance.images[ControlExample.ciOpenFolder]);
			item.setEnabled(enabledButton.getSelection());
			hookListeners(item);
		}
				
		if (radioButton.getSelection()) {
			item = new MenuItem(menu, SWT.RADIO);
			item.setText(getMenuItemText("1Radio"));
			if (acceleratorsButton.getSelection()) item.setAccelerator(SWT.MOD1 + SWT.MOD2 + '1');
			if (imagesButton.getSelection()) item.setImage(instance.images[ControlExample.ciTarget]);
			item.setSelection(true);
			item.setEnabled(enabledButton.getSelection());
			hookListeners(item);

			item = new MenuItem(menu, SWT.RADIO);
			item.setText(getMenuItemText("2Radio"));
			if (acceleratorsButton.getSelection()) item.setAccelerator(SWT.MOD1 + SWT.MOD2 + '2');
			if (imagesButton.getSelection()) item.setImage(instance.images[ControlExample.ciTarget]);
			item.setEnabled(enabledButton.getSelection());
			hookListeners(item);
		}

		if (createSubMenu && cascadeButton.getSelection()) {
			/* Create cascade button and drop-down menu for the sub-menu. */
			item = new MenuItem(menu, SWT.CASCADE);
			item.setText(getMenuItemText("Cascade"));
			if (imagesButton.getSelection()) item.setImage(instance.images[ControlExample.ciOpenFolder]);
			hookListeners(item);
			Menu subMenu = new Menu(menu.getShell(), SWT.DROP_DOWN);
			item.setMenu(subMenu);
			item.setEnabled(enabledButton.getSelection());
			hookListeners(subMenu);
			
			createMenuItems(subMenu, createSubSubMenu, false);
		}
	}
	
	String getMenuItemText(String item) {
		boolean cascade = item.equals("Cascade");
		boolean mnemonic = mnemonicsButton.getSelection();
		boolean accelerator = acceleratorsButton.getSelection();
		char acceleratorKey = item.charAt(0);
		if (mnemonic && accelerator && !cascade) {
			return ControlExample.getResourceString(item + "WithMnemonic") + "\tCtrl+Shift+" + acceleratorKey;
		}
		if (accelerator && !cascade) {
			return ControlExample.getResourceString(item) + "\tCtrl+Shift+" + acceleratorKey;
		}
		if (mnemonic) {
			return ControlExample.getResourceString(item + "WithMnemonic");
		}
		return ControlExample.getResourceString(item);
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Menu";
	}
}
