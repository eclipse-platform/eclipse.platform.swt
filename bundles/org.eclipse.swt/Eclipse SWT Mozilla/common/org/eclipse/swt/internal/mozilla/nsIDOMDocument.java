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
 * -  Copyright (C) 2011 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIDOMDocument extends nsIDOMNode {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + 17;

	public static final String NS_IDOMDOCUMENT_IID_STR =
		"a6cf9075-15b3-11d2-932e-00805f8add32";

	public static final nsID NS_IDOMDOCUMENT_IID =
		new nsID(NS_IDOMDOCUMENT_IID_STR);

	public nsIDOMDocument(int /*long*/ address) {
		super(address);
	}

	public int GetDoctype(int /*long*/[] aDoctype) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 1, getAddress(), aDoctype);
	}

	public int GetImplementation(int /*long*/[] aImplementation) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 2, getAddress(), aImplementation);
	}

	public int GetDocumentElement(int /*long*/[] aDocumentElement) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 3, getAddress(), aDocumentElement);
	}

	public int CreateElement(int /*long*/ tagName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 4, getAddress(), tagName, _retval);
	}

	public int CreateDocumentFragment(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 5, getAddress(), _retval);
	}

	public int CreateTextNode(int /*long*/ data, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 6, getAddress(), data, _retval);
	}

	public int CreateComment(int /*long*/ data, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 7, getAddress(), data, _retval);
	}

	public int CreateCDATASection(int /*long*/ data, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 8, getAddress(), data, _retval);
	}

	public int CreateProcessingInstruction(int /*long*/ target, int /*long*/ data, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 9, getAddress(), target, data, _retval);
	}

	public int CreateAttribute(int /*long*/ name, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 10, getAddress(), name, _retval);
	}

	public int CreateEntityReference(int /*long*/ name, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 11, getAddress(), name, _retval);
	}

	public int GetElementsByTagName(int /*long*/ tagname, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 12, getAddress(), tagname, _retval);
	}

	public int ImportNode(int /*long*/ importedNode, int deep, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 13, getAddress(), importedNode, deep, _retval);
	}

	public int CreateElementNS(int /*long*/ namespaceURI, int /*long*/ qualifiedName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 14, getAddress(), namespaceURI, qualifiedName, _retval);
	}

	public int CreateAttributeNS(int /*long*/ namespaceURI, int /*long*/ qualifiedName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 15, getAddress(), namespaceURI, qualifiedName, _retval);
	}

	public int GetElementsByTagNameNS(int /*long*/ namespaceURI, int /*long*/ localName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 16, getAddress(), namespaceURI, localName, _retval);
	}

	public int GetElementById(int /*long*/ elementId, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 17, getAddress(), elementId, _retval);
	}
}
