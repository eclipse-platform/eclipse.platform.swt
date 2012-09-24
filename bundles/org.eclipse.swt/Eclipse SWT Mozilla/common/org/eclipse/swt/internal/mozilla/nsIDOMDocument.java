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

public class nsIDOMDocument extends nsIDOMNode {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 61 : 17);

	public static final String NS_IDOMDOCUMENT_IID_STR =
		"a6cf9075-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMDOCUMENT_10_IID_STR =
		"5c3bff4d-ae7f-4c93-948c-519589672c30";

	public static final nsID NS_IDOMDOCUMENT_IID =
		new nsID(NS_IDOMDOCUMENT_IID_STR);

	public static final nsID NS_IDOMDOCUMENT_10_IID =
		new nsID(NS_IDOMDOCUMENT_10_IID_STR);

	public nsIDOMDocument(long /*int*/ address) {
		super(address);
	}

	public int GetDoctype(long /*int*/[] aDoctype) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 1, getAddress(), aDoctype);
	}

	public int GetImplementation(long /*int*/[] aImplementation) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 2, getAddress(), aImplementation);
	}

	public int GetDocumentElement(long /*int*/[] aDocumentElement) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 3, getAddress(), aDocumentElement);
	}

	public int CreateElement(long /*int*/ tagName, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 4, getAddress(), tagName, _retval);
	}

	public int CreateDocumentFragment(long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 5, getAddress(), _retval);
	}

	public int CreateTextNode(long /*int*/ data, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 6, getAddress(), data, _retval);
	}

	public int CreateComment(long /*int*/ data, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 7, getAddress(), data, _retval);
	}

	public int CreateCDATASection(long /*int*/ data, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 8, getAddress(), data, _retval);
	}

	public int CreateProcessingInstruction(long /*int*/ target, long /*int*/ data, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 9, getAddress(), target, data, _retval);
	}

	public int CreateAttribute(long /*int*/ name, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 10, getAddress(), name, _retval);
	}

	public int CreateEntityReference(long /*int*/ name, long /*int*/[] _retval) {
		if (IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 11, getAddress(), name, _retval);
	}

	public int GetElementsByTagName(long /*int*/ tagname, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 11 : 12), getAddress(), tagname, _retval);
	}

	public int ImportNode(long /*int*/ importedNode, int deep, long /*int*/[] _retval) {
		if (IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 13, getAddress(), importedNode, deep, _retval);
	}

	public int ImportNode(long /*int*/ importedNode, int deep, int _argc, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 12, getAddress(), importedNode, deep, _argc, _retval);
	}

	public int CreateElementNS(long /*int*/ namespaceURI, long /*int*/ qualifiedName, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 13 : 14), getAddress(), namespaceURI, qualifiedName, _retval);
	}

	public int CreateAttributeNS(long /*int*/ namespaceURI, long /*int*/ qualifiedName, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 14 : 15), getAddress(), namespaceURI, qualifiedName, _retval);
	}

	public int GetElementsByTagNameNS(long /*int*/ namespaceURI, long /*int*/ localName, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 15 : 16), getAddress(), namespaceURI, localName, _retval);
	}

	public int GetElementById(long /*int*/ elementId, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 16 : 17), getAddress(), elementId, _retval);
	}
	
	public int GetInputEncoding(long /*int*/ aInputEncoding) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 17, getAddress(), aInputEncoding);
	}

	public int GetDocumentURI(long /*int*/ aDocumentURI) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 18, getAddress(), aDocumentURI);
	}

	public int AdoptNode(long /*int*/ source, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 19, getAddress(), source, _retval);
	}

	public int CreateRange(long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 20, getAddress(), _retval);
	}

	public int CreateNodeIterator(long /*int*/ root, int whatToShow, long /*int*/ filter, int entityReferenceExpansion, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 21, getAddress(), root, whatToShow, filter, entityReferenceExpansion, _retval);
	}

	public int CreateTreeWalker(long /*int*/ root, int whatToShow, long /*int*/ filter, int entityReferenceExpansion, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 22, getAddress(), root, whatToShow, filter, entityReferenceExpansion, _retval);
	}

	public int CreateEvent(long /*int*/ eventType, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 23, getAddress(), eventType, _retval);
	}

	public int GetDefaultView(long /*int*/[] aDefaultView) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 24, getAddress(), aDefaultView);
	}

	public int GetCharacterSet(long /*int*/ aCharacterSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 25, getAddress(), aCharacterSet);
	}

	public int GetDir(long /*int*/ aDir) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 26, getAddress(), aDir);
	}

	public int SetDir(long /*int*/ aDir) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 27, getAddress(), aDir);
	}

	public int GetLocation(long /*int*/[] aLocation) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 28, getAddress(), aLocation);
	}

	public int GetTitle(long /*int*/ aTitle) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 29, getAddress(), aTitle);
	}

	public int SetTitle(long /*int*/ aTitle) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 30, getAddress(), aTitle);
	}

	public int GetReadyState(long /*int*/ aReadyState) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 31, getAddress(), aReadyState);
	}

	public int GetLastModified(long /*int*/ aLastModified) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 32, getAddress(), aLastModified);
	}

	public int GetReferrer(long /*int*/ aReferrer) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 33, getAddress(), aReferrer);
	}

	public int HasFocus(int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 34, getAddress(), _retval);
	}

	public int GetActiveElement(long /*int*/[] aActiveElement) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 35, getAddress(), aActiveElement);
	}

	public int GetElementsByClassName(long /*int*/ classes, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 36, getAddress(), classes, _retval);
	}

	public int GetStyleSheets(long /*int*/[] aStyleSheets) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 37, getAddress(), aStyleSheets);
	}

	public int GetPreferredStyleSheetSet(long /*int*/ aPreferredStyleSheetSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 38, getAddress(), aPreferredStyleSheetSet);
	}

	public int GetSelectedStyleSheetSet(long /*int*/ aSelectedStyleSheetSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 39, getAddress(), aSelectedStyleSheetSet);
	}

	public int SetSelectedStyleSheetSet(long /*int*/ aSelectedStyleSheetSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 40, getAddress(), aSelectedStyleSheetSet);
	}

	public int GetLastStyleSheetSet(long /*int*/ aLastStyleSheetSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 41, getAddress(), aLastStyleSheetSet);
	}

	public int GetStyleSheetSets(long /*int*/[] aStyleSheetSets) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 42, getAddress(), aStyleSheetSets);
	}

	public int EnableStyleSheetsForSet(long /*int*/ name) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 43, getAddress(), name);
	}

	public int ElementFromPoint(float x, float y, long /*int*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 44, getAddress(), x, y, _retval);
	}

	public int GetContentType(long /*int*/ aContentType) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 45, getAddress(), aContentType);
	}

	public int GetMozSyntheticDocument(int[] aMozSyntheticDocument) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 46, getAddress(), aMozSyntheticDocument);
	}

	public int GetCurrentScript(long /*int*/[] aCurrentScript) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 47, getAddress(), aCurrentScript);
	}

	public int ReleaseCapture() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 48, getAddress());
	}

	public int MozSetImageElement(long /*int*/ aImageElementId, long /*int*/ aImageElement) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 49, getAddress(), aImageElementId, aImageElement);
	}

	public int GetMozFullScreenElement(long /*int*/[] aMozFullScreenElement) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 50, getAddress(), aMozFullScreenElement);
	}

	public int MozCancelFullScreen() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 51, getAddress());
	}

	public int GetMozFullScreen(int[] aMozFullScreen) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 52, getAddress(), aMozFullScreen);
	}

	public int GetMozFullScreenEnabled(int[] aMozFullScreenEnabled) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 53, getAddress(), aMozFullScreenEnabled);
	}

	public int GetOnreadystatechange(long /*int*/ cx, long /*int*/ aOnreadystatechange) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 54, getAddress(), cx, aOnreadystatechange);
	}

	public int SetOnreadystatechange(long /*int*/ cx, long /*int*/ aOnreadystatechange) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 55, getAddress(), cx, aOnreadystatechange);
	}

	public int GetOnmouseenter(long /*int*/ cx, long /*int*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 56, getAddress(), cx, aOnmouseenter);
	}

	public int SetOnmouseenter(long /*int*/ cx, long /*int*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 57, getAddress(), cx, aOnmouseenter);
	}

	public int GetOnmouseleave(long /*int*/ cx, long /*int*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 58, getAddress(), cx, aOnmouseleave);
	}

	public int SetOnmouseleave(long /*int*/ cx, long /*int*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 59, getAddress(), cx, aOnmouseleave);
	}

	public int GetMozHidden(int[] aMozHidden) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 60, getAddress(), aMozHidden);
	}

	public int GetMozVisibilityState(long /*int*/ aMozVisibilityState) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 61, getAddress(), aMozVisibilityState);
	}
}
