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
 * -  Copyright (C) 2003, 2008 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIDOMUIEvent extends nsIDOMEvent {

	static final int LAST_METHOD_ID = nsIDOMEvent.LAST_METHOD_ID + (IsXULRunner10 ? 13 : 3);

	public static final String NS_IDOMUIEVENT_IID_STR =
		"a6cf90c3-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMUIEVENT_10_IID_STR =
		"af3f130e-0c22-4613-a150-780a46c22e3a";
	
	public static final nsID NS_IDOMUIEVENT_IID =
		new nsID(NS_IDOMUIEVENT_IID_STR);

	public static final nsID NS_IDOMUIEVENT_10_IID =
		new nsID(NS_IDOMUIEVENT_10_IID_STR);

	public nsIDOMUIEvent(int /*long*/ address) {
		super(address);
	}

	public int GetView(int /*long*/[] aView) {
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 1, getAddress(), aView);
	}

	public int GetDetail(int[] aDetail) {
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 2, getAddress(), aDetail);
	}

	public int InitUIEvent(int /*long*/ typeArg, int canBubbleArg, int cancelableArg, int /*long*/ viewArg, int detailArg) {
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 3, getAddress(), typeArg, canBubbleArg, cancelableArg, viewArg, detailArg);
	}
	
	/* the following constants are defined in Mozilla 10 */
	public static final int SCROLL_PAGE_UP = -32768;
	public static final int SCROLL_PAGE_DOWN = 32768;

	public int GetLayerX(int[] aLayerX) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 4, getAddress(), aLayerX);
	}

	public int GetLayerY(int[] aLayerY) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 5, getAddress(), aLayerY);
	}

	public int GetPageX(int[] aPageX) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 6, getAddress(), aPageX);
	}

	public int GetPageY(int[] aPageY) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 7, getAddress(), aPageY);
	}

	public int GetWhich(int[] aWhich) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 8, getAddress(), aWhich);
	}

	public int GetRangeParent(int /*long*/[] aRangeParent) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 9, getAddress(), aRangeParent);
	}

	public int GetRangeOffset(int[] aRangeOffset) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 10, getAddress(), aRangeOffset);
	}

	public int GetCancelBubble(int[] aCancelBubble) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 11, getAddress(), aCancelBubble);
	}

	public int SetCancelBubble(int aCancelBubble) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 12, getAddress(), aCancelBubble);
	}

	public int GetIsChar(int[] aIsChar) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMEvent.LAST_METHOD_ID + 13, getAddress(), aIsChar);
	}
}
