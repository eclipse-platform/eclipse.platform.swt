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

public class nsIDOMWindowInternal extends nsIDOMWindow {

	static final int LAST_METHOD_ID = nsIDOMWindow.LAST_METHOD_ID + 69;

	public static final String NS_IDOMWINDOWINTERNAL_IID_STRING =
		"9c911860-7dd9-11d4-9a83-000064657374";

	public static final nsID NS_IDOMWINDOWINTERNAL_IID =
		new nsID(NS_IDOMWINDOWINTERNAL_IID_STRING);

	public nsIDOMWindowInternal(int address) {
		super(address);
	}

	public int GetWindow(int[] aWindow) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aWindow);
	}

	public int GetSelf(int[] aSelf) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aSelf);
	}

	public int GetNavigator(int[] aNavigator) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), aNavigator);
	}

	public int GetScreen(int[] aScreen) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aScreen);
	}

	public int GetHistory(int[] aHistory) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aHistory);
	}

	public int GetContent(int[] aContent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aContent);
	}

	public int GetSidebar(int[] aSidebar) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aSidebar);
	}

	public int GetPrompter(int[] aPrompter) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aPrompter);
	}

	public int GetMenubar(int[] aMenubar) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aMenubar);
	}

	public int GetToolbar(int[] aToolbar) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aToolbar);
	}

	public int GetLocationbar(int[] aLocationbar) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), aLocationbar);
	}

	public int GetPersonalbar(int[] aPersonalbar) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aPersonalbar);
	}

	public int GetStatusbar(int[] aStatusbar) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), aStatusbar);
	}

	public int GetDirectories(int[] directories) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), directories);
	}

	public int GetClosed(boolean[] aClosed) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), aClosed);
	}

	public int GetCrypto(int[] aCrypto) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 16, getAddress(), aCrypto);
	}

	public int GetPkcs11(int[] aPkcs11) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 17, getAddress(), aPkcs11);
	}

	public int GetControllers(int[] controllers) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 18, getAddress(), controllers);
	}

	public int GetOpener(int[] aOpener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 19, getAddress(), aOpener);
	}

	public int SetOpener(int aOpener) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 20, getAddress(), aOpener);
	}

	public int GetStatus(int status) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 21, getAddress(), status);
	}

	public int SetStatus(int status) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 22, getAddress(), status);
	}

	public int GetDefaultStatus(int defaultStatus) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 23, getAddress(), defaultStatus);
	}

	public int SetDefaultStatus(int defaultStatus) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 24, getAddress(), defaultStatus);
	}

	public int GetLocation(int[] aLocation) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 25, getAddress(), aLocation);
	}

	public int GetInnerWidth(int[] aInnerWidth) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 26, getAddress(), aInnerWidth);
	}

	public int SetInnerWidth(int aInnerWidth) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 27, getAddress(), aInnerWidth);
	}

	public int GetInnerHeight(int[] aInnerHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 28, getAddress(), aInnerHeight);
	}

	public int SetInnerHeight(int aInnerHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 29, getAddress(), aInnerHeight);
	}

	public int GetOuterWidth(int[] aOuterWidth) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 30, getAddress(), aOuterWidth);
	}

	public int SetOuterWidth(int aOuterWidth) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 31, getAddress(), aOuterWidth);
	}

	public int GetOuterHeight(int[] aOuterHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 32, getAddress(), aOuterHeight);
	}

	public int SetOuterHeight(int aOuterHeight) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 33, getAddress(), aOuterHeight);
	}

	public int GetScreenX(int[] aScreenX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 34, getAddress(), aScreenX);
	}

	public int SetScreenX(int aScreenX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 35, getAddress(), aScreenX);
	}

	public int GetScreenY(int[] aScreenY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 36, getAddress(), aScreenY);
	}

	public int SetScreenY(int aScreenY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 37, getAddress(), aScreenY);
	}

	public int GetPageXOffset(int[] aPageXOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 38, getAddress(), aPageXOffset);
	}

	public int GetPageYOffset(int[] aPageYOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 39, getAddress(), aPageYOffset);
	}

	public int GetScrollMaxX(int[] aScrollMaxX) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 40, getAddress(), aScrollMaxX);
	}

	public int GetScrollMaxY(int[] aScrollMaxY) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 41, getAddress(), aScrollMaxY);
	}

	public int GetLength(int[] aLength) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 42, getAddress(), aLength);
	}

	public int GetFullScreen(boolean[] aFullScreen) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 43, getAddress(), aFullScreen);
	}

	public int SetFullScreen(boolean aFullScreen) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 44, getAddress(), aFullScreen);
	}

	public int Alert(int text) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 45, getAddress(), text);
	}

	public int Confirm(int text, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 46, getAddress(), text, _retval);
	}

	public int Prompt(int aMessage, int aInitial, int aTitle, int aSavePassword, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 47, getAddress(), aMessage, aInitial, aTitle, aSavePassword, _retval);
	}

	public int Focus() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 48, getAddress());
	}

	public int Blur() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 49, getAddress());
	}

	public int Back() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 50, getAddress());
	}

	public int Forward() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 51, getAddress());
	}

	public int Home() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 52, getAddress());
	}

	public int Stop() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 53, getAddress());
	}

	public int Print() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 54, getAddress());
	}

	public int MoveTo(int xPos, int yPos) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 55, getAddress(), xPos, yPos);
	}

	public int MoveBy(int xDif, int yDif) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 56, getAddress(), xDif, yDif);
	}

	public int ResizeTo(int width, int height) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 57, getAddress(), width, height);
	}

	public int ResizeBy(int widthDif, int heightDif) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 58, getAddress(), widthDif, heightDif);
	}

	public int Scroll(int xScroll, int yScroll) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 59, getAddress(), xScroll, yScroll);
	}

	public int Open(int url, int name, int options, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 60, getAddress(), url, name, options, _retval);
	}

	public int OpenDialog(int url, int name, int options, int aExtraArgument, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 61, getAddress(), url, name, options, aExtraArgument, _retval);
	}

	public int Close() {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 62, getAddress());
	}

	public int UpdateCommands(int action) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 63, getAddress(), action);
	}

	public int Escape(int str, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 64, getAddress(), str, _retval);
	}

	public int Unescape(int str, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 65, getAddress(), str, _retval);
	}

	public int Find(int str, boolean caseSensitive, boolean backwards, boolean wrapAround, boolean wholeWord, boolean searchInFrames, boolean showDialog, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 66, getAddress(), str, caseSensitive, backwards, wrapAround, wholeWord, searchInFrames, showDialog, _retval);
	}

	public int Atob(int aAsciiString, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 67, getAddress(), aAsciiString, _retval);
	}

	public int Btoa(int aBase64Data, int _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 68, getAddress(), aBase64Data, _retval);
	}

	public int GetFrameElement(int[] aFrameElement) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 69, getAddress(), aFrameElement);
	}
}