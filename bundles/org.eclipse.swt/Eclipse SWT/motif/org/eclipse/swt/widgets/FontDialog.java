package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * SWT - The Simple Widget Toolkit,
 * (c) Copyright IBM Corp 1998, 1999.
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;
import java.text.*;
import java.util.*;

/**
 * A FontDialog allows the user to select a font
 * from all available fonts in the system.
 */
public /*final*/ class FontDialog extends Dialog {
	private static final String TEXT_SAMPLE = "AaBbYyZz";
	private static final String TEXT_FONT_NOT_LOADED = "Could not load selected font";	// text used in place of sample text when the selected font could not be loaded
	
	private Shell shell;						// the dialog shell
	private Combo characterSet;
	private Combo faceName;
	private Combo fontSize;	
	private Combo fontStyle;
	private Combo extendedStyle;
	private Label sampleLabel;
	private Button ok;
	private Button cancel;

	private boolean okSelected;					// true if the dialog was hidden 
												// because the ok button was selected
	private FontData dialogResult;								
	private Hashtable characterSets = new Hashtable();	// maps character sets to a hashtable 
												// that maps the fonts in that 
												// character set to FontStyles objects
	private FontData initialSelection;			// can be set by the programmer and
	private Font sampleFont;					// the sample font for the font data selected by the user.
												// Used to correctly clean up allocated fonts
												// will be used to initialize the font 
												// combo boxes when the dialog is opened												
/**
 * Create a new instance of the receiver with 'parent' as 
 * its parent shell.
 * @param parent - the parent shell. May be null
 */
public FontDialog(Shell parent) {
	this(parent, SWT.NULL);
}
/**
 * Create a new instance of the receiver with 'parent' as 
 * its parent shell using 'style' as the widget style.
 * @param parent - the parent shell. May be null
 * @param style - style bits used to create the receiver.
 *	See class definition for details
 */
public FontDialog(Shell parent, int style) {
	super(parent, style | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
}
/**
 * Add the fonts found in 'fonts' to the list of fonts.
 * Fonts are stored by character set and face name. For each character 
 * set/face name combination there is one FontExtStyles object that 
 * captures the different extended styles and the sizes and styles 
 * available for that extended style.
 */
void addFonts(FontData fonts[]) {
	FontData font;
	String faceName;
	String characterSet;
	Hashtable characterSets = getFonts();	
	Hashtable faceNames;
	FontExtStyles fontExtStyles;
	
	for (int i = 0; i < fonts.length; i++) {
		font = fonts[i];
		characterSet = getTranslatedCharSet(font);
		faceNames = (Hashtable) characterSets.get(characterSet);
		faceName = getTranslatedFaceName(font);		
		if (faceNames == null) {
			faceNames = new Hashtable();
			characterSets.put(characterSet, faceNames);
		}
		fontExtStyles = (FontExtStyles) faceNames.get(faceName);
		if (fontExtStyles == null) {
			fontExtStyles = new FontExtStyles(font.getName());		// use original face name for FontExtStyles
			faceNames.put(faceName, fontExtStyles);
		}
		fontExtStyles.add(font);
	}
	setFonts(characterSets);
}
/**
 * Create the widgets of the dialog.
 */
void createChildren() {
	Shell dialog = getDialogShell();
	Label characterSetLabel = new Label(dialog, SWT.NULL);
	Label faceNameLabel = new Label(dialog, SWT.NULL);
	Label extendedStyleLabel = new Label(dialog, SWT.NULL);	
	Label fontSizeLabel;
	Label fontStyleLabel;
	Label fillLabel;
	Group sampleGroup;
	GridData gridData;
	GridLayout layout = new GridLayout();
	final int ColumnOneWidth = 200;
	final int ColumnTwoWidth = 150;
	final int ColumnThreeWidth = 100;	
	
	layout.numColumns = 4;
	layout.marginWidth = 15;
	layout.marginHeight = 15;
	layout.horizontalSpacing = 10;
	layout.verticalSpacing = 2;
	dialog.setLayout(layout);

	// row one
	characterSetLabel.setText("Character set:");
	faceNameLabel.setText("Font:");
	extendedStyleLabel.setText("Extended style:");
	
	new Label(dialog, SWT.NULL);

	// row two	
	characterSet = new Combo(dialog, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData();
	gridData.widthHint = ColumnOneWidth;
	gridData.heightHint = 150;
	gridData.verticalSpan = 2;
	characterSet.setData(new Integer(-1));	
	characterSet.setLayoutData(gridData);
	
	faceName = new Combo(dialog, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData();
	gridData.widthHint = ColumnTwoWidth;
	gridData.heightHint = 150;	
	gridData.verticalSpan = 2;
	gridData.verticalAlignment = GridData.FILL;
	faceName.setData(new Integer(-1));
	faceName.setLayoutData(gridData);

	extendedStyle = new Combo(dialog, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData();
	gridData.widthHint = ColumnThreeWidth;
	gridData.heightHint = 150;	
	gridData.verticalSpan = 2;
	gridData.verticalAlignment = GridData.FILL;	
	extendedStyle.setData(new Integer(-1));
	extendedStyle.setLayoutData(gridData);

	// create ok and cancel buttons (row two and three)
	createOkCancel();
	
	// row four
	createEmptyRow();
	
	// row five
	fontSizeLabel = new Label(dialog, SWT.NULL);	
	fontSizeLabel.setText("Size:");	
	fontStyleLabel = new Label(dialog, SWT.NULL);
	fontStyleLabel.setText("Style:");

	fillLabel = new Label(dialog, SWT.NULL);
	gridData = new GridData();
	gridData.horizontalSpan = 2;
	fillLabel.setLayoutData(gridData);

	// row six
	fontSize = new Combo(dialog, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.FILL;		
	gridData.heightHint = 110;	
	fontSize.setData(new Integer(-1));
	fontSize.setLayoutData(gridData);
		
	fontStyle = new Combo(dialog, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.FILL;		
	fontStyle.setData(new Integer(-1));
	fontStyle.setLayoutData(gridData);
	
	fillLabel = new Label(dialog, SWT.NULL);
	gridData = new GridData();
	gridData.horizontalSpan = 2;
	fillLabel.setLayoutData(gridData);

	// row seven
	createEmptyRow();
	
	// row eight
	sampleGroup = new Group(dialog, SWT.NULL);
	sampleGroup.setText("Sample");
	gridData = new GridData();
	gridData.heightHint = 70;	
	gridData.horizontalSpan = 3;
	gridData.horizontalAlignment = GridData.FILL;	
	sampleGroup.setLayoutData(gridData);

	// setup group box with sample text 
	layout = new GridLayout();
	layout.marginWidth = 10;
	layout.marginHeight = 10;
	sampleGroup.setLayout(layout);
	
	sampleLabel = new Label(sampleGroup, SWT.CENTER);
	sampleLabel.setText(TEXT_SAMPLE);
	gridData = new GridData();
	gridData.grabExcessHorizontalSpace = true;
	gridData.grabExcessVerticalSpace = true;	
	gridData.verticalAlignment = GridData.FILL;	
	gridData.horizontalAlignment = GridData.FILL;	
	sampleLabel.setLayoutData(gridData);

	dialog.setSize(445, 410);
}
/**
 * Answer the combo used to display all available character sets.
 */
Combo getCharacterSetCombo() {
	return characterSet;
}
/**
 * Answer the combo used to display all extended styles of
 * the selected font.
 */
Combo getExtStyleCombo() {
	return extendedStyle;
}
/**
 * Answer the combo used to display the face names of the 
 * fonts in the selected character set.
 */
Combo getFaceNameCombo() {
	return faceName;
}
/**
 * Answer the FontData for the selected font.
 * Answer null if no font was selected and the dialog was cancelled.
 */
public FontData getFontData() {
	return dialogResult;
}
/**
 * Answer the collection of fonts that are displayed by the 
 * receiver.
 * See the class definition for an explanation of the structure
 * of the returned Hashtable.
 */
Hashtable getFonts() {
	return characterSets;
}
/**
 * Return the sample font created from the selected font metrics.
 * This font is set into the sampleLabel.
 */
Font getSampleFont() {
	return sampleFont;
}
/**
 * Answer the label used to display a sample of the selected font.
 */
Label getSampleLabel() {
	return sampleLabel;
}
/**
 * Answer the selected character set in the format used to load 
 * fonts.
 */
String getSelectedCharSet() {
	String translatedCharSet = getCharacterSetCombo().getText();
	String platformCharSet;
	int characterSetIndex = translatedCharSet.indexOf("(");

	if (characterSetIndex == -1) {
		platformCharSet = translatedCharSet;
	}
	else {
		platformCharSet = translatedCharSet.substring(
			characterSetIndex + 1, translatedCharSet.length()-1);
	}
	return platformCharSet;
}
/**
 * Answer the selected face name in the format used to load 
 * fonts.
 */
String getSelectedFaceName() {
	String translatedFaceName = getFaceNameCombo().getText();
	String platformFaceName;
	int foundryIndex = translatedFaceName.indexOf("(");

	if (foundryIndex == -1) {			// if this is true, the face name is not selected from the list
		platformFaceName = translatedFaceName;
	}
	else {
		platformFaceName = translatedFaceName.substring(
			0, translatedFaceName.indexOf(" ("));
	}
	return platformFaceName;
}
/**
 * Answer the selected font foundry in the format used to load 
 * fonts.
 */
String getSelectedFoundry() {
	String translatedFaceName = getFaceNameCombo().getText();
	String foundry = new String();
	int foundryIndex = translatedFaceName.indexOf("(");

	if (foundryIndex != -1) {
		foundry = translatedFaceName.substring(
			foundryIndex + 1, translatedFaceName.length()-1);
	}
	return foundry;
}
/**
 * Answer a FontData object that can be used to load the selected 
 * font.
 */
FontData getSelectionFontData() {
	String fontSize = getSizeCombo().getText();
	String style = getStyleCombo().getText();
	String extStyle = getExtStyleCombo().getText();
	int styleBits = SWT.NULL;
	Integer fontSizeInt = null;
	FontData fontData;

	if (style.indexOf(FontExtStyles.BOLD) != -1) {
		styleBits |= SWT.BOLD;
	}
	if (style.indexOf(FontExtStyles.ITALIC) != -1) {
		styleBits |= SWT.ITALIC;
	}
	try {
		fontSizeInt = Integer.valueOf(fontSize);
	}
	catch (NumberFormatException exception) {}
	if (fontSizeInt != null) {
		fontData = new FontData(
			getSelectedFaceName(),
			fontSizeInt.intValue(),
			styleBits);
	}
	else {
		fontData = new FontData();
		fontData.setName(getSelectedFaceName());
		fontData.setStyle(styleBits);
	}
	if (extStyle.length() > 0) {
		fontData.addStyle = extStyle;
	}
	fontData.characterSetRegistry = getSelectedCharSet();
	fontData.foundry = getSelectedFoundry();
	return fontData;
}
/**
 * Answer the combo box used to display the available sizes of 
 * the selected font.
 */
Combo getSizeCombo() {
	return fontSize;
}
/**
 * Answer the combo box used to display the available styles of 
 * the selected font.
 */
Combo getStyleCombo() {
	return fontStyle;
}
/**
 * Answer the character set found in 'fontData' prefixed
 * with a string explaining the character set.
 */
String getTranslatedCharSet(FontData fontData) {
	String characterSet = fontData.characterSetRegistry;
	String translatedCharSet = null;

	if (characterSet.startsWith("iso8859") == true) {
		translatedCharSet = "Western";
	}
	else	
	if (characterSet.startsWith("iso646") == true) {
		translatedCharSet = "ASCII";
	}
	else	
	if (characterSet.startsWith("ucs") == true) {
		translatedCharSet = "Unicode";
	}
	else	
	if (characterSet.startsWith("jis") == true) {
		translatedCharSet = "Japanese";
	}
	else	
	if (characterSet.startsWith("gb") == true) {
		translatedCharSet = "Simplified Chinese";
	}
	else	
	if (characterSet.startsWith("cns") == true) {
		translatedCharSet = "Traditional Chinese";
	}
	else	
	if (characterSet.startsWith("ks") == true) {
		translatedCharSet = "Korean";
	}
	if (translatedCharSet != null) {
		translatedCharSet += " (" + characterSet + ')';
	}
	else {
		translatedCharSet = characterSet;
	}
	return translatedCharSet;
}
/**
 * Answer the face name as specified in FontData.familyName followed by
 * the foundry set in parantheses if available.
 * We display the face name first so that the list box sorts the fonts by 
 * face name, not by foundry. Users generally want to select fonts based 
 * on the face name and not by foundry. Once they've found the desired 
 * face name in the list they can compare the font variations from 
 * different foundries if available.
 */
String getTranslatedFaceName(FontData fontData) {
	StringBuffer faceNameBuffer;
	
	if (fontData.foundry != null && fontData.foundry.length() > 0) {
		faceNameBuffer = new StringBuffer(fontData.fontFamily);
		faceNameBuffer.append(" (");
		faceNameBuffer.append(fontData.foundry);
		faceNameBuffer.append(')');			
	}
	else {
		faceNameBuffer = new StringBuffer(fontData.getName());
	}
	return faceNameBuffer.toString();
}
/**
 * Handle the events the receiver is listening to.
 * Combo selections cause the downstream combos to be initialized 
 * with font data and the sample text to be updated.
 */
void handleEvents(Event event) {
	int oldSelectionIndex;
	int newSelectionIndex;

	if (event.widget instanceof Combo) {
		oldSelectionIndex = ((Integer) event.widget.getData()).intValue();
		newSelectionIndex = ((Combo) event.widget).getSelectionIndex();
		// work around broken getSelectionIndex
		newSelectionIndex = ((Combo) event.widget).indexOf(((Combo) event.widget).getText());
		event.widget.setData(new Integer(newSelectionIndex));
		if (newSelectionIndex != oldSelectionIndex) {
			if (event.widget == getCharacterSetCombo()) {
				initFaceNameCombo();
			}
			else
			if (event.widget == getFaceNameCombo()) {
				initExtStyleCombo();
			}
			else
			if (event.widget == getExtStyleCombo()) {
				initFontDataCombos();
			}
		}		
		updateSample();
	}
	else
	if (event.widget == getOKButton()) {
		setOkSelected(true);
		getDialogShell().setVisible(false);
	}
	else
	if (event.widget == getCancelButton()) {
		setOkSelected(false);		
		getDialogShell().setVisible(false);
	}	
}
/**
 * Initialize the extended styles combo with the extended styles
 * available for the selected font.
 * Downstream combos are initialized as well (style and size).
 */
void initExtStyleCombo() {
	String characterSet = getCharacterSetCombo().getText();
	String faceName = getFaceNameCombo().getText();
	Hashtable faceNames = (Hashtable) getFonts().get(characterSet);
	FontExtStyles fontExtStyles = (FontExtStyles) faceNames.get(faceName);
	Combo extStyleCombo = getExtStyleCombo();

	setItemsSorted(extStyleCombo, fontExtStyles.getExtStyles());
	extStyleCombo.select(0);
	initFontDataCombos();
}
/**
 * Initialize the face name combo box with all font names 
 * available in the selected character set.
 * Downstream combos are initialized as well (extended style).
 */
void initFaceNameCombo() {
	Hashtable faceNames = (Hashtable) getFonts().get(getCharacterSetCombo().getText());
	Combo faceNameCombo = getFaceNameCombo();	

	faceNameCombo.removeAll();	
	setItemsSorted(faceNameCombo, faceNames);
//	faceNameCombo.select(0);
//	initExtStyleCombo();
}
/**
 * Initialize the styles and size combos with the styles and sizes
 * the selected font is available in.
 */
void initFontDataCombos() {
	String characterSet = getCharacterSetCombo().getText();
	String faceName = getFaceNameCombo().getText();
	Hashtable faceNames = (Hashtable) getFonts().get(characterSet);
	FontExtStyles fontStyles = (FontExtStyles) faceNames.get(faceName);

	initSizeCombo(fontStyles);
	initStyleCombo(fontStyles);	
}
/**
 * Initialize the size combo with the sizes the selected font 
 * is available in.
 * If the selected font is scalable a selection of preset sizes 
 * is used.
 */
void initSizeCombo(FontExtStyles fontExtStyles) {
	Vector sizes = null;
	Integer size;
	Combo sizeCombo = getSizeCombo();
	String sizeStrings[] = {"8", "10", "11", "12", "14", "16", "18", "22", "24", "26"};
	int selectionIndex = -1;
	final int SelectionSize = 14;	

	sizeCombo.removeAll();
	if (fontExtStyles.isScalable() == true) {
		sizeCombo.setItems(sizeStrings);
		selectionIndex = 4;
	}
	else {
		sizes = fontExtStyles.getSizes(getExtStyleCombo().getText());
		for (int i = 0; i < sizes.size(); i++) {
			size = (Integer) sizes.elementAt(i);
			sizeCombo.add(size.toString());
			if (size.intValue() >= SelectionSize && selectionIndex == -1) {
				selectionIndex = i;
			}
		}
	}	
	if (selectionIndex == -1) {
		selectionIndex = sizes.size() - 1;			// select largest height if there's no font 
													// size that is at least as high as SelectionSize 
	}
	sizeCombo.select(selectionIndex);	
}
/**
 * Initialize the styles combo with the styles the selected font 
 * is available in.
 */
void initStyleCombo(FontExtStyles fontExtStyles) {
	Vector styleVector = fontExtStyles.getStyles(getExtStyleCombo().getText());
	Enumeration styleEnum = styleVector.elements();
	Combo styleCombo = getStyleCombo();
	int selectionIndex = styleVector.indexOf(FontExtStyles.MEDIUM);
	String style;

	styleCombo.removeAll();
	while (styleEnum.hasMoreElements() == true) {
		style = (String) styleEnum.nextElement();
		styleCombo.add(style);
	}		
	if (selectionIndex == -1) {
		selectionIndex = 0;
	}
	styleCombo.select(selectionIndex);
}

/**
 * Initialize the widgets of the receiver with the data of 
 * all installed fonts.
 * If the user specified a default font preselect that font in 
 * the combo boxes.
 */
void initializeWidgets() {
	Combo characterSetCombo = getCharacterSetCombo();
	Display display = getDialogShell().getDisplay();
	FontData initialFontData = getFontData();
	Font initialFont;
	
	addFonts(display.getFontList(null, false));		// get all fonts availabe on the current display
	addFonts(display.getFontList(null, true));
	setItemsSorted(characterSetCombo, getFonts());

	if (initialFontData != null) {
		initialFont = new Font(display, initialFontData);	// verify that the initial font data is a valid font
		setFontCombos(initialFont.getFontData()[0]);
		initialFont.dispose();
		updateSample();
	}
}
/**
 * Register the receiver to receive events.
 */
void installListeners() {
	Listener listener = new Listener() {
		public void handleEvent(Event event) {handleEvents(event);}
	};

	getOKButton().addListener(SWT.Selection, listener);
	getCancelButton().addListener(SWT.Selection, listener);
	getCharacterSetCombo().addListener(SWT.Selection, listener);
	getFaceNameCombo().addListener(SWT.Selection, listener);
	getSizeCombo().addListener(SWT.Selection, listener);
	getStyleCombo().addListener(SWT.Selection, listener);
	getExtStyleCombo().addListener(SWT.Selection, listener);
}
/**
 * Initialize the widgets of the receiver, open the dialog
 * and block the method until the dialog is closed by the user.
 */
public FontData open() {
	FontData dialogResult = null;
	Font sampleFont;
	Shell dialog = new Shell(getParent(), getStyle() | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	
	setDialogShell(dialog);
	createChildren();
	installListeners();	
	openModal();
	if (isOkSelected() == true) {
		dialogResult = getSelectionFontData();
		setFontData(dialogResult);
	}
	// Fix for 1FRTJZV
	sampleFont = getSampleFont();
	if (sampleFont != null) {
		sampleFont.dispose();
	}
	// Fix for 1G5NLY7
	if (dialog.isDisposed() == false) {
		dialog.dispose();
	}
	return dialogResult;
}
/**
 * Initialize the combo boxes with the data of the preselected
 * font specified by the user.
 */
void setFontCombos(FontData fontData) {
	String characterSet = getTranslatedCharSet(fontData);
	String faceName = getTranslatedFaceName(fontData);
	Hashtable faceNames = (Hashtable) getFonts().get(characterSet);
	FontExtStyles fontStyles = (FontExtStyles) faceNames.get(faceName);
	String value;
	
	getCharacterSetCombo().setText(characterSet);
	initFaceNameCombo();
	
	getFaceNameCombo().setText(faceName);
	initExtStyleCombo();
	
	getExtStyleCombo().setText(fontData.addStyle);
	initSizeCombo(fontStyles);
	
	value = Integer.toString(fontData.getHeight());
	getSizeCombo().setText(value);
	initStyleCombo(fontStyles);
	
	value = FontExtStyles.getStyleString(fontData.getStyle());
	getStyleCombo().setText(value);
}
/**
 * Set the preselected font of the receiver to 'fontData'.
 */
public void setFontData(FontData fontData) {
	dialogResult = fontData;
}
/**
 * Set the fonts that are displayed by the receiver to 'fonts'.
 */
void setFonts(Hashtable fonts) {
	characterSets = fonts;
}
/**
 * Set the contents of 'combo' to the keys of 'items'.
 * Keys are sorted in ascending order first and have to be Strings.
 */
void setItemsSorted(Combo combo, Hashtable items) {
	Enumeration itemKeys = items.keys();
	String item;
	String sortedItems[] = new String[items.size()];
	int index = 0;
	
	while (itemKeys.hasMoreElements() == true) {
		sortedItems[index++] = (String) itemKeys.nextElement();
	}
	sort(sortedItems);
	combo.setItems(sortedItems);
}
/**
 * Set the sample font created from the selected font metrics 
 * to 'newSampleFont'.
 * This font is set into the sampleLabel.
 */
void setSampleFont(Font newSampleFont) {
	// only dispose fonts we created. See 1FRTK1M for details.
	if (sampleFont != null) {
		sampleFont.dispose();
	}		
	sampleFont = newSampleFont;
	getSampleLabel().setFont(sampleFont);
}
/**
 * Sort 'items' in ascending order.
 */
void sort(String items[]) {
	Collator collator = Collator.getInstance();
	
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap = length/2; gap > 0; gap /= 2) {
		for (int i = gap; i < length; i++) {
			for (int j = i-gap; j >= 0; j -= gap) {
		   		if (collator.compare(items[j], items[j+gap]) > 0) {
					String swap = items[j];
					items[j] = items[j+gap];
					items[j+gap] = swap;
		   		}
	    	}
	    }
	}
}
/**
 * Set the font of the sample text to the selected font.
 * Display an error in place of the sample text if the selected 
 * font could not be loaded.
 */
void updateSample() {
	Display display = getDialogShell().getDisplay();
	FontData selectionFontData = getSelectionFontData();
	Font sampleFont;

	// sampleFont may not be the same as the one specified in selectionFontData.
	// This happens when selectionFontData specifies a font alias. In that case, 
	// Font loads the real font. See 1FG3UWX for details.
	sampleFont = new Font(display, selectionFontData);
	setSampleFont(sampleFont);
}
/**
 * Fill one row in the grid layout with empty widgets.
 * Used to achieve a bigger vertical spacing between separate 
 * groups of widgets (ie. new rows of Text/Combo combinations).
 */
void createEmptyRow() {
	Shell dialog = getDialogShell();
	Label fillLabel = new Label(dialog, SWT.NULL);
	GridData gridData = new GridData();
	
	gridData.heightHint = 5;
	gridData.horizontalSpan = ((GridLayout) dialog.getLayout()).numColumns;
	fillLabel.setLayoutData(gridData);
}
/**
 * Create the widgets of the dialog.
 */
void createOkCancel() {
	Shell dialog = getDialogShell();
	GridData gridData;
	
	ok = new Button(dialog, SWT.PUSH);
	ok.setText("OK");
	dialog.setDefaultButton(ok);	
	gridData = new GridData();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.widthHint = 70;
	ok.setLayoutData(gridData);

	cancel = new Button(dialog, SWT.PUSH);
	cancel.setText("Cancel");
	gridData = new GridData();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;		
	cancel.setLayoutData(gridData);
}
/**
 * Answer the cancel button
 */
Button getCancelButton() {
	return cancel;
}

/**
 * Answer the dialog shell.
 */
Shell getDialogShell() {
	return shell;
}
/**
 * Answer the ok button.
 */
Button getOKButton() {
	return ok;
}

/**
 * Insert the method's description here.
 * Creation date: (08/05/99 12:34:43)
 * @return boolean
 */
boolean isOkSelected() {
	return okSelected;
}
/**
 * Open the receiver and set its size to the size calculated by 
 * the layout manager.
 */
void openDialog() {
	Shell dialog = getDialogShell();
	Point pt;
	Rectangle displayRect;
	int widthLimit;
		
	// Start everything off by setting the shell size to its computed size.
	pt = dialog.computeSize(-1, -1, false);
	
	// Ensure that the width of the shell fits the display.
	displayRect = dialog.getDisplay().getBounds();
	widthLimit = displayRect.width * 7 / 8;
	if (pt.x > widthLimit) {
		pt = dialog.computeSize (widthLimit, -1, false);
	}
	dialog.setBounds (0, 0, pt.x, pt.y);
	dialog.setText(getText());
	// Open the window.
	dialog.open();
}
/**
 * Initialize the widgets of the receiver, open the dialog
 * and block the method until the dialog is closed by the user.
 */
void openModal() {
	Shell dialog = getDialogShell();
	Display display = dialog.getDisplay();

	initializeWidgets();
	setFontData(null);
	openDialog();
	while (dialog.isDisposed() == false && dialog.getVisible() == true) {
		if (display.readAndDispatch() == false) {
			display.sleep();
		}
	}
}
/**
 * Set whether the dialog was closed by selecting the ok button.
 */
void setOkSelected(boolean newOkSelected) {
	okSelected = newOkSelected;
}
/**
 * Set the shell used as the dialog window.
 */
void setDialogShell(Shell shell) {
	this.shell = shell;
}
}
