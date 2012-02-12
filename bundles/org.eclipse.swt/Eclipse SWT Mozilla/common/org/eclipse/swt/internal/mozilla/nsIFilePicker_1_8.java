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
 * -  Copyright (C) 2003 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIFilePicker_1_8 extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 17 : 15);

	public static final String NS_IFILEPICKER_IID_STR =
		"80faf095-c807-4558-a2cc-185ed70754ea";

	public static final String NS_IFILEPICKER_10_IID_STR =
		"f2c0e216-5d07-4df4-bbcb-37683077ae7e";
	
	public static final nsID NS_IFILEPICKER_IID =
		new nsID(NS_IFILEPICKER_IID_STR);

	public static final nsID NS_IFILEPICKER_10_IID =
		new nsID(NS_IFILEPICKER_10_IID_STR);
	
	public nsIFilePicker_1_8(int /*long*/ address) {
		super(address);
	}

	public static final int modeOpen = 0;
	public static final int modeSave = 1;
	public static final int modeGetFolder = 2;
	public static final int modeOpenMultiple = 3;
	public static final int returnOK = 0;
	public static final int returnCancel = 1;
	public static final int returnReplace = 2;
	public static final int filterAll = 1;
	public static final int filterHTML = 2;
	public static final int filterText = 4;
	public static final int filterImages = 8;
	public static final int filterXML = 16;
	public static final int filterXUL = 32;
	public static final int filterApps = 64;
	
	/* the following constants are defined in mozilla 8 */
	public static final int filterAllowURLs = 128;
	public static final int filterAudio = 256;
	public static final int filterVideo = 512;

	public int Init(int /*long*/ parent, int /*long*/ title, short mode) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), parent, title, mode);
	}

	public int AppendFilters(int filterMask) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), filterMask);
	}

	public int AppendFilter(int /*long*/ title, int /*long*/ filter) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), title, filter);
	}

	public int GetDefaultString(int /*long*/ aDefaultString) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aDefaultString);
	}

	public int SetDefaultString(int /*long*/ aDefaultString) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aDefaultString);
	}

	public int GetDefaultExtension(int /*long*/ aDefaultExtension) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aDefaultExtension);
	}

	public int SetDefaultExtension(int /*long*/ aDefaultExtension) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aDefaultExtension);
	}

	public int GetFilterIndex(int[] aFilterIndex) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aFilterIndex);
	}

	public int SetFilterIndex(int aFilterIndex) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aFilterIndex);
	}

	public int GetDisplayDirectory(int /*long*/[] aDisplayDirectory) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aDisplayDirectory);
	}

	public int SetDisplayDirectory(int /*long*/ aDisplayDirectory) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aDisplayDirectory);
	}

	public int GetFile(int /*long*/[] aFile) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aFile);
	}

	public int GetFileURL(int /*long*/[] aFileURL) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aFileURL);
	}

	public int GetFiles(int /*long*/[] aFiles) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aFiles);
	}

	public int Show(int /*long*/ _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 17 : 15), getAddress(), _retval);
	}
	
	public int GetAddToRecentDocs(int[] aAddToRecentDocs) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aAddToRecentDocs);
	}

	public int SetAddToRecentDocs(int aAddToRecentDocs) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aAddToRecentDocs);
	}
}
