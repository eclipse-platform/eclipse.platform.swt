/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
	int ascent, descent, leading, height;
	double averageCharWidth;

FontMetrics() {
}

public static FontMetrics cocoa_new (int ascent, int descent, int averageCharWidth, int leading, int height) {
	FontMetrics fontMetrics = new FontMetrics();
	fontMetrics.ascent = ascent;
	fontMetrics.descent = descent;
	fontMetrics.averageCharWidth = averageCharWidth;
	fontMetrics.leading = leading;
	fontMetrics.height = height;
	return fontMetrics;
}

/**
 * Invokes platform specific functionality to allocate a new FontMetrics.
 * <p>
 * <b>IMPORTANT:</b> This method is <em>not</em> part of the public
 * API for <code>FontMetrics</code>. It is marked public only so that it
 * can be shared within the packages provided by SWT. It is not
 * available on all platforms, and should never be called from
 * application code.
 * </p>
 *
 * @noreference This method is not intended to be referenced by clients.
 */
public static FontMetrics cocoa_new (int ascent, int descent, double averageCharWidth, int leading, int height) {
	FontMetrics fontMetrics = new FontMetrics();
	fontMetrics.ascent = ascent;
	fontMetrics.descent = descent;
	fontMetrics.averageCharWidth = averageCharWidth;
	fontMetrics.leading = leading;
	fontMetrics.height = height;
	return fontMetrics;
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
	FontMetrics metrics = (FontMetrics)object;
	return ascent == metrics.ascent && descent == metrics.descent && leading == metrics.leading &&
		height == metrics.height && (Double.compare (averageCharWidth, metrics.averageCharWidth) == 0);
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
	return ascent;
}

/**
 * Returns the average character width, measured in points,
 * of the font described by the receiver.
 *
 * @return the average character width of the font
 * @since 3.107
 */
public double getAverageCharacterWidth () {
	return averageCharWidth;
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
	return (int) averageCharWidth;
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
	return descent;
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
	return height;
}

/**
 * Returns the leading area of the font described by the
 * receiver. A font's <em>leading area</em> is the space
 * above its ascent which may include accents or other marks.
 *
 * @return the leading space of the font
 */
public int getLeading() {
	return leading;
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
	return ascent ^ descent ^ Double.hashCode (averageCharWidth) ^ leading ^ height;
}

String getName () {
	String string = getClass ().getName ();
	int index = string.lastIndexOf ('.');
	if (index == -1) return string;
	return string.substring (index + 1, string.length ());
}

@Override
public String toString() {
	return getName() +
		"{"
		+ " ascent=" + ascent
		+ " descent=" + descent
		+ " averageCharWidth=" + averageCharWidth
		+ " leading=" + leading
		+ " height=" + height
		+ "}";
}
}
