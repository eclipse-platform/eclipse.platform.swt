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
 * -  Copyright (C) 2003, 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIDocShell extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 51;

	public static final String NS_IDOCSHELL_IID_STR =
		"69e5de00-7b8b-11d3-af61-00a024ffc08c";

	public static final nsID NS_IDOCSHELL_IID =
		new nsID(NS_IDOCSHELL_IID_STR);

	public static final String NS_IDOCSHELL_1_8_IID_STR =
		"9f0c7461-b9a4-47f6-b88c-421dce1bce66";

	public static final nsID NS_IDOCSHELL_1_8_IID =
		new nsID(NS_IDOCSHELL_1_8_IID_STR);

	public static final String NS_IDOCSHELL_10_IID_STR =
		"0666adf8-8738-4ca7-a917-0348f47d2f40";

	public static final nsID NS_IDOCSHELL_10_IID =
		new nsID(NS_IDOCSHELL_10_IID_STR);

	public nsIDocShell(long /*int*/ address) {
		super(address);
	}

	public int LoadURI(long /*int*/ uri, long /*int*/ loadInfo, int aLoadFlags, int firstParty) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), uri, loadInfo, aLoadFlags, firstParty);
	}

	public int LoadStream(long /*int*/ aStream, long /*int*/ aURI, long /*int*/ aContentType, long /*int*/ aContentCharset, long /*int*/ aLoadInfo) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aStream, aURI, aContentType, aContentCharset, aLoadInfo);
	}

	public int InternalLoad(long /*int*/ aURI, long /*int*/ aReferrer, long /*int*/ aOwner, int aInheritOwner, char[] aWindowTarget, long /*int*/ aPostDataStream, long /*int*/ aHeadersStream, int aLoadFlags, long /*int*/ aSHEntry, int firstParty, long /*int*/[] aDocShell, long /*int*/[] aRequest) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aURI, aReferrer, aOwner, aInheritOwner, aWindowTarget, aPostDataStream, aHeadersStream, aLoadFlags, aSHEntry, firstParty, aDocShell, aRequest);
	}

	public int CreateLoadInfo(long /*int*/[] loadInfo) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), loadInfo);
	}

	public int PrepareForNewContentModel() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress());
	}

	public int SetCurrentURI(long /*int*/ aURI) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aURI);
	}

	public int FireUnloadNotification() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress());
	}

	public int GetPresContext(long /*int*/[] aPresContext) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aPresContext);
	}

	public int GetPresShell(long /*int*/[] aPresShell) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aPresShell);
	}

	public int GetEldestPresShell(long /*int*/[] aEldestPresShell) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aEldestPresShell);
	}

	public int GetContentViewer(long /*int*/[] aContentViewer) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aContentViewer);
	}

	public int GetChromeEventHandler(long /*int*/[] aChromeEventHandler) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aChromeEventHandler);
	}

	public int SetChromeEventHandler(long /*int*/ aChromeEventHandler) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aChromeEventHandler);
	}

	public int GetParentURIContentListener(long /*int*/[] aParentURIContentListener) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aParentURIContentListener);
	}

	public int SetParentURIContentListener(long /*int*/ aParentURIContentListener) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aParentURIContentListener);
	}

	public int GetDocumentCharsetInfo(long /*int*/[] aDocumentCharsetInfo) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress(), aDocumentCharsetInfo);
	}

	public int SetDocumentCharsetInfo(long /*int*/ aDocumentCharsetInfo) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress(), aDocumentCharsetInfo);
	}

	public int GetAllowPlugins(int[] aAllowPlugins) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress(), aAllowPlugins);
	}

	public int SetAllowPlugins(int aAllowPlugins) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress(), aAllowPlugins);
	}

	public int GetAllowJavascript(int[] aAllowJavascript) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), aAllowJavascript);
	}

	public int SetAllowJavascript(int aAllowJavascript) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 21, getAddress(), aAllowJavascript);
	}

	public int GetAllowMetaRedirects(int[] aAllowMetaRedirects) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), aAllowMetaRedirects);
	}

	public int SetAllowMetaRedirects(int aAllowMetaRedirects) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), aAllowMetaRedirects);
	}

	public int GetAllowSubframes(int[] aAllowSubframes) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 24, getAddress(), aAllowSubframes);
	}

	public int SetAllowSubframes(int aAllowSubframes) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), aAllowSubframes);
	}

	public int GetAllowImages(int[] aAllowImages) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), aAllowImages);
	}

	public int SetAllowImages(int aAllowImages) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 27, getAddress(), aAllowImages);
	}

	public static final int ENUMERATE_FORWARDS = 0;

	public static final int ENUMERATE_BACKWARDS = 1;

	public int GetDocShellEnumerator(int aItemType, int aDirection, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 28, getAddress(), aItemType, aDirection, _retval);
	}

	public static final int APP_TYPE_UNKNOWN = 0;

	public static final int APP_TYPE_MAIL = 1;

	public int GetAppType(int[] aAppType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 29, getAddress(), aAppType);
	}

	public int SetAppType(int aAppType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 30, getAddress(), aAppType);
	}

	public int GetAllowAuth(int[] aAllowAuth) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 31, getAddress(), aAllowAuth);
	}

	public int SetAllowAuth(int aAllowAuth) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 32, getAddress(), aAllowAuth);
	}

	public int GetZoom(float[] aZoom) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 33, getAddress(), aZoom);
	}

	public int SetZoom(float aZoom) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 34, getAddress(), aZoom);
	}

	public int GetMarginWidth(int[] aMarginWidth) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 35, getAddress(), aMarginWidth);
	}

	public int SetMarginWidth(int aMarginWidth) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 36, getAddress(), aMarginWidth);
	}

	public int GetMarginHeight(int[] aMarginHeight) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 37, getAddress(), aMarginHeight);
	}

	public int SetMarginHeight(int aMarginHeight) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 38, getAddress(), aMarginHeight);
	}

	public int GetHasFocus(int[] aHasFocus) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 39, getAddress(), aHasFocus);
	}

	public int SetHasFocus(int aHasFocus) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 40, getAddress(), aHasFocus);
	}

	public int GetCanvasHasFocus(int[] aCanvasHasFocus) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 41, getAddress(), aCanvasHasFocus);
	}

	public int SetCanvasHasFocus(int aCanvasHasFocus) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 42, getAddress(), aCanvasHasFocus);
	}

	public int TabToTreeOwner(int forward, int[] tookFocus) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 43, getAddress(), forward, tookFocus);
	}

	public static final int BUSY_FLAGS_NONE = 0;
	public static final int BUSY_FLAGS_BUSY = 1;
	public static final int BUSY_FLAGS_BEFORE_PAGE_LOAD = 2;
	public static final int BUSY_FLAGS_PAGE_LOADING = 4;
	public static final int LOAD_CMD_NORMAL = 1;
	public static final int LOAD_CMD_RELOAD = 2;
	public static final int LOAD_CMD_HISTORY = 4;

	public int GetBusyFlags(int[] aBusyFlags) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 44, getAddress(), aBusyFlags);
	}

	public int GetLoadType(int[] aLoadType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 45, getAddress(), aLoadType);
	}

	public int SetLoadType(int aLoadType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 46, getAddress(), aLoadType);
	}

	public int IsBeingDestroyed(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 47, getAddress(), _retval);
	}

	public int GetIsExecutingOnLoadHandler(int[] aIsExecutingOnLoadHandler) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 48, getAddress(), aIsExecutingOnLoadHandler);
	}

	public int GetLayoutHistoryState(long /*int*/[] aLayoutHistoryState) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 49, getAddress(), aLayoutHistoryState);
	}

	public int SetLayoutHistoryState(long /*int*/ aLayoutHistoryState) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 50, getAddress(), aLayoutHistoryState);
	}

	public int GetShouldSaveLayoutState(int[] aShouldSaveLayoutState) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 51, getAddress(), aShouldSaveLayoutState);
	}
}
