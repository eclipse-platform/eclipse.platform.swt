/*****************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.examples.texteditor;

import java.io.*;
import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.browser.*;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class TextEditor {
	Display display;
	Shell shell;
	CoolBar coolBar;
	StyledText styledText;
	Label statusBar;
	ToolItem boldControl, italicControl, leftAlignmentItem, centerAlignmentItem, rightAlignmentItem, justifyAlignmentItem, blockSelectionItem;
	Combo fontNameControl, fontSizeControl;
	MenuItem underlineSingleItem, underlineDoubleItem, underlineErrorItem, underlineSquiggleItem, borderSolidItem, borderDashItem, borderDotItem;
	
	boolean insert = true;
	StyleRange[] selectedRanges;
	int newCharCount, start;
	String fileName = null;
	int styleState;
	String link;


	// Resources
	Image iBold, iItalic, iUnderline, iStrikeout, iLeftAlignment, iRightAlignment, iCenterAlignment, iJustifyAlignment, iCopy, iCut, iLink;
	Image iPaste, iSpacing, iIndent, iTextForeground, iTextBackground, iBaselineUp, iBaselineDown, iBulletList, iNumberedList, iBlockSelection, iBorderStyle;
	Font font, textFont;
	Color textForeground, textBackground, strikeoutColor, underlineColor, borderColor;

	static final int BULLET_WIDTH = 40;
	static final int MARGIN = 5;
	static final int BOLD = SWT.BOLD;
	static final int ITALIC = SWT.ITALIC;
	static final int FONT_STYLE = BOLD | ITALIC;
	static final int STRIKEOUT = 1 << 3;
	static final int FOREGROUND = 1 << 4;
	static final int BACKGROUND = 1 << 5;
	static final int FONT = 1 << 6;
	static final int BASELINE_UP = 1 << 7;
	static final int BASELINE_DOWN = 1 << 8;
	static final int UNDERLINE_SINGLE = 1 << 9;
	static final int UNDERLINE_DOUBLE = 1 << 10;
	static final int UNDERLINE_ERROR = 1 << 11;
	static final int UNDERLINE_SQUIGGLE = 1 << 12;
	static final int UNDERLINE_LINK = 1 << 13;
	static final int UNDERLINE = UNDERLINE_SINGLE | UNDERLINE_DOUBLE | UNDERLINE_SQUIGGLE | UNDERLINE_ERROR | UNDERLINE_LINK;
	static final int BORDER_SOLID = 1 << 23;
	static final int BORDER_DASH = 1 << 24;
	static final int BORDER_DOT = 1 << 25;
	static final int BORDER = BORDER_SOLID | BORDER_DASH | BORDER_DOT;
	
	static final boolean SAMPLE_TEXT = false;
	static final boolean USE_BASELINE = false;

	static final String[] FONT_SIZES = new String[] {
			"6",		//$NON-NLS-1$
			"8", 		//$NON-NLS-1$
			"9", 		//$NON-NLS-1$
			"10", 		//$NON-NLS-1$
			"11", 		//$NON-NLS-1$
			"12",	 	//$NON-NLS-1$
			"14",		//$NON-NLS-1$
			"24",		//$NON-NLS-1$
			"36",		//$NON-NLS-1$
			"48" 		//$NON-NLS-1$
	};
	
	static final ResourceBundle resources = ResourceBundle.getBundle("examples_texteditor");  //$NON-NLS-1$

	static String getResourceString(String key) {
		try {
			return resources.getString(key);
		} catch (MissingResourceException e) {
			return key;
		} catch (NullPointerException e) {
			return "!" + key + "!";  //$NON-NLS-1$ //$NON-NLS-2$
		}
	}
	
	public static void main(String[] args) {
		Display display = new Display();
		TextEditor editor = new TextEditor(display);
		Shell shell = editor.shell;
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		editor.releaseResources();
		display.dispose();
	}

	public TextEditor(Display display) {
		this.display = display;
		initResources();
		shell = new Shell(display);
		shell.setText(getResourceString("Window_title")); //$NON-NLS-1$
		styledText = new StyledText(shell, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		createMenuBar();
		createToolBar();
		createPopup();
		statusBar = new Label(shell, SWT.NONE);
		installListeners();
		updateToolBar();
		updateStatusBar();
		shell.setSize(1000, 700);
		shell.open();
	}
	
	void addControl(Control control) {
		int offset = styledText.getCaretOffset();
		styledText.replaceTextRange(offset, 0, "\uFFFC"); //$NON-NLS-1$
		StyleRange style = new StyleRange();
		Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int ascent = 2 * size.y / 3;
		int descent = size.y - ascent;
		style.metrics = new GlyphMetrics(ascent + MARGIN, descent + MARGIN, size.x + 2 * MARGIN);
		style.data = control;
		int[] ranges = {offset, 1};
		StyleRange[] styles = {style};
		styledText.setStyleRanges(0,0, ranges, styles);
		control.setSize(size);
	}

	void addImage(Image image) {
		int offset = styledText.getCaretOffset();
		styledText.replaceTextRange(offset, 0, "\uFFFC"); //$NON-NLS-1$
		StyleRange style = new StyleRange();
		Rectangle rect = image.getBounds();
		style.metrics = new GlyphMetrics(rect.height, 0, rect.width);
		style.data = image;
		int[] ranges = {offset, 1};
		StyleRange[] styles = {style};
		styledText.setStyleRanges(0,0, ranges, styles);
	}
	
	void adjustFontSize (int increment) {
		int newIndex = fontSizeControl.getSelectionIndex() + increment;
		if (0 <= newIndex && newIndex < fontSizeControl.getItemCount()) {
			disposeResource(textFont);
			String name = fontNameControl.getText();
			int size = Integer.parseInt(fontSizeControl.getItem(newIndex));
			textFont = new Font(display, name, size, SWT.NORMAL);
			setStyle(FONT);
			updateToolBar();
		}
	}

	void createMenuBar() {
		Menu menu = new Menu(shell, SWT.BAR);
		shell.setMenuBar(menu);

		MenuItem fileItem = new MenuItem(menu, SWT.CASCADE);
		Menu fileMenu = new Menu(shell, SWT.DROP_DOWN);
		fileItem.setText(getResourceString("File_menuitem")); //$NON-NLS-1$
		fileItem.setMenu(fileMenu);		
		
		MenuItem openItem = new MenuItem(fileMenu, SWT.PUSH);
		openItem.setText(getResourceString("Open_menuitem")); //$NON-NLS-1$
		openItem.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				FileDialog dialog = new FileDialog(shell, SWT.OPEN);
				dialog.setFilterNames(new String [] {getResourceString("Text_Documents")}); //$NON-NLS-1$
				dialog.setFilterExtensions (new String [] {"*.txt"}); //$NON-NLS-1$
		        String name = dialog.open();
		        if (name == null)  return;
		        fileName = name;
		        FileInputStream file = null;
		        try {
		        	file = new FileInputStream(name);
		        	styledText.setText(openFile(file));
		        } catch (IOException e) {
		        	showError(getResourceString("Error"), e.getMessage()); //$NON-NLS-1$
		        } finally {
		        	try {
		        		if (file != null) file.close();
		        	} catch (IOException e) {
		        		showError(getResourceString("Error"), e.getMessage()); //$NON-NLS-1$
		        	}
		        }
			}					
		});
		
		final MenuItem saveItem = new MenuItem(fileMenu, SWT.PUSH);
		saveItem.setText(getResourceString("Save_menuitem")); //$NON-NLS-1$
		saveItem.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				saveFile();
			}											
		});
		
		fileMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent event){
				saveItem.setEnabled(fileName != null);
			}			 			
		});
		
		MenuItem saveAsItem = new MenuItem(fileMenu, SWT.PUSH);
		saveAsItem.setText(getResourceString("SaveAs_menuitem")); //$NON-NLS-1$
		saveAsItem.addSelectionListener(new SelectionAdapter () {
			public void widgetSelected(SelectionEvent event) {
				FileDialog dialog = new FileDialog (shell, SWT.SAVE);
				dialog.setFilterNames(new String [] {getResourceString("Text_Documents")}); //$NON-NLS-1$ 
				dialog.setFilterExtensions(new String [] {"*.txt"}); //$NON-NLS-1$
				if (fileName != null) dialog.setFileName(fileName);
				String name = dialog.open(); 
				if (name != null) {
					fileName = name;
					saveFile();
				}
			}
		});
		
		new MenuItem(fileMenu, SWT.SEPARATOR);
		
		MenuItem exitItem = new MenuItem(fileMenu, SWT.PUSH);
		exitItem.setText(getResourceString("Exit_menuitem")); //$NON-NLS-1$
		exitItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				shell.dispose();
			}
		});	

		MenuItem editItem = new MenuItem(menu, SWT.CASCADE);
		final Menu editMenu = new Menu(shell, SWT.DROP_DOWN);
		editItem.setText(getResourceString("Edit_menuitem")); //$NON-NLS-1$
		editItem.setMenu(editMenu);
		final MenuItem cutItem = new MenuItem(editMenu, SWT.PUSH);
		cutItem.setText(getResourceString("Cut_menuitem")); //$NON-NLS-1$
		cutItem.setImage(iCut);
		cutItem.setAccelerator(SWT.MOD1 | 'x');
		cutItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.cut();
			}
		});

		final MenuItem copyItem = new MenuItem(editMenu, SWT.PUSH);
		copyItem.setText(getResourceString("Copy_menuitem")); //$NON-NLS-1$
		copyItem.setImage(iCopy);
		copyItem.setAccelerator(SWT.MOD1 | 'c');
		copyItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.copy();
			}
		});

		MenuItem pasteItem = new MenuItem(editMenu, SWT.PUSH);
		pasteItem.setText(getResourceString("Paste_menuitem")); //$NON-NLS-1$
		pasteItem.setImage(iPaste);
		pasteItem.setAccelerator(SWT.MOD1 | 'v');
		pasteItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.paste();
			}
		});

		new MenuItem(editMenu, SWT.SEPARATOR);
		final MenuItem selectAllItem = new MenuItem(editMenu, SWT.PUSH);
		selectAllItem.setText(getResourceString("SelectAll_menuitem")); //$NON-NLS-1$
		selectAllItem.setAccelerator(SWT.MOD1 | 'a');
		selectAllItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.selectAll();
			}
		});

		editMenu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent event) {
				int selectionCount = styledText.getSelectionCount();
				cutItem.setEnabled(selectionCount > 0);
				copyItem.setEnabled(selectionCount > 0);
				selectAllItem.setEnabled(selectionCount < styledText.getCharCount());
			}
		});

		MenuItem wrapItem = new MenuItem(editMenu, SWT.CHECK);
		wrapItem.setText(getResourceString("Wrap_menuitem")); //$NON-NLS-1$
		wrapItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				MenuItem item = (MenuItem) event.widget;
				boolean enabled = item.getSelection();
				styledText.setWordWrap(enabled);
				editMenu.getItem(6).setEnabled(enabled);
				editMenu.getItem(8).setEnabled(enabled);
				leftAlignmentItem.setEnabled(enabled);
				centerAlignmentItem.setEnabled(enabled);
				rightAlignmentItem.setEnabled(enabled);
				justifyAlignmentItem.setEnabled(enabled);
				blockSelectionItem.setEnabled(!enabled);
			}
		});

		MenuItem justifyItem = new MenuItem(editMenu, SWT.CHECK);
		justifyItem.setText(getResourceString("Justify_menuitem")); //$NON-NLS-1$
		justifyItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				MenuItem item = (MenuItem) event.widget;
				styledText.setJustify(item.getSelection());
				updateToolBar();
			}
		});
		justifyItem.setEnabled(false);

		MenuItem setFontItem = new MenuItem(editMenu, SWT.PUSH);
		setFontItem.setText(getResourceString("SetFont_menuitem")); //$NON-NLS-1$
		setFontItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FontDialog fontDialog = new FontDialog(shell);
				fontDialog.setFontList(styledText.getFont().getFontData());
				FontData data = fontDialog.open();
				if (data != null) {
					Font newFont = new Font(display, data);
					styledText.setFont(newFont);
					if (font != null) font.dispose();
					font = newFont;
					updateToolBar();
				}
			}
		});

		MenuItem alignmentItem = new MenuItem(editMenu, SWT.CASCADE);
		alignmentItem.setText(getResourceString("Alignment_menuitem")); //$NON-NLS-1$
		Menu alignmentMenu = new Menu(shell, SWT.DROP_DOWN);
		alignmentItem.setMenu(alignmentMenu);
		final MenuItem leftAlignmentItem = new MenuItem(alignmentMenu, SWT.RADIO);
		leftAlignmentItem.setText(getResourceString("Left_menuitem")); //$NON-NLS-1$
		leftAlignmentItem.setSelection(true);
		leftAlignmentItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.setAlignment(SWT.LEFT);
				updateToolBar();
			}
		});
		alignmentItem.setEnabled(false);

		final MenuItem centerAlignmentItem = new MenuItem(alignmentMenu, SWT.RADIO);
		centerAlignmentItem.setText(getResourceString("Center_menuitem")); //$NON-NLS-1$
		centerAlignmentItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.setAlignment(SWT.CENTER);
				updateToolBar();
			}
		});

		MenuItem rightAlignmentItem = new MenuItem(alignmentMenu, SWT.RADIO);
		rightAlignmentItem.setText(getResourceString("Right_menuitem")); //$NON-NLS-1$
		rightAlignmentItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.setAlignment(SWT.RIGHT);
				updateToolBar();
			}
		});
		
		MenuItem editOrientationItem = new MenuItem(editMenu, SWT.CASCADE);
		editOrientationItem.setText(getResourceString("Orientation_menuitem")); //$NON-NLS-1$
		Menu editOrientationMenu = new Menu(shell, SWT.DROP_DOWN);
		editOrientationItem.setMenu(editOrientationMenu);

		MenuItem leftToRightItem = new MenuItem(editOrientationMenu, SWT.RADIO);
		leftToRightItem.setText(getResourceString("LeftToRight_menuitem")); //$NON-NLS-1$
		leftToRightItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){
				styledText.setOrientation(SWT.LEFT_TO_RIGHT);
			}
		});
		leftToRightItem.setSelection(true);
		
		MenuItem rightToLeftItem = new MenuItem(editOrientationMenu, SWT.RADIO);
		rightToLeftItem.setText(getResourceString("RightToLeft_menuitem")); //$NON-NLS-1$
		rightToLeftItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.setOrientation(SWT.RIGHT_TO_LEFT);
			}
		});
		
		new MenuItem(editMenu, SWT.SEPARATOR);
		MenuItem insertObjectItem = new MenuItem(editMenu, SWT.CASCADE);
		insertObjectItem.setText(getResourceString("InsertObject_menuitem")); //$NON-NLS-1$
		Menu insertObjectMenu = new Menu(shell, SWT.DROP_DOWN);
		insertObjectItem.setMenu(insertObjectMenu);

		MenuItem insertControlItem = new MenuItem(insertObjectMenu, SWT.CASCADE);
		insertControlItem.setText(getResourceString("Controls_menuitem")); //$NON-NLS-1$
		Menu controlChoice = new Menu(shell, SWT.DROP_DOWN);
		insertControlItem.setMenu(controlChoice);

		MenuItem buttonItem = new MenuItem(controlChoice, SWT.PUSH);
		buttonItem.setText(getResourceString("Button_menuitem")); //$NON-NLS-1$
		MenuItem comboItem = new MenuItem(controlChoice, SWT.PUSH);
		comboItem.setText(getResourceString("Combo_menuitem")); //$NON-NLS-1$

		buttonItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Button button = new Button(styledText, SWT.PUSH);
				button.setText(getResourceString("Button_menuitem")); //$NON-NLS-1$
				addControl(button);
			}
		});

		comboItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Combo combo = new Combo(styledText, SWT.NONE);
				combo.setText(getResourceString("Combo_menuitem")); //$NON-NLS-1$
				addControl(combo);
			}
		});

		MenuItem insertImageItem = new MenuItem(insertObjectMenu, SWT.PUSH);
		insertImageItem.setText(getResourceString("Image_menuitem")); //$NON-NLS-1$

		insertImageItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				FileDialog fileDialog = new FileDialog(shell, SWT.OPEN);
				String fileName = fileDialog.open();
				if (fileName != null) {
					try {
						Image image = new Image(display, fileName);
						addImage(image);
					} catch (Exception e) {
						showError(getResourceString("Bad_image"), e.getMessage()); //$NON-NLS-1$
					}
				}
			}
		});

		if (SAMPLE_TEXT) {
			new MenuItem(editMenu, SWT.SEPARATOR);
			MenuItem loadProfileItem = new MenuItem(editMenu, SWT.CASCADE);
			loadProfileItem.setText(getResourceString("LoadProfile_menuitem")); //$NON-NLS-1$
			Menu loadProfileMenu = new Menu(shell, SWT.DROP_DOWN);
			loadProfileItem.setMenu(loadProfileMenu);
			SelectionAdapter adapter = new SelectionAdapter() {
				public void widgetSelected(SelectionEvent event) {
					int profile = Integer.parseInt((String) event.widget.getData());
					loadProfile(profile);
				}
			};
	
			MenuItem profileItem = new MenuItem(loadProfileMenu, SWT.PUSH);
			profileItem.setText(getResourceString("Profile1_menuitem")); //$NON-NLS-1$
			profileItem.setData("1"); //$NON-NLS-1$
			profileItem.addSelectionListener(adapter);
			profileItem = new MenuItem(loadProfileMenu, SWT.PUSH);
			profileItem.setText(getResourceString("Profile2_menuitem")); //$NON-NLS-1$
			profileItem.setData("2"); //$NON-NLS-1$
			profileItem.addSelectionListener(adapter);
			profileItem = new MenuItem(loadProfileMenu, SWT.PUSH);
			profileItem.setText(getResourceString("Profile3_menuitem")); //$NON-NLS-1$
			profileItem.setData("3"); //$NON-NLS-1$
			profileItem.addSelectionListener(adapter);
			profileItem = new MenuItem(loadProfileMenu, SWT.PUSH);
			profileItem.setText(getResourceString("Profile4_menuitem")); //$NON-NLS-1$
			profileItem.setData("4"); //$NON-NLS-1$
			profileItem.addSelectionListener(adapter);
		}
	}

	void createPopup() {
		Menu menu = new Menu (styledText);
		final MenuItem cutItem = new MenuItem (menu, SWT.PUSH);
		cutItem.setText (getResourceString("Cut_menuitem")); //$NON-NLS-1$
		cutItem.setImage(iCut);
		cutItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				styledText.cut();
			}
		});
		final MenuItem copyItem = new MenuItem (menu, SWT.PUSH);
		copyItem.setText (getResourceString("Copy_menuitem")); //$NON-NLS-1$
		copyItem.setImage(iCopy);
		copyItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				styledText.copy();
			}
		});
		final MenuItem pasteItem = new MenuItem (menu, SWT.PUSH);
		pasteItem.setText (getResourceString("Paste_menuitem")); //$NON-NLS-1$
		pasteItem.setImage(iPaste);
		pasteItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				styledText.paste();
			}
		});
		new MenuItem (menu, SWT.SEPARATOR);
		final MenuItem selectAllItem = new MenuItem (menu, SWT.PUSH);
		selectAllItem.setText (getResourceString("SelectAll_menuitem")); //$NON-NLS-1$
		selectAllItem.addListener (SWT.Selection, new Listener () {
			public void handleEvent (Event event) {
				styledText.selectAll();
			}
		});
		menu.addMenuListener(new MenuAdapter() {
			public void menuShown(MenuEvent event) {
				int selectionCount = styledText.getSelectionCount();
				cutItem.setEnabled(selectionCount > 0);
				copyItem.setEnabled(selectionCount > 0);
				selectAllItem.setEnabled(selectionCount < styledText.getCharCount());
			}
		});
		styledText.setMenu(menu);
	}

	void createToolBar() {
		coolBar = new CoolBar(shell, SWT.FLAT);
		ToolBar styleToolBar = new ToolBar(coolBar, SWT.FLAT);
		boldControl = new ToolItem(styleToolBar, SWT.CHECK);
		boldControl.setImage(iBold);
		boldControl.setToolTipText(getResourceString("Bold")); //$NON-NLS-1$
		boldControl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setStyle(BOLD);
			}
		});

		italicControl = new ToolItem(styleToolBar, SWT.CHECK);
		italicControl.setImage(iItalic);
		italicControl.setToolTipText(getResourceString("Italic")); //$NON-NLS-1$
		italicControl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setStyle(ITALIC);
			}
		});

		final Menu underlineMenu = new Menu(shell, SWT.POP_UP);
		underlineSingleItem = new MenuItem(underlineMenu, SWT.RADIO);
		underlineSingleItem.setText(getResourceString("Single_menuitem")); //$NON-NLS-1$
		underlineSingleItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (underlineSingleItem.getSelection()) {
					setStyle(UNDERLINE_SINGLE);
				}
			}
		});
		underlineSingleItem.setSelection(true);

		underlineDoubleItem = new MenuItem(underlineMenu, SWT.RADIO);
		underlineDoubleItem.setText(getResourceString("Double_menuitem")); //$NON-NLS-1$
		underlineDoubleItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (underlineDoubleItem.getSelection()) {
					setStyle(UNDERLINE_DOUBLE);
				}
			}
		});

		underlineSquiggleItem = new MenuItem(underlineMenu, SWT.RADIO);
		underlineSquiggleItem.setText(getResourceString("Squiggle_menuitem")); //$NON-NLS-1$
		underlineSquiggleItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (underlineSquiggleItem.getSelection()) {
					setStyle(UNDERLINE_SQUIGGLE);
				}
			}
		});

		underlineErrorItem = new MenuItem(underlineMenu, SWT.RADIO);
		underlineErrorItem.setText(getResourceString("Error_menuitem")); //$NON-NLS-1$
		underlineErrorItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (underlineErrorItem.getSelection()) {
					setStyle(UNDERLINE_ERROR);
				}
			}
		});

		MenuItem underlineColorItem = new MenuItem(underlineMenu, SWT.PUSH);
		underlineColorItem.setText(getResourceString("Color_menuitem")); //$NON-NLS-1$
		underlineColorItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				ColorDialog dialog = new ColorDialog(shell);
				RGB rgb = underlineColor != null ? underlineColor.getRGB() : null;
				dialog.setRGB(rgb);
				RGB newRgb = dialog.open();
				if (newRgb != null) {
					if (!newRgb.equals(rgb)) {
						disposeResource(underlineColor);
						underlineColor = new Color(display, newRgb);					
					}
					if (underlineSingleItem.getSelection()) setStyle(UNDERLINE_SINGLE);
					else if (underlineDoubleItem.getSelection()) setStyle(UNDERLINE_DOUBLE);
					else if (underlineErrorItem.getSelection()) setStyle(UNDERLINE_ERROR);
					else if (underlineSquiggleItem.getSelection()) setStyle(UNDERLINE_SQUIGGLE);
				}
			}
		});
		
		final ToolItem underlineControl = new ToolItem(styleToolBar, SWT.DROP_DOWN);
		underlineControl.setImage(iUnderline);
		underlineControl.setToolTipText(getResourceString("Underline")); //$NON-NLS-1$
		underlineControl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == SWT.ARROW) {
					Rectangle rect = underlineControl.getBounds();
					Point pt = new Point(rect.x, rect.y + rect.height);
					underlineMenu.setLocation(display.map(underlineControl.getParent(), null, pt));
					underlineMenu.setVisible(true);
				} else {
					if (underlineSingleItem.getSelection()) setStyle(UNDERLINE_SINGLE);
					else if (underlineDoubleItem.getSelection()) setStyle(UNDERLINE_DOUBLE);
					else if (underlineErrorItem.getSelection()) setStyle(UNDERLINE_ERROR);
					else if (underlineSquiggleItem.getSelection()) setStyle(UNDERLINE_SQUIGGLE);
				}
			}
		});

		ToolItem strikeoutControl = new ToolItem(styleToolBar, SWT.DROP_DOWN);
		strikeoutControl.setImage(iStrikeout);
		strikeoutControl.setToolTipText(getResourceString("Strikeout")); //$NON-NLS-1$
		strikeoutControl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == SWT.ARROW) {
					ColorDialog dialog = new ColorDialog(shell);
					RGB rgb = strikeoutColor != null ? strikeoutColor.getRGB() : null;
					dialog.setRGB(rgb);
					RGB newRgb = dialog.open();
					if (newRgb == null) return;
					if (!newRgb.equals(rgb)) {
						disposeResource(strikeoutColor);
						strikeoutColor = new Color(display, newRgb);
					}
				}
				setStyle(STRIKEOUT);
			}
		});

		final Menu borderMenu = new Menu(shell, SWT.POP_UP);
		borderSolidItem = new MenuItem(borderMenu, SWT.RADIO);
		borderSolidItem.setText(getResourceString("Solid")); //$NON-NLS-1$
		borderSolidItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){
				if (borderSolidItem.getSelection()) {
					setStyle(BORDER_SOLID);
				}
			}
		});
		borderSolidItem.setSelection(true);
		
		borderDashItem = new MenuItem(borderMenu, SWT.RADIO);
		borderDashItem.setText(getResourceString("Dash")); //$NON-NLS-1$
		borderDashItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){
				if (borderDashItem.getSelection()) {
					setStyle(BORDER_DASH);
				}
			}
		});
		
		borderDotItem = new MenuItem(borderMenu, SWT.RADIO);
		borderDotItem.setText(getResourceString("Dot")); //$NON-NLS-1$
		borderDotItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){
				if (borderDotItem.getSelection()) {
					setStyle(BORDER_DOT);
				}
			}
		});
		
		MenuItem borderColorItem = new MenuItem(borderMenu, SWT.PUSH);
		borderColorItem.setText(getResourceString("Color_menuitem")); //$NON-NLS-1$
		borderColorItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event){
				ColorDialog dialog = new ColorDialog(shell);
				RGB rgb = borderColor != null ? borderColor.getRGB() : null;
				dialog.setRGB(rgb);
				RGB newRgb = dialog.open();
				if (newRgb != null) {
					if (!newRgb.equals(rgb)) {
						disposeResource(borderColor);
						borderColor = new Color(display, newRgb);
					}
					if (borderDashItem.getSelection()) setStyle(BORDER_DASH);
					else if (borderDotItem.getSelection()) setStyle(BORDER_DOT);
					else if (borderSolidItem.getSelection()) setStyle(BORDER_SOLID);
				}
			}
		});

		final ToolItem borderControl = new ToolItem(styleToolBar, SWT.DROP_DOWN);
		borderControl.setImage(iBorderStyle);
		borderControl.setToolTipText(getResourceString("Box")); //$NON-NLS-1$
		borderControl.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (event.detail == SWT.ARROW) {
					Rectangle rect = borderControl.getBounds();
					Point pt = new Point(rect.x, rect.y + rect.height);
					borderMenu.setLocation(display.map(borderControl.getParent(), null, pt));
					borderMenu.setVisible(true);
				} else {
					if (borderDashItem.getSelection()) setStyle(BORDER_DASH);
					else if (borderDotItem.getSelection()) setStyle(BORDER_DOT);
					else if (borderSolidItem.getSelection()) setStyle(BORDER_SOLID);
				}
			}
		});

		ToolItem foregroundItem = new ToolItem(styleToolBar, SWT.DROP_DOWN);
		foregroundItem.setImage(iTextForeground);
		foregroundItem.setToolTipText(getResourceString("TextForeground")); //$NON-NLS-1$
		foregroundItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {		
				if (event.detail == SWT.ARROW || textForeground == null) {
					ColorDialog dialog = new ColorDialog(shell);
					RGB rgb = textForeground != null ? textForeground.getRGB() : null;
					dialog.setRGB(rgb);
					RGB newRgb = dialog.open();
					if (newRgb == null) return;
					if (!newRgb.equals(rgb)) {
						disposeResource(textForeground);
						textForeground = new Color(display, newRgb);					
					}
				}
				setStyle(FOREGROUND);				
			}
		});

		ToolItem backgroundItem = new ToolItem(styleToolBar, SWT.DROP_DOWN);
		backgroundItem.setImage(iTextBackground);
		backgroundItem.setToolTipText(getResourceString("TextBackground")); //$NON-NLS-1$
		backgroundItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {			
				if (event.detail == SWT.ARROW || textBackground == null) {
					ColorDialog dialog = new ColorDialog(shell);
					RGB rgb = textBackground != null ? textBackground.getRGB() : null;
					dialog.setRGB(rgb);
					RGB newRgb = dialog.open();
					if (newRgb == null) return;
					if (!newRgb.equals(rgb)) {
						disposeResource(textBackground);
						textBackground = new Color(display, newRgb);
					}
				}
				setStyle(BACKGROUND);
			}
		});

		ToolItem baselineUpItem = new ToolItem(styleToolBar, SWT.PUSH);
		baselineUpItem.setImage(iBaselineUp);
		String tooltip = "IncreaseFont"; //$NON-NLS-1$
		if (USE_BASELINE) tooltip = "IncreaseBaseline"; //$NON-NLS-1$
		baselineUpItem.setToolTipText(getResourceString(tooltip));
		baselineUpItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (USE_BASELINE) {
					setStyle(BASELINE_UP);
				} else {
					adjustFontSize(1);
				}
			}
		});

		ToolItem baselineDownItem = new ToolItem(styleToolBar, SWT.PUSH);
		baselineDownItem.setImage(iBaselineDown);
		tooltip = "DecreaseFont"; //$NON-NLS-1$
		if (USE_BASELINE) tooltip = "DecreaseBaseline"; //$NON-NLS-1$
		baselineDownItem.setToolTipText(getResourceString(tooltip));
		baselineDownItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				if (USE_BASELINE) {
					setStyle(BASELINE_DOWN);
				} else {
					adjustFontSize(-1);
				}
			}
		});
		ToolItem linkItem = new ToolItem(styleToolBar, SWT.PUSH);
		linkItem.setImage(iLink);
		linkItem.setToolTipText(getResourceString("Link")); //$NON-NLS-1$
		linkItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setLink();
			}
		});
		
		CoolItem coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(styleToolBar);
		
		Composite composite = new Composite(coolBar, SWT.NONE);
		GridLayout layout = new GridLayout(2, false);
		layout.marginHeight = 1;
		composite.setLayout(layout);
		fontNameControl = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		fontNameControl.setItems(getFontNames());
		fontNameControl.setVisibleItemCount(12);
		fontSizeControl = new Combo(composite, SWT.DROP_DOWN | SWT.READ_ONLY);
		fontSizeControl.setItems(FONT_SIZES);
		fontSizeControl.setVisibleItemCount(8);
		SelectionAdapter adapter = new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				String name = fontNameControl.getText();
				int size = Integer.parseInt(fontSizeControl.getText());
				disposeResource(textFont);
				textFont = new Font(display, name, size, SWT.NORMAL);
				setStyle(FONT);
			}
		};
		fontSizeControl.addSelectionListener(adapter);
		fontNameControl.addSelectionListener(adapter);
		coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(composite);

		ToolBar alignmentToolBar = new ToolBar(coolBar, SWT.FLAT);
		blockSelectionItem = new ToolItem(alignmentToolBar, SWT.CHECK);
		blockSelectionItem.setImage(iBlockSelection);
		blockSelectionItem.setToolTipText(getResourceString("BlockSelection")); //$NON-NLS-1$
		blockSelectionItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				styledText.invokeAction(ST.TOGGLE_BLOCKSELECTION);
			}
		});
		
		leftAlignmentItem = new ToolItem(alignmentToolBar, SWT.RADIO);
		leftAlignmentItem.setImage(iLeftAlignment);
		leftAlignmentItem.setToolTipText(getResourceString("AlignLeft")); //$NON-NLS-1$
		leftAlignmentItem.setSelection(true);
		leftAlignmentItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Point selection = styledText.getSelection();
				int lineStart = styledText.getLineAtOffset(selection.x);
				int lineEnd = styledText.getLineAtOffset(selection.y);
				styledText.setLineAlignment(lineStart, lineEnd - lineStart + 1,	SWT.LEFT);
			}
		});
		leftAlignmentItem.setEnabled(false);

		centerAlignmentItem = new ToolItem(alignmentToolBar, SWT.RADIO);
		centerAlignmentItem.setImage(iCenterAlignment);
		centerAlignmentItem.setToolTipText(getResourceString("Center_menuitem")); //$NON-NLS-1$
		centerAlignmentItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Point selection = styledText.getSelection();
				int lineStart = styledText.getLineAtOffset(selection.x);
				int lineEnd = styledText.getLineAtOffset(selection.y);
				styledText.setLineAlignment(lineStart, lineEnd - lineStart + 1, SWT.CENTER);
			}
		});
		centerAlignmentItem.setEnabled(false);

		rightAlignmentItem = new ToolItem(alignmentToolBar, SWT.RADIO);
		rightAlignmentItem.setImage(iRightAlignment);
		rightAlignmentItem.setToolTipText(getResourceString("AlignRight")); //$NON-NLS-1$
		rightAlignmentItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Point selection = styledText.getSelection();
				int lineStart = styledText.getLineAtOffset(selection.x);
				int lineEnd = styledText.getLineAtOffset(selection.y);
				styledText.setLineAlignment(lineStart, lineEnd - lineStart + 1,	SWT.RIGHT);
			}
		});
		rightAlignmentItem.setEnabled(false);

		justifyAlignmentItem = new ToolItem(alignmentToolBar, SWT.CHECK);
		justifyAlignmentItem.setImage(iJustifyAlignment);
		justifyAlignmentItem.setToolTipText(getResourceString("Justify")); //$NON-NLS-1$
		justifyAlignmentItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Point selection = styledText.getSelection();
				int lineStart = styledText.getLineAtOffset(selection.x);
				int lineEnd = styledText.getLineAtOffset(selection.y);
				styledText.setLineJustify(lineStart, lineEnd - lineStart + 1, justifyAlignmentItem.getSelection());
			}
		});
		justifyAlignmentItem.setEnabled(false);

		ToolItem bulletListItem = new ToolItem(alignmentToolBar, SWT.PUSH);
		bulletListItem.setImage(iBulletList);
		bulletListItem.setToolTipText(getResourceString("BulletList")); //$NON-NLS-1$
		bulletListItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setBullet(ST.BULLET_DOT);}
		});

		ToolItem numberedListItem = new ToolItem(alignmentToolBar, SWT.PUSH);
		numberedListItem.setImage(iNumberedList);
		numberedListItem.setToolTipText(getResourceString("NumberedList")); //$NON-NLS-1$
		numberedListItem.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				setBullet(ST.BULLET_CUSTOM);
			}
		});

		coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(alignmentToolBar);
		composite = new Composite(coolBar, SWT.NONE);
		layout = new GridLayout(4, false);
		layout.marginHeight = 1;
		composite.setLayout(layout);
		Label label = new Label(composite, SWT.NONE);
		label.setText(getResourceString("Indent")); //$NON-NLS-1$
		Spinner indent = new Spinner(composite, SWT.BORDER);
		indent.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Spinner spinner = (Spinner) event.widget;
				styledText.setIndent(spinner.getSelection());
			}
		});
		label = new Label(composite, SWT.NONE);
		label.setText(getResourceString("Spacing")); //$NON-NLS-1$
		Spinner spacing = new Spinner(composite, SWT.BORDER);
		spacing.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent event) {
				Spinner spinner = (Spinner) event.widget;
				styledText.setLineSpacing(spinner.getSelection());
			}
		});

		coolItem = new CoolItem(coolBar, SWT.NONE);
		coolItem.setControl(composite);
		CoolItem[] coolItems = coolBar.getItems();
		for (int i = 0; i < coolItems.length; i++) {
			CoolItem item = coolItems[i];
			Control control = item.getControl();
			Point size = control.computeSize(SWT.DEFAULT, SWT.DEFAULT);
			item.setMinimumSize(size);
			size = item.computeSize(size.x, size.y);
			item.setPreferredSize(size);
			item.setSize(size);
		}
		coolBar.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				handleResize(event);
			}
		});
	}

	void disposeRanges(StyleRange[] ranges) {
		StyleRange[] allRanges = styledText.getStyleRanges(0, styledText.getCharCount(), false);
		for (int i = 0; i < ranges.length; i++) {
			StyleRange style = ranges[i];
			boolean disposeFg = true, disposeBg = true, disposeStrike= true, disposeUnder= true, disposeBorder = true, disposeFont = true;

			for (int j = 0; j < allRanges.length; j++) {
				StyleRange s = allRanges[j];
				if (disposeFont && style.font == s.font) disposeFont = false;
				if (disposeFg && style.foreground == s.foreground) disposeFg = false;
				if (disposeBg && style.background == s.background) disposeBg = false;
				if (disposeStrike && style.strikeoutColor == s.strikeoutColor) disposeStrike = false;
				if (disposeUnder && style.underlineColor == s.underlineColor) disposeUnder = false;
				if (disposeBorder && style.borderColor == s.borderColor) disposeBorder =  false;
			}
			if (disposeFont && style.font != textFont && style.font != null)  style.font.dispose();
			if (disposeFg && style.foreground != textForeground && style.foreground != null) style.foreground.dispose();
			if (disposeBg && style.background != textBackground && style.background != null) style.background.dispose();
			if (disposeStrike && style.strikeoutColor != strikeoutColor && style.strikeoutColor != null) style.strikeoutColor.dispose();
			if (disposeUnder && style.underlineColor != underlineColor && style.underlineColor != null) style.underlineColor.dispose();
			if (disposeBorder && style.borderColor != borderColor && style.borderColor != null) style.borderColor.dispose();
			
			Object data = style.data;
			if (data != null) {
				if (data instanceof Image) ((Image)data).dispose();
				if (data instanceof Control) ((Control)data).dispose();
			}
		}
	}

	void disposeResource(Resource resource) {
		if (resource == null) return;
		StyleRange[] styles = styledText.getStyleRanges(0, styledText.getCharCount(), false);
		int index = 0;
		while (index < styles.length) {
			if (styles[index].font == resource) break;
			if (styles[index].foreground == resource) break;
			if (styles[index].background == resource) break;
			if (styles[index].strikeoutColor == resource) break;
			if (styles[index].underlineColor == resource) break;
			if (styles[index].borderColor == resource) break;
			index++;
		}
		if (index == styles.length) resource.dispose();
	}

	String[] getFontNames() {
		FontData[] fontNames = display.getFontList(null, true);
		String[] names = new String[fontNames.length];
		int count = 0;
		mainfor:
		for (int i = 0; i < fontNames.length; i++) {
			String fontName = fontNames[i].getName();
			if (fontName.startsWith("@")) //$NON-NLS-1$
				continue;
			for (int j = 0; j < count; j++) {
				if (names[j].equals(fontName)) continue mainfor;
			}
			names[count++] = fontName;
		}
		if (count < names.length) {
			String[] newNames = new String[count];
			System.arraycopy(names, 0, newNames, 0, count);
			names = newNames;
		}
		return names;
	}

	StyleRange[] getStyles(InputStream stream) {
		try {
			StyleRange[] styles = new StyleRange[256];
			int count = 0;
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
			String line;
			while ((line = reader.readLine()) != null) {
				StringTokenizer tokenizer = new StringTokenizer(line, ";", false);  //$NON-NLS-1$
				StyleRange range = new StyleRange();
				range.start = Integer.parseInt(tokenizer.nextToken());
				range.length = Integer.parseInt(tokenizer.nextToken());
				range.fontStyle = Integer.parseInt(tokenizer.nextToken());
				range.strikeout = tokenizer.nextToken().equals("true");  //$NON-NLS-1$
				range.underline = tokenizer.nextToken().equals("true");  //$NON-NLS-1$
				if (tokenizer.hasMoreTokens()) {
					int red = Integer.parseInt(tokenizer.nextToken());
					int green = Integer.parseInt(tokenizer.nextToken());
					int blue = Integer.parseInt(tokenizer.nextToken());
					range.foreground = new Color(display, red, green, blue);
				}
				if (tokenizer.hasMoreTokens()) {
					int red = Integer.parseInt(tokenizer.nextToken());
					int green = Integer.parseInt(tokenizer.nextToken());
					int blue = Integer.parseInt(tokenizer.nextToken());
					range.background = new Color(display, red, green, blue);
				}
				if (count >= styles.length) {
					StyleRange[] newStyles =  new StyleRange[styles.length + 256];
					System.arraycopy(styles, 0, newStyles, 0, styles.length);
					styles = newStyles;
				}
				styles[count++] = range;
			}
			if (count < styles.length) {
				StyleRange[] newStyles = new StyleRange[count];
				System.arraycopy(styles, 0, newStyles, 0, count);
				styles = newStyles;
			}
			return styles;
		} catch (IOException e) {
			showError(getResourceString("Error"), e.getMessage()); //$NON-NLS-1$
		}
		return null;
	}
	
	void handleKeyDown (Event event) {
		if (event.keyCode == SWT.INSERT) {
			insert = !insert;
		}
	}
	
	void handleModify (ModifyEvent event) {
		if (newCharCount > 0 && start >= 0) {
			StyleRange style = new StyleRange();
			if (textFont != null && !textFont.equals(styledText.getFont())) {
				style.font = textFont;
			} else {
				style.fontStyle = SWT.NONE;
				if (boldControl.getSelection()) style.fontStyle |= SWT.BOLD;
				if (italicControl.getSelection()) style.fontStyle |= SWT.ITALIC;
			}
			if ((styleState & FOREGROUND) != 0) {
				style.foreground = textForeground;
			}
			if ((styleState & BACKGROUND) != 0) {
				style.background = textBackground;
			}
			int underlineStyle = styleState & UNDERLINE;
			if (underlineStyle != 0) {
				style.underline = true;
				style.underlineColor = underlineColor;
				switch (underlineStyle) {
					case UNDERLINE_SINGLE:	style.underlineStyle = SWT.UNDERLINE_SINGLE; break;
					case UNDERLINE_DOUBLE:	style.underlineStyle = SWT.UNDERLINE_DOUBLE; break;
					case UNDERLINE_SQUIGGLE:	style.underlineStyle = SWT.UNDERLINE_SQUIGGLE; break;
					case UNDERLINE_ERROR:	style.underlineStyle = SWT.UNDERLINE_ERROR; break;
					case UNDERLINE_LINK: {
						style.underlineColor = null;
						if (link != null && link.length() > 0) {
							style.underlineStyle = SWT.UNDERLINE_LINK;
							style.data = link;
						} else {
							style.underline = false;
						}
						break;
					}
				}
			}
			if ((styleState & STRIKEOUT) != 0) {
				style.strikeout = true;
				style.strikeoutColor = strikeoutColor;
			}
			int borderStyle = styleState & BORDER;
			if (borderStyle != 0) {
				style.borderColor = borderColor;
				switch (borderStyle) {
					case BORDER_DASH:	style.borderStyle = SWT.BORDER_DASH; break;
					case BORDER_DOT:	style.borderStyle = SWT.BORDER_DOT; break;
					case BORDER_SOLID: style.borderStyle = SWT.BORDER_SOLID; break;
				}
			}
			int[] ranges = {start, newCharCount};
			StyleRange[] styles = {style}; 
			styledText.setStyleRanges(start, newCharCount, ranges, styles);
		}
		disposeRanges(selectedRanges);
	}
	
	void handleMouseUp (Event event) {
		if (link != null) {
			int offset = styledText.getCaretOffset();
			StyleRange range = offset > 0 ? styledText.getStyleRangeAtOffset(offset-1) : null;
			if (range != null) {
				if (link == range.data) {
					Shell dialog = new Shell(shell);
					dialog.setLayout(new FillLayout());
					dialog.setText(getResourceString("Browser")); //$NON-NLS-1$
					Browser browser = new Browser(dialog, SWT.MOZILLA);
					browser.setUrl(link);
					dialog.open();
				}
			}
		}
	}
	
	void handlePaintObject(PaintObjectEvent event) {
		GC gc = event.gc;
		StyleRange style = event.style;
		Bullet bullet = event.bullet;
		if (bullet != null && bullet.type == ST.BULLET_CUSTOM) {
			Display display = event.display;
			Font font = style.font;
			if (font == null) font = styledText.getFont();
			TextLayout layout = new TextLayout(display);
			layout.setAscent(event.ascent);
			layout.setDescent(event.descent);
			layout.setFont(font);
			layout.setText(event.bulletIndex + 1 + "."); //$NON-NLS-1$
			layout.draw(gc, event.x + BULLET_WIDTH * 2 / 3, event.y);
			layout.dispose();
		} else {
			Object data = style.data;
			if (data instanceof Image) {
				Image image = (Image)data;
				int x = event.x;
				int y = event.y + event.ascent - style.metrics.ascent;
				gc.drawImage(image, x, y);
			}
			if (data instanceof Control) {
				Control control = (Control)data;
				Point pt = control.getSize();
				int x = event.x + MARGIN;
				int y = event.y + event.ascent - 2 * pt.y / 3;
				control.setLocation(x, y);
			}
		}
	}

	void handleResize(ControlEvent event) {
		Rectangle rect = shell.getClientArea();
		Point cSize = coolBar.computeSize(rect.width, SWT.DEFAULT);
		Point sSize = statusBar.computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int statusMargin = 2;
		coolBar.setBounds(rect.x, rect.y, cSize.x, cSize.y);
		styledText.setBounds(rect.x, rect.y + cSize.y, rect.width, rect.height - cSize.y - (sSize.y + 2 * statusMargin));
		statusBar.setBounds(rect.x + statusMargin, rect.y + rect.height - sSize.y - statusMargin, rect.width - (2 * statusMargin), sSize.y);
	}

	void handleVerifyText(VerifyEvent event) {
		start = event.start;
		newCharCount = event.text.length();
		int replaceCharCount = event.end - start;

		// mark styles to be disposed
		selectedRanges = styledText.getStyleRanges(start, replaceCharCount, false);
	}

	void initResources() {
		iBold = loadImage(display, "bold"); //$NON-NLS-1$
		iItalic = loadImage(display, "italic"); //$NON-NLS-1$
		iUnderline = loadImage(display, "underline"); //$NON-NLS-1$
		iStrikeout = loadImage(display, "strikeout"); //$NON-NLS-1$
		iBlockSelection = loadImage(display, "fullscrn"); //$NON-NLS-1$
		iBorderStyle = loadImage(display, "resize"); //$NON-NLS-1$
		iLeftAlignment = loadImage(display, "left"); //$NON-NLS-1$
		iRightAlignment = loadImage(display, "right"); //$NON-NLS-1$
		iCenterAlignment = loadImage(display, "center"); //$NON-NLS-1$
		iJustifyAlignment = loadImage(display, "justify"); //$NON-NLS-1$
		iCut = loadImage(display, "cut"); //$NON-NLS-1$
		iCopy = loadImage(display, "copy"); //$NON-NLS-1$
		iPaste = loadImage(display, "paste"); //$NON-NLS-1$
		iTextForeground = loadImage(display, "textForeground"); //$NON-NLS-1$
		iTextBackground = loadImage(display, "textBackground"); //$NON-NLS-1$
		iBaselineUp = loadImage(display, "font_big"); //$NON-NLS-1$
		iBaselineDown = loadImage(display, "font_sml"); //$NON-NLS-1$
		iBulletList = loadImage(display, "para_bul"); //$NON-NLS-1$
		iNumberedList = loadImage(display, "para_num"); //$NON-NLS-1$
		iLink = new Image(display, getClass().getResourceAsStream("link_obj.gif")); //$NON-NLS-1$
	}

	void installListeners() {
		styledText.addCaretListener(new CaretListener() {
			public void caretMoved(CaretEvent event) {
				updateStatusBar();
				updateToolBar();
			}
		});
		styledText.addListener(SWT.MouseUp, new Listener() {
			public void handleEvent(Event event) {
				handleMouseUp(event);
			}
		});
		styledText.addListener(SWT.KeyDown, new Listener() {
			public void handleEvent(Event event) {
				handleKeyDown(event);
			}
		});
		styledText.addVerifyListener(new VerifyListener() {
			public void verifyText(VerifyEvent event) {
				handleVerifyText(event);
			}
		});
		styledText.addModifyListener(new ModifyListener(){
			public void modifyText(ModifyEvent event) {
				handleModify(event);
			}
		});
		styledText.addPaintObjectListener(new PaintObjectListener() {
			public void paintObject(PaintObjectEvent event) {
				handlePaintObject(event);
			}
		});
		styledText.addListener(SWT.Dispose, new Listener() {
			public void handleEvent(Event event) {
				StyleRange[] styles = styledText.getStyleRanges(0, styledText.getCharCount(), false);
				for (int i = 0; i < styles.length; i++) {
					Object data = styles[i].data;
					if (data != null) {
						if (data instanceof Image) ((Image)data).dispose();
						if (data instanceof Control) ((Control)data).dispose();
					}
				}
			}
		});
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent event) {
				handleResize(event);
			}
		});
	}

	Image loadImage(Display display, String fileName) {
		Image image = null; 
		try {
			InputStream sourceStream = getClass().getResourceAsStream(fileName + ".ico");  //$NON-NLS-1$ //$NON-NLS-2$
			ImageData source = new ImageData(sourceStream);
			ImageData mask = source.getTransparencyMask();
			image = new Image(display, source, mask);
			sourceStream.close();
		} catch (IOException e) {
			showError(getResourceString("Error"), e.getMessage()); //$NON-NLS-1$
		}
		return image;
	}

	void loadProfile(int profile) {
		try {
			switch (profile) {
				case 1: {
					String text = openFile(TextEditor.class.getResourceAsStream("text.txt"));  //$NON-NLS-1$
					StyleRange[] styles = getStyles(TextEditor.class.getResourceAsStream("styles.txt"));  //$NON-NLS-1$
					styledText.setText(text);
					if (styles != null) styledText.setStyleRanges(styles);
					break;
				}
				case 2: {
					styledText.setText(getResourceString("Profile2"));  //$NON-NLS-1$
					break;
				}
				case 3: {
					String text = openFile(TextEditor.class.getResourceAsStream("text4.txt"));  //$NON-NLS-1$
					styledText.setText(text);
					break;
				}
				case 4: {
					styledText.setText(getResourceString("Profile4"));  //$NON-NLS-1$
					break;
				}
			}
			updateToolBar();
		} catch (Exception e) {
			showError(getResourceString("Error"), e.getMessage()); //$NON-NLS-1$
		}
	}

	String openFile(InputStream stream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
		StringBuffer buffer = new StringBuffer();
		String line;
		String lineDelimiter = styledText.getLineDelimiter();
		while ((line = reader.readLine()) != null) {
			buffer.append(line);
			buffer.append(lineDelimiter);
		}
		return buffer.toString();
	}

	void releaseResources() {
		iBold.dispose();
		iBold = null;
		iItalic.dispose();
		iItalic = null;
		iUnderline.dispose();
		iUnderline = null;
		iStrikeout.dispose();
		iStrikeout = null;
		iBorderStyle.dispose();
		iBorderStyle = null;
		iBlockSelection.dispose();
		iBlockSelection = null;
		iLeftAlignment.dispose();
		iLeftAlignment = null;
		iRightAlignment.dispose();
		iRightAlignment = null;
		iCenterAlignment.dispose();
		iCenterAlignment = null;
		iJustifyAlignment.dispose();
		iJustifyAlignment = null;
		iCut.dispose();
		iCut = null;
		iCopy.dispose();
		iCopy = null;
		iPaste.dispose();
		iPaste = null;
		iTextForeground.dispose();
		iTextForeground = null;
		iTextBackground.dispose();
		iTextBackground = null;
		iBaselineUp.dispose();
		iBaselineUp = null;
		iBaselineDown.dispose();
		iBaselineDown = null;
		iBulletList.dispose();
		iBulletList = null;
		iNumberedList.dispose();
		iNumberedList = null;
		
		if (textFont != null) textFont.dispose();
		textFont = null;
		if (textForeground != null) textForeground.dispose();
		textForeground = null;
		if (textBackground != null) textBackground.dispose();
		textBackground = null;
		if (strikeoutColor != null) strikeoutColor.dispose();
		strikeoutColor = null;
		if (underlineColor != null) underlineColor.dispose();
		underlineColor = null;
		if (borderColor != null) borderColor.dispose();
		borderColor = null;

		if (font != null) font.dispose();
		font = null;
	}

	void saveFile() {
		if (fileName != null) {
			FileWriter file = null;
			try {
				file = new FileWriter(fileName);
		       	file.write(styledText.getText());
		       	file.close();
			} catch (IOException e) {
	        	showError(getResourceString("Error"), e.getMessage());
	        } finally {
	        	try {
	        		if (file != null) file.close();
	        	} catch (IOException e) {
	        		showError(getResourceString("Error"), e.getMessage());
	        	}
	        }
		}
	}
	
	void setBullet(int type) {
		Point selection = styledText.getSelection();
		int lineStart = styledText.getLineAtOffset(selection.x);
		int lineEnd = styledText.getLineAtOffset(selection.y);
		StyleRange styleRange = new StyleRange();
		styleRange.metrics = new GlyphMetrics(0, 0, BULLET_WIDTH);
		Bullet bullet = new Bullet(type, styleRange);
		for (int lineIndex = lineStart; lineIndex <= lineEnd; lineIndex++) {
			Bullet oldBullet = styledText.getLineBullet(lineIndex);
			styledText.setLineBullet(lineIndex, 1, oldBullet != null ? null : bullet);
		}
	}
	
	void setLink() {
		final Shell dialog = new Shell(shell, SWT.APPLICATION_MODAL | SWT.SHELL_TRIM);
		dialog.setLayout(new GridLayout(2, false));
		dialog.setText(getResourceString("SetLink")); //$NON-NLS-1$
		Label label = new Label(dialog, SWT.NONE);
		label.setText(getResourceString("URL")); //$NON-NLS-1$
		final Text text = new Text(dialog, SWT.SINGLE);
		text.setLayoutData(new GridData(200, SWT.DEFAULT));
		if (link != null) {
			text.setText(link);
			text.selectAll();
		}
		final Button okButton = new Button(dialog, SWT.PUSH);
		okButton.setText(getResourceString("Ok")); //$NON-NLS-1$
		final Button cancelButton = new Button(dialog, SWT.PUSH);
		cancelButton.setText(getResourceString("Cancel")); //$NON-NLS-1$
		Listener listener = new Listener() {
			public void handleEvent(Event event) {
				if (event.widget == okButton) {
					link = text.getText();
					setStyle(UNDERLINE_LINK);
				}
				dialog.dispose();
			}
		};
		okButton.addListener(SWT.Selection, listener);
		cancelButton.addListener(SWT.Selection, listener);
		dialog.setDefaultButton(okButton);
		dialog.pack();
		dialog.open();
		while (!dialog.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}
	
	void setStyle(int style) {
		int[] ranges = styledText.getSelectionRanges();
		int i = 0;
		while (i < ranges.length) {
			setStyle(style, ranges[i++], ranges[i++]);
		}
		updateStyleState(style, FOREGROUND);
		updateStyleState(style, BACKGROUND);
		updateStyleState(style, UNDERLINE);
		updateStyleState(style, STRIKEOUT);
		updateStyleState(style, BORDER);
	}
	void setStyle(int style, int start, int length) {
		if (length == 0) return;
		
		/* Create new style range */
		StyleRange newRange = new StyleRange();
		if ((style & FONT) != 0) {
			newRange.font = textFont;
		}
		if ((style & FONT_STYLE) != 0) {
			newRange.fontStyle = style & FONT_STYLE;
		}
		if ((style & FOREGROUND) != 0) {
			newRange.foreground = textForeground;
		}
		if ((style & BACKGROUND) != 0) {
			newRange.background = textBackground;
		}
		if ((style & BASELINE_UP) != 0)	newRange.rise++;
		if ((style & BASELINE_DOWN) != 0) newRange.rise--;
		if ((style & STRIKEOUT) != 0) {
			newRange.strikeout = true;
			newRange.strikeoutColor = strikeoutColor;
		}
		if ((style & UNDERLINE) != 0) {
			newRange.underline = true;
			newRange.underlineColor = underlineColor;
			switch (style & UNDERLINE) {
				case UNDERLINE_SINGLE:
					newRange.underlineStyle = SWT.UNDERLINE_SINGLE;
					break;
				case UNDERLINE_DOUBLE:
					newRange.underlineStyle = SWT.UNDERLINE_DOUBLE;
					break;
				case UNDERLINE_ERROR:
					newRange.underlineStyle = SWT.UNDERLINE_ERROR;
					break;
				case UNDERLINE_SQUIGGLE:
					newRange.underlineStyle = SWT.UNDERLINE_SQUIGGLE;
					break;
				case UNDERLINE_LINK:
					newRange.underlineColor = null;
					if (link != null && link.length() > 0) {
						newRange.underlineStyle = SWT.UNDERLINE_LINK;
						newRange.data = link;
					} else {
						newRange.underline = false;
					}
					break;
			}
		}
		if ((style & BORDER) != 0) {
			switch (style & BORDER) {
				case BORDER_DASH:
					newRange.borderStyle = SWT.BORDER_DASH;
					break;
				case BORDER_DOT:
					newRange.borderStyle = SWT.BORDER_DOT;
					break;
				case BORDER_SOLID:
					newRange.borderStyle = SWT.BORDER_SOLID;
					break;
			}
			newRange.borderColor = borderColor;
		}
		
		int newRangeStart = start;
		int newRangeLength = length;
		int[] ranges = styledText.getRanges(start, length);
		StyleRange[] styles = styledText.getStyleRanges(start, length, false);		
		int maxCount = ranges.length * 2 + 2;
		int[] newRanges = new int[maxCount];
		StyleRange[] newStyles = new StyleRange[maxCount / 2];		
		int count = 0;
		for (int i = 0; i < ranges.length; i+=2) {
			int rangeStart = ranges[i];
			int rangeLength = ranges[i + 1];
			StyleRange range = styles[i / 2];
			if (rangeStart > newRangeStart) {
				newRangeLength = rangeStart - newRangeStart;
				newRanges[count] = newRangeStart;
				newRanges[count + 1] = newRangeLength;
				newStyles[count / 2] = newRange;
				count += 2;
			}
			newRangeStart = rangeStart + rangeLength;
			newRangeLength = (start + length) - newRangeStart;

			/* Create merged style range*/
			StyleRange mergedRange = new StyleRange(range);
			//Note: fontStyle is not copied by the constructor
			mergedRange.fontStyle = range.fontStyle;
			if ((style & FONT) != 0) {
				mergedRange.font =  newRange.font;
			}
			if ((style & FONT_STYLE) != 0) {
				mergedRange.fontStyle =  range.fontStyle ^ newRange.fontStyle;
			}
			if (mergedRange.font != null && ((style & FONT) != 0 || (style & FONT_STYLE) != 0)) {
				boolean change = false;
				FontData[] fds = mergedRange.font.getFontData();
				for (int j = 0; j < fds.length; j++) {
					FontData fd = fds[j];
					if (fd.getStyle() != mergedRange.fontStyle) {
						fds[j].setStyle(mergedRange.fontStyle);
						change = true;
					}
				}
				if (change) {
					mergedRange.font = new Font(display, fds);
				}
			}
			if ((style & FOREGROUND) != 0) {
				mergedRange.foreground = newRange.foreground != range.foreground ? newRange.foreground : null;
			}
			if ((style & BACKGROUND) != 0) {
				mergedRange.background = newRange.background != range.background ? newRange.background : null;
			}
			if ((style & BASELINE_UP) != 0) mergedRange.rise++;
			if ((style & BASELINE_DOWN) != 0) mergedRange.rise--;
			if ((style & STRIKEOUT) != 0) {
				mergedRange.strikeout = !range.strikeout || range.strikeoutColor != newRange.strikeoutColor;
				mergedRange.strikeoutColor = mergedRange.strikeout ? newRange.strikeoutColor : null;
			}
			if ((style & UNDERLINE) != 0) {
				if ((style & UNDERLINE_LINK) != 0) {
					if (link != null && link.length() > 0) {
						mergedRange.underline = !range.underline || range.underlineStyle != newRange.underlineStyle  || range.data != newRange.data;
					} else {
						mergedRange.underline = false;
					}
					mergedRange.underlineColor = null;
				} else {
					mergedRange.underline = !range.underline || range.underlineStyle != newRange.underlineStyle || range.underlineColor != newRange.underlineColor;
					mergedRange.underlineColor = mergedRange.underline ? newRange.underlineColor : null;
				}
				mergedRange.underlineStyle = mergedRange.underline ? newRange.underlineStyle : SWT.NONE;
				mergedRange.data = mergedRange.underline ? newRange.data : null;
			}
			if ((style & BORDER) != 0) {
				if (range.borderStyle != newRange.borderStyle || range.borderColor != newRange.borderColor) {
					mergedRange.borderStyle = newRange.borderStyle;
					mergedRange.borderColor = newRange.borderColor;
				} else {
					mergedRange.borderStyle = SWT.NONE;
					mergedRange.borderColor = null;
				}
			}
			
			newRanges[count] = rangeStart;
			newRanges[count + 1] = rangeLength;
			newStyles[count / 2] = mergedRange;
			count += 2;
		}
		if (newRangeLength > 0) {
			newRanges[count] = newRangeStart;
			newRanges[count + 1] = newRangeLength;
			newStyles[count / 2] = newRange;
			count += 2;
		}
		if (0 < count && count < maxCount) {			
			int[] tmpRanges = new int[count];
			StyleRange[] tmpStyles = new StyleRange[count / 2];
			System.arraycopy(newRanges, 0, tmpRanges, 0, count);
			System.arraycopy(newStyles, 0, tmpStyles, 0, count / 2);
			newRanges = tmpRanges;
			newStyles = tmpStyles;
		}
		styledText.setStyleRanges(start, length, newRanges, newStyles);
		disposeRanges(styles);
	}
	
	void showError (String title, String message) {
		MessageBox messageBox = new MessageBox(shell, SWT.ICON_ERROR | SWT.CLOSE);
		messageBox.setText(title);
		messageBox.setMessage(message);
		messageBox.open();
	}

	void updateStatusBar() {
		int offset = styledText.getCaretOffset();
		int lineIndex = styledText.getLineAtOffset(offset);
		String insertLabel = getResourceString(insert ? "Insert" : "Overwrite"); //$NON-NLS-1$ //$NON-NLS-2$
		statusBar.setText(getResourceString("Offset")	//$NON-NLS-1$
				+ offset + " "							//$NON-NLS-1$
				+ getResourceString("Line")				//$NON-NLS-1$
				+ lineIndex + "\t"						//$NON-NLS-1$
				+ insertLabel);
	}
	
	void updateStyleState(int style, int changingStyle) {
		if ((style & changingStyle) != 0) {
			if ((style & changingStyle) == (styleState & changingStyle)) {
				styleState &= ~changingStyle;
			} else {
				styleState &= ~changingStyle;
				styleState |= style;
			}
		}
	}

	void updateToolBar() {
		styleState = 0;
		link = null;
		boolean bold = false, italic = false;
		Font font = null;

		int offset = styledText.getCaretOffset();
		StyleRange range = offset > 0 ? styledText.getStyleRangeAtOffset(offset-1) : null;
		if (range != null) {
			if (range.font != null) {
				font = range.font;
				FontData[] fds = font.getFontData();
				for (int i = 0; i < fds.length; i++) {
					int fontStyle = fds[i].getStyle();
					if (!bold && (fontStyle & SWT.BOLD) != 0) bold = true;
					if (!italic && (fontStyle & SWT.ITALIC) != 0) italic = true;
				}
			} else {
				bold = (range.fontStyle & SWT.BOLD) != 0;
				italic = (range.fontStyle & SWT.ITALIC) != 0;
			}
			if (range.foreground != null) {
				styleState |= FOREGROUND;
				if (textForeground != range.foreground) {
					disposeResource(textForeground);
					textForeground = range.foreground;
				}
			}
			if (range.background != null) {
				styleState |= BACKGROUND;
				if (textBackground != range.background) {
					disposeResource(textBackground);
					textBackground = range.background;
				}
			}
			if (range.underline) {
				switch (range.underlineStyle) {
					case SWT.UNDERLINE_SINGLE:	styleState |= UNDERLINE_SINGLE; break;
					case SWT.UNDERLINE_DOUBLE: 	styleState |= UNDERLINE_DOUBLE; break;
					case SWT.UNDERLINE_SQUIGGLE:	styleState |= UNDERLINE_SQUIGGLE; break;
					case SWT.UNDERLINE_ERROR: 	styleState |= UNDERLINE_ERROR; break;
					case SWT.UNDERLINE_LINK: 	
						styleState |= UNDERLINE_LINK;
						link = (String)range.data;
						break;
				}
				if (range.underlineStyle != SWT.UNDERLINE_LINK) {
					underlineSingleItem.setSelection((styleState & UNDERLINE_SINGLE) != 0);
					underlineDoubleItem.setSelection((styleState & UNDERLINE_DOUBLE) != 0);
					underlineErrorItem.setSelection((styleState & UNDERLINE_ERROR) != 0);
					underlineSquiggleItem.setSelection((styleState & UNDERLINE_SQUIGGLE) != 0);
					disposeResource(underlineColor);
					underlineColor = range.underlineColor;
				}
			}
			if (range.strikeout) {
				styleState |= STRIKEOUT;
				disposeResource(strikeoutColor);
				strikeoutColor = range.strikeoutColor;
			}
			if (range.borderStyle != SWT.NONE) {
				switch (range.borderStyle) {
					case SWT.BORDER_SOLID:	styleState |= BORDER_SOLID; break;
					case SWT.BORDER_DASH:	styleState |= BORDER_DASH; break;
					case SWT.BORDER_DOT:	styleState |= BORDER_DOT; break;
				}
				borderSolidItem.setSelection((styleState & BORDER_SOLID) != 0);
				borderDashItem.setSelection((styleState & BORDER_DASH) != 0);
				borderDotItem.setSelection((styleState & BORDER_DOT) != 0);
				disposeResource(borderColor);
				borderColor = range.borderColor;
			}
		}
		
		boldControl.setSelection(bold);
		italicControl.setSelection(italic);
		FontData fontData = font != null ? font.getFontData()[0] : styledText.getFont().getFontData()[0];
		int index = 0;
		int count = fontNameControl.getItemCount();
		String fontName = fontData.getName();
		while (index < count) {
			if (fontNameControl.getItem(index).equals(fontName)) {
				fontNameControl.select(index);
				break;
			}
			index++;
		}
		index = 0;
		count = fontSizeControl.getItemCount();
		int fontSize = fontData.getHeight();
		while (index < count) {
			int size = Integer.parseInt(fontSizeControl.getItem(index));
			if (fontSize == size) {
				fontSizeControl.select(index);
				break;
			}
			if (size > fontSize) {
				fontSizeControl.add (String.valueOf(fontSize), index);
				fontSizeControl.select(index);
				break;
			}
			index++;
		}
		
		disposeResource(textFont);
		textFont = font;
		int lineIndex = styledText.getLineAtOffset(offset);
		int alignment = styledText.getLineAlignment(lineIndex);
		leftAlignmentItem.setSelection((alignment & SWT.LEFT) != 0);
		centerAlignmentItem.setSelection((alignment & SWT.CENTER) != 0);
		rightAlignmentItem.setSelection((alignment & SWT.RIGHT) != 0);
		boolean justify = styledText.getLineJustify(lineIndex);
		justifyAlignmentItem.setSelection(justify);
	}
}