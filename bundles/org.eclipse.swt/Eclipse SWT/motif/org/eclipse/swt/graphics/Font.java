package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/**
 * Instances of this class manage operating system resources that
 * define how text looks when it is displayed. Fonts may be constructed
 * by providing a device and either name, size and style information
 * or a <code>FontData</code> object which encapsulates this data.
 * <p>
 * Application code must explicitly invoke the <code>Font.dispose()</code> 
 * method to release the operating system resources managed by each instance
 * when those instances are no longer required.
 * </p>
 *
 * @see FontData
 */
public final class Font {
	/**
	 * the handle to the OS font resource
	 * (Warning: This field is platform dependent)
	 */
	public int handle;

	/**
	 * the code page of the font
	 * (Warning: This field is platform dependent)
	 * 
	 * @since 2.0
	 */
	public String codePage;

	/**
	 * The device where this image was created.
	 */
	Device device;

Font () {
}
/**	 
 * Constructs a new font given a device and font data
 * which describes the desired font's appearance.
 * <p>
 * You must dispose the font when it is no longer required. 
 * </p>
 *
 * @param device the device to create the font on
 * @param fd the FontData that describes the desired font (must not be null)
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the fd argument is null</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a font could not be created from the given font data</li>
 * </ul>
 */
public Font (Device device, FontData fd) {
	if (fd == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, fd);
}
/**	 
 * Constructs a new font given a device, a font name,
 * the height of the desired font in points, and a font
 * style.
 * <p>
 * You must dispose the font when it is no longer required. 
 * </p>
 *
 * @param device the device to create the font on
 * @param name the name of the font (must not be null)
 * @param height the font height in points
 * @param style a bit or combination of NORMAL, BOLD, ITALIC
 * 
 * @exception IllegalArgumentException <ul>
 *    <li>ERROR_NULL_ARGUMENT - if the name argument is null</li>
 *    <li>ERROR_INVALID_ARGUMENT - if the height is negative</li>
 * </ul>
 * @exception SWTError <ul>
 *    <li>ERROR_NO_HANDLES - if a font could not be created from the given arguments</li>
 * </ul>
 */
public Font (Device device, String name, int height, int style) {
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new FontData(name, height, style));
}
/**
 * Disposes of the operating system resources associated with
 * the font. Applications must dispose of all fonts which
 * they allocate.
 */
public void dispose () {
	if (handle == 0) return;
	if (device.isDisposed()) return;
	if (handle == device.systemFont.handle) return;
	
	/* Free the fonts associated with the font list */
	int [] buffer = new int [1];
	int xDisplay = device.xDisplay;
	if (OS.XmFontListInitFontContext (buffer, handle)) {
		int context = buffer [0];
		int fontListEntry;
		while ((fontListEntry = OS.XmFontListNextEntry (context)) != 0) {
			int fontPtr = OS.XmFontListEntryGetFont (fontListEntry, buffer);
			if (buffer [0] == OS.XmFONT_IS_FONT) {
				OS.XFreeFont(xDisplay, fontPtr);
			} else {
				OS.XFreeFontSet(xDisplay, fontPtr);
			}
		}
		OS.XmFontListFreeFontContext (context);
	}	
	
	/* Free the font list */
	OS.XmFontListFree (handle);
	device = null;
	handle = 0;
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
	if (!(object instanceof Font)) return false;
	Font font = (Font)object;
	return device == font.device && handle == font.handle;
}
/**
 * Returns the code page for the specified font list.
 *
 * @return the code page for the font list
 */	
static String getCodePage (int xDisplay, int fontList) {
	int[] buffer = new int[1];
	if (!OS.XmFontListInitFontContext(buffer, fontList)) return null;
	int context = buffer[0];
	XFontStruct fontStruct = new XFontStruct();
	int fontListEntry;
	int[] fontStructPtr = new int[1];
	int[] fontNamePtr = new int[1];
	String codePage = null;
	/* Go through each entry in the font list */
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, buffer);
		if (buffer[0] == OS.XmFONT_IS_FONT) { 
			/* FontList contains a single font */
			OS.memmove(fontStruct,fontPtr,20 * 4);
			int propPtr = fontStruct.properties;
			for (int i = 0; i < fontStruct.n_properties; i++) {
				/* Reef through properties looking for XAFONT */
				int[] prop = new int[2];
				OS.memmove(prop, propPtr, 8);
				if (prop[0] == OS.XA_FONT) {
					/* Found it, prop[1] points to the string */
					int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
					int length = OS.strlen(ptr);
					byte[] nameBuf = new byte[length];
					OS.memmove(nameBuf, ptr, length);
					/* Use the character encoding for the default locale */
					String xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
					int start = xlfd.lastIndexOf ('-');
					if (start != -1 && start > 0) {
						start = xlfd.lastIndexOf ('-', start - 1);
						if (start != -1) {
							codePage = xlfd.substring (start + 1, xlfd.length ());
							if (codePage.indexOf ("iso") == 0) {
								if (OS.IsLinux) {
									codePage = "ISO-" + codePage.substring (3, codePage.length ());
								}
							}
						}
					}
					OS.XtFree(ptr);
					break;
				}
				propPtr += 8;
			}
		}
		else { 
			/* FontList contains a fontSet */
			
			/* Get the font set locale */
			int localePtr = OS.XLocaleOfFontSet(fontPtr);
			int length = OS.strlen (localePtr);
			byte [] locale = new byte [length + 1];
			OS.memmove (locale, localePtr, length);
			
			/* Get code page for the font set locale */
			OS.setlocale (OS.LC_CTYPE,  locale);
			int codesetPtr = OS.nl_langinfo (OS.CODESET);
			length = OS.strlen (codesetPtr);
			byte [] codeset = new byte [length];
			OS.memmove (codeset, codesetPtr, length);
			codePage = new String (codeset);
			
			/* Reset the locale */
			OS.setlocale (OS.LC_CTYPE, new byte[1]);
		}
	}
	OS.XmFontListFreeFontContext(context);
	return codePage;
}
/**
 * Returns an array of <code>FontData</code>s representing the receiver.
 * On Windows, only one FontData will be returned per font. On X however, 
 * a <code>Font</code> object <em>may</em> be composed of multiple X 
 * fonts. To support this case, we return an array of font data objects.
 *
 * @return an array of font data objects describing the receiver
 *
 * @exception SWTException <ul>
 *    <li>ERROR_GRAPHIC_DISPOSED - if the receiver has been disposed</li>
 * </ul>
 */
public FontData[] getFontData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	int xDisplay = device.xDisplay;
	/**
	 * Create a font context to iterate over each element in the font list.
	 * If a font context can not be created, return null.
	 */
	int[] buffer = new int[1];
	if (!OS.XmFontListInitFontContext(buffer, handle)) return null;
	int context = buffer[0];
	XFontStruct fontStruct = new XFontStruct();
	int fontListEntry;
	int[] fontStructPtr = new int[1];
	int[] fontNamePtr = new int[1];
	String[] xlfds = new String[0];
	/* Go through each entry in the font list */
	while ((fontListEntry = OS.XmFontListNextEntry(context)) != 0) {
		int fontPtr = OS.XmFontListEntryGetFont(fontListEntry, buffer);
		if (buffer[0] == OS.XmFONT_IS_FONT) { 
			/* FontList contains a single font */
			OS.memmove(fontStruct,fontPtr,20 * 4);
			int propPtr = fontStruct.properties;
			for (int i = 0; i < fontStruct.n_properties; i++) {
				/* Reef through properties looking for XAFONT */
				int[] prop = new int[2];
				OS.memmove(prop, propPtr, 8);
				if (prop[0] == OS.XA_FONT) {
					/* Found it, prop[1] points to the string */
					int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
					int length = OS.strlen(ptr);
					byte[] nameBuf = new byte[length];
					OS.memmove(nameBuf, ptr, length);
					/* Use the character encoding for the default locale */
					String xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
					/* Add the xlfd to the array */
					String[] newXlfds = new String[xlfds.length + 1];
					System.arraycopy(xlfds, 0, newXlfds, 0, xlfds.length);
					newXlfds[newXlfds.length - 1] = xlfd;
					xlfds = newXlfds;
					OS.XtFree(ptr);
					break;
				}
				propPtr += 8;
			}
		}
		else { 
			/* FontList contains a fontSet */
			int nFonts = OS.XFontsOfFontSet(fontPtr,fontStructPtr,fontNamePtr);
			int [] fontStructs = new int[nFonts];
			OS.memmove(fontStructs,fontStructPtr[0],nFonts * 4);
			for (int i = 0; i < nFonts; i++) { // Go through each fontStruct in the font set.
				OS.memmove(fontStruct,fontStructs[i],20 * 4);
				int propPtr = fontStruct.properties;
				for (int j = 0; j < fontStruct.n_properties; j++) {
					// Reef through properties looking for XAFONT
					int[] prop = new int[2];
					OS.memmove(prop, propPtr, 8);
					if (prop[0] == OS.XA_FONT) {
						/* Found it, prop[1] points to the string */
						int ptr = OS.XmGetAtomName(xDisplay, prop[1]);
						int length = OS.strlen(ptr);
						byte[] nameBuf = new byte[length];
						OS.memmove(nameBuf, ptr, length);
						String xlfd = new String(Converter.mbcsToWcs(null, nameBuf)).toLowerCase();
						/* Add the xlfd to the array */
						String[] newXlfds = new String[xlfds.length + 1];
						System.arraycopy(xlfds, 0, newXlfds, 0, xlfds.length);
						newXlfds[newXlfds.length - 1] = xlfd;
						xlfds = newXlfds;
						OS.XFree(ptr);
						break;
					}
					propPtr += 8;
				}
			}
		}
	}
	OS.XmFontListFreeFontContext(context);
	if (xlfds.length == 0) return null;
	FontData[] fontData = new FontData[xlfds.length];
	/* Construct each fontData out of the xlfd */
	try {
		for (int i = 0; i < xlfds.length; i++) {
			fontData[i] = FontData.motif_new(xlfds[i]);
		}
	} catch (Exception e) {
		/**
		 * Some font servers, for example, xfstt, do not pass
		 * reasonable font properties to the client, so we
		 * cannot construct a FontData for these. Return null.
		 */
		return null;
	}
	return fontData;
}
/**
 * Returns an integer hash code for the receiver. Any two 
 * objects which return <code>true</code> when passed to 
 * <code>equals</code> must return the same value for this
 * method.
 *
 * @return the receiver's hash
 *
 * @see #equals
 */
public int hashCode () {
	return handle;
}
int loadFont(int xDisplay, FontData fd) {
	/* Use the character encoding for the default locale */
	byte[] buffer = Converter.wcsToMbcs(null, fd.getXlfd(), true);
	return OS.XLoadQueryFont(xDisplay, buffer);
}
int loadFontSet(int xDisplay, FontData fd) {
	/* Use the character encoding for the default locale */
	byte[] buffer = Converter.wcsToMbcs(null, fd.getXlfd(), true);
	int [] missing_charset = new int [1];
	int [] missing_charset_count = new int [1];
	int [] def_string = new int [1];
	return OS.XCreateFontSet(xDisplay, buffer, missing_charset, missing_charset_count, def_string);
}
int matchFont(int xDisplay, FontData fd, boolean fontSet) {	
	int font = fontSet ? loadFontSet(xDisplay, fd) : loadFont(xDisplay, fd);
	if (font != 0) return font;
	if (fd.slant != null) {
		fd.slant = null;
		font = fontSet ? loadFontSet(xDisplay, fd) : loadFont(xDisplay, fd);
		if (font != 0) return font;
	}
	if (fd.weight != null) {
		fd.weight = null;
		font = fontSet ? loadFontSet(xDisplay, fd) : loadFont(xDisplay, fd);
		if (font != 0) return font;
	}
	if (fd.points != 0) {
		fd.points = 0;
		font = fontSet ? loadFontSet(xDisplay, fd) : loadFont(xDisplay, fd);
		if (font != 0) return font;
	}
	return 0;
}
void init (Device device, FontData fd) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	int xDisplay = device.xDisplay;
	int fontListEntry;
//	int fontStruct = loadFont(xDisplay, fd);
//	if (fontStruct == 0) {
//		/*
//		* If the desired font can not be loaded, the XLFD fields are wildcard
//		* in order to preserve the font style and height. If there is no
//		* font with the desired style and height, the slant, weight and points
//		* are wildcard in that order, until a font can be loaded.
//		*/
//		FontData newFD = new FontData();
//		newFD.slant = fd.slant;
//		newFD.weight = fd.weight;
//		newFD.points = fd.points;
//		newFD.characterSetName = fd.characterSetName;
//		if (newFD.characterSetName == null) {
//			newFD.characterSetName = device.characterSetName;
//		}
//		newFD.characterSetRegistry = fd.characterSetRegistry;
//		if (newFD.characterSetRegistry == null) {
//			newFD.characterSetRegistry = device.characterSetRegistry;
//		}
//		fontStruct = matchFont(xDisplay, newFD, false);
//
//		/* Failed to load any font. Use the system font. */
//		if (fontStruct == 0) {
//			handle = device.systemFont;
//			if (handle != 0) return;
//		}
//	}
//	fontListEntry = OS.XmFontListEntryCreate(OS.XmFONTLIST_DEFAULT_TAG, OS.XmFONT_IS_FONT, fontStruct);
	if (fd.lang != null) {
		String lang = fd.lang;
		String country = fd.country;
		String variant = fd.variant;
		String osLocale = lang;
		if (country != null) osLocale += "_" + country;
		if (variant != null) osLocale += "." + variant;
		int length = osLocale.length();
		byte [] buffer = new byte[length + 1];
		for (int i=0; i<length; i++) {
			buffer[i] = (byte)osLocale.charAt(i);
		}
		OS.setlocale (OS.LC_CTYPE, buffer);
	}		
	int fontSet = loadFontSet(xDisplay, fd);
	if (fontSet == 0) {
		/*
		* If the desired font can not be loaded, the XLFD fields are wildcard
		* in order to preserve the font style and height. If there is no
		* font with the desired style and height, the slant, weight and points
		* are wildcard in that order, until a font can be loaded.
		*/
		FontData newFD = new FontData();
		newFD.slant = fd.slant;
		newFD.weight = fd.weight;
		newFD.points = fd.points;
		newFD.characterSetName = fd.characterSetName;
		if (newFD.characterSetName == null) {
			newFD.characterSetName = device.characterSetName;
		}
		newFD.characterSetRegistry = fd.characterSetRegistry;
		if (newFD.characterSetRegistry == null) {
			newFD.characterSetRegistry = device.characterSetRegistry;
		}
		fontSet = matchFont(xDisplay, newFD, true);
	}
	if (fd.lang != null) OS.setlocale (OS.LC_CTYPE, new byte [0]);
	
	/* Failed to load any font. Use the system font. */
	if (fontSet == 0) {
		handle = device.systemFont.handle;
	} else {
		fontListEntry = OS.XmFontListEntryCreate(OS.XmFONTLIST_DEFAULT_TAG, OS.XmFONT_IS_FONTSET, fontSet);
		if (fontListEntry == 0) SWT.error(SWT.ERROR_NO_HANDLES);
		handle = OS.XmFontListAppendEntry(0, fontListEntry);
		OS.XmFontListEntryFree(new int[]{fontListEntry});
	}
	if (handle == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	
	codePage = getCodePage(xDisplay, handle);
}
/**
 * Returns <code>true</code> if the font has been disposed,
 * and <code>false</code> otherwise.
 * <p>
 * This method gets the dispose state for the font.
 * When a font has been disposed, it is an error to
 * invoke any other method using the font.
 *
 * @return <code>true</code> when the font is disposed and <code>false</code> otherwise
 */
public boolean isDisposed() {
	return handle == 0;
}
public static Font motif_new(Device device, int handle) {
	if (device == null) device = Device.getDevice();
	Font font = new Font();
	font.device = device;
	font.handle = handle;
	font.codePage = getCodePage(device.xDisplay, handle);
	return font;
}
/**
 * Returns a string containing a concise, human-readable
 * description of the receiver.
 *
 * @return a string representation of the receiver
 */
public String toString () {
	if (isDisposed()) return "Font {*DISPOSED*}";
	return "Font {" + handle + "}";
}
}
