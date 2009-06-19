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
	Button borderButton, enabledButton, visibleButton, backgroundImageButton;
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
	static final boolean RTL_SUPPORT_ENABLE = false;
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

	/* Event logging variables and controls */
	Text eventConsole;
	boolean logging = false;
	boolean [] eventsFilter;
	
	/* Set/Get API controls */
	Combo nameCombo;
	Label returnTypeLabel;
	Button getButton, setButton;
	Text setText, getText;

	static final String [] EVENT_NAMES = {
		"None",
		"KeyDown", "KeyUp",
		"MouseDown", "MouseUp", "MouseMove", "MouseEnter", "MouseExit", "MouseDoubleClick",
		"Paint", "Move", "Resize", "Dispose",
		"Selection", "DefaultSelection",
		"FocusIn", "FocusOut",
		"Expand", "Collapse",
		"Iconify", "Deiconify", "Close",
		"Show", "Hide",
		"Modify", "Verify",
		"Activate", "Deactivate",
		"Help", "DragDetect", "Arm", "Traverse", "MouseHover",
		"HardKeyDown", "HardKeyUp",
		"MenuDetect",
		"SetData",
		"MouseWheel",
		"Settings",  // note: this event only goes to Display
		"EraseItem",
		"MeasureItem",
		"PaintItem",
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
		if (RTL_SUPPORT_ENABLE) {
			createOrientationGroup ();
		}
	
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
			}
		}
		if (RTL_SUPPORT_ENABLE) {
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
			Button setGetButton = new Button (otherGroup, SWT.PUSH);
			setGetButton.setText (ControlExample.getResourceString ("Set_Get"));
			setGetButton.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false));
			setGetButton.addSelectionListener (new SelectionAdapter() {
				public void widgetSelected (SelectionEvent e) {
					Button button = (Button)e.widget;
					Point pt = button.getLocation();
					pt = e.display.map(button, null, pt);
					createSetGetDialog(pt.x, pt.y, methodNames);
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
			}
		});
	}
	
	void changeFontOrColor(int index) {
		switch (index) {
			case FOREGROUND_COLOR: {
				Color oldColor = foregroundColor;
				if (oldColor == null) {
					Control [] controls = getExampleWidgets ();
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
					Control [] controls = getExampleWidgets ();
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
					Control [] controls = getExampleWidgets ();
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
	
		/* Set the default state */
		enabledButton.setSelection(true);
		visibleButton.setSelection(true);
		backgroundImageButton.setSelection(false);
	}
	
	/**
	 * Creates the "Background Mode" group.
	 */
	void createBackgroundModeGroup () {
		// note that this method must be called after createExampleWidgets
		if (getExampleWidgets ().length == 0) return;
		
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
		final Shell dialog = new Shell (shell, SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		dialog.setText (ControlExample.getResourceString ("Select_Listeners"));
		dialog.setLayout (new GridLayout (2, false));
		final Table table = new Table (dialog, SWT.BORDER | SWT.V_SCROLL | SWT.CHECK);
		GridData data = new GridData(GridData.FILL_BOTH);
		data.verticalSpan = 2;
		table.setLayoutData(data);
		for (int i = 0; i < EVENT_NAMES.length; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (EVENT_NAMES[i]);
			item.setChecked (eventsFilter[i]);
		}
		final String [] customNames = getCustomEventNames ();
		for (int i = 0; i < customNames.length; i++) {
			TableItem item = new TableItem (table, SWT.NONE);
			item.setText (customNames[i]);
			item.setChecked (eventsFilter[EVENT_NAMES.length + i]);
		}
		Button selectAll = new Button (dialog, SWT.PUSH);
		selectAll.setText(ControlExample.getResourceString ("Select_All"));
		selectAll.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL));
		selectAll.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem [] items = table.getItems();
				for (int i = 0; i < EVENT_NAMES.length; i++) {
					items[i].setChecked(true);
				}
				for (int i = 0; i < customNames.length; i++) {
					items[EVENT_NAMES.length + i].setChecked(true);
				}
			}
		});
		Button deselectAll = new Button (dialog, SWT.PUSH);
		deselectAll.setText(ControlExample.getResourceString ("Deselect_All"));
		deselectAll.setLayoutData(new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_BEGINNING));
		deselectAll.addSelectionListener (new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				TableItem [] items = table.getItems();
				for (int i = 0; i < EVENT_NAMES.length; i++) {
					items[i].setChecked(false);
				}
				for (int i = 0; i < customNames.length; i++) {
					items[EVENT_NAMES.length + i].setChecked(false);
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
				for (int i = 0; i < EVENT_NAMES.length; i++) {
					eventsFilter[i] = items[i].getChecked();
				}
				for (int i = 0; i < customNames.length; i++) {
					eventsFilter[EVENT_NAMES.length + i] = items[EVENT_NAMES.length + i].getChecked();
				}
				dialog.dispose();
			}
		});
		dialog.pack ();
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
		eventsFilter = new boolean [EVENT_NAMES.length + customEventCount];
		for (int i = 0; i < EVENT_NAMES.length + customEventCount; i++) {
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

	void createSetGetDialog(int x, int y, String[] methodNames) {
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
		dialog.setLocation(x, y);
		dialog.open();
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
		Control[] controls = getExampleWidgets();
		for (int i = 0; i < controls.length; i++) {
			try {
				java.lang.reflect.Method method = controls[i].getClass().getMethod(methodName, null);
				Object result = method.invoke(controls[i], null);
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
			if (i + 1 < controls.length) {
				getText.append("\n\n");
			}
		}
	}

	Class getReturnType(String methodRoot) {
		Class returnType = null;
		String methodName = "get" + methodRoot;
		Control[] controls = getExampleWidgets();
		try {
			java.lang.reflect.Method method = controls[0].getClass().getMethod(methodName, null);
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
		Control[] controls = getExampleWidgets();
		for (int i = 0; i < controls.length; i++) {
			try {
				java.lang.reflect.Method method = controls[i].getClass().getMethod(methodName, new Class[] {returnType});
				String typeName = returnType.getName();
				Object[] parameter = null;
				if (typeName.equals("int")) {
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
					String xy[] = value.split(",");
					parameter = new Object[] {new Point(new Integer(xy[0]).intValue(),new Integer(xy[1]).intValue())};
				} else if (typeName.equals("[I")) {
					String strings[] = value.split(",");
					int[] ints = new int[strings.length];
					for (int j = 0; j < strings.length; j++) {
						ints[j] = new Integer(strings[j]).intValue();
					}
					parameter = new Object[] {ints};
				} else if (typeName.equals("[Ljava.lang.String;")) {
					parameter = new Object[] {value.split(",")};
				} else {
					parameter = parameterForType(typeName, value, controls[i]);
				}
				method.invoke(controls[i], parameter);
			} catch (Exception e) {
				getText.setText(e.toString());
			}
		}
	}

	Object[] parameterForType(String typeName, String value, Control control) {
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
		createBackgroundModeGroup ();
		setExampleWidgetState ();
		
		return tabFolderPage;
	}
	
	/**
	 * Disposes the "Example" widgets.
	 */
	void disposeExampleWidgets () {
		Control [] controls = getExampleWidgets ();
		for (int i=0; i<controls.length; i++) {
			controls [i].dispose ();
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
	 * 
	 * @return an array containing custom event names
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
	 * Gets the "Example" widget children.
	 *
	 * @return an array containing the example widget children
	 */
	Control [] getExampleWidgets () {
		return new Control [0];
	}
	
	/**
	 * Gets the "Example" widget children's items, if any.
	 *
	 * @return an array containing the example widget children's items
	 */
	Item [] getExampleWidgetItems () {
		return new Item [0];
	}
	
	/**
	 * Gets the short text for the tab folder item.
	 *
	 * @return the short text for the tab item
	 */
	public String getShortTabText() {
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
			Control[] exampleControls = getExampleWidgets ();
			for (int i = 0; i < exampleControls.length; i++) {
				hookListeners (exampleControls [i]);
			}
			Item[] exampleItems = getExampleWidgetItems ();
			for (int i = 0; i < exampleItems.length; i++) {
				hookListeners (exampleItems [i]);
			}
			String [] customNames = getCustomEventNames ();
			for (int i = 0; i < customNames.length; i++) {
				if (eventsFilter [EVENT_NAMES.length + i]) hookCustomListener (customNames[i]);
			}
		}
	}
	
	/**
	 * Hooks the custom listener specified by eventName.
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
			for (int i = 0; i < EVENT_NAMES.length; i++) {
				if (eventsFilter [i]) widget.addListener (i, listener);
			}
		}
	}
	
	/**
	 * Logs an untyped event to the event console.
	 */
	void log(Event event) {
		String toString = EVENT_NAMES[event.type] + " ["+event.type+"]: ";
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
		eventConsole.append (toString);
		eventConsole.append ("\n");
	}
	
	/**
	 * Logs a string to the event console.
	 */
	void log (String string) {
		eventConsole.append (string);
		eventConsole.append ("\n");
	}

	/**
	 * Logs a typed event to the event console.
	 */
	void log (String eventName, TypedEvent event) {
		eventConsole.append (eventName + ": ");
		eventConsole.append (event.toString ());
		eventConsole.append ("\n");
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
		Control [] controls = getExampleWidgets ();
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
		Control [] controls = getExampleWidgets ();
		for (int i=0; i<controls.length; i++) {
			controls [i].setEnabled (enabledButton.getSelection ());
		}
	}
	
	/**
	 * Sets the font of the "Example" widgets.
	 */
	void setExampleWidgetFont () {
		if (colorAndFontTable == null) return; // user cannot change color/font on this tab
		Control [] controls = getExampleWidgets ();
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
		Control [] controls = getExampleWidgets ();
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
		Control [] controls = getExampleWidgets ();
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
	 * reimplement this method to set "Example" widget state
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
			setExampleWidgetSize ();
		}
		//TEMPORARY CODE
//		Control [] controls = getExampleWidgets ();
//		for (int i=0; i<controls.length; i++) {
//			log ("Control=" + controls [i] + ", border width=" + controls [i].getBorderWidth ());
//		}
	}
	
	/**
	 * Sets the visibility of the "Example" widgets.
	 */
	void setExampleWidgetVisibility () {
		Control [] controls = getExampleWidgets ();
		for (int i=0; i<controls.length; i++) {
			controls [i].setVisible (visibleButton.getSelection ());
		}
	}

	/**
	 * Sets the background image of the "Example" widgets.
	 */
	void setExampleWidgetBackgroundImage () {
		Control [] controls = getExampleWidgets ();
		for (int i=0; i<controls.length; i++) {
			controls [i].setBackgroundImage (backgroundImageButton.getSelection () ? instance.images[ControlExample.ciBackground] : null);
		}
	}
}
