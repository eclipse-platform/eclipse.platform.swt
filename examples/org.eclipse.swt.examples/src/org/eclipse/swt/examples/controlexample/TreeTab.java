/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

class TreeTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	Tree tree1, tree2;
	TreeItem textNode1, imageNode1;
	Group treeGroup, imageTreeGroup, itemGroup;
	
	/* Style widgets added to the "Style" group */
	Button checkButton;

	/* Controls and resources added to the "Colors" group */
	Button itemForegroundButton, itemBackgroundButton, itemFontButton;
	Color itemForegroundColor, itemBackgroundColor;
	Image itemForegroundImage, itemBackgroundImage;
	Font itemFont;
	
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TreeTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Colors" group.
	 */
	void createColorGroup () {
		super.createColorGroup();
		
		itemGroup = new Group (colorGroup, SWT.NONE);
		itemGroup.setText (ControlExample.getResourceString ("Tree_Item_Colors"));
		GridData data = new GridData ();
		data.horizontalSpan = 2;
		itemGroup.setLayoutData (data);
		itemGroup.setLayout (new GridLayout (2, false));
		new Label (itemGroup, SWT.NONE).setText (ControlExample.getResourceString ("Foreground_Color"));
		itemForegroundButton = new Button (itemGroup, SWT.PUSH);
		new Label (itemGroup, SWT.NONE).setText (ControlExample.getResourceString ("Background_Color"));
		itemBackgroundButton = new Button (itemGroup, SWT.PUSH);
		itemFontButton = new Button (itemGroup, SWT.PUSH);
		itemFontButton.setText(ControlExample.getResourceString("Font"));
		itemFontButton.setLayoutData(new GridData (GridData.HORIZONTAL_ALIGN_FILL));
		
		Shell shell = colorGroup.getShell ();
		final ColorDialog foregroundDialog = new ColorDialog (shell);
		final ColorDialog backgroundDialog = new ColorDialog (shell);
		final FontDialog fontDialog = new FontDialog (shell);

		int imageSize = 12;
		Display display = shell.getDisplay ();
		itemForegroundImage = new Image(display, imageSize, imageSize);
		itemBackgroundImage = new Image(display, imageSize, imageSize);

		/* Add listeners to set the colors and font */
		itemForegroundButton.setImage(itemForegroundImage);
		itemForegroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = itemForegroundColor;
				if (oldColor == null) oldColor = textNode1.getForeground ();
				foregroundDialog.setRGB(oldColor.getRGB());
				RGB rgb = foregroundDialog.open();
				if (rgb == null) return;
				oldColor = itemForegroundColor;
				itemForegroundColor = new Color (event.display, rgb);
				setItemForeground ();
				if (oldColor != null) oldColor.dispose ();
			}
		});
		itemBackgroundButton.setImage(itemBackgroundImage);
		itemBackgroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = itemBackgroundColor;
				if (oldColor == null) oldColor = textNode1.getBackground ();
				backgroundDialog.setRGB(oldColor.getRGB());
				RGB rgb = backgroundDialog.open();
				if (rgb == null) return;
				oldColor = itemBackgroundColor;
				itemBackgroundColor = new Color (event.display, rgb);
				setItemBackground ();
				if (oldColor != null) oldColor.dispose ();
			}
		});
		itemFontButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Font oldFont = itemFont;
				if (oldFont == null) oldFont = textNode1.getFont ();
				fontDialog.setFontList(oldFont.getFontData());
				FontData fontData = fontDialog.open ();
				if (fontData == null) return;
				oldFont = itemFont;
				itemFont = new Font (event.display, fontData);
				setItemFont ();
				setExampleWidgetSize ();
				if (oldFont != null) oldFont.dispose ();
			}
		});
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (itemBackgroundImage != null) itemBackgroundImage.dispose();
				if (itemForegroundImage != null) itemForegroundImage.dispose();
				if (itemBackgroundColor != null) itemBackgroundColor.dispose();
				if (itemForegroundColor != null) itemForegroundColor.dispose();
				if (itemFont != null) itemFont.dispose();
				itemBackgroundColor = null;
				itemForegroundColor = null;			
				itemFont = null;
			}
		});
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the text tree */
		treeGroup = new Group (exampleGroup, SWT.NONE);
		treeGroup.setLayout (new GridLayout ());
		treeGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		treeGroup.setText ("Tree");
	
		/* Create a group for the image tree */
		imageTreeGroup = new Group (exampleGroup, SWT.NONE);
		imageTreeGroup.setLayout (new GridLayout ());
		imageTreeGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		imageTreeGroup.setText (ControlExample.getResourceString("Tree_With_Images"));
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (singleButton.getSelection()) style |= SWT.SINGLE;
		if (multiButton.getSelection()) style |= SWT.MULTI;
		if (checkButton.getSelection()) style |= SWT.CHECK;
		if (borderButton.getSelection()) style |= SWT.BORDER;
	
		/* Create the text tree */
		tree1 = new Tree (treeGroup, style);
		textNode1 = new TreeItem (tree1, SWT.NONE);
		textNode1.setText (ControlExample.getResourceString("Node_1"));
		TreeItem node2 = new TreeItem (tree1, SWT.NONE);
		node2.setText (ControlExample.getResourceString("Node_2"));
		TreeItem node3 = new TreeItem (tree1, SWT.NONE);
		node3.setText (ControlExample.getResourceString("Node_3"));
		TreeItem node4 = new TreeItem (tree1, SWT.NONE);
		node4.setText (ControlExample.getResourceString("Node_4"));
		TreeItem node1_1 = new TreeItem (textNode1, SWT.NONE);
		node1_1.setText (ControlExample.getResourceString("Node_1_1"));
		TreeItem node2_1 = new TreeItem (node2, SWT.NONE);
		node2_1.setText (ControlExample.getResourceString("Node_2_1"));
		TreeItem node3_1 = new TreeItem (node3, SWT.NONE);
		node3_1.setText (ControlExample.getResourceString("Node_3_1"));
		TreeItem node2_2 = new TreeItem (node2, SWT.NONE);
		node2_2.setText (ControlExample.getResourceString("Node_2_2"));
		TreeItem node2_2_1 = new TreeItem (node2_2, SWT.NONE);
		node2_2_1.setText (ControlExample.getResourceString("Node_2_2_1"));
	
		/* Create the image tree */	
		tree2 = new Tree (imageTreeGroup, style);
		imageNode1 = new TreeItem (tree2, SWT.NONE);
		imageNode1.setText (ControlExample.getResourceString("Node_1"));
		imageNode1.setImage (instance.images[ControlExample.ciClosedFolder]);
		node2 = new TreeItem (tree2, SWT.NONE);
		node2.setText (ControlExample.getResourceString("Node_2"));
		node2.setImage (instance.images[ControlExample.ciClosedFolder]);
		node3 = new TreeItem (tree2, SWT.NONE);
		node3.setText (ControlExample.getResourceString("Node_3"));
		node3.setImage (instance.images[ControlExample.ciClosedFolder]);
		node4 = new TreeItem (tree2, SWT.NONE);
		node4.setText (ControlExample.getResourceString("Node_4"));
		node4.setImage (instance.images[ControlExample.ciClosedFolder]);
		node1_1 = new TreeItem (imageNode1, SWT.NONE);
		node1_1.setText (ControlExample.getResourceString("Node_1_1"));
		node1_1.setImage (instance.images[ControlExample.ciClosedFolder]);
		node2_1 = new TreeItem (node2, SWT.NONE);
		node2_1.setText (ControlExample.getResourceString("Node_2_1"));
		node2_1.setImage (instance.images[ControlExample.ciClosedFolder]);
		node3_1 = new TreeItem (node3, SWT.NONE);
		node3_1.setText (ControlExample.getResourceString("Node_3_1"));
		node3_1.setImage (instance.images[ControlExample.ciClosedFolder]);
		node2_2 = new TreeItem(node2, SWT.NONE);
		node2_2.setText (ControlExample.getResourceString("Node_2_2"));
		node2_2.setImage (instance.images[ControlExample.ciClosedFolder]);
		node2_2_1 = new TreeItem (node2_2, SWT.NONE);
		node2_2_1.setText (ControlExample.getResourceString("Node_2_2_1"));
		node2_2_1.setImage (instance.images[ControlExample.ciClosedFolder]);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
		
		/* Create the extra widgets */
		checkButton = new Button (styleGroup, SWT.CHECK);
		checkButton.setText ("SWT.CHECK");
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {tree1, tree2};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Tree";
	}

	/**
	 * Sets the foreground color, background color, and font
	 * of the "Example" widgets to their default settings.
	 * Also sets foreground and background color of the Node 1
	 * TreeItems to default settings.
	 */
	void resetColorsAndFonts () {
		super.resetColorsAndFonts ();
		Color oldColor = itemForegroundColor;
		itemForegroundColor = null;
		setItemForeground ();
		if (oldColor != null) oldColor.dispose();
		oldColor = itemBackgroundColor;
		itemBackgroundColor = null;
		setItemBackground ();
		if (oldColor != null) oldColor.dispose();
		Font oldFont = font;
		itemFont = null;
		setItemFont ();
		setExampleWidgetSize ();
		if (oldFont != null) oldFont.dispose();
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		setItemBackground ();
		setItemForeground ();
		setItemFont ();
		setExampleWidgetSize ();
		checkButton.setSelection ((tree1.getStyle () & SWT.CHECK) != 0);
	}
	
	/**
	 * Sets the background color of the Node 1 TreeItems.
	 */
	void setItemBackground () {
		textNode1.setBackground (itemBackgroundColor);
		imageNode1.setBackground (itemBackgroundColor);
		/* Set the background button's color to match the color just set. */
		Color color = itemBackgroundColor;
		if (color == null) color = textNode1.getBackground ();
		drawImage (itemBackgroundImage, color);
		itemBackgroundButton.setImage (itemBackgroundImage);
	}
	
	/**
	 * Sets the foreground color of the Node 1 TreeItems.
	 */
	void setItemForeground () {
		textNode1.setForeground (itemForegroundColor);
		imageNode1.setForeground (itemForegroundColor);
		/* Set the foreground button's color to match the color just set. */
		Color color = itemForegroundColor;
		if (color == null) color = textNode1.getForeground ();
		drawImage (itemForegroundImage, color);
		itemForegroundButton.setImage (itemForegroundImage);
	}
	
	/**
	 * Sets the font of the Node 1 TreeItems.
	 */
	void setItemFont () {
		if (instance.startup) return;
		textNode1.setFont (itemFont);
		imageNode1.setFont (itemFont);
	}
}
