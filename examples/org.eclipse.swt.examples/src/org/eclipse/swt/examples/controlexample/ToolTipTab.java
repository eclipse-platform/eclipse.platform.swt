/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.ToolTip;
import org.eclipse.swt.widgets.Tray;
import org.eclipse.swt.widgets.TrayItem;
import org.eclipse.swt.widgets.Widget;

class ToolTipTab extends Tab {

	/* Example widgets and groups that contain them */
	ToolTip toolTip1;
	Group toolTipGroup;

	/* Style widgets added to the "Style" group */
	Button balloonButton, iconErrorButton, iconInformationButton, iconWarningButton, noIconButton;

	/* Other widgets added to the "Other" group */
	Button autoHideButton, showInTrayButton;

	Tray tray;
	TrayItem trayItem;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	ToolTipTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
	void createExampleGroup () {
		super.createExampleGroup ();

		/* Create a group for the tooltip visibility check box */
		toolTipGroup = new Group (exampleGroup, SWT.NONE);
		toolTipGroup.setLayout (new GridLayout ());
		toolTipGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		toolTipGroup.setText ("ToolTip");
		visibleButton = new Button(toolTipGroup, SWT.CHECK);
		visibleButton.setText(ControlExample.getResourceString("Visible"));
		visibleButton.addSelectionListener (widgetSelectedAdapter(event -> setExampleWidgetVisibility ()));
	}

	/**
	 * Creates the "Example" widgets.
	 */
	@Override
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
	@Override
	Composite createTabFolderPage (TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(ControlListener.controlResizedAdapter(e -> setExampleWidgetSize ()));

		return tabFolderPage;
	}

	/**
	 * Creates the "Style" group.
	 */
	@Override
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

	@Override
	void createColorAndFontGroup () {
		// ToolTip does not need a color and font group.
	}

	@Override
	void createOtherGroup () {
		/* Create the group */
		otherGroup = new Group (controlGroup, SWT.NONE);
		otherGroup.setLayout (new GridLayout ());
		otherGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		otherGroup.setText (ControlExample.getResourceString("Other"));

		/* Create the controls */
		autoHideButton = new Button(otherGroup, SWT.CHECK);
		autoHideButton.setText(ControlExample.getResourceString("AutoHide"));
		showInTrayButton = new Button(otherGroup, SWT.CHECK);
		showInTrayButton.setText(ControlExample.getResourceString("Show_In_Tray"));
		tray = display.getSystemTray();
		showInTrayButton.setEnabled(tray != null);

		/* Add the listeners */
		autoHideButton.addSelectionListener (widgetSelectedAdapter(event -> setExampleWidgetAutoHide ()));
		showInTrayButton.addSelectionListener (widgetSelectedAdapter(event -> showExampleWidgetInTray ()));
		shell.addDisposeListener(event -> disposeTrayItem());

		/* Set the default state */
		autoHideButton.setSelection(true);
	}

	@Override
	void createSizeGroup () {
		// ToolTip does not need a size group.
	}

	@Override
	void createBackgroundModeGroup () {
		// ToolTip does not need a background mode group.
	}

	/**
	 * Disposes the "Example" widgets.
	 */
	@Override
	void disposeExampleWidgets () {
		disposeTrayItem();
		super.disposeExampleWidgets();
	}

	/**
	 * Gets the "Example" widget children.
	 */
	@Override
	// Tab uses this for many things - widgets would only get set/get, listeners, and dispose.
	Widget[] getExampleWidgets () {
		return new Widget [] {toolTip1};
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	@Override
	String[] getMethodNames() {
		return new String[] {"Message", "Text"};
	}

	/**
	 * Gets the short text for the tab folder item.
	 */
	@Override
	String getShortTabText() {
		return "TT";
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "ToolTip";
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	@Override
	void setExampleWidgetState () {
		showExampleWidgetInTray ();
		setExampleWidgetAutoHide ();
		super.setExampleWidgetState ();
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
	@Override
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
		if (showInTrayButton.getSelection ()) {
			createTrayItem();
			trayItem.setToolTip(toolTip1);
		} else {
			disposeTrayItem();
		}
	}

	void createTrayItem() {
		if (trayItem == null) {
			trayItem = new TrayItem(tray, SWT.NONE);
			trayItem.setImage(instance.images[ControlExample.ciTarget]);
		}
	}

	void disposeTrayItem() {
		if (trayItem != null) {
			trayItem.setToolTip(null);
			trayItem.dispose();
			trayItem = null;
		}
	}
}
