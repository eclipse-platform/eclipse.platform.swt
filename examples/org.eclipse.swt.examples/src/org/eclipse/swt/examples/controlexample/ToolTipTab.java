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
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.*;

class ToolTipTab extends Tab {

	/* Example widgets and groups that contain them */
	ToolTip toolTip1;
	Group toolTipGroup;
	
	/* Style widgets added to the "Style" group */
	Button balloonButton, iconErrorButton, iconInformationButton, iconWarningButton, noIconButton;
	
	/* Other widgets added to the "Other" group */
	Button autoHideButton, showInTrayButton;
	
	Tray tray;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ToolTipTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the combo box */
		toolTipGroup = new Group (exampleGroup, SWT.NONE);
		toolTipGroup.setLayout (new GridLayout ());
		toolTipGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		toolTipGroup.setText ("ToolTip");
		Button button = new Button (toolTipGroup, SWT.PUSH);
		button.setText(ControlExample.getResourceString("Press_For_ToolTip"));
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				toolTip1.setVisible(true);
			}
		});
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (balloonButton.getSelection ()) style |= SWT.BALLOON;
		if (iconErrorButton.getSelection ()) style |= SWT.ICON_ERROR;
		if (iconInformationButton.getSelection ()) style |= SWT.ICON_INFORMATION;
		if (iconWarningButton.getSelection ()) style |= SWT.ICON_WARNING;
		
		/* Create the example widgets */
		toolTip1 = new ToolTip (shell, style);
		toolTip1.setText(ControlExample.getResourceString("ToolTip_Title"));
		toolTip1.setMessage(ControlExample.getResourceString("Example_string"));
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				setExampleWidgetSize ();
			}
		});
		
		return tabFolderPage;
	}

	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();
	
		/* Create the extra widgets */
		balloonButton = new Button (styleGroup, SWT.CHECK);
		balloonButton.setText ("SWT.BALLOON");
		iconErrorButton = new Button (styleGroup, SWT.RADIO);
		iconErrorButton.setText("SWT.ICON_ERROR");
		iconInformationButton = new Button (styleGroup, SWT.RADIO);
		iconInformationButton.setText("SWT.ICON_INFORMATION");
		iconWarningButton = new Button (styleGroup, SWT.RADIO);
		iconWarningButton.setText("SWT.ICON_WARNING");
		noIconButton = new Button (styleGroup, SWT.RADIO);
		noIconButton.setText(ControlExample.getResourceString("No_Icon"));
	}
	
	void createColorAndFontGroup () {
		// ToolTip does not need a color and font group.
	}
	
	void createOtherGroup () {
		/* Create the group */
		otherGroup = new Group (controlGroup, SWT.NONE);
		otherGroup.setLayout (new GridLayout ());
		otherGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		otherGroup.setText (ControlExample.getResourceString("Other"));
	
		/* Create the controls */
		visibleButton = new Button(otherGroup, SWT.CHECK);
		visibleButton.setText(ControlExample.getResourceString("Visible"));
		autoHideButton = new Button(otherGroup, SWT.CHECK);
		autoHideButton.setText(ControlExample.getResourceString("AutoHide"));
		showInTrayButton = new Button(otherGroup, SWT.CHECK);
		showInTrayButton.setText(ControlExample.getResourceString("ShowInTray"));
		tray = display.getSystemTray();
		showInTrayButton.setEnabled(tray != null);

		/* Add the listeners */
		visibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleWidgetVisibility ();
			}
		});
		autoHideButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleWidgetAutoHide ();
			}
		});
		showInTrayButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				showExampleWidgetInTray ();
			}
		});

		/* Set the default state */
		visibleButton.setSelection(false);
		autoHideButton.setSelection(true);
	}
	
	void createSizeGroup () {
		// ToolTip does not need a size group.
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	// Tab uses this for many things - widgets would only get set/get, listeners, and dispose.
	Widget[] getExampleWidgets () {
		return new Widget [] {toolTip1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Message", "Text"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "ToolTip";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		setExampleWidgetAutoHide ();
		balloonButton.setSelection ((toolTip1.getStyle () & SWT.BALLOON) != 0);
		iconErrorButton.setSelection ((toolTip1.getStyle () & SWT.ICON_ERROR) != 0);
		iconInformationButton.setSelection ((toolTip1.getStyle () & SWT.ICON_INFORMATION) != 0);
		iconWarningButton.setSelection ((toolTip1.getStyle () & SWT.ICON_WARNING) != 0);
		noIconButton.setSelection ((toolTip1.getStyle () & (SWT.ICON_ERROR | SWT.ICON_INFORMATION | SWT.ICON_WARNING)) == 0);
		autoHideButton.setSelection(toolTip1.getAutoHide());
	}

	/**
	 * Sets the visibility of the "Example" widgets.
	 */
	void setExampleWidgetVisibility () {
		toolTip1.setVisible (visibleButton.getSelection ());
	}
	
	/**
	 * Sets the autoHide state of the "Example" widgets.
	 */
	void setExampleWidgetAutoHide () {
		toolTip1.setAutoHide(autoHideButton.getSelection ());
	}
	
	void showExampleWidgetInTray () {
		if (autoHideButton.getSelection ()) {
			TrayItem item = new TrayItem(tray, SWT.NONE);
			item.setImage(instance.images[ControlExample.ciTarget]);
			item.setToolTip(toolTip1);
		} else {
			// TODO: how to turn off show in tray
		}
	}
}