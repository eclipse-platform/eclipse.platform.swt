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
 * -  Copyright (C) 2003 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIPromptService extends nsISupports {

	static final int LAST_METHOD_ID = nsISupports.LAST_METHOD_ID + 9;

	public static final String NS_IPROMPTSERVICE_IID_STRING =
		"1630C61A-325E-49ca-8759-A31B16C47AA5";

	public static final nsID NS_IPROMPTSERVICE_IID =
		new nsID(NS_IPROMPTSERVICE_IID_STRING);

	public nsIPromptService(int address) {
		super(address);
	}

	public int Alert(int parent, char[] dialogTitle, char[] text) {
		return XPCOM.VtblCall(super.LAST_METHOD_ID + 1, getAddress(), parent, dialogTitle, text);
	}

	public int AlertCheck(int parent, char[] dialogTitle, char[] text, char[] checkMsg, boolean[] checkValue) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int Confirm(int parent, char[] dialogTitle, char[] text, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int ConfirmCheck(int parent, char[] dialogTitle, char[] text, char[] checkMsg, boolean[] checkValue, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public static final int BUTTON_POS_0 = 1;

	public static final int BUTTON_POS_1 = 256;

	public static final int BUTTON_POS_2 = 65536;

	public static final int BUTTON_TITLE_OK = 1;

	public static final int BUTTON_TITLE_CANCEL = 2;

	public static final int BUTTON_TITLE_YES = 3;

	public static final int BUTTON_TITLE_NO = 4;

	public static final int BUTTON_TITLE_SAVE = 5;

	public static final int BUTTON_TITLE_DONT_SAVE = 6;

	public static final int BUTTON_TITLE_REVERT = 7;

	public static final int BUTTON_TITLE_IS_STRING = 127;

	public static final int STD_OK_CANCEL_BUTTONS = 513;

	public int ConfirmEx(int parent, char[] dialogTitle, char[] text, int buttonFlags, char[] button0Title, char[] button1Title, char[] button2Title, char[] checkMsg, boolean[] checkValue, int[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int Prompt(int parent, char[] dialogTitle, char[] text, int[] value, char[] checkMsg, boolean[] checkValue, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int PromptUsernameAndPassword(int parent, char[] dialogTitle, char[] text, int[] username, int[] password, char[] checkMsg, boolean[] checkValue, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int PromptPassword(int parent, char[] dialogTitle, char[] text, int[] password, char[] checkMsg, boolean[] checkValue, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}

	public int Select(int parent, char[] dialogTitle, char[] text, int count, char[] selectList, int[] outSelection, boolean[] _retval) {
		return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
	}
}