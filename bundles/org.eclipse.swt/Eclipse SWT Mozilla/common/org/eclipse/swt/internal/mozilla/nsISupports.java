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
 * -  Copyright (C) 2003, 2013 IBM Corp.  All Rights Reserved.
 *
 * ***** END LICENSE BLOCK ***** */
package org.eclipse.swt.internal.mozilla;

import org.eclipse.swt.*;


public class nsISupports {

	static final boolean IsSolaris;
	static {
		String osName = System.getProperty ("os.name").toLowerCase (); //$NON-NLS-1$
		IsSolaris = osName.startsWith ("sunos") || osName.startsWith("solaris"); //$NON-NLS-1$
	}

	static final int FIRST_METHOD_ID = IsSolaris ? 2 : 0;
	static final int LAST_METHOD_ID = FIRST_METHOD_ID + 2;

	protected static boolean IsXULRunner10 () {
		return MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR10, true);
	}

	protected static boolean IsXULRunner24 () {
		return MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR24, true);
	}

	protected static boolean IsXULRunner31 () {
		return MozillaVersion.CheckVersion (MozillaVersion.VERSION_XR31, true);
	}

	protected static boolean IsXULRVersionOrLater (int version) {
		return MozillaVersion.CheckVersion (version, false);
	}

	public static final String NS_ISUPPORTS_IID_STR = "00000000-0000-0000-c000-000000000046";

	static {
		IIDStore.RegisterIID (nsISupports.class, MozillaVersion.VERSION_BASE, new nsID (NS_ISUPPORTS_IID_STR));
	}

	private static byte[] toByteArray (String str) {
		byte[] bytes = new byte[str.length() + 1];
		for (int i = str.length (); i-- > 0; )
			bytes[i] = (byte)str.charAt (i);
		return bytes;
	}

	protected int getGetterIndex (String attribute) {
		return getMethodIndex (attribute);
	}

	protected int getSetterIndex (String attribute) {
		return getMethodIndex (attribute) + 1;
	}

	protected String getClassName() {
		return getClass ().getSimpleName ();
	}

	protected int getMethodIndex (String methodString) {
		long /*int*/[] result = new long /*int*/[1];
		result[0] = 0;
		int rc = XPCOM.NS_GetServiceManager (result);
		if (rc != XPCOM.NS_OK) {
			throw new SWTError(rc);
		}

		nsIServiceManager serviceManager = new nsIServiceManager (result[0]);
		result[0] = 0;
		rc = serviceManager.GetServiceByContractID (toByteArray (XPCOM.NS_INTERFACEINFOMANAGER_CONTRACTID), IIDStore.GetIID (nsIInterfaceInfoManager.class), result);
		serviceManager.Release ();
		if (rc != XPCOM.NS_OK) {
			throw new SWTError(rc);
		}

		nsIInterfaceInfoManager iim = new nsIInterfaceInfoManager (result[0]);
		result[0] = 0;
		rc = iim.GetInfoForName (toByteArray (getClassName()), result);
		iim.Release ();
		if (rc != XPCOM.NS_OK) {
			throw new SWTError(rc);
		}

		nsIInterfaceInfo info = new nsIInterfaceInfo (result[0]);
		int[] index = new int [1];
		long /*int*/[] dummy = new long /*int*/[1];
		rc = info.GetMethodInfoForName (toByteArray (methodString), index, dummy);
		info.Release ();
		if (rc != XPCOM.NS_OK) {
			throw new SWTError(rc);
		}

		return index[0];
	}

	long /*int*/ address;

	public nsISupports(long /*int*/ address) {
		this.address = address;
	}

	public long /*int*/ getAddress() {
		return this.address;
	}

	public int QueryInterface(nsID uuid, long /*int*/[] result) {
		return XPCOM.VtblCall(FIRST_METHOD_ID, getAddress(), uuid, result);
	}

	public int AddRef() {
		return XPCOM.VtblCall(FIRST_METHOD_ID + 1, getAddress());
	}

	public int Release() {
		return XPCOM.VtblCall(FIRST_METHOD_ID + 2, getAddress());
	}
}
