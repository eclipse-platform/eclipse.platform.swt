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
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.events.*;

class TableTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	Table table1;
	Group tableGroup, tableItemGroup;

	/* Style widgets added to the "Style" group */
	Button checkButton, fullSelectionButton, hideSelectionButton;

	/* Display widgets added to the "Display" group */
	Button headerVisibleButton, linesVisibleButton;
	
	/* Controls and resources added to the "Colors" group */
	Button itemForegroundButton, itemBackgroundButton;
	Color itemForegroundColor, itemBackgroundColor;
	Image itemForegroundImage, itemBackgroundImage;
	
	static String [] columnTitles	= {ControlExample.getResourceString("TableTitle_0"),
									   ControlExample.getResourceString("TableTitle_1"),
									   ControlExample.getResourceString("TableTitle_2"),
									   ControlExample.getResourceString("TableTitle_3")};
									   
	static String [] stringLine0	= {ControlExample.getResourceString("TableLine0_0"),
									   ControlExample.getResourceString("TableLine0_1"),
									   ControlExample.getResourceString("TableLine0_2"),
									   ControlExample.getResourceString("TableLine0_3")};
									   
	static String [] stringLine1	= {ControlExample.getResourceString("TableLine1_0"),
									   ControlExample.getResourceString("TableLine1_1"),
									   ControlExample.getResourceString("TableLine1_2"),
									   ControlExample.getResourceString("TableLine1_3")};
									   
	static String [] stringLine2	= {ControlExample.getResourceString("TableLine2_0"),
									   ControlExample.getResourceString("TableLine2_1"),
									   ControlExample.getResourceString("TableLine2_2"),
									   ControlExample.getResourceString("TableLine2_3")};

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
		
		tableItemGroup = new Group (colorGroup, SWT.NONE);
		tableItemGroup.setText (ControlExample.getResourceString ("Table_Item_Colors"));
		GridData data = new GridData ();
		data.horizontalSpan = 2;
		tableItemGroup.setLayoutData (data);
		tableItemGroup.setLayout (new GridLayout (2, false));
		new Label (tableItemGroup, SWT.NONE).setText (ControlExample.getResourceString ("Item_Foreground_Color"));
		itemForegroundButton = new Button (tableItemGroup, SWT.PUSH);
		new Label (tableItemGroup, SWT.NONE).setText (ControlExample.getResourceString ("Item_Background_Color"));
		itemBackgroundButton = new Button (tableItemGroup, SWT.PUSH);
		
		Shell shell = colorGroup.getShell ();
		final ColorDialog foregroundDialog = new ColorDialog (shell);
		final ColorDialog backgroundDialog = new ColorDialog (shell);

		int imageSize = 12;
		Display display = shell.getDisplay ();
		itemForegroundImage = new Image(display, imageSize, imageSize);
		itemBackgroundImage = new Image(display, imageSize, imageSize);

		/* Add listeners to set the colors and font */
		itemForegroundButton.setImage(itemForegroundImage); // sets the size of the button
		itemForegroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = itemForegroundColor;
				if (oldColor == null) oldColor = table1.getItem (0).getForeground ();
				foregroundDialog.setRGB(oldColor.getRGB()); // seed dialog with current color
				RGB rgb = foregroundDialog.open();
				if (rgb == null) return;
				oldColor = itemForegroundColor; // save old foreground color to dispose when done
				itemForegroundColor = new Color (event.display, rgb);
				setItemForeground ();
				if (oldColor != null) oldColor.dispose ();
			}
		});
		itemBackgroundButton.setImage(itemBackgroundImage); // sets the size of the button
		itemBackgroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = itemBackgroundColor;
				if (oldColor == null) oldColor = table1.getItem (0).getBackground ();
				backgroundDialog.setRGB(oldColor.getRGB()); // seed dialog with current color
				RGB rgb = backgroundDialog.open();
				if (rgb == null) return;
				oldColor = itemBackgroundColor; // save old background color to dispose when done
				itemBackgroundColor = new Color (event.display, rgb);
				setItemBackground ();
				if (oldColor != null) oldColor.dispose ();
			}
		});
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (itemBackgroundImage != null) itemBackgroundImage.dispose();
				if (itemForegroundImage != null) itemForegroundImage.dispose();
				if (itemBackgroundColor != null) itemBackgroundColor.dispose();
				if (itemForegroundColor != null) itemForegroundColor.dispose();
				itemBackgroundColor = null;
				itemForegroundColor = null;			
			}
		});
	}

	/**
	 * Creates the "Display" group.
	 */
	void createDisplayGroup () {
		super.createDisplayGroup ();
	
		/* Create display controls specific to this example */
		headerVisibleButton = new Button (displayGroup, SWT.CHECK);
		headerVisibleButton.setText (ControlExample.getResourceString("Header_Visible"));
		linesVisibleButton = new Button (displayGroup, SWT.CHECK);
		linesVisibleButton.setText (ControlExample.getResourceString("Lines_Visible"));
	
		/* Add the listeners */
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
		for (int i = 0; i < columnTitles.length; i++) {
			TableColumn tableColumn = new TableColumn(table1, SWT.NONE);
			tableColumn.setText(columnTitles[i]);
		}	
		for (int i=0; i<16; i++) {
			TableItem item = new TableItem (table1, SWT.NONE);
			item.setImage (instance.images [i % 3]);
			switch (i % 3) {
				case 0:
					stringLine0 [0] = ControlExample.getResourceString("Index") + i;
					item.setText(stringLine0);
					break;
				case 1:
					stringLine1 [0] = ControlExample.getResourceString("Index") + i;
					item.setText(stringLine1);
					break;
				case 2:
					stringLine2 [0] = ControlExample.getResourceString("Index") + i;
					item.setText(stringLine2);
					break;
			}
		}
		for (int i = 0; i < columnTitles.length; i++) {
			TableColumn tableColumn = table1.getColumn(i);
			tableColumn.pack();
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
		itemForegroundColor = null;
		setItemForeground ();
		itemBackgroundColor = null;
		setItemBackground ();
	}
	
	/**
	 * Sets the background color of TableItem [0].
	 */
	void setItemBackground () {
		table1.getItem (0).setBackground (itemBackgroundColor);
		// Set the background button's color to match the color just set.
		Color color = itemBackgroundColor;
		if (color == null) color = table1.getItem (0).getBackground ();
		drawImage (itemForegroundImage, color);
		itemBackgroundButton.setImage (itemForegroundImage);
	}
	
	/**
	 * Sets the foreground color of TableItem [0].
	 */
	void setItemForeground () {
		table1.getItem (0).setForeground (itemForegroundColor);
		// Set the foreground button's color to match the color just set.
		Color color = itemForegroundColor;
		if (color == null) color = table1.getItem (0).getForeground ();
		drawImage (itemBackgroundImage, color);
		itemForegroundButton.setImage (itemBackgroundImage);
	}
	
	/**
	 * Sets the font of the "Example" widgets.
	 */
	void setExampleWidgetFont () {
		super.setExampleWidgetFont();
		for (int i = 0; i < columnTitles.length; i++) {
			TableColumn tableColumn = table1.getColumn(i);
			tableColumn.pack();
		}
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		setItemBackground ();
		setItemForeground ();
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
