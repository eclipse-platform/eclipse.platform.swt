/*******************************************************************************
 * Copyright (c) 2000, 2014 IBM Corporation and others.
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


import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Widget;

class LinkTab extends Tab {
	/* Example widgets and groups that contain them */
	Link link1;
	Group linkGroup;

	/* Controls and resources added to the "Fonts" group */
	static final int LINK_FOREGROUND_COLOR = 3;
	Color linkForegroundColor;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	LinkTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
	void createExampleGroup () {
		super.createExampleGroup ();

		/* Create a group for the list */
		linkGroup = new Group (exampleGroup, SWT.NONE);
		linkGroup.setLayout (new GridLayout ());
		linkGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		linkGroup.setText ("Link");
	}

	/**
	 * Creates the "Example" widgets.
	 */
	@Override
	void createExampleWidgets () {

		/* Compute the widget style */
		int style = getDefaultStyle();
		if (borderButton.getSelection ()) style |= SWT.BORDER;

		/* Create the example widgets */
		link1 = new Link (linkGroup, style);
		link1.setText (ControlExample.getResourceString("LinkText"));
	}

	/**
	 * Creates the "Style" group.
	 */
	@Override
	void createStyleGroup() {
		super.createStyleGroup ();

		/* Create the extra widgets */
		borderButton = new Button(styleGroup, SWT.CHECK);
		borderButton.setText("SWT.BORDER");
	}

	@Override
	void createColorAndFontGroup () {
		super.createColorAndFontGroup();

		TableItem item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Link_Foreground_Color"));

		shell.addDisposeListener(event -> {
			if (linkForegroundColor != null) linkForegroundColor.dispose();
			linkForegroundColor = null;
		});
	}
	
	@Override
	void changeFontOrColor(int index) {
		switch (index) {
			case LINK_FOREGROUND_COLOR: {
				Color oldColor = linkForegroundColor;
				if (oldColor == null) oldColor = link1.getLinkForeground();
				colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = linkForegroundColor;
				linkForegroundColor = new Color (rgb);
				setLinkForeground ();
				if (oldColor != null) oldColor.dispose ();
			}
			break;
			default:
				super.changeFontOrColor(index);
		}
	}

	void setLinkForeground () {
		if (!instance.startup) {
			link1.setLinkForeground(linkForegroundColor);
		}
		Color color = linkForegroundColor;
		if (color == null) color = link1.getLinkForeground ();
		TableItem item = colorAndFontTable.getItem(LINK_FOREGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	@Override
	void resetColorsAndFonts () {
		super.resetColorsAndFonts ();
		Color oldColor = linkForegroundColor;
		linkForegroundColor = null;
		setLinkForeground ();
		if (oldColor != null) oldColor.dispose();
	}

	@Override
	void setExampleWidgetState () {
		super.setExampleWidgetState();
		setLinkForeground ();
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [] {link1};
	}

	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	@Override
	String[] getMethodNames() {
		return new String[] {"Text", "ToolTipText"};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "Link";
	}

}
