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
 * -  Copyright (C) 2011, 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIDOMElement extends nsIDOMNode {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 47 : (IsXULRunner10 ? 20 : 16));

	public static final String NS_IDOMELEMENT_IID_STR =
		"a6cf9078-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMELEMENT_10_IID_STR =
		"1f249e8b-7b41-44c0-a8d5-15298c1198cd";

	public static final String NS_IDOMELEMENT_17_IID_STR =
		"69d44ce2-b544-49a8-bb5f-87804b971ee4";

	public static final nsID NS_IDOMELEMENT_IID =
		new nsID(NS_IDOMELEMENT_IID_STR);
	
	public static final nsID NS_IDOMELEMENT_10_IID =
		new nsID(NS_IDOMELEMENT_10_IID_STR);

	public static final nsID NS_IDOMELEMENT_17_IID =
		new nsID(NS_IDOMELEMENT_17_IID_STR);

	public nsIDOMElement(long /*int*/ address) {
		super(address);
	}

	public int GetTagName(long /*int*/ aTagName) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 1, getAddress(), aTagName);
	}

	public int GetAttribute(long /*int*/ name, long /*int*/ _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 3 : 2), getAddress(), name, _retval);
	}

	public int SetAttribute(long /*int*/ name, long /*int*/ value) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 5 : 3), getAddress(), name, value);
	}

	public int RemoveAttribute(long /*int*/ name) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 7 : 4), getAddress(), name);
	}

	public int GetAttributeNode(long /*int*/ name, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 11 : 5), getAddress(), name, _retval);
	}

	public int SetAttributeNode(long /*int*/ newAttr, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 12 : 6), getAddress(), newAttr, _retval);
	}

	public int RemoveAttributeNode(long /*int*/ oldAttr, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 13 : 7), getAddress(), oldAttr, _retval);
	}

	public int GetElementsByTagName(long /*int*/ name, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 16 : 8), getAddress(), name, _retval);
	}

	public int GetAttributeNS(long /*int*/ namespaceURI, long /*int*/ localName, long /*int*/ _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 4 : 9), getAddress(), namespaceURI, localName, _retval);
	}

	public int SetAttributeNS(long /*int*/ namespaceURI, long /*int*/ qualifiedName, long /*int*/ value) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 6 : 10), getAddress(), namespaceURI, qualifiedName, value);
	}

	public int RemoveAttributeNS(long /*int*/ namespaceURI, long /*int*/ localName) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 8 : 11), getAddress(), namespaceURI, localName);
	}

	public int GetAttributeNodeNS(long /*int*/ namespaceURI, long /*int*/ localName, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 14 : 12), getAddress(), namespaceURI, localName, _retval);
	}

	public int SetAttributeNodeNS(long /*int*/ newAttr, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 15 : 13), getAddress(), newAttr, _retval);
	}

	public int GetElementsByTagNameNS(long /*int*/ namespaceURI, long /*int*/ localName, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 17 : 14), getAddress(), namespaceURI, localName, _retval);
	}

	public int HasAttribute(long /*int*/ name, int[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 9 : 15), getAddress(), name, _retval);
	}

	public int HasAttributeNS(long /*int*/ namespaceURI, long /*int*/ localName, int[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 10 : 16), getAddress(), namespaceURI, localName, _retval);
	}

	public int GetOnmouseenter(long /*int*/ cx, long /*int*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 25 : 17), getAddress(), cx, aOnmouseenter);
	}

	public int SetOnmouseenter(long /*int*/ cx, long /*int*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 26 : 18), getAddress(), cx, aOnmouseenter);
	}

	public int GetOnmouseleave(long /*int*/ cx, long /*int*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 27 : 19), getAddress(), cx, aOnmouseleave);
	}

	public int SetOnmouseleave(long /*int*/ cx, long /*int*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 28 : 20), getAddress(), cx, aOnmouseleave);
	}
}
