package org.eclipse.swt.graphics;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public final class FontMetrics {
	int ascent, descent, averageCharWidth, leading, height;
FontMetrics() {
}
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof FontMetrics)) return false;
	FontMetrics metrics = (FontMetrics)object;
	return ascent == metrics.ascent && descent == metrics.descent &&
		averageCharWidth == metrics.averageCharWidth && leading == metrics.leading &&
		height == metrics.height;
}
public int getAscent() {
	return ascent;
}
public int getAverageCharWidth() {
	return averageCharWidth;
}
public int getDescent() {
	return descent;
}
public int getHeight() {
	return height;
}
public int getLeading() {
	return leading;
}
public int hashCode() {
	return ascent ^ descent ^ averageCharWidth ^ leading ^ height;
}
public static FontMetrics motif_new(int ascent, int descent, int averageCharWidth, int leading, int height) {
	FontMetrics fontMetrics = new FontMetrics();
	fontMetrics.ascent = ascent;
	fontMetrics.descent = descent;
	fontMetrics.averageCharWidth = averageCharWidth;
	fontMetrics.leading = leading;
	fontMetrics.height = height;
	return fontMetrics;
}
}
