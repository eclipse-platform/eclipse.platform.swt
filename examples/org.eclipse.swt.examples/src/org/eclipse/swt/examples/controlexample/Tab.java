/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

/**
 * <code>Tab</code> is the abstract superclass of every page
 * in the example's tab folder.  Each page in the tab folder
 * describes a control.
 *
 * A Tab itself is not a control but instead provides a
 * hierarchy with which to share code that is common to
 * every page in the folder.
 *
 * A typical page in a Tab contains a two column composite.
 * The left column contains the "Example" group.  The right
 * column contains "Control" group.  The "Control" group
 * contains controls that allow the user to interact with
 * the example control.  The "Control" group typically
 * contains a "Style", "Other" and "Size" group.  Subclasses
 * can override these defaults to augment a group or stop
 * a group from being created.
 */
abstract class Tab {
	Shell shell;
	Display display;
	
	/* Common control buttons */
	Button borderButton, enabledButton, visibleButton, backgroundImageButton, popupMenuButton;
	Button preferredButton, tooSmallButton, smallButton, largeButton, fillHButton, fillVButton;

	/* Common groups and composites */
	Composite tabFolderPage;
	Group exampleGroup, controlGroup, listenersGroup, otherGroup, sizeGroup, styleGroup, colorGroup, backgroundModeGroup;

	/* Controlling instance */
	final ControlExample instance;

	/* Sizing constants for the "Size" group */
	static final int TOO_SMALL_SIZE	= 10;
	static final int SMALL_SIZE		= 50;
	static final int LARGE_SIZE		= 100;
	
	/* Right-to-left support */
	static final boolean RTL_SUPPORT_ENABLE = "win32".equals(SWT.getPlatform()) || "gtk".equals(SWT.getPlatform());
	Group orientationGroup;
	Button rtlButton, ltrButton, defaultOrietationButton;

	/* Controls and resources for the "Colors & Fonts" group */
	static final int IMAGE_SIZE = 12;
	static final int FOREGROUND_COLOR = 0;
	static final int BACKGROUND_COLOR = 1;
	static final int FONT = 2;
	Table colorAndFontTable;
	ColorDialog colorDialog;
	FontDialog fontDialog;
	Color foregroundColor, backgroundColor;
	Font font;
	
	/* Controls and resources for the "Background Mode" group */
	Combo backgroundModeCombo;
	Button backgroundModeImageButton, backgroundModeColorButton;

	boolean samplePopup = false;
	
	/* Set/Get API controls */
	Combo nameCombo;
	Label returnTypeLabel;
	Button getButton, setButton;
	Text setText, getText;
	Shell setGetDialog;

	/* Event logging variables and controls */
	Text eventConsole;
	boolean logging = false;
	boolean [] eventsFilter;
	int setFieldsMask = 0;
	Event setFieldsEvent = new Event ();
	boolean ignore = false;
	
	/* Event logging constants */
	static final int DOIT	= 0x0100;
	static final int DETAIL	= 0x0200;
	static final int TEXT	= 0x0400;
	static final int X		= 0x0800;
	static final int Y		= 0x1000;
	static final int WIDTH	= 0x2000;
	static final int HEIGHT	= 0x4000;

	static final int DETAIL_IME			= 0;
	static final int DETAIL_ERASE_ITEM	= 1;
	static final int DETAIL_TRAVERSE	= 2;

	static class EventInfo {
		String name;
		int type;
		int settableFields;
		int setFields;
		Event event;
		EventInfo (String name, int type, int settableFields, int setFields, Event event) {
			this.name = name;
			this.type = type;
			this.settableFields = settableFields;
			this.setFields = setFields;
			this.event = event;
		}
	}
	
	final EventInfo [] EVENT_INFO = {
		new EventInfo ("Activate", SWT.Activate, 0, 0, new Event()), 
		new EventInfo ("Arm", SWT.Arm, 0, 0, new Event()), 
		new EventInfo ("Close", SWT.Close, DOIT, 0, new Event()),
		new EventInfo ("Collapse", SWT.Collapse, 0, 0, new Event()),
		new EventInfo ("Deactivate", SWT.Deactivate, 0, 0, new Event()),
		new EventInfo ("DefaultSelection", SWT.DefaultSelection, 0, 0, new Event()),
		new EventInfo ("Deiconify", SWT.Deiconify, 0, 0, new Event()), 
		new EventInfo ("Dispose", SWT.Dispose, 0, 0, new Event()),
		new EventInfo ("DragDetect", SWT.DragDetect, 0, 0, new Event()), 
		new EventInfo ("EraseItem", SWT.EraseItem, DETAIL | DETAIL_ERASE_ITEM, 0, new Event()),
		new EventInfo ("Expand", SWT.Expand, 0, 0, new Event()), 
		new EventInfo ("FocusIn", SWT.FocusIn, 0, 0, new Event()), 
		new EventInfo ("FocusOut", SWT.FocusOut, 0, 0, new Event()),
		new EventInfo ("HardKeyDown", SWT.HardKeyDown, 0, 0, new Event()), 
		new EventInfo ("HardKeyUp", SWT.HardKeyUp, 0, 0, new Event()),
		new EventInfo ("Help", SWT.Help, 0, 0, new Event()), 
		new EventInfo ("Hide", SWT.Hide, 0, 0, new Event()),
		new EventInfo ("Iconify", SWT.Iconify, 0, 0, new Event()), 
		new EventInfo ("KeyDown", SWT.KeyDown, DOIT, 0, new Event()),
		new EventInfo ("KeyUp", SWT.KeyUp, DOIT, 0, new Event()),
		new EventInfo ("MeasureItem", SWT.MeasureItem, 0, 0, new Event()),
		new EventInfo ("MenuDetect", SWT.MenuDetect, X | Y | DOIT, 0, new Event()),
		new EventInfo ("Modify", SWT.Modify, 0, 0, new Event()), 
		new EventInfo ("MouseDoubleClick", SWT.MouseDoubleClick, 0, 0, new Event()),
		new EventInfo ("MouseDown", SWT.MouseDown, 0, 0, new Event()), 
		new EventInfo ("MouseEnter", SWT.MouseEnter, 0, 0, new Event()), 
		new EventInfo ("MouseExit", SWT.MouseExit, 0, 0, new Event()), 
		new EventInfo ("MouseHover", SWT.MouseHover, 0, 0, new Event()),
		new EventInfo ("MouseMove", SWT.MouseMove, 0, 0, new Event()), 
		new EventInfo ("MouseUp", SWT.MouseUp, 0, 0, new Event()), 
		new EventInfo ("MouseWheel", SWT.MouseWheel, 0, 0, new Event()),
		new EventInfo ("Move", SWT.Move, 0, 0, new Event()), 
		new EventInfo ("Paint", SWT.Paint, 0, 0, new Event()), 
		new EventInfo ("PaintItem", SWT.PaintItem, 0, 0, new Event()),
		new EventInfo ("Resize", SWT.Resize, 0, 0, new Event()), 
		new EventInfo ("Selection", SWT.Selection, X | Y | DOIT, 0, new Event()), // sash
		new EventInfo ("SetData", SWT.SetData, 0, 0, new Event()),
//		new EventInfo ("Settings", SWT.Settings, 0, 0, new Event()),  // note: this event only goes to Display
		new EventInfo ("Show", SWT.Show, 0, 0, new Event()), 
		new EventInfo ("Traverse", SWT.Traverse, DETAIL | DETAIL_TRAVERSE | DOIT, 0, new Event()),
		new EventInfo ("Verify", SWT.Verify, TEXT | DOIT, 0, new Event()),
		new EventInfo ("ImeComposition", SWT.ImeComposition, DETAIL | DETAIL_IME | TEXT | DOIT, 0, new Event()),
	};

	static final String [][] DETAIL_CONSTANTS = {
		{ // DETAIL_IME = 0
			"SWT.COMPOSITION_CHANGED",
			"SWT.COMPOSITION_OFFSET",
			"SWT.COMPOSITION_SELECTION",
		},
		{ // DETAIL_ERASE_ITEM = 1
			"SWT.SELECTED",
			"SWT.FOCUSED",
			"SWT.BACKGROUND",
			"SWT.FOREGROUND",
			"SWT.HOT",
		},
		{ // DETAIL_TRAVERSE = 2
			"SWT.TRAVERSE_NONE",
			"SWT.TRAVERSE_ESCAPE",
			"SWT.TRAVERSE_RETURN",
			"SWT.TRAVERSE_TAB_PREVIOUS",
			"SWT.TRAVERSE_TAB_NEXT",
			"SWT.TRAVERSE_ARROW_PREVIOUS",
			"SWT.TRAVERSE_ARROW_NEXT",
			"SWT.TRAVERSE_MNEMONIC",
			"SWT.TRAVERSE_PAGE_PREVIOUS",
			"SWT.TRAVERSE_PAGE_NEXT",
		},
	};

	static final Object [] DETAIL_VALUES = {
		"SWT.COMPOSITION_CHANGED", new Integer(SWT.COMPOSITION_CHANGED),
		"SWT.COMPOSITION_OFFSET", new Integer(SWT.COMPOSITION_OFFSET),
		"SWT.COMPOSITION_SELECTION", new Integer(SWT.COMPOSITION_SELECTION),
		"SWT.SELECTED", new Integer(SWT.SELECTED),
		"SWT.FOCUSED", new Integer(SWT.FOCUSED),
		"SWT.BACKGROUND", new Integer(SWT.BACKGROUND),
		"SWT.FOREGROUND", new Integer(SWT.FOREGROUND),
		"SWT.HOT", new Integer(SWT.HOT),
		"SWT.TRAVERSE_NONE", new Integer(SWT.TRAVERSE_NONE),
		"SWT.TRAVERSE_ESCAPE", new Integer(SWT.TRAVERSE_ESCAPE),
		"SWT.TRAVERSE_RETURN", new Integer(SWT.TRAVERSE_RETURN),
		"SWT.TRAVERSE_TAB_PREVIOUS", new Integer(SWT.TRAVERSE_TAB_PREVIOUS),
		"SWT.TRAVERSE_TAB_NEXT", new Integer(SWT.TRAVERSE_TAB_NEXT),
		"SWT.TRAVERSE_ARROW_PREVIOUS", new Integer(SWT.TRAVERSE_ARROW_PREVIOUS),
		"SWT.TRAVERSE_ARROW_NEXT", new Integer(SWT.TRAVERSE_ARROW_NEXT),
		"SWT.TRAVERSE_MNEMONIC", new Integer(SWT.TRAVERSE_MNEMONIC),
		"SWT.TRAVERSE_PAGE_PREVIOUS", new Integer(SWT.TRAVERSE_PAGE_PREVIOUS),
		"SWT.TRAVERSE_PAGE_NEXT", new Integer(SWT.TRAVERSE_PAGE_NEXT),
	};
		
	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	Tab(ControlExample instance) {
		this.instance = instance;
	}

