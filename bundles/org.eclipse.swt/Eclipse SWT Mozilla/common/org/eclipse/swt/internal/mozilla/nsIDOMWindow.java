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
 * -  Copyright (C) 2003, 2005 IBM Corp.  All Rights Reserved.
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

	public nsIDOMWindow(int /*long*/ address) {
		super(address);
	}

	public int GetWindow(int /*long*/[] aWindow) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aWindow);
	}

	public int GetSelf(int /*long*/[] aSelf) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aSelf);
	}

	public int GetDocument(int /*long*/[] aDocument) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 3 : 1), getAddress(), aDocument);
	}

	public int GetParent(int /*long*/[] aParent) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 24 : 2), getAddress(), aParent);
	}

	public int GetTop(int /*long*/[] aTop) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 21 : 3), getAddress(), aTop);
	}

	public int GetScrollbars(int /*long*/[] aScrollbars) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 11 : 4), getAddress(), aScrollbars);
	}

	public int GetFrames(int /*long*/[] aFrames) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 62 : 5), getAddress(), aFrames);
	}

	public int GetName(int /*long*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 4 : 6), getAddress(), aName);
	}

	public int SetName(int /*long*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 5 : 7), getAddress(), aName);
	}

	public int GetLocation(int /*long*/[] aLocation) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aLocation);
	}

	public int GetHistory(int /*long*/[] aHistory) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aHistory);
	}

	public int GetLocationbar(int /*long*/[] aLocationbar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aLocationbar);
	}

	public int GetMenubar(int /*long*/[] aMenubar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aMenubar);
	}

	public int GetPersonalbar(int /*long*/[] aPersonalbar) {
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

	public int GetSelection(int /*long*/[] _retval) {
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
	

	public int GetStatusbar(int /*long*/[] aStatusbar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aStatusbar);
	}

	public int GetToolbar(int /*long*/[] aToolbar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aToolbar);
	}

	public int GetStatus(int /*long*/ aStatus) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aStatus);
	}

	public int SetStatus(int /*long*/ aStatus) {
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

	public int GetOpener(int /*long*/[] aOpener) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), aOpener);
	}

	public int SetOpener(int /*long*/ aOpener) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), aOpener);
	}

	public int GetFrameElement(int /*long*/[] aFrameElement) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), aFrameElement);
	}

	public int GetNavigator(int /*long*/[] aNavigator) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), aNavigator);
	}

	public int GetApplicationCache(int /*long*/[] aApplicationCache) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 27, getAddress(), aApplicationCache);
	}

	public int Alert(int /*long*/ text) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 28, getAddress(), text);
	}

	public int Confirm(int /*long*/ text, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 29, getAddress(), text, _retval);
	}

	public int Prompt(int /*long*/ aMessage, int /*long*/ aInitial, int /*long*/ _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 30, getAddress(), aMessage, aInitial, _retval);
	}

	public int Print() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 31, getAddress());
	}

	public int ShowModalDialog(int /*long*/ aURI, int /*long*/ aArgs, int /*long*/ aOptions, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 32, getAddress(), aURI, aArgs, aOptions, _retval);
	}

	public int PostMessageMoz(int /*long*/ message, int /*long*/ targetOrigin, int /*long*/ cx) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 33, getAddress(), message, targetOrigin, cx);
	}

	public int Atob(int /*long*/ aAsciiString, int /*long*/ _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 34, getAddress(), aAsciiString, _retval);
	}

	public int Btoa(int /*long*/ aBase64Data, int /*long*/ _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 35, getAddress(), aBase64Data, _retval);
	}

	public int GetSessionStorage(int /*long*/[] aSessionStorage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 36, getAddress(), aSessionStorage);
	}

	public int GetLocalStorage(int /*long*/[] aLocalStorage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 37, getAddress(), aLocalStorage);
	}

	public int MatchMedia(int /*long*/ media_query_list, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 39, getAddress(), media_query_list, _retval);
	}

	public int GetScreen(int /*long*/[] aScreen) {
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

	public int GetComputedStyle(int /*long*/ elt, int /*long*/ pseudoElt, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 60, getAddress(), elt, pseudoElt, _retval);
	}

	public int GetWindowRoot(int /*long*/[] aWindowRoot) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 61, getAddress(), aWindowRoot);
	}

	public int GetContent(int /*long*/[] aContent) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 68, getAddress(), aContent);
	}

	public int GetPrompter(int /*long*/[] aPrompter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 69, getAddress(), aPrompter);
	}

	public int GetClosed(int[] aClosed) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 70, getAddress(), aClosed);
	}

	public int GetCrypto(int /*long*/[] aCrypto) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 71, getAddress(), aCrypto);
	}

	public int GetPkcs11(int /*long*/[] aPkcs11) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 72, getAddress(), aPkcs11);
	}

	public int GetControllers(int /*long*/[] aControllers) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 73, getAddress(), aControllers);
	}

	public int GetDefaultStatus(int /*long*/ aDefaultStatus) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 74, getAddress(), aDefaultStatus);
	}

	public int SetDefaultStatus(int /*long*/ aDefaultStatus) {
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

	public int Open(int /*long*/ url, int /*long*/ name, int /*long*/ options, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 89, getAddress(), url, name, options, _retval);
	}

	public int OpenDialog(int /*long*/ url, int /*long*/ name, int /*long*/ options, int /*long*/ aExtraArgument, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 90, getAddress(), url, name, options, aExtraArgument, _retval);
	}

	public int UpdateCommands(int /*long*/ action) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 91, getAddress(), action);
	}

	public int Find(int /*long*/ str, int caseSensitive, int backwards, int wrapAround, int wholeWord, int searchInFrames, int showDialog, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 92, getAddress(), str, caseSensitive, backwards, wrapAround, wholeWord, searchInFrames, showDialog, _retval);
	}

	public int GetMozPaintCount(int /*long*/ aMozPaintCount) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 93, getAddress(), aMozPaintCount);
	}

	public int MozRequestAnimationFrame(int /*long*/ aListener) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 94, getAddress(), aListener);
	}

	public int GetMozAnimationStartTime(long[] aMozAnimationStartTime) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 95, getAddress(), aMozAnimationStartTime);
	}

	public int GetURL(int /*long*/[] aURL) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 96, getAddress(), aURL);
	}

	public int GetGlobalStorage(int /*long*/[] aGlobalStorage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 97, getAddress(), aGlobalStorage);
	}

	public int GetOnafterprint(int /*long*/ cx, int /*long*/ aOnafterprint) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 98, getAddress(), cx, aOnafterprint);
	}

	public int SetOnafterprint(int /*long*/ cx, int /*long*/ aOnafterprint) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 99, getAddress(), cx, aOnafterprint);
	}

	public int GetOnbeforeprint(int /*long*/ cx, int /*long*/ aOnbeforeprint) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 100, getAddress(), cx, aOnbeforeprint);
	}

	public int SetOnbeforeprint(int /*long*/ cx, int /*long*/ aOnbeforeprint) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 101, getAddress(), cx, aOnbeforeprint);
	}

	public int GetOnbeforeunload(int /*long*/ cx, int /*long*/ aOnbeforeunload) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 102, getAddress(), cx, aOnbeforeunload);
	}

	public int SetOnbeforeunload(int /*long*/ cx, int /*long*/ aOnbeforeunload) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 103, getAddress(), cx, aOnbeforeunload);
	}

	public int GetOnhashchange(int /*long*/ cx, int /*long*/ aOnhashchange) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 104, getAddress(), cx, aOnhashchange);
	}

	public int SetOnhashchange(int /*long*/ cx, int /*long*/ aOnhashchange) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 105, getAddress(), cx, aOnhashchange);
	}

	public int GetOnmessage(int /*long*/ cx, int /*long*/ aOnmessage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 106, getAddress(), cx, aOnmessage);
	}

	public int SetOnmessage(int /*long*/ cx, int /*long*/ aOnmessage) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 107, getAddress(), cx, aOnmessage);
	}

	public int GetOnoffline(int /*long*/ cx, int /*long*/ aOnoffline) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 108, getAddress(), cx, aOnoffline);
	}

	public int SetOnoffline(int /*long*/ cx, int /*long*/ aOnoffline) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 109, getAddress(), cx, aOnoffline);
	}

	public int GetOnonline(int /*long*/ cx, int /*long*/ aOnonline) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 110, getAddress(), cx, aOnonline);
	}

	public int SetOnonline(int /*long*/ cx, int /*long*/ aOnonline) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 111, getAddress(), cx, aOnonline);
	}

	public int GetOnpopstate(int /*long*/ cx, int /*long*/ aOnpopstate) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 112, getAddress(), cx, aOnpopstate);
	}

	public int SetOnpopstate(int /*long*/ cx, int /*long*/ aOnpopstate) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 113, getAddress(), cx, aOnpopstate);
	}

	public int GetOnpagehide(int /*long*/ cx, int /*long*/ aOnpagehide) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 114, getAddress(), cx, aOnpagehide);
	}

	public int SetOnpagehide(int /*long*/ cx, int /*long*/ aOnpagehide) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 115, getAddress(), cx, aOnpagehide);
	}

	public int GetOnpageshow(int /*long*/ cx, int /*long*/ aOnpageshow) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 116, getAddress(), cx, aOnpageshow);
	}

	public int SetOnpageshow(int /*long*/ cx, int /*long*/ aOnpageshow) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 117, getAddress(), cx, aOnpageshow);
	}

	public int GetOnresize(int /*long*/ cx, int /*long*/ aOnresize) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 118, getAddress(), cx, aOnresize);
	}

	public int SetOnresize(int /*long*/ cx, int /*long*/ aOnresize) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 119, getAddress(), cx, aOnresize);
	}

	public int GetOnunload(int /*long*/ cx, int /*long*/ aOnunload) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 120, getAddress(), cx, aOnunload);
	}

	public int SetOnunload(int /*long*/ cx, int /*long*/ aOnunload) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 121, getAddress(), cx, aOnunload);
	}

	public int GetOndevicemotion(int /*long*/ cx, int /*long*/ aOndevicemotion) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 122, getAddress(), cx, aOndevicemotion);
	}

	public int SetOndevicemotion(int /*long*/ cx, int /*long*/ aOndevicemotion) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 123, getAddress(), cx, aOndevicemotion);
	}

	public int GetOndeviceorientation(int /*long*/ cx, int /*long*/ aOndeviceorientation) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 124, getAddress(), cx, aOndeviceorientation);
	}

	public int SetOndeviceorientation(int /*long*/ cx, int /*long*/ aOndeviceorientation) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 125, getAddress(), cx, aOndeviceorientation);
	}

	public int GetOnmouseenter(int /*long*/ cx, int /*long*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 126, getAddress(), cx, aOnmouseenter);
	}

	public int SetOnmouseenter(int /*long*/ cx, int /*long*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 127, getAddress(), cx, aOnmouseenter);
	}

	public int GetOnmouseleave(int /*long*/ cx, int /*long*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 128, getAddress(), cx, aOnmouseleave);
	}

	public int SetOnmouseleave(int /*long*/ cx, int /*long*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 129, getAddress(), cx, aOnmouseleave);
	}
}
