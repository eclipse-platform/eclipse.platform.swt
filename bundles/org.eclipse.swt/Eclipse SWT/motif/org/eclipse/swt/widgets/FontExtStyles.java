package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * SWT - The Simple Widget Toolkit,
 * (c) Copyright IBM Corp 1998, 1999.
 */

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import java.util.*;

/**
 * This class stores the extended styles that are available for a 
 * font as well as the sizes and styles that are available for each 
 * extended styles.
 */
class FontExtStyles {
	final static String MEDIUM = "medium";
	final static String BOLD = "bold";
	final static String ITALIC = "italic";
	
	private String faceName;
	private boolean isScalable = false;
	private Hashtable extStyles = new Hashtable();	// keys are extended styles, 
													// values a Vector with the 
													// corresponding sizes or null 
													// if the font is scalable
/**
 * Create a new instance of the receiver for the font with the
 * name 'faceName'.
 */
FontExtStyles(String faceName) {
	this.faceName = faceName;
}
/**
 * Add the extended style, style and size data of 'fontData' 
 * to the receiver.
 * The face name in 'fontData' has to match the face name of 
 * the receiver.
 */
void add(FontData fontData) {
	Hashtable extStyles = getExtStyles();
	FontExtStyle extStyle;
	Integer height;
	String style;
		
	if (fontData.getName().equals(getFaceName()) == false) {
		return;
	}
	extStyle = (FontExtStyle) extStyles.get(fontData.addStyle);
	if (extStyle == null) {
		extStyle = new FontExtStyle();
		extStyles.put(fontData.addStyle, extStyle);
	}
	if (fontData.getHeight() == 0) {
		setScalable(true);		
	}
	else {	// add the font size if the font is not scalable
		extStyle.addSize(fontData.getHeight());
	}
	// add the style
	style = getStyleString(fontData.getStyle());
	extStyle.addStyle(style);
}
/**
 * Answer the extended styles stored by the receiver.
 * See the class definition for an explanation of the returned 
 * data structure.
 */
Hashtable getExtStyles() {
	return extStyles;
}
/**
 * Answer the face name of the receiver.
 * The extended styles are stored for this face name.
 */
String getFaceName() {
	return faceName;
}
/**
 * Answer the font sizes of the receiver for the extended style 
 * identified by 'extStyle'.
 * @return the font sizes of the receiver for the extended style 
 *	identified by 'extStyle'.
 *	Empty collection if isScalable() answers true.
 */
Vector getSizes(String extStyle) {
	return ((FontExtStyle) getExtStyles().get(extStyle)).getSizes();
}
/**
 * Answer the string displayed for 'style'
 * @param style - the font style. Can be SWT.BOLD, SWT.ITALIC 
 *	or both combined with logical OR.
 */
static String getStyleString(int style) {
	String styleString;
		
	if ((style & SWT.BOLD) != 0) {
		styleString = BOLD;
	}
	else {
		styleString = MEDIUM;
	}
	if ((style & SWT.ITALIC) != 0) {
		styleString += ' ' + ITALIC;
	}
	return styleString;
}
/**
 * Answer the font styles of the receiver for the extended style 
 * identified by 'extStyle'.
 */
Vector getStyles(String extStyle) {
	return ((FontExtStyle) getExtStyles().get(extStyle)).getStyles();
}
/**
 * Answer whether the receiver is scalable or not.
 * @return true=receiver is scalable. false=receiver is not 
 *	scalable. It stores all available font sizes.
 */
boolean isScalable() {
	return isScalable;
}
/**
 * Set whether the receiveris scalable..
 * @param newIsScalable - true=receiver is scalable. 
 *	false=receiver is not scalable. It stores all available font sizes.
 */
void setScalable(boolean newIsScalable) {
	isScalable = newIsScalable;
}
}
