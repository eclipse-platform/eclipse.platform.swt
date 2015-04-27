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
 * -  Copyright (C) 2003, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIDOMMouseEvent extends nsIDOMUIEvent {

	static final int LAST_METHOD_ID = nsIDOMUIEvent.LAST_METHOD_ID + (IsXULRVersionOrLater(MozillaVersion.VERSION_XR24) ? 18 : (IsXULRunner10() ? 14 : 11));

	static final String NS_IDOMMOUSEEVENT_IID_STR = "ff751edc-8b02-aae7-0010-8301838a3123";
	static final String NS_IDOMMOUSEEVENT_10_IID_STR = "7f57aa45-6792-4d8b-ba5b-201533cf0b2f";
	static final String NS_IDOMMOUSEEVENT_24_IID_STR = "afb2e57b-2822-4969-b2a9-0cada6859534";
	static final String NS_IDOMMOUSEEVENT_31_IID_STR = "df068636-9a5b-11e3-b71f-2c27d728e7f9";

	static {
		IIDStore.RegisterIID(nsIDOMMouseEvent.class, MozillaVersion.VERSION_BASE, new nsID(NS_IDOMMOUSEEVENT_IID_STR));
		IIDStore.RegisterIID(nsIDOMMouseEvent.class, MozillaVersion.VERSION_XR10, new nsID(NS_IDOMMOUSEEVENT_10_IID_STR));
		IIDStore.RegisterIID(nsIDOMMouseEvent.class, MozillaVersion.VERSION_XR24, new nsID(NS_IDOMMOUSEEVENT_24_IID_STR));
		IIDStore.RegisterIID(nsIDOMMouseEvent.class, MozillaVersion.VERSION_XR31, new nsID(NS_IDOMMOUSEEVENT_31_IID_STR));
	}

	public nsIDOMMouseEvent(long /*int*/ address) {
		super(address);
	}

	public int GetScreenX(int[] aScreenX) {
		return XPCOM.VtblCall(this.getGetterIndex("screenX"), getAddress(), aScreenX);
	}

	public int GetScreenY(int[] aScreenY) {
		return XPCOM.VtblCall(this.getGetterIndex("screenY"), getAddress(), aScreenY);
	}

	public int GetCtrlKey(int[] aCtrlKey) {
		return XPCOM.VtblCall(this.getGetterIndex("ctrlKey"), getAddress(), aCtrlKey);
	}

	public int GetShiftKey(int[] aShiftKey) {
		return XPCOM.VtblCall(this.getGetterIndex("shiftKey"), getAddress(), aShiftKey);
	}

	public int GetAltKey(int[] aAltKey) {
		return XPCOM.VtblCall(this.getGetterIndex("altKey"), getAddress(), aAltKey);
	}

	public int GetMetaKey(int[] aMetaKey) {
		return XPCOM.VtblCall(this.getGetterIndex("metaKey"), getAddress(), aMetaKey);
	}

	public int GetButton(short[] aButton) {
		return XPCOM.VtblCall(this.getGetterIndex("button"), getAddress(), aButton);
	}

	public int GetRelatedTarget(long /*int*/[] aRelatedTarget) {
		return XPCOM.VtblCall(this.getGetterIndex("relatedTarget"), getAddress(), aRelatedTarget);
	}

	/* the following constants are defined in Mozilla 10 */
	public static final int MOZ_SOURCE_UNKNOWN = 0;
	public static final int MOZ_SOURCE_MOUSE = 1;
	public static final int MOZ_SOURCE_PEN = 2;
	public static final int MOZ_SOURCE_ERASER = 3;
	public static final int MOZ_SOURCE_CURSOR = 4;
	public static final int MOZ_SOURCE_TOUCH = 5;
	public static final int MOZ_SOURCE_KEYBOARD = 6;
}
