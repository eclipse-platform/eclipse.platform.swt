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

public class nsIDOMEvent extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 12 : 10);

	public static final String NS_IDOMEVENT_IID_STR =
		"a66b7b80-ff46-bd97-0080-5f8ae38add32";

	public static final String NS_IDOMEVENT_10_IID_STR =
		"e85cff74-951f-45c1-be0c-89442ea2f500";

	public static final nsID NS_IDOMEVENT_IID =
		new nsID(NS_IDOMEVENT_IID_STR);

	public static final nsID NS_IDOMEVENT_10_IID =
		new nsID(NS_IDOMEVENT_10_IID_STR);

	public nsIDOMEvent(long /*int*/ address) {
		super(address);
	}

	public static final int CAPTURING_PHASE = 1;
	public static final int AT_TARGET = 2;
	public static final int BUBBLING_PHASE = 3;

	public int GetType(long /*int*/ aType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aType);
	}

	public int GetTarget(long /*int*/[] aTarget) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aTarget);
	}

	public int GetCurrentTarget(long /*int*/[] aCurrentTarget) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aCurrentTarget);
	}

	public int GetEventPhase(short[] aEventPhase) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aEventPhase);
	}

	public int GetBubbles(int[] aBubbles) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aBubbles);
	}

	public int GetCancelable(int[] aCancelable) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aCancelable);
	}

	public int GetTimeStamp(long /*int*/ aTimeStamp) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 7, getAddress(), aTimeStamp);
	}

	public int StopPropagation() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 8, getAddress());
	}

	public int PreventDefault() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 9, getAddress());
	}

	public int InitEvent(long /*int*/ eventTypeArg, int canBubbleArg, int cancelableArg) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 10, getAddress(), eventTypeArg, canBubbleArg, cancelableArg);
	}
	
	public int GetDefaultPrevented(int[] aDefaultPrevented) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 11, getAddress(), aDefaultPrevented);
	}

	public int StopImmediatePropagation() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 12, getAddress());
	}
}
