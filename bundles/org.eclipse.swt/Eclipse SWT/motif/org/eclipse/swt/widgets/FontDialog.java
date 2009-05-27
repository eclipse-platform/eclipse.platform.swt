/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.layout.*;

import java.util.*;

/**
 * Instances of this class allow the user to select a font
 * from all available fonts in the system.
 * <dl>
 * <dt><b>Styles:</b></dt>
 * <dd>(none)</dd>
 * <dt><b>Events:</b></dt>
 * <dd>(none)</dd>
 * </dl>
 * <p>
 * IMPORTANT: This class is intended to be subclassed <em>only</em>
 * within the SWT implementation.
 * </p>
 * 
 * @see <a href="http://www.eclipse.org/swt/examples.php">SWT Example: ControlExample, Dialog tab</a>
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 * @noextend This class is not intended to be subclassed by clients.
 */
public class FontDialog extends Dialog {
	private FontData [] fontData;
	private FontData currentFontData;
	private Font sampleFont;	// the displayed sample font
	private Color sampleColor;	// the displayed sample color
	private RGB rgb;
	private boolean okSelected = false;
	private boolean ignoreEvents = false;
	/*
	 * Table containing all available fonts as FontData objects.
	 * The table is structured as a series of embedded Hashtables as follows:
	 * <br>characterRegistryName -> faceName -> extendedStyle -> size -> style
	 */
	private Hashtable characterSets = new Hashtable ();

	// widgets	
	private Shell shell;
	private List fontSetList;
	private List charSetList, faceNameList, extStyleList;
	private List fontStyleList, fontSizeList;
	private Label sampleLabel;
	private Button upButton, downButton, newButton, removeButton;
	private Button okButton, cancelButton, colorButton;	

	// constants
	private static final String TEXT_SAMPLE = "AaBbYyZz";
	private static String SCALABLE_SIZES [];
	private static final int DEFAULT_SIZE = 14;
	private static final String DEFAULT_STYLE = "medium";
	private static final Integer SCALABLE_KEY = new Integer (0);
	private static final int LIST_WIDTH = 200;
	private static final int EXTSTYLE_WIDTH = 150;
	private static final int LIST_HEIGHT = 150;
	private static final int SAMPLE_HEIGHT = 75;
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

