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

import org.eclipse.swt.internal.win32.*;

public class GRE {
	
	public static String grePath = null;
	public static String mozillaPath = null;

	/* Get the GRE and Mozilla directories from the windows registry */	
	static {
		String greVersion = getGREVersion();
		if (greVersion != null) grePath = getGRELocation(greVersion);
		String mozillaVersion = getMozillaVersion();
		mozillaPath = getMozillaLocation(mozillaVersion);
		if (grePath == null || mozillaPath == null) {
			System.out.println("Warning: SWT Mozilla requires Mozilla "+XPCOM.SUPPORTED_VERSION+" to be installed"); //$NON-NLS-1$ //$NON-NLS-2$
			if (grePath == null) System.out.println("Warning: could not find matching GRE"); //$NON-NLS-1$
			if (mozillaPath == null) System.out.println("Warning: could not find matching Mozilla"); //$NON-NLS-1$
		}
	}
	
	static String getGREVersion() {
		String version = null;
		TCHAR lpSubKey = new TCHAR(0, "Software\\mozilla.org\\GRE", true); //$NON-NLS-1$
		int[] phkResult = new int[1];
		if (OS.RegOpenKeyEx (OS.HKEY_LOCAL_MACHINE, lpSubKey, 0, OS.KEY_READ, phkResult) != 0) {
			return null;
		}
		int hKey = phkResult[0];
		int dwIndex = 0;
		TCHAR lpName = new TCHAR (0, 256);
		int[] lpcName = new int[] {lpName.length()};
		while (OS.RegEnumKeyEx (hKey, dwIndex++, lpName, lpcName, null, null, null, null) != OS.ERROR_NO_MORE_ITEMS) {
			version = lpName.toString(0, lpName.strlen());
			if (version.startsWith(XPCOM.SUPPORTED_VERSION)) break;
		}
		OS.RegCloseKey (hKey);
		return version;
	}

	static String getGRELocation(String greVersion) {
		String location = null;
		TCHAR kRegLocation = new TCHAR(0, "Software\\mozilla.org\\GRE\\" + greVersion, true); //$NON-NLS-1$
		int[] hRegKey = new int[1];
		if (OS.RegOpenKeyEx(OS.HKEY_LOCAL_MACHINE, kRegLocation, 0, OS.KEY_READ, hRegKey) != 0) {
			return null;
		}
		int[] lpcbData = {256};
		/* Use the character encoding for the default locale */
		TCHAR lpData = new TCHAR(0, lpcbData [0]);
		TCHAR lpValueName = new TCHAR(0, "GreHome", true); //$NON-NLS-1$
		if (OS.RegQueryValueEx(hRegKey[0], lpValueName, 0, null, lpData, lpcbData) == 0) {
			if (lpcbData[0] != 0) location = lpData.toString(0, lpData.strlen());
		}
		OS.RegCloseKey(hRegKey[0]);
		return location;
	}

	static String getMozillaVersion() {
		String version = null;
		TCHAR lpSubKey = new TCHAR(0, "Software\\mozilla.org\\Mozilla", true); //$NON-NLS-1$
		int[] phkResult = new int[1];
		if (OS.RegOpenKeyEx(OS.HKEY_LOCAL_MACHINE, lpSubKey, 0, OS.KEY_READ, phkResult) != 0) {
			return null;
		}
		int hKey = phkResult[0];
		int dwIndex = 0;
		int[] lpcName = {256};
		TCHAR lpName = new TCHAR(0, lpcName[0]);
		while (OS.RegEnumKeyEx(hKey, dwIndex++, lpName, lpcName, null, null, null, null) != OS.ERROR_NO_MORE_ITEMS) {
			if (lpcName[0] != 0) {
				version = lpName.toString(0, lpName.strlen());
				if (version != null && version.startsWith(XPCOM.SUPPORTED_VERSION)) break;
			}
		}
		OS.RegCloseKey (hKey);
		return version;
	}
	
	static String getMozillaLocation(String mozillaVersion) {
		String location = null;
		TCHAR kRegLocation = new TCHAR(0, "Software\\mozilla.org\\Mozilla\\" + mozillaVersion + "\\Main", true); //$NON-NLS-1$ //$NON-NLS-2$
		int[] phkResult = new int[1];
		if (OS.RegOpenKeyEx(OS.HKEY_LOCAL_MACHINE, kRegLocation, 0, OS.KEY_READ, phkResult) != 0) {
			return null;
		}
		int hKey = phkResult[0];
		int[] lpcbData = {256};
		/* Use the character encoding for the default locale */
		TCHAR lpData = new TCHAR (0, lpcbData [0]);
		TCHAR lpValueName = new TCHAR (0, "Install Directory", true); //$NON-NLS-1$
		if (OS.RegQueryValueEx(hKey, lpValueName, 0, null, lpData, lpcbData) == 0) {
			if (lpcbData[0] != 0) location = lpData.toString(0, lpData.strlen());
		}
		OS.RegCloseKey (hKey);
		return location;		
	}	
}