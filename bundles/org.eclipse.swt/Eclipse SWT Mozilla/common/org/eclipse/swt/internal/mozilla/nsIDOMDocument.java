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

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 61 : 17);

	public static final String NS_IDOMDOCUMENT_IID_STR =
		"a6cf9075-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMDOCUMENT_10_IID_STR =
		"5c3bff4d-ae7f-4c93-948c-519589672c30";

	public static final nsID NS_IDOMDOCUMENT_IID =
		new nsID(NS_IDOMDOCUMENT_IID_STR);

	public static final nsID NS_IDOMDOCUMENT_10_IID =
		new nsID(NS_IDOMDOCUMENT_10_IID_STR);

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
		if (IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 11, getAddress(), name, _retval);
	}

	public int GetElementsByTagName(int /*long*/ tagname, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 11 : 12), getAddress(), tagname, _retval);
	}

	public int ImportNode(int /*long*/ importedNode, int deep, int /*long*/[] _retval) {
		if (IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 13, getAddress(), importedNode, deep, _retval);
	}

	public int ImportNode(int /*long*/ importedNode, int deep, int _argc, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 12, getAddress(), importedNode, deep, _argc, _retval);
	}

	public int CreateElementNS(int /*long*/ namespaceURI, int /*long*/ qualifiedName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 13 : 14), getAddress(), namespaceURI, qualifiedName, _retval);
	}

	public int CreateAttributeNS(int /*long*/ namespaceURI, int /*long*/ qualifiedName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 14 : 15), getAddress(), namespaceURI, qualifiedName, _retval);
	}

	public int GetElementsByTagNameNS(int /*long*/ namespaceURI, int /*long*/ localName, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 15 : 16), getAddress(), namespaceURI, localName, _retval);
	}

	public int GetElementById(int /*long*/ elementId, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + (IsXULRunner10 ? 16 : 17), getAddress(), elementId, _retval);
	}
	
	public int GetInputEncoding(int /*long*/ aInputEncoding) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 17, getAddress(), aInputEncoding);
	}

	public int GetDocumentURI(int /*long*/ aDocumentURI) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 18, getAddress(), aDocumentURI);
	}

	public int AdoptNode(int /*long*/ source, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 19, getAddress(), source, _retval);
	}

	public int CreateRange(int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 20, getAddress(), _retval);
	}

	public int CreateNodeIterator(int /*long*/ root, int whatToShow, int /*long*/ filter, int entityReferenceExpansion, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 21, getAddress(), root, whatToShow, filter, entityReferenceExpansion, _retval);
	}

	public int CreateTreeWalker(int /*long*/ root, int whatToShow, int /*long*/ filter, int entityReferenceExpansion, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 22, getAddress(), root, whatToShow, filter, entityReferenceExpansion, _retval);
	}

	public int CreateEvent(int /*long*/ eventType, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 23, getAddress(), eventType, _retval);
	}

	public int GetDefaultView(int /*long*/[] aDefaultView) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 24, getAddress(), aDefaultView);
	}

	public int GetCharacterSet(int /*long*/ aCharacterSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 25, getAddress(), aCharacterSet);
	}

	public int GetDir(int /*long*/ aDir) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 26, getAddress(), aDir);
	}

	public int SetDir(int /*long*/ aDir) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 27, getAddress(), aDir);
	}

	public int GetLocation(int /*long*/[] aLocation) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 28, getAddress(), aLocation);
	}

	public int GetTitle(int /*long*/ aTitle) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 29, getAddress(), aTitle);
	}

	public int SetTitle(int /*long*/ aTitle) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 30, getAddress(), aTitle);
	}

	public int GetReadyState(int /*long*/ aReadyState) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 31, getAddress(), aReadyState);
	}

	public int GetLastModified(int /*long*/ aLastModified) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 32, getAddress(), aLastModified);
	}

	public int GetReferrer(int /*long*/ aReferrer) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 33, getAddress(), aReferrer);
	}

	public int HasFocus(int[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 34, getAddress(), _retval);
	}

	public int GetActiveElement(int /*long*/[] aActiveElement) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 35, getAddress(), aActiveElement);
	}

	public int GetElementsByClassName(int /*long*/ classes, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 36, getAddress(), classes, _retval);
	}

	public int GetStyleSheets(int /*long*/[] aStyleSheets) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 37, getAddress(), aStyleSheets);
	}

	public int GetPreferredStyleSheetSet(int /*long*/ aPreferredStyleSheetSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 38, getAddress(), aPreferredStyleSheetSet);
	}

	public int GetSelectedStyleSheetSet(int /*long*/ aSelectedStyleSheetSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 39, getAddress(), aSelectedStyleSheetSet);
	}

	public int SetSelectedStyleSheetSet(int /*long*/ aSelectedStyleSheetSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 40, getAddress(), aSelectedStyleSheetSet);
	}

	public int GetLastStyleSheetSet(int /*long*/ aLastStyleSheetSet) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 41, getAddress(), aLastStyleSheetSet);
	}

	public int GetStyleSheetSets(int /*long*/[] aStyleSheetSets) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 42, getAddress(), aStyleSheetSets);
	}

	public int EnableStyleSheetsForSet(int /*long*/ name) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 43, getAddress(), name);
	}

	public int ElementFromPoint(float x, float y, int /*long*/[] _retval) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 44, getAddress(), x, y, _retval);
	}

	public int GetContentType(int /*long*/ aContentType) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 45, getAddress(), aContentType);
	}

	public int GetMozSyntheticDocument(int[] aMozSyntheticDocument) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 46, getAddress(), aMozSyntheticDocument);
	}

	public int GetCurrentScript(int /*long*/[] aCurrentScript) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 47, getAddress(), aCurrentScript);
	}

	public int ReleaseCapture() {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 48, getAddress());
	}

	public int MozSetImageElement(int /*long*/ aImageElementId, int /*long*/ aImageElement) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 49, getAddress(), aImageElementId, aImageElement);
	}

	public int GetMozFullScreenElement(int /*long*/[] aMozFullScreenElement) {
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

	public int GetOnreadystatechange(int /*long*/ cx, int /*long*/ aOnreadystatechange) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 54, getAddress(), cx, aOnreadystatechange);
	}

	public int SetOnreadystatechange(int /*long*/ cx, int /*long*/ aOnreadystatechange) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 55, getAddress(), cx, aOnreadystatechange);
	}

	public int GetOnmouseenter(int /*long*/ cx, int /*long*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 56, getAddress(), cx, aOnmouseenter);
	}

	public int SetOnmouseenter(int /*long*/ cx, int /*long*/ aOnmouseenter) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 57, getAddress(), cx, aOnmouseenter);
	}

	public int GetOnmouseleave(int /*long*/ cx, int /*long*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 58, getAddress(), cx, aOnmouseleave);
	}

	public int SetOnmouseleave(int /*long*/ cx, int /*long*/ aOnmouseleave) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 59, getAddress(), cx, aOnmouseleave);
	}

	public int GetMozHidden(int[] aMozHidden) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 60, getAddress(), aMozHidden);
	}

	public int GetMozVisibilityState(int /*long*/ aMozVisibilityState) {
		if (!IsXULRunner10) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 61, getAddress(), aMozVisibilityState);
	}
}
