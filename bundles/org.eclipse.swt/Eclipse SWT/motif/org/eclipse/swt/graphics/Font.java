package org.eclipse.swt.graphics;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.motif.*;
import org.eclipse.swt.*;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public final class Font {
	/**
	 * The handle to the OS font resource.
	 * Warning: This field is platform dependent.
	 */
	public int handle;

	/**
	 * The device where this image was created.
	 */
	Device device;

Font () {
}
public Font (Device device, FontData fd) {
	init(device, fd);
}
public Font (Device device, String fontFamily, int height, int style) {
	if (fontFamily == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, new FontData(fontFamily, height, style));
}
public void dispose () {
	if (handle != 0) OS.XmFontListFree (handle);
	handle = 0;
}
public boolean equals (Object object) {
	if (object == this) return true;
	if (!(object instanceof Font)) return false;
	Font font = (Font)object;
	return device == font.device && handle == font.handle;
}
public FontData[] getFontData() {
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
		if (buffer[0] == 0) { 
			/* FontList contains a single font */
			OS.memmove(fontStruct,fontPtr,20 * 4);
			int propPtr = fontStruct.properties;
			for (int i = 0; i < fontStruct.n_properties; i++) {
				/* Reef through properties looking for XAFONT */
				int[] prop = new int[2];
				OS.memmove(prop, propPtr, 8);
				if (prop[0] == OS.XA_FONT) {
					/* Found it, prop[1] points to the string */
					StringBuffer stringBuffer = new StringBuffer();
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
						StringBuffer stringBuffer = new StringBuffer();
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
public int hashCode () {
	return handle;
}
void init (Device device, FontData fd) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	byte[] buffer = Converter.wcsToMbcs(null, fd.getXlfd(), true);
	boolean warnings = device.getWarnings();
	device.setWarnings(false);
	int fontListEntry = OS.XmFontListEntryLoad(device.xDisplay, buffer, 0, OS.XmFONTLIST_DEFAULT_TAG);
	device.setWarnings(warnings);
	if (fontListEntry == 0) SWT.error(SWT.ERROR_NO_HANDLES);
	handle = OS.XmFontListAppendEntry(0, fontListEntry);
	OS.XmFontListEntryFree(new int[]{fontListEntry});
}
public boolean isDisposed() {
	return handle == 0;
}
public static Font motif_new(Device device, int handle) {
	if (device == null) device = Device.getDevice();
	Font font = new Font();
	font.device = device;
	font.handle = handle;
	return font;
}
}
