package org.eclipse.swt.graphics;

import org.eclipse.swt.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public final class FontData {
	/**
	 * The company that produced the font
	 * Warning: This field is platform dependent.
	 */
	public String foundry;
	/**
	 * The common name of the font
	 * Warning: This field is platform dependent.
	 */
	public String fontFamily;
	/**
	 * The weight ("normal", "bold")
	 * Warning: This field is platform dependent.
	 */
	public String weight;
	/**
	 * The slant ("o" for oblique, "i" for italic)
	 * Warning: This field is platform dependent.
	 */
	public String slant;
	/**
	 * The set width of the font
	 * Warning: This field is platform dependent.
	 */
	public String setWidth;
	/**
	 * Additional font styles
	 * Warning: This field is platform dependent.
	 */
	public String addStyle;
	/**
	 * The height of the font in pixels
	 * Warning: This field is platform dependent.
	 */
	public int pixels;
	/**
	 * The height of the font in tenths of a point
	 * Warning: This field is platform dependent.
	 */
	public int points;
	/**
	 * The horizontal screen resolution for which the font was designed
	 * Warning: This field is platform dependent.
	 */
	public int horizontalResolution;
	/**
	 * The vertical screen resolution for which the font was designed
	 * Warning: This field is platform dependent.
	 */
	public int verticalResolution;
	/**
	 * The font spacing ("m" for monospace, "p" for proportional)
	 * Warning: This field is platform dependent.
	 */
	public String spacing;
	/**
	 * The average character width for the font
	 * Warning: This field is platform dependent.
	 */
	public int averageWidth;
	/**
	 * The ISO character set registry
	 * Warning: This field is platform dependent.
	 */
	public String characterSetRegistry;
	/**
	 * The ISO character set name
	 * Warning: This field is platform dependent.
	 */
	public String characterSetName;
public FontData () {
}
public FontData(String string) {
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int start = 0;
	int end = string.indexOf('|');
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	String version1 = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	String name = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int height = 0;
	try {
		height = Integer.parseInt(string.substring(start, end));
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int style = 0;
	try {
		style = Integer.parseInt(string.substring(start, end));
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_NULL_ARGUMENT);
	}

	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) {
		setName(name);
		setHeight(height);
		setStyle(style);
		return;
	}
	String platform = string.substring(start, end);

	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) {
		setName(name);
		setHeight(height);
		setStyle(style);
		return;
	}
	String version2 = string.substring(start, end);

	if (platform.equals("MOTIF") && version2.equals("1")) {
		start = end + 1;
		end = string.length();
		if (end == -1) {
			setName(name);
			setHeight(height);
			setStyle(style);
			return;
		}
		String xlfd = string.substring(start, end);
		setXlfd(xlfd);
		return;
	}
	setName(name);
	setHeight(height);
	setStyle(style);
}
public FontData (String name, int height, int style) {
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	if (name != null) {
		int dash = name.indexOf('-');
		if (dash != -1) {
			foundry = name.substring(0, dash);
			fontFamily = name.substring(dash + 1);
		} else {
			fontFamily = name;
		}
	}
	points = height * 10;
	if ((style & SWT.BOLD) != 0)
		weight = "bold";
	else
		weight = "medium";
	if ((style & SWT.ITALIC) != 0)
		slant = "i";
	else
		slant = "r";
}
public boolean equals (Object object) {
	return (object == this) || ((object instanceof FontData) && 
		getXlfd().equals(((FontData)object).getXlfd()));
}
public int getHeight() {
	return points / 10;
}
public String getName() {
	StringBuffer buffer = new StringBuffer();
	if (foundry != null) {
		buffer.append(foundry);
		buffer.append("-");
	}
	if (fontFamily != null) buffer.append(fontFamily);
	return buffer.toString();
}
public int getStyle() {
	int style = 0;
	if (weight.equals("bold"))
		style |= SWT.BOLD;
	if (slant.equals("i"))
		style |= SWT.ITALIC;
	return style;
}
String getXlfd() {
	String s1, s2, s3, s4, s5, s6, s7, s8, s9, s10, s11, s12, s13, s14;
	s1 = s2 = s3 = s4 = s5 = s6 = s7 = s8 = s9 = s10 = s11 = s12 = s13 = s14 = "*";
	
	if (foundry != null) s1 = foundry;
	if (fontFamily != null) s2 = fontFamily;
	if (weight != null) s3 = weight;
	if (slant != null) s4 = slant;
	if (setWidth != null) s5 = setWidth;
	if (addStyle != null) s6 = addStyle;
	if (pixels != 0) s7 = Integer.toString(pixels);
	if (points != 0) s8 = Integer.toString(points);
	if (horizontalResolution != 0) s9 = Integer.toString(horizontalResolution);
	if (verticalResolution != 0) s10 = Integer.toString(verticalResolution);
	if (spacing != null) s11 = spacing;
	if (averageWidth != 0) s12 = Integer.toString(averageWidth);
	if (characterSetRegistry != null) s13 = characterSetRegistry;
	if (characterSetName != null) s14 = characterSetName;

	return "-" + s1+ "-" + s2 + "-" + s3 + "-" + s4 + "-" + s5 + "-" + s6 + "-" + s7 + "-" + s8 + "-" 
		+ s9 + "-" + s10 + "-" + s11 + "-" + s12 + "-" + s13 + "-" + s14;
}
public int hashCode () {
	return getXlfd().hashCode();
}
public static FontData motif_new(String xlfd) {
	FontData fontData = new FontData();
	fontData.setXlfd(xlfd);
	return fontData;
}
public void setHeight(int height) {
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	points = height * 10;
}
public void setName(String name) {
	if (name == null) {
		foundry = fontFamily = null;
		return;
	}
	int dash = name.indexOf('-');
	if (dash != -1) {
		foundry = name.substring(0, dash);
		fontFamily = name.substring(dash + 1);
	} else {
		fontFamily = name;
	}
}
public void setStyle(int style) {
	if ((style & SWT.BOLD) == SWT.BOLD)
		weight = "bold";
	else
		weight = "normal";
	if ((style & SWT.ITALIC) == SWT.ITALIC)
		slant = "i";
	else
		slant = "r";
}
void setXlfd(String xlfd) {
	int start, stop;
	start = 1;
	stop = xlfd.indexOf ("-", start);
	foundry = xlfd.substring(start, stop);
	if (foundry.equals("*")) foundry = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	fontFamily = xlfd.substring(start, stop);
	if (fontFamily.equals("*")) fontFamily = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
 	weight = xlfd.substring(start, stop);
 	if (weight.equals("*")) weight = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	slant = xlfd.substring(start, stop);
	if (slant.equals("*")) slant = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	setWidth = xlfd.substring(start, stop);
	if (setWidth.equals("*")) setWidth = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	addStyle = xlfd.substring(start, stop);
	if (addStyle.equals("*")) addStyle = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	String s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		pixels = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		points = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		horizontalResolution = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		verticalResolution = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	spacing  = xlfd.substring(start, stop);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
	s = xlfd.substring(start, stop);
	if (!s.equals("") && !s.equals("*"))
		averageWidth = Integer.parseInt(s);
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
 	characterSetRegistry = xlfd.substring(start, stop);
 	if (characterSetRegistry.equals("*")) characterSetRegistry = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
 	characterSetName = xlfd.substring(start);
 	if (characterSetName.equals("*")) characterSetName = null;
}
public String toString() {
	return "1|" + fontFamily + "|" + getHeight() + "|" + getStyle() + "|" +
		"MOTIF|1|" + getXlfd();
}
}
