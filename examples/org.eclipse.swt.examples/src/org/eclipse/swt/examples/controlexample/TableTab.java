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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class TableTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	Table table1;
	Group tableGroup;

	/* Size widgets added to the "Size" group */
	Button packColumnsButton;
	
	/* Style widgets added to the "Style" group */
	Button noScrollButton, checkButton, fullSelectionButton, hideSelectionButton;

	/* Other widgets added to the "Other" group */
	Button multipleColumns, moveableColumns, resizableColumns, headerVisibleButton, sortIndicatorButton, headerImagesButton, linesVisibleButton, subImagesButton;
	
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
	TableTab(ControlExample instance) {
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
			if (oldColor == null) oldColor = table1.getItem (0).getForeground ();
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
			if (oldColor == null) oldColor = table1.getItem (0).getBackground ();
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
			if (oldFont == null) oldFont = table1.getItem (0).getFont ();
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
			if (oldColor == null) oldColor = table1.getItem (0).getForeground (1);
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
			if (oldColor == null) oldColor = table1.getItem (0).getBackground (1);
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
			if (oldFont == null) oldFont = table1.getItem (0).getFont (1);
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
		multipleColumns.setSelection(true);
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
		
		/* Create a group for the table */
		tableGroup = new Group (exampleGroup, SWT.NONE);
		tableGroup.setLayout (new GridLayout ());
		tableGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		tableGroup.setText ("Table");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {	
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (singleButton.getSelection ()) style |= SWT.SINGLE;
		if (multiButton.getSelection ()) style |= SWT.MULTI;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (noScrollButton.getSelection ()) style |= SWT.NO_SCROLL;
		if (checkButton.getSelection ()) style |= SWT.CHECK;
		if (fullSelectionButton.getSelection ()) style |= SWT.FULL_SELECTION;
		if (hideSelectionButton.getSelection ()) style |= SWT.HIDE_SELECTION;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
	
		/* Create the table widget */
		table1 = new Table (tableGroup, style);
	
		/* Fill the table with data */
		boolean multiColumn = multipleColumns.getSelection();
		if (multiColumn) {
			for (int i = 0; i < columnTitles.length; i++) {
				TableColumn tableColumn = new TableColumn(table1, SWT.NONE);
				tableColumn.setText(columnTitles[i]);
				tableColumn.setToolTipText(ControlExample.getResourceString("Tooltip", new String [] {columnTitles[i]}));
				if (headerImagesButton.getSelection()) tableColumn.setImage(instance.images [i % 3]);
			}
			table1.setSortColumn(table1.getColumn(0));
		}
		for (int i=0; i<16; i++) {
			TableItem item = new TableItem (table1, SWT.NONE);
			if (multiColumn && subImagesButton.getSelection()) {
				for (int j = 0; j < columnTitles.length; j++) {
					item.setImage(j, instance.images [i % 3]);
				}
			} else {
				item.setImage(instance.images [i % 3]);
			}
			setItemText (item, i, ControlExample.getResourceString("Index") + i);
		}
		packColumns();
	}
	
	void setItemText(TableItem item, int i, String node) {
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
				packColumns ();
				setExampleWidgetSize ();
			}
		});
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		noScrollButton = new Button (styleGroup, SWT.CHECK);
		noScrollButton.setText ("SWT.NO_SCROLL");
		checkButton = new Button (styleGroup, SWT.CHECK);
		checkButton.setText ("SWT.CHECK");
		fullSelectionButton = new Button (styleGroup, SWT.CHECK);
		fullSelectionButton.setText ("SWT.FULL_SELECTION");
		hideSelectionButton = new Button (styleGroup, SWT.CHECK);
		hideSelectionButton.setText ("SWT.HIDE_SELECTION");
	}
	
	/**
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		Item [] columns = table1.getColumns();
		Item [] items = table1.getItems();
		Item [] allItems = new Item [columns.length + items.length];
		System.arraycopy(columns, 0, allItems, 0, columns.length);
		System.arraycopy(items, 0, allItems, columns.length, items.length);
		return allItems;
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {table1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"ColumnOrder", "ItemCount", "Selection", "SelectionIndex", "ToolTipText", "TopIndex"};
	}

	String setMethodName(String methodRoot) {
		/* Override to handle special case of int getSelectionIndex()/setSelection(int) */
		return (methodRoot.equals("SelectionIndex")) ? "setSelection" : "set" + methodRoot;
	}

	void packColumns () {
		int columnCount = table1.getColumnCount(); 
		for (int i = 0; i < columnCount; i++) {
			TableColumn tableColumn = table1.getColumn(i);
			tableColumn.pack();
		}
	}

	Object[] parameterForType(String typeName, String value, Widget widget) {
		if (value.equals("")) return new Object[] {new TableItem[0]}; // bug in Table?
		if (typeName.equals("org.eclipse.swt.widgets.TableItem")) {
			TableItem item = findItem(value, ((Table) widget).getItems());
			if (item != null) return new Object[] {item};
		}
		if (typeName.equals("[Lorg.eclipse.swt.widgets.TableItem;")) {
			String[] values = split(value, ',');
			TableItem[] items = new TableItem[values.length];
			for (int i = 0; i < values.length; i++) {
				items[i] = findItem(values[i], ((Table) widget).getItems());
			}
			return new Object[] {items};
		}
		return super.parameterForType(typeName, value, widget);
	}

	TableItem findItem(String value, TableItem[] items) {
		for (int i = 0; i < items.length; i++) {
			TableItem item = items[i];
			if (item.getText().equals(value)) return item;
		}
		return null;
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "Table";
	}
	
	/**
	 * Sets the foreground color, background color, and font
	 * of the "Example" widgets to their default settings.
	 * Also sets foreground and background color of TableItem [0]
	 * to default settings.
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
	 * Sets the background color of the Row 0 TableItem in column 1.
	 */
	void setCellBackground () {
		if (!instance.startup) {
			table1.getItem (0).setBackground (1, cellBackgroundColor);
		}
		/* Set the background color item's image to match the background color of the cell. */
		Color color = cellBackgroundColor;
		if (color == null) color = table1.getItem (0).getBackground (1);
		TableItem item = colorAndFontTable.getItem(CELL_BACKGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the foreground color of the Row 0 TableItem in column 1.
	 */
	void setCellForeground () {
		if (!instance.startup) {
			table1.getItem (0).setForeground (1, cellForegroundColor);
		}
		/* Set the foreground color item's image to match the foreground color of the cell. */
		Color color = cellForegroundColor;
		if (color == null) color = table1.getItem (0).getForeground (1);
		TableItem item = colorAndFontTable.getItem(CELL_FOREGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the font of the Row 0 TableItem in column 1.
	 */
	void setCellFont () {
		if (!instance.startup) {
			table1.getItem (0).setFont (1, cellFont);
		}
		/* Set the font item's image to match the font of the item. */
		Font ft = cellFont;
		if (ft == null) ft = table1.getItem (0).getFont (1);
		TableItem item = colorAndFontTable.getItem(CELL_FONT);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (fontImage(ft));
		item.setFont(ft);
		colorAndFontTable.layout ();
	}

	/**
	 * Sets the background color of TableItem [0].
	 */
	void setItemBackground () {
		if (!instance.startup) {
			table1.getItem (0).setBackground (itemBackgroundColor);
		}
		/* Set the background color item's image to match the background color of the item. */
		Color color = itemBackgroundColor;
		if (color == null) color = table1.getItem (0).getBackground ();
		TableItem item = colorAndFontTable.getItem(ITEM_BACKGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the foreground color of TableItem [0].
	 */
	void setItemForeground () {
		if (!instance.startup) {
			table1.getItem (0).setForeground (itemForegroundColor);
		}
		/* Set the foreground color item's image to match the foreground color of the item. */
		Color color = itemForegroundColor;
		if (color == null) color = table1.getItem (0).getForeground ();
		TableItem item = colorAndFontTable.getItem(ITEM_FOREGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the font of TableItem 0.
	 */
	void setItemFont () {
		if (!instance.startup) {
			table1.getItem (0).setFont (itemFont);
		}
		/* Set the font item's image to match the font of the item. */
		Font ft = itemFont;
		if (ft == null) ft = table1.getItem (0).getFont ();
		TableItem item = colorAndFontTable.getItem(ITEM_FONT);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (fontImage(ft));
		item.setFont(ft);
		colorAndFontTable.layout ();
	}

	/**
	 * Sets the moveable columns state of the "Example" widgets.
	 */
	void setColumnsMoveable () {
		boolean selection = moveableColumns.getSelection();
		TableColumn[] columns = table1.getColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].setMoveable(selection);
		}
	}

	/**
	 * Sets the resizable columns state of the "Example" widgets.
	 */
	void setColumnsResizable () {
		boolean selection = resizableColumns.getSelection();
		TableColumn[] columns = table1.getColumns();
		for (int i = 0; i < columns.length; i++) {
			columns[i].setResizable(selection);
		}
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
		noScrollButton.setSelection ((table1.getStyle () & SWT.NO_SCROLL) != 0);
		checkButton.setSelection ((table1.getStyle () & SWT.CHECK) != 0);
		fullSelectionButton.setSelection ((table1.getStyle () & SWT.FULL_SELECTION) != 0);
		hideSelectionButton.setSelection ((table1.getStyle () & SWT.HIDE_SELECTION) != 0);
		try {
			TableColumn column = table1.getColumn(0);
			moveableColumns.setSelection (column.getMoveable());
			resizableColumns.setSelection (column.getResizable());
		} catch (IllegalArgumentException ex) {}
		headerVisibleButton.setSelection (table1.getHeaderVisible());
		linesVisibleButton.setSelection (table1.getLinesVisible());
	}
	
	/**
	 * Sets the header visible state of the "Example" widgets.
	 */
	void setWidgetHeaderVisible () {
		table1.setHeaderVisible (headerVisibleButton.getSelection ());
	}
	
	/**
	 * Sets the sort indicator state of the "Example" widgets.
	 */
	void setWidgetSortIndicator () {
		TableColumn [] columns = table1.getColumns();
		if (sortIndicatorButton.getSelection ()) {
			/* Reset to known state: 'down' on column 0. */
			table1.setSortDirection (SWT.DOWN);
			for (int i = 0; i < columns.length; i++) {
				TableColumn column = columns[i];
				if (i == 0) table1.setSortColumn(column);
				SelectionListener listener = new SelectionAdapter() {
					public void widgetSelected(SelectionEvent e) {
						int sortDirection = SWT.DOWN;
						if (e.widget == table1.getSortColumn()) {
							/* If the sort column hasn't changed, cycle down -> up -> none. */
							switch (table1.getSortDirection ()) {
							case SWT.DOWN: sortDirection = SWT.UP; break;
							case SWT.UP: sortDirection = SWT.NONE; break;
							}
						} else {
							table1.setSortColumn((TableColumn)e.widget);
						}
						table1.setSortDirection (sortDirection);
					}
				};
				column.addSelectionListener(listener);
				column.setData("SortListener", listener);	//$NON-NLS-1$
			}
		} else {
			table1.setSortDirection (SWT.NONE);
			for (int j = 0; j < columns.length; j++) {
				SelectionListener listener = (SelectionListener)columns[j].getData("SortListener");	//$NON-NLS-1$
				if (listener != null) columns[j].removeSelectionListener(listener);
			}
		}
	}
	
	/**
	 * Sets the lines visible state of the "Example" widgets.
	 */
	void setWidgetLinesVisible () {
		table1.setLinesVisible (linesVisibleButton.getSelection ());
	}
	
	protected void specialPopupMenuItems(Menu menu, Event event) {
    	MenuItem item = new MenuItem(menu, SWT.PUSH);
    	item.setText("getItem(Point) on mouse coordinates");
    	menuMouseCoords = table1.toControl(new Point(event.x, event.y));
    	item.addSelectionListener(new SelectionAdapter() {
    		public void widgetSelected(SelectionEvent e) {
    			eventConsole.append ("getItem(Point(" + menuMouseCoords + ")) returned: " + table1.getItem(menuMouseCoords));
    			eventConsole.append ("\n");
    		}
    	});
	}
}
