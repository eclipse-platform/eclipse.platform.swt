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
import java.util.ResourceBundle;

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
 * contains a "Style", "Display" and "Size" group.  Subclasses
 * can override these defaults to augment a group or stop
 * a group from being created.
 */
abstract class Tab {	
	/* Common control buttons */
	Button borderButton, enabledButton, visibleButton;
	Button preferredButton, tooSmallButton, smallButton, largeButton, fillButton;

	/* Common groups and composites */
	Composite tabFolderPage;
	Group exampleGroup, controlGroup, displayGroup, sizeGroup, styleGroup, colorGroup;

	/* Controlling instance */
	final ControlExample instance;

	/* Sizing constants for the "Size" group */
	static final int TOO_SMALL_SIZE	= 10;
	static final int SMALL_SIZE		= 50;
	static final int LARGE_SIZE		= 100;

	/* Common controls for the "Colors" group */
	Button backgroundButton, foregroundButton, fontButton;
	Color defaultBackgroundColor, defaultForegroundColor, backgroundColor, foregroundColor;
	RGB chosenBackgroundColor, chosenForegroundColor;
	Image backgroundImage, foregroundImage;
	Font font;
	FontData chosenFont;
	

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
		 * left half of each example tab.  It consists of the
		 * style group, the display group and the size group.
		 */	
		controlGroup = new Group (tabFolderPage, SWT.NONE);
		GridLayout gridLayout= new GridLayout ();
		controlGroup.setLayout (gridLayout);
		gridLayout.numColumns = 2;
		gridLayout.makeColumnsEqualWidth = true;
		controlGroup.setLayoutData (new GridData(GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		controlGroup.setText (ControlExample.getResourceString("Parameters"));
	
		/* Create individual groups inside the "Control" group */
		createStyleGroup ();
		createDisplayGroup ();
		createSizeGroup ();
		createColorGroup ();
	
		/*
		 * For each Button child in the style group, add a selection
		 * listener that will recreate the example controls.  If the
		 * style group button is a RADIO button, ensure that the radio
		 * button is selected before recreating the example controls.
		 * When the user selects a RADIO button, the curreont RADIO
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
		fontButton.addSelectionListener (selectionListener);
	}
	
	/**
	 * Creates the "Control" widget children.
	 * Subclasses override this method to augment
	 * the standard controls created in the "Style",
	 * "Display" and "Size" groups.
	 */
	void createControlWidgets () {
	}
	
	/**
	 * Creates the "Color" group. This is typically
	 * a child of the "Control" group. Subclasses override
	 * this method to customize and set system colors.
	 */
	void createColorGroup () {
		/* Create the group */
		colorGroup = new Group(controlGroup, SWT.NULL);
		colorGroup.setLayout (new GridLayout (2, false));
		colorGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		colorGroup.setText (ControlExample.getResourceString ("Colors"));
		new Label (colorGroup, SWT.NONE).setText (ControlExample.getResourceString ("Background_Color"));
		backgroundButton = new Button (colorGroup, SWT.PUSH);
		new Label (colorGroup, SWT.NONE).setText (ControlExample.getResourceString ("Foreground_Color"));
		foregroundButton = new Button (colorGroup, SWT.PUSH);
		fontButton = new Button (colorGroup, SWT.PUSH);
		fontButton.setText(ControlExample.getResourceString("Font"));
		
		/* Create images to display current colors */
		chosenBackgroundColor = defaultBackgroundColor.getRGB();
		chosenForegroundColor = defaultForegroundColor.getRGB();
		GC gc = new GC(fontButton.getShell());
		final int fontHeight = gc.getFontMetrics().getHeight();
		gc.dispose ();
		backgroundImage = new Image(colorGroup.getDisplay(),fontHeight*2,fontHeight);
		gc = new GC(backgroundImage);
		gc.setBackground(defaultBackgroundColor);
		gc.fillRectangle(0,0,fontHeight*2-1,fontHeight-1);
		gc.drawRectangle(0,0,fontHeight*2-1,fontHeight-1);
		gc.dispose();
		foregroundImage = new Image(colorGroup.getDisplay(),fontHeight*2,fontHeight);
		gc = new GC(foregroundImage);
		gc.setBackground(defaultForegroundColor);
		gc.fillRectangle(0,0,fontHeight*2-1,fontHeight-1);
		gc.drawRectangle(0,0,fontHeight*2-1,fontHeight-1);
		gc.dispose();
		
		/* Add listeners to set the colors and font */
		backgroundButton.setImage(backgroundImage);
		backgroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ColorDialog colorDialog= new ColorDialog(backgroundButton.getShell());
				colorDialog.setRGB(chosenBackgroundColor);
				RGB newColor = colorDialog.open();
				if (newColor != null) {
					Color oldColor = null;
					if (backgroundColor != null) oldColor = backgroundColor;
					backgroundColor = new Color (backgroundButton.getDisplay(), newColor);
					GC gc = new GC(backgroundImage);
					gc.setBackground(backgroundColor);
					gc.fillRectangle(0,0,fontHeight*2-1,fontHeight-1);
					gc.drawRectangle(0,0,fontHeight*2-1,fontHeight-1);
					gc.dispose();
					backgroundButton.setImage(backgroundImage);
					chosenBackgroundColor = newColor;
					setColors ();
					if (oldColor != null) oldColor.dispose ();
				}
			}
		});
		foregroundButton.setImage(foregroundImage);
		foregroundButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ColorDialog colorDialog= new ColorDialog(foregroundButton.getShell());
				colorDialog.setRGB(chosenForegroundColor);
				RGB newColor = colorDialog.open();
				if (newColor != null) {
					Color oldColor = null;
					if (foregroundColor != null) oldColor = foregroundColor;
					foregroundColor = new Color (foregroundButton.getDisplay(), newColor);
					GC gc= new GC(foregroundImage);
					gc.setBackground(foregroundColor);
					gc.fillRectangle(0,0,fontHeight*2-1,fontHeight-1);
					gc.drawRectangle(0,0,fontHeight*2-1,fontHeight-1);
					gc.dispose();
					foregroundButton.setImage(foregroundImage);
					chosenForegroundColor = newColor;
					setColors ();
					if (oldColor != null) oldColor.dispose ();
				}
			}
		});
		fontButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				FontDialog fontDialog = new FontDialog (fontButton.getShell());
				if (chosenFont == null) {
					fontDialog.setFontData ((fontButton.getFont().getFontData ())[0]);
				} else {
					fontDialog.setFontData(chosenFont);
				}
				FontData fontData = fontDialog.open ();
				if (fontData != null) {
					if (font != null) font.dispose ();
					font = new Font (fontButton.getDisplay(), fontData);
					chosenFont = fontData;
				}
			}
		});
		backgroundButton.addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				if (backgroundImage != null) backgroundImage.dispose();
				if (foregroundImage != null) foregroundImage.dispose();
				if (backgroundColor != null) backgroundColor.dispose();
				if (foregroundColor != null) foregroundColor.dispose();
				if (font != null) font.dispose();
				backgroundImage = null;
				foregroundImage = null;
				backgroundColor = null;
				foregroundColor = null;
				font = null;				
			}
		});
	}
	
	/**
	 * Creates the "Display" group.  This is typically
	 * a child of the "Control" group.
	 */
	void createDisplayGroup () {
		/* Create the group */
		displayGroup = new Group (controlGroup, SWT.NONE);
		displayGroup.setLayout (new GridLayout ());
		displayGroup.setLayoutData (new GridData (GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		displayGroup.setText (ControlExample.getResourceString("State"));
	
		/* Create the controls */
		enabledButton = new Button(displayGroup, SWT.CHECK);
		enabledButton.setText(ControlExample.getResourceString("Enabled"));
		visibleButton = new Button(displayGroup, SWT.CHECK);
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
		tabFolderPage = new Composite (tabFolder, SWT.NULL);
		GridLayout gridLayout = new GridLayout ();
		tabFolderPage.setLayout (gridLayout);
		gridLayout.numColumns = 2;
	
		/* Create the "Example" and "Control" columns */
		createExampleGroup ();
		createControlGroup ();
	
		/* Create the widgets in the two columns */
		createExampleWidgets ();
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
	
	/**
	 * Gets the "Example" widget children.
	 *
	 * @return an array of example widget children
	 */
	Control [] getExampleWidgets () {
		return new Control [0];
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
	 * Recreates the "Example" widgets.
	 */
	void recreateExampleWidgets () {
		disposeExampleWidgets ();
		createExampleWidgets ();
		setExampleWidgetState ();
	}
	
	/**
	 * Sets the background and foreground colors of the 
	 * "Example" widgets. Subclasses override this method.
	 */
	void setColors () {
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
		 * even when the child sizes nay not have
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
