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
 * -  Copyright (C) 2012 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIAuthPrompt2 extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 2;

	public static final String NS_IAUTHPROMPT2_IID_STR =
		"651395eb-8612-4876-8ac0-a88d4dce9e1e";

	public static final nsID NS_IAUTHPROMPT2_IID =
		new nsID(NS_IAUTHPROMPT2_IID_STR);

	public nsIAuthPrompt2(long /*int*/ address) {
		super(address);
	}

	public static final int LEVEL_NONE = 0;
	public static final int LEVEL_PW_ENCRYPTED = 1;
	public static final int LEVEL_SECURE = 2;

	public int PromptAuth(long /*int*/ aParent, long /*int*/ aChannel, int level, long /*int*/ authInfo, char[] checkboxLabel, int[] checkValue, int[] _retval) {
		return XPCOM.VtblCall(nsIPromptService.LAST_METHOD_ID + 1, getAddress(), aParent, aChannel, level, authInfo, checkboxLabel, checkValue, _retval);
	}

	public int AsyncPromptAuth(long /*int*/ aParent, long /*int*/ aChannel, long /*int*/ aCallback, long /*int*/ aContext, int level, long /*int*/ authInfo, char[] checkboxLabel, int[] checkValue, long /*int*/[] _retval) {
		return XPCOM.VtblCall(nsIPromptService.LAST_METHOD_ID + 2, getAddress(), aParent, aChannel, aCallback, aContext, level, authInfo, checkboxLabel, checkValue, _retval);
	}
}