	/**
	 * Creates the "Control" group.  The "Control" group
	 * is typically the right hand column in the tab.
	 */
	void createControlGroup () {
	
		/*
		 * Create the "Control" group.  This is the group on the
		 * right half of each example tab.  It consists of the
		 * "Style" group, the "Other" group and the "Size" group.
		 */	
		controlGroup = new Group (tabFolderPage, SWT.NONE);
		controlGroup.setLayout (new GridLayout (2, true));
		controlGroup.setLayoutData (new GridData(SWT.FILL, SWT.FILL, false, false));
		controlGroup.setText (ControlExample.getResourceString("Parameters"));
	
		/* Create individual groups inside the "Control" group */
		createStyleGroup ();
		createOtherGroup ();
		createSetGetGroup();
		createSizeGroup ();
		createColorAndFontGroup ();
		if (rtlSupport()) {
			createOrientationGroup ();
		}
		createBackgroundModeGroup ();
	
		/*
		 * For each Button child in the style group, add a selection
		 * listener that will recreate the example controls.  If the
		 * style group button is a RADIO button, ensure that the radio
		 * button is selected before recreating the example controls.
		 * When the user selects a RADIO button, the current RADIO
		 * button in the group is deselected and the new RADIO button
		 * is selected automatically.  The listeners are notified for
		 * both these operations but typically only do work when a RADIO
		 * button is selected.
		 */
		SelectionListener selectionListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				if ((event.widget.getStyle () & SWT.RADIO) != 0) {
					if (!((Button) event.widget).getSelection ()) return;
				}
				recreateExampleWidgets ();
			}
		};
		Control [] children = styleGroup.getChildren ();
		for (int i=0; i<children.length; i++) {
			if (children [i] instanceof Button) {
				Button button = (Button) children [i];
				button.addSelectionListener (selectionListener);
			} else {
				if (children [i] instanceof Composite) {
					/* Look down one more level of children in the style group. */
					Composite composite = (Composite) children [i];
					Control [] grandchildren = composite.getChildren ();
					for (int j=0; j<grandchildren.length; j++) {
						if (grandchildren [j] instanceof Button) {
							Button button = (Button) grandchildren [j];
							button.addSelectionListener (selectionListener);
						}
					}
				}
			}
		}
		if (rtlSupport()) {
			rtlButton.addSelectionListener (selectionListener); 
			ltrButton.addSelectionListener (selectionListener);		
			defaultOrietationButton.addSelectionListener (selectionListener);
		}
	}
	
	/**
	 * Append the Set/Get API controls to the "Other" group.
	 */
	void createSetGetGroup() {
		/*
		 * Create the button to access set/get API functionality.
		 */
		final String [] methodNames = getMethodNames ();
		if (methodNames != null) {
			final Button setGetButton = new Button (otherGroup, SWT.PUSH);
			setGetButton.setText (ControlExample.getResourceString ("Set_Get"));
			setGetButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			setGetButton.addSelectionListener (new SelectionAdapter() {
				public void widgetSelected (SelectionEvent e) {
					if (getExampleWidgets().length >  0) {
						if (setGetDialog == null) {
							setGetDialog = createSetGetDialog(methodNames);
						}
						Point pt = setGetButton.getLocation();
						pt = display.map(setGetButton.getParent(), null, pt);
						setGetDialog.setLocation(pt.x, pt.y);
						setGetDialog.open();
					}
				}
			});
		}
	}

	/**
	 * Creates the "Control" widget children.
	 * Subclasses override this method to augment
	 * the standard controls created in the "Style",
	 * "Other" and "Size" groups.
	 */
	void createControlWidgets () {
	}
	
	/**
	 * Creates the "Colors and Fonts" group. This is typically
	 * a child of the "Control" group. Subclasses override
	 * this method to customize color and font settings.
	 */
	void createColorAndFontGroup () {
		/* Create the group. */
		colorGroup = new Group(controlGroup, SWT.NONE);
		colorGroup.setLayout (new GridLayout (2, true));
		colorGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		colorGroup.setText (ControlExample.getResourceString ("Colors"));
		colorAndFontTable = new Table(colorGroup, SWT.BORDER | SWT.V_SCROLL);
		colorAndFontTable.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false, 2, 1));
		TableItem item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Foreground_Color"));
		colorAndFontTable.setSelection(0);
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Background_Color"));
		item = new TableItem(colorAndFontTable, SWT.None);
		item.setText(ControlExample.getResourceString ("Font"));
		Button changeButton = new Button (colorGroup, SWT.PUSH);
		changeButton.setText(ControlExample.getResourceString("Change"));
		changeButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		Button defaultsButton = new Button (colorGroup, SWT.PUSH);
		defaultsButton.setText(ControlExample.getResourceString("Defaults"));
		defaultsButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));

		/* Add listeners to set/reset colors and fonts. */
		colorDialog = new ColorDialog (shell);
		fontDialog = new FontDialog (shell);
		colorAndFontTable.addSelectionListener(new SelectionAdapter() {
			public void widgetDefaultSelected(SelectionEvent event) {
				changeFontOrColor (colorAndFontTable.getSelectionIndex());
			}
		});
		changeButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				changeFontOrColor (colorAndFontTable.getSelectionIndex());
			}
		});
		defaultsButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				resetColorsAndFonts ();
			}
		});
		shell.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (foregroundColor != null) foregroundColor.dispose();
				if (backgroundColor != null) backgroundColor.dispose();
				if (font != null) font.dispose();
				foregroundColor = null;
				backgroundColor = null;
				font = null;
				if (colorAndFontTable != null && !colorAndFontTable.isDisposed()) {
					TableItem [] items = colorAndFontTable.getItems();
					for (int i = 0; i < items.length; i++) {
						Image image = items[i].getImage();
						if (image != null) image.dispose();
					}
				}
			}
		});
	}
	
	void changeFontOrColor(int index) {
		switch (index) {
			case FOREGROUND_COLOR: {
				Color oldColor = foregroundColor;
				if (oldColor == null) {
					Control [] controls = getExampleControls ();
					if (controls.length > 0) oldColor = controls [0].getForeground ();
				}
				if (oldColor != null) colorDialog.setRGB(oldColor.getRGB()); // seed dialog with current color
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = foregroundColor; // save old foreground color to dispose when done
				foregroundColor = new Color (display, rgb);
				setExampleWidgetForeground ();
				if (oldColor != null) oldColor.dispose ();
			}
			break;
			case BACKGROUND_COLOR: {
				Color oldColor = backgroundColor;
				if (oldColor == null) {
					Control [] controls = getExampleControls ();
					if (controls.length > 0) oldColor = controls [0].getBackground (); // seed dialog with current color
				}
				if (oldColor != null) colorDialog.setRGB(oldColor.getRGB());
				RGB rgb = colorDialog.open();
				if (rgb == null) return;
				oldColor = backgroundColor; // save old background color to dispose when done
				backgroundColor = new Color (display, rgb);
				setExampleWidgetBackground ();
				if (oldColor != null) oldColor.dispose ();
			}
			break;
			case FONT: {
				Font oldFont = font;
				if (oldFont == null) {
					Control [] controls = getExampleControls ();
					if (controls.length > 0) oldFont = controls [0].getFont ();
				}
				if (oldFont != null) fontDialog.setFontList(oldFont.getFontData()); // seed dialog with current font
				FontData fontData = fontDialog.open ();
				if (fontData == null) return;
				oldFont = font; // dispose old font when done
				font = new Font (display, fontData);
				setExampleWidgetFont ();
				setExampleWidgetSize ();
				if (oldFont != null) oldFont.dispose ();
			}
			break;
		}
	}

	/**
	 * Creates the "Other" group.  This is typically
	 * a child of the "Control" group.
	 */
	void createOtherGroup () {
		/* Create the group */
		otherGroup = new Group (controlGroup, SWT.NONE);
		otherGroup.setLayout (new GridLayout ());
		otherGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		otherGroup.setText (ControlExample.getResourceString("Other"));
	
		/* Create the controls */
		enabledButton = new Button(otherGroup, SWT.CHECK);
		enabledButton.setText(ControlExample.getResourceString("Enabled"));
		visibleButton = new Button(otherGroup, SWT.CHECK);
		visibleButton.setText(ControlExample.getResourceString("Visible"));
		backgroundImageButton = new Button(otherGroup, SWT.CHECK);
		backgroundImageButton.setText(ControlExample.getResourceString("BackgroundImage"));
		popupMenuButton = new Button(otherGroup, SWT.CHECK);
		popupMenuButton.setText(ControlExample.getResourceString("PopupMenu"));
		
		/* Add the listeners */
		enabledButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleWidgetEnabled ();
			}
		});
		visibleButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleWidgetVisibility ();
			}
		});
		backgroundImageButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleWidgetBackgroundImage ();
			}
		});
		popupMenuButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleWidgetPopupMenu ();
			}
		});
	
		/* Set the default state */
		enabledButton.setSelection(true);
		visibleButton.setSelection(true);
		backgroundImageButton.setSelection(false);
		popupMenuButton.setSelection(false);
	}
	
	/**
	 * Creates the "Background Mode" group.
	 */
	void createBackgroundModeGroup () {
		/* Create the group */
		backgroundModeGroup = new Group (controlGroup, SWT.NONE);
		backgroundModeGroup.setLayout (new GridLayout ());
		backgroundModeGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		backgroundModeGroup.setText (ControlExample.getResourceString("Background_Mode"));
	
		/* Create the controls */
		backgroundModeCombo = new Combo(backgroundModeGroup, SWT.READ_ONLY);
		backgroundModeCombo.setItems(new String[] {"SWT.INHERIT_NONE", "SWT.INHERIT_DEFAULT", "SWT.INHERIT_FORCE"});
		backgroundModeImageButton = new Button(backgroundModeGroup, SWT.CHECK);
		backgroundModeImageButton.setText(ControlExample.getResourceString("BackgroundImage"));
		backgroundModeColorButton = new Button(backgroundModeGroup, SWT.CHECK);
		backgroundModeColorButton.setText(ControlExample.getResourceString("BackgroundColor"));
	
		/* Add the listeners */
		backgroundModeCombo.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleGroupBackgroundMode ();
			}
		});
		backgroundModeImageButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleGroupBackgroundImage ();
			}
		});
		backgroundModeColorButton.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleGroupBackgroundColor ();
			}
		});
	
		/* Set the default state */
		backgroundModeCombo.setText(backgroundModeCombo.getItem(0));
		backgroundModeImageButton.setSelection(false);
		backgroundModeColorButton.setSelection(false);
	}
	
	void createEditEventDialog(Shell parent, int x, int y, final int index) {
		final Shell dialog = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		dialog.setLayout(new GridLayout());
		dialog.setText(ControlExample.getResourceString ("Edit_Event"));
		Label label = new Label (dialog, SWT.NONE);
		label.setText (ControlExample.getResourceString ("Edit_Event_Fields", new String [] {EVENT_INFO[index].name}));
		
		Group group = new Group (dialog, SWT.NONE);
		group.setLayout(new GridLayout(2, false));
		group.setLayoutData(new GridData (SWT.FILL, SWT.FILL, true, true)); 
		
		final int fields = EVENT_INFO[index].settableFields;
		final int eventType = EVENT_INFO[index].type;
		setFieldsMask = EVENT_INFO[index].setFields;
		setFieldsEvent = EVENT_INFO[index].event;
		
		if ((fields & DOIT) != 0) {
			new Label (group, SWT.NONE).setText ("doit");
			final Combo doitCombo = new Combo (group, SWT.READ_ONLY);
			doitCombo.setItems (new String [] {"", "true", "false"});
			if ((setFieldsMask & DOIT) != 0) doitCombo.setText(Boolean.toString(setFieldsEvent.doit));
			doitCombo.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
			doitCombo.addSelectionListener(new SelectionAdapter () {
				public void widgetSelected(SelectionEvent e) {
					String newValue = doitCombo.getText();
					if (newValue.length() == 0) {
						setFieldsMask &= ~DOIT;
					} else {
						setFieldsEvent.type = eventType;
						setFieldsEvent.doit = newValue.equals("true");
						setFieldsMask |= DOIT;
					}
				}
			});
		}

		if ((fields & DETAIL) != 0) {
			new Label (group, SWT.NONE).setText ("detail");			
			int detailType = fields & 0xFF;
			final Combo detailCombo = new Combo (group, SWT.READ_ONLY);
			detailCombo.setItems (DETAIL_CONSTANTS[detailType]);
			detailCombo.add ("", 0);
			detailCombo.setVisibleItemCount(detailCombo.getItemCount());
			if ((setFieldsMask & DETAIL) != 0) detailCombo.setText (DETAIL_CONSTANTS[detailType][setFieldsEvent.detail]);
			detailCombo.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
			detailCombo.addSelectionListener(new SelectionAdapter () {
				public void widgetSelected(SelectionEvent e) {
					String newValue = detailCombo.getText();
					if (newValue.length() == 0) {
						setFieldsMask &= ~DETAIL;
					} else {
						setFieldsEvent.type = eventType;
						for (int i = 0; i < DETAIL_VALUES.length; i += 2) {
							if (newValue.equals (DETAIL_VALUES [i])) {
								setFieldsEvent.detail = ((Integer) DETAIL_VALUES [i + 1]).intValue();
								break;
							}
						}
						setFieldsMask |= DETAIL;
					}
				}
			});
		}

		if ((fields & TEXT) != 0) {
			new Label (group, SWT.NONE).setText ("text");	
			final Text textText = new Text (group, SWT.BORDER);
			if ((setFieldsMask & TEXT) != 0) textText.setText(setFieldsEvent.text);
			textText.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
			textText.addModifyListener(new ModifyListener () {
				public void modifyText(ModifyEvent e) {
					String newValue = textText.getText();
					if (newValue.length() == 0) {
						setFieldsMask &= ~TEXT;
					} else {
						setFieldsEvent.type = eventType;
						setFieldsEvent.text = newValue;
						setFieldsMask |= TEXT;
					}
				}
			});
		}

		if ((fields & X) != 0) {
			new Label (group, SWT.NONE).setText ("x");	
			final Text xText = new Text (group, SWT.BORDER);
			if ((setFieldsMask & X) != 0) xText.setText(Integer.toString(setFieldsEvent.x));
			xText.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
			xText.addModifyListener(new ModifyListener () {
				public void modifyText(ModifyEvent e) {
					String newValue = xText.getText ();
					try {
						int newIntValue = Integer.parseInt (newValue);
						setFieldsEvent.type = eventType;
						setFieldsEvent.x = newIntValue;
						setFieldsMask |= X;
					} catch (NumberFormatException ex) {
						setFieldsMask &= ~X;
					}
				}
			});
		}

		if ((fields & Y) != 0) {
			new Label (group, SWT.NONE).setText ("y");	
			final Text yText = new Text (group, SWT.BORDER);
			if ((setFieldsMask & Y) != 0) yText.setText(Integer.toString(setFieldsEvent.y));
			yText.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
			yText.addModifyListener(new ModifyListener () {
				public void modifyText(ModifyEvent e) {
					String newValue = yText.getText ();
					try {
						int newIntValue = Integer.parseInt (newValue);
						setFieldsEvent.type = eventType;
						setFieldsEvent.y = newIntValue;
						setFieldsMask |= Y;
					} catch (NumberFormatException ex) {
						setFieldsMask &= ~Y;
					}
				}
			});
		}

		if ((fields & WIDTH) != 0) {
			new Label (group, SWT.NONE).setText ("width");	
			final Text widthText = new Text (group, SWT.BORDER);
			if ((setFieldsMask & WIDTH) != 0) widthText.setText(Integer.toString(setFieldsEvent.width));
			widthText.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
			widthText.addModifyListener(new ModifyListener () {
				public void modifyText(ModifyEvent e) {
					String newValue = widthText.getText ();
					try {
						int newIntValue = Integer.parseInt (newValue);
						setFieldsEvent.type = eventType;
						setFieldsEvent.width = newIntValue;
						setFieldsMask |= WIDTH;
					} catch (NumberFormatException ex) {
						setFieldsMask &= ~WIDTH;
					}
				}
			});
		}

		if ((fields & HEIGHT) != 0) {
			new Label (group, SWT.NONE).setText ("height");	
			final Text heightText = new Text (group, SWT.BORDER);
			if ((setFieldsMask & HEIGHT) != 0) heightText.setText(Integer.toString(setFieldsEvent.height));
			heightText.setLayoutData (new GridData (SWT.FILL, SWT.CENTER, true, false));
			heightText.addModifyListener(new ModifyListener () {
				public void modifyText(ModifyEvent e) {
					String newValue = heightText.getText ();
					try {
						int newIntValue = Integer.parseInt (newValue);
						setFieldsEvent.type = eventType;
						setFieldsEvent.height = newIntValue;
						setFieldsMask |= HEIGHT;
					} catch (NumberFormatException ex) {
						setFieldsMask &= ~HEIGHT;
					}
				}
			});
		}

		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText (ControlExample.getResourceString("OK"));
		GridData data = new GridData (70, SWT.DEFAULT);
		data.horizontalAlignment = SWT.RIGHT;
		ok.setLayoutData (data);
		ok.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				EVENT_INFO[index].setFields = setFieldsMask;
				EVENT_INFO[index].event = setFieldsEvent;
				dialog.dispose();
			}
		});

		dialog.setDefaultButton(ok);
		dialog.pack();
		dialog.setLocation(x, y);
		dialog.open();
	}

	/**
	 * Create the event console popup menu.
	 */
	void createEventConsolePopup () {
		Menu popup = new Menu (shell, SWT.POP_UP);
		eventConsole.setMenu (popup);

		MenuItem cut = new MenuItem (popup, SWT.PUSH);
		cut.setText (ControlExample.getResourceString("MenuItem_Cut"));
		cut.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				eventConsole.cut ();
			}
		});
		MenuItem copy = new MenuItem (popup, SWT.PUSH);
		copy.setText (ControlExample.getResourceString("MenuItem_Copy"));
		copy.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				eventConsole.copy ();
			}
		});
		MenuItem paste = new MenuItem (popup, SWT.PUSH);
		paste.setText (ControlExample.getResourceString("MenuItem_Paste"));
		paste.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				eventConsole.paste ();
			}
		});
		new MenuItem (popup, SWT.SEPARATOR);
		MenuItem selectAll = new MenuItem (popup, SWT.PUSH);
		selectAll.setText(ControlExample.getResourceString("MenuItem_SelectAll"));
		selectAll.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				eventConsole.selectAll ();
			}
		});
	}

	/**
	 * Creates the "Example" group.  The "Example" group
	 * is typically the left hand column in the tab.
	 */
	void createExampleGroup () {
		exampleGroup = new Group (tabFolderPage, SWT.NONE);
		exampleGroup.setLayout (new GridLayout ());
		exampleGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
	}
	
	/**
	 * Creates the "Example" widget children of the "Example" group.
	 * Subclasses override this method to create the particular
	 * example control.
	 */
	void createExampleWidgets () {
		/* Do nothing */
	}
	
	/**
	 * Creates and opens the "Listener selection" dialog.
	 */
	void createListenerSelectionDialog () {
		final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.APPLICATION_MODAL);
		dialog.setText (ControlExample.getResourceString ("Select_Listeners"));
		dialog.setLayout (new GridLayout (2, false));
		final Table table = new Table (dialog, SWT.BORDER | SWT.V_SCROLL | SWT.CHECK);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.verticalSpan = 3;
		table.setLayoutData(data);
		for (int i = 0; i < EVENT_INFO.length; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (EVENT_INFO[i].name);
			item.setChecked (eventsFilter[i]);
		}
		final String [] customNames = getCustomEventNames ();
		for (int i = 0; i < customNames.length; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (customNames[i]);
			item.setChecked (eventsFilter[EVENT_INFO.length + i]);
		}
		Button selectAll = new Button (dialog, SWT.PUSH);
		selectAll.setText(ControlExample.getResourceString ("Select_All"));
		selectAll.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		selectAll.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem [] items = table.getItems();
				for (int i = 0; i < EVENT_INFO.length; i++) {
					items[i].setChecked(true);
				}
				for (int i = 0; i < customNames.length; i++) {
					items[EVENT_INFO.length + i].setChecked(true);
				}
			}
		});
		Button deselectAll = new Button (dialog, SWT.PUSH);
		deselectAll.setText(ControlExample.getResourceString ("Deselect_All"));
		deselectAll.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		deselectAll.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem [] items = table.getItems();
				for (int i = 0; i < EVENT_INFO.length; i++) {
					items[i].setChecked(false);
				}
				for (int i = 0; i < customNames.length; i++) {
					items[EVENT_INFO.length + i].setChecked(false);
				}
			}
		});
		final Button editEvent = new Button (dialog, SWT.PUSH);
		editEvent.setText (ControlExample.getResourceString ("Edit_Event"));
		editEvent.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING));
		editEvent.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected (SelectionEvent e) {
				Point pt = editEvent.getLocation();
				pt = e.display.map(editEvent, null, pt);
				int index = table.getSelectionIndex();
				if (getExampleWidgets().length > 0 && index != -1) {
					createEditEventDialog(dialog, pt.x, pt.y, index);
				}
			}
		});
		editEvent.setEnabled(false);
		table.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				int fields = 0;
				int index = table.getSelectionIndex();
				if (index != -1 && index < EVENT_INFO.length) {  // TODO: Allow custom widgets to specify event info
					fields = (EVENT_INFO[index].settableFields);
				}
				editEvent.setEnabled(fields != 0);
			}
			public void widgetDefaultSelected(SelectionEvent e) {
				if (editEvent.getEnabled()) {
					Point pt = editEvent.getLocation();
					pt = e.display.map(editEvent, null, pt);
					int index = table.getSelectionIndex();
					if (getExampleWidgets().length > 0 && index != -1 && index < EVENT_INFO.length) {
						createEditEventDialog(dialog, pt.x, pt.y, index);
					}
				}
			}
		});

		new Label(dialog, SWT.NONE); /* Filler */
		Button ok = new Button (dialog, SWT.PUSH);
		ok.setText(ControlExample.getResourceString ("OK"));
		dialog.setDefaultButton(ok);
		ok.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		ok.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem [] items = table.getItems();
				for (int i = 0; i < EVENT_INFO.length; i++) {
					eventsFilter[i] = items[i].getChecked();
				}
				for (int i = 0; i < customNames.length; i++) {
					eventsFilter[EVENT_INFO.length + i] = items[EVENT_INFO.length + i].getChecked();
				}
				dialog.dispose();
			}
		});
		dialog.pack ();
		/*
		 * If the preferred size of the dialog is too tall for the display,
		 * then reduce the height, so that the vertical scrollbar will appear.
		 */
		Rectangle bounds = dialog.getBounds();
		Rectangle trim = dialog.computeTrim(0, 0, 0, 0);
		Rectangle clientArea = display.getClientArea();
		if (bounds.height > clientArea.height) {
			dialog.setSize(bounds.width, clientArea.height - trim.height);
		}
		dialog.setLocation(bounds.x, clientArea.y);
		dialog.open ();
		while (! dialog.isDisposed()) {
			if (! display.readAndDispatch()) display.sleep();
		}
	}

	/**
	 * Creates the "Listeners" group.  The "Listeners" group
	 * goes below the "Example" and "Control" groups.
	 */
	void createListenersGroup () {
		listenersGroup = new Group (tabFolderPage, SWT.NONE);
		listenersGroup.setLayout (new GridLayout (3, false));
		listenersGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true, 2, 1));
		listenersGroup.setText (ControlExample.getResourceString ("Listeners"));

		/*
		 * Create the button to access the 'Listeners' dialog.
		 */
		Button listenersButton = new Button (listenersGroup, SWT.PUSH);
		listenersButton.setText (ControlExample.getResourceString ("Select_Listeners"));
		listenersButton.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected (SelectionEvent e) {
				createListenerSelectionDialog ();
				recreateExampleWidgets ();
			}
		});
		
		/*
		 * Create the checkbox to add/remove listeners to/from the example widgets.
		 */
		final Button listenCheckbox = new Button (listenersGroup, SWT.CHECK);
		listenCheckbox.setText (ControlExample.getResourceString ("Listen"));
		listenCheckbox.addSelectionListener (new SelectionAdapter () {
			public void widgetSelected(SelectionEvent e) {
				logging = listenCheckbox.getSelection ();
				recreateExampleWidgets ();
			}
		});

		/*
		 * Create the button to clear the text.
		 */
		Button clearButton = new Button (listenersGroup, SWT.PUSH);
		clearButton.setText (ControlExample.getResourceString ("Clear"));
		clearButton.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_END));
		clearButton.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected (SelectionEvent e) {
				eventConsole.setText ("");
			}
		});
		
		/* Initialize the eventsFilter to log all events. */
		int customEventCount = getCustomEventNames ().length;
		eventsFilter = new boolean [EVENT_INFO.length + customEventCount];
		for (int i = 0; i < EVENT_INFO.length + customEventCount; i++) {
			eventsFilter [i] = true;
		}

		/* Create the event console Text. */
		eventConsole = new Text (listenersGroup, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData data = new GridData (GridData.FILL_BOTH);
		data.horizontalSpan = 3;
		data.heightHint = 80;
		eventConsole.setLayoutData (data);
		createEventConsolePopup ();
		eventConsole.addKeyListener (new KeyAdapter () {
			public void keyPressed (KeyEvent e) {
				if ((e.keyCode == 'A' || e.keyCode == 'a') && (e.stateMask & SWT.MOD1) != 0) {
					eventConsole.selectAll ();
					e.doit = false;
				}
			}
		});
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return null;
	}

	Shell createSetGetDialog(String[] methodNames) {
		final Shell dialog = new Shell(shell, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MODELESS);
		dialog.setLayout(new GridLayout(2, false));
		dialog.setText(getTabText() + " " + ControlExample.getResourceString ("Set_Get"));
		nameCombo = new Combo(dialog, SWT.READ_ONLY);
		nameCombo.setItems(methodNames);
		nameCombo.setText(methodNames[0]);
		nameCombo.setVisibleItemCount(methodNames.length);
		nameCombo.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		nameCombo.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				resetLabels();
			}
		});
		returnTypeLabel = new Label(dialog, SWT.NONE);
		returnTypeLabel.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
		setButton = new Button(dialog, SWT.PUSH);
		setButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
		setButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				setValue();
				setText.selectAll();
				setText.setFocus();
			}
		});
		setText = new Text(dialog, SWT.SINGLE | SWT.BORDER);
		setText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
		getButton = new Button(dialog, SWT.PUSH);
		getButton.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, false, false));
		getButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				getValue();
			}
		});
		getText = new Text(dialog, SWT.MULTI | SWT.BORDER | SWT.READ_ONLY | SWT.H_SCROLL | SWT.V_SCROLL);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		data.widthHint = 240;
		data.heightHint = 200;
		getText.setLayoutData(data);
		resetLabels();
		dialog.setDefaultButton(setButton);
		dialog.pack();
		dialog.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				setGetDialog = null;
			}
		});
		return dialog;
	}

	void resetLabels() {
		String methodRoot = nameCombo.getText();
		returnTypeLabel.setText(parameterInfo(methodRoot));
		setButton.setText(setMethodName(methodRoot));
		getButton.setText("get" + methodRoot);
		setText.setText("");
		getText.setText("");
		getValue();
		setText.setFocus();
	}

	String setMethodName(String methodRoot) {
		return "set" + methodRoot;
	}

	String parameterInfo(String methodRoot) {
		String typeName = null;
		Class returnType = getReturnType(methodRoot);
		boolean isArray = returnType.isArray();
		if (isArray) {
			typeName = returnType.getComponentType().getName();
		} else {
			typeName = returnType.getName();
		}
		String typeNameString = typeName;
		int index = typeName.lastIndexOf('.');
		if (index != -1 && index+1 < typeName.length()) typeNameString = typeName.substring(index+1);
		String info = ControlExample.getResourceString("Info_" + typeNameString + (isArray ? "A" : ""));
		if (isArray) {
			typeNameString += "[]";
		}
		return ControlExample.getResourceString("Parameter_Info", new Object[] {typeNameString, info});
	}

	void getValue() {
		String methodName = "get" + nameCombo.getText();
		getText.setText("");
		Widget[] widgets = getExampleWidgets();
		for (int i = 0; i < widgets.length; i++) {
			try {
				java.lang.reflect.Method method = widgets[i].getClass().getMethod(methodName, null);
				Object result = method.invoke(widgets[i], null);
				if (result == null) {
					getText.append("null");
				} else if (result.getClass().isArray()) {
					int length = java.lang.reflect.Array.getLength(result);
					if (length == 0) {
						getText.append(result.getClass().getComponentType() + "[0]");
					}
					for (int j = 0; j < length; j++) {
						getText.append(java.lang.reflect.Array.get(result,j).toString() + "\n");
					}
				} else {
					getText.append(result.toString());
				}
			} catch (Exception e) {
				getText.append(e.toString());
			}
			if (i + 1 < widgets.length) {
				getText.append("\n\n");
			}
		}
	}

	Class getReturnType(String methodRoot) {
		Class returnType = null;
		String methodName = "get" + methodRoot;
		Widget[] widgets = getExampleWidgets();
		try {
			java.lang.reflect.Method method = widgets[0].getClass().getMethod(methodName, null);
			returnType = method.getReturnType();
		} catch (Exception e) {
		}
		return returnType;
	}
	
	void setValue() {
		/* The parameter type must be the same as the get method's return type */
		String methodRoot = nameCombo.getText();
		Class returnType = getReturnType(methodRoot);
		String methodName = setMethodName(methodRoot);
		String value = setText.getText();
		Widget[] widgets = getExampleWidgets();
		for (int i = 0; i < widgets.length; i++) {
			try {
				java.lang.reflect.Method method = widgets[i].getClass().getMethod(methodName, new Class[] {returnType});
				String typeName = returnType.getName();
				Object[] parameter = null;
				if (value.equals("null")) {
					parameter = new Object[] {null};
				} else if (typeName.equals("int")) {
					parameter = new Object[] {new Integer(value)};
				} else if (typeName.equals("long")) {
					parameter = new Object[] {new Long(value)};
				} else if (typeName.equals("char")) {
					parameter = new Object[] {value.length() == 1 ? new Character(value.charAt(0)) : new Character('\0')};
				} else if (typeName.equals("boolean")) {
					parameter = new Object[] {new Boolean(value)};
				} else if (typeName.equals("java.lang.String")) {
					parameter = new Object[] {value};
				} else if (typeName.equals("org.eclipse.swt.graphics.Point")) {
					String xy[] = split(value, ',');
					parameter = new Object[] {new Point(new Integer(xy[0]).intValue(),new Integer(xy[1]).intValue())};
				} else if (typeName.equals("[I")) {
					String strings[] = split(value, ',');
					int[] ints = new int[strings.length];
					for (int j = 0; j < strings.length; j++) {
						ints[j] = new Integer(strings[j]).intValue();
					}
					parameter = new Object[] {ints};
				} else if (typeName.equals("[Ljava.lang.String;")) {
					parameter = new Object[] {split(value, ',')};
				} else {
					parameter = parameterForType(typeName, value, widgets[i]);
				}
				method.invoke(widgets[i], parameter);
			} catch (Exception e) {
				Throwable cause = e.getCause();
				String message = e.getMessage();
				getText.setText(e.toString());
				if (cause != null) getText.append(", cause=\n" + cause.toString());
				if (message != null) getText.append(", message=\n" + message);
			}
		}
	}

	Object[] parameterForType(String typeName, String value, Widget widget) {
		return new Object[] {value};
	}

	void createOrientationGroup () {
		/* Create Orientation group*/
		orientationGroup = new Group (controlGroup, SWT.NONE);
		orientationGroup.setLayout (new GridLayout());
		orientationGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		orientationGroup.setText (ControlExample.getResourceString("Orientation"));
		defaultOrietationButton = new Button (orientationGroup, SWT.RADIO);
		defaultOrietationButton.setText (ControlExample.getResourceString("Default"));
		defaultOrietationButton.setSelection (true);
		ltrButton = new Button (orientationGroup, SWT.RADIO);
		ltrButton.setText ("SWT.LEFT_TO_RIGHT");
		rtlButton = new Button (orientationGroup, SWT.RADIO);
		rtlButton.setText ("SWT.RIGHT_TO_LEFT");
	}
	
	/**
	 * Creates the "Size" group.  The "Size" group contains
	 * controls that allow the user to change the size of
	 * the example widgets.
	 */
	void createSizeGroup () {
		/* Create the group */
		sizeGroup = new Group (controlGroup, SWT.NONE);
		sizeGroup.setLayout (new GridLayout());
		sizeGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		sizeGroup.setText (ControlExample.getResourceString("Size"));
	
		/* Create the controls */
	
		/*
		 * The preferred size of a widget is the size returned
		 * by widget.computeSize (SWT.DEFAULT, SWT.DEFAULT).
		 * This size is defined on a widget by widget basis.
		 * Many widgets will attempt to display their contents.
		 */
		preferredButton = new Button (sizeGroup, SWT.RADIO);
		preferredButton.setText (ControlExample.getResourceString("Preferred"));
		tooSmallButton = new Button (sizeGroup, SWT.RADIO);
		tooSmallButton.setText (TOO_SMALL_SIZE + " X " + TOO_SMALL_SIZE);
		smallButton = new Button(sizeGroup, SWT.RADIO);
		smallButton.setText (SMALL_SIZE + " X " + SMALL_SIZE);
		largeButton = new Button (sizeGroup, SWT.RADIO);
		largeButton.setText (LARGE_SIZE + " X " + LARGE_SIZE);
		fillHButton = new Button (sizeGroup, SWT.CHECK);
		fillHButton.setText (ControlExample.getResourceString("Fill_X"));
		fillVButton = new Button (sizeGroup, SWT.CHECK);
		fillVButton.setText (ControlExample.getResourceString("Fill_Y"));
		
		/* Add the listeners */
		SelectionAdapter selectionListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				setExampleWidgetSize ();
			}
		};
		preferredButton.addSelectionListener(selectionListener);
		tooSmallButton.addSelectionListener(selectionListener);
		smallButton.addSelectionListener(selectionListener);
		largeButton.addSelectionListener(selectionListener);
		fillHButton.addSelectionListener(selectionListener);
		fillVButton.addSelectionListener(selectionListener);
	
		/* Set the default state */
		preferredButton.setSelection (true);
	}
	
	/**
	 * Creates the "Style" group.  The "Style" group contains
	 * controls that allow the user to change the style of
	 * the example widgets.  Changing a widget "Style" causes
	 * the widget to be destroyed and recreated.
	 */
	void createStyleGroup () {
		styleGroup = new Group (controlGroup, SWT.NONE);
		styleGroup.setLayout (new GridLayout ());
		styleGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, false, false));
		styleGroup.setText (ControlExample.getResourceString("Styles"));
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		/* Cache the shell and display. */
		shell = tabFolder.getShell ();
		display = shell.getDisplay ();
		
		/* Create a two column page. */
		tabFolderPage = new Composite (tabFolder, SWT.NONE);
		tabFolderPage.setLayout (new GridLayout (2, false));
	
		/* Create the "Example" and "Control" groups. */
		createExampleGroup ();
		createControlGroup ();
		
		/* Create the "Listeners" group under the "Control" group. */
		createListenersGroup ();
		
		/* Create and initialize the example and control widgets. */
		createExampleWidgets ();
		hookExampleWidgetListeners ();
		createControlWidgets ();
		setExampleWidgetState ();
		
		return tabFolderPage;
	}
	
	void setExampleWidgetPopupMenu() {
		Control[] controls = getExampleControls();
		for (int i = 0; i < controls.length; i++) {
			final Control control = controls [i];
			control.addListener(SWT.MenuDetect, new Listener() {
				public void handleEvent(Event event) {
		        	Menu menu = control.getMenu();
		        	if (menu != null && samplePopup) {
		        		menu.dispose();
		        		menu = null;
		        	}
		        	if (menu == null && popupMenuButton.getSelection()) {
		        		menu = new Menu(shell, SWT.POP_UP | (control.getStyle() & (SWT.RIGHT_TO_LEFT | SWT.LEFT_TO_RIGHT)));
			        	MenuItem item = new MenuItem(menu, SWT.PUSH);
			        	item.setText("Sample popup menu item");
			        	specialPopupMenuItems(menu, event);
			        	control.setMenu(menu);
		        		samplePopup = true;
		        	}
				}
			});
		}
	}

	protected void specialPopupMenuItems(final Menu menu, final Event event) {
	}

	/**
	 * Disposes the "Example" widgets.
	 */
	void disposeExampleWidgets () {
		Widget [] widgets = getExampleWidgets ();
		for (int i=0; i<widgets.length; i++) {
			widgets [i].dispose ();
		}
	}
	
	Image colorImage (Color color) {
		Image image = new Image (display, IMAGE_SIZE, IMAGE_SIZE);
		GC gc = new GC(image);
		gc.setBackground(color);
		Rectangle bounds = image.getBounds();
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
		gc.dispose();
		return image;
	}
	
	Image fontImage (Font font) {
		Image image = new Image (display, IMAGE_SIZE, IMAGE_SIZE);
		GC gc = new GC(image);
		Rectangle bounds = image.getBounds();
		gc.setBackground(display.getSystemColor(SWT.COLOR_WHITE));
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
		FontData data[] = font.getFontData();
		int style = data[0].getStyle();
		switch (style) {
		case SWT.NORMAL:
			gc.drawLine(3, 3, 3, 8);
			gc.drawLine(4, 3, 7, 8);
			gc.drawLine(8, 3, 8, 8);
			break;
		case SWT.BOLD:
			gc.drawLine(3, 2, 3, 9);
			gc.drawLine(4, 2, 4, 9);
			gc.drawLine(5, 2, 7, 2);
			gc.drawLine(5, 3, 8, 3);
			gc.drawLine(5, 5, 7, 5);
			gc.drawLine(5, 6, 7, 6);
			gc.drawLine(5, 8, 8, 8);
			gc.drawLine(5, 9, 7, 9);
			gc.drawLine(7, 4, 8, 4);
			gc.drawLine(7, 7, 8, 7);
			break;
		case SWT.ITALIC:
			gc.drawLine(6, 2, 8, 2);
			gc.drawLine(7, 3, 4, 8);
			gc.drawLine(3, 9, 5, 9);
			break;
		case SWT.BOLD | SWT.ITALIC:
			gc.drawLine(5, 2, 8, 2);
			gc.drawLine(5, 3, 8, 3);
			gc.drawLine(6, 4, 4, 7);
			gc.drawLine(7, 4, 5, 7);
			gc.drawLine(3, 8, 6, 8);
			gc.drawLine(3, 9, 6, 9);
			break;
		}
		gc.dispose();
		return image;
	}
	
	/**
	 * Gets the list of custom event names.
	 * Subclasses override this method to allow adding of custom events.
	 * 
	 * @return an array containing custom event names
	 * @see hookCustomListener
	 */
	String [] getCustomEventNames () {
		return new String [0];
	}
	
	/**
	 * Gets the default style for a widget
	 *
	 * @return the default style bit
	 */
	int getDefaultStyle () {
		if (ltrButton != null && ltrButton.getSelection()) {
			return SWT.LEFT_TO_RIGHT;
		}
		if (rtlButton != null && rtlButton.getSelection()) {
			return SWT.RIGHT_TO_LEFT;
		}
		return SWT.NONE;
	}
	
	/**
	 * Gets the "Example" widgets.
	 *
	 * @return an array containing the example widgets
	 */
	Widget [] getExampleWidgets () {
		return new Widget [0];
	}
	
	/**
	 * Gets the "Example" controls.
	 * This is the subset of "Example" widgets that are controls.
	 *
	 * @return an array containing the example controls
	 */
	Control [] getExampleControls () {
		Widget [] widgets = getExampleWidgets ();
		Control [] controls = new Control [0];
		for (int i = 0; i < widgets.length; i++) {
			if (widgets[i] instanceof Control) {
				Control[] newControls = new Control[controls.length + 1];
				System.arraycopy(controls, 0, newControls, 0, controls.length);
				controls = newControls;
				controls[controls.length - 1] = (Control)widgets[i];
			}
		}
		return controls;
	}
	
	/**
	 * Gets the "Example" widget's items, if any.
	 *
	 * @return an array containing the example widget's items
	 */
	Item [] getExampleWidgetItems () {
		return new Item [0];
	}
	
	/**
	 * Gets the short text for the tab folder item.
	 *
	 * @return the short text for the tab item
	 */
	String getShortTabText() {
		return getTabText();
	}

	/**
	 * Gets the text for the tab folder item.
	 *
	 * @return the text for the tab item
	 */
	String getTabText () {
		return "";
	}
	
	/**
	 * Hooks all listeners to all example controls
	 * and example control items.
	 */
	void hookExampleWidgetListeners () {
		if (logging) {
			Widget[] widgets = getExampleWidgets ();
			for (int i = 0; i < widgets.length; i++) {
				hookListeners (widgets [i]);
			}
			Item[] exampleItems = getExampleWidgetItems ();
			for (int i = 0; i < exampleItems.length; i++) {
				hookListeners (exampleItems [i]);
			}
			String [] customNames = getCustomEventNames ();
			for (int i = 0; i < customNames.length; i++) {
				if (eventsFilter [EVENT_INFO.length + i]) {
					hookCustomListener (customNames[i]);
				}
			}
		}
	}
	
	/**
	 * Hooks the custom listener specified by eventName.
	 * Subclasses override this method to add custom listeners.
	 * @see getCustomEventNames
	 */
	void hookCustomListener (String eventName) {
	}
	
	/**
	 * Hooks all listeners to the specified widget.
	 */
	void hookListeners (Widget widget) {
		if (logging) {
			Listener listener = new Listener() {
				public void handleEvent (Event event) {
					log (event);
				}
			};
			for (int i = 0; i < EVENT_INFO.length; i++) {
				if (eventsFilter [i]) {
					widget.addListener (EVENT_INFO[i].type, listener);
				}
			}
		}
	}
	
	/**
	 * Logs an untyped event to the event console.
	 */
	void log(Event event) {
		int i = 0;
		while (i < EVENT_INFO.length) {
			if (EVENT_INFO[i].type == event.type) break;
			i++;
		}
		String toString = EVENT_INFO[i].name + " [" + event.type + "]: ";
		switch (event.type) {
			case SWT.KeyDown:
			case SWT.KeyUp: toString += new KeyEvent (event).toString (); break;
			case SWT.MouseDown:
			case SWT.MouseUp:
			case SWT.MouseMove:
			case SWT.MouseEnter:
			case SWT.MouseExit:
			case SWT.MouseDoubleClick:
			case SWT.MouseWheel: 
			case SWT.MouseHover: toString += new MouseEvent (event).toString (); break;
			case SWT.Paint: toString += new PaintEvent (event).toString (); break;
			case SWT.Move:
			case SWT.Resize: toString += new ControlEvent (event).toString (); break;
			case SWT.Dispose: toString += new DisposeEvent (event).toString (); break;
			case SWT.Selection:
			case SWT.DefaultSelection: toString += new SelectionEvent (event).toString (); break;
			case SWT.FocusIn:
			case SWT.FocusOut: toString += new FocusEvent (event).toString (); break;
			case SWT.Expand:
			case SWT.Collapse: toString += new TreeEvent (event).toString (); break;
			case SWT.Iconify:
			case SWT.Deiconify:
			case SWT.Close:
			case SWT.Activate:
			case SWT.Deactivate: toString += new ShellEvent (event).toString (); break;
			case SWT.Show:
			case SWT.Hide: toString += (event.widget instanceof Menu) ? new MenuEvent (event).toString () : event.toString(); break;
			case SWT.Modify: toString += new ModifyEvent (event).toString (); break;
			case SWT.Verify: toString += new VerifyEvent (event).toString (); break;
			case SWT.Help: toString += new HelpEvent (event).toString (); break;
			case SWT.Arm: toString += new ArmEvent (event).toString (); break;
			case SWT.Traverse: toString += new TraverseEvent (event).toString (); break;
			case SWT.HardKeyDown:
			case SWT.HardKeyUp:
			case SWT.DragDetect:
			case SWT.MenuDetect:
			case SWT.SetData:
			default: toString += event.toString ();
		}
		log (toString);
		
		/* Return values for event fields. */
		int mask = EVENT_INFO[i].setFields;
		if (!ignore && mask != 0) {
			Event setFieldsEvent = EVENT_INFO[i].event;
			if ((mask & DOIT) != 0) event.doit = setFieldsEvent.doit;
			if ((mask & DETAIL) != 0) event.detail = setFieldsEvent.detail;
			if ((mask & TEXT) != 0) event.text = setFieldsEvent.text;
			if ((mask & X) != 0) event.x = setFieldsEvent.x;
			if ((mask & Y) != 0) event.y = setFieldsEvent.y;
			if ((mask & WIDTH) != 0) event.width = setFieldsEvent.width;
			if ((mask & HEIGHT) != 0) event.height = setFieldsEvent.height;
			eventConsole.append (ControlExample.getResourceString("Returning"));
			ignore = true;
			log (event);
			ignore = false;
		}
	}
	
	/**
	 * Logs a string to the event console.
	 */
	void log (String string) {
		if (!eventConsole.isDisposed()) {
			eventConsole.append (string);
			eventConsole.append ("\n");
		}
	}

	/**
	 * Logs a typed event to the event console.
	 */
	void log (String eventName, TypedEvent event) {
		log (eventName + ": " + event.toString ());
	}
	
	/**
	 * Recreates the "Example" widgets.
	 */
	void recreateExampleWidgets () {
		disposeExampleWidgets ();
		createExampleWidgets ();
		hookExampleWidgetListeners ();
		setExampleWidgetState ();
	}
	
	/**
	 * Sets the foreground color, background color, and font
	 * of the "Example" widgets to their default settings.
	 * Subclasses may extend in order to reset other colors
	 * and fonts to default settings as well.
	 */
	void resetColorsAndFonts () {
		Color oldColor = foregroundColor;
		foregroundColor = null;
		setExampleWidgetForeground ();
		if (oldColor != null) oldColor.dispose();
		oldColor = backgroundColor;
		backgroundColor = null;
		setExampleWidgetBackground ();
		if (oldColor != null) oldColor.dispose();
		Font oldFont = font;
		font = null;
		setExampleWidgetFont ();
		setExampleWidgetSize ();
		if (oldFont != null) oldFont.dispose();
	}
	
	boolean rtlSupport() {
		return RTL_SUPPORT_ENABLE;
	}
	
	/**
	 * Sets the background color of the "Example" widgets' parent.
	 */
	void setExampleGroupBackgroundColor () {
		if (backgroundModeGroup == null) return;
		exampleGroup.setBackground (backgroundModeColorButton.getSelection () ? display.getSystemColor(SWT.COLOR_BLUE) : null);
	}
	/**
	 * Sets the background image of the "Example" widgets' parent.
	 */
	void setExampleGroupBackgroundImage () {
		if (backgroundModeGroup == null) return;
		exampleGroup.setBackgroundImage (backgroundModeImageButton.getSelection () ? instance.images[ControlExample.ciParentBackground] : null);
	}

	/**
	 * Sets the background mode of the "Example" widgets' parent.
	 */
	void setExampleGroupBackgroundMode () {
		if (backgroundModeGroup == null) return;
		String modeString = backgroundModeCombo.getText ();
		int mode = SWT.INHERIT_NONE;
		if (modeString.equals("SWT.INHERIT_DEFAULT")) mode = SWT.INHERIT_DEFAULT;
		if (modeString.equals("SWT.INHERIT_FORCE")) mode = SWT.INHERIT_FORCE;
		exampleGroup.setBackgroundMode (mode);
	}

	/**
	 * Sets the background color of the "Example" widgets.
	 */
	void setExampleWidgetBackground () {
		if (colorAndFontTable == null) return; // user cannot change color/font on this tab
		Control [] controls = getExampleControls ();
		if (!instance.startup) {
			for (int i = 0; i < controls.length; i++) {
				controls[i].setBackground (backgroundColor);
			}
		}
		// Set the background color item's image to match the background color of the example widget(s).
		Color color = backgroundColor;
		if (controls.length == 0) return;
		if (color == null) color = controls [0].getBackground ();
		TableItem item = colorAndFontTable.getItem(BACKGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage (color));
	}
	
	/**
	 * Sets the enabled state of the "Example" widgets.
	 */
	void setExampleWidgetEnabled () {
		Control [] controls = getExampleControls ();
		for (int i=0; i<controls.length; i++) {
			controls [i].setEnabled (enabledButton.getSelection ());
		}
	}
	
	/**
	 * Sets the font of the "Example" widgets.
	 */
	void setExampleWidgetFont () {
		if (colorAndFontTable == null) return; // user cannot change color/font on this tab
		Control [] controls = getExampleControls ();
		if (!instance.startup) {
			for (int i = 0; i < controls.length; i++) {
				controls[i].setFont(font);
			}
		}
		/* Set the font item's image and font to match the font of the example widget(s). */
		Font ft = font;
		if (controls.length == 0) return;
		if (ft == null) ft = controls [0].getFont ();
		TableItem item = colorAndFontTable.getItem(FONT);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (fontImage (ft));
		item.setFont(ft);
		colorAndFontTable.layout ();
	}
	
	/**
	 * Sets the foreground color of the "Example" widgets.
	 */
	void setExampleWidgetForeground () {
		if (colorAndFontTable == null) return; // user cannot change color/font on this tab
		Control [] controls = getExampleControls ();
		if (!instance.startup) {
			for (int i = 0; i < controls.length; i++) {
				controls[i].setForeground (foregroundColor);
			}
		}
		/* Set the foreground color item's image to match the foreground color of the example widget(s). */
		Color color = foregroundColor;
		if (controls.length == 0) return;
		if (color == null) color = controls [0].getForeground ();
		TableItem item = colorAndFontTable.getItem(FOREGROUND_COLOR);
		Image oldImage = item.getImage();
		if (oldImage != null) oldImage.dispose();
		item.setImage (colorImage(color));
	}
	
	/**
	 * Sets the size of the "Example" widgets.
	 */
	void setExampleWidgetSize () {
		int size = SWT.DEFAULT;
		if (preferredButton == null) return;
		if (preferredButton.getSelection()) size = SWT.DEFAULT;
		if (tooSmallButton.getSelection()) size = TOO_SMALL_SIZE;
		if (smallButton.getSelection()) size = SMALL_SIZE;
		if (largeButton.getSelection()) size = LARGE_SIZE;
		Control [] controls = getExampleControls ();
		for (int i=0; i<controls.length; i++) {
			GridData gridData = new GridData(size, size); 
			gridData.grabExcessHorizontalSpace = fillHButton.getSelection();
			gridData.grabExcessVerticalSpace = fillVButton.getSelection();
			gridData.horizontalAlignment = fillHButton.getSelection() ? SWT.FILL : SWT.LEFT;
			gridData.verticalAlignment = fillVButton.getSelection() ? SWT.FILL : SWT.TOP;
			controls [i].setLayoutData (gridData);
		}
		tabFolderPage.layout (controls);
	}
	
	/**
	 * Sets the state of the "Example" widgets.  Subclasses
	 * may extend this method to set "Example" widget state
	 * that is specific to the widget.
	 */
	void setExampleWidgetState () {
		setExampleWidgetBackground ();
		setExampleWidgetForeground ();
		setExampleWidgetFont ();
		if (!instance.startup) {
			setExampleWidgetEnabled ();
			setExampleWidgetVisibility ();
			setExampleGroupBackgroundMode ();
			setExampleGroupBackgroundColor ();
			setExampleGroupBackgroundImage ();
			setExampleWidgetBackgroundImage ();
			setExampleWidgetPopupMenu ();
			setExampleWidgetSize ();
		}
		//TEMPORARY CODE
//		Control [] controls = getExampleControls ();
//		for (int i=0; i<controls.length; i++) {
//			log ("Control=" + controls [i] + ", border width=" + controls [i].getBorderWidth ());
//		}
	}
	
	/**
	 * Sets the visibility of the "Example" widgets.
	 */
	void setExampleWidgetVisibility () {
		Control [] controls = getExampleControls ();
		for (int i=0; i<controls.length; i++) {
			controls [i].setVisible (visibleButton.getSelection ());
		}
	}

	/**
	 * Sets the background image of the "Example" widgets.
	 */
	void setExampleWidgetBackgroundImage () {
		if (backgroundImageButton != null && backgroundImageButton.isDisposed()) return;
		Control [] controls = getExampleControls ();
		for (int i=0; i<controls.length; i++) {
			controls [i].setBackgroundImage (backgroundImageButton.getSelection () ? instance.images[ControlExample.ciBackground] : null);
		}
	}
	
	/**
	 * Splits the given string around matches of the given character.
	 * 
	 * This subset of java.lang.String.split(String regex)
	 * uses only code that can be run on CLDC platforms.
	 */
	String [] split (String string, char ch) {
		String [] result = new String[0];
		int start = 0;
		int length = string.length();
		while (start < length) {
			int end = string.indexOf(ch, start);
			if (end == -1) end = length;
			String substr = string.substring(start, end);
			String [] newResult = new String[result.length + 1];
			System.arraycopy(result, 0, newResult, 0, result.length);
			newResult [result.length] = substr;
			result = newResult;
			start = end + 1;
		}
		return result;
	}
}
