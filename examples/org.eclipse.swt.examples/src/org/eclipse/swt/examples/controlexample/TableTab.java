package org.eclipse.swt.examples.controlexample;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

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
	Button checkButton, fullSelectionButton;

	/* Display widgets added to the "Display" group */
	Button headerVisibleButton, linesVisibleButton;
	
	/* Color widgets added to the "Color" group */
	Button itemBackgroundButton, itemForegroundButton;
	Color defaultItemBackgroundColor, defaultItemForegroundColor, itemBackgroundColor, itemForegroundColor;
	Image itemBackgroundImage, itemForegroundImage;
	RGB chosenItemBackgroundColor, chosenItemForegroundColor;
	boolean itemSet = false;
	
	static String [] columnTitles	= {ControlExample.getResourceString("TableTitle_0"),
									   ControlExample.getResourceString("TableTitle_1"),
									   ControlExample.getResourceString("TableTitle_2"),
									   ControlExample.getResourceString("TableTitle_3")};
									   
	static String [] stringLine0		= {ControlExample.getResourceString("TableLine0_0"),
										  ControlExample.getResourceString("TableLine0_1"),
										  ControlExample.getResourceString("TableLine0_2"),
										  ControlExample.getResourceString("TableLine0_3")};
									   
	static String [] stringLine1		= {ControlExample.getResourceString("TableLine1_0"),
										  ControlExample.getResourceString("TableLine1_1"),
										  ControlExample.getResourceString("TableLine1_2"),
										  ControlExample.getResourceString("TableLine1_3")};
									   
	static String [] stringLine2		= {ControlExample.getResourceString("TableLine2_0"),
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
		/* Get default system colors for the control */
		table1 = new Table (tableGroup, SWT.NONE);
		defaultBackgroundColor = table1.getBackground ();
		defaultForegroundColor = table1.getForeground ();
		
		super.createColorGroup();
		
		tableItemGroup = new Group (colorGroup, SWT.NONE);
		tableItemGroup.setText (ControlExample.getResourceString ("Item_Colors"));
		GridData data = new GridData ();
		data.horizontalSpan = 2;
		tableItemGroup.setLayoutData (data);
		tableItemGroup.setLayout (new GridLayout (2, false));
		new Label (tableItemGroup, SWT.NONE).setText (ControlExample.getResourceString ("Item_Background_Color"));
		itemBackgroundButton = new Button (tableItemGroup, SWT.PUSH);
		new Label (tableItemGroup, SWT.NONE).setText (ControlExample.getResourceString ("Item_Foreground_Color"));
		itemForegroundButton = new Button (tableItemGroup, SWT.PUSH);
		
		/* Get default system colors for the control */
		TableItem item = new TableItem (table1, SWT.NONE);
		defaultItemBackgroundColor = item.getBackground();
		defaultItemForegroundColor = item.getForeground();
		table1.dispose ();
		
		/* Create images to display current colors */
		chosenItemBackgroundColor = defaultItemBackgroundColor.getRGB();
		chosenItemForegroundColor = defaultItemForegroundColor.getRGB();
		GC gc = new GC (fontButton.getShell());
		final int fontHeight = gc.getFontMetrics().getHeight();
		gc.dispose ();
		itemBackgroundImage = new Image(colorGroup.getDisplay(),fontHeight*2,fontHeight);
		gc = new GC(itemBackgroundImage);
		gc.setBackground(defaultItemBackgroundColor);
		gc.fillRectangle(0,0,fontHeight*2-1,fontHeight-1);
		gc.drawRectangle(0,0,fontHeight*2-1,fontHeight-1);
		gc.dispose();
		itemForegroundImage = new Image(colorGroup.getDisplay(),fontHeight*2,fontHeight);
		gc = new GC(itemForegroundImage);
		gc.setBackground(defaultItemForegroundColor);
		gc.fillRectangle(0,0,fontHeight*2-1,fontHeight-1);
		gc.drawRectangle(0,0,fontHeight*2-1,fontHeight-1);
		gc.dispose();
		
		/* Add listeners to set the colors and font */
		itemBackgroundButton.setImage(itemBackgroundImage);
		itemBackgroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				itemSet = true;
				ColorDialog colorDialog= new ColorDialog(itemBackgroundButton.getShell());
				colorDialog.setRGB(chosenItemBackgroundColor);
				RGB newColor = colorDialog.open();
				if (newColor != null) {
					Color oldColor = null;
					if (itemBackgroundColor != null) oldColor = itemBackgroundColor;
					itemBackgroundColor = new Color (itemBackgroundButton.getDisplay(), newColor);
					GC gc = new GC(itemBackgroundImage);
					gc.setBackground(itemBackgroundColor);
					gc.fillRectangle(0,0,fontHeight*2-1,fontHeight-1);
					gc.drawRectangle(0,0,fontHeight*2-1,fontHeight-1);
					gc.dispose();
					itemBackgroundButton.setImage(itemBackgroundImage);
					chosenItemBackgroundColor = newColor;
					setItemColors ();
					if (oldColor != null) oldColor.dispose ();
				}
			}
		});
		itemForegroundButton.setImage(itemForegroundImage);
		itemForegroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				itemSet = true;
				ColorDialog colorDialog= new ColorDialog(itemForegroundButton.getShell());
				colorDialog.setRGB(chosenItemForegroundColor);
				RGB newColor = colorDialog.open();
				if (newColor != null) {
					Color oldColor = null;
					if (itemForegroundColor != null) oldColor = itemForegroundColor;
					itemForegroundColor = new Color (itemForegroundButton.getDisplay(), newColor);
					GC gc= new GC(itemForegroundImage);
					gc.setBackground(itemForegroundColor);
					gc.fillRectangle(0,0,fontHeight*2-1,fontHeight-1);
					gc.drawRectangle(0,0,fontHeight*2-1,fontHeight-1);
					gc.dispose();
					itemForegroundButton.setImage(itemForegroundImage);
					chosenItemForegroundColor = newColor;
					setItemColors ();
					if (oldColor != null) oldColor.dispose ();
				}
			}
		});
		itemBackgroundButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (itemBackgroundImage != null) itemBackgroundImage.dispose();
				if (itemForegroundImage != null) itemForegroundImage.dispose();
				if (itemBackgroundColor != null) itemBackgroundColor.dispose();
				if (itemForegroundColor != null) itemForegroundColor.dispose();
				itemBackgroundImage = null;
				itemForegroundImage = null;
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
		tableGroup = new Group (exampleGroup, SWT.NULL);
		tableGroup.setLayout (new GridLayout ());
		tableGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		tableGroup.setText ("Table");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {	
		/* Compute the widget style */
		int style = SWT.NONE;
		if (singleButton.getSelection ()) style |= SWT.SINGLE;
		if (multiButton.getSelection ()) style |= SWT.MULTI;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (checkButton.getSelection ()) style |= SWT.CHECK;
		if (fullSelectionButton.getSelection ()) style |= SWT.FULL_SELECTION;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
	
		/* Create the table widget */
		table1 = new Table (tableGroup, style);
		table1.setFont (font);
	
		/* Fill the table with data */
		for (int i = 0; i < columnTitles.length; i++) {
			TableColumn tableColumn = new TableColumn(table1, SWT.NULL);
			tableColumn.setText(columnTitles[i]);
		}	
		for (int i=0; i<16; i++) {
			TableItem item = new TableItem (table1, SWT.NULL);
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
		
		/* Set the colors */
		setColors ();
		setItemColors ();
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
	 * Sets the background and foreground colors of the "Example" widgets.
	 */
	void setColors () {
		table1.setBackground (backgroundColor);
		table1.setForeground (foregroundColor);
	}
	
	/**
	 * Sets the background and foreground colors of the TableItems.
	 */
	void setItemColors () {
		if (!itemSet) return;
		for (int i = 0; i < table1.getItemCount(); i++) {
			TableItem item = table1.getItem (i);
			item.setBackground (itemBackgroundColor);
			item.setForeground (itemForegroundColor);
		}
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		setWidgetHeaderVisible ();
		setWidgetLinesVisible ();
		checkButton.setSelection ((table1.getStyle () & SWT.CHECK) != 0);
		fullSelectionButton.setSelection ((table1.getStyle () & SWT.FULL_SELECTION) != 0);
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
