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

public class nsIDocShellTreeItem extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 15;

	public static final String NS_IDOCSHELLTREEITEM_IID_STRING =
		"B52AE780-A966-11d3-AFC7-00A024FFC08C";

	public static final nsID NS_IDOCSHELLTREEITEM_IID =
		new nsID(NS_IDOCSHELLTREEITEM_IID_STRING);

	public nsIDocShellTreeItem(int address) {
		super(address);
	}

	public int GetName(int[] aName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), aName);
	}

	public int SetName(char[] aName) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 2, getAddress(), aName);
	}

	public int NameEquals(char[] name, boolean[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 3, getAddress(), name, _retval);
	}

	public static final int typeChrome = 0;

	public static final int typeContent = 1;

	public static final int typeContentWrapper = 2;

	public static final int typeChromeWrapper = 3;

	public static final int typeAll = 2147483647;

	public int GetItemType(int[] aItemType) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 4, getAddress(), aItemType);
	}

	public int SetItemType(int aItemType) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 5, getAddress(), aItemType);
	}

	public int GetParent(int[] aParent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 6, getAddress(), aParent);
	}

	public int SetParent(int aParent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 7, getAddress(), aParent);
	}

	public int GetSameTypeParent(int[] aSameTypeParent) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 8, getAddress(), aSameTypeParent);
	}

	public int GetRootTreeItem(int[] aRootTreeItem) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 9, getAddress(), aRootTreeItem);
	}

	public int GetSameTypeRootTreeItem(int[] aSameTypeRootTreeItem) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 10, getAddress(), aSameTypeRootTreeItem);
	}

	public int FindItemWithName(char[] name, int aRequestor, int[] _retval) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 11, getAddress(), name, aRequestor, _retval);
	}

	public int GetTreeOwner(int[] aTreeOwner) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 12, getAddress(), aTreeOwner);
	}

	public int SetTreeOwner(int aTreeOwner) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 13, getAddress(), aTreeOwner);
	}

	public int GetChildOffset(int[] aChildOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 14, getAddress(), aChildOffset);
	}

	public int SetChildOffset(int aChildOffset) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 15, getAddress(), aChildOffset);
	}
}