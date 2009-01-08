/*******************************************************************************
 * Copyright (c) 2005, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.internal.win32.*;
import org.eclipse.swt.widgets.*;

class MozillaDelegate {
	Browser browser;
	int /*long*/ mozillaProc;
	Callback subclassProc;
	
MozillaDelegate (Browser browser) {
	super ();
	this.browser = browser;
}

static Browser findBrowser (int /*long*/ handle) {
	Display display = Display.getCurrent ();
	return (Browser)display.findWidget (handle);
}

static char[] mbcsToWcs (String codePage, byte[] buffer) {
	char[] chars = new char[buffer.length];
	int charCount = OS.MultiByteToWideChar (OS.CP_ACP, OS.MB_PRECOMPOSED, buffer, buffer.length, chars, chars.length);
	if (charCount == chars.length) return chars;
	char[] result = new char[charCount];
	System.arraycopy (chars, 0, result, 0, charCount);
	return result;
}

static byte[] wcsToMbcs (String codePage, String string, boolean terminate) {
	int byteCount;
	char[] chars = new char[string.length()];
	string.getChars (0, chars.length, chars, 0);
	byte[] bytes = new byte[byteCount = chars.length * 2 + (terminate ? 1 : 0)];
	byteCount = OS.WideCharToMultiByte (OS.CP_ACP, 0, chars, chars.length, bytes, byteCount, null, null);
	if (terminate) {
		byteCount++;
	} else {
		if (bytes.length != byteCount) {
			byte[] result = new byte[byteCount];
			System.arraycopy (bytes, 0, result, 0, byteCount);
			bytes = result;
		}
	}
	return bytes;
}

void addWindowSubclass () {
	int /*long*/ hwndChild = OS.GetWindow (browser.handle, OS.GW_CHILD);
	mozillaProc = OS.GetWindowLong (hwndChild, OS.GWL_WNDPROC);
	subclassProc = new Callback (this, "windowProc", 4); //$NON-NLS-1$
	OS.SetWindowLong (hwndChild, OS.GWL_WNDPROC, subclassProc.getAddress ());
}

int /*long*/ getHandle () {
	return browser.handle;
}

String getLibraryName () {
	return "xpcom.dll"; //$NON-NLS-1$
}

String getSWTInitLibraryName () {
	return "swt-xulrunner"; //$NON-NLS-1$
}

void handleFocus () {
}

void handleMouseDown () {
}

boolean hookEnterExit () {
	return true;
}

void init () {
}

boolean needsSpinup () {
	return false;
}

void onDispose (int /*long*/ embedHandle) {
	browser = null;
	if (subclassProc != null) {
		subclassProc.dispose ();
		subclassProc = null;
	}
}

void removeWindowSubclass () {
	if (subclassProc == null) return;
	int /*long*/ hwndChild = OS.GetWindow (browser.handle, OS.GW_CHILD);
	OS.SetWindowLong (hwndChild, OS.GWL_WNDPROC, mozillaProc);
	subclassProc.dispose ();
	subclassProc = null;
}

void setSize (int /*long*/ embedHandle, int width, int height) {
}

int /*long*/ windowProc (int /*long*/ hwnd, int /*long*/ msg, int /*long*/ wParam, int /*long*/ lParam) {
	switch (msg) {
		case OS.WM_ERASEBKGND:
			RECT rect = new RECT ();
			OS.GetClientRect (hwnd, rect);
			OS.FillRect (wParam, rect, OS.GetSysColorBrush (OS.COLOR_WINDOW));
			break;
	}
	return OS.CallWindowProc (mozillaProc, hwnd, msg, wParam, lParam);
}
}
