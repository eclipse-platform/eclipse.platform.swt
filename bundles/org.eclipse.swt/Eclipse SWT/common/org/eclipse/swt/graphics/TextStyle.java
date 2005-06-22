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
package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

/**
 * <code>TextStyle</code> defines a set of styles that can be applied
 * to a range of text.
 * <p>
 * The hashCode() method in this class uses the values of the public
 * fields to compute the hash value. When storing instances of the
 * class in hashed collections, do not modify these fields after the
 * object has been inserted.  
 * </p>
 * <p>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 * </p>
 * 
 * @see TextLayout
 * @see Font
 * @see Color
 *  
 * @since 3.0
 */
public class TextStyle {

	/**
	 * the font of the style
	 */
	public Font font;

	/**
	 * the foreground of the style
	 */
	public Color foreground;

	/**
	 * the background of the style
	 */
	public Color background;

	/**
	 * the underline flag of the style
	 */	
	public boolean underline;
	
	/**
	 * the strikeout flag of the style
	 */	
	public boolean strikeout;

/** 
 * Create a new text style with the specified font, foreground
 * and background.
 *
 * @param font the font of the style, <code>null</code> if none 
 * @param foreground the foreground color of the style, <code>null</code> if none 
 * @param background the background color of the style, <code>null</code> if none
 */
public TextStyle (Font font, Color foreground, Color background) {
	if (font != null && font.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	if (foreground != null && foreground.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);
	if (background != null && background.isDisposed()) SWT.error (SWT.ERROR_INVALID_ARGUMENT);	
	this.font = font;
	this.foreground = foreground;
	this.background = background;
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
	if (object == null) return false;
	if (!(object instanceof TextStyle)) return false;
	TextStyle style = (TextStyle)object;	
	if (this.foreground != null) {
		if (!this.foreground.equals(style.foreground)) return false;
	} else if (style.foreground != null) return false;
	if (this.background != null) {
		if (!this.background.equals(style.background)) return false;
	} else if (style.background != null) return false;
	if (this.font != null) {
		if (!this.font.equals(style.font)) return false;
	} else if (style.font != null) return false;
	if (this.underline != style.underline) return false;
	if (this.strikeout != style.strikeout) return false;
	return true;
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
	int hash = 0;
	if (foreground != null) hash ^= foreground.hashCode();
	if (background != null) hash ^= background.hashCode();
	if (font != null) hash ^= font.hashCode();
	if (underline) hash ^= hash;
	if (strikeout) hash ^= hash;
	return hash;
}

/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the <code>RGB</code>
 */
public String toString () {
	return "TextStyle {font: " + font + ", foreground: " + foreground + ", background: " + background + ", underline: " + underline + ", strikeout: " + strikeout + "}"; //$NON-NLS-1$//$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
}

}
