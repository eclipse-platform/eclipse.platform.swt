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

class CTabFolderTab extends Tab {
	int lastSelectedTab = 0;
	
	/* Example widgets and groups that contain them */
	CTabFolder tabFolder1;
	Group tabFolderGroup, itemGroup;
	
	/* Style widgets added to the "Style" group */
	Button topButton, bottomButton, flatButton, closeButton;

	static String [] CTabItems1 = {ControlExample.getResourceString("CTabItem1_0"),
								  ControlExample.getResourceString("CTabItem1_1"),
								  ControlExample.getResourceString("CTabItem1_2")};

	/* Controls and resources added to the "Fonts" group */
	Button itemFontButton;
	Font itemFont;
	
	/* Other widgets added to the "Other" group */
	Button setSimpleTabButton, setImageButton;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	CTabFolderTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates the "Fonts" group.
	 */
	void createColorGroup () {
		super.createColorGroup();
		
		itemGroup = new Group (colorGroup, SWT.NONE);
		itemGroup.setText (ControlExample.getResourceString ("CTab_Item_Colors"));
		GridData data = new GridData ();
		data.horizontalSpan = 2;
		itemGroup.setLayoutData (data);
		itemGroup.setLayout (new GridLayout (2, false));
		itemFontButton = new Button (itemGroup, SWT.PUSH);
		itemFontButton.setText(ControlExample.getResourceString("Font"));
		itemFontButton.setLayoutData(new GridData (GridData.HORIZONTAL_ALIGN_FILL));
		
		Shell shell = colorGroup.getShell ();
		final FontDialog fontDialog = new FontDialog (shell);

		/* Add listeners to set the colors and font */
		itemFontButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Font oldFont = itemFont;
				if (oldFont == null) oldFont = tabFolder1.getItem (0).getFont ();
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
				if (itemFont != null) itemFont.dispose();
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
		setSimpleTabButton = new Button (otherGroup, SWT.CHECK);
		setSimpleTabButton.setText (ControlExample.getResourceString("Set_Simple_Tabs"));
		setSimpleTabButton.setSelection(true);
		setImageButton = new Button (otherGroup, SWT.CHECK);
		setImageButton.setText (ControlExample.getResourceString("Set_Image"));
	
		/* Add the listeners */
		setSimpleTabButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setSimpleTabs();
			}
		});
		setImageButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setImages();
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
		tabFolderGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
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
			Text text = new Text(tabFolder1, SWT.NONE);
			text.setText(ControlExample.getResourceString("CTabItem_content") + ": " + i);
			item.setControl(text);
		}
		tabFolder1.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				lastSelectedTab = tabFolder1.getSelectionIndex();
			}
		});
		tabFolder1.setSelection(lastSelectedTab);
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
		flatButton.setEnabled(false);
		closeButton = new Button (styleGroup, SWT.CHECK);
		closeButton.setText ("SWT.CLOSE");
	
		/* Add the listeners */
		SelectionListener selectionListener = new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				if ((event.widget.getStyle() & SWT.RADIO) != 0) {
					if (!((Button) event.widget).getSelection ()) return;
				}
				recreateExampleWidgets ();
			};
		};
		topButton.addSelectionListener (selectionListener);
		bottomButton.addSelectionListener (selectionListener);
		borderButton.addSelectionListener (selectionListener);
		flatButton.addSelectionListener (selectionListener);
		closeButton.addSelectionListener (selectionListener);
		borderButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				flatButton.setEnabled(borderButton.getSelection());
			}
		});
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
	Control [] getExampleWidgets () {
		return new Control [] {tabFolder1};
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
			tabFolder1.addCTabFolderListener (new CTabFolderAdapter () {
				public void itemClosed (CTabFolderEvent event) {
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
		setSimpleTabs ();
		setImages ();
		setItemFont ();
		setExampleWidgetSize ();
	}
	
	/**
	 * Sets the header visible state of the "Example" widgets.
	 */
	void setSimpleTabs () {
		tabFolder1.setSimpleTab (setSimpleTabButton.getSelection ());
	}
	
	/**
	 * Sets an image into each item of the "Example" widgets.
	 */
	void setImages () {
		boolean setImage = setImageButton.getSelection ();
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
	 * Sets the font of CTabItem 0.
	 */
	void setItemFont () {
		if (instance.startup) return;
		tabFolder1.getItem (0).setFont (itemFont);
	}
}
