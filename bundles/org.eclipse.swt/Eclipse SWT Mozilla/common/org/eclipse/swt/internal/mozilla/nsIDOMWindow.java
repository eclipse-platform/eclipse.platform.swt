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

public class nsIDOMWindow extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 129 : 17);

	public static final String NS_IDOMWINDOW_IID_STR =
		"a6cf906b-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMWINDOW_10_IID_STR =
		"8f577294-d572-4473-94b1-d2c5a74a2a74";

	public static final nsID NS_IDOMWINDOW_IID =
		new nsID(NS_IDOMWINDOW_IID_STR);

	public static final nsID NS_IDOMWINDOW_10_IID =
		new nsID(NS_IDOMWINDOW_10_IID_STR);

	public nsIDOMWindow(long /*int*/ address) {
		super(address);
	}

	public int GetWindow(long /*int*/[] aWindow) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aWindow);
	}

	public int GetSelf(long /*int*/[] aSelf) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aSelf);
	}

	public int GetDocument(long /*int*/[] aDocument) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 3 : 1), getAddress(), aDocument);
	}

	public int GetParent(long /*int*/[] aParent) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 24 : 2), getAddress(), aParent);
	}

	public int GetTop(long /*int*/[] aTop) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 21 : 3), getAddress(), aTop);
	}

	public int GetScrollbars(long /*int*/[] aScrollbars) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 11 : 4), getAddress(), aScrollbars);
	}

	public int GetFrames(long /*int*/[] aFrames) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 62 : 5), getAddress(), aFrames);
	}

	public int GetName(long /*int*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 4 : 6), getAddress(), aName);
	}

	public int SetName(long /*int*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 5 : 7), getAddress(), aName);
	}

	public int GetLocation(long /*int*/[] aLocation) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aLocation);
	}

	public int GetHistory(long /*int*/[] aHistory) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aHistory);
	}

	public int GetLocationbar(long /*int*/[] aLocationbar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aLocationbar);
	}

	public int GetMenubar(long /*int*/[] aMenubar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aMenubar);
	}

	public int GetPersonalbar(long /*int*/[] aPersonalbar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aPersonalbar);
	}

	public int GetTextZoom(float[] aTextZoom) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 63 : 8), getAddress(), aTextZoom);
	}

	public int SetTextZoom(float aTextZoom) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 64 : 9), getAddress(), aTextZoom);
	}

	public int GetScrollX(int[] aScrollX) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 45 : 10), getAddress(), aScrollX);
	}

	public int GetScrollY(int[] aScrollY) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 47 : 11), getAddress(), aScrollY);
	}

	public int ScrollTo(int xScroll, int yScroll) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 50 : 12), getAddress(), xScroll, yScroll);
	}

	public int ScrollBy(int xScrollDif, int yScrollDif) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 51 : 13), getAddress(), xScrollDif, yScrollDif);
	}

	public int GetSelection(long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 38 : 14), getAddress(), _retval);
	}

	public int ScrollByLines(int numLines) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 65 : 15), getAddress(), numLines);
	}

	public int ScrollByPages(int numPages) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 66 : 16), getAddress(), numPages);
	}

	public int SizeToContent() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 67 : 17), getAddress());
	}
	

	public int GetStatusbar(long /*int*/[] aStatusbar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aStatusbar);
	}

	public int GetToolbar(long /*int*/[] aToolbar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aToolbar);
	}

	public int GetStatus(long /*int*/ aStatus) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aStatus);
	}

	public int SetStatus(long /*int*/ aStatus) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aStatus);
	}

	public int Close() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress());
	}

	public int Stop() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress());
	}

	public int Focus() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress());
	}

	public int Blur() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress());
	}

	public int GetLength(int[] aLength) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), aLength);
	}

	public int GetOpener(long /*int*/[] aOpener) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), aOpener);
	}

	public int SetOpener(long /*int*/ aOpener) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), aOpener);
	}

	public int GetFrameElement(long /*int*/[] aFrameElement) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), aFrameElement);
	}

	public int GetNavigator(long /*int*/[] aNavigator) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), aNavigator);
	}

	public int GetApplicationCache(long /*int*/[] aApplicationCache) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 27, getAddress(), aApplicationCache);
	}

	public int Alert(long /*int*/ text) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 28, getAddress(), text);
	}

	public int Confirm(long /*int*/ text, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 29, getAddress(), text, _retval);
	}

	public int Prompt(long /*int*/ aMessage, long /*int*/ aInitial, long /*int*/ _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 30, getAddress(), aMessage, aInitial, _retval);
	}

	public int Print() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 31, getAddress());
	}

	public int ShowModalDialog(long /*int*/ aURI, long /*int*/ aArgs, long /*int*/ aOptions, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 32, getAddress(), aURI, aArgs, aOptions, _retval);
	}

	public int PostMessageMoz(long /*int*/ message, long /*int*/ targetOrigin, long /*int*/ cx) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 33, getAddress(), message, targetOrigin, cx);
	}

	public int Atob(long /*int*/ aAsciiString, long /*int*/ _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 34, getAddress(), aAsciiString, _retval);
	}

	public int Btoa(long /*int*/ aBase64Data, long /*int*/ _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 35, getAddress(), aBase64Data, _retval);
	}

	public int GetSessionStorage(long /*int*/[] aSessionStorage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 36, getAddress(), aSessionStorage);
	}

	public int GetLocalStorage(long /*int*/[] aLocalStorage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 37, getAddress(), aLocalStorage);
	}

	public int MatchMedia(long /*int*/ media_query_list, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 39, getAddress(), media_query_list, _retval);
	}

	public int GetScreen(long /*int*/[] aScreen) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 40, getAddress(), aScreen);
	}

	public int GetInnerWidth(int[] aInnerWidth) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 41, getAddress(), aInnerWidth);
	}

	public int SetInnerWidth(int aInnerWidth) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 42, getAddress(), aInnerWidth);
	}

	public int GetInnerHeight(int[] aInnerHeight) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 43, getAddress(), aInnerHeight);
	}

	public int SetInnerHeight(int aInnerHeight) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 44, getAddress(), aInnerHeight);
	}

	public int GetPageXOffset(int[] aPageXOffset) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 46, getAddress(), aPageXOffset);
	}

	public int GetPageYOffset(int[] aPageYOffset) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 48, getAddress(), aPageYOffset);
	}

	public int Scroll(int xScroll, int yScroll) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 49, getAddress(), xScroll, yScroll);
	}

	public int GetScreenX(int[] aScreenX) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 52, getAddress(), aScreenX);
	}

	public int SetScreenX(int aScreenX) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 53, getAddress(), aScreenX);
	}

	public int GetScreenY(int[] aScreenY) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 54, getAddress(), aScreenY);
	}

	public int SetScreenY(int aScreenY) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 55, getAddress(), aScreenY);
	}

	public int GetOuterWidth(int[] aOuterWidth) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 56, getAddress(), aOuterWidth);
	}

	public int SetOuterWidth(int aOuterWidth) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 57, getAddress(), aOuterWidth);
	}

	public int GetOuterHeight(int[] aOuterHeight) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 58, getAddress(), aOuterHeight);
	}

	public int SetOuterHeight(int aOuterHeight) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 59, getAddress(), aOuterHeight);
	}

	public int GetComputedStyle(long /*int*/ elt, long /*int*/ pseudoElt, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 60, getAddress(), elt, pseudoElt, _retval);
	}

	public int GetWindowRoot(long /*int*/[] aWindowRoot) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 61, getAddress(), aWindowRoot);
	}

	public int GetContent(long /*int*/[] aContent) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 68, getAddress(), aContent);
	}

	public int GetPrompter(long /*int*/[] aPrompter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 69, getAddress(), aPrompter);
	}

	public int GetClosed(int[] aClosed) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 70, getAddress(), aClosed);
	}

	public int GetCrypto(long /*int*/[] aCrypto) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 71, getAddress(), aCrypto);
	}

	public int GetPkcs11(long /*int*/[] aPkcs11) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 72, getAddress(), aPkcs11);
	}

	public int GetControllers(long /*int*/[] aControllers) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 73, getAddress(), aControllers);
	}

	public int GetDefaultStatus(long /*int*/ aDefaultStatus) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 74, getAddress(), aDefaultStatus);
	}

	public int SetDefaultStatus(long /*int*/ aDefaultStatus) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 75, getAddress(), aDefaultStatus);
	}

	public int GetMozInnerScreenX(float[] aMozInnerScreenX) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 76, getAddress(), aMozInnerScreenX);
	}

	public int GetMozInnerScreenY(float[] aMozInnerScreenY) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 77, getAddress(), aMozInnerScreenY);
	}

	public int GetScrollMaxX(int[] aScrollMaxX) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 78, getAddress(), aScrollMaxX);
	}

	public int GetScrollMaxY(int[] aScrollMaxY) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 79, getAddress(), aScrollMaxY);
	}

	public int GetFullScreen(int[] aFullScreen) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 80, getAddress(), aFullScreen);
	}

	public int SetFullScreen(int aFullScreen) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 81, getAddress(), aFullScreen);
	}

	public int Back() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 82, getAddress());
	}

	public int Forward() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 83, getAddress());
	}

	public int Home() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 84, getAddress());
	}

	public int MoveTo(int xPos, int yPos) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 85, getAddress(), xPos, yPos);
	}

	public int MoveBy(int xDif, int yDif) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 86, getAddress(), xDif, yDif);
	}

	public int ResizeTo(int width, int height) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 87, getAddress(), width, height);
	}

	public int ResizeBy(int widthDif, int heightDif) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 88, getAddress(), widthDif, heightDif);
	}

	public int Open(long /*int*/ url, long /*int*/ name, long /*int*/ options, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 89, getAddress(), url, name, options, _retval);
	}

	public int OpenDialog(long /*int*/ url, long /*int*/ name, long /*int*/ options, long /*int*/ aExtraArgument, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 90, getAddress(), url, name, options, aExtraArgument, _retval);
	}

	public int UpdateCommands(long /*int*/ action) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 91, getAddress(), action);
	}

	public int Find(long /*int*/ str, int caseSensitive, int backwards, int wrapAround, int wholeWord, int searchInFrames, int showDialog, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 92, getAddress(), str, caseSensitive, backwards, wrapAround, wholeWord, searchInFrames, showDialog, _retval);
	}

	public int GetMozPaintCount(long /*int*/ aMozPaintCount) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 93, getAddress(), aMozPaintCount);
	}

	public int MozRequestAnimationFrame(long /*int*/ aListener) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 94, getAddress(), aListener);
	}

	public int GetMozAnimationStartTime(long[] aMozAnimationStartTime) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 95, getAddress(), aMozAnimationStartTime);
	}

	public int GetURL(long /*int*/[] aURL) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 96, getAddress(), aURL);
	}

	public int GetGlobalStorage(long /*int*/[] aGlobalStorage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 97, getAddress(), aGlobalStorage);
	}

	public int GetOnafterprint(long /*int*/ cx, long /*int*/ aOnafterprint) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 98, getAddress(), cx, aOnafterprint);
	}

	public int SetOnafterprint(long /*int*/ cx, long /*int*/ aOnafterprint) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 99, getAddress(), cx, aOnafterprint);
	}

	public int GetOnbeforeprint(long /*int*/ cx, long /*int*/ aOnbeforeprint) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 100, getAddress(), cx, aOnbeforeprint);
	}

	public int SetOnbeforeprint(long /*int*/ cx, long /*int*/ aOnbeforeprint) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 101, getAddress(), cx, aOnbeforeprint);
	}

	public int GetOnbeforeunload(long /*int*/ cx, long /*int*/ aOnbeforeunload) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 102, getAddress(), cx, aOnbeforeunload);
	}

	public int SetOnbeforeunload(long /*int*/ cx, long /*int*/ aOnbeforeunload) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 103, getAddress(), cx, aOnbeforeunload);
	}

	public int GetOnhashchange(long /*int*/ cx, long /*int*/ aOnhashchange) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 104, getAddress(), cx, aOnhashchange);
	}

	public int SetOnhashchange(long /*int*/ cx, long /*int*/ aOnhashchange) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 105, getAddress(), cx, aOnhashchange);
	}

	public int GetOnmessage(long /*int*/ cx, long /*int*/ aOnmessage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 106, getAddress(), cx, aOnmessage);
	}

	public int SetOnmessage(long /*int*/ cx, long /*int*/ aOnmessage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 107, getAddress(), cx, aOnmessage);
	}

	public int GetOnoffline(long /*int*/ cx, long /*int*/ aOnoffline) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 108, getAddress(), cx, aOnoffline);
	}

	public int SetOnoffline(long /*int*/ cx, long /*int*/ aOnoffline) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 109, getAddress(), cx, aOnoffline);
	}

	public int GetOnonline(long /*int*/ cx, long /*int*/ aOnonline) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 110, getAddress(), cx, aOnonline);
	}

	public int SetOnonline(long /*int*/ cx, long /*int*/ aOnonline) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 111, getAddress(), cx, aOnonline);
	}

	public int GetOnpopstate(long /*int*/ cx, long /*int*/ aOnpopstate) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 112, getAddress(), cx, aOnpopstate);
	}

	public int SetOnpopstate(long /*int*/ cx, long /*int*/ aOnpopstate) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 113, getAddress(), cx, aOnpopstate);
	}

	public int GetOnpagehide(long /*int*/ cx, long /*int*/ aOnpagehide) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 114, getAddress(), cx, aOnpagehide);
	}

	public int SetOnpagehide(long /*int*/ cx, long /*int*/ aOnpagehide) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 115, getAddress(), cx, aOnpagehide);
	}

	public int GetOnpageshow(long /*int*/ cx, long /*int*/ aOnpageshow) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 116, getAddress(), cx, aOnpageshow);
	}

	public int SetOnpageshow(long /*int*/ cx, long /*int*/ aOnpageshow) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 117, getAddress(), cx, aOnpageshow);
	}

	public int GetOnresize(long /*int*/ cx, long /*int*/ aOnresize) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 118, getAddress(), cx, aOnresize);
	}

	public int SetOnresize(long /*int*/ cx, long /*int*/ aOnresize) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 119, getAddress(), cx, aOnresize);
	}

	public int GetOnunload(long /*int*/ cx, long /*int*/ aOnunload) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 120, getAddress(), cx, aOnunload);
	}

	public int SetOnunload(long /*int*/ cx, long /*int*/ aOnunload) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 121, getAddress(), cx, aOnunload);
	}

	public int GetOndevicemotion(long /*int*/ cx, long /*int*/ aOndevicemotion) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 122, getAddress(), cx, aOndevicemotion);
	}

	public int SetOndevicemotion(long /*int*/ cx, long /*int*/ aOndevicemotion) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 123, getAddress(), cx, aOndevicemotion);
	}

	public int GetOndeviceorientation(long /*int*/ cx, long /*int*/ aOndeviceorientation) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 124, getAddress(), cx, aOndeviceorientation);
	}

	public int SetOndeviceorientation(long /*int*/ cx, long /*int*/ aOndeviceorientation) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 125, getAddress(), cx, aOndeviceorientation);
	}

	public int GetOnmouseenter(long /*int*/ cx, long /*int*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 126, getAddress(), cx, aOnmouseenter);
	}

	public int SetOnmouseenter(long /*int*/ cx, long /*int*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 127, getAddress(), cx, aOnmouseenter);
	}

	public int GetOnmouseleave(long /*int*/ cx, long /*int*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 128, getAddress(), cx, aOnmouseleave);
	}

	public int SetOnmouseleave(long /*int*/ cx, long /*int*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 129, getAddress(), cx, aOnmouseleave);
	}
}
