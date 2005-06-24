/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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

	static String [] CTabItems1 = {ControlExample.getResourceString("CTabItem1_0"),
								  ControlExample.getResourceString("CTabItem1_1"),
								  ControlExample.getResourceString("CTabItem1_2")};

	/* Controls and resources added to the "Fonts" group */
	Button foregroundSelectionButton, backgroundSelectionButton, itemFontButton;
	Image foregroundSelectionImage, backgroundSelectionImage;
	Color foregroundSelectionColor, backgroundSelectionColor;
	Font itemFont;
	
	/* Other widgets added to the "Other" group */
	Button simpleTabButton, singleTabButton, imageButton, showMinButton, showMaxButton, unselectedCloseButton, unselectedImageButton;

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
		/* Create the group */
		colorGroup = new Group(controlGroup, SWT.NONE);
		colorGroup.setLayout (new GridLayout (2, false));
		colorGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		colorGroup.setText (ControlExample.getResourceString ("Colors"));
		
		new Label (colorGroup, SWT.NONE).setText (ControlExample.getResourceString ("Foreground_Color"));
		foregroundButton = new Button (colorGroup, SWT.PUSH);
		
		new Label (colorGroup, SWT.NONE).setText (ControlExample.getResourceString ("Background_Color"));
		backgroundButton = new Button (colorGroup, SWT.PUSH);
		
		new Label (colorGroup, SWT.NONE).setText (ControlExample.getResourceString ("Selection_Foreground_Color"));
		foregroundSelectionButton = new Button (colorGroup, SWT.PUSH);
		
		new Label (colorGroup, SWT.NONE).setText (ControlExample.getResourceString ("Selection_Background_Color"));
		backgroundSelectionButton = new Button (colorGroup, SWT.PUSH);
		
		fontButton = new Button (colorGroup, SWT.PUSH);
		fontButton.setText(ControlExample.getResourceString("Font"));
		fontButton.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false, 2, 1));
	
		itemFontButton = new Button (colorGroup, SWT.PUSH);
		itemFontButton.setText(ControlExample.getResourceString("Item_Font"));
		itemFontButton.setLayoutData(new GridData (SWT.FILL, SWT.CENTER, false, false, 2, 1));
		
		Button defaultsButton = new Button (colorGroup, SWT.PUSH);
		defaultsButton.setText(ControlExample.getResourceString("Defaults"));

		Shell shell = controlGroup.getShell ();
		final ColorDialog colorDialog = new ColorDialog (shell);
		final FontDialog fontDialog = new FontDialog (shell);

		/* Create images to display current colors */
		int imageSize = 12;
		Display display = shell.getDisplay ();
		foregroundImage = new Image (display, imageSize, imageSize);
		backgroundImage = new Image (display, imageSize, imageSize);
		foregroundSelectionImage = new Image (display, imageSize, imageSize);
		backgroundSelectionImage = new Image (display, imageSize, imageSize);

		/* Add listeners to set the colors and font */
		foregroundButton.setImage(foregroundImage); // sets the size of the button
		foregroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = foregroundColor;
				if (oldColor == null) {
					Control [] controls = getExampleWidgets ();
					if (controls.length > 0) oldColor = controls [0].getForeground ();
				}
				if (oldColor != null) colorDialog.setRGB(oldColor.getRGB()); // seed dialog with current color
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = foregroundColor; // save old foreground color to dispose when done
				foregroundColor = new Color (event.display, rgb);
				setExampleWidgetForeground ();
				if (oldColor != null) oldColor.dispose ();
			}
		});
		backgroundButton.setImage(backgroundImage); // sets the size of the button
		backgroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = backgroundColor;
				if (oldColor == null) {
					Control [] controls = getExampleWidgets ();
					if (controls.length > 0) oldColor = controls [0].getBackground (); // seed dialog with current color
				}
				if (oldColor != null) colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = backgroundColor; // save old background color to dispose when done
				backgroundColor = new Color (event.display, rgb);
				setExampleWidgetBackground ();
				if (oldColor != null) oldColor.dispose ();
			}
		});
		foregroundSelectionButton.setImage(foregroundSelectionImage); // sets the size of the button
		foregroundSelectionButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = foregroundSelectionColor;
				if (oldColor == null) {
					Control [] controls = getExampleWidgets ();
					if (controls.length > 0) oldColor = controls [0].getForeground ();
				}
				if (oldColor != null) colorDialog.setRGB(oldColor.getRGB()); // seed dialog with current color
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = foregroundSelectionColor; // save old foreground color to dispose when done
				foregroundSelectionColor = new Color (event.display, rgb);
				setExampleWidgetForeground ();
				if (oldColor != null) oldColor.dispose ();
			}
		});
		backgroundSelectionButton.setImage(backgroundSelectionImage); // sets the size of the button
		backgroundSelectionButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = backgroundSelectionColor;
				if (oldColor == null) {
					Control [] controls = getExampleWidgets ();
					if (controls.length > 0) oldColor = controls [0].getBackground (); // seed dialog with current color
				}
				if (oldColor != null) colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = backgroundSelectionColor; // save old background color to dispose when done
				backgroundSelectionColor = new Color (event.display, rgb);
				setExampleWidgetBackground ();
				if (oldColor != null) oldColor.dispose ();
			}
		});
		fontButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				Font oldFont = font;
				if (oldFont == null) {
					Control [] controls = getExampleWidgets ();
					if (controls.length > 0) oldFont = controls [0].getFont ();
				}
				if (oldFont != null) fontDialog.setFontList(oldFont.getFontData()); // seed dialog with current font
				FontData fontData = fontDialog.open ();
				if (fontData == null) return;
				oldFont = font; // dispose old font when done
				font = new Font (event.display, fontData);
				setExampleWidgetFont ();
				setExampleWidgetSize ();
				if (oldFont != null) oldFont.dispose ();
			}
		});
	

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
		
		defaultsButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				resetColorsAndFonts ();
			}
		});
		
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (foregroundImage != null) foregroundImage.dispose();
				if (backgroundImage != null) backgroundImage.dispose();
				if (foregroundColor != null) foregroundColor.dispose();
				if (backgroundColor != null) backgroundColor.dispose();
				if (font != null) font.dispose();
				foregroundColor = null;
				backgroundColor = null;
				font = null;				
				if (foregroundSelectionImage != null) foregroundSelectionImage.dispose();
				if (backgroundSelectionImage != null) backgroundSelectionImage.dispose();
				if (foregroundSelectionColor != null) foregroundSelectionColor.dispose();
				if (backgroundSelectionColor != null) backgroundSelectionColor.dispose();
				foregroundSelectionColor = null;
				backgroundSelectionColor = null;
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
			Text text = new Text(tabFolder1, SWT.READ_ONLY);
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
		closeButton = new Button (styleGroup, SWT.CHECK);
		closeButton.setText ("SWT.CLOSE");
	
		/* Add the listeners */
		SelectionListener selectionListener = new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				if ((event.widget.getStyle() & SWT.RADIO) != 0) {
					if (!((Button) event.widget).getSelection ()) return;
				}
				recreateExampleWidgets ();
			}
		};
		topButton.addSelectionListener (selectionListener);
		bottomButton.addSelectionListener (selectionListener);
		borderButton.addSelectionListener (selectionListener);
		flatButton.addSelectionListener (selectionListener);
		closeButton.addSelectionListener (selectionListener);
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
		Color oldColor = foregroundSelectionColor;
		foregroundSelectionColor = null;
		if (oldColor != null) oldColor.dispose();
		oldColor = backgroundSelectionColor;
		backgroundSelectionColor = null;
		if (oldColor != null) oldColor.dispose();
		Font oldFont = itemFont;
		itemFont = null;
		if (oldFont != null) oldFont.dispose();
		super.resetColorsAndFonts ();
	}
	
	void setExampleWidgetForeground () {
		if (foregroundSelectionButton == null || tabFolder1 == null) return;
		tabFolder1.setSelectionForeground(foregroundSelectionColor);
		// Set the foreground button's color to match the color just set.
		Color color = foregroundSelectionColor;
		if (color == null) color = tabFolder1.getSelectionForeground ();
		drawImage (foregroundSelectionImage, color);
		foregroundSelectionButton.setImage (foregroundSelectionImage);
		super.setExampleWidgetForeground();
	}
	
	void setExampleWidgetBackground () {
		if (backgroundSelectionButton == null || tabFolder1 == null) return;
		tabFolder1.setSelectionBackground(backgroundSelectionColor);
		// Set the background button's color to match the color just set.
		Color color = backgroundSelectionColor;
		if (color == null) color = tabFolder1.getSelectionBackground ();
		drawImage (backgroundSelectionImage, color);
		backgroundSelectionButton.setImage (backgroundSelectionImage);
		super.setExampleWidgetBackground();
	}
	void setExampleWidgetFont () {
		if (instance.startup) return;
		if (itemFontButton == null) return; // no font button on this tab
		CTabItem[] items = tabFolder1.getItems();
		if (items.length > 0) {
			items[0].setFont(itemFont);
		}
		super.setExampleWidgetFont();
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
	 * Sets the font of CTabItem 0.
	 */
	void setItemFont () {
		if (instance.startup) return;
		tabFolder1.getItem (0).setFont (itemFont);
		setExampleWidgetSize();
	}
}
