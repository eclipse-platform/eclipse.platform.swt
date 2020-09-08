/*******************************************************************************
 * Copyright (c) 2000, 2018 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

/**
 * Instances of this class provide measurement information
 * about fonts including ascent, descent, height, leading
 * space between rows, and average character width.
 * <code>FontMetrics</code> are obtained from <code>GC</code>s
 * using the <code>getFontMetrics()</code> method.
 *
 * @see GC#getFontMetrics
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class FontMetrics {

	/**
	 * On Windows, handle is a Win32 TEXTMETRIC struct
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 *
	 * @noreference This field is not intended to be referenced by clients.
	 */
	public TEXTMETRIC handle;

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
	TEXTMETRIC metric = ((FontMetrics)object).handle;
	return handle.tmHeight == metric.tmHeight &&
		handle.tmAscent == metric.tmAscent &&
		handle.tmDescent == metric.tmDescent &&
		handle.tmInternalLeading == metric.tmInternalLeading &&
		handle.tmExternalLeading == metric.tmExternalLeading &&
		handle.tmAveCharWidth == metric.tmAveCharWidth &&
		handle.tmMaxCharWidth == metric.tmMaxCharWidth &&
		handle.tmWeight == metric.tmWeight &&
		handle.tmOverhang == metric.tmOverhang &&
		handle.tmDigitizedAspectX == metric.tmDigitizedAspectX &&
		handle.tmDigitizedAspectY == metric.tmDigitizedAspectY &&
//		handle.tmFirstChar == metric.tmFirstChar &&
//		handle.tmLastChar == metric.tmLastChar &&
//		handle.tmDefaultChar == metric.tmDefaultChar &&
//		handle.tmBreakChar == metric.tmBreakChar &&
		handle.tmItalic == metric.tmItalic &&
		handle.tmUnderlined == metric.tmUnderlined &&
		handle.tmStruckOut == metric.tmStruckOut &&
		handle.tmPitchAndFamily == metric.tmPitchAndFamily &&
		handle.tmCharSet == metric.tmCharSet;
}

/**
 * Returns the ascent of the font described by the receiver. A
 * font's <em>ascent</em> is the distance from the baseline to the
 * top of actual characters, not including any of the leading area,
 * measured in points.
 *
 * @return the ascent of the font
 */
public int getAscent() {
	return DPIUtil.autoScaleDown(handle.tmAscent - handle.tmInternalLeading);
}

/**
 * Returns the average character width, measured in points,
 * of the font described by the receiver.
 *
 * @return the average character width of the font
 * @since 3.107
 */
public double getAverageCharacterWidth() {
	return getAverageCharWidth();
}

/**
 * Returns the average character width, measured in points,
 * of the font described by the receiver.
 *
 * @return the average character width of the font
 * @deprecated Use getAverageCharacterWidth() instead
 */
@Deprecated
public int getAverageCharWidth() {
	return DPIUtil.autoScaleDown(handle.tmAveCharWidth);
}

/**
 * Returns the descent of the font described by the receiver. A
 * font's <em>descent</em> is the distance from the baseline to the
 * bottom of actual characters, not including any of the leading area,
 * measured in points.
 *
 * @return the descent of the font
 */
public int getDescent() {
	return DPIUtil.autoScaleDown(handle.tmDescent);
}

/**
 * Returns the height of the font described by the receiver,
 * measured in points. A font's <em>height</em> is the sum of
 * its ascent, descent and leading area.
 *
 * @return the height of the font
 *
 * @see #getAscent
 * @see #getDescent
 * @see #getLeading
 */
public int getHeight() {
	return DPIUtil.autoScaleDown(handle.tmHeight);
}

/**
 * Returns the leading area of the font described by the
 * receiver. A font's <em>leading area</em> is the space
 * above its ascent which may include accents or other marks.
 *
 * @return the leading space of the font
 */
public int getLeading() {
	/*
	 * HiHPI rounding problem (bug 490743 comment 17):
	 *
	 * API clients expect this invariant:
	 *    getHeight() == getLeading() + getAscent() + getDescent()
	 *
	 * Separate rounding of each RHS term can break the invariant.
	 *
	 * An additional problem is that ascent and descent are more important to
	 * be as close as possible to the real value. Any necessary rounding
	 * adjustment should go into leading, that's why compute this as a derived
	 * value here:
	 */
	return getHeight() - getAscent() - getDescent();
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
	return handle.tmHeight ^ handle.tmAscent ^ handle.tmDescent ^
		handle.tmInternalLeading ^ handle.tmExternalLeading ^
		handle.tmAveCharWidth ^ handle.tmMaxCharWidth ^ handle.tmWeight ^
		handle.tmOverhang ^ handle.tmDigitizedAspectX ^ handle.tmDigitizedAspectY ^
//		handle.tmFirstChar ^ handle.tmLastChar ^ handle.tmDefaultChar ^ handle.tmBreakChar ^
		handle.tmItalic ^ handle.tmUnderlined ^ handle.tmStruckOut ^
		handle.tmPitchAndFamily ^ handle.tmCharSet;
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
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static FontMetrics win32_new(TEXTMETRIC handle) {
	FontMetrics fontMetrics = new FontMetrics();
	fontMetrics.handle = handle;
	return fontMetrics;
}

}
