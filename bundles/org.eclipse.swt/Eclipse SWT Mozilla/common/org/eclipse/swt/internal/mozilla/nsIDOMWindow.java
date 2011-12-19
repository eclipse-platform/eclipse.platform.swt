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

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (Is8 ? 97 : 17);

	public static final String NS_IDOMWINDOW_IID_STR =
		"a6cf906b-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMWINDOW_8_IID_STR =
		"972cb379-6bdc-4544-8b46-8d721e12e906";

	public static final nsID NS_IDOMWINDOW_IID =
		new nsID(NS_IDOMWINDOW_IID_STR);

	public static final nsID NS_IDOMWINDOW_8_IID =
		new nsID(NS_IDOMWINDOW_8_IID_STR);

	public nsIDOMWindow(int /*long*/ address) {
		super(address);
	}

	public int GetDocument(int /*long*/[] aDocument) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 3 : 1), getAddress(), aDocument);
	}

	public int GetParent(int /*long*/[] aParent) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 24 : 2), getAddress(), aParent);
	}

	public int GetTop(int /*long*/[] aTop) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 21 : 3), getAddress(), aTop);
	}

	public int GetScrollbars(int /*long*/[] aScrollbars) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 11 : 4), getAddress(), aScrollbars);
	}

	public int GetFrames(int /*long*/[] aFrames) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 62 : 5), getAddress(), aFrames);
	}

	public int GetName(int /*long*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 4 : 6), getAddress(), aName);
	}

	public int SetName(int /*long*/ aName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 5 : 7), getAddress(), aName);
	}

	public int GetTextZoom(float[] aTextZoom) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 63 : 8), getAddress(), aTextZoom);
	}

	public int SetTextZoom(float aTextZoom) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 64 : 9), getAddress(), aTextZoom);
	}

	public int GetScrollX(int[] aScrollX) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 45 : 10), getAddress(), aScrollX);
	}

	public int GetScrollY(int[] aScrollY) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 47 : 11), getAddress(), aScrollY);
	}

	public int ScrollTo(int xScroll, int yScroll) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 50 : 12), getAddress(), xScroll, yScroll);
	}

	public int ScrollBy(int xScrollDif, int yScrollDif) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 51 : 13), getAddress(), xScrollDif, yScrollDif);
	}

	public int GetSelection(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 38 : 14), getAddress(), _retval);
	}

	public int ScrollByLines(int numLines) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 65 : 15), getAddress(), numLines);
	}

	public int ScrollByPages(int numPages) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 66 : 16), getAddress(), numPages);
	}

	public int SizeToContent() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (Is8 ? 67 : 17), getAddress());
	}
	
	public int GetWindow(int /*long*/[] aWindow) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aWindow);
	}

	public int GetSelf(int /*long*/[] aSelf) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aSelf);
	}

	public int GetLocation(int /*long*/[] aLocation) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aLocation);
	}

	public int GetHistory(int /*long*/[] aHistory) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aHistory);
	}

	public int GetLocationbar(int /*long*/[] aLocationbar) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress(), aLocationbar);
	}

	public int GetMenubar(int /*long*/[] aMenubar) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress(), aMenubar);
	}

	public int GetPersonalbar(int /*long*/[] aPersonalbar) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), aPersonalbar);
	}

	public int GetStatusbar(int /*long*/[] aStatusbar) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress(), aStatusbar);
	}

	public int GetToolbar(int /*long*/[] aToolbar) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 13, getAddress(), aToolbar);
	}

	public int GetStatus(int /*long*/ aStatus) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 14, getAddress(), aStatus);
	}

	public int SetStatus(int /*long*/ aStatus) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 15, getAddress(), aStatus);
	}

	public int Close() {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 16, getAddress());
	}

	public int Stop() {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 17, getAddress());
	}

	public int Focus() {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 18, getAddress());
	}

	public int Blur() {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 19, getAddress());
	}

	public int GetLength(int[] aLength) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 20, getAddress(), aLength);
	}

	public int GetOpener(int /*long*/[] aOpener) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 22, getAddress(), aOpener);
	}

	public int SetOpener(int /*long*/ aOpener) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), aOpener);
	}

	public int GetFrameElement(int /*long*/[] aFrameElement) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), aFrameElement);
	}

	public int GetNavigator(int /*long*/[] aNavigator) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), aNavigator);
	}

	public int GetApplicationCache(int /*long*/[] aApplicationCache) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 27, getAddress(), aApplicationCache);
	}

	public int Alert(int /*long*/ text) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 28, getAddress(), text);
	}

	public int Confirm(int /*long*/ text, int[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 29, getAddress(), text, _retval);
	}

	public int Prompt(int /*long*/ aMessage, int /*long*/ aInitial, int /*long*/ _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 30, getAddress(), aMessage, aInitial, _retval);
	}

	public int Print() {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 31, getAddress());
	}

	public int ShowModalDialog(int /*long*/ aURI, int /*long*/ aArgs, int /*long*/ aOptions, int /*long*/[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 32, getAddress(), aURI, aArgs, aOptions, _retval);
	}

	public int PostMessageMoz(int /*long*/ message, int /*long*/ targetOrigin, int /*long*/ cx) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 33, getAddress(), message, targetOrigin, cx);
	}

	public int Atob(int /*long*/ aAsciiString, int /*long*/ _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 34, getAddress(), aAsciiString, _retval);
	}

	public int Btoa(int /*long*/ aBase64Data, int /*long*/ _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 35, getAddress(), aBase64Data, _retval);
	}

	public int GetSessionStorage(int /*long*/[] aSessionStorage) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 36, getAddress(), aSessionStorage);
	}

	public int GetLocalStorage(int /*long*/[] aLocalStorage) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 37, getAddress(), aLocalStorage);
	}

	public int MatchMedia(int /*long*/ media_query_list, int /*long*/[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 39, getAddress(), media_query_list, _retval);
	}

	public int GetScreen(int /*long*/[] aScreen) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 40, getAddress(), aScreen);
	}

	public int GetInnerWidth(int[] aInnerWidth) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 41, getAddress(), aInnerWidth);
	}

	public int SetInnerWidth(int aInnerWidth) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 42, getAddress(), aInnerWidth);
	}

	public int GetInnerHeight(int[] aInnerHeight) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 43, getAddress(), aInnerHeight);
	}

	public int SetInnerHeight(int aInnerHeight) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 44, getAddress(), aInnerHeight);
	}

	public int GetPageXOffset(int[] aPageXOffset) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 46, getAddress(), aPageXOffset);
	}

	public int GetPageYOffset(int[] aPageYOffset) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 48, getAddress(), aPageYOffset);
	}

	public int Scroll(int xScroll, int yScroll) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 49, getAddress(), xScroll, yScroll);
	}

	public int GetScreenX(int[] aScreenX) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 52, getAddress(), aScreenX);
	}

	public int SetScreenX(int aScreenX) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 53, getAddress(), aScreenX);
	}

	public int GetScreenY(int[] aScreenY) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 54, getAddress(), aScreenY);
	}

	public int SetScreenY(int aScreenY) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 55, getAddress(), aScreenY);
	}

	public int GetOuterWidth(int[] aOuterWidth) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 56, getAddress(), aOuterWidth);
	}

	public int SetOuterWidth(int aOuterWidth) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 57, getAddress(), aOuterWidth);
	}

	public int GetOuterHeight(int[] aOuterHeight) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 58, getAddress(), aOuterHeight);
	}

	public int SetOuterHeight(int aOuterHeight) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 59, getAddress(), aOuterHeight);
	}

	public int GetComputedStyle(int /*long*/ elt, int /*long*/ pseudoElt, int /*long*/[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 60, getAddress(), elt, pseudoElt, _retval);
	}

	public int GetWindowRoot(int /*long*/[] aWindowRoot) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 61, getAddress(), aWindowRoot);
	}

	public int GetContent(int /*long*/[] aContent) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 68, getAddress(), aContent);
	}

	public int GetPrompter(int /*long*/[] aPrompter) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 69, getAddress(), aPrompter);
	}

	public int GetClosed(int[] aClosed) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 70, getAddress(), aClosed);
	}

	public int GetCrypto(int /*long*/[] aCrypto) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 71, getAddress(), aCrypto);
	}

	public int GetPkcs11(int /*long*/[] aPkcs11) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 72, getAddress(), aPkcs11);
	}

	public int GetControllers(int /*long*/[] aControllers) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 73, getAddress(), aControllers);
	}

	public int GetDefaultStatus(int /*long*/ aDefaultStatus) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 74, getAddress(), aDefaultStatus);
	}

	public int SetDefaultStatus(int /*long*/ aDefaultStatus) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 75, getAddress(), aDefaultStatus);
	}

	public int GetMozInnerScreenX(float[] aMozInnerScreenX) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 76, getAddress(), aMozInnerScreenX);
	}

	public int GetMozInnerScreenY(float[] aMozInnerScreenY) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 77, getAddress(), aMozInnerScreenY);
	}

	public int GetScrollMaxX(int[] aScrollMaxX) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 78, getAddress(), aScrollMaxX);
	}

	public int GetScrollMaxY(int[] aScrollMaxY) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 79, getAddress(), aScrollMaxY);
	}

	public int GetFullScreen(int[] aFullScreen) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 80, getAddress(), aFullScreen);
	}

	public int SetFullScreen(int aFullScreen) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 81, getAddress(), aFullScreen);
	}

	public int Back() {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 82, getAddress());
	}

	public int Forward() {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 83, getAddress());
	}

	public int Home() {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 84, getAddress());
	}

	public int MoveTo(int xPos, int yPos) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 85, getAddress(), xPos, yPos);
	}

	public int MoveBy(int xDif, int yDif) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 86, getAddress(), xDif, yDif);
	}

	public int ResizeTo(int width, int height) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 87, getAddress(), width, height);
	}

	public int ResizeBy(int widthDif, int heightDif) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 88, getAddress(), widthDif, heightDif);
	}

	public int Open(int /*long*/ url, int /*long*/ name, int /*long*/ options, int /*long*/[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 89, getAddress(), url, name, options, _retval);
	}

	public int OpenDialog(int /*long*/ url, int /*long*/ name, int /*long*/ options, int /*long*/ aExtraArgument, int /*long*/[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 90, getAddress(), url, name, options, aExtraArgument, _retval);
	}

	public int UpdateCommands(int /*long*/ action) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 91, getAddress(), action);
	}

	public int Find(int /*long*/ str, int caseSensitive, int backwards, int wrapAround, int wholeWord, int searchInFrames, int showDialog, int[] _retval) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 92, getAddress(), str, caseSensitive, backwards, wrapAround, wholeWord, searchInFrames, showDialog, _retval);
	}

	public int GetMozPaintCount(int /*long*/ aMozPaintCount) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 93, getAddress(), aMozPaintCount);
	}

	public int MozRequestAnimationFrame(int /*long*/ aListener) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 94, getAddress(), aListener);
	}

	public int GetMozAnimationStartTime(long[] aMozAnimationStartTime) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 95, getAddress(), aMozAnimationStartTime);
	}

	public int GetURL(int /*long*/[] aURL) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 96, getAddress(), aURL);
	}

	public int GetGlobalStorage(int /*long*/[] aGlobalStorage) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 97, getAddress(), aGlobalStorage);
	}
}
