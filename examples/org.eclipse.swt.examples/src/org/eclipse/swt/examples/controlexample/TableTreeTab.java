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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class TableTreeTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	TableTree tree1;
	TableTreeItem node1;
	Group treeGroup, itemGroup;
	
	/* Style widgets added to the "Style" group */
	Button checkButton, fullSelectionButton;
	
	/* Controls and resources added to the "Colors" group */
	Button itemForegroundButton, itemBackgroundButton, itemFontButton;
	Color itemForegroundColor, itemBackgroundColor;
	Image itemForegroundImage, itemBackgroundImage;
	Font itemFont;
	
	/* Other widgets added to the "Other" group */
	Button headerVisibleButton, linesVisibleButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TableTreeTab(ControlExample instance) {
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
				if (oldColor == null) oldColor = node1.getForeground ();
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
				if (oldColor == null) oldColor = node1.getBackground ();
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
				if (oldFont == null) oldFont = node1.getFont ();
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
	 * Creates the "Other" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
	
		/* Create display controls specific to this example */
		headerVisibleButton = new Button (otherGroup, SWT.CHECK);
		headerVisibleButton.setText (ControlExample.getResourceString("Header_Visible"));
		linesVisibleButton = new Button (otherGroup, SWT.CHECK);
		linesVisibleButton.setText (ControlExample.getResourceString("Lines_Visible"));
	
		/* Add the listeners */
		headerVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetHeaderVisible();
			}
		});
		linesVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetLinesVisible();
			};
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
		treeGroup.setText ("TableTree");
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
		if (fullSelectionButton.getSelection()) style |= SWT.FULL_SELECTION;
	
		/* Create the text tree */
		tree1 = new TableTree (treeGroup, style);
		Table table = tree1.getTable();
		for (int i = 0; i < 3; i++) {
			TableColumn column = new TableColumn (table,SWT.NONE);
			column.setWidth(100);
			column.setText(ControlExample.getResourceString("TableTree_column") + ": " + i);
		}
		node1 = new TableTreeItem (tree1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node1.setText (i, ControlExample.getResourceString("Node_1") + "-" + i);
		}
		node1.setImage (instance.images[ControlExample.ciOpenFolder]);
		TableTreeItem node2 = new TableTreeItem (tree1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node2.setText (i, ControlExample.getResourceString("Node_2") + "-" + i);
		}
		node2.setImage (instance.images[ControlExample.ciClosedFolder]);
		TableTreeItem node3 = new TableTreeItem (tree1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node3.setText (i, ControlExample.getResourceString("Node_3") + "-" + i);
		}
		node3.setImage (instance.images[ControlExample.ciTarget]);
		TableTreeItem node4 = new TableTreeItem (tree1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node4.setText (i, ControlExample.getResourceString("Node_4") + "-" + i);
		}
		node4.setImage (instance.images[ControlExample.ciOpenFolder]);
		TableTreeItem node1_1 = new TableTreeItem (node1, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node1_1.setText (i, ControlExample.getResourceString("Node_1_1") + "-" + i);
		}
		node1_1.setImage (instance.images[ControlExample.ciClosedFolder]);
		TableTreeItem node2_1 = new TableTreeItem (node2, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node2_1.setText (i, ControlExample.getResourceString("Node_2_1") + "-" + i);
		}
		node2_1.setImage (instance.images[ControlExample.ciTarget]);
		TableTreeItem node3_1 = new TableTreeItem (node3, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node3_1.setText (i, ControlExample.getResourceString("Node_3_1") + "-" + i);
		}
		node3_1.setImage (instance.images[ControlExample.ciOpenFolder]);
		TableTreeItem node2_2 = new TableTreeItem (node2, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node2_2.setText (i, ControlExample.getResourceString("Node_2_2") + "-" + i);
		}
		node2_2.setImage (instance.images[ControlExample.ciClosedFolder]);
		TableTreeItem node2_2_1 = new TableTreeItem (node2_2, SWT.NONE);
		for (int i = 0; i < 3; i++) {
			node2_2_1.setText (i, ControlExample.getResourceString("Node_2_2_1") + "-" + i);
		}
		node2_2_1.setImage (instance.images[ControlExample.ciTarget]);
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
		
		/* Create the extra widgets */
		checkButton = new Button (styleGroup, SWT.CHECK);
		checkButton.setText ("SWT.CHECK");
		fullSelectionButton = new Button (styleGroup, SWT.CHECK);
		fullSelectionButton.setText ("SWT.FULL_SELECTION");
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
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		return tree1.getItems();
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {tree1};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "TableTree";
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
		setWidgetHeaderVisible ();
		setWidgetLinesVisible ();
	}
	
	/**
	 * Sets the background color of the Node 1 TreeItems.
	 */
	void setItemBackground () {
		node1.setBackground (itemBackgroundColor);
		/* Set the background button's color to match the color just set. */
		Color color = itemBackgroundColor;
		if (color == null) color = node1.getBackground ();
		drawImage (itemBackgroundImage, color);
		itemBackgroundButton.setImage (itemBackgroundImage);
	}
	
	/**
	 * Sets the foreground color of the Node 1 TreeItems.
	 */
	void setItemForeground () {
		node1.setForeground (itemForegroundColor);
		/* Set the foreground button's color to match the color just set. */
		Color color = itemForegroundColor;
		if (color == null) color = node1.getForeground ();
		drawImage (itemForegroundImage, color);
		itemForegroundButton.setImage (itemForegroundImage);
	}
	
	/**
	 * Sets the font of the Node 1 TreeItems.
	 */
	void setItemFont () {
		node1.setFont (itemFont);
	}

	/**
	 * Sets the header visible state of the "Example" widgets.
	 */
	void setWidgetHeaderVisible () {
		tree1.getTable().setHeaderVisible (headerVisibleButton.getSelection ());
	}
	
	/**
	 * Sets the lines visible state of the "Example" widgets.
	 */
	void setWidgetLinesVisible () {
		tree1.getTable().setLinesVisible (linesVisibleButton.getSelection ());
	}
}
