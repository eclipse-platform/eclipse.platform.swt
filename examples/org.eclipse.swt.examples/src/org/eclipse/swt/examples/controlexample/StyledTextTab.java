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
import org.eclipse.swt.custom.*;

class StyledTextTab extends ScrollableTab {
	/* Example widgets and groups that contain them */
	StyledText styledText;
	Group styledTextGroup, styledTextStyleGroup;

	/* Style widgets added to the "Style" group */
	Button wrapButton, readOnlyButton, fullSelectionButton;
	
	/* Buttons for adding StyleRanges to StyledText */
	Button boldButton, italicButton, redButton, yellowButton;
	Image boldImage, italicImage, redImage, yellowImage;

	/**
	 * Creates the Tab within a given instance of ControlExample.
	 */
	StyledTextTab(ControlExample instance) {
		super(instance);
	}
	
	/**
	 * Creates a bitmap image. 
	 */
	Image createBitmapImage (Display display, String name) {
		ImageData source = new ImageData(ControlExample.class.getResourceAsStream(name + ".bmp"));
		ImageData mask = new ImageData(ControlExample.class.getResourceAsStream(name + "_mask.bmp"));
		return new Image (display, source, mask);
	}
	
	/**
	 * Creates the "Control" widget children.
	 */
	void createControlWidgets () {
		super.createControlWidgets ();
		
		/* Add a group for modifying the StyledText widget */
		createStyledTextStyleGroup ();
	}

	/**
	 * Creates the "Example" group.
	 */
	void createExampleGroup () {
		super.createExampleGroup ();
		
		/* Create a group for the styled text widget */
		styledTextGroup = new Group (exampleGroup, SWT.NONE);
		styledTextGroup.setLayout (new GridLayout ());
		styledTextGroup.setLayoutData (new GridData (GridData.GRAB_HORIZONTAL | GridData.HORIZONTAL_ALIGN_FILL | GridData.VERTICAL_ALIGN_FILL));
		styledTextGroup.setText ("StyledText");
	}
	
	/**
	 * Creates the "Example" widgets.
	 */
	void createExampleWidgets () {
		
		/* Compute the widget style */
		int style = getDefaultStyle();
		if (singleButton.getSelection ()) style |= SWT.SINGLE;
		if (multiButton.getSelection ()) style |= SWT.MULTI;
		if (horizontalButton.getSelection ()) style |= SWT.H_SCROLL;
		if (verticalButton.getSelection ()) style |= SWT.V_SCROLL;
		if (wrapButton.getSelection ()) style |= SWT.WRAP;
		if (readOnlyButton.getSelection ()) style |= SWT.READ_ONLY;
		if (borderButton.getSelection ()) style |= SWT.BORDER;
		if (fullSelectionButton.getSelection ()) style |= SWT.FULL_SELECTION;
	
		/* Create the example widgets */
		styledText = new StyledText (styledTextGroup, style);
		styledText.setText (ControlExample.getResourceString("Example_string"));
		styledText.append ("\n");
		styledText.append (ControlExample.getResourceString("One_Two_Three"));
	}
	
	/**
	 * Creates the "Style" group.
	 */
	void createStyleGroup() {
		super.createStyleGroup();
	
		/* Create the extra widgets */
		wrapButton = new Button (styleGroup, SWT.CHECK);
		wrapButton.setText ("SWT.WRAP");
		readOnlyButton = new Button (styleGroup, SWT.CHECK);
		readOnlyButton.setText ("SWT.READ_ONLY");
		fullSelectionButton = new Button (styleGroup, SWT.CHECK);
		fullSelectionButton.setText ("SWT.FULL_SELECTION");
	}
	
