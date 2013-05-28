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

public class nsIDOMNode extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + ((IsXULRunner10 || IsXULRunner17) ? 36 : 25);

	public static final String NS_IDOMNODE_IID_STR =
		"a6cf907c-15b3-11d2-932e-00805f8add32";

	public static final String NS_IDOMNODE_10_IID_STR =
		"ce82fb71-60f2-4c38-be31-de5f2f90dada";
	
	public static final String NS_IDOMNODE_17_IID_STR =
		"5e9bcec9-5928-4f77-8a9c-424ef01c20e1";

	public static final nsID NS_IDOMNODE_IID =
		new nsID(NS_IDOMNODE_IID_STR);

	public static final nsID NS_IDOMNODE_10_IID =
		new nsID(NS_IDOMNODE_10_IID_STR);

	public static final nsID NS_IDOMNODE_17_IID =
		new nsID(NS_IDOMNODE_17_IID_STR);

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

	/* the following constants are available as of Mozilla 10 */
	public static final int DOCUMENT_POSITION_DISCONNECTED = 1;
	public static final int DOCUMENT_POSITION_PRECEDING = 2;
	public static final int DOCUMENT_POSITION_FOLLOWING = 4;
	public static final int DOCUMENT_POSITION_CONTAINS = 8;
	public static final int DOCUMENT_POSITION_CONTAINED_BY = 16;
	public static final int DOCUMENT_POSITION_IMPLEMENTATION_SPECIFIC = 32;
}
