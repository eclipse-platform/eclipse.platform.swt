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

public class nsIDOMMouseEvent extends nsIDOMUIEvent {

	static final int LAST_METHOD_ID = nsIDOMUIEvent.LAST_METHOD_ID + (IsXULRunner10 ? 14 : 11);

	public static final String NS_IDOMMOUSEEVENT_IID_STR =
		"ff751edc-8b02-aae7-0010-8301838a3123";

	public static final String NS_IDOMMOUSEEVENT_10_IID_STR =
		"7f57aa45-6792-4d8b-ba5b-201533cf0b2f";

	public static final nsID NS_IDOMMOUSEEVENT_IID =
		new nsID(NS_IDOMMOUSEEVENT_IID_STR);

	public static final nsID NS_IDOMMOUSEEVENT_10_IID =
		new nsID(NS_IDOMMOUSEEVENT_10_IID_STR);

	public nsIDOMMouseEvent(int /*long*/ address) {
		super(address);
	}

	public int GetScreenX(int[] aScreenX) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 1, getAddress(), aScreenX);
	}

	public int GetScreenY(int[] aScreenY) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 2, getAddress(), aScreenY);
	}

	public int GetClientX(int[] aClientX) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 3, getAddress(), aClientX);
	}

	public int GetClientY(int[] aClientY) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 4, getAddress(), aClientY);
	}

	public int GetCtrlKey(int[] aCtrlKey) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 5, getAddress(), aCtrlKey);
	}

	public int GetShiftKey(int[] aShiftKey) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 6, getAddress(), aShiftKey);
	}

	public int GetAltKey(int[] aAltKey) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 7, getAddress(), aAltKey);
	}

	public int GetMetaKey(int[] aMetaKey) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 8, getAddress(), aMetaKey);
	}

	public int GetButton(short[] aButton) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 9, getAddress(), aButton);
	}

	public int GetRelatedTarget(int /*long*/[] aRelatedTarget) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 10, getAddress(), aRelatedTarget);
	}

	public int InitMouseEvent(int /*long*/ typeArg, int canBubbleArg, int cancelableArg, int /*long*/ viewArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, int ctrlKeyArg, int altKeyArg, int shiftKeyArg, int metaKeyArg, short buttonArg, int /*long*/ relatedTargetArg) {
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 11, getAddress(), typeArg, canBubbleArg, cancelableArg, viewArg, detailArg, screenXArg, screenYArg, clientXArg, clientYArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, buttonArg, relatedTargetArg);
	}

	/* the following constants are defined in Mozilla 10 */
	public static final int MOZ_SOURCE_UNKNOWN = 0;
	public static final int MOZ_SOURCE_MOUSE = 1;
	public static final int MOZ_SOURCE_PEN = 2;
	public static final int MOZ_SOURCE_ERASER = 3;
	public static final int MOZ_SOURCE_CURSOR = 4;
	public static final int MOZ_SOURCE_TOUCH = 5;
	public static final int MOZ_SOURCE_KEYBOARD = 6;

	public int GetMozPressure(float[] aMozPressure) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 12, getAddress(), aMozPressure);
	}

	public int GetMozInputSource(short[] aMozInputSource) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 13, getAddress(), aMozInputSource);
	}

	public int InitNSMouseEvent(int /*long*/ typeArg, int canBubbleArg, int cancelableArg, int /*long*/ viewArg, int detailArg, int screenXArg, int screenYArg, int clientXArg, int clientYArg, int ctrlKeyArg, int altKeyArg, int shiftKeyArg, int metaKeyArg, short buttonArg, int /*long*/ relatedTargetArg, float pressure, short inputSourceArg) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMUIEvent.LAST_METHOD_ID + 14, getAddress(), typeArg, canBubbleArg, cancelableArg, viewArg, detailArg, screenXArg, screenYArg, clientXArg, clientYArg, ctrlKeyArg, altKeyArg, shiftKeyArg, metaKeyArg, buttonArg, relatedTargetArg, pressure, inputSourceArg);
	}

}