	/**
	 * Creates the "StyledText Style" group.
	 */
	void createStyledTextStyleGroup () {
		final Display display = controlGroup.getDisplay ();
		styledTextStyleGroup = new Group (controlGroup, SWT.NONE);
		styledTextStyleGroup.setText (ControlExample.getResourceString ("StyledText_Styles"));
		styledTextStyleGroup.setLayout (new GridLayout(5, false));
		GridData data = new GridData (GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = 2;
		styledTextStyleGroup.setLayoutData (data);
		
		/* Get images */
		boldImage = createBitmapImage (display, "bold");
		italicImage = createBitmapImage (display, "italic");
		redImage = createBitmapImage (display, "red");
		yellowImage = createBitmapImage (display, "yellow");
		
		/* Create controls to modify the StyledText */
		Label label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("StyledText_Style_Instructions"));
		data = new GridData(GridData.FILL_HORIZONTAL);
		data.horizontalSpan = 5;
		label.setLayoutData(data);
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Bold"));
		boldButton = new Button (styledTextStyleGroup, SWT.PUSH);
		boldButton.setImage (boldImage);
		boldButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				Point sel = styledText.getSelectionRange();
				if ((sel == null) || (sel.y == 0)) return;
				StyleRange style;
				for (int i = sel.x; i<sel.x+sel.y; i++) {
					StyleRange range = styledText.getStyleRangeAtOffset(i);
					if (range == null) {style = new StyleRange(i, 1, null, null, SWT.BOLD);}
					else {style = new StyleRange(i, 1, range.foreground, range.background, range.fontStyle | SWT.BOLD);};
					styledText.setStyleRange(style);
				}
				styledText.setSelectionRange(sel.x + sel.y, 0);
			}
		});
		new Label (styledTextStyleGroup, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Foreground_Style"));
		redButton = new Button (styledTextStyleGroup, SWT.PUSH);
		redButton.setImage (redImage);
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Italic"));
		italicButton = new Button (styledTextStyleGroup, SWT.PUSH);
		italicButton.setImage (italicImage);
		italicButton.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				Point sel = styledText.getSelectionRange();
				if ((sel == null) || (sel.y == 0)) return;
				StyleRange style;
				for (int i = sel.x; i<sel.x+sel.y; i++) {
					StyleRange range = styledText.getStyleRangeAtOffset(i);
					if (range == null) {style = new StyleRange(i, 1, null, null, SWT.ITALIC);}
					else {style = new StyleRange(i, 1, range.foreground, range.background, range.fontStyle | SWT.ITALIC);};
					styledText.setStyleRange(style);
				}
				styledText.setSelectionRange(sel.x + sel.y, 0);
			}
		});
		new Label (styledTextStyleGroup, SWT.NONE).setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		new Label (styledTextStyleGroup, SWT.NONE).setText (ControlExample.getResourceString ("Background_Style"));
		yellowButton = new Button (styledTextStyleGroup, SWT.PUSH);
		yellowButton.setImage (yellowImage);
		SelectionListener colorListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				Point sel = styledText.getSelectionRange();
				if ((sel == null) || (sel.y == 0)) return;
				Color fg, bg;
				if (e.widget == redButton) {
					fg = display.getSystemColor (SWT.COLOR_RED);
					bg = null;
				} else if (e.widget == yellowButton) {
					fg = null;
					bg = display.getSystemColor (SWT.COLOR_YELLOW);
				} else {
					fg = bg = null;
				}
				StyleRange style;
				for (int i = sel.x; i<sel.x+sel.y; i++) {
					StyleRange range = styledText.getStyleRangeAtOffset(i);
					if (range == null) {
						style = new StyleRange(i, 1, fg, bg, SWT.NORMAL);
					}
					else {
						if (range.foreground != null) fg = range.foreground;
						if (range.background != null) bg = range.background;
						style = new StyleRange(i, 1, fg, bg, range.fontStyle);
					};
					styledText.setStyleRange(style);
				}
				styledText.setSelectionRange(sel.x + sel.y, 0);
			};
		};
		redButton.addSelectionListener(colorListener);
		yellowButton.addSelectionListener(colorListener);
		yellowButton.addDisposeListener(new DisposeListener () {
			public void widgetDisposed (DisposeEvent e) {
				boldImage.dispose();
				italicImage.dispose();
				redImage.dispose();
				yellowImage.dispose();
			}
		});
	}
	
	/**
	 * Creates the tab folder page.
	 *
	 * @param tabFolder org.eclipse.swt.widgets.TabFolder
	 * @return the new page for the tab folder
	 */
	Composite createTabFolderPage (TabFolder tabFolder) {
		super.createTabFolderPage (tabFolder);

		/*
		 * Add a resize listener to the tabFolderPage so that
		 * if the user types into the example widget to change
		 * its preferred size, and then resizes the shell, we
		 * recalculate the preferred size correctly.
		 */
		tabFolderPage.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				setExampleWidgetSize ();
			}
		});
		
		return tabFolderPage;
	}

	/**
	 * Gets the "Example" widget children.
	 */
	Control [] getExampleWidgets () {
		return new Control [] {styledText};
	}
	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "StyledText";
	}
	
	/**
	 * Sets the state of the "Example" widgets.
	 */
	void setExampleWidgetState () {
		super.setExampleWidgetState ();
		wrapButton.setSelection ((styledText.getStyle () & SWT.WRAP) != 0);
		readOnlyButton.setSelection ((styledText.getStyle () & SWT.READ_ONLY) != 0);
		fullSelectionButton.setSelection ((styledText.getStyle () & SWT.FULL_SELECTION) != 0);
		horizontalButton.setEnabled ((styledText.getStyle () & SWT.MULTI) != 0);
		verticalButton.setEnabled ((styledText.getStyle () & SWT.MULTI) != 0);
		wrapButton.setEnabled ((styledText.getStyle () & SWT.MULTI) != 0);
	}
}
