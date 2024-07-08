/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


/**
 * Instances of this class provide measurement information
 * about fonts including ascent, descent, height, leading
 * space between rows, and average character width.
 * <code>FontMetrics</code> are obtained from <code>GC</code>s
 * using the <code>getFontMetrics()</code> method.
 *
 * @see GC#getFontMetrics
 */

public final class FontMetrics {

	/**
	 * On Windows, handle is a Win32 TEXTMETRIC struct
	 * On Photon, handle is a Photon FontQueryInfo struct
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public java.awt.FontMetrics handle;

  int ascent;
  int descent;
  int height;
  int leading;
  int averageCharWidth;

/**
 * Prevents instances from being created outside the package.
 */
FontMetrics() {
}

/**
 * Compares the argument to the receiver, and returns true
 * if they represent the <em>same</em> object using a class
 * specific comparison.
 *
 * @param object the object to compare with this object
 * @return <code>true</code> if the object is the same as this object and <code>false</code> otherwise
 *
 * @see #hashCode
 */
@Override
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof FontMetrics)) return false;
  return ((FontMetrics)object).handle.equals(handle);
//	TEXTMETRIC metric = ((FontMetrics)object).handle;
//	return handle.tmHeight == metric.tmHeight &&
//		handle.tmAscent == metric.tmAscent &&
//		handle.tmDescent == metric.tmDescent &&
//		handle.tmInternalLeading == metric.tmInternalLeading &&
//		handle.tmExternalLeading == metric.tmExternalLeading &&
//		handle.tmAveCharWidth == metric.tmAveCharWidth &&
//		handle.tmMaxCharWidth == metric.tmMaxCharWidth &&
//		handle.tmWeight == metric.tmWeight &&
//		handle.tmOverhang == metric.tmOverhang &&
//		handle.tmDigitizedAspectX == metric.tmDigitizedAspectX &&
//		handle.tmDigitizedAspectY == metric.tmDigitizedAspectY &&
////		handle.tmFirstChar == metric.tmFirstChar &&
////		handle.tmLastChar == metric.tmLastChar &&
////		handle.tmDefaultChar == metric.tmDefaultChar &&
////		handle.tmBreakChar == metric.tmBreakChar &&
//		handle.tmItalic == metric.tmItalic &&
//		handle.tmUnderlined == metric.tmUnderlined &&
//		handle.tmStruckOut == metric.tmStruckOut &&
//		handle.tmPitchAndFamily == metric.tmPitchAndFamily &&
//		handle.tmCharSet == metric.tmCharSet;
}

/**
 * Returns the ascent of the font described by the receiver. A
 * font's <em>ascent</em> is the distance from the baseline to the
 * top of actual characters, not including any of the leading area,
 * measured in pixels.
 *
 * @return the ascent of the font
 */
public int getAscent() {
  return handle == null? ascent: handle.getAscent();
}

/**
 * Returns the average character width, measured in pixels,
 * of the font described by the receiver.
 *
 * @return the average character width of the font
 */
public int getAverageCharWidth() {
  // Use a letter that looks average
  return handle == null? averageCharWidth: handle.charWidth('c');
}

/**
 * Returns the descent of the font described by the receiver. A
 * font's <em>descent</em> is the distance from the baseline to the
 * bottom of actual characters, not including any of the leading area,
 * measured in pixels.
 *
 * @return the descent of the font
 */
public int getDescent() {
	return handle == null? descent: handle.getDescent();
}

/**
 * Returns the height of the font described by the receiver,
 * measured in pixels. A font's <em>height</em> is the sum of
 * its ascent, descent and leading area.
 *
 * @return the height of the font
 *
 * @see #getAscent
 * @see #getDescent
 * @see #getLeading
 */
public int getHeight() {
	return handle == null? height: handle.getHeight();
}

/**
 * Returns the leading area of the font described by the
 * receiver. A font's <em>leading area</em> is the space
 * above its ascent which may include accents or other marks.
 *
 * @return the leading space of the font
 */
public int getLeading() {
	return handle == null? leading: handle.getLeading();
}

/**
 * Returns an integer hash code for the receiver. Any two
 * objects that return <code>true</code> when passed to
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
@Override
public int hashCode() {
  return (handle == null? 0: handle.hashCode()) ^ getAscent() ^ getDescent() ^ getHeight() ^ getLeading() ^ getAverageCharWidth();
}

/**
 * Invokes platform specific functionality to allocate a new font metrics.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>FontMetrics</code>. It is marked public only so that
 * it can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @param handle the <code>TEXTMETRIC</code> containing information about a font
 * @return a new font metrics object containing the specified <code>TEXTMETRIC</code>
 */
public static FontMetrics swing_new(java.awt.FontMetrics handle) {
	FontMetrics fontMetrics = new FontMetrics();
	fontMetrics.handle = handle;
	return fontMetrics;
}

static FontMetrics internal_new(int ascent, int descent, int averageCharWidth, int leading, int height) {
  FontMetrics fontMetrics = new FontMetrics();
  fontMetrics.ascent = ascent;
  fontMetrics.descent = descent;
  fontMetrics.height = height;
  fontMetrics.leading = leading;
  fontMetrics.averageCharWidth = averageCharWidth;
  return fontMetrics;
}

public int getAverageCharacterWidth() {
	throw new UnsupportedOperationException("Not implemented yet");
}

}
