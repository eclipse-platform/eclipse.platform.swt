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

import java.io.*;

public class GRE {
	
	public static String grePath = null;
	public static String mozillaPath = null;
	
	static {
		String gre = "/etc/gre.conf"; //$NON-NLS-1$
		FileReader file = null;
		BufferedReader reader = null;
		try {
			file = new FileReader(gre);
			reader = new BufferedReader(file);
			String version = reader.readLine();
			int major = -1, minor = -1;
			try {
				/* the version line is formatted as follow: [major.minor] or [major.minor.subminor] */
				int sep = version.indexOf('.'); //$NON-NLS-1$
				String majorVersion = version.substring(version.indexOf('[') + 1, sep); //$NON-NLS-1$
				String minorVersion = version.substring(sep + 1, version.indexOf(']')); //$NON-NLS-1$
				sep = minorVersion.indexOf('.'); //$NON-NLS-1$
				if (sep != -1) minorVersion = minorVersion.substring(0, sep);
				major = Integer.parseInt(majorVersion);
				minor = Integer.parseInt(minorVersion);
			} catch (IndexOutOfBoundsException e) {
			} catch (NumberFormatException e) {
			}
			if ((major == XPCOM.MAJOR && minor >= XPCOM.MINOR) || major > XPCOM.MAJOR) {
				String string = reader.readLine();
				/* the location line is formatted as follow: GRE_PATH=<path> */
				String GRE_PATH = "GRE_PATH="; //$NON-NLS-1$
				if (string.startsWith(GRE_PATH)) {
					String path = string.substring(GRE_PATH.length());
					grePath = path;
				}
				if (grePath == null) {
					System.out.println("Warning: SWT Mozilla requires Mozilla "+XPCOM.SUPPORTED_VERSION+" (or greater) to be installed"); //$NON-NLS-1$ //$NON-NLS-2$
					System.out.println("Warning: The Mozilla configuration file "+gre+" does not contain the property "+GRE_PATH); //$NON-NLS-1$ //$NON-NLS-2$									
				}
				mozillaPath = grePath;
			} else {
				System.out.println("Warning: SWT Mozilla requires Mozilla "+XPCOM.SUPPORTED_VERSION+" (or greater) to be installed"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
				System.out.println("Warning: The Mozilla configuration file "+gre+" indicates version "+version+" is installed"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$				
			}			
		} catch (FileNotFoundException e) {
			System.out.println("Warning: SWT Mozilla requires Mozilla "+XPCOM.SUPPORTED_VERSION+" to be installed"); //$NON-NLS-1$ //$NON-NLS-2$
			System.out.println("Warning: The Mozilla configuration file "+gre+" was not found"); //$NON-NLS-1$ //$NON-NLS-2$
		} catch (IOException e) {
			System.out.println("Warning: SWT Mozilla requires Mozilla "+XPCOM.SUPPORTED_VERSION+" to be installed"); //$NON-NLS-1$ //$NON-NLS-2$
			System.out.println("Warning: The Mozilla configuration file "+gre+" could not be parsed"); //$NON-NLS-1$ //$NON-NLS-2$	
		} finally {
			try {
				if (reader != null) reader.close();
			} catch (IOException e) {
			}
		}
	}
}
