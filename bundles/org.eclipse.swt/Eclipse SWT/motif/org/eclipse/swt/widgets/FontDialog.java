package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

import java.util.*;

/**
 * Instances of this class allow the user to select a font
 * from all available fonts in the system.
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 */
public class FontDialog extends Dialog {
	/*
	 * Table containing all available fonts as FontData objects.
	 * The table is structured as a series of embedded Hashtables as follows:
	 * <br>characterRegistryName -> faceName -> extendedStyle -> size -> style
	 */
	private Hashtable characterSets = new Hashtable ();
	private FontData initialFontData;
	private Font sampleFont;			// the current displayed sample font
	private boolean okSelected = false;
	private boolean ignoreEvents = false;

	// widgets	
	private Shell shell;
	private Combo charSetCombo;
	private Combo faceNameCombo;
	private Combo fontSizeCombo;	
	private Combo fontStyleCombo;
	private Combo extStyleCombo;
	private Label sampleLabel;
	private Button okButton;
	private Button cancelButton;

	// constants
	private static final String TEXT_SAMPLE = "AaBbYyZz";
	private static final String SCALABLE_SIZES[] = new String[] {"8", "10", "11", "12", "14", "16", "18", "22", "24", "26"};
	private static final int DEFAULT_SIZE = 14;
	private static final String DEFAULT_STYLE = "medium";
	private static final Integer SCALABLE_KEY = new Integer (0);
	private static final Integer NO_SELECTION = new Integer (-1);
	private static final int COLUMN1_WIDTH = 200;
	private static final int COLUMN2_WIDTH = 150;
	private static final int COLUMN3_WIDTH = 100;
	private static final String PREFIX_ISO8859 = "iso8859";
	private static final String PREFIX_ISO646 = "iso646";
	private static final String PREFIX_UNICODE = "ucs";
	private static final String PREFIX_JAPANESE = "jis";
	private static final String PREFIX_SIMPLIFIEDCHINESE = "gb";
	private static final String PREFIX_TRADITIONALCHINESE = "cns";
	private static final String PREFIX_KOREAN = "ks";
	private static final String [] ISO_CHARSETS = new String [] {
		"",	// 0 undefined
		SWT.getMessage ("SWT_Charset_Western"),
		SWT.getMessage ("SWT_Charset_EastEuropean"),
		SWT.getMessage ("SWT_Charset_SouthEuropean"),
		SWT.getMessage ("SWT_Charset_NorthEuropean"),
		SWT.getMessage ("SWT_Charset_Cyrillic"),
		SWT.getMessage ("SWT_Charset_Arabic"),
		SWT.getMessage ("SWT_Charset_Greek"),
		SWT.getMessage ("SWT_Charset_Hebrew"),
		SWT.getMessage ("SWT_Charset_Turkish"),
		SWT.getMessage ("SWT_Charset_Nordic"),
		SWT.getMessage ("SWT_Charset_Thai"),
		"",	// 12 undefined
		SWT.getMessage ("SWT_Charset_BalticRim"),
		SWT.getMessage ("SWT_Charset_Celtic"),
		SWT.getMessage ("SWT_Charset_Euro"),
		SWT.getMessage ("SWT_Charset_Romanian")
	};

/**
 * Constructs a new instance of this class given only its
 * parent.
 * <p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FontDialog (Shell parent) {
	this (parent, SWT.NONE);
}

/**
 * Constructs a new instance of this class given its parent
 * and a style value describing its behavior and appearance.
 * <p>
 * The style value is either one of the style constants defined in
 * class <code>SWT</code> which is applicable to instances of this
 * class, or must be built by <em>bitwise OR</em>'ing together 
 * (that is, using the <code>int</code> "|" operator) two or more
 * of those <code>SWT</code> style constants. The class description
 * for all SWT dialog classes should include a comment which
 * describes the style constants which are applicable to the class.
 * </p>
 * Note: Currently, null can be passed in for the parent.
 * This has the effect of creating the dialog on the currently active
 * display if there is one. If there is no current display, the 
 * dialog is created on a "default" display. <b>Passing in null as
 * the parent is not considered to be good coding style,
 * and may not be supported in a future release of SWT.</b>
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the parent is null</li>
 * </ul>
 * @exception SWTException <ul>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the parent</li>
 *    <li>ERROR_INVALID_SUBCLASS - if this class is not an allowed subclass</li>
 * </ul>
 */
public FontDialog (Shell parent, int style) {
	super (parent, style | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	checkSubclass ();
}

/**
 * Add the fonts found in 'fonts' to the list of fonts.
 * Fonts are stored by character set and face name. For each character 
 * set/face name combination there is one FontExtStyles object that 
 * captures the different extended styles and the sizes and styles 
 * available for that extended style.
 */
void addFonts (FontData fonts[]) {

	for (int i = 0; i < fonts.length; i++) {
		FontData font = fonts [i];

		String charSetName = getTranslatedCharSet (font);
		Hashtable charSet = (Hashtable) characterSets.get (charSetName);
		if (charSet == null) {
			charSet = new Hashtable (9);
			characterSets.put (charSetName, charSet);
		}

		String faceName = getTranslatedFaceName (font);
		Hashtable faceSet = (Hashtable) charSet.get (faceName);
		if (faceSet == null) {
			faceSet = new Hashtable (9);
			charSet.put (faceName, faceSet);
		}

		String extStyleName = font.addStyle;
		Hashtable extStyleSet = (Hashtable) faceSet.get (extStyleName);
		if (extStyleSet == null) {
			extStyleSet = new Hashtable (9);
			faceSet.put (extStyleName, extStyleSet);
		}
		
		Integer sizeValue = new Integer (font.getHeight ());
		Hashtable sizeSet = (Hashtable) extStyleSet.get (sizeValue);
		if (sizeSet == null) {
			sizeSet = new Hashtable (9);
			extStyleSet.put (sizeValue, sizeSet);
		}
		
		String style = font.weight;
		sizeSet.put (style,font);
	}
}

/**
 * Create the widgets of the dialog.
 */
void createChildren () {
	Label characterSetLabel = new Label (shell, SWT.NONE);
	Label faceNameLabel = new Label (shell, SWT.NONE);
	Label extendedStyleLabel = new Label (shell, SWT.NONE);	
	GridLayout layout = new GridLayout ();
	
	layout.numColumns = 4;
	layout.marginWidth = 15;
	layout.marginHeight = 15;
	layout.horizontalSpacing = 10;
	layout.verticalSpacing = 2;
	shell.setLayout (layout);

	// row one
	characterSetLabel.setText (SWT.getMessage ("SWT_Character_set") + ":");
	faceNameLabel.setText (SWT.getMessage ("SWT_Font") + ":");
	extendedStyleLabel.setText (SWT.getMessage ("SWT_Extended_style") + ":");
	
	new Label (shell, SWT.NONE);

	// row two	
	charSetCombo = new Combo (shell, SWT.SIMPLE | SWT.V_SCROLL);
	GridData gridData = new GridData ();
	gridData.widthHint = COLUMN1_WIDTH;
	gridData.heightHint = 150;
	gridData.verticalSpan = 2;
	charSetCombo.setLayoutData (gridData);
	charSetCombo.setData (NO_SELECTION);
	
	faceNameCombo = new Combo (shell, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData ();
	gridData.widthHint = COLUMN2_WIDTH;
	gridData.heightHint = 150;	
	gridData.verticalSpan = 2;
	gridData.verticalAlignment = GridData.FILL;
	faceNameCombo.setLayoutData (gridData);
	faceNameCombo.setData (NO_SELECTION);
	
	extStyleCombo = new Combo (shell, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData ();
	gridData.widthHint = COLUMN3_WIDTH;
	gridData.heightHint = 150;	
	gridData.verticalSpan = 2;
	gridData.verticalAlignment = GridData.FILL;	
	extStyleCombo.setLayoutData (gridData);
	extStyleCombo.setData (NO_SELECTION);
	
	// create ok and cancel buttons (row two and three)
	createOkCancel ();
	
	// row four
	createEmptyRow ();
	
	// row five
	Label fontSizeLabel = new Label (shell, SWT.NONE);	
	fontSizeLabel.setText (SWT.getMessage ("SWT_Size") + ":");	
	Label fontStyleLabel = new Label (shell, SWT.NONE);
	fontStyleLabel.setText (SWT.getMessage ("SWT_Style") + ":");
	
	Label fillLabel = new Label (shell, SWT.NONE);
	gridData = new GridData ();
	gridData.horizontalSpan = 2;
	fillLabel.setLayoutData (gridData);

	// row six
	fontSizeCombo = new Combo (shell, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData ();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.FILL;		
	gridData.heightHint = 110;	
	fontSizeCombo.setLayoutData (gridData);
	fontSizeCombo.setData (NO_SELECTION);
			
	fontStyleCombo = new Combo (shell, SWT.SIMPLE | SWT.V_SCROLL);
	gridData = new GridData ();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.FILL;		
	fontStyleCombo.setLayoutData (gridData);
	fontStyleCombo.setData (NO_SELECTION);
	
	fillLabel = new Label (shell, SWT.NONE);
	gridData = new GridData ();
	gridData.horizontalSpan = 2;
	fillLabel.setLayoutData (gridData);

	// row seven
	createEmptyRow ();
	
	// row eight
	Group sampleGroup = new Group (shell, SWT.NONE);
	sampleGroup.setText (SWT.getMessage ("SWT_Sample"));
	gridData = new GridData ();
	gridData.heightHint = 70;	
	gridData.horizontalSpan = 3;
	gridData.horizontalAlignment = GridData.FILL;	
	sampleGroup.setLayoutData (gridData);

	// setup group box with sample text 
	layout = new GridLayout ();
	layout.marginWidth = 10;
	layout.marginHeight = 10;
	sampleGroup.setLayout (layout);
	
	sampleLabel = new Label (sampleGroup, SWT.CENTER);
	sampleLabel.setText (TEXT_SAMPLE);
	gridData = new GridData ();
	gridData.grabExcessHorizontalSpace = true;
	gridData.grabExcessVerticalSpace = true;	
	gridData.verticalAlignment = GridData.FILL;	
	gridData.horizontalAlignment = GridData.FILL;	
	sampleLabel.setLayoutData (gridData);
	shell.setSize (445, 410);
}

/**
 * Fill one row in the grid layout with empty widgets.
 * Used to achieve a bigger vertical spacing between separate 
 * groups of widgets (ie. new rows of Text/Combo combinations).
 */
void createEmptyRow () {
	Label fillLabel = new Label (shell, SWT.NONE);
	GridData gridData = new GridData ();
	
	gridData.heightHint = 5;
	gridData.horizontalSpan = ((GridLayout) shell.getLayout ()).numColumns;
	fillLabel.setLayoutData (gridData);
}

/**
 * Create the widgets of the dialog.
 */
void createOkCancel () {
	okButton = new Button (shell, SWT.PUSH);
	okButton.setText (SWT.getMessage ("SWT_OK"));
	shell.setDefaultButton (okButton);	
	GridData gridData = new GridData ();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.widthHint = 70;
	okButton.setLayoutData (gridData);

	cancelButton = new Button (shell, SWT.PUSH);
	cancelButton.setText (SWT.getMessage ("SWT_Cancel"));
	gridData = new GridData ();
	gridData.horizontalAlignment = GridData.FILL;
	gridData.verticalAlignment = GridData.BEGINNING;		
	cancelButton.setLayoutData (gridData);
}

Hashtable getExtStyles (String charsetName, String faceName) {
	Hashtable faces = getFaces (charsetName);
	if (faces == null) return null;
	return (Hashtable) faces.get (faceName);
}

Hashtable getFaces (String charsetName) {
	return (Hashtable) getFonts ().get (charsetName);
}

/**
 * Returns a FontData object describing the font that was
 * selected in the dialog, or null if none is available.
 * 
 * @return the FontData for the selected font, or null
 */
public FontData getFontData () {
	if (sampleFont != null) {
		return sampleFont.getFontData ()[0];
	}
	return initialFontData;
}

FontData getFontData (String charsetName, String faceName, String extStyle, int size, String style) {
	Hashtable styles = getStyles (charsetName, faceName, extStyle, size);
	if (styles == null) return null;
	return (FontData) styles.get (style);
}

/**
 * Returns the collection of fonts that are displayed by the 
 * receiver.
 * See the class definition for an explanation of the structure
 * of the returned Hashtable.
 */
Hashtable getFonts () {
	return characterSets;
}

/**
 * Returns a FontData object that can be used to load the selected 
 * font.
 */
FontData getSelectionFontData () {
	String charSetName = charSetCombo.getText ();
	String faceName = faceNameCombo.getText ();
	String extStyle = extStyleCombo.getText ();
	int size = DEFAULT_SIZE;
	try {
		size = Integer.valueOf (fontSizeCombo.getText ()).intValue ();
	} catch (NumberFormatException e) {
		/*
		 * This block is purposely left empty since a default
		 * value is already specified above.
		 */
	}
	String style = fontStyleCombo.getText ();
	FontData result = getFontData (charSetName, faceName, extStyle, size, style);

	if (result == null) {
		/*
		* One or more of the dialog's widgets contain custom typed values.
		* Create a FontData that mirrors these values so that the Font created
		* below will try to find the best match.
		*/
		result = new FontData ();
		result.characterSetRegistry = charSetName;
		result.setName(faceName);
		result.addStyle = extStyle;
		result.weight = style;
	}
	result.setHeight (size);
	return result;
}

Hashtable getSizes (String charsetName, String faceName, String extStyle) {
	Hashtable extStyles = getExtStyles (charsetName, faceName);
	if (extStyles == null) return null;
	return (Hashtable) extStyles.get (extStyle);
}

Hashtable getStyles (String charsetName, String faceName, String extStyle, int size) {
	Hashtable sizes = getSizes (charsetName, faceName, extStyle);
	if (sizes == null) return null;
	Hashtable result = (Hashtable) sizes.get (new Integer (size));
	if (result == null)
		result = (Hashtable) sizes.get (SCALABLE_KEY);
	return result;
}
	
/**
 * Returns the character set found in 'fontData' prefixed
 * with a string explaining the character set.
 */
String getTranslatedCharSet (FontData fontData) {
	String characterSet = fontData.characterSetRegistry;
	String translatedCharSet = null;

	if (characterSet.startsWith (PREFIX_ISO8859)) {
		int charSetName = 1;
		try {
			charSetName = Integer.valueOf (fontData.characterSetName).intValue ();
		} catch (NumberFormatException e) {
			/*
			 * This block is purposely left empty since a default
			 * value is already specified above.
			 */
		}
		characterSet += "-" + charSetName;
		if (charSetName < ISO_CHARSETS.length) {
			translatedCharSet = ISO_CHARSETS [charSetName];
		}
	}
	else	
	if (characterSet.startsWith (PREFIX_ISO646)) {
		translatedCharSet = SWT.getMessage("SWT_Charset_ASCII");
	}
	else	
	if (characterSet.startsWith (PREFIX_UNICODE)) {
		translatedCharSet = SWT.getMessage("SWT_Charset_Unicode");
	}
	else	
	if (characterSet.startsWith (PREFIX_JAPANESE)) {
		translatedCharSet = SWT.getMessage("SWT_Charset_Japanese");
	}
	else	
	if (characterSet.startsWith (PREFIX_SIMPLIFIEDCHINESE)) {
		translatedCharSet = SWT.getMessage("SWT_Charset_SimplifiedChinese");
	}
	else	
	if (characterSet.startsWith (PREFIX_TRADITIONALCHINESE)) {
		translatedCharSet = SWT.getMessage("SWT_Charset_TraditionalChinese");
	}
	else	
	if (characterSet.startsWith (PREFIX_KOREAN)) {
		translatedCharSet = SWT.getMessage("SWT_Charset_Korean");
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
 * Returns the face name as specified in FontData.familyName followed by
 * the foundry set in parantheses if available.
 * We display the face name first so that the list box sorts the fonts by 
 * face name, not by foundry. Users generally want to select fonts based 
 * on the face name and not by foundry. Once they've found the desired 
 * face name in the list they can compare the font variations from 
 * different foundries if available.
 */
String getTranslatedFaceName (FontData fontData) {
	StringBuffer faceNameBuffer;
	
	if (fontData.foundry != null && fontData.foundry.length () > 0) {
		faceNameBuffer = new StringBuffer (fontData.fontFamily);
		faceNameBuffer.append (" (");
		faceNameBuffer.append (fontData.foundry);
		faceNameBuffer.append (')');			
	}
	else {
		faceNameBuffer = new StringBuffer (fontData.getName ());
	}
	return faceNameBuffer.toString ();
}

/**
 * Handle the events the receiver is listening to.
 * Combo selections cause the downstream combos to be initialized 
 * with font data and the sample text to be updated.
 */
void handleEvent (Event event) {
	if (ignoreEvents) return;
	if (event.widget instanceof Combo) {
		Combo combo = (Combo) event.widget;
		int prevSelectIndex = ((Integer) combo.getData ()).intValue ();
		String text = combo.getText ();
		int newSelectIndex = combo.indexOf (text);
		if (prevSelectIndex != newSelectIndex || newSelectIndex == -1) {
			ignoreEvents = true;
			combo.setData (new Integer (newSelectIndex));
			if (combo == charSetCombo) initFaceNameCombo ();
			else if (combo == faceNameCombo) initExtStyleCombo ();
			else if (combo == extStyleCombo) initSizeCombo ();
			else if (combo == fontSizeCombo) initStyleCombo ();
			updateSampleFont ();
			if (newSelectIndex != -1) {
				// in case it came by typing the name
				combo.select (newSelectIndex);
			}
			ignoreEvents = false;
		}
	}		
	else
	if (event.widget == okButton) {
		okSelected = true;
		shell.close ();
	}
	else
	if (event.widget == cancelButton) {
		okSelected = false;
		shell.close ();
	}	
}

/**
 * Initialize the extended styles combo with the extended styles
 * available for the selected font.
 * Downstream combos are initialized as well (style and size).
 */
void initExtStyleCombo () {
	String oldSelect = extStyleCombo.getText ();
	extStyleCombo.removeAll ();
	
	String characterSet = charSetCombo.getText ();
	String faceName = faceNameCombo.getText ();
	Hashtable extStyles = getExtStyles (characterSet, faceName);
	if (extStyles == null) return;
	setItemsSorted (extStyleCombo, extStyles);
	
	int selectIndex = extStyleCombo.indexOf (oldSelect);
	selectIndex = Math.max (0, selectIndex);
	extStyleCombo.select (selectIndex);
	extStyleCombo.setData (new Integer (selectIndex));
	initSizeCombo ();
}

/**
 * Initialize the face name combo box with all font names 
 * available in the selected character set.
 * Downstream combos are initialized as well (extended style).
 */
void initFaceNameCombo () {
	String oldSelect = faceNameCombo.getText ();
	faceNameCombo.removeAll ();
	
	Hashtable faceNames = getFaces (charSetCombo.getText ());
	setItemsSorted (faceNameCombo, faceNames);
	
	int selectIndex = faceNameCombo.indexOf (oldSelect);
	selectIndex = Math.max (0, selectIndex);
	faceNameCombo.select (selectIndex);
	faceNameCombo.setData (new Integer (selectIndex));
	initExtStyleCombo ();
}

/**
 * Initialize the widgets of the receiver with the data of 
 * all installed fonts.
 * If the user specified a default font preselect that font in 
 * the combo boxes.
 */
void initializeWidgets () {
	Display display = shell.getDisplay ();
	addFonts (display.getFontList (null, false));		// get all fonts availabe on the current display
	addFonts (display.getFontList (null, true));
	setItemsSorted (charSetCombo, getFonts ());
	if (initialFontData != null) {
		Font initialFont = new Font (display, initialFontData);	// verify that the initial font data is a valid font
		initialFontData = null;
		ignoreEvents = true;
		setFontCombos (initialFont.getFontData ()[0]);
		ignoreEvents = false;
		initialFont.dispose ();
		updateSampleFont ();
	}
}

/**
 * Initialize the size combo with the sizes the selected font 
 * is available in.
 * If the selected font is scalable a selection of preset sizes 
 * is used.
 */
void initSizeCombo () {
	String oldSelect = fontSizeCombo.getText ();
	fontSizeCombo.removeAll ();
	
	String characterSet = charSetCombo.getText ();
	String faceName = faceNameCombo.getText ();
	String extStyle = extStyleCombo.getText ();
	Hashtable sizes = getSizes (characterSet, faceName, extStyle);
	if (sizes == null) return;
	if (sizes.get (SCALABLE_KEY) == null) {
		/*
		 * Font is not scalable so just present the provided sizes.
		 */
		setSizeItemsSorted (sizes.keys ());
	} else {
		/*
		 * Font is scalable so present the provided sizes and scalable
		 * sizes for selection.
		 */
		Vector allSizes = new Vector ();
		/*
		 * Add the scalable sizes.
		 */
		for (int i = 0; i < SCALABLE_SIZES.length; i++) {
			allSizes.addElement (Integer.valueOf (SCALABLE_SIZES [i]));
		}
		/*
		 * Add the provided sizes.
		 */
		Enumeration providedSizes = sizes.keys ();
		while (providedSizes.hasMoreElements ()) {
			Integer size = (Integer) providedSizes.nextElement ();
			if (!size.equals (SCALABLE_KEY) && !allSizes.contains (size)) {
				allSizes.addElement (size);
			}
		}
		setSizeItemsSorted (allSizes.elements ());
	}
	
	int selectIndex = fontSizeCombo.indexOf (oldSelect);
	if (selectIndex == -1) {
		selectIndex = fontSizeCombo.indexOf (String.valueOf (DEFAULT_SIZE));
	}
	selectIndex = Math.max (0, selectIndex);
	fontSizeCombo.select (selectIndex);
	fontSizeCombo.setData (new Integer (selectIndex));
	initStyleCombo ();
}

/**
 * Initialize the styles combo with the styles the selected font 
 * is available in.
 */
void initStyleCombo () {
	String oldSelect = fontStyleCombo.getText ();
	fontStyleCombo.removeAll ();
	
	String characterSet = charSetCombo.getText ();
	String faceName = faceNameCombo.getText ();
	String extStyle = extStyleCombo.getText ();
	int size = DEFAULT_SIZE;
	try {
		size = Integer.valueOf (fontSizeCombo.getText ()).intValue ();
	} catch (NumberFormatException e) {
		/*
		 * This block is purposely left empty since a default
		 * value is already specified above.
		 */
	}
	Hashtable styles = getStyles (characterSet, faceName, extStyle, size);
	if (styles == null) return;
	setItemsSorted (fontStyleCombo, styles);
	
	int selectIndex = fontStyleCombo.indexOf (oldSelect);
	if (selectIndex == -1) {
		selectIndex = fontStyleCombo.indexOf (String.valueOf (DEFAULT_STYLE));
	}
	selectIndex = Math.max (0, selectIndex);
	fontStyleCombo.select (selectIndex);
	fontStyleCombo.setData (new Integer (selectIndex));
	fontStyleCombo.select (Math.max (0, selectIndex));
}

/**
 * Register the receiver to receive events.
 */
void installListeners () {
	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			FontDialog.this.handleEvent (event);
		}
	};
	okButton.addListener (SWT.Selection, listener);
	cancelButton.addListener (SWT.Selection, listener);
	charSetCombo.addListener (SWT.Selection, listener);
	charSetCombo.addListener (SWT.Modify, listener);
	faceNameCombo.addListener (SWT.Modify, listener);
	fontStyleCombo.addListener (SWT.Modify, listener);
	extStyleCombo.addListener (SWT.Modify, listener);
	fontSizeCombo.addListener (SWT.Modify, listener);
}

/**
 * Makes the dialog visible and brings it to the front
 * of the display.
 *
 * @return a FontData object describing the font that was selected,
 *         or null if the dialog was cancelled or an error occurred
 *
 * @exception SWTException <ul>
 *    <li>ERROR_WIDGET_DISPOSED - if the dialog has been disposed</li>
 *    <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the dialog</li>
 * </ul>
 */
public FontData open () {
	shell = new Shell (getParent (), getStyle () | SWT.TITLE | SWT.BORDER | SWT.APPLICATION_MODAL);
	createChildren ();
	installListeners ();	
	
	initializeWidgets ();
	setFontData (null);
	openDialog ();
	Display display = shell.getDisplay ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	
	FontData result = null;
	if (okSelected) result = getFontData ();
	if (sampleFont != null) sampleFont.dispose ();
	return result;
}

/**
 * Open the receiver and set its size to the size calculated by 
 * the layout manager.
 */
void openDialog () {
	// Start everything off by setting the shell size to its computed size.
	Point pt = shell.computeSize(SWT.DEFAULT, SWT.DEFAULT, false);
	
	// Ensure that the width of the shell fits the display.
	Rectangle displayRect = shell.getDisplay().getBounds();
	int widthLimit = displayRect.width * 7 / 8;
	int heightLimit = displayRect.height * 7 / 8;
	if (pt.x > widthLimit) {
		pt = shell.computeSize (widthLimit, SWT.DEFAULT, false);
	}
	
	// centre the dialog on its parent, and ensure that the
	// whole dialog appears within the screen bounds
	Rectangle parentBounds = getParent ().getBounds ();
	int originX = (parentBounds.width - pt.x) / 2 + parentBounds.x;
	originX = Math.max (originX, 0);
	originX = Math.min (originX, widthLimit - pt.x);
	int originY = (parentBounds.height - pt.y) / 2 + parentBounds.y;
	originY = Math.max (originY, 0);
	originY = Math.min (originY, heightLimit - pt.y);
	shell.setBounds (originX, originY, pt.x, pt.y);
	
	String title = getText ();
	if (title.length () == 0) title = SWT.getMessage ("SWT_FontDialog_Title");
	shell.setText(title);
	
	// Open the window.
	shell.open();
}

/**
 * Initialize the combo boxes with the data of the preselected
 * font specified by the user.
 */
void setFontCombos (FontData fontData) {
	String characterSet = getTranslatedCharSet (fontData);
	String faceName = getTranslatedFaceName (fontData);
	charSetCombo.setText (characterSet);
	charSetCombo.setData (new Integer (charSetCombo.indexOf (characterSet)));

	initFaceNameCombo ();
	faceNameCombo.setText (faceName);
	faceNameCombo.setData (new Integer (faceNameCombo.indexOf (faceName)));

	initExtStyleCombo ();
	extStyleCombo.setText (fontData.addStyle);
	extStyleCombo.setData (new Integer (extStyleCombo.indexOf (fontData.addStyle)));
		
	initSizeCombo ();
	String value = String.valueOf (fontData.getHeight ());
	fontSizeCombo.setText (value);
	fontSizeCombo.setData (new Integer (fontSizeCombo.indexOf (value)));
	
	initStyleCombo ();
	fontStyleCombo.setText (fontData.weight);
	fontStyleCombo.setData (new Integer (fontStyleCombo.indexOf (fontData.weight)));
}

/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the FontData to use initially, or null
 */
public void setFontData (FontData fontData) {
	initialFontData = fontData;
}

/**
 * Set the contents of 'combo' to the keys of 'items'.
 * Keys are sorted in ascending order first and have to be Strings.
 */
void setItemsSorted (Combo combo, Hashtable items) {
	Enumeration itemKeys = items.keys ();
	String [] sortedItems = new String[items.size ()];
	int index = 0;
	while (itemKeys.hasMoreElements ()) {
		String item = (String) itemKeys.nextElement ();
		if (item.length () != 0) sortedItems[index++] = item;
	}
	if (index != sortedItems.length) {
		String [] newItems = new String[index];
		System.arraycopy (sortedItems, 0, newItems, 0, index);
		sortedItems = newItems;
	}
	sort (sortedItems);
	combo.setItems (sortedItems);
}

/**
 * Set the contents of the size combo to the keys of 'items'.
 * Keys are sorted in ascending order first and have to be Integers.
 */
void setSizeItemsSorted (Enumeration itemsEnum) {
	Vector items = new Vector ();
	while (itemsEnum.hasMoreElements ()) {
		items.addElement (itemsEnum.nextElement ());
	}
	Integer[] sortedItems = new Integer [items.size ()];
	items.copyInto (sortedItems);
	int index = 0;
	sort (sortedItems);
	String[] sortedItemStrings = new String [items.size ()];
	for (int i = 0; i < sortedItemStrings.length; i++) {
		sortedItemStrings [i] = String.valueOf (sortedItems [i].intValue ());
	}
	fontSizeCombo.setItems (sortedItemStrings);
}

/**
 * Sort 'items' in ascending order.
 */
void sort (Integer[] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap = length / 2; gap > 0; gap /= 2) {
		for (int i = gap; i < length; i++) {
			for (int j = i - gap; j >= 0; j -= gap) {
		   		if (items [j].intValue () > items [j + gap].intValue ()) {
					Integer swap = items [j];
					items[j] = items [j + gap];
					items[j + gap] = swap;
		   		}
	    	}
	    }
	}
}

/**
 * Sort 'items' in ascending order.
 */
void sort (String items[]) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap = length / 2; gap > 0; gap /= 2) {
		for (int i = gap; i < length; i++) {
			for (int j = i - gap; j >= 0; j -= gap) {
		   		if (items [j].compareTo (items [j + gap]) > 0) {
					String swap = items [j];
					items [j] = items[j + gap];
					items [j + gap] = swap;
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
void updateSampleFont () {
	FontData selectionFontData = getSelectionFontData ();
	/*
	 * sampleFont may not be the same as the one specified in selectionFontData.
	 * This happens when selectionFontData specifies a font alias.
	 */
	Font newSampleFont = new Font (shell.getDisplay (), selectionFontData);
	sampleLabel.setFont (newSampleFont);
	if (sampleFont != null) sampleFont.dispose ();
	sampleFont = newSampleFont;
}
}
