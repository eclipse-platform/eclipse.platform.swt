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

public class nsIDOMNode extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 36 : 25);

	public static final String NS_IDOMNODE_IID_STR =
		"a6cf907c-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMNODE_10_IID_STR =
		"ce82fb71-60f2-4c38-be31-de5f2f90dada";

	public static final nsID NS_IDOMNODE_IID =
		new nsID(NS_IDOMNODE_IID_STR);

	public static final nsID NS_IDOMNODE_10_IID =
		new nsID(NS_IDOMNODE_10_IID_STR);

	public nsIDOMNode(long /*int*/ address) {
		super(address);
	}

	public static final int ELEMENT_NODE = 1;
	public static final int ATTRIBUTE_NODE = 2;
	public static final int TEXT_NODE = 3;
	public static final int CDATA_SECTION_NODE = 4;
	public static final int ENTITY_REFERENCE_NODE = 5;
	public static final int ENTITY_NODE = 6;
	public static final int PROCESSING_INSTRUCTION_NODE = 7;
	public static final int COMMENT_NODE = 8;
	public static final int DOCUMENT_NODE = 9;
	public static final int DOCUMENT_TYPE_NODE = 10;
	public static final int DOCUMENT_FRAGMENT_NODE = 11;
	public static final int NOTATION_NODE = 12;

	public int GetNodeName(long /*int*/ aNodeName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 1, getAddress(), aNodeName);
	}

	public int GetNodeValue(long /*int*/ aNodeValue) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 2, getAddress(), aNodeValue);
	}

	public int SetNodeValue(long /*int*/ aNodeValue) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 3, getAddress(), aNodeValue);
	}

	public int GetNodeType(short[] aNodeType) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 4, getAddress(), aNodeType);
	}

	public int GetParentNode(long /*int*/[] aParentNode) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 5, getAddress(), aParentNode);
	}

	public int GetParentElement(long /*int*/[] aParentElement) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 6, getAddress(), aParentElement);
	}

	public int GetChildNodes(long /*int*/[] aChildNodes) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 7 : 6), getAddress(), aChildNodes);
	}

	public int GetFirstChild(long /*int*/[] aFirstChild) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 8 : 7), getAddress(), aFirstChild);
	}

	public int GetLastChild(long /*int*/[] aLastChild) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 9 : 8), getAddress(), aLastChild);
	}

	public int GetPreviousSibling(long /*int*/[] aPreviousSibling) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 10 : 9), getAddress(), aPreviousSibling);
	}

	public int GetNextSibling(long /*int*/[] aNextSibling) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 11 : 10), getAddress(), aNextSibling);
	}

	public int GetAttributes(long /*int*/[] aAttributes) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 12 : 11), getAddress(), aAttributes);
	}

	public int GetOwnerDocument(long /*int*/[] aOwnerDocument) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 13 : 12), getAddress(), aOwnerDocument);
	}

	public int InsertBefore(long /*int*/ newChild, long /*int*/ refChild, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 14 : 13), getAddress(), newChild, refChild, _retval);
	}

	public int ReplaceChild(long /*int*/ newChild, long /*int*/ oldChild, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 15 : 14), getAddress(), newChild, oldChild, _retval);
	}

	public int RemoveChild(long /*int*/ oldChild, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 16 : 15), getAddress(), oldChild, _retval);
	}

	public int AppendChild(long /*int*/ newChild, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 17 : 16), getAddress(), newChild, _retval);
	}

	public int HasChildNodes(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 18 : 17), getAddress(), _retval);
	}

	public int CloneNode(int deep, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 19 : 18), getAddress(), deep, _retval);
	}

	public int Normalize() {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 20 : 19), getAddress());
	}

	public int IsSupported(long /*int*/ feature, long /*int*/ version, int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 21 : 20), getAddress(), feature, version, _retval);
	}

	public int GetNamespaceURI(long /*int*/ aNamespaceURI) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 22 : 21), getAddress(), aNamespaceURI);
	}

	public int GetPrefix(long /*int*/ aPrefix) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + (IsXULRunner10 ? 23 : 22), getAddress(), aPrefix);
	}

	public int SetPrefix(long /*int*/ aPrefix) {
		if (IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 23, getAddress(), aPrefix);
	}

	public int GetLocalName(long /*int*/ aLocalName) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 24, getAddress(), aLocalName);
	}

	public int HasAttributes(int[] _retval) {
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 25, getAddress(), _retval);
	}
	
	public int GetDOMBaseURI(long /*int*/ aBaseURI) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 26, getAddress(), aBaseURI);
	}

	/* the following constants are defined in Mozilla 10 */
	public static final int DOCUMENT_POSITION_DISCONNECTED = 1;
	public static final int DOCUMENT_POSITION_PRECEDING = 2;
	public static final int DOCUMENT_POSITION_FOLLOWING = 4;
	public static final int DOCUMENT_POSITION_CONTAINS = 8;
	public static final int DOCUMENT_POSITION_CONTAINED_BY = 16;
	public static final int DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;

	public int CompareDocumentPosition(long /*int*/ other, short[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 27, getAddress(), other, _retval);
	}

	public int GetTextContent(long /*int*/ aTextContent) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 28, getAddress(), aTextContent);
	}

	public int SetTextContent(long /*int*/ aTextContent) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 29, getAddress(), aTextContent);
	}

	public int LookupPrefix(long /*int*/ namespaceURI, long /*int*/ _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 30, getAddress(), namespaceURI, _retval);
	}

	public int IsDefaultNamespace(long /*int*/ namespaceURI, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 31, getAddress(), namespaceURI, _retval);
	}

	public int LookupNamespaceURI(long /*int*/ prefix, long /*int*/ _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 32, getAddress(), prefix, _retval);
	}

	public int IsEqualNode(long /*int*/ arg, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 33, getAddress(), arg, _retval);
	}

	public int SetUserData(long /*int*/ key, long /*int*/ data, long /*int*/ handler, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 34, getAddress(), key, data, handler, _retval);
	}

	public int GetUserData(long /*int*/ key, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 35, getAddress(), key, _retval);
	}

	public int Contains(long /*int*/ aOther, int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsISupports.LAST_METHOD_ID + 36, getAddress(), aOther, _retval);
	}
}
