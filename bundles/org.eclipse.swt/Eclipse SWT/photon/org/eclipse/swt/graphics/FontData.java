package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;

public final class FontData {

	/**
	 * the font name
	 * (Warning: This field is platform dependent)
	 */
	String name;

	/**
	 * the font height
	 * (Warning: This field is platform dependent)
	 */
	int    height;

	/**
	 * the font style
	 * (Warning: This field is platform dependent)
	 */
	int    style;
	
	/**
	 * A Photon stem
	 * (Warning: This field is platform dependent)
	 */
	public byte[] stem;

FontData(byte[] stem) {
	FontQueryInfo info = new FontQueryInfo();
	if (OS.PfQueryFontInfo(stem, info) == 0) {
		this.stem = info.font;
		name = new String(Converter.mbcsToWcs(null, info.desc)).trim();
		if ((info.style & OS.PHFONT_INFO_PLAIN) != 0) style = SWT.NORMAL;
		else if ((info.style & OS.PHFONT_INFO_BOLD) != 0) style = SWT.BOLD;
		else if ((info.style & OS.PHFONT_INFO_ITALIC) != 0) style = SWT.ITALIC;
		else if ((info.style & OS.PHFONT_INFO_BLDITC) != 0) style = SWT.BOLD | SWT.ITALIC;
		else style = SWT.NORMAL;
		/*
		* For some reason, sometimes PfQueryFontInfo does not
		* set the size of the font.  In that case, the size is
		* parsed from the stem.
		*/
		if (info.size != 0) {
			height = info.size;
		} else {
			String fontName = new String(Converter.mbcsToWcs(null, this.stem)).trim();
			int end = fontName.length();
			for (int i = end - 1; i >= 0; i--) {
				if (Character.isDigit(fontName.charAt(i))) break;
				end--;
			}
			int start = end;
			for (int i = end - 1; i >= 0; i--) {
				if (!Character.isDigit(fontName.charAt(i))) break;
				start--;
			}
			try {
				height = Integer.parseInt(fontName.substring(start, end));
			} catch (NumberFormatException e) {}
		}
	} else {
		this.stem = stem;
	}
}

public FontData() {
	this("", 12, SWT.NORMAL);
}

public FontData(String string) {	
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int start = 0;
	int end = string.indexOf('|');
	if (end == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	String version1 = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	String name = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int height = 0;
	try {
		height = Integer.parseInt(string.substring(start, end));
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int style = 0;
	try {
		style = Integer.parseInt(string.substring(start, end));
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	start = end + 1;
	end = string.indexOf('|', start);
	setName(name);
	setHeight(height);
	setStyle(style);
	if (end == -1) return;
	String platform = string.substring(start, end);

	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) return;
	String version2 = string.substring(start, end);

	if (platform.equals("PHOTON") && version2.equals("1")) {
		return;
	}
}

public FontData(String name, int height, int style) {
	setName(name);
	setHeight(height);
	setStyle(style);
}

public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof FontData)) return false;
	FontData data = (FontData)object;
	return name == name && height == data.height && style == data.style;
}

public int getHeight() {
	return height;
}

public String getName() {
	return name;
}

public int getStyle() {
	return style;
}

public int hashCode () {
	return name.hashCode() ^ height ^ style;
}

public void setHeight(int height) {
	this.height = height;
}

public void setName(String name) {
	this.name = name;
}

public void setStyle(int style) {
	this.style = style;
}

public String toString() {
	StringBuffer buffer = new StringBuffer();
	buffer.append("1|");
	buffer.append(getName());
	buffer.append("|");
	buffer.append(getHeight());
	buffer.append("|");
	buffer.append(getStyle());
	buffer.append("|");
	buffer.append("PHOTON|1|");	
	return buffer.toString();
}

public static FontData photon_new(byte[] stem) {
	return new FontData(stem);
}

}
