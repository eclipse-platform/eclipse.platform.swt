package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;
import org.eclipse.swt.*;

public final class Font {

	/**
	 * the handle to the OS font resource
	 * (Warning: This field is platform dependent)
	 */
	public byte[] handle;
	
	/**
	 * the device where this font was created
	 */
	Device device;
	
	static final byte[] DefaultFontName = {(byte)'h', (byte)'e', (byte)'l', (byte)'v'};
	static final byte[] DefaultFont = {(byte)'h', (byte)'e', (byte)'l', (byte)'v', (byte)'1', (byte)'2'};

Font() {
}

public Font(Device device, FontData fd) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (fd == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, fd.getName(), fd.getHeight(), fd.getStyle(), fd.stem);
	if (device.tracking) device.new_Object(this);	
}

public Font(Device device, String name, int height, int style) {
	if (device == null) device = Device.getDevice();
	if (device == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	if (name == null) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	init(device, name, height, style, null);
	if (device.tracking) device.new_Object(this);	
}

public void dispose() {
	if (handle == null) return;
		
	handle = null;
	if (device.tracking) device.dispose_Object(this);
	device = null;
}

public boolean equals(Object object) {
	if (object == this) return true;
	if (!(object instanceof Font)) return false;
	byte[] h = ((Font)object).handle;
	if (h == handle) return true;
	if (h == null || handle == null) return false;
	if (h.length != handle.length) return false;
	for (int i = 0; i < handle.length; i++) {
		if (handle[i] != h[i]) return false;
	}
	return true;
}

public FontData[] getFontData() {
	if (isDisposed()) SWT.error(SWT.ERROR_GRAPHIC_DISPOSED);
	return new FontData[]{new FontData(handle)};
}

public int hashCode () {
	if (handle == null) return 0;
	return handle.hashCode();
}

public boolean isDisposed() {
	return handle == null;
}

void init(Device device, String name, int height, int style, byte[] stem) {
	if (height < 0) SWT.error(SWT.ERROR_NULL_ARGUMENT);
	this.device = device;
	if (stem != null) {
		handle = stem;
	} else {
		byte[] description = (name == null) ? null : Converter.wcsToMbcs(null, name, true);
		int osStyle = 0;
		if ((style & SWT.BOLD) != 0) osStyle |= OS.PF_STYLE_BOLD;
		if ((style & SWT.ITALIC) != 0) osStyle |= OS.PF_STYLE_ITALIC;
		byte[] buffer = new byte[OS.MAX_FONT_TAG];
		handle = OS.PfGenerateFontName(description, osStyle, height, buffer);
		if (handle == null) handle = OS.PfGenerateFontName(DefaultFontName, osStyle, height, buffer);
	}
	if (handle == null) handle = DefaultFont;
	FontQueryInfo info = new FontQueryInfo();
	if (OS.PfQueryFontInfo(handle, info) == 0) handle = info.font;
}

public static Font photon_new(Device device, byte[] stem) {
	if (device == null) device = Device.getDevice();
	Font font = new Font();
	font.init(device, null, 0, 0, stem);
	return font;
}

public String toString () {
	if (isDisposed()) return "Font {*DISPOSED*}";
	return "Font {" + new String(handle).trim() + "}";
}

}
