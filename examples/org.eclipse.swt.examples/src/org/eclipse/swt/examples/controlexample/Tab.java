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
	/* Common control buttons */
	Button borderButton, enabledButton, visibleButton;
	Button preferredButton, tooSmallButton, smallButton, largeButton, fillButton;

	/* Common groups and composites */
	Composite tabFolderPage;
	Group exampleGroup, controlGroup, listenersGroup, otherGroup, sizeGroup, styleGroup, colorGroup;

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

	/* Controls and resources for the "Colors" group */
	Button foregroundButton, backgroundButton, fontButton;
	Image foregroundImage, backgroundImage;
	Color foregroundColor, backgroundColor;
	Font font;

	/* Event logging variables and controls */
	Text eventConsole;
	boolean logging = false;
	boolean [] eventsFilter;

	static final String [] EVENT_NAMES = {
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
		"MenuDetect"
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
		controlGroup.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		controlGroup.setText (ControlExample.getResourceString("Parameters"));
	
		/* Create individual groups inside the "Control" group */
		createStyleGroup ();
		createOtherGroup ();
		createSizeGroup ();
		createColorGroup ();
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
			};
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
	 * Creates the "Control" widget children.
	 * Subclasses override this method to augment
	 * the standard controls created in the "Style",
	 * "Other" and "Size" groups.
	 */
	void createControlWidgets () {
	}
	
	/**
	 * Creates the "Colors" group. This is typically
	 * a child of the "Control" group. Subclasses override
	 * this method to customize and set system colors.
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
		fontButton = new Button (colorGroup, SWT.PUSH);
		fontButton.setText(ControlExample.getResourceString("Font"));
		fontButton.setLayoutData(new GridData (GridData.HORIZONTAL_ALIGN_FILL));
		Button defaultsButton = new Button (colorGroup, SWT.PUSH);
		defaultsButton.setText(ControlExample.getResourceString("Defaults"));

		Shell shell = controlGroup.getShell ();
		final ColorDialog foregroundDialog = new ColorDialog (shell);
		final ColorDialog backgroundDialog = new ColorDialog (shell);
		final FontDialog fontDialog = new FontDialog (shell);

		/* Create images to display current colors */
		int imageSize = 12;
		Display display = shell.getDisplay ();
		foregroundImage = new Image (display, imageSize, imageSize);
		backgroundImage = new Image (display, imageSize, imageSize);	

		/* Add listeners to set the colors and font */
		foregroundButton.setImage(foregroundImage); // sets the size of the button
		foregroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Color oldColor = foregroundColor;
				if (oldColor == null) {
					Control [] controls = getExampleWidgets ();
					if (controls.length > 0) oldColor = controls [0].getForeground ();
				}
				if (oldColor != null) foregroundDialog.setRGB(oldColor.getRGB()); // seed dialog with current color
				RGB rgb = foregroundDialog.open();
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
				if (oldColor != null) backgroundDialog.setRGB(oldColor.getRGB());
				RGB rgb = backgroundDialog.open();
				if (rgb == null) return;
				oldColor = backgroundColor; // save old background color to dispose when done
				backgroundColor = new Color (event.display, rgb);
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
			}
		});
	}
	
	/**
	 * Creates the "Other" group.  This is typically
	 * a child of the "Control" group.
	 */
	void createOtherGroup () {
		/* Create the group */
		otherGroup = new Group (controlGroup, SWT.NONE);
		otherGroup.setLayout (new GridLayout ());
		otherGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		otherGroup.setText (ControlExample.getResourceString("Other"));
	
		/* Create the controls */
		enabledButton = new Button(otherGroup, SWT.CHECK);
		enabledButton.setText(ControlExample.getResourceString("Enabled"));
		visibleButton = new Button(otherGroup, SWT.CHECK);
		visibleButton.setText(ControlExample.getResourceString("Visible"));
	
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
	
		/* Set the default state */
		enabledButton.setSelection(true);
		visibleButton.setSelection(true);
	}
	
	/**
	 * Create the event console popup menu.
	 */
	void createEventConsolePopup () {
		Menu popup = new Menu (eventConsole.getShell (), SWT.POP_UP);
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
		GridLayout gridLayout = new GridLayout ();
		exampleGroup.setLayout (gridLayout);
		exampleGroup.setLayoutData (new GridData (GridData.FILL_BOTH));
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
		final Shell dialog = new Shell (tabFolderPage.getShell (), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
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
		Label filler = new Label(dialog, SWT.NONE);
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
			if (! dialog.getDisplay().readAndDispatch()) dialog.getDisplay().sleep();
		}
	}

	/**
	 * Creates the "Listeners" group.  The "Listeners" group
	 * goes below the "Example" and "Control" groups.
	 */
	void createListenersGroup () {
		listenersGroup = new Group (tabFolderPage, SWT.NONE);
		listenersGroup.setLayout (new GridLayout (3, false));
		GridData gridData = new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.FILL_VERTICAL);
		gridData.horizontalSpan = 2;
		listenersGroup.setLayoutData (gridData);
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
	
	void createOrientationGroup () {
		/* Create Orientation group*/
		orientationGroup = new Group (controlGroup, SWT.NONE);
		orientationGroup.setLayout (new GridLayout());
		orientationGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
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
		sizeGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
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
		fillButton = new Button (sizeGroup, SWT.RADIO);
		fillButton.setText (ControlExample.getResourceString("Fill"));
	
		/* Add the listeners */
		SelectionAdapter selectionListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent event) {
				if (!((Button) event.widget).getSelection ()) return;
				setExampleWidgetSize ();
			};
		};
		preferredButton.addSelectionListener(selectionListener);
		tooSmallButton.addSelectionListener(selectionListener);
		smallButton.addSelectionListener(selectionListener);
		largeButton.addSelectionListener(selectionListener);
		fillButton.addSelectionListener(selectionListener);
	
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
		styleGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		styleGroup.setText (ControlExample.getResourceString("Styles"));
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		/*
		* Create a two column page.
		*/
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
	
	/**
	 * Disposes the "Example" widgets.
	 */
	void disposeExampleWidgets () {
		Control [] controls = getExampleWidgets ();
		for (int i=0; i<controls.length; i++) {
			controls [i].dispose ();
		}
	}
	
	void drawImage (Image image, Color color) {
		GC gc = new GC(image);
		gc.setBackground(color);
		Rectangle bounds = image.getBounds();
		gc.fillRectangle(0, 0, bounds.width, bounds.height);
		gc.drawRectangle(0, 0, bounds.width - 1, bounds.height - 1);
		gc.dispose();
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
				if (eventsFilter [i]) widget.addListener (i + 1, listener);
			}
		}
	}
	
	/**
	 * Logs an untyped event to the event console.
	 */
	void log(Event event) {
		String toString = EVENT_NAMES[event.type - 1] + ": ";
		switch (event.type) {
			case SWT.KeyDown:
			case SWT.KeyUp: toString += new KeyEvent (event).toString (); break;
			case SWT.MouseDown:
			case SWT.MouseUp:
			case SWT.MouseMove:
			case SWT.MouseEnter:
			case SWT.MouseExit:
			case SWT.MouseDoubleClick:
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
			case SWT.Hide: toString += new MenuEvent (event).toString (); break;
			case SWT.Modify: toString += new ModifyEvent (event).toString (); break;
			case SWT.Verify: toString += new VerifyEvent (event).toString (); break;
			case SWT.Help: toString += new HelpEvent (event).toString (); break;
			case SWT.Arm: toString += new ArmEvent (event).toString (); break;
			case SWT.Traverse: toString += new TraverseEvent (event).toString (); break;
			case SWT.HardKeyDown:
			case SWT.HardKeyUp:
			case SWT.DragDetect:
			case SWT.MenuDetect:
			default: toString += event.toString ();
		}
		eventConsole.append (toString);
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
	 * Sets the background color of the "Example" widgets.
	 */
	void setExampleWidgetBackground () {
		if (backgroundButton == null) return; // no background button on this tab
		Control [] controls = getExampleWidgets ();
		for (int i = 0; i < controls.length; i++) {
			controls[i].setBackground (backgroundColor);
		}
		// Set the background button's color to match the color just set.
		Color color = backgroundColor;
		if (controls.length == 0) return;
		if (color == null) color = controls [0].getBackground ();
		drawImage (backgroundImage, color);
		backgroundButton.setImage (backgroundImage);
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
		if (fontButton == null) return; // no font button on this tab
		Control [] controls = getExampleWidgets ();
		for (int i = 0; i < controls.length; i++) {
			Control control = controls[i];
			control.setFont(font);
		}
	}
	
	/**
	 * Sets the foreground color of the "Example" widgets.
	 */
	void setExampleWidgetForeground () {
		if (foregroundButton == null) return; // no foreground button on this tab
		Control [] controls = getExampleWidgets ();
		for (int i = 0; i < controls.length; i++) {
			controls[i].setForeground (foregroundColor);
		}
		// Set the foreground button's color to match the color just set.
		Color color = foregroundColor;
		if (controls.length == 0) return;
		if (color == null) color = controls [0].getForeground ();
		drawImage (foregroundImage, color);
		foregroundButton.setImage (foregroundImage);
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
			GridData gridData; 
			if (fillButton.getSelection()) {
				gridData = new GridData (GridData.FILL_BOTH);
			} else {
				gridData = new GridData ();
				gridData.widthHint = size;
				gridData.heightHint = size;
			}
			controls [i].setLayoutData (gridData);
		}
		/*
		 * Force the entire widget tree to layout,
		 * even when the child sizes may not have
		 * changed.
		 */
		int seenCount = 0;
		Composite [] seen = new Composite [4];
		for (int i=0; i<controls.length; i++) {
			Control control = controls [i];
			while (control != exampleGroup) {
				Composite parent = control.getParent ();
				int index = 0;
				while (index < seenCount) {
					if (seen [index] == parent) break;
					index++;
				}
				if (index == seenCount) parent.layout ();
				if (seenCount == seen.length) {
					Composite [] newSeen = new Composite [seen.length + 4];
					System.arraycopy (seen, 0, newSeen, 0, seen.length);
					seen = newSeen;
				}
				seen [seenCount++] = parent;
				control = control.getParent ();
			}
		}
	}
	
	/**
	 * Sets the state of the "Example" widgets.  Subclasses
	 * reimplement this method to set "Example" widget state
	 * that is specific to the widget.
	 */
	void setExampleWidgetState () {
		setExampleWidgetEnabled ();
		setExampleWidgetVisibility ();
		setExampleWidgetBackground ();
		setExampleWidgetForeground ();
		setExampleWidgetFont ();
		setExampleWidgetSize ();
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
}
