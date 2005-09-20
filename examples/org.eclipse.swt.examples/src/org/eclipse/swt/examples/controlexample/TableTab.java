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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class TableTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	Table table1;
	Group tableGroup, itemGroup;

	/* Style widgets added to the "Style" group */
	Button checkButton, fullSelectionButton, hideSelectionButton;

	/* Other widgets added to the "Other" group */
	Button multipleColumns, moveableColumns, headerVisibleButton, linesVisibleButton;
	
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
	TableTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Colors" group.
	 */
	void createColorGroup () {
		super.createColorGroup();
		
		itemGroup = new Group (colorGroup, SWT.NONE);
		itemGroup.setText (ControlExample.getResourceString ("Table_Item_Colors"));
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
				if (oldColor == null) oldColor = table1.getItem (0).getForeground ();
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
				if (oldColor == null) oldColor = table1.getItem (0).getBackground ();
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
				if (oldFont == null) oldFont = table1.getItem (0).getFont ();
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
		multipleColumns = new Button (otherGroup, SWT.CHECK);
		multipleColumns.setText (ControlExample.getResourceString("Multiple_Columns"));
		multipleColumns.setSelection(true);
		moveableColumns = new Button (otherGroup, SWT.CHECK);
		moveableColumns.setText (ControlExample.getResourceString("Moveable_Columns"));
		moveableColumns.setSelection(false);
		linesVisibleButton = new Button (otherGroup, SWT.CHECK);
		linesVisibleButton.setText (ControlExample.getResourceString("Lines_Visible"));
	
		/* Add the listeners */
		headerVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetHeaderVisible ();
			}
		});
		multipleColumns.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				recreateExampleWidgets ();
			}
		});
		moveableColumns.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setColumnsMoveable ();
			}
		});
		linesVisibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setWidgetLinesVisible ();
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
		tableGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
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
		if (checkButton.getSelection ()) style |= SWT.CHECK;
		if (fullSelectionButton.getSelection ()) style |= SWT.FULL_SELECTION;
		if (hideSelectionButton.getSelection ()) style |= SWT.HIDE_SELECTION;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
	
		/* Create the table widget */
		table1 = new Table (tableGroup, style);
	
		/* Fill the table with data */
		if (multipleColumns.getSelection()) {
			for (int i = 0; i < columnTitles.length; i++) {
				TableColumn tableColumn = new TableColumn(table1, SWT.NONE);
				tableColumn.setText(columnTitles[i]);
				tableColumn.setToolTipText(ControlExample.getResourceString("Tooltip") + columnTitles[i]);
			}
		} else {
			new TableColumn(table1, SWT.NONE);
		}
		table1.setSortColumn(table1.getColumn(0));
		for (int i=0; i<16; i++) {
			TableItem item = new TableItem (table1, SWT.NONE);
			item.setImage (instance.images [i % 3]);
			setItemText (item, i, ControlExample.getResourceString("Index") + i);
		}
		packColumns();
		setColumnsMoveable();
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
	 * Creates the "Style" group.
	 */
	void createStyleGroup () {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
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
	Control [] getExampleWidgets () {
		return new Control [] {table1};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"ItemCount", "Selection", "SelectionIndex", "SortDirection", "TopIndex"};
	}

	String setMethodName(String methodRoot) {
		/* Override to handle special case of int getSelectionIndex()/setSelection(int) */
		return (methodRoot.equals("SelectionIndex")) ? "setSelection" : "set" + methodRoot;
	}

	Object[] parameterForType(String typeName, String value, Control control) {
		if (value.equals("")) return new Object[] {new TableItem[0]}; // bug in Table?
		if (typeName.equals("org.eclipse.swt.widgets.TableItem")) {
			TableItem item = findItem(value, ((Table) control).getItems());
			if (item != null) return new Object[] {item};
		}
		if (typeName.equals("[Lorg.eclipse.swt.widgets.TableItem;")) {
			String[] values = value.split(",");
			TableItem[] items = new TableItem[values.length];
			for (int i = 0; i < values.length; i++) {
				items[i] = findItem(values[i], ((Table) control).getItems());
			}
			return new Object[] {items};
		}
		return super.parameterForType(typeName, value, control);
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
		setExampleWidgetSize ();
		if (oldFont != null) oldFont.dispose();
	}
	
	/**
	 * Sets the background color of TableItem [0].
	 */
	void setItemBackground () {
		table1.getItem (0).setBackground (itemBackgroundColor);
		/* Set the background button's color to match the color just set. */
		Color color = itemBackgroundColor;
		if (color == null) color = table1.getItem (0).getBackground ();
		drawImage (itemBackgroundImage, color);
		itemBackgroundButton.setImage (itemBackgroundImage);
	}
	
	/**
	 * Sets the foreground color of TableItem [0].
	 */
	void setItemForeground () {
		table1.getItem (0).setForeground (itemForegroundColor);
		/* Set the foreground button's color to match the color just set. */
		Color color = itemForegroundColor;
		if (color == null) color = table1.getItem (0).getForeground ();
		drawImage (itemForegroundImage, color);
		itemForegroundButton.setImage (itemForegroundImage);
	}
	
	/**
	 * Sets the font of TableItem 0.
	 */
	void setItemFont () {
		if (instance.startup) return;
		table1.getItem (0).setFont (itemFont);
		packColumns ();
	}

	/**
	 * Sets the font of the "Example" widgets.
	 */
	void setExampleWidgetFont () {
		super.setExampleWidgetFont();
		packColumns ();
	}
	
	void packColumns () {
		int columnCount = table1.getColumnCount(); 
		for (int i = 0; i < columnCount; i++) {
			TableColumn tableColumn = table1.getColumn(i);
			tableColumn.pack();
		}
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
		checkButton.setSelection ((table1.getStyle () & SWT.CHECK) != 0);
		fullSelectionButton.setSelection ((table1.getStyle () & SWT.FULL_SELECTION) != 0);
		hideSelectionButton.setSelection ((table1.getStyle () & SWT.HIDE_SELECTION) != 0);
	}
	
	/**
	 * Sets the header visible state of the "Example" widgets.
	 */
	void setWidgetHeaderVisible () {
		table1.setHeaderVisible (headerVisibleButton.getSelection ());
	}
	
	/**
	 * Sets the lines visible state of the "Example" widgets.
	 */
	void setWidgetLinesVisible () {
		table1.setLinesVisible (linesVisibleButton.getSelection ());
	}
}
