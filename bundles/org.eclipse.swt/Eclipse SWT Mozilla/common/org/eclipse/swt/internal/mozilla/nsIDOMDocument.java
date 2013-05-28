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

	static final int LAST_METHOD_ID = nsIDOMNode.LAST_METHOD_ID + (IsXULRunner17 ? 63 : (IsXULRunner10 ? 61 : 17));

	public static final String NS_IDOMDOCUMENT_IID_STR =
		"a6cf9075-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMDOCUMENT_10_IID_STR =
		"5c3bff4d-ae7f-4c93-948c-519589672c30";
	
	public static final String NS_IDOMDOCUMENT_17_IID_STR =
		"fdb92f4f-c6b4-4509-a29d-a309981e28ac";

	public static final nsID NS_IDOMDOCUMENT_IID =
		new nsID(NS_IDOMDOCUMENT_IID_STR);

	public static final nsID NS_IDOMDOCUMENT_10_IID =
		new nsID(NS_IDOMDOCUMENT_10_IID_STR);

	public static final nsID NS_IDOMDOCUMENT_17_IID =
		new nsID(NS_IDOMDOCUMENT_17_IID_STR);

	public nsIDOMDocument(long /*int*/ address) {
		super(address);
	}

	public int GetDocumentElement(long /*int*/[] aDocumentElement) {
		return XPCOM.VtblCall(nsIDOMNode.LAST_METHOD_ID + 3, getAddress(), aDocumentElement);
	}
}
