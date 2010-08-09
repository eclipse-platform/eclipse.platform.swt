/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * Portion Copyright (c) 2009-2010 compeople AG (http://www.compeople.de).
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *     Compeople AG	- QtJambi/Qt based implementation for Windows/Mac OS X/Linux
 *******************************************************************************/
package org.eclipse.swt.graphics;

import com.trolltech.qt.gui.QFontMetrics;

/**
 * Instances of this class provide measurement information about fonts including
 * ascent, descent, height, leading space between rows, and average character
 * width. <code>FontMetrics</code> are obtained from <code>GC</code>s using the
 * <code>getFontMetrics()</code> method.
 * 
 * @see GC#getFontMetrics
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further
 *      information</a>
 */
public final class FontMetrics {
	private final int ascent;
	private final int descent;
	private final int aveCharWidth;
	private final int leading;
	private final int height;

	private FontMetrics(int ascent, int descent, int aveCharWidth, int leading, int height) {
		this.ascent = ascent;
		this.descent = descent;
		this.aveCharWidth = aveCharWidth;
		this.leading = leading;
		this.height = height;
	}

	/**
	 * Compares the argument to the receiver, and returns true if they represent
	 * the <em>same</em> object using a class specific comparison.
	 * 
	 * @param object
	 *            the object to compare with this object
	 * @return <code>true</code> if the object is the same as this object and
	 *         <code>false</code> otherwise
	 * 
	 * @see #hashCode
	 */
	@Override
	public boolean equals(Object object) {
		if (object == this) {
			return true;
		}
		if (!(object instanceof FontMetrics)) {
			return false;
		}
		FontMetrics other = (FontMetrics) object;
		return ascent == other.ascent && descent == other.descent && aveCharWidth == other.aveCharWidth
				&& leading == other.leading && height == other.height;
	}

	/**
	 * Returns the ascent of the font described by the receiver. A font's
	 * <em>ascent</em> is the distance from the baseline to the top of actual
	 * characters, not including any of the leading area, measured in pixels.
	 * 
	 * @return the ascent of the font
	 */
	public int getAscent() {
		return ascent;
	}

	/**
	 * Returns the average character width, measured in pixels, of the font
	 * described by the receiver.
	 * 
	 * @return the average character width of the font
	 */
	public int getAverageCharWidth() {
		return aveCharWidth;
	}

	/**
	 * Returns the descent of the font described by the receiver. A font's
	 * <em>descent</em> is the distance from the baseline to the bottom of
	 * actual characters, not including any of the leading area, measured in
	 * pixels.
	 * 
	 * @return the descent of the font
	 */
	public int getDescent() {
		return descent;
	}

	/**
	 * Returns the height of the font described by the receiver, measured in
	 * pixels. A font's <em>height</em> is the sum of its ascent, descent and
	 * leading area.
	 * 
	 * @return the height of the font
	 * 
	 * @see #getAscent
	 * @see #getDescent
	 * @see #getLeading
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Returns the leading area of the font described by the receiver. A font's
	 * <em>leading area</em> is the space above its ascent which may include
	 * accents or other marks.
	 * 
	 * @return the leading space of the font
	 */
	public int getLeading() {
		return leading;
	}

	/**
	 * Returns an integer hash code for the receiver. Any two objects that
	 * return <code>true</code> when passed to <code>equals</code> must return
	 * the same value for this method.
	 * 
	 * @return the receiver's hash
	 * 
	 * @see #equals
	 */
	@Override
	public int hashCode() {
		return ascent ^ descent ^ aveCharWidth ^ leading ^ height;
	}

	/**
	 * Invokes platform specific functionality to allocate a new font metrics.
	 * <p>
	 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public API for
	 * <code>FontMetrics</code>. It is marked public only so that it can be
	 * shared within the packages provided by SWT. It is not available on all
	 * platforms, and should never be called from application code.
	 * </p>
	 * 
	 * @param metrics
	 *            the <code>QFontMetrics</code> containing information about a
	 *            font
	 * @return a new font metrics object containing the specified
	 *         <code>TEXTMETRIC</code>
	 */
	public static FontMetrics internal_new(QFontMetrics qfm) {
		return new FontMetrics(qfm.ascent(), qfm.descent(), qfm.averageCharWidth(), 0, qfm.height());
	}

	public static FontMetrics internal_new(Font font) {
		return internal_new(new QFontMetrics(font.getQFont()));
	}

	public static FontMetrics internal_new(int ascent, int descent, int aveCharWidth, int leading, int height) {
		return new FontMetrics(ascent, descent, aveCharWidth, leading, height);
	}
}
