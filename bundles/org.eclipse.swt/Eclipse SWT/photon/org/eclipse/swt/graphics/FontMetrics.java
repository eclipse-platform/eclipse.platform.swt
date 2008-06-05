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
package org.eclipse.swt.graphics;


import org.eclipse.swt.internal.photon.*;

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
	 * On Photon, handle is a Photon FontQueryInfo struct
	 * (Warning: This field is platform dependent)
	 * <p>
	 * <b>IMPORTANT:</b> This field is <em>not</em> part of the SWT
	 * public API. It is marked public only so that it can be shared
	 * within the packages provided by SWT. It is not available on all
	 * platforms and should never be accessed from application code.
	 * </p>
	 */
	public FontQueryInfo handle;
	
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
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof FontMetrics)) return false;
	FontQueryInfo info = ((FontMetrics)object).handle;
	if (info == handle) return true;
	if (info == null || handle == null) return false;
	if (handle.size == info.size && 
		handle.style == info.style &&
		handle.ascender == info.ascender &&
		handle.descender == info.descender &&
		handle.width == info.width &&
		handle.lochar == info.lochar &&
		handle.hichar == info.hichar &&
		handle.desc.length == info.desc.length &&
		handle.font.length == info.font.length)
	{
		for (int i = handle.desc.length - 1; i >= 0; i--) {
			if (handle.desc[i] != info.desc[i])	return false;
		}
		for (int i = handle.font.length - 1; i >= 0; i--) {
			if (handle.font[i] != info.font[i]) return false;
		}
		return true;
	}
	return false;
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
	return -handle.ascender;
}

/**
 * Returns the average character width, measured in pixels,
 * of the font described by the receiver.
 *
 * @return the average character width of the font
 */
public int getAverageCharWidth() {
	if ((handle.style & OS.PHFONT_INFO_FIXED) != 0) return handle.width;
	return handle.width / 3;
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
	return handle.descender;
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
	return -handle.ascender + handle.descender;
}

/**
 * Returns the leading area of the font described by the
 * receiver. A font's <em>leading area</em> is the space
 * above its ascent which may include accents or other marks.
 *
 * @return the leading space of the font
 */
public int getLeading() {
	return 0;
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
public int hashCode() {
	if (handle == null) return 0;
	return handle.size ^ handle.style ^ handle.ascender ^
		handle.descender ^ handle.width ^
		handle.lochar ^ handle.hichar ^ handle.font.hashCode() ^
		handle.desc.hashCode();
}

public static FontMetrics internal_new(int ascent, int descent, int aveCharWidth, int leading, int height) {
	FontQueryInfo info = new FontQueryInfo();
	info.ascender = (short)-ascent;
	info.descender = (short)descent;
	info.style = OS.PHFONT_INFO_FIXED;
	info.width = (short)aveCharWidth;
	return photon_new(info);
}

public static FontMetrics photon_new(FontQueryInfo handle) {
	FontMetrics fontMetrics = new FontMetrics();
	fontMetrics.handle = handle;
	return fontMetrics;
}
}
