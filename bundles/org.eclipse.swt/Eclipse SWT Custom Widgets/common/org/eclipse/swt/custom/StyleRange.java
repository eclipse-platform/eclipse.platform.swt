/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public int fontStyle = SWT.NORMAL;	// may be SWT.NORMAL, SWT.ITALIC or SWT.BOLD
	public boolean underline;
	public boolean strikeout;

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
 * @param fontStyle font style of the style, may be SWT.NORMAL, SWT.ITALIC or SWT.BOLD
 */
public StyleRange(int start, int length, Color foreground, Color background, int fontStyle) {
	this.start = start;
	this.length = length;
	this.foreground = foreground;
	this.background = background;
	this.fontStyle = fontStyle;
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode()
 */
public boolean equals(Object object) {
	StyleRange style;
	if (object == this) return true;
	if (object instanceof StyleRange) style = (StyleRange)object;
	else return false;
	if (this.start != style.start) return false;
	if (this.length != style.length) return false;
	return similarTo(style);
}
/**
 * Returns an integer hash code for the receiver. Any two 
 * objects that return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals(Object)
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
	if (this.underline) return false;
	if (this.strikeout) return false;
	return true;
}
/**
 * Compares the specified object to this StyleRange and answer if the two 
 * are similar. The object must be an instance of StyleRange and have the
 * same field values for except for start and length.
 * <p>
 *
 * @param style the object to compare with this object
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
	if (this.underline != style.underline) return false;
	if (this.strikeout != style.strikeout) return false;
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
 	style.underline = this.underline;
 	style.strikeout = this.strikeout;
	return style;
}
/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
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
	if (underline) buf.append(" underline");
	if (strikeout) buf.append(" strikeout");
	return buf.toString();
}
}
