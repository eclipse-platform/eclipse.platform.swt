/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others.
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
 * Instances of this class describe operating system fonts.
 * <p>
 * For platform-independent behaviour, use the get and set methods
 * corresponding to the following properties:
 * <dl>
 * <dt>height</dt><dd>the height of the font in points</dd>
 * <dt>name</dt><dd>the face name of the font, which may include the foundry</dd>
 * <dt>style</dt><dd>A bitwise combination of NORMAL, ITALIC and BOLD</dd>
 * </dl>
 * If extra, platform-dependent functionality is required:
 * <ul>
 * <li>On <em>Windows</em>, the data member of the <code>FontData</code>
 * corresponds to a Windows <code>LOGFONT</code> structure whose fields
 * may be retrieved and modified.</li>
 * <li>On <em>X</em>, the fields of the <code>FontData</code> correspond
 * to the entries in the font's XLFD name and may be retrieved and modified.
 * </ul>
 * Application code does <em>not</em> need to explicitly release the
 * resources managed by each instance when those instances are no longer
 * required, and thus no <code>dispose()</code> method is provided.
 *
 * @see Font
 * @see <a href="http://www.eclipse.org/swt/">Sample code and further information</a>
 */
public final class FontData {
	/**
	 * The company that produced the font
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
	public String foundry;
	/**
	 * The common name of the font
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
	public String fontFamily;
	/**
	 * The weight ("medium", "bold")
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
	public String weight;
	/**
	 * The slant ("o" for oblique, "i" for italic)
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
	public String slant;
	/**
	 * The set width of the font
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
	public String setWidth;
	/**
	 * Additional font styles
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
	public String addStyle;
	/**
	 * The height of the font in pixels
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
	public int pixels;
	/**
	 * The height of the font in tenths of a point
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
	public int points;
	/**
	 * The horizontal screen resolution for which the font was designed
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
	public int horizontalResolution;
	/**
	 * The vertical screen resolution for which the font was designed
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
	public int verticalResolution;
	/**
	 * The font spacing ("m" for monospace, "p" for proportional)
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
	public String spacing;
	/**
	 * The average character width for the font
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
	public int averageWidth;
	/**
	 * The ISO character set registry
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
	public String characterSetRegistry;
	/**
	 * The ISO character set name
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
	public String characterSetName;

	/**
	 * The locales of the font
	 */
	String lang, country, variant;
/**	 
 * Constructs a new uninitialized font data.
 */
public FontData () {
}
/**
 * Constructs a new FontData given a string representation
 * in the form generated by the <code>FontData.toString</code>
 * method.
 * <p>
 * Note that the representation varies between platforms,
 * and a FontData can only be created from a string that was 
 * generated on the same platform.
 * </p>
 *
 * @param string the string representation of a <code>FontData</code> (must not be null)
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the argument does not represent a valid description</li>
 * </ul>
 *
 * @see #toString
 */
public FontData(String string) {
	if (string == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int start = 0;
	int end = string.indexOf('|');
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	String version1 = string.substring(start, end);
	try {
		if (Integer.parseInt(version1) != 1) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	} catch (NumberFormatException e) {
		SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	}

	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	String name = string.substring(start, end);
	
	start = end + 1;
	end = string.indexOf('|', start);
	if (end == -1) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	float height = 0;
	try {
		height = Float.parseFloat(string.substring(start, end));
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
/**	 
 * Constructs a new font data given a font name,
 * the height of the desired font in points, 
 * and a font style.
 *
 * @param name the name of the font (must not be null)
 * @param height the font height in points
 * @param style a bit or combination of NORMAL, BOLD, ITALIC
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - when the font name is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 */
public FontData (String name, int height, int style) {
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int dash = name.indexOf('-');
	if (dash != -1) {
		foundry = name.substring(0, dash);
		fontFamily = name.substring(dash + 1);
	} else {
		fontFamily = name;
	}
	points = height * 10;
	weight = (style & SWT.BOLD) != 0 ? "bold" : "medium"; 
	slant = (style & SWT.ITALIC) != 0 ? "i" : "r"; 
}
/*public*/ FontData (String name, float height, int style) {
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	int dash = name.indexOf('-');
	if (dash != -1) {
		foundry = name.substring(0, dash);
		fontFamily = name.substring(dash + 1);
	} else {
		fontFamily = name;
	}
	points = (int)(height * 10);
	weight = (style & SWT.BOLD) != 0 ? "bold" : "medium"; 
	slant = (style & SWT.ITALIC) != 0 ? "i" : "r"; 
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
	return (object == this) || ((object instanceof FontData) && 
		getXlfd().equals(((FontData)object).getXlfd()));
}
/**
 * Returns the height of the receiver in points.
 *
 * @return the height of this FontData
 *
 * @see #setHeight(int)
 */
public int getHeight() {
	return points / 10;
}
/*public*/ float getHeightF() {
	return points / 10f;
}
/**
 * Returns the locale of the receiver.
 * <p>
 * The locale determines which platform character set this
 * font is going to use. Widgets and graphics operations that
 * use this font will convert UNICODE strings to the platform
 * character set of the specified locale.
 * </p>
 * <p>
 * On platforms where there are multiple character sets for a
 * given language/country locale, the variant portion of the
 * locale will determine the character set.
 * </p>
 * 
 * @return the <code>String</code> representing a Locale object
 * @since 3.0
 */
public String getLocale () {
	StringBuffer buffer = new StringBuffer ();
	char sep = '_';
	if (lang != null) {
		buffer.append (lang);
		buffer.append (sep);
	}
	if (country != null) {
		buffer.append (country);
		buffer.append (sep);
	}
	if (variant != null) {
		buffer.append (variant);
	}
	
	String result = buffer.toString ();
	int length = result.length ();
	if (length > 0) {
		if (result.charAt (length - 1) == sep) {
			result = result.substring (0, length - 1);
		}
	} 
	return result;
}
/**
 * Returns the name of the receiver.
 * On platforms that support font foundries, the return value will
 * be the foundry followed by a dash ("-") followed by the face name.
 *
 * @return the name of this <code>FontData</code>
 *
 * @see #setName
 */
public String getName() {
	StringBuffer buffer = new StringBuffer();
	if (foundry != null) {
		buffer.append(foundry);
		buffer.append("-");
	}
	if (fontFamily != null) buffer.append(fontFamily);
	return buffer.toString();
}
/**
 * Returns the style of the receiver which is a bitwise OR of 
 * one or more of the <code>SWT</code> constants NORMAL, BOLD
 * and ITALIC.
 *
 * @return the style of this <code>FontData</code>
 * 
 * @see #setStyle
 */
public int getStyle() {
	int style = 0;
	if (weight != null && weight.equals("bold")) style |= SWT.BOLD;
	if (slant != null && slant.equals("i")) style |= SWT.ITALIC;
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
public int hashCode () {
	return getXlfd().hashCode();
}
public static FontData motif_new(String xlfd) {
	FontData fontData = new FontData();
	fontData.setXlfd(xlfd);
	return fontData;
}
/**
 * Sets the height of the receiver. The parameter is
 * specified in terms of points, where a point is one
 * seventy-second of an inch.
 *
 * @param height the height of the <code>FontData</code>
 *
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 * 
 * @see #getHeight
 */
public void setHeight(int height) {
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	points = height * 10;
}
/*public*/ void setHeight(float height) {
	if (height < 0) SWT.error(SWT.ERROR_INVALID_ARGUMENT);
	points = (int)(height * 10);
}
/**
 * Sets the name of the receiver.
 * <p>
 * Some platforms support font foundries. On these platforms, the name
 * of the font specified in setName() may have one of the following forms:
 * <ol>
 * <li>a face name (for example, "courier")</li>
 * <li>a foundry followed by a dash ("-") followed by a face name (for example, "adobe-courier")</li>
 * </ol>
 * In either case, the name returned from getName() will include the
 * foundry.
 * </p>
 * <p>
 * On platforms that do not support font foundries, only the face name
 * (for example, "courier") is used in <code>setName()</code> and 
 * <code>getName()</code>.
 * </p>
 *
 * @param name the name of the font data (must not be null)
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - when the font name is null</li>
 * </ul>
 *
 * @see #getName
 */
public void setName(String name) {
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	int dash = name.indexOf('-');
	if (dash != -1) {
		foundry = name.substring(0, dash);
		fontFamily = name.substring(dash + 1);
	} else {
		fontFamily = name;
	}
}
/**
 * Sets the locale of the receiver.
 * <p>
 * The locale determines which platform character set this
 * font is going to use. Widgets and graphics operations that
 * use this font will convert UNICODE strings to the platform
 * character set of the specified locale.
 * </p>
 * <p>
 * On platforms where there are multiple character sets for a
 * given language/country locale, the variant portion of the
 * locale will determine the character set.
 * </p>
 * 
 * @param locale the <code>String</code> representing a Locale object
 * @see java.util.Locale#toString
 */
public void setLocale(String locale) {
	lang = country = variant = null;
	if (locale != null) {
		char sep = '_';
		int length = locale.length();
		int firstSep, secondSep;
		
		firstSep = locale.indexOf(sep);
		if (firstSep == -1) {
			firstSep = secondSep = length;
		} else {
			secondSep = locale.indexOf(sep, firstSep + 1);
			if (secondSep == -1) secondSep = length;
		}
		if (firstSep > 0) lang = locale.substring(0, firstSep);
		if (secondSep > firstSep + 1) country = locale.substring(firstSep + 1, secondSep);
		if (length > secondSep + 1) variant = locale.substring(secondSep + 1);
	}	
}
/**
 * Sets the style of the receiver to the argument which must
 * be a bitwise OR of one or more of the <code>SWT</code> 
 * constants NORMAL, BOLD and ITALIC.  All other style bits are
 * ignored.
 *
 * @param style the new style for this <code>FontData</code>
 *
 * @see #getStyle
 */
public void setStyle(int style) {
	weight = (style & SWT.BOLD) != 0 ? "bold" : "medium"; 
	slant = (style & SWT.ITALIC) != 0 ? "i" : "r"; 
	averageWidth = 0;
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
	if (!s.equals("") && !s.equals("*")) {
		if (s.startsWith ("~")) {
			s = "-" + s.substring(1);
		}
		averageWidth = Integer.parseInt(s);
	}
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
 	characterSetRegistry = xlfd.substring(start, stop);
 	if (characterSetRegistry.equals("*")) characterSetRegistry = null;
	start = stop + 1;
	stop = xlfd.indexOf ("-", start);
 	characterSetName = xlfd.substring(start);
 	if (characterSetName.equals("*")) characterSetName = null;
}
/**
 * Returns a string representation of the receiver which is suitable
 * for constructing an equivalent instance using the 
 * <code>FontData(String)</code> constructor.
 *
 * @return a string representation of the FontData
 *
 * @see FontData
 */
public String toString() {
	return "1|" + fontFamily + "|" + getHeightF() + "|" + getStyle() + "|" +
		"MOTIF|1|" + getXlfd();
}
}
