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
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

class CTabFolderTab extends Tab {
	int lastSelectedTab = 0;
	
	/* Example widgets and groups that contain them */
	CTabFolder tabFolder1;
	Group tabFolderGroup, itemGroup;
	
	/* Style widgets added to the "Style" group */
	Button topButton, bottomButton, flatButton, closeButton;
	Button rightButton, fillButton, wrapButton;

	static String [] CTabItems1 = {ControlExample.getResourceString("CTabItem1_0"),
								  ControlExample.getResourceString("CTabItem1_1"),
								  ControlExample.getResourceString("CTabItem1_2")};

	/* Controls and resources added to the "Fonts" group */
	static final int SELECTION_FOREGROUND_COLOR = 3;
	static final int SELECTION_BACKGROUND_COLOR = 4;
	static final int ITEM_FONT = 5;
	Color selectionForegroundColor, selectionBackgroundColor;
	Font itemFont;
	
	/* Other widgets added to the "Other" group */
	Button simpleTabButton, singleTabButton, imageButton, showMinButton, showMaxButton,
	topRightButton, unselectedCloseButton, unselectedImageButton;

	ToolBar topRightControl;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	CTabFolderTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Colors and Fonts" group.
	 */
	void createColorAndFontGroup () {
		super.createColorAndFontGroup();
		
		TableItem item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Selection_Foreground_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Selection_Background_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Item_Font"));

		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (selectionBackgroundColor != null) selectionBackgroundColor.dispose();
				if (selectionForegroundColor != null) selectionForegroundColor.dispose();
				if (itemFont != null) itemFont.dispose();
				selectionBackgroundColor = null;
				selectionForegroundColor = null;			
				itemFont = null;
			}
		});
	}

	void changeFontOrColor(int index) {
		switch (index) {
			case SELECTION_FOREGROUND_COLOR: {
				Color oldColor = selectionForegroundColor;
				if (oldColor == null) oldColor = tabFolder1.getSelectionForeground();
				colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = selectionForegroundColor;
				selectionForegroundColor = new Color (display, rgb);
				setSelectionForeground ();
				if (oldColor != null) oldColor.dispose ();
			}
			break;
			case SELECTION_BACKGROUND_COLOR: {
				Color oldColor = selectionBackgroundColor;
				if (oldColor == null) oldColor = tabFolder1.getSelectionBackground();
				colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = selectionBackgroundColor;
				selectionBackgroundColor = new Color (display, rgb);
				setSelectionBackground ();
				if (oldColor != null) oldColor.dispose ();
			}
			break;
			case ITEM_FONT: {
				Font oldFont = itemFont;
				if (oldFont == null) oldFont = tabFolder1.getItem (0).getFont ();
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
		simpleTabButton = new Button (otherGroup, SWT.CHECK);
		simpleTabButton.setText (ControlExample.getResourceString("Set_Simple_Tabs"));
		simpleTabButton.setSelection(true);
		simpleTabButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setSimpleTabs();
			}
		});
				
		singleTabButton = new Button (otherGroup, SWT.CHECK);
		singleTabButton.setText (ControlExample.getResourceString("Set_Single_Tabs"));
		singleTabButton.setSelection(false);
		singleTabButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setSingleTabs();
			}
		});
		
		showMinButton = new Button (otherGroup, SWT.CHECK);
		showMinButton.setText (ControlExample.getResourceString("Set_Min_Visible"));
		showMinButton.setSelection(false);
		showMinButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setMinimizeVisible();
			}
		});
		
		showMaxButton = new Button (otherGroup, SWT.CHECK);
		showMaxButton.setText (ControlExample.getResourceString("Set_Max_Visible"));
		showMaxButton.setSelection(false);
		showMaxButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setMaximizeVisible();
			}
		});
		
		topRightButton = new Button (otherGroup, SWT.CHECK);
		topRightButton.setText (ControlExample.getResourceString("Set_Top_Right"));
		topRightButton.setSelection(false);
		topRightButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setTopRight();
			}
		});
		
		imageButton = new Button (otherGroup, SWT.CHECK);
		imageButton.setText (ControlExample.getResourceString("Set_Image"));
		imageButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setImages();
			}
		});
		
		unselectedImageButton = new Button (otherGroup, SWT.CHECK);
		unselectedImageButton.setText (ControlExample.getResourceString("Set_Unselected_Image_Visible"));
		unselectedImageButton.setSelection(true);
		unselectedImageButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setUnselectedImageVisible();
			}
		});
		unselectedCloseButton = new Button (otherGroup, SWT.CHECK);
		unselectedCloseButton.setText (ControlExample.getResourceString("Set_Unselected_Close_Visible"));
		unselectedCloseButton.setSelection(true);
		unselectedCloseButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setUnselectedCloseVisible();
			}
		});
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the CTabFolder */
		tabFolderGroup = new Group (exampleGroup, SWT.NONE);
		tabFolderGroup.setLayout (new GridLayout ());
		tabFolderGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		tabFolderGroup.setText ("CTabFolder");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (topButton.getSelection ()) style |= SWT.TOP;
		if (bottomButton.getSelection ()) style |= SWT.BOTTOM;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (flatButton.getSelection ()) style |= SWT.FLAT;
		if (closeButton.getSelection ()) style |= SWT.CLOSE;

		/* Create the example widgets */
		tabFolder1 = new CTabFolder (tabFolderGroup, style);
		for (int i = 0; i < CTabItems1.length; i++) {
			CTabItem item = new CTabItem(tabFolder1, SWT.NONE);
			item.setText(CTabItems1[i]);
			Text text = new Text(tabFolder1, SWT.WRAP | SWT.MULTI);
			text.setText(ControlExample.getResourceString("CTabItem_content") + ": " + i);
			item.setControl(text);
		}
		tabFolder1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				lastSelectedTab = tabFolder1.getSelectionIndex();
			}
		});
		
		/* If we have saved state, restore it */
		tabFolder1.setSelection(lastSelectedTab);
		setTopRight ();
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup ();
		
		/* Create the extra widgets */
		topButton = new Button (styleGroup, SWT.RADIO);
		topButton.setText ("SWT.TOP");
		topButton.setSelection(true);
		bottomButton = new Button (styleGroup, SWT.RADIO);
		bottomButton.setText ("SWT.BOTTOM");
		borderButton = new Button (styleGroup, SWT.CHECK);
		borderButton.setText ("SWT.BORDER");
		flatButton = new Button (styleGroup, SWT.CHECK);
		flatButton.setText ("SWT.FLAT");
		closeButton = new Button (styleGroup, SWT.CHECK);
		closeButton.setText ("SWT.CLOSE");
		
		Group topRightGroup = new Group(styleGroup, SWT.NONE);
		topRightGroup.setLayout (new GridLayout ());
		topRightGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		topRightGroup.setText (ControlExample.getResourceString("Top_Right_Styles"));
		rightButton = new Button (topRightGroup, SWT.RADIO);
		rightButton.setText ("SWT.RIGHT");
		rightButton.setSelection(true);
		fillButton = new Button (topRightGroup, SWT.RADIO);
		fillButton.setText ("SWT.FILL");
		wrapButton = new Button (topRightGroup, SWT.RADIO);
		wrapButton.setText ("SWT.RIGHT | SWT.WRAP");
	}
	
	/**
	 * Gets the list of custom event names.
	 * 
	 * @return an array containing custom event names
	 */
	String [] getCustomEventNames () {
		return new String [] {"CTabFolderEvent"};
	}
	
	/**
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		return tabFolder1.getItems();
	}
	
	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {tabFolder1};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "CTabFolder";
	}

	/**
	 * Hooks the custom listener specified by eventName.
	 */
	void hookCustomListener (final String eventName) {
		if (eventName == "CTabFolderEvent") {
			tabFolder1.addCTabFolder2Listener (new CTabFolder2Adapter () {
				public void close (CTabFolderEvent event) {
					log (eventName, event);
				}
			});
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
		Color oldColor = selectionForegroundColor;
		selectionForegroundColor = null;
		setSelectionForeground ();
		if (oldColor != null) oldColor.dispose();
		oldColor = selectionBackgroundColor;
		selectionBackgroundColor = null;
		setSelectionBackground ();
		if (oldColor != null) oldColor.dispose();
		Font oldFont = itemFont;
		itemFont = null;
		setItemFont ();
		if (oldFont != null) oldFont.dispose();
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState();
		setSimpleTabs();
		setSingleTabs();
		setImages();
		setMinimizeVisible();
		setMaximizeVisible();
		setUnselectedCloseVisible();
		setUnselectedImageVisible();
		setSelectionBackground ();
		setSelectionForeground ();
		setItemFont ();
		setExampleWidgetSize();
	}
	
	/**
	 * Sets the shape that the CTabFolder will use to render itself. 
	 */
	void setSimpleTabs () {
		tabFolder1.setSimple (simpleTabButton.getSelection ());
		setExampleWidgetSize();
	}
	
	/**
	 * Sets the number of tabs that the CTabFolder should display.
	 */
	void setSingleTabs () {
		tabFolder1.setSingle (singleTabButton.getSelection ());
		setExampleWidgetSize();
	}
	/**
	 * Sets an image into each item of the "Example" widgets.
	 */
	void setImages () {
		boolean setImage = imageButton.getSelection ();
		CTabItem items[] = tabFolder1.getItems ();
		for (int i = 0; i < items.length; i++) {
			if (setImage) {
				items[i].setImage (instance.images[ControlExample.ciClosedFolder]);
			} else {
				items[i].setImage (null);
			}
		}
		setExampleWidgetSize ();
	}
	/**
	 * Sets the visibility of the minimize button
	 */
	void setMinimizeVisible () {
		tabFolder1.setMinimizeVisible(showMinButton.getSelection ());
		setExampleWidgetSize();
	}
	/**
	 * Sets the visibility of the maximize button
	 */
	void setMaximizeVisible () {
		tabFolder1.setMaximizeVisible(showMaxButton.getSelection ());
		setExampleWidgetSize();
	}
	/**
	 * Sets the top right control to a toolbar
	 */
	void setTopRight () {
		if (topRightButton.getSelection ()) {
			topRightControl = new ToolBar(tabFolder1, SWT.FLAT);
			ToolItem item = new ToolItem(topRightControl, SWT.PUSH);
			item.setImage(instance.images[ControlExample.ciClosedFolder]);
			item = new ToolItem(topRightControl, SWT.PUSH);
			item.setImage(instance.images[ControlExample.ciOpenFolder]);
			int topRightStyle = 0;
			if (rightButton.getSelection ()) topRightStyle |= SWT.RIGHT;
			if (fillButton.getSelection ()) topRightStyle |= SWT.FILL;
			if (wrapButton.getSelection ()) topRightStyle |= SWT.RIGHT | SWT.WRAP;
			tabFolder1.setTopRight(topRightControl, topRightStyle);
		} else {
			if (topRightControl != null) {
				tabFolder1.setTopRight(null);
				topRightControl.dispose();
			}
		}
		setExampleWidgetSize();
	}
	/**
	 * Sets the visibility of the close button on unselected tabs
	 */
	void setUnselectedCloseVisible () {
		tabFolder1.setUnselectedCloseVisible(unselectedCloseButton.getSelection ());
		setExampleWidgetSize();
	}
	/**
	 * Sets the visibility of the image on unselected tabs
	 */
	void setUnselectedImageVisible () {
		tabFolder1.setUnselectedImageVisible(unselectedImageButton.getSelection ());
		setExampleWidgetSize();
	}
	/**
	 * Sets the background color of CTabItem 0.
	 */
	void setSelectionBackground () {
		if (!instance.startup) {
			tabFolder1.setSelectionBackground(selectionBackgroundColor);
		}
		// Set the selection background item's image to match the background color of the selection.
		Color color = selectionBackgroundColor;
		if (color == null) color = tabFolder1.getSelectionBackground ();
		TableItem item = colorAndFontTable.getItem(SELECTION_BACKGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the foreground color of CTabItem 0.
	 */
	void setSelectionForeground () {
		if (!instance.startup) {
			tabFolder1.setSelectionForeground(selectionForegroundColor);
		}
		// Set the selection foreground item's image to match the foreground color of the selection.
		Color color = selectionForegroundColor;
		if (color == null) color = tabFolder1.getSelectionForeground ();
		TableItem item = colorAndFontTable.getItem(SELECTION_FOREGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the font of CTabItem 0.
	 */
	void setItemFont () {
		if (!instance.startup) {
			tabFolder1.getItem (0).setFont (itemFont);
			setExampleWidgetSize();
		}
		/* Set the font item's image to match the font of the item. */
		Font ft = itemFont;
		if (ft == null) ft = tabFolder1.getItem (0).getFont ();
		TableItem item = colorAndFontTable.getItem(ITEM_FONT);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (fontImage(ft));
		item.setFont(ft);
		colorAndFontTable.layout ();
	}
}
