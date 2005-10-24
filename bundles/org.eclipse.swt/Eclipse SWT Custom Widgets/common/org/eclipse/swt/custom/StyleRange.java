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
	
	/**
	 * the start offset of the range. zero-based from the document start
	 */
	public int start;
	
	/**
	 * the length of the range
	 */
	public int length;

	/**
	 * the foreground of the range
	 */
	public Color foreground;
	
	/**
	 * the background of the range
	 */
	public Color background;
	
	/**
	 * the underline flag of the range
	 */
	public boolean underline;
	
	/**
	 * the strikeout flag of the range
	 */
	public boolean strikeout;
	
	/**
	 * the font style of the range. It may be a combination of
	 * SWT.NORMAL, SWT.ITALIC or SWT.BOLD
	 */
	public int fontStyle = SWT.NORMAL;
	
	/*
	 * API UNDER CONSTRUCTION - DO NOT USE THIS FIELD
	 * 
	 * the font of the range, when set the fontStyle flag is ignored
	 *
	 */
	public Font font;
	
	/*
	 * API UNDER CONSTRUCTION - DO NOT USE THIS FIELD
	 * 
	 * baseline rise
	 *
	 */
	public int rise;
	
	/*
	 * API UNDER CONSTRUCTION - DO NOT USE THIS FIELD
	 * 
	 * embedded object
	 *
	 */
	public EmbeddedObject object;
	
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
	if (object == this) return true;
	if (object instanceof StyleRange) {
		StyleRange style = (StyleRange)object;
		if (start != style.start) return false;
		if (length != style.length) return false;
		return similarTo(style);
	}
	return false;
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
	if (foreground != null) code += foreground.hashCode();
	if (background != null) code += background.hashCode();
	if (font != null) code += font.hashCode();
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
	if (foreground != null) return false;
	if (background != null) return false;
	if (font != null) return false;
	if (object != null) return false;
	if (fontStyle != SWT.NORMAL) return false;
	if (underline) return false;
	if (strikeout) return false;
	if (rise != 0) return false;
	return true;
}

boolean isVariableLineHeight() {
	return font != null || object != null || rise != 0;
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
	if (foreground != null) {
		if (!foreground.equals(style.foreground)) return false;
	} else if (style.foreground != null) {
		return false;
	}
	if (background != null) {
		if (!background.equals(style.background)) return false;
	} else if (style.background != null) {
		return false; 
	}
	if (font != null) {
		if (!font.equals(style.font)) return false;
	} else if (style.font != null) {
		return false; 
	}
	if (object != null) {
		if (!object.equals(style.object)) return false;
	} else if (style.object != null) {
		return false; 
	}
	if (fontStyle != style.fontStyle) return false;
	if (underline != style.underline) return false;
	if (strikeout != style.strikeout) return false;
	if (rise != style.rise) return false;
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
 	style.font = font;
 	style.object = object;
 	style.underline = underline;
 	style.strikeout = strikeout;
 	style.rise = rise; 	
	return style;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the event
 */
public String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append(start + "," + length + " fg:" + foreground + " bg:" + background);
	if (font != null) {
		buffer.append(" font:");
		FontData fontData = font.getFontData()[0];
		buffer.append(fontData);
	} else {
		buffer.append(" fStyle:");
		switch (fontStyle) {
			case SWT.BOLD:
				buffer.append("bold");
				break;
			case SWT.ITALIC:
				buffer.append("italic");
				break;
			case SWT.BOLD | SWT.ITALIC:
				buffer.append("bold-italic");
				break;
			default:
				buffer.append("normal");
		}
	}
	if (underline) buffer.append(" underline");
	if (strikeout) buffer.append(" strikeout");
	return buffer.toString();
}
}
