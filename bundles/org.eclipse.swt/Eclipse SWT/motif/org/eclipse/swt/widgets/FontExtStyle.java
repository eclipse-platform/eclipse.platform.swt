package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * SWT - The Simple Widget Toolkit,
 * (c) Copyright IBM Corp 1998, 1999.
 */
 
import java.util.*;

/**
 * This class stores the sizes and styles for a font extended style.
 * A font on X can have different styles and sizes depending on the 
 * extended style of a font. 
 */
class FontExtStyle {
	private Vector sizes;
	private Vector styles;
/**
 * Create a new instance of the receiver
 */
FontExtStyle() {
	sizes = new Vector();
	styles = new Vector();		
}
/**
 * Add size to the existing sizes of the receiver. 
 * Insert in ascending sort order.
 */
void addSize(int size) {
	Vector sizes = getSizes();
	Integer sizeInteger;
	Integer newSizeInteger = new Integer(size);

	if (sizes.contains(newSizeInteger) == false) {
		for (int i = 0; i < sizes.size(); i++) {
			sizeInteger = (Integer) sizes.elementAt(i);			
			if (sizeInteger.intValue() > size) {
				sizes.insertElementAt(newSizeInteger, i);
				return;
			}
		}
		sizes.addElement(newSizeInteger);
	}
}
/**
 * Add the 'style' to the list of styles.
 * Styles are sorted in the order "medium", "medium italic", "bold",
 * "bold italic"
 * @param style - the font style. Can be "medium", "medium italic", 
 * 	"bold", "bold italic"
 */
void addStyle(String style) {
	Vector styles = getStyles();
	String existingStyle;

	if (styles.contains(style) == true) {
		return;
	}			
	// "medium" always goes to top of list
	if (style.equals(FontExtStyles.MEDIUM) == true) {
		styles.insertElementAt(style, 0);
	}
	else {
		// order is: "medium" "medium italic" "bold" "bold italic"
		for (int i = 0; i < styles.size(); i++) {
			existingStyle = (String) styles.elementAt(i);
			if (existingStyle.startsWith(FontExtStyles.BOLD) == true && 
				(style.startsWith(FontExtStyles.MEDIUM) == true || 
				 style.equals(FontExtStyles.BOLD) == true)) {
				styles.insertElementAt(style, i);
				return;
			}
		}
		styles.addElement(style);
	}
}
/**
 * Answer the sizes that are available for the existing font.
 * Vector is empty if the font is scalable.
 */
Vector getSizes() {
	return sizes;
}
/**
 * Answer the styles that are available for this extended style.
 */
Vector getStyles() {
	return styles;
}
}
