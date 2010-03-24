/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.events.*;

class TreeTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	Tree tree1, tree2;
	TreeItem textNode1, imageNode1;
	Group treeGroup, imageTreeGroup, itemGroup;
	
	/* Size widgets added to the "Size" group */
	Button packColumnsButton;
	
	/* Style widgets added to the "Style" group */
	Button noScrollButton, checkButton, fullSelectionButton;

	/* Other widgets added to the "Other" group */
	Button multipleColumns, moveableColumns, resizableColumns, headerVisibleButton, sortIndicatorButton, headerImagesButton, subImagesButton, linesVisibleButton;
	
	/* Controls and resources added to the "Colors and Fonts" group */
	static final int ITEM_FOREGROUND_COLOR = 3;
	static final int ITEM_BACKGROUND_COLOR = 4;
	static final int ITEM_FONT = 5;
	static final int CELL_FOREGROUND_COLOR = 6;
	static final int CELL_BACKGROUND_COLOR = 7;
	static final int CELL_FONT = 8;
	Color itemForegroundColor, itemBackgroundColor, cellForegroundColor, cellBackgroundColor;
	Font itemFont, cellFont;

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

	Point menuMouseCoords;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	TreeTab(ControlExample instance) {
		super(instance);
	}

	/**
	 * Creates the "Colors and Fonts" group.
	 */
	void createColorAndFontGroup () {
		super.createColorAndFontGroup();
		
		TableItem item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Item_Foreground_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Item_Background_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Item_Font"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Cell_Foreground_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Cell_Background_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Cell_Font"));

		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (itemBackgroundColor != null) itemBackgroundColor.dispose();
				if (itemForegroundColor != null) itemForegroundColor.dispose();
				if (itemFont != null) itemFont.dispose();
				if (cellBackgroundColor != null) cellBackgroundColor.dispose();
				if (cellForegroundColor != null) cellForegroundColor.dispose();
				if (cellFont != null) cellFont.dispose();
				itemBackgroundColor = null;
				itemForegroundColor = null;			
				itemFont = null;
				cellBackgroundColor = null;
				cellForegroundColor = null;			
				cellFont = null;
			}
		});
	}

	void changeFontOrColor(int index) {
		switch (index) {
		case ITEM_FOREGROUND_COLOR: {
			Color oldColor = itemForegroundColor;
			if (oldColor == null) oldColor = textNode1.getForeground ();
			colorDialog.setRGB(oldColor.getRGB());
			RGB rgb = colorDialog.open();
			if (rgb == null) return;
			oldColor = itemForegroundColor;
			itemForegroundColor = new Color (display, rgb);
			setItemForeground ();
			if (oldColor != null) oldColor.dispose ();
		}
		break;
		case ITEM_BACKGROUND_COLOR: {
			Color oldColor = itemBackgroundColor;
			if (oldColor == null) oldColor = textNode1.getBackground ();
			colorDialog.setRGB(oldColor.getRGB());
			RGB rgb = colorDialog.open();
			if (rgb == null) return;
			oldColor = itemBackgroundColor;
			itemBackgroundColor = new Color (display, rgb);
			setItemBackground ();
			if (oldColor != null) oldColor.dispose ();
		}
		break;
		case ITEM_FONT: {
			Font oldFont = itemFont;
			if (oldFont == null) oldFont = textNode1.getFont ();
			fontDialog.setFontList(oldFont.getFontData());
			FontData fontData = fontDialog.open ();
			if (fontData == null) return;
			oldFont = itemFont;
			itemFont = new Font (display, fontData);
			setItemFont ();
			setExampleWidgetSize ();
			if (oldFont != null) oldFont.dispose ();
		}
		break;
		case CELL_FOREGROUND_COLOR: {
			Color oldColor = cellForegroundColor;
			if (oldColor == null) oldColor = textNode1.getForeground (1);
			colorDialog.setRGB(oldColor.getRGB());
			RGB rgb = colorDialog.open();
			if (rgb == null) return;
			oldColor = cellForegroundColor;
			cellForegroundColor = new Color (display, rgb);
			setCellForeground ();
			if (oldColor != null) oldColor.dispose ();
		}
		break;
		case CELL_BACKGROUND_COLOR: {
			Color oldColor = cellBackgroundColor;
			if (oldColor == null) oldColor = textNode1.getBackground (1);
			colorDialog.setRGB(oldColor.getRGB());
			RGB rgb = colorDialog.open();
			if (rgb == null) return;
			oldColor = cellBackgroundColor;
			cellBackgroundColor = new Color (display, rgb);
			setCellBackground ();
			if (oldColor != null) oldColor.dispose ();
		}
		break;
		case CELL_FONT: {
			Font oldFont = cellFont;
			if (oldFont == null) oldFont = textNode1.getFont (1);
			fontDialog.setFontList(oldFont.getFontData());
			FontData fontData = fontDialog.open ();
			if (fontData == null) return;
			oldFont = cellFont;
			cellFont = new Font (display, fontData);
			setCellFont ();
			setExampleWidgetSize ();
			if (oldFont != null) oldFont.dispose ();
		}
		break;
		default:
			super.changeFontOrColor(index);
		}
	}

	/**
	 * Creates the "Other" group.
	 */
	void createOtherGroup () {
		super.createOtherGroup ();
	
		/* Create display controls specific to this example */
		linesVisibleButton = new Button (otherGroup, SWT.CHECK);
		linesVisibleButton.setText (ControlExample.getResourceString("Lines_Visible"));
		multipleColumns = new Button (otherGroup, SWT.CHECK);
		multipleColumns.setText (ControlExample.getResourceString("Multiple_Columns"));
		headerVisibleButton = new Button (otherGroup, SWT.CHECK);
		headerVisibleButton.setText (ControlExample.getResourceString("Header_Visible"));
		sortIndicatorButton = new Button (otherGroup, SWT.CHECK);
		sortIndicatorButton.setText (ControlExample.getResourceString("Sort_Indicator"));
		moveableColumns = new Button (otherGroup, SWT.CHECK);
		moveableColumns.setText (ControlExample.getResourceString("Moveable_Columns"));
		resizableColumns = new Button (otherGroup, SWT.CHECK);
		resizableColumns.setText (ControlExample.getResourceString("Resizable_Columns"));
		headerImagesButton = new Button (otherGroup, SWT.CHECK);
		headerImagesButton.setText (ControlExample.getResourceString("Header_Images"));
		subImagesButton = new Button (otherGroup, SWT.CHECK);
		subImagesButton.setText (ControlExample.getResourceString("Sub_Images"));
	
		/* Add the listeners */
		linesVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetLinesVisible ();
			}
		});
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
		sortIndicatorButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetSortIndicator ();
			}
		});
		moveableColumns.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setColumnsMoveable ();
			}
		});
		resizableColumns.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setColumnsResizable ();
			}
		});
		headerImagesButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				recreateExampleWidgets ();
			}
		});
		subImagesButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				recreateExampleWidgets ();
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
		treeGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		treeGroup.setText ("Tree");
	
		/* Create a group for the image tree */
		imageTreeGroup = new Group (exampleGroup, SWT.NONE);
		imageTreeGroup.setLayout (new GridLayout ());
		imageTreeGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
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
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (noScrollButton.getSelection()) style |= SWT.NO_SCROLL;
		if (checkButton.getSelection()) style |= SWT.CHECK;
		if (fullSelectionButton.getSelection ()) style |= SWT.FULL_SELECTION;
		if (borderButton.getSelection()) style |= SWT.BORDER;
	
		/* Create the text tree */
		tree1 = new Tree (treeGroup, style);
		boolean multiColumn = multipleColumns.getSelection();
		if (multiColumn) {
			for (int i = 0; i < columnTitles.length; i++) {
				TreeColumn treeColumn = new TreeColumn(tree1, SWT.NONE);
				treeColumn.setText(columnTitles[i]);
				treeColumn.setToolTipText(ControlExample.getResourceString("Tooltip", new String [] {columnTitles[i]}));
			}
			tree1.setSortColumn(tree1.getColumn(0));
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
		try {
			TreeColumn column = tree1.getColumn(0);
			resizableColumns.setSelection (column.getResizable());
		} catch (IllegalArgumentException ex) {}

		/* Create the image tree */	
		tree2 = new Tree (imageTreeGroup, style);
		Image image = instance.images[ControlExample.ciClosedFolder];
		if (multiColumn) {
			for (int i = 0; i < columnTitles.length; i++) {
				TreeColumn treeColumn = new TreeColumn(tree2, SWT.NONE);
				treeColumn.setText(columnTitles[i]);
				treeColumn.setToolTipText(ControlExample.getResourceString("Tooltip", new String [] {columnTitles[i]}));
				if (headerImagesButton.getSelection()) treeColumn.setImage(instance.images [i % 3]);
			}
		}
		for (int i = 0; i < 4; i++) {
			item = new TreeItem (tree2, SWT.NONE);
			setItemText(item, i, ControlExample.getResourceString("Node_" + (i + 1)));
			if (multiColumn && subImagesButton.getSelection()) {
				for (int j = 0; j < columnTitles.length; j++) {
					item.setImage(j, image);
				}
			} else {
				item.setImage(image);
			}
			if (i < 3) {
				TreeItem subitem = new TreeItem (item, SWT.NONE);
				setItemText(subitem, i, ControlExample.getResourceString("Node_" + (i + 1) + "_1"));
				if (multiColumn && subImagesButton.getSelection()) {
					for (int j = 0; j < columnTitles.length; j++) {
						subitem.setImage(j, image);
					}
				} else {
					subitem.setImage(image);
				}
			}
		}
		treeRoots = tree2.getItems ();
		item = new TreeItem (treeRoots[1], SWT.NONE);
		setItemText(item, 1, ControlExample.getResourceString("Node_2_2"));
		if (multiColumn && subImagesButton.getSelection()) {
			for (int j = 0; j < columnTitles.length; j++) {
				item.setImage(j, image);
			}
		} else {
			item.setImage(image);
		}
		item = new TreeItem (item, SWT.NONE);
		setItemText(item, 1, ControlExample.getResourceString("Node_2_2_1"));
		if (multiColumn && subImagesButton.getSelection()) {
			for (int j = 0; j < columnTitles.length; j++) {
				item.setImage(j, image);
			}
		} else {
			item.setImage(image);
		}
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
	 * Creates the "Size" group.  The "Size" group contains
	 * controls that allow the user to change the size of
	 * the example widgets.
	 */
	void createSizeGroup () {
		super.createSizeGroup();
	
		packColumnsButton = new Button (sizeGroup, SWT.PUSH);
		packColumnsButton.setText (ControlExample.getResourceString("Pack_Columns"));
		packColumnsButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				packColumns (tree1);
				packColumns (tree2);
				setExampleWidgetSize ();
			}
		});
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
		
		/* Create the extra widgets */
		noScrollButton = new Button (styleGroup, SWT.CHECK);
		noScrollButton.setText ("SWT.NO_SCROLL");
		noScrollButton.moveAbove(borderButton);
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
	Widget [] getExampleWidgets () {
		return new Widget [] {tree1, tree2};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"ColumnOrder", "Selection", "ToolTipText", "TopItem"};
	}

	Object[] parameterForType(String typeName, String value, Widget widget) {
		if (typeName.equals("org.eclipse.swt.widgets.TreeItem")) {
			TreeItem item = findItem(value, ((Tree) widget).getItems());
			if (item != null) return new Object[] {item};
		}
		if (typeName.equals("[Lorg.eclipse.swt.widgets.TreeItem;")) {
			String[] values = split(value, ',');
			TreeItem[] items = new TreeItem[values.length];
			for (int i = 0; i < values.length; i++) {
				TreeItem item = findItem(values[i], ((Tree) widget).getItems());
				if (item == null) break;
				items[i] = item;				
			}
			return new Object[] {items};
		}
		return super.parameterForType(typeName, value, widget);
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
	 * Sets the moveable columns state of the "Example" widgets.
	 */
	void setColumnsMoveable () {
		boolean selection = moveableColumns.getSelection();
		TreeColumn[] columns1 = tree1.getColumns();
		for (int i = 0; i < columns1.length; i++) {
			columns1[i].setMoveable(selection);
		}
		TreeColumn[] columns2 = tree2.getColumns();
		for (int i = 0; i < columns2.length; i++) {
			columns2[i].setMoveable(selection);
		}
	}

	/**
	 * Sets the resizable columns state of the "Example" widgets.
	 */
	void setColumnsResizable () {
		boolean selection = resizableColumns.getSelection();
		TreeColumn[] columns1 = tree1.getColumns();
		for (int i = 0; i < columns1.length; i++) {
			columns1[i].setResizable(selection);
		}
		TreeColumn[] columns2 = tree2.getColumns();
		for (int i = 0; i < columns2.length; i++) {
			columns2[i].setResizable(selection);
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
		if (oldFont != null) oldFont.dispose();
		oldColor = cellForegroundColor;
		cellForegroundColor = null;
		setCellForeground ();
		if (oldColor != null) oldColor.dispose();
		oldColor = cellBackgroundColor;
		cellBackgroundColor = null;
		setCellBackground ();
		if (oldColor != null) oldColor.dispose();
		oldFont = font;
		cellFont = null;
		setCellFont ();
		if (oldFont != null) oldFont.dispose();
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		setItemBackground ();
		setItemForeground ();
		setItemFont ();
		setCellBackground ();
		setCellForeground ();
		setCellFont ();
		if (!instance.startup) {
			setColumnsMoveable ();
			setColumnsResizable ();
			setWidgetHeaderVisible ();
			setWidgetSortIndicator ();
			setWidgetLinesVisible ();
		}
		super.setExampleWidgetState ();
		noScrollButton.setSelection ((tree1.getStyle () & SWT.NO_SCROLL) != 0);
		checkButton.setSelection ((tree1.getStyle () & SWT.CHECK) != 0);
		fullSelectionButton.setSelection ((tree1.getStyle () & SWT.FULL_SELECTION) != 0);
		try {
			TreeColumn column = tree1.getColumn(0);
			moveableColumns.setSelection (column.getMoveable());
			resizableColumns.setSelection (column.getResizable());
		} catch (IllegalArgumentException ex) {}
		headerVisibleButton.setSelection (tree1.getHeaderVisible());
		linesVisibleButton.setSelection (tree1.getLinesVisible());
	}
	
	/**
	 * Sets the background color of the Node 1 TreeItems in column 1.
	 */
	void setCellBackground () {
		if (!instance.startup) {
			textNode1.setBackground (1, cellBackgroundColor);
			imageNode1.setBackground (1, cellBackgroundColor);
		}
		/* Set the background color item's image to match the background color of the cell. */
		Color color = cellBackgroundColor;
		if (color == null) color = textNode1.getBackground (1);
		TableItem item = colorAndFontTable.getItem(CELL_BACKGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the foreground color of the Node 1 TreeItems in column 1.
	 */
	void setCellForeground () {
		if (!instance.startup) {
			textNode1.setForeground (1, cellForegroundColor);
			imageNode1.setForeground (1, cellForegroundColor);
		}
		/* Set the foreground color item's image to match the foreground color of the cell. */
		Color color = cellForegroundColor;
		if (color == null) color = textNode1.getForeground (1);
		TableItem item = colorAndFontTable.getItem(CELL_FOREGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the font of the Node 1 TreeItems in column 1.
	 */
	void setCellFont () {
		if (!instance.startup) {
			textNode1.setFont (1, cellFont);
			imageNode1.setFont (1, cellFont);
		}
		/* Set the font item's image to match the font of the item. */
		Font ft = cellFont;
		if (ft == null) ft = textNode1.getFont (1);
		TableItem item = colorAndFontTable.getItem(CELL_FONT);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (fontImage(ft));
		item.setFont(ft);
		colorAndFontTable.layout ();
	}

	/**
	 * Sets the background color of the Node 1 TreeItems.
	 */
	void setItemBackground () {
		if (!instance.startup) {
			textNode1.setBackground (itemBackgroundColor);
			imageNode1.setBackground (itemBackgroundColor);
		}
		/* Set the background button's color to match the background color of the item. */
		Color color = itemBackgroundColor;
		if (color == null) color = textNode1.getBackground ();
		TableItem item = colorAndFontTable.getItem(ITEM_BACKGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the foreground color of the Node 1 TreeItems.
	 */
	void setItemForeground () {
		if (!instance.startup) {
			textNode1.setForeground (itemForegroundColor);
			imageNode1.setForeground (itemForegroundColor);
		}
		/* Set the foreground button's color to match the foreground color of the item. */
		Color color = itemForegroundColor;
		if (color == null) color = textNode1.getForeground ();
		TableItem item = colorAndFontTable.getItem(ITEM_FOREGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the font of the Node 1 TreeItems.
	 */
	void setItemFont () {
		if (!instance.startup) {
			textNode1.setFont (itemFont);
			imageNode1.setFont (itemFont);
		}
		/* Set the font item's image to match the font of the item. */
		Font ft = itemFont;
		if (ft == null) ft = textNode1.getFont ();
		TableItem item = colorAndFontTable.getItem(ITEM_FONT);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (fontImage(ft));
		item.setFont(ft);
		colorAndFontTable.layout ();
	}

	/**
	 * Sets the header visible state of the "Example" widgets.
	 */
	void setWidgetHeaderVisible () {
		tree1.setHeaderVisible (headerVisibleButton.getSelection ());
		tree2.setHeaderVisible (headerVisibleButton.getSelection ());
	}
	
	/**
	 * Sets the sort indicator state of the "Example" widgets.
	 */
	void setWidgetSortIndicator () {
		if (sortIndicatorButton.getSelection ()) {
			initializeSortState (tree1);
			initializeSortState (tree2);
		} else {
			resetSortState (tree1);
			resetSortState (tree2);
		}
	}
	
	/**
	 * Sets the initial sort indicator state and adds a listener
	 * to cycle through sort states and columns.
	 */
	void initializeSortState (final Tree tree) {
		/* Reset to known state: 'down' on column 0. */
		tree.setSortDirection (SWT.DOWN);
		TreeColumn [] columns = tree.getColumns();
		for (int i = 0; i < columns.length; i++) {
			TreeColumn column = columns[i];
			if (i == 0) tree.setSortColumn(column);
			SelectionListener listener = new SelectionAdapter() {
				public void widgetSelected(SelectionEvent e) {
					int sortDirection = SWT.DOWN;
					if (e.widget == tree.getSortColumn()) {
						/* If the sort column hasn't changed, cycle down -> up -> none. */
						switch (tree.getSortDirection ()) {
						case SWT.DOWN: sortDirection = SWT.UP; break;
						case SWT.UP: sortDirection = SWT.NONE; break;
						}
					} else {
						tree.setSortColumn((TreeColumn)e.widget);
					}
					tree.setSortDirection (sortDirection);
				}
			};
			column.addSelectionListener(listener);
			column.setData("SortListener", listener);	//$NON-NLS-1$
		}
	}

	void resetSortState (final Tree tree) {
		tree.setSortDirection (SWT.NONE);
		TreeColumn [] columns = tree.getColumns();
		for (int i = 0; i < columns.length; i++) {
			SelectionListener listener = (SelectionListener)columns[i].getData("SortListener");	//$NON-NLS-1$
			if (listener != null) columns[i].removeSelectionListener(listener);
		}
	}
	
	/**
	 * Sets the lines visible state of the "Example" widgets.
	 */
	void setWidgetLinesVisible () {
		tree1.setLinesVisible (linesVisibleButton.getSelection ());
		tree2.setLinesVisible (linesVisibleButton.getSelection ());
	}

	protected void specialPopupMenuItems(Menu menu, Event event) {
    	MenuItem item = new MenuItem(menu, SWT.PUSH);
    	item.setText("getItem(Point) on mouse coordinates");
    	final Tree t = (Tree) event.widget;
    	menuMouseCoords = t.toControl(new Point(event.x, event.y));
    	item.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent e) {
    			eventConsole.append ("getItem(Point(" + menuMouseCoords + ")) returned: " + t.getItem(menuMouseCoords));
    			eventConsole.append ("\n");
    		}
    	});
	}
}
