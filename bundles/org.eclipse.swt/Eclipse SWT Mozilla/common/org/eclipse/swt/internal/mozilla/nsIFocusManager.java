/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is Mozilla Communicator client code, released March 31, 1998.
 *
 * The Initial Developer of the Original Code is
 * Netscape Communications Corporation.
 * Portions created by Netscape are Copyright (C) 1998-1999
 * Netscape Communications Corporation.  All Rights Reserved.
 *
 * Contributor(s):
 *
 * IBM
 * -  Binding to permit interfacing between Mozilla and SWT
 * -  Copyright (C) 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIFocusManager extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 18 : 17);

	public static final String NS_IFOCUSMANAGER_IID_STR =
			"cd6040a8-243f-412a-8a16-0bf2aa1083b9";
	
	public static final String NS_IFOCUSMANAGER_10_IID_STR =
			"51db277b-7ee7-4bce-9b84-fd2efcd2c8bd";

	public static final nsID NS_IFOCUSMANAGER_IID =
		new nsID(NS_IFOCUSMANAGER_IID_STR);
	
	public static final nsID NS_IFOCUSMANAGER_10_IID =
			new nsID(NS_IFOCUSMANAGER_10_IID_STR);

	public nsIFocusManager(int /*long*/ address) {
		super(address);
	}

	public int GetActiveWindow(int /*long*/[] aActiveWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aActiveWindow);
	}

	public int SetActiveWindow(int /*long*/ aActiveWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aActiveWindow);
	}

	public int GetFocusedWindow(int /*long*/[] aFocusedWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aFocusedWindow);
	}

	public int SetFocusedWindow(int /*long*/ aFocusedWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aFocusedWindow);
	}

	public int GetFocusedElement(int /*long*/[] aFocusedElement) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aFocusedElement);
	}

	public int GetLastFocusMethod(int /*long*/ window, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), window, _retval);
	}

	public int SetFocus(int /*long*/ aElement, int aFlags) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aElement, aFlags);
	}

	public int MoveFocus(int /*long*/ aWindow, int /*long*/ aStartElement, int aType, int aFlags, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aWindow, aStartElement, aType, aFlags, _retval);
	}

	public int ClearFocus(int /*long*/ aWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aWindow);
	}

	public int GetFocusedElementForWindow(int /*long*/ aWindow, int aDeep, int /*long*/[] aFocusedWindow, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aWindow, aDeep, aFocusedWindow, _retval);
	}

	public int MoveCaretToFocus(int /*long*/ aWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aWindow);
	}

	public static final int FLAG_RAISE = 1;
	public static final int FLAG_NOSCROLL = 2;
	public static final int FLAG_NOSWITCHFRAME = 4;
	public static final int FLAG_BYMOUSE = 4096;
	public static final int FLAG_BYKEY = 8192;
	public static final int FLAG_BYMOVEFOCUS = 16384;
	public static final int MOVEFOCUS_FORWARD = 1;
	public static final int MOVEFOCUS_BACKWARD = 2;
	public static final int MOVEFOCUS_FORWARDDOC = 3;
	public static final int MOVEFOCUS_BACKWARDDOC = 4;
	public static final int MOVEFOCUS_FIRST = 5;
	public static final int MOVEFOCUS_LAST = 6;
	public static final int MOVEFOCUS_ROOT = 7;
	public static final int MOVEFOCUS_CARET = 8;

	public int WindowRaised(int /*long*/ aWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aWindow);
	}

	public int WindowLowered(int /*long*/ aWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aWindow);
	}

	public int ContentRemoved(int /*long*/ aDocument, int /*long*/ aElement) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aDocument, aElement);
	}

	public int WindowShown(int /*long*/ aWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aWindow);
	}

	public int WindowHidden(int /*long*/ aWindow) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aWindow);
	}

	public int FireDelayedEvents(int /*long*/ aDocument) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), aDocument);
	}
	
	public int FocusPlugin(int /*long*/ aPlugin) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), aPlugin);
	}
}
