/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.custom;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.internal.CloneableCompatibility;

public class StyleRange implements CloneableCompatibility {
	public int start;		// style start offset. 0 based from the document start
	public int length;		// style length.	
	public Color foreground; 
	public Color background;
	public int fontStyle = SWT.NORMAL;	// may be SWT.NORMAL or SWT.BOLD

public StyleRange() {
}
/** 
 * Create a new style range.
 * <p>
 *
 * @param start start offset of the style
 * @param length length of the style 
 * @param foreground foreground color of the style, null if none 
 * @param background background color of the style, null if none
 */
public StyleRange(int start, int length, Color foreground, Color background) {
	this.start = start;
	this.length = length;
	this.foreground = foreground;
	this.background = background;
}

/** 
 * Create a new style range.
 * <p>
 *
 * @param start start offset of the style
 * @param length length of the style 
 * @param foreground foreground color of the style, null if none 
 * @param background background color of the style, null if none
 * @param fontStyle font style of the style, may be SWT.NORMAL or SWT.BOLD
 */
public StyleRange(int start, int length, Color foreground, Color background, int fontStyle) {
	this.start = start;
	this.length = length;
	this.foreground = foreground;
	this.background = background;
	this.fontStyle = fontStyle;
}

/**
 * Compare the specified object to this StyleRange and answer if the two 
 * are equal. The object must be an instance of StyleRange and have the
 * same field values.
 * <p>
 *
 * @param object the object to compare with this object
 * @return true if the objects are equal, false otherwise
 */
public boolean equals(Object object) {
	StyleRange style;
	if (object == this) return true;
	if (object instanceof StyleRange) style = (StyleRange)object;
	else return false;
	if (this.start != style.start) return false;
	if (this.length != style.length) return false;
	if (this.foreground != null) {
		if (!this.foreground.equals(style.foreground)) return false;
	} else if (style.foreground != null) return false;
	if (this.background != null) {
		if (!this.background.equals(style.background)) return false;
	} else if (style.background != null) return false; 
	if (this.fontStyle != style.fontStyle) return false;
	return true;
}
/**
 * Returns an integer hash code for the receiver. Objects which are
 * equal answer the same value for this method.
 * <p>
 *
 * @return the receiver's hash
 */
public int hashCode() {
	int code = start + length;
	
	if (foreground != null)
		code += foreground.hashCode();
	if (background != null)
		code += background.hashCode();
	return code + fontStyle;
}
/**
 * Returns whether or not the receiver is unstyled (i.e., does not have any 
 * style attributes specified).
 * <p>
 *
 * @return true if the receiver is unstyled, false otherwise.
 */
public boolean isUnstyled() {
	if (this.foreground != null) return false;
	if (this.background != null) return false;
	if (this.fontStyle != SWT.NORMAL) return false;
	return true;
}
/**
 * Compares the specified object to this StyleRange and answer if the two 
 * are similar. The object must be an instance of StyleRange and have the
 * same field values for except for start and length.
 * <p>
 *
 * @param object the object to compare with this object
 * @return true if the objects are similar, false otherwise
 */
public boolean similarTo(StyleRange style) {
	if (this.foreground != null) {
		if (!this.foreground.equals(style.foreground)) return false;
	} else if (style.foreground != null) return false;
	if (this.background != null) {
		if (!this.background.equals(style.background)) return false;
	} else if (style.background != null) return false; 
	if (this.fontStyle != style.fontStyle) return false;
	return true;
}
/**
 * Answers a new StyleRange with the same values as this StyleRange.
 * <p>
 *
 * @return a shallow copy of this StyleRange
 */	
public Object clone() {
 	StyleRange style = new StyleRange(start, length, foreground, background, fontStyle);
	return style;
}
/** 
 * Answers a string description of the receiver.
 * <p>
 *
 * @return a printable representation for the receiver.
 */
public String toString() {
	StringBuffer buf = new StringBuffer();
	buf.append(start + "," + length + " fg:" + foreground + " bg:" + background + " fStyle:");
	switch (fontStyle) {
		case SWT.BOLD:
			buf.append("bold");
			break;
		case SWT.ITALIC:
			buf.append("italic");
			break;
		case SWT.BOLD | SWT.ITALIC:
			buf.append("bold-italic");
			break;
		default:
			buf.append("normal");
	}
	return buf.toString();
}
}
