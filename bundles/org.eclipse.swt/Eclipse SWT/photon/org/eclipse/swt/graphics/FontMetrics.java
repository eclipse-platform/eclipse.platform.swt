package org.eclipse.swt.graphics;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.photon.*;

public final class FontMetrics {

	/**
	 * A Photon FontQueryInfo struct
	 * (Warning: This field is platform dependent)
	 */
	FontQueryInfo handle;
	
FontMetrics() {
}

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
		for (int i = handle.font.length - 1; i >= 0; i--) {
			if (handle.font[i] != info.font[i]) return false;
		}
		return true;
	}
	return false;
}

public int getAscent() {
	return -handle.ascender;
}

public int getAverageCharWidth() {
	return handle.width / 2;
}

public int getDescent() {
	return handle.descender;
}

public int getHeight() {
	return -handle.ascender + handle.descender;
}

public int getLeading() {
	return 0;
}

public int hashCode() {
	if (handle == null) return 0;
	return handle.size ^ handle.style ^ handle.ascender ^
		handle.descender ^ handle.width ^
		handle.lochar ^ handle.hichar ^ handle.font.hashCode() ^
		handle.desc.hashCode();
}

public static FontMetrics photon_new(FontQueryInfo handle) {
	FontMetrics fontMetrics = new FontMetrics();
	fontMetrics.handle = handle;
	return fontMetrics;
}
}