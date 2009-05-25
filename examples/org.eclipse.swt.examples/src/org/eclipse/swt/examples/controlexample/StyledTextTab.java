/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.controlexample;


import java.io.*;
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
	Button boldButton, italicButton, redButton, yellowButton, underlineButton, strikeoutButton;
	Image boldImage, italicImage, redImage, yellowImage, underlineImage, strikeoutImage;
	
	/* Variables for saving state. */
	String text;
	StyleRange[] styleRanges;

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
		InputStream sourceStream = ControlExample.class.getResourceAsStream (name + ".bmp");
		InputStream maskStream = ControlExample.class.getResourceAsStream (name + "_mask.bmp");
		ImageData source = new ImageData (sourceStream);
		ImageData mask = new ImageData (maskStream);
		Image result = new Image (display, source, mask);
		try {
			sourceStream.close ();
			maskStream.close ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
		return result;
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
		styledTextGroup.setLayoutData (new GridData (SWT.FILL, SWT.FILL, true, true));
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
		
		if (text != null) {
			styledText.setText(text);
			text = null;
		}
		if (styleRanges != null) {
			styledText.setStyleRanges(styleRanges);
			styleRanges = null;
		}
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
		styledTextStyleGroup = new Group (controlGroup, SWT.NONE);
		styledTextStyleGroup.setText (ControlExample.getResourceString ("StyledText_Styles"));
		styledTextStyleGroup.setLayout (new GridLayout(6, false));
		GridData data = new GridData (GridData.HORIZONTAL_ALIGN_FILL);
		data.horizontalSpan = 2;
		styledTextStyleGroup.setLayoutData (data);
		
		/* Get images */
		boldImage = createBitmapImage (display, "bold");
		italicImage = createBitmapImage (display, "italic");
		redImage = createBitmapImage (display, "red");
		yellowImage = createBitmapImage (display, "yellow");
		underlineImage = createBitmapImage (display, "underline");
		strikeoutImage = createBitmapImage (display, "strikeout");
		
		/* Create controls to modify the StyledText */
		Label label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("StyledText_Style_Instructions"));
		label.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 6, 1));
		label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("Bold"));
		label.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		boldButton = new Button (styledTextStyleGroup, SWT.PUSH);
		boldButton.setImage (boldImage);
		label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("Underline"));
		label.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		underlineButton = new Button (styledTextStyleGroup, SWT.PUSH);
		underlineButton.setImage (underlineImage);
		label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("Foreground_Style"));
		label.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		redButton = new Button (styledTextStyleGroup, SWT.PUSH);
		redButton.setImage (redImage);
		label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("Italic"));
		label.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		italicButton = new Button (styledTextStyleGroup, SWT.PUSH);
		italicButton.setImage (italicImage);
		label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("Strikeout"));
		label.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		strikeoutButton = new Button (styledTextStyleGroup, SWT.PUSH);
		strikeoutButton.setImage (strikeoutImage);
		label = new Label (styledTextStyleGroup, SWT.NONE);
		label.setText (ControlExample.getResourceString ("Background_Style"));
		label.setLayoutData(new GridData(SWT.END, SWT.CENTER, true, false));
		yellowButton = new Button (styledTextStyleGroup, SWT.PUSH);
		yellowButton.setImage (yellowImage);
		SelectionListener styleListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				Point sel = styledText.getSelectionRange();
				if ((sel == null) || (sel.y == 0)) return;
				StyleRange style;
				for (int i = sel.x; i<sel.x+sel.y; i++) {
					StyleRange range = styledText.getStyleRangeAtOffset(i);
					if (range != null) {
						style = (StyleRange)range.clone();
						style.start = i;
						style.length = 1;
					} else {
						style = new StyleRange(i, 1, null, null, SWT.NORMAL);
					}
					if (e.widget == boldButton) {
						style.fontStyle ^= SWT.BOLD;
					} else if (e.widget == italicButton) {
						style.fontStyle ^= SWT.ITALIC;						
					} else if (e.widget == underlineButton) {
						style.underline = !style.underline;
					} else if (e.widget == strikeoutButton) {
						style.strikeout = !style.strikeout;
					}
					styledText.setStyleRange(style);
				}
				styledText.setSelectionRange(sel.x + sel.y, 0);			
			}
		};
		SelectionListener colorListener = new SelectionAdapter () {
			public void widgetSelected (SelectionEvent e) {
				Point sel = styledText.getSelectionRange();
				if ((sel == null) || (sel.y == 0)) return;
				Color fg = null, bg = null;
				if (e.widget == redButton) {
					fg = display.getSystemColor (SWT.COLOR_RED);
				} else if (e.widget == yellowButton) {
					bg = display.getSystemColor (SWT.COLOR_YELLOW);
				}
				StyleRange style;
				for (int i = sel.x; i<sel.x+sel.y; i++) {
					StyleRange range = styledText.getStyleRangeAtOffset(i);
					if (range != null) {
						style = (StyleRange)range.clone();
						style.start = i;
						style.length = 1;
						style.foreground = style.foreground != null ? null : fg;
						style.background = style.background != null ? null : bg;
					} else {
						style = new StyleRange (i, 1, fg, bg, SWT.NORMAL);
					}
					styledText.setStyleRange(style);
				}
				styledText.setSelectionRange(sel.x + sel.y, 0);
			}
		};
		boldButton.addSelectionListener(styleListener);
		italicButton.addSelectionListener(styleListener);
		underlineButton.addSelectionListener(styleListener);
		strikeoutButton.addSelectionListener(styleListener);
		redButton.addSelectionListener(colorListener);
		yellowButton.addSelectionListener(colorListener);
		yellowButton.addDisposeListener(new DisposeListener () {
			public void widgetDisposed (DisposeEvent e) {
				boldImage.dispose();
				italicImage.dispose();
				redImage.dispose();
				yellowImage.dispose();
				underlineImage.dispose();
				strikeoutImage.dispose();
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
	 * Disposes the "Example" widgets.
	 */
	void disposeExampleWidgets () {
		/* store the state of the styledText if applicable */
		if (styledText != null) {
			styleRanges = styledText.getStyleRanges();
			text = styledText.getText();
		}
		super.disposeExampleWidgets();	
	}

	/**
	 * Gets the list of custom event names.
	 * 
	 * @return an array containing custom event names
	 */
	String [] getCustomEventNames () {
		return new String [] {
				"ExtendedModifyListener", "BidiSegmentListener", "LineBackgroundListener",
				"LineStyleListener", "PaintObjectListener", "TextChangeListener",
				"VerifyKeyListener", "WordMovementListener"};
	}

	/**
	 * Gets the "Example" widget children.
	 */
	Widget [] getExampleWidgets () {
		return new Widget [] {styledText};
	}
	
	/**
	 * Returns a list of set/get API method names (without the set/get prefix)
	 * that can be used to set/get values in the example control(s).
	 */
	String[] getMethodNames() {
		return new String[] {"Alignment", "BlockSelection", "BottomMargin", "CaretOffset", "DoubleClickEnabled", "Editable", "HorizontalIndex", "HorizontalPixel", "Indent", "Justify", "LeftMargin", "LineSpacing", "Orientation", "RightMargin", "Selection", "Tabs", "Text", "TextLimit", "ToolTipText", "TopIndex", "TopMargin", "TopPixel", "WordWrap"};
	}

	
	/**
	 * Gets the text for the tab folder item.
	 */
	String getTabText () {
		return "StyledText";
	}
	
	/**
	 * Hooks the custom listener specified by eventName.
	 */
	void hookCustomListener (final String eventName) {
		if (eventName == "ExtendedModifyListener") {
			styledText.addExtendedModifyListener (new ExtendedModifyListener() {
				public void modifyText(ExtendedModifyEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "BidiSegmentListener") {
			styledText.addBidiSegmentListener (new BidiSegmentListener() {
				public void lineGetSegments(BidiSegmentEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "LineBackgroundListener") {
			styledText.addLineBackgroundListener (new LineBackgroundListener() {
				public void lineGetBackground(LineBackgroundEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "LineStyleListener") {
			styledText.addLineStyleListener (new LineStyleListener() {
				public void lineGetStyle(LineStyleEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "PaintObjectListener") {
			styledText.addPaintObjectListener (new PaintObjectListener() {
				public void paintObject(PaintObjectEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "TextChangeListener") {
			styledText.getContent().addTextChangeListener (new TextChangeListener() {
				public void textChanged(TextChangedEvent event) {
					log (eventName + ".textChanged", event);
				}
				public void textChanging(TextChangingEvent event) {
					log (eventName + ".textChanging", event);
				}
				public void textSet(TextChangedEvent event) {
					log (eventName + ".textSet", event);
				}
			});
		}
		if (eventName == "VerifyKeyListener") {
			styledText.addVerifyKeyListener (new VerifyKeyListener() {
				public void verifyKey(VerifyEvent event) {
					log (eventName, event);
				}
			});
		}
		if (eventName == "WordMovementListener") {
			styledText.addWordMovementListener (new MovementListener() {
				public void getNextOffset(MovementEvent event) {
					log (eventName + ".getNextOffset", event);
				}
				public void getPreviousOffset(MovementEvent event) {
					log (eventName + ".getPreviousOffset", event);
				}
			});
		}
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
