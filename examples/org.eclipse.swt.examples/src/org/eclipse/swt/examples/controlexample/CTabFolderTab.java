/*******************************************************************************
 * Copyright (c) 2000, 2016 IBM Corporation and others.
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


import static org.eclipse.swt.events.SelectionListener.widgetSelectedAdapter;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabFolder2Listener;
import org.eclipse.swt.custom.CTabFolderEvent;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.swt.widgets.Widget;

class CTabFolderTab extends Tab {
	int lastSelectedTab = 0;

	/* Example widgets and groups that contain them */
	CTabFolder tabFolder1;
	Group tabFolderGroup, itemGroup, tabHeightGroup;

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
	static final int ITEM_FOREGROUND_COLOR = 6;
	static final int ITEM_BACKGROUND_COLOR = 7;
	Color selectionForegroundColor, selectionBackgroundColor, itemForegroundColor, itemBackgroundColor;
	Font itemFont;

	/* Other widgets added to the "Other" group */
	Button simpleTabButton, singleTabButton, imageButton, showMinButton, showMaxButton,
	topRightButton, unselectedCloseButton, unselectedImageButton;

	ToolBar topRightControl;

	Button tabHeightDefault, tabHeightSmall, tabHeightMedium, tabHeightLarge;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	CTabFolderTab(ControlExample instance) {
		super(instance);
	}

	@Override
	void createControlGroup() {
		super.createControlGroup();

		/* Create a group for the CTabFolder */
		tabHeightGroup = new Group (controlGroup, SWT.NONE);
		tabHeightGroup.setLayout (new GridLayout ());
		tabHeightGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
		tabHeightGroup.setText (ControlExample.getResourceString ("Tab_Height"));

		tabHeightDefault = new Button(tabHeightGroup, SWT.RADIO);
		tabHeightDefault.setText(ControlExample.getResourceString("Preferred"));
		tabHeightDefault.setSelection(true);
		tabHeightDefault.addSelectionListener (widgetSelectedAdapter(event -> setTabHeight(SWT.DEFAULT)));

		tabHeightSmall = new Button(tabHeightGroup, SWT.RADIO);
		tabHeightSmall.setText("8");
		tabHeightSmall.addSelectionListener (widgetSelectedAdapter(event -> setTabHeight(Integer.parseInt(tabHeightSmall.getText()))));

		tabHeightMedium = new Button(tabHeightGroup, SWT.RADIO);
		tabHeightMedium.setText("20");
		tabHeightMedium.addSelectionListener (widgetSelectedAdapter(event -> setTabHeight(Integer.parseInt(tabHeightMedium.getText()))));

		tabHeightLarge = new Button(tabHeightGroup, SWT.RADIO);
		tabHeightLarge.setText("45");
		tabHeightLarge.addSelectionListener (widgetSelectedAdapter(event -> setTabHeight(Integer.parseInt(tabHeightLarge.getText()))));
	}

	/**
	 * Creates the "Colors and Fonts" group.
	 */
	@Override
	void createColorAndFontGroup () {
		super.createColorAndFontGroup();

		TableItem item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Selection_Foreground_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Selection_Background_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Item_Font"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Item_Foreground_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Item_Background_Color"));

		shell.addDisposeListener(event -> {
			if (selectionBackgroundColor != null) selectionBackgroundColor.dispose();
			if (selectionForegroundColor != null) selectionForegroundColor.dispose();
			if (itemFont != null) itemFont.dispose();
			if (itemBackgroundColor != null) itemBackgroundColor.dispose();
			if (itemForegroundColor != null) itemForegroundColor.dispose();
			selectionBackgroundColor = null;
			selectionForegroundColor = null;
			itemFont = null;
			itemBackgroundColor = null;
			itemForegroundColor = null;
		});
	}

	@Override
	void changeFontOrColor(int index) {
		switch (index) {
			case SELECTION_FOREGROUND_COLOR: {
				Color oldColor = selectionForegroundColor;
				if (oldColor == null) oldColor = tabFolder1.getSelectionForeground();
				colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = selectionForegroundColor;
				selectionForegroundColor = new Color (rgb);
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
				selectionBackgroundColor = new Color (rgb);
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
			case ITEM_FOREGROUND_COLOR: {
				Color oldColor = itemForegroundColor;
				if (oldColor == null) oldColor = tabFolder1.getItem(0).getControl().getForeground();
				colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = itemForegroundColor;
				itemForegroundColor = new Color (rgb);
				setItemForeground ();
				if (oldColor != null) oldColor.dispose ();
			}
			break;
			case ITEM_BACKGROUND_COLOR: {
				Color oldColor = itemBackgroundColor;
				if (oldColor == null) oldColor = tabFolder1.getItem(0).getControl().getBackground();
				colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = itemBackgroundColor;
				itemBackgroundColor = new Color (rgb);
				setItemBackground ();
				if (oldColor != null) oldColor.dispose ();
			}
			break;
			default:
				super.changeFontOrColor(index);
		}
	}

	/**
	 * Creates the "Other" group.
	 */
	@Override
	void createOtherGroup () {
		super.createOtherGroup ();

		/* Create display controls specific to this example */
		simpleTabButton = new Button (otherGroup, SWT.CHECK);
		simpleTabButton.setText (ControlExample.getResourceString("Set_Simple_Tabs"));
		simpleTabButton.setSelection(true);
		simpleTabButton.addSelectionListener (widgetSelectedAdapter(event -> setSimpleTabs()));

		singleTabButton = new Button (otherGroup, SWT.CHECK);
		singleTabButton.setText (ControlExample.getResourceString("Set_Single_Tabs"));
		singleTabButton.setSelection(false);
		singleTabButton.addSelectionListener (widgetSelectedAdapter(event -> setSingleTabs()));

		showMinButton = new Button (otherGroup, SWT.CHECK);
		showMinButton.setText (ControlExample.getResourceString("Set_Min_Visible"));
		showMinButton.setSelection(false);
		showMinButton.addSelectionListener (widgetSelectedAdapter(event -> setMinimizeVisible()));

		showMaxButton = new Button (otherGroup, SWT.CHECK);
		showMaxButton.setText (ControlExample.getResourceString("Set_Max_Visible"));
		showMaxButton.setSelection(false);
		showMaxButton.addSelectionListener (widgetSelectedAdapter(event -> setMaximizeVisible()));

		topRightButton = new Button (otherGroup, SWT.CHECK);
		topRightButton.setText (ControlExample.getResourceString("Set_Top_Right"));
		topRightButton.setSelection(false);
		topRightButton.addSelectionListener (widgetSelectedAdapter(event -> setTopRight()));

		imageButton = new Button (otherGroup, SWT.CHECK);
		imageButton.setText (ControlExample.getResourceString("Set_Image"));
		imageButton.addSelectionListener (widgetSelectedAdapter(event -> setImages()));

		unselectedImageButton = new Button (otherGroup, SWT.CHECK);
		unselectedImageButton.setText (ControlExample.getResourceString("Set_Unselected_Image_Visible"));
		unselectedImageButton.setSelection(true);
		unselectedImageButton.addSelectionListener (widgetSelectedAdapter(event -> setUnselectedImageVisible()));
		unselectedCloseButton = new Button (otherGroup, SWT.CHECK);
		unselectedCloseButton.setText (ControlExample.getResourceString("Set_Unselected_Close_Visible"));
		unselectedCloseButton.setSelection(true);
		unselectedCloseButton.addSelectionListener (widgetSelectedAdapter(event -> setUnselectedCloseVisible()));
	}

	/**
	 * Creates the "Example" group.
	 */
	@Override
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
	@Override
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
		tabFolder1.addListener(SWT.Selection, event -> lastSelectedTab = tabFolder1.getSelectionIndex());

		/* If we have saved state, restore it */
		tabFolder1.setSelection(lastSelectedTab);
		setTopRight ();
	}

	/**
	 * Creates the "Style" group.
	 */
	@Override
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
	@Override
	String [] getCustomEventNames () {
		return new String [] {"CTabFolderEvent"};
	}

	/**
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	@Override
	Item [] getExampleWidgetItems () {
		return tabFolder1.getItems();
	}

	/**
	 * Gets the "Example" widget children.
	 */
	@Override
	Widget [] getExampleWidgets () {
		return new Widget [] {tabFolder1};
	}

	/**
	 * Gets the text for the tab folder item.
	 */
	@Override
	String getTabText () {
		return "CTabFolder";
	}

	/**
	 * Hooks the custom listener specified by eventName.
	 */
	@Override
	void hookCustomListener (final String eventName) {
		if (eventName == "CTabFolderEvent") {
			tabFolder1.addCTabFolder2Listener (new CTabFolder2Listener () {
				@Override
				public void close (CTabFolderEvent event) {
					log (eventName, event);
				}
				@Override
				public void minimize(CTabFolderEvent event) {
					log (eventName, event);
				}
				@Override
				public void maximize(CTabFolderEvent event) {
					log (eventName, event);
				}
				@Override
				public void restore(CTabFolderEvent event) {
					log (eventName, event);
				}
				@Override
				public void showList(CTabFolderEvent event) {
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
	@Override
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
		oldColor = itemForegroundColor;
		itemForegroundColor = null;
		setItemForeground ();
		if (oldColor != null) oldColor.dispose();
		oldColor = itemBackgroundColor;
		itemBackgroundColor = null;
		setItemBackground ();
	}

	/**
	 * Sets the state of the "Example" widgets.
	 */
	@Override
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
		setItemBackground();
		setItemForeground();
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
		for (CTabItem item : items) {
			if (setImage) {
				item.setImage (instance.images[ControlExample.ciClosedFolder]);
			} else {
				item.setImage (null);
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

	/**
	 * Sets the background color of the CTabItem 0 content element.
	 */
	void setItemBackground () {
		if (!instance.startup) {
			tabFolder1.getItem(0).getControl().setBackground(itemBackgroundColor);
		}
		/* Set the background color item's image to match the background color of the content. */
		Color color = itemBackgroundColor;
		if (color == null) color = tabFolder1.getItem(0).getControl().getBackground();
		TableItem item = colorAndFontTable.getItem(ITEM_BACKGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}

	/**
	 * Sets the foreground color of the CTabItem 0  content element.
	 */
	void setItemForeground () {
		if (!instance.startup) {
			tabFolder1.getItem(0).getControl().setForeground(itemForegroundColor);
		}
		/* Set the foreground color item's image to match the foreground color of the content. */
		Color color = itemForegroundColor;
		if (color == null) color = tabFolder1.getItem(0).getControl().getForeground();
		TableItem item = colorAndFontTable.getItem(ITEM_FOREGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}

	/**
	 * Set the fixed item height for the CTabFolder
	 *
	 * @param height new fixed header height
	 */
	void setTabHeight(int height) {
		tabFolder1.setTabHeight(height);
	}
}
