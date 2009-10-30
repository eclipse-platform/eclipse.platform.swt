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
 * -  Copyright (C) 2003, 2008 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIPromptService2 extends nsIPromptService {

	static final int LAST_METHOD_ID = nsIPromptService.LAST_METHOD_ID + 2;

	public static final String NS_IPROMPTSERVICE2_IID_STR =
		"cf86d196-dbee-4482-9dfa-3477aa128319";

	public static final nsID NS_IPROMPTSERVICE2_IID =
		new nsID(NS_IPROMPTSERVICE2_IID_STR);

	public nsIPromptService2(int /*long*/ address) {
		super(address);
	}

	public int PromptAuth(int /*long*/ aParent, int /*long*/ aChannel, int level, int /*long*/ authInfo, char[] checkboxLabel, int[] checkValue, int[] _retval) {
		return XPCOM.VtblCall(nsIPromptService.LAST_METHOD_ID + 1, getAddress(), aParent, aChannel, level, authInfo, checkboxLabel, checkValue, _retval);
	}

	public int AsyncPromptAuth(int /*long*/ aParent, int /*long*/ aChannel, int /*long*/ aCallback, int /*long*/ aContext, int level, int /*long*/ authInfo, char[] checkboxLabel, int[] checkValue, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIPromptService.LAST_METHOD_ID + 2, getAddress(), aParent, aChannel, aCallback, aContext, level, authInfo, checkboxLabel, checkValue, _retval);
	}
}
