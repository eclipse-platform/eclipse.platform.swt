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
 * -  Copyright (C) 2003, 2009 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

public class nsIScriptSecurityManager extends nsIXPCSecurityManager {

	static final int LAST_METHOD_ID = nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 27 : 26);

	public static final String NS_ISCRIPTSECURITYMANAGER_IID_STR =
		"3fffd8e8-3fea-442e-a0ed-2ba81ae197d5";

	public static final nsID NS_ISCRIPTSECURITYMANAGER_IID =
		new nsID(NS_ISCRIPTSECURITYMANAGER_IID_STR);

	public static final String NS_ISCRIPTSECURITYMANAGER_191_IID_STR =
		"f8e350b9-9f31-451a-8c8f-d10fea26b780";

	public static final nsID NS_ISCRIPTSECURITYMANAGER_191_IID =
		new nsID(NS_ISCRIPTSECURITYMANAGER_191_IID_STR);

	public static final String NS_ISCRIPTSECURITYMANAGER_8_IID_STR =
		"50eda256-4dd2-4c7c-baed-96983910af9f";

	public static final nsID NS_ISCRIPTSECURITYMANAGER_8_IID =
		new nsID(NS_ISCRIPTSECURITYMANAGER_8_IID_STR);

	public nsIScriptSecurityManager(int /*long*/ address) {
		super(address);
	}

//	public int CheckPropertyAccess(int /*long*/ aJSContext, int /*long*/ aJSObject, byte[] aClassName, !ERROR UNKNOWN C TYPE <jsval >! aProperty, int aAction) {
//		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + 1, getAddress(), aJSContext, aJSObject, aClassName, aProperty, aAction);
//	}

	public int CheckConnect(int /*long*/ aJSContext, int /*long*/ aTargetURI, byte[] aClassName, byte[] aProperty) {
		if (Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + 2, getAddress(), aJSContext, aTargetURI, aClassName, aProperty);
	}

	public int CheckLoadURIFromScript(int /*long*/ cx, int /*long*/ uri) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 2 : 3), getAddress(), cx, uri);
	}

	public static final int STANDARD = 0;
	public static final int LOAD_IS_AUTOMATIC_DOCUMENT_REPLACEMENT = 1;
	public static final int ALLOW_CHROME = 2;
	public static final int DISALLOW_INHERIT_PRINCIPAL = 4;
	public static final int DISALLOW_SCRIPT_OR_DATA = 4;
	public static final int DISALLOW_SCRIPT = 8;

	public int CheckLoadURIWithPrincipal(int /*long*/ aPrincipal, int /*long*/ uri, int flags) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 3 : 4), getAddress(), aPrincipal, uri, flags);
	}

	public int CheckLoadURI(int /*long*/ from, int /*long*/ uri, int flags) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 4 : 5), getAddress(), from, uri, flags);
	}

	public int CheckLoadURIStrWithPrincipal(int /*long*/ aPrincipal, int /*long*/ uri, int flags) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 5 : 6), getAddress(), aPrincipal, uri, flags);
	}

	public int CheckLoadURIStr(int /*long*/ from, int /*long*/ uri, int flags) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 6 : 7), getAddress(), from, uri, flags);
	}

	public int CheckFunctionAccess(int /*long*/ cx, int /*long*/ funObj, int /*long*/ targetObj) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 7 : 8), getAddress(), cx, funObj, targetObj);
	}

	public int CanExecuteScripts(int /*long*/ cx, int /*long*/ principal, int[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 8 : 9), getAddress(), cx, principal, _retval);
	}

	public int GetSubjectPrincipal(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 9 : 10), getAddress(), _retval);
	}

	public int GetSystemPrincipal(int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 10 : 11), getAddress(), _retval);
	}

//	public int GetCertificatePrincipal(int /*long*/ aCertFingerprint, int /*long*/ aSubjectName, int /*long*/ aPrettyName, int /*long*/ aCert, int /*long*/ aURI, int /*long*/[] _retval) {
//		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 11 : 12), getAddress(), aCertFingerprint, aSubjectName, aPrettyName, aCert, aURI, _retval);
//	}

	public int GetCodebasePrincipal(int /*long*/ aURI, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 12 : 13), getAddress(), aURI, _retval);
	}

//	public int RequestCapability(int /*long*/ principal, byte[] capability, int /*long*/ _retval) {
//		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 13 : 14), getAddress(), principal, capability, _retval);
//	}

	public int IsCapabilityEnabled(byte[] capability, int[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 14 : 15), getAddress(), capability, _retval);
	}
	
	public int EnableCapability(byte[] capability) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 15 : 16), getAddress(), capability);
	}
	
	public int RevertCapability(byte[] capability) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 16 : 17), getAddress(), capability);
	}
	
	public int DisableCapability(byte[] capability) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 17 : 18), getAddress(), capability);
	}

//	public int SetCanEnableCapability(int /*long*/ certificateFingerprint, byte[] capability, !ERROR UNKNOWN C TYPE <PRInt16 >! canEnable) {
//		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 18 : 19), getAddress(), certificateFingerprint, capability, canEnable);
//	}

	public int GetObjectPrincipal(int /*long*/ cx, int /*long*/ obj, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 19 : 20), getAddress(), cx, obj, _retval);
	}
	
	public int SubjectPrincipalIsSystem(int[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 20 : 21), getAddress(), _retval);
	}
	
	public int CheckSameOrigin(int /*long*/ aJSContext, int /*long*/ aTargetURI) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 21 : 22), getAddress(), aJSContext, aTargetURI);
	}
	
	public int CheckSameOriginURI(int /*long*/ aSourceURI, int /*long*/ aTargetURI, int reportError) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 22 : 23), getAddress(), aSourceURI, aTargetURI, reportError);
	}
	
	public int GetPrincipalFromContext(int /*long*/ cx, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 23 : 24), getAddress(), cx, _retval);
	}
	
	public int GetChannelPrincipal(int /*long*/ aChannel, int /*long*/[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 24 : 25), getAddress(), aChannel, _retval);
	}
	
	public int IsSystemPrincipal(int /*long*/ aPrincipal, int[] _retval) {
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + (Is8 ? 25 : 26), getAddress(), aPrincipal, _retval);
	}
	
	public int PushContextPrincipal(int /*long*/ cx, int /*long*/ fp, int /*long*/ principal) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + 26, getAddress(), cx, fp, principal);
	}
	
	public int PopContextPrincipal(int /*long*/ cx) {
		if (!Is8) return XPCOM.NS_COMFALSE;
		return XPCOM.VtblCall(nsIXPCSecurityManager.LAST_METHOD_ID + 27, getAddress(), cx);
	}
}