	static {
		SCALABLE_SIZES = new String [69];
		for (int i = 0; i < 69; i++) {
			SCALABLE_SIZES [i] = String.valueOf (i + 4);
		}
	}

/**
 * Constructs a new instance of this class given only its parent.
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
	this (parent, SWT.APPLICATION_MODAL);
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
 * lists the style constants that are applicable to the class.
 * Style bits are also inherited from superclasses.
 * </p>
 *
 * @param parent a shell which will be the parent of the new instance
 * @param style the style of dialog to construct
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
	super (parent, checkStyle (parent, style));
	checkSubclass ();
}

/**
 * Add the fonts found in 'fonts' to the list of fonts.
 * Fonts are stored by character set and face name. For each character 
 * set/face name combination there is one FontExtStyles object that 
 * captures the different extended styles and the sizes and styles 
 * available for that extended style.
 */
void addFonts (FontData fonts []) {
	for (int i = 0; i < fonts.length; i++) {
		FontData font = fonts [i];
		String charSetName = getTranslatedCharSet (font, true);
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

void centerListIndex (List list, int index) {
	int visibleItems = list.getSize ().y / list.getItemHeight ();
	int topIndex = Math.max (0, index - visibleItems / 2);
	list.setTopIndex (topIndex);
}

FontData copyFontData (FontData data) {
	FontData result = new FontData ();
	result.addStyle = data.addStyle;
	result.averageWidth = data.averageWidth;
	result.characterSetName = data.characterSetName;
	result.characterSetRegistry = data.characterSetRegistry;
	result.fontFamily = data.fontFamily;
	result.foundry = data.foundry;
	result.horizontalResolution = data.horizontalResolution;
	result.pixels = data.pixels;
	result.points = data.points;
	result.setWidth = data.setWidth;
	result.slant = data.slant;
	result.spacing = data.spacing;
	result.verticalResolution = data.verticalResolution;
	result.weight = data.weight;
	return result;
}

void createButtons (Composite parent) {
	int buttonAlignment = GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING;
	okButton = new Button (parent, SWT.PUSH);
	okButton.setText (SWT.getMessage ("SWT_OK"));
	okButton.setLayoutData (new GridData (buttonAlignment));
	shell.setDefaultButton (okButton);
	
	cancelButton = new Button (parent, SWT.PUSH);
	cancelButton.setText (SWT.getMessage ("SWT_Cancel"));
	cancelButton.setLayoutData (new GridData (buttonAlignment));

	colorButton = new Button (parent, SWT.PUSH);
	colorButton.setText (SWT.getMessage ("SWT_Color"));
	colorButton.setLayoutData (new GridData (buttonAlignment));
}

void createControls (Composite parent) {
	Composite composite = new Composite (parent, SWT.NONE);
	GridLayout layout = new GridLayout ();
	layout.numColumns = 2;
	composite.setLayout (layout);

	Composite controls = new Composite (composite, SWT.NONE);
	layout = new GridLayout ();
	layout.marginHeight = layout.marginWidth = 0;
	layout.numColumns = 3;
	controls.setLayout (layout);
	
	// labels row (1)
	new Label (controls, SWT.NONE).setText (SWT.getMessage ("SWT_Character_set") + ":");
	new Label (controls, SWT.NONE).setText (SWT.getMessage ("SWT_Font") + ":");
	new Label (controls, SWT.NONE).setText (SWT.getMessage ("SWT_Extended_style") + ":");	

	// lists row (2)
	charSetList = new List (controls, SWT.V_SCROLL | SWT.BORDER);
	GridData gridData = new GridData (GridData.FILL_HORIZONTAL);
	gridData.heightHint = LIST_HEIGHT;
	gridData.widthHint = LIST_WIDTH;
	charSetList.setLayoutData (gridData);

	faceNameList = new List (controls, SWT.V_SCROLL | SWT.BORDER);
	gridData = new GridData (GridData.FILL_HORIZONTAL);
	gridData.heightHint = LIST_HEIGHT;
	gridData.widthHint = LIST_WIDTH;
	faceNameList.setLayoutData (gridData);

	extStyleList = new List (controls, SWT.V_SCROLL | SWT.MULTI | SWT.BORDER);
	gridData = new GridData (GridData.FILL_HORIZONTAL);
	gridData.heightHint = LIST_HEIGHT;
	gridData.widthHint = EXTSTYLE_WIDTH;
	extStyleList.setLayoutData (gridData);

	// labels row (3)
	new Label (controls, SWT.NONE).setText (SWT.getMessage ("SWT_Size") + ":");	
	new Label (controls, SWT.NONE).setText (SWT.getMessage ("SWT_Style") + ":");
	new Label (controls, SWT.NONE);		// filler

	// lists row (4)
	fontSizeList = new List (controls, SWT.V_SCROLL | SWT.BORDER);
	gridData = new GridData (GridData.FILL_HORIZONTAL);
	gridData.heightHint = LIST_HEIGHT;
	gridData.widthHint = LIST_WIDTH;
	fontSizeList.setLayoutData (gridData);

	fontStyleList = new List (controls, SWT.V_SCROLL | SWT.BORDER);
	gridData = new GridData (GridData.FILL_HORIZONTAL);
	gridData.heightHint = LIST_HEIGHT;
	gridData.widthHint = LIST_WIDTH;
	fontStyleList.setLayoutData (gridData);

	new Label (controls, SWT.NONE);		// filler

	// font sets group
	Group fontSetGroup = new Group (controls, SWT.NONE);
	fontSetGroup.setText(SWT.getMessage ("SWT_FontSet"));
	layout = new GridLayout ();
	layout.numColumns = 2;
	fontSetGroup.setLayout (layout);
	GridData data = new GridData (GridData.FILL_BOTH);
	data.horizontalSpan = 3;
	fontSetGroup.setLayoutData (data);

	fontSetList = new List (fontSetGroup, SWT.V_SCROLL | SWT.BORDER);
	data = new GridData (GridData.FILL_BOTH);
	data.grabExcessHorizontalSpace = true;
	fontSetList.setLayoutData (data);

	Composite buttonsGroup = new Composite (fontSetGroup, SWT.NONE);
	layout = new GridLayout ();
	layout.numColumns = 3;
	layout.makeColumnsEqualWidth = false;
	layout.marginHeight = layout.marginWidth = 0;
	layout.horizontalSpacing = layout.verticalSpacing = 0; 
	buttonsGroup.setLayout (layout);
	
	Composite upDownButtonsGroup = new Composite (buttonsGroup, SWT.NONE);
	layout = new GridLayout ();
	layout.marginHeight = layout.marginWidth = 0;
	layout.horizontalSpacing = layout.verticalSpacing = 0; 
	upDownButtonsGroup.setLayout(layout);

	int buttonAlignment = GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_BEGINNING;
	upButton = new Button (upDownButtonsGroup, SWT.PUSH);
	upButton.setLayoutData (new GridData (buttonAlignment));
	upButton.setText (SWT.getMessage ("SWT_Up"));
	downButton = new Button (upDownButtonsGroup, SWT.PUSH);
	downButton.setLayoutData (new GridData (buttonAlignment));
	downButton.setText (SWT.getMessage ("SWT_Down"));

	new Label (buttonsGroup, SWT.SEPARATOR | SWT.VERTICAL);	

	Composite newRemoveButtonsGroup = new Composite (buttonsGroup, SWT.NONE);
	layout = new GridLayout ();
	layout.marginHeight = layout.marginWidth = 0;
	layout.horizontalSpacing = layout.verticalSpacing = 0; 
	newRemoveButtonsGroup.setLayout(layout);
		
	newButton = new Button (newRemoveButtonsGroup, SWT.PUSH);
	newButton.setLayoutData (new GridData (buttonAlignment));
	newButton.setText (SWT.getMessage ("SWT_NewFont"));
	removeButton = new Button (newRemoveButtonsGroup, SWT.PUSH);
	removeButton.setLayoutData (new GridData (buttonAlignment));
	removeButton.setText (SWT.getMessage ("SWT_Remove"));
	
	// font sample group
	Group sampleGroup = new Group (controls, SWT.NONE);
	sampleGroup.setText (SWT.getMessage ("SWT_Sample"));
	gridData = new GridData ();
	gridData.heightHint = SAMPLE_HEIGHT;	
	gridData.horizontalSpan = 3;
	gridData.horizontalAlignment = GridData.FILL;	
	sampleGroup.setLayoutData (gridData);
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
	
	Composite okCancelGroup = new Composite (composite, SWT.NONE);
	layout = new GridLayout ();
	layout.marginHeight = layout.marginWidth = layout.verticalSpacing = 0;
	okCancelGroup.setLayout (layout);
	okCancelGroup.setLayoutData (new GridData (GridData.VERTICAL_ALIGN_BEGINNING));
	createButtons (okCancelGroup);
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
 * @deprecated use #getFontList ()
 */
public FontData getFontData () {
	if (fontData != null && fontData.length > 0) {
		return fontData [0];
	}
	return null;

}

FontData getFontData (String charsetName, String faceName, String extStyle, int size, String style) {
	Hashtable styles = getStyles (charsetName, faceName, extStyle, size);
	if (styles == null) return null;
	return (FontData) styles.get (style);
}

/**
 * Returns a FontData set describing the font that was
 * selected in the dialog, or null if none is available.
 * 
 * @return the FontData for the selected font, or null
 * @since 2.1.1
 */
public FontData [] getFontList () {
	return fontData;
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

String getListSelection (List list) {
	String [] selection = list.getSelection ();
	if (selection.length > 0) return selection [0];
	return "";
}

/**
 * Returns an RGB describing the color that was selected
 * in the dialog, or null if none is available.
 *
 * @return the RGB value for the selected color, or null
 *
 * @see PaletteData#getRGBs
 * 
 * @since 2.1
 */
public RGB getRGB () {
	return rgb;
}

/**
 * Returns a FontData object that can be used to load the selected 
 * font.
 */
FontData getSelectionFontData () {
	String charSetName = getListSelection (charSetList);
	String faceName = getListSelection (faceNameList);
	String extStyle = getListSelection (extStyleList);
	int size = DEFAULT_SIZE;
	try {
		size = Integer.valueOf (getListSelection (fontSizeList)).intValue ();
		if (size < 1) size = DEFAULT_SIZE;
	} catch (NumberFormatException e) {
		/*
		 * This block is purposely left empty since a default
		 * value is already specified above.
		 */
	}
	String style = getListSelection (fontStyleList);
	FontData result = getFontData (charSetName, faceName, extStyle, size, style);

	if (result != null) {
		result = copyFontData (result);
	} else {
		/*
		* One or more of the dialog's widgets contain custom typed values.
		* Create a FontData that mirrors these values so that the Font created
		* below will try to find the best match.
		*/
		result = new FontData ();
		result.characterSetRegistry = charSetName;
		result.setName (faceName);
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
String getTranslatedCharSet (FontData fontData, boolean includeDescription) {
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
		characterSet = PREFIX_ISO8859 + "-" + charSetName;
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
	if (includeDescription && translatedCharSet != null) {
		translatedCharSet = characterSet + " (" + translatedCharSet + ')';
	}
	else {
		translatedCharSet = characterSet;
	}
	return translatedCharSet;
}

/**
 * Returns the face name as specified in FontData.familyName followed by
 * the foundry set in parantheses if available.
 * We display the face name first so that the list sorts the fonts by 
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
 * List selections cause the downstream lists to be initialized 
 * with font data and the sample text to be updated.
 */
void handleEvent (Event event) {
	if (ignoreEvents) return;
	if (event.widget instanceof List) {
		List list = (List) event.widget;
		String text = getListSelection (list);
		int oldSelectIndex = ((Integer)list.getData ()).intValue ();
		int newSelectIndex = list.indexOf (text);
		if (oldSelectIndex != newSelectIndex || newSelectIndex == -1) {
			ignoreEvents = true;
			if (list == charSetList) initFaceNameList ();
			else if (list == faceNameList) initExtStyleList ();
			else if (list == extStyleList) initSizeList ();
			else if (list == fontSizeList) initStyleList ();
			else if (event.widget == fontSetList) {
				currentFontData = fontData [fontSetList.getSelectionIndex ()];
				setFontControls (currentFontData);
				updateButtonEnablements ();
			}
	
			updateSampleFont ();
			updateFontList ();
			list.setData (new Integer (newSelectIndex));
			if (newSelectIndex != -1) {
				list.select (newSelectIndex);
			}
			ignoreEvents = false;
		}
		return;
	}
	
	if (event.widget instanceof Button) {
		if (event.widget == okButton) {
			okSelected = true;
			shell.close ();
		}
		else if (event.widget == cancelButton) {
			okSelected = false;
			shell.close ();
		}
		else if (event.widget == colorButton) {
			ColorDialog colorDialog = new ColorDialog (shell, SWT.NONE);
			colorDialog.setRGB (rgb);
			RGB newRgb = colorDialog.open ();
			if (newRgb != null) {
				rgb = newRgb;
				updateSampleColor ();
			}
		}
		else if (event.widget == newButton) {
			FontData [] newFontData = new FontData [fontData.length + 1];
			System.arraycopy (fontData, 0, newFontData, 0, fontData.length);
			FontData source = fontData [fontSetList.getSelectionIndex ()];
			FontData newFd = copyFontData (source);
			newFontData [newFontData.length - 1] = newFd;
			this.fontData = newFontData;
			updateFontList ();
			fontSetList.select (newFontData.length - 1);
			fontSetList.setData (new Integer (newFontData.length - 1));
			fontSetList.showSelection();
			updateButtonEnablements ();
		}
		else if (event.widget == removeButton) {
			int selectionIndex = fontSetList.getSelectionIndex ();
			FontData [] newFontData = new FontData [fontData.length - 1];
			System.arraycopy (fontData, 0, newFontData, 0, selectionIndex);
			System.arraycopy (fontData, selectionIndex + 1, newFontData, selectionIndex, newFontData.length - selectionIndex);
			fontData = newFontData;
			updateFontList ();
			updateButtonEnablements ();
			setFontControls (fontData [fontSetList.getSelectionIndex ()]);
		}
		else if (event.widget == upButton) {
			int selectionIndex = fontSetList.getSelectionIndex ();
			FontData temp = fontData [selectionIndex];
			fontData [selectionIndex] = fontData [selectionIndex - 1];
			fontData [selectionIndex - 1] = temp;
			fontSetList.select (selectionIndex - 1);
			fontSetList.setData (new Integer (selectionIndex - 1));
			updateFontList ();
			updateButtonEnablements ();
		}
		else if (event.widget == downButton) {
			int selectionIndex = fontSetList.getSelectionIndex ();
			FontData temp = fontData [selectionIndex];
			fontData [selectionIndex] = fontData [selectionIndex + 1];
			fontData [selectionIndex + 1] = temp;
			fontSetList.select (selectionIndex + 1);
			fontSetList.setData (new Integer (selectionIndex + 1));
			updateFontList ();
			updateButtonEnablements ();
		}
	}
}

void hookListeners () {
	Listener listener = new Listener () {
		public void handleEvent (Event event) {
			FontDialog.this.handleEvent (event);
		}
	};
	okButton.addListener (SWT.Selection, listener);
	cancelButton.addListener (SWT.Selection, listener);
	colorButton.addListener (SWT.Selection, listener);	
	charSetList.addListener (SWT.Selection, listener);
	faceNameList.addListener (SWT.Selection, listener);
	fontStyleList.addListener (SWT.Selection, listener);
	extStyleList.addListener (SWT.Selection, listener);
	fontSizeList.addListener (SWT.Selection, listener);
	newButton.addListener (SWT.Selection, listener);
	removeButton.addListener (SWT.Selection, listener);
	upButton.addListener (SWT.Selection, listener);
	downButton.addListener (SWT.Selection, listener);
	fontSetList.addListener (SWT.Selection, listener);
}

/**
 * Initialize the extended styles list with the extended styles
 * available for the selected font.
 * Downstream lists are initialized as well (style and size).
 */
void initExtStyleList () {
	String oldSelect = getListSelection (extStyleList);
	extStyleList.removeAll ();
	
	String characterSet = getListSelection (charSetList);
	String faceName = getListSelection (faceNameList);
	Hashtable extStyles = getExtStyles (characterSet, faceName);
	setItemsSorted (extStyleList, extStyles);
	
	int selectIndex = extStyleList.indexOf (oldSelect);
	extStyleList.select (selectIndex);
	extStyleList.setData (new Integer (selectIndex));
	centerListIndex (extStyleList, selectIndex);
	initSizeList ();
}

/**
 * Initialize the face name list with all font names 
 * available in the selected character set.
 * Downstream lists are initialized as well (extended style).
 */
void initFaceNameList () {
	String oldSelect = getListSelection (faceNameList);
	faceNameList.removeAll ();
	String charSetText = getListSelection (charSetList);
	if (charSetText.length () == 0) return;
	
	Hashtable faceNames = getFaces (charSetText);
	setItemsSorted (faceNameList, faceNames);
	
	int selectIndex = faceNameList.indexOf (oldSelect);
	selectIndex = Math.max (0, selectIndex);
	faceNameList.select (selectIndex);
	faceNameList.setData (new Integer (selectIndex));
	centerListIndex (faceNameList, selectIndex);
	initExtStyleList ();
}

/**
 * Initialize the widgets of the receiver with the data of 
 * all installed fonts.  If the user specified a default font
 * preselect that font in the lists.
 */
void initFonts () {
	Display display = shell.display;
	// get all fonts available on the current display
	addFonts (display.getFontList (null, false));
	addFonts (display.getFontList (null, true));
	setItemsSorted (charSetList, getFonts ());
	if (fontData != null) {
		// verify that the initial font data is a valid font
		Font font = new Font (display, fontData);
		fontData = font.getFontData ();
		currentFontData = fontData [0];
		font.dispose ();
	} else {
		fontData = display.textFont.getFontData ();
		currentFontData = fontData [0];
	}
}

/**
 * Initialize the size list with the sizes the selected font 
 * is available in.  If the selected font is scalable a selection
 * of preset sizes is used.
 */
void initSizeList () {
	String oldSelect = getListSelection (fontSizeList);
	fontSizeList.removeAll ();
	
	String characterSet = getListSelection (charSetList);
	String faceName = getListSelection (faceNameList);
	String extStyle = getListSelection (extStyleList);
	Hashtable sizes = getSizes (characterSet, faceName, extStyle);
	if (sizes != null) {
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
	}
	
	int selectIndex = fontSizeList.indexOf (oldSelect);
	if (selectIndex == -1) {
		selectIndex = fontSizeList.indexOf (String.valueOf (DEFAULT_SIZE));
	}
	selectIndex = Math.max (0, selectIndex);
	fontSizeList.select (selectIndex);
	fontSizeList.setData (new Integer (selectIndex));
	centerListIndex (fontSizeList, selectIndex);
	initStyleList ();
}

/**
 * Initialize the styles list with the styles the selected font 
 * is available in.
 */
void initStyleList () {
	String oldSelect = getListSelection (fontStyleList);
	fontStyleList.removeAll ();
	
	String characterSet = getListSelection (charSetList);
	String faceName = getListSelection (faceNameList);
	String extStyle = getListSelection (extStyleList);
	try {
		int size = Integer.valueOf (getListSelection (fontSizeList)).intValue ();
		if (size > 0) {
			Hashtable styles = getStyles (characterSet, faceName, extStyle, size);
			setItemsSorted (fontStyleList, styles);
		}
	} catch (NumberFormatException e) {
		// fall through
	}

	int selectIndex = fontStyleList.indexOf (oldSelect);
	if (selectIndex == -1) {
		selectIndex = fontStyleList.indexOf (String.valueOf (DEFAULT_STYLE));
	}
	selectIndex = Math.max (0, selectIndex);
	fontStyleList.select (selectIndex);
	fontStyleList.setData (new Integer (selectIndex));
	centerListIndex (fontStyleList, selectIndex);
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
	shell.setLayout (new GridLayout ());
	createControls (shell);
	
	FontData [] originalFontData = fontData;
	RGB originalRGB = rgb;
	initFonts ();
	openDialog ();
	setFontControls (currentFontData);
	updateSampleFont ();
	updateSampleColor ();
	updateFontList ();
	fontSetList.select (0);
	fontSetList.setData (new Integer (0));
	updateButtonEnablements ();
	hookListeners ();
	Display display = shell.display;
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	
	FontData result = null;
	if (okSelected) {
		result = fontData [0];
	} else {
		fontData = originalFontData;
		rgb = originalRGB;
	}	
	if (sampleFont != null) sampleFont.dispose ();
	sampleFont = null;
	if (sampleColor != null) sampleColor.dispose ();
	sampleColor = null;
	return result;
}

/**
 * Open the receiver and set its size to the size calculated by 
 * the layout manager.
 */
void openDialog () {
	// Start everything off by setting the shell size to its computed size.
	Point pt = shell.computeSize (SWT.DEFAULT, SWT.DEFAULT, false);
	
	// Ensure that the width of the shell fits the display.
	Display display = shell.display;
	Rectangle displayRect = display.getBounds ();
	int widthLimit = displayRect.width * 7 / 8;
	int heightLimit = displayRect.height * 7 / 8;
	if (pt.x > widthLimit) {
		pt = shell.computeSize (widthLimit, SWT.DEFAULT, false);
	}
	
	/*
	 * If the parent is visible then center this dialog on it,
	 * otherwise center this dialog on the parent's monitor
	 */
	Rectangle parentBounds = null;
	if (parent.isVisible ()) {
		parentBounds = getParent ().getBounds ();
	} else {
		parentBounds = parent.getMonitor ().getBounds ();
	}
	int originX = (parentBounds.width - pt.x) / 2 + parentBounds.x;
	originX = Math.max (originX, 0);
	originX = Math.min (originX, widthLimit - pt.x);
	int originY = (parentBounds.height - pt.y) / 2 + parentBounds.y;
	originY = Math.max (originY, 0);
	originY = Math.min (originY, heightLimit - pt.y);
	shell.setBounds (originX, originY, pt.x, pt.y);
	
	String title = getText ();
	if (title.length () == 0) title = SWT.getMessage ("SWT_FontDialog_Title");
	shell.setText (title);
	
	// Open the window.
	shell.open ();
}

/**
 * Initialize the lists with the data of the preselected
 * font specified by the user.
 */
void setFontControls (FontData fontData) {
	ignoreEvents = true;
	String characterSet = getTranslatedCharSet (fontData, true);
	String faceName = getTranslatedFaceName (fontData);
	charSetList.select (new String[] {characterSet});
	int index = charSetList.indexOf (characterSet);
	charSetList.setData (new Integer (index));
	if (index != -1) centerListIndex (charSetList, index);

	initFaceNameList ();
	faceNameList.select (new String[] {faceName});
	index = faceNameList.indexOf (faceName);
	faceNameList.setData (new Integer (index));
	if (index != -1) centerListIndex (faceNameList, index);

	initExtStyleList ();
	extStyleList.select (new String[] {fontData.addStyle});
	index = extStyleList.indexOf (fontData.addStyle);
	extStyleList.setData (new Integer (index));
	if (index != -1) centerListIndex (extStyleList, index);

	initSizeList ();
	String value = String.valueOf (fontData.getHeight ());
	fontSizeList.select (new String[] {value});
	index = fontSizeList.indexOf (value);
	fontSizeList.setData (new Integer (index));
	if (index != -1) centerListIndex (fontSizeList, index);

	initStyleList ();
	fontStyleList.select (new String[] {fontData.weight});
	index = fontStyleList.indexOf (fontData.weight);
	fontStyleList.setData (new Integer (index));
	if (index != -1) centerListIndex (fontStyleList, index);
	ignoreEvents = false;
}

/**
 * Sets a FontData object describing the font to be
 * selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the FontData to use initially, or null
 * @deprecated use #setFontList (FontData [])
 */
public void setFontData (FontData fontData) {
	if (fontData == null) {
		this.fontData = null;
	} else {
		this.fontData = new FontData [1];
		this.fontData [0] = fontData;
	}
}

/**
 * Sets the set of FontData objects describing the font to
 * be selected by default in the dialog, or null to let
 * the platform choose one.
 * 
 * @param fontData the set of FontData objects to use initially, or null
 *        to let the platform select a default when open() is called
 *
 * @see Font#getFontData
 * 
 * @since 2.1.1
 */
public void setFontList (FontData [] fontData) {
	this.fontData = fontData;
}

/**
 * Set the contents of 'list' to the keys of 'items'.
 * Keys are sorted in ascending order first and have to be Strings.
 */
void setItemsSorted (List list, Hashtable items) {
	if (items == null) return;
	Enumeration itemKeys = items.keys ();
	String [] sortedItems = new String [items.size ()];
	int index = 0;
	while (itemKeys.hasMoreElements ()) {
		String item = (String) itemKeys.nextElement ();
		if (item.length () != 0) sortedItems [index++] = item;
	}
	if (index != sortedItems.length) {
		String [] newItems = new String [index];
		System.arraycopy (sortedItems, 0, newItems, 0, index);
		sortedItems = newItems;
	}
	sort (sortedItems);
	list.setItems (sortedItems);
}

/**
 * Sets the RGB describing the color to be selected by default
 * in the dialog, or null to let the platform choose one.
 *
 * @param rgb the RGB value to use initially, or null to let
 *        the platform select a default when open() is called
 *
 * @see PaletteData#getRGBs
 * 
 * @since 2.1
 */
public void setRGB (RGB rgb) {
	this.rgb = rgb;
}

/**
 * Set the contents of the size list to the keys of 'items'.
 * Keys are sorted in ascending order first and have to be Integers.
 */
void setSizeItemsSorted (Enumeration itemsEnum) {
	Vector items = new Vector ();
	while (itemsEnum.hasMoreElements ()) {
		items.addElement (itemsEnum.nextElement ());
	}
	Integer [] sortedItems = new Integer [items.size ()];
	items.copyInto (sortedItems);
	sort (sortedItems);
	String [] sortedItemStrings = new String [items.size ()];
	for (int i = 0; i < sortedItemStrings.length; i++) {
		sortedItemStrings [i] = String.valueOf (sortedItems [i].intValue ());
	}
	fontSizeList.setItems (sortedItemStrings);
}

/**
 * Sort 'items' in ascending order.
 */
void sort (Integer [] items) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap = length / 2; gap > 0; gap /= 2) {
		for (int i = gap; i < length; i++) {
			for (int j = i - gap; j >= 0; j -= gap) {
		   		if (items [j].intValue () > items [j + gap].intValue ()) {
					Integer swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}

/**
 * Sort 'items' in ascending order.
 */
void sort (String items []) {
	/* Shell Sort from K&R, pg 108 */
	int length = items.length;
	for (int gap = length / 2; gap > 0; gap /= 2) {
		for (int i = gap; i < length; i++) {
			for (int j = i - gap; j >= 0; j -= gap) {
		   		if (items [j].compareTo (items [j + gap]) > 0) {
					String swap = items [j];
					items [j] = items [j + gap];
					items [j + gap] = swap;
		   		}
	    	}
	    }
	}
}

void updateButtonEnablements () {
	removeButton.setEnabled (fontSetList.getItemCount () > 1);
	upButton.setEnabled (fontSetList.getSelectionIndex () > 0);
	downButton.setEnabled (fontSetList.getSelectionIndex () < fontSetList.getItemCount () - 1);
}

void updateFontList () {
	int selectionIndex = fontSetList.getSelectionIndex ();
	int topIndex = Math.max (0, fontSetList.getTopIndex ());
	String [] items = new String [fontData.length];
	for (int i = 0; i < fontData.length; i++) {
		StringBuffer buffer = new StringBuffer ();
		buffer.append (i);
		buffer.append (": ");
		buffer.append (getTranslatedCharSet (fontData [i], false));
		buffer.append ("-");
		buffer.append (getTranslatedFaceName (fontData [i]));
		buffer.append ("-");
		if (!fontData [i].addStyle.equals ("")) {
			buffer.append (fontData [i].addStyle);
			buffer.append ("-");
		}
		buffer.append (fontData [i].getHeight ());
		buffer.append ("-");
		buffer.append (fontData [i].weight);
		items [i] = buffer.toString (); 
	}
	fontSetList.setItems (items);
	if (selectionIndex >= items.length) selectionIndex--;
	fontSetList.select (selectionIndex);
	fontSetList.setData (new Integer (selectionIndex));
	fontSetList.setTopIndex (topIndex);
	fontSetList.showSelection ();
}

void updateSampleColor () {
	if (rgb == null) {
		rgb = new RGB (0, 0, 0);
	}
	if (sampleColor != null) {
		if (sampleColor.getRGB ().equals (rgb)) return;
		sampleColor.dispose ();
	}
	sampleColor = new Color (parent.display, rgb);
	sampleLabel.setForeground (sampleColor);
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
	if (sampleFont != null) sampleFont.dispose ();
	int selectionIndex = Math.max (0, fontSetList.getSelectionIndex ());
	fontData [selectionIndex] = selectionFontData;
	sampleFont = new Font (shell.display, selectionFontData);
	sampleLabel.setFont (sampleFont);
}

}
