/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import java.awt.Frame;
import java.util.Hashtable;

import org.eclipse.swt.awt.SWT_AWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.LONG;
import org.eclipse.swt.internal.carbon.*;
import org.eclipse.swt.internal.cocoa.Cocoa;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	Frame frame;
	static Hashtable handles = new Hashtable ();
	
MozillaDelegate (Browser browser) {
	super ();
	this.browser = browser;
}

static Browser findBrowser (int handle) {
	LONG value = (LONG)handles.get (new LONG (handle));
	if (value != null) {
		Display display = Display.getCurrent ();
		return (Browser)display.findWidget (value.value);
	}
	return null;
}

static char[] mbcsToWcs (String codePage, byte [] buffer) {
	int encoding = OS.CFStringGetSystemEncoding ();
	int cfstring = OS.CFStringCreateWithBytes (OS.kCFAllocatorDefault, buffer, buffer.length, encoding, false);
	char[] chars = null;
	if (cfstring != 0) {
		int length = OS.CFStringGetLength (cfstring);
		chars = new char [length];
		if (length != 0) {
			CFRange range = new CFRange ();
			range.length = length;
			OS.CFStringGetCharacters (cfstring, range, chars);
		}
		OS.CFRelease (cfstring);
	}
	return chars;
}

public static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	char[] chars = new char [string.length()];
	string.getChars (0, chars.length, chars, 0);
	int cfstring = OS.CFStringCreateWithCharacters (OS.kCFAllocatorDefault, chars, chars.length);
	byte[] buffer = null;
	if (cfstring != 0) {
		CFRange range = new CFRange ();
		range.length = chars.length;
		int encoding = OS.CFStringGetSystemEncoding ();
		int[] size = new int[1];
		int numChars = OS.CFStringGetBytes (cfstring, range, encoding, (byte)'?', true, null, 0, size);
		buffer = new byte [size[0] + (terminate ? 1 : 0)];
		if (numChars != 0) {
			numChars = OS.CFStringGetBytes (cfstring, range, encoding, (byte)'?', true, buffer, size[0], size);
		}
		OS.CFRelease (cfstring);
	}
	return buffer;
}

int getHandle () {
	frame = SWT_AWT.new_Frame (browser);
	int embedHandle = (int)Cocoa.getNativeHandleFromAWT (frame);
	handles.put (new LONG (embedHandle), new LONG (browser.handle));
	return embedHandle;
}

void onDispose (int embedHandle) {
	handles.remove (new LONG (embedHandle));
	frame.dispose ();
}

void setSize (int embedHandle, int width, int height) {
	// TODO
}

}
