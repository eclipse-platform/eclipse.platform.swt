package org.eclipse.swt.graphics;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.photon.*;

public abstract class Device implements Drawable {
	
	/* Debugging */
	public static boolean DEBUG;
	boolean debug = DEBUG;
	public boolean tracking = DEBUG;
	Error [] errors;
	Object [] objects;

	boolean disposed;
	
	/*
	* TEMPORARY CODE. When a graphics object is
	* created and the device parameter is null,
	* the current Display is used. This presents
	* a problem because SWT graphics does not
	* reference classes in SWT widgets. The correct
	* fix is to remove this feature. Unfortunately,
	* too many application programs rely on this
	* feature.
	*
	* This code will be removed in the future.
	*/
	protected static Device CurrentDevice;
	protected static Runnable DeviceFinder;
	static {
		try {
			Class.forName ("org.eclipse.swt.widgets.Display");
		} catch (Throwable e) {}
	}	

static Device getDevice () {
	if (DeviceFinder != null) DeviceFinder.run();
	Device device = CurrentDevice;
	CurrentDevice = null;
	return device;
}
		
public Device(DeviceData data) {
	if (data != null) {
		debug = data.debug;
		tracking = data.tracking;
	}
	create (data);
	init ();
	if (tracking) {
		errors = new Error [128];
		objects = new Object [128];
	}
}

protected void checkDevice () {
	if (disposed) SWT.error(SWT.ERROR_DEVICE_DISPOSED);
}

protected void create (DeviceData data) {
}

protected void destroy () {
}

public void dispose () {
	if (isDisposed()) return;
	checkDevice ();
	release ();
	destroy ();
	disposed = true;
	if (tracking) {
		objects = null;
		errors = null;
	}
}

void dispose_Object (Object object) {
	for (int i=0; i<objects.length; i++) {
		if (objects [i] == object) {
			objects [i] = null;
			errors [i] = null;
			return;
		}
	}
}

public Rectangle getBounds () {
	checkDevice ();
	PgDisplaySettings_t settings = new PgDisplaySettings_t ();
	OS.PgGetVideoMode (settings);
	return new Rectangle (0, 0, settings.xres, settings.yres);
}

public Rectangle getClientArea () {
	checkDevice ();
	PhRect_t rect = new PhRect_t ();
	OS.PhWindowQueryVisible (OS.Ph_QUERY_GRAPHICS, 0, 1, rect);
	int width = rect.lr_x - rect.ul_x + 1;
	int height = rect.lr_y - rect.ul_y + 1;
	return new Rectangle (rect.ul_x, rect.ul_y, width, height);
}

public int getDepth () {
	checkDevice ();
	PgDisplaySettings_t settings = new PgDisplaySettings_t ();
	OS.PgGetVideoMode (settings);
	PgVideoModeInfo_t mode_info = new PgVideoModeInfo_t ();
	OS.PgGetVideoModeInfo ((short) settings.mode, mode_info);
	return mode_info.bits_per_pixel;
}

public DeviceData getDeviceData () {
	checkDevice();
	DeviceData data = new DeviceData ();
	data.debug = debug;
	data.tracking = tracking;
	int count = 0, length = 0;
	if (tracking) length = objects.length;
	for (int i=0; i<length; i++) {
		if (objects [i] != null) count++;
	}
	int index = 0;
	data.objects = new Object [count];
	data.errors = new Error [count];
	for (int i=0; i<length; i++) {
		if (objects [i] != null) {
			data.objects [index] = objects [i];
			data.errors [index] = errors [i];
			index++;
		}
	}
	return data;
}

public Point getDPI () {
	checkDevice ();
	//NOT DONE
	return new Point (96, 96);
}

public FontData [] getFontList (String faceName, boolean scalable) {
	checkDevice ();
	int flags = OS.PHFONT_FIXED | OS.PHFONT_PROP | OS.PFFONT_DONT_SHOW_LEGACY;
	flags |= scalable ? OS.PHFONT_SCALABLE : OS.PHFONT_BITMAP;
	int nfonts = OS.PfQueryFonts(OS.PHFONT_ALL_FONTS, flags, 0, 0);
	if (nfonts <= 0) return new FontData[0];
	
	int list_ptr = OS.malloc(nfonts * FontDetails.sizeof);
	nfonts = OS.PfQueryFonts(OS.PHFONT_ALL_FONTS, flags, list_ptr, nfonts);
	int ptr = list_ptr;
	int nFds = 0;
	FontData[] fds = new FontData[faceName != null ? 4 : nfonts];
	FontDetails details = new FontDetails();
	for (int i = 0; i < nfonts; i++) {
		OS.memmove(details, ptr, FontDetails.sizeof);
		String name = new String(Converter.mbcsToWcs(null, details.desc)).trim();
		if (faceName == null || Compatibility.equalsIgnoreCase(faceName, name)) {
			int size;
			if (details.losize == 0 && details.hisize == 0) size = 9; // This value was taken from the PhAB editor
			else size = details.losize;
			flags = details.flags & ~(OS.PHFONT_INFO_PROP | OS.PHFONT_INFO_FIXED);
			while (flags != 0) {
				int style;
				if ((flags & OS.PHFONT_INFO_PLAIN) != 0) {
					style = SWT.NORMAL;
					flags &= ~OS.PHFONT_INFO_PLAIN;
				} else if ((flags & OS.PHFONT_INFO_BOLD) != 0) {
					style = SWT.BOLD;
					flags &= ~OS.PHFONT_INFO_BOLD;
				} else if ((flags & OS.PHFONT_INFO_ITALIC) != 0) {
					style = SWT.ITALIC;
					flags &= ~OS.PHFONT_INFO_ITALIC;
				} else if ((flags & OS.PHFONT_INFO_BLDITC) != 0) {
					style = SWT.BOLD | SWT.ITALIC;
					flags &= ~OS.PHFONT_INFO_BLDITC;
				} else break;					
				if (nFds == fds.length) {
					FontData[] newFds = new FontData[fds.length + nfonts];
					System.arraycopy(fds, 0, newFds, 0, nFds);
					fds = newFds;
				}
				fds[nFds++] = new FontData(name, size, style);
			}
		}
		ptr += FontDetails.sizeof;
	}
	OS.free(list_ptr);

	if (nFds == fds.length) return fds;

	FontData[] result = new FontData[nFds];
	System.arraycopy(fds, 0, result, 0, nFds);
	return result;
}

public Color getSystemColor (int id) {
	checkDevice ();
	int color = 0x000000;
	switch (id) {
		case SWT.COLOR_BLACK: 		color = 0x000000; break;
		case SWT.COLOR_DARK_RED: 	color = 0x800000; break;
		case SWT.COLOR_DARK_GREEN:	color = 0x008000; break;
		case SWT.COLOR_DARK_YELLOW: 	color = 0x808000; break;
		case SWT.COLOR_DARK_BLUE: 	color = 0x000080; break;
		case SWT.COLOR_DARK_MAGENTA: 	color = 0x800080; break;
		case SWT.COLOR_DARK_CYAN: 	color = 0x008080; break;
		case SWT.COLOR_GRAY: 		color = 0x808080; break;
		case SWT.COLOR_DARK_GRAY: 	color = 0x404040; break;
		case SWT.COLOR_RED: 		color = 0xFF0000; break;
		case SWT.COLOR_GREEN: 		color = 0x00FF00; break;
		case SWT.COLOR_YELLOW: 		color = 0xFFFF00; break;
		case SWT.COLOR_BLUE: 		color = 0x0000FF; break;
		case SWT.COLOR_MAGENTA: 	color = 0xFF00FF; break;
		case SWT.COLOR_CYAN: 		color = 0x00FFFF; break;
		case SWT.COLOR_WHITE: 		color = 0xFFFFFF; break;
	}
	return Color.photon_new (this, color);
}

public Font getSystemFont () {
	checkDevice ();
	return Font.photon_new (this, Font.DefaultFont);
}

public boolean getWarnings () {
	checkDevice ();
	return false;
}

protected void init () {
}

public abstract int internal_new_GC (GCData data);

public abstract void internal_dispose_GC (int handle, GCData data);

public boolean isDisposed () {
	return disposed;
}

void new_Object (Object object) {
	for (int i=0; i<objects.length; i++) {
		if (objects [i] == null) {
			objects [i] = object;
			errors [i] = new Error ();
			return;
		}
	}
	Object [] newObjects = new Object [objects.length + 128];
	System.arraycopy (objects, 0, newObjects, 0, objects.length);
	newObjects [objects.length] = object;
	objects = newObjects;
	Error [] newErrors = new Error [errors.length + 128];
	System.arraycopy (errors, 0, newErrors, 0, errors.length);
	newErrors [errors.length] = new Error ();
	errors = newErrors;
}

protected void release () {
}

public void setWarnings (boolean warnings) {
	checkDevice ();
}

}
