/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

class ShellTab extends Tab {	
	/* Style widgets added to the "Style" groups, and "Other" group */
	Button noParentButton, parentButton;
	Button noTrimButton, closeButton, titleButton, minButton, maxButton, borderButton, resizeButton, onTopButton, toolButton, sheetButton, shellTrimButton, dialogTrimButton;
	Button createButton, closeAllButton;
	Button modelessButton, primaryModalButton, applicationModalButton, systemModalButton;
	Button imageButton;
	Group parentStyleGroup, modalStyleGroup;

	/* Variables used to track the open shells */
	int shellCount = 0;
	Shell [] shells = new Shell [4];
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ShellTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Close all the example shells.
	 */
	void closeAllShells() {
		for (int i = 0; i<shellCount; i++) {
			if (shells [i] != null & !shells [i].isDisposed ()) {
				shells [i].dispose();
				shells [i] = null;
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
	
		/* Compute the shell style */
		int style = SWT.NONE;
		if (noTrimButton.getSelection()) style |= SWT.NO_TRIM;
		if (closeButton.getSelection()) style |= SWT.CLOSE;
		if (titleButton.getSelection()) style |= SWT.TITLE;
		if (minButton.getSelection()) style |= SWT.MIN;
		if (maxButton.getSelection()) style |= SWT.MAX;
		if (borderButton.getSelection()) style |= SWT.BORDER;
		if (resizeButton.getSelection()) style |= SWT.RESIZE;
		if (onTopButton.getSelection()) style |= SWT.ON_TOP;
		if (toolButton.getSelection()) style |= SWT.TOOL;
		if (sheetButton.getSelection()) style |= SWT.SHEET;
		if (modelessButton.getSelection()) style |= SWT.MODELESS;
		if (primaryModalButton.getSelection()) style |= SWT.PRIMARY_MODAL;
		if (applicationModalButton.getSelection()) style |= SWT.APPLICATION_MODAL;
		if (systemModalButton.getSelection()) style |= SWT.SYSTEM_MODAL;
	
		/* Create the shell with or without a parent */
		if (noParentButton.getSelection ()) {
			shells [shellCount] = new Shell (style);
		} else {
			shells [shellCount] = new Shell (shell, style);
		}
		final Shell currentShell = shells [shellCount];
		final Button button = new Button(currentShell, SWT.CHECK);
		button.setBounds(20, 20, 120, 30);
		button.setText(ControlExample.getResourceString("FullScreen"));
		button.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				currentShell.setFullScreen(button.getSelection());
			}
		});
		Button close = new Button(currentShell, SWT.PUSH);
		close.setBounds(160, 20, 120, 30);
		close.setText(ControlExample.getResourceString("Close"));
		close.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				currentShell.dispose();
			}
		});
	
		/* Set the size, title, and image, and open the shell */
		currentShell.setSize (300, 100);
		currentShell.setText (ControlExample.getResourceString("Title") + shellCount);
		if (imageButton.getSelection()) currentShell.setImage(instance.images[ControlExample.ciTarget]);
		if (backgroundImageButton.getSelection()) currentShell.setBackgroundImage(instance.images[ControlExample.ciBackground]);
		hookListeners (currentShell);
		currentShell.open ();
		shellCount++;
	}
	
	/**
	 * Creates the "Control" group. 
	 */
	void createControlGroup () {
		/*
		 * Create the "Control" group.  This is the group on the
		 * right half of each example tab.  It consists of the
		 * style group, the 'other' group and the size group.
		 */		
		controlGroup = new Group (tabFolderPage, SWT.NONE);
		controlGroup.setLayout (new GridLayout (2, true));
		controlGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		controlGroup.setText (ControlExample.getResourceString("Parameters"));
	
		/* Create a group for the decoration style controls */
		styleGroup = new Group (controlGroup, SWT.NONE);
		styleGroup.setLayout (new GridLayout ());
		styleGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false, 1, 3));
		styleGroup.setText (ControlExample.getResourceString("Decoration_Styles"));
	
		/* Create a group for the modal style controls */
		modalStyleGroup = new Group (controlGroup, SWT.NONE);
		modalStyleGroup.setLayout (new GridLayout ());
		modalStyleGroup.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		modalStyleGroup.setText (ControlExample.getResourceString("Modal_Styles"));		

		/* Create a group for the 'other' controls */
		otherGroup = new Group (controlGroup, SWT.NONE);
		otherGroup.setLayout (new GridLayout ());
		otherGroup.setLayoutData (new GridData(SWT.FILL, SWT.FILL, false, false));
		otherGroup.setText (ControlExample.getResourceString("Other"));

		/* Create a group for the parent style controls */
		parentStyleGroup = new Group (controlGroup, SWT.NONE);
		parentStyleGroup.setLayout (new GridLayout ());
		GridData gridData = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		parentStyleGroup.setLayoutData (gridData);
		parentStyleGroup.setText (ControlExample.getResourceString("Parent"));
	}
	
	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets () {
	
		/* Create the parent style buttons */
		noParentButton = new Button (parentStyleGroup, SWT.RADIO);
		noParentButton.setText (ControlExample.getResourceString("No_Parent"));
		parentButton = new Button (parentStyleGroup, SWT.RADIO);
		parentButton.setText (ControlExample.getResourceString("Parent"));
	
		/* Create the decoration style buttons */
		noTrimButton = new Button (styleGroup, SWT.CHECK);
		noTrimButton.setText ("SWT.NO_TRIM");
		closeButton = new Button (styleGroup, SWT.CHECK);
		closeButton.setText ("SWT.CLOSE");
		titleButton = new Button (styleGroup, SWT.CHECK);
		titleButton.setText ("SWT.TITLE");
		minButton = new Button (styleGroup, SWT.CHECK);
		minButton.setText ("SWT.MIN");
		maxButton = new Button (styleGroup, SWT.CHECK);
		maxButton.setText ("SWT.MAX");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
		resizeButton = new Button (styleGroup, SWT.CHECK);
		resizeButton.setText ("SWT.RESIZE");
		onTopButton = new Button (styleGroup, SWT.CHECK);
		onTopButton.setText ("SWT.ON_TOP");
		toolButton = new Button (styleGroup, SWT.CHECK);
		toolButton.setText ("SWT.TOOL");
		sheetButton = new Button (styleGroup, SWT.CHECK);
		sheetButton.setText ("SWT.SHEET");
		Label separator = new Label(styleGroup, SWT.SEPARATOR | SWT.HORIZONTAL);
		separator.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, true, false));
		shellTrimButton = new Button (styleGroup, SWT.CHECK);
		shellTrimButton.setText ("SWT.SHELL_TRIM");
		dialogTrimButton = new Button (styleGroup, SWT.CHECK);
		dialogTrimButton.setText ("SWT.DIALOG_TRIM");
	
		/* Create the modal style buttons */
		modelessButton = new Button (modalStyleGroup, SWT.RADIO);
		modelessButton.setText ("SWT.MODELESS");
		primaryModalButton = new Button (modalStyleGroup, SWT.RADIO);
		primaryModalButton.setText ("SWT.PRIMARY_MODAL");
		applicationModalButton = new Button (modalStyleGroup, SWT.RADIO);
		applicationModalButton.setText ("SWT.APPLICATION_MODAL");
		systemModalButton = new Button (modalStyleGroup, SWT.RADIO);
		systemModalButton.setText ("SWT.SYSTEM_MODAL");
	
		/* Create the 'other' buttons */
		imageButton = new Button (otherGroup, SWT.CHECK);
		imageButton.setText (ControlExample.getResourceString("Image"));
		backgroundImageButton = new Button(otherGroup, SWT.CHECK);
		backgroundImageButton.setText(ControlExample.getResourceString("BackgroundImage"));
	
		/* Create the "create" and "closeAll" buttons */
		createButton = new Button (controlGroup, SWT.NONE);
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_END);
		createButton.setLayoutData (gridData);
		createButton.setText (ControlExample.getResourceString("Create_Shell"));
		closeAllButton = new Button (controlGroup, SWT.NONE);
		gridData = new GridData (GridData.HORIZONTAL_ALIGN_BEGINNING);
		closeAllButton.setText (ControlExample.getResourceString("Close_All_Shells"));
		closeAllButton.setLayoutData (gridData);
	
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
		SelectionListener decorationButtonListener = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				decorationButtonSelected(event);
			}
		};
		noTrimButton.addSelectionListener (decorationButtonListener);
		closeButton.addSelectionListener (decorationButtonListener);
		titleButton.addSelectionListener (decorationButtonListener);
		minButton.addSelectionListener (decorationButtonListener);
		maxButton.addSelectionListener (decorationButtonListener);
		borderButton.addSelectionListener (decorationButtonListener);
		resizeButton.addSelectionListener (decorationButtonListener);
		dialogTrimButton.addSelectionListener (decorationButtonListener);
		shellTrimButton.addSelectionListener (decorationButtonListener);
		applicationModalButton.addSelectionListener (decorationButtonListener);
		systemModalButton.addSelectionListener (decorationButtonListener);
	
		/* Set the default state */
		noParentButton.setSelection (true);
		modelessButton.setSelection (true);
	}
	
	/**
	 * Handle a decoration button selection event.
	 *
	 * @param event org.eclipse.swt.events.SelectionEvent
	 */
	public void decorationButtonSelected(SelectionEvent event) {
		Button widget = (Button) event.widget;
		
		/*
		 * Make sure that if the modal style is SWT.APPLICATION_MODAL 
		 * or SWT.SYSTEM_MODAL the style SWT.CLOSE is also selected.
		 * This is to make sure the user can close the shell.
		 */
		if (widget == applicationModalButton || widget == systemModalButton) {
			if (widget.getSelection()) {
				closeButton.setSelection (true);
				noTrimButton.setSelection (false);
			} 
			return;
		}
		if (widget == closeButton) {
			if (applicationModalButton.getSelection() || systemModalButton.getSelection()) {
				closeButton.setSelection (true);
			}
		}	
		/*
		 * Make sure that if the SWT.NO_TRIM button is selected
		 * then all other decoration buttons are deselected.
		 */
		if (widget.getSelection()) {
			if (widget == noTrimButton) {
				if (applicationModalButton.getSelection() || systemModalButton.getSelection()) {
					noTrimButton.setSelection (false);
					return;
				}
				closeButton.setSelection (false);
				titleButton.setSelection (false);
				minButton.setSelection (false);
				maxButton.setSelection (false);
				borderButton.setSelection (false);
				resizeButton.setSelection (false);
			} else {
				noTrimButton.setSelection (false);
			}
		}
		
		/*
		 * Make sure that the SWT.DIALOG_TRIM and SWT.SHELL_TRIM buttons
		 * are consistent.
		 */
		if (widget == dialogTrimButton || widget == shellTrimButton) {
			if (widget.getSelection() && widget == dialogTrimButton) {
				shellTrimButton.setSelection(false);
			} else {
				dialogTrimButton.setSelection(false);
			}
			//SHELL_TRIM = CLOSE | TITLE | MIN | MAX | RESIZE;
			//DIALOG_TRIM = TITLE | CLOSE | BORDER;
			closeButton.setSelection (widget.getSelection ());
			titleButton.setSelection (widget.getSelection ());
			minButton.setSelection (widget == shellTrimButton && widget.getSelection( ));
			maxButton.setSelection (widget == shellTrimButton && widget.getSelection ());
			borderButton.setSelection (widget == dialogTrimButton && widget.getSelection ());
			resizeButton.setSelection (widget == shellTrimButton && widget.getSelection ());
		} else {
			boolean title = titleButton.getSelection ();
			boolean close = closeButton.getSelection ();
			boolean min = minButton.getSelection ();
			boolean max = maxButton.getSelection ();
			boolean border = borderButton.getSelection ();
			boolean resize = resizeButton.getSelection ();
			dialogTrimButton.setSelection(title && close && border && !min && !max && !resize);
			shellTrimButton.setSelection(title && close && min && max && resize && !border);
		}
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Shell";
	}
}
