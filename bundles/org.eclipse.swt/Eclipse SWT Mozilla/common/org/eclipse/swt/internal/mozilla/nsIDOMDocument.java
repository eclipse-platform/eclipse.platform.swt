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
 * -  Copyright (C) 2011, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;


public class nsIDOMDocument extends nsIDOMNode {

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + (IsXULRunner31 () ? 64 : (IsXULRunner24() ? 68 : (IsXULRunner10() ? 61 : 17)));

	static final String NS_IDOMDOCUMENT_IID_STR = "a6cf9075-15b3-11d2-932e-00805f8add32";
	static final String NS_IDOMDOCUMENT_10_IID_STR = "5c3bff4d-ae7f-4c93-948c-519589672c30";
	static final String NS_IDOMDOCUMENT_24_IID_STR = "75996de6-6b0f-43e5-ae79-c98fa669da9a";
	static final String NS_IDOMDOCUMENT_31_IID_STR = "d24d1118-a527-4d5a-9c4e-fb07dfc2fc27";

	static {
		IIDStore.RegisterIID(nsIDOMDocument.class, MozillaVersion.VERSION_BASE, new nsID(NS_IDOMDOCUMENT_IID_STR));
		IIDStore.RegisterIID(nsIDOMDocument.class, MozillaVersion.VERSION_XR10, new nsID(NS_IDOMDOCUMENT_10_IID_STR));
		IIDStore.RegisterIID(nsIDOMDocument.class, MozillaVersion.VERSION_XR24, new nsID(NS_IDOMDOCUMENT_24_IID_STR));
		IIDStore.RegisterIID(nsIDOMDocument.class, MozillaVersion.VERSION_XR31, new nsID(NS_IDOMDOCUMENT_31_IID_STR));
	}

	public nsIDOMDocument(long /*int*/ address) {
		super(address);
	}

	public int GetDocumentElement(long /*int*/[] aDocumentElement) {
		return XPCOM.VtblCall(this.getGetterIndex("documentElement"), getAddress(), aDocumentElement);
	}
}
