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
	Button checkButton, fullSelectionButton;

	/* Other widgets added to the "Other" group */
	Button multipleColumns, headerVisibleButton, linesVisibleButton;
	
	/* Controls and resources added to the "Colors" group */
	Button itemForegroundButton, itemBackgroundButton, itemFontButton;
	Color itemForegroundColor, itemBackgroundColor;
	Image itemForegroundImage, itemBackgroundImage;
	Font itemFont;
	
	static String [] columnTitles	= {ControlExample.getResourceString("TableTitle_0"),
		   ControlExample.getResourceString("TableTitle_1"),
		   ControlExample.getResourceString("TableTitle_2"),
		   ControlExample.getResourceString("TableTitle_3")};
		   
	static String[][] tableData = {
			{ ControlExample.getResourceString("TableLine0_0"),
					ControlExample.getResourceString("TableLine0_1"),
					ControlExample.getResourceString("TableLine0_2"),
					ControlExample.getResourceString("TableLine0_3") },
			{ ControlExample.getResourceString("TableLine1_0"),
					ControlExample.getResourceString("TableLine1_1"),
					ControlExample.getResourceString("TableLine1_2"),
					ControlExample.getResourceString("TableLine1_3") },
			{ ControlExample.getResourceString("TableLine2_0"),
					ControlExample.getResourceString("TableLine2_1"),
					ControlExample.getResourceString("TableLine2_2"),
					ControlExample.getResourceString("TableLine2_3") } };

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
	 * Creates the "Other" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
	
		/* Create display controls specific to this example */
		multipleColumns = new Button (otherGroup, SWT.CHECK);
		multipleColumns.setText (ControlExample.getResourceString("Multiple_Columns"));
		headerVisibleButton = new Button (otherGroup, SWT.CHECK);
		headerVisibleButton.setText (ControlExample.getResourceString("Header_Visible"));
		linesVisibleButton = new Button (otherGroup, SWT.CHECK);
		linesVisibleButton.setText (ControlExample.getResourceString("Lines_Visible"));
	
		/* Add the listeners */
		multipleColumns.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				recreateExampleWidgets ();
			}
		});
		headerVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetHeaderVisible ();
			}
		});
		linesVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetLinesVisible ();
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
		if (fullSelectionButton.getSelection ()) style |= SWT.FULL_SELECTION;
		if (borderButton.getSelection()) style |= SWT.BORDER;
	
		/* Create the text tree */
		tree1 = new Tree (treeGroup, style);
		if (multipleColumns.getSelection()) {
			for (int i = 0; i < columnTitles.length; i++) {
				TreeColumn treeColumn = new TreeColumn(tree1, SWT.NONE);
				treeColumn.setText(columnTitles[i]);
			}
		}
		for (int i = 0; i < 4; i++) {
			TreeItem item = new TreeItem (tree1, SWT.NONE);
			setItemText(item, i, ControlExample.getResourceString("Node_" + (i + 1)));
			if (i < 3) {
				TreeItem subitem = new TreeItem (item, SWT.NONE);
				setItemText(subitem, i, ControlExample.getResourceString("Node_" + (i + 1) + "_1"));
			}
		}
		TreeItem treeRoots[] = tree1.getItems ();
		TreeItem item = new TreeItem (treeRoots[1], SWT.NONE);
		setItemText(item, 1, ControlExample.getResourceString("Node_2_2"));
		item = new TreeItem (item, SWT.NONE);
		setItemText(item, 1, ControlExample.getResourceString("Node_2_2_1"));					
		textNode1 = treeRoots[0];
		packColumns(tree1);

		/* Create the image tree */	
		tree2 = new Tree (imageTreeGroup, style);
		Image image = instance.images[ControlExample.ciClosedFolder];
		if (multipleColumns.getSelection()) {
			for (int i = 0; i < columnTitles.length; i++) {
				TreeColumn treeColumn = new TreeColumn(tree2, SWT.NONE);
				treeColumn.setText(columnTitles[i]);
			}
		}
		for (int i = 0; i < 4; i++) {
			item = new TreeItem (tree2, SWT.NONE);
			setItemText(item, i, ControlExample.getResourceString("Node_" + (i + 1)));
			item.setImage(image);
			if (i < 3) {
				TreeItem subitem = new TreeItem (item, SWT.NONE);
				setItemText(subitem, i, ControlExample.getResourceString("Node_" + (i + 1) + "_1"));
				subitem.setImage(image);
			}
		}
		treeRoots = tree2.getItems ();
		item = new TreeItem (treeRoots[1], SWT.NONE);
		setItemText(item, 1, ControlExample.getResourceString("Node_2_2"));
		item.setImage(image);
		item = new TreeItem (item, SWT.NONE);
		setItemText(item, 1, ControlExample.getResourceString("Node_2_2_1"));
		item.setImage(image);
		imageNode1 = treeRoots[0];
		packColumns(tree2);
	}
	
	void setItemText(TreeItem item, int i, String node) {
		int index = i % 3;
		if (multipleColumns.getSelection()) {
			tableData [index][0] = node;
			item.setText (tableData [index]);
		} else {
			item.setText (node);
		}		
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
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		/* Note: We do not bother collecting the tree items
		 * because tree items don't have any events. If events
		 * are ever added to TreeItem, then this needs to change.
		 */
		Item [] columns1 = tree1.getColumns();
		Item [] columns2 = tree2.getColumns();
		Item [] allItems = new Item [columns1.length + columns2.length];
		System.arraycopy(columns1, 0, allItems, 0, columns1.length);
		System.arraycopy(columns2, 0, allItems, columns1.length, columns2.length);
		return allItems;
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {tree1, tree2};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Selection", "TopItem"};
	}

	Object[] parameterForType(String typeName, String value, Control control) {
		if (typeName.equals("org.eclipse.swt.widgets.TreeItem")) {
			TreeItem item = findItem(value, ((Tree) control).getItems());
			if (item != null) return new Object[] {item};
		}
		if (typeName.equals("[Lorg.eclipse.swt.widgets.TreeItem;")) {
			String[] values = value.split(",");
			TreeItem[] items = new TreeItem[values.length];
			for (int i = 0; i < values.length; i++) {
				TreeItem item = findItem(values[i], ((Tree) control).getItems());
				if (item == null) break;
				items[i] = item;				
			}
			return new Object[] {items};
		}
		return super.parameterForType(typeName, value, control);
	}

	TreeItem findItem(String value, TreeItem[] items) {
		for (int i = 0; i < items.length; i++) {
			TreeItem item = items[i];
			if (item.getText().equals(value)) return item;
			item = findItem(value, item.getItems());
			if (item != null) return item;
		}
		return null;
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Tree";
	}

	void packColumns (Tree tree) {
		if (multipleColumns.getSelection()) {
			int columnCount = tree.getColumnCount();
			for (int i = 0; i < columnCount; i++) {
				TreeColumn treeColumn = tree.getColumn(i);
				treeColumn.pack();
			}
		}
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
		checkButton.setSelection ((tree1.getStyle () & SWT.CHECK) != 0);
		checkButton.setSelection ((tree2.getStyle () & SWT.CHECK) != 0);
		fullSelectionButton.setSelection ((tree1.getStyle () & SWT.FULL_SELECTION) != 0);
		fullSelectionButton.setSelection ((tree2.getStyle () & SWT.FULL_SELECTION) != 0);
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

	/**
	 * Sets the header visible state of the "Example" widgets.
	 */
	void setWidgetHeaderVisible () {
		tree1.setHeaderVisible (headerVisibleButton.getSelection ());
		tree2.setHeaderVisible (headerVisibleButton.getSelection ());
	}
	
	/**
	 * Sets the lines visible state of the "Example" widgets.
	 */
	void setWidgetLinesVisible () {
		tree1.setLinesVisible (linesVisibleButton.getSelection ());
		tree2.setLinesVisible (linesVisibleButton.getSelection ());
	}
}
