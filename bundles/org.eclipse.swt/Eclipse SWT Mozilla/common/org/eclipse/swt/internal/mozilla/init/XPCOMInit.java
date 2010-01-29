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
package org.eclipse.swt.internal.mozilla.init;

import org.eclipse.swt.internal.Platform;

/** @jniclass flags=cpp */
public class XPCOMInit extends Platform {
	public static final int PATH_MAX = 4096;
	
public static final native int GREVersionRange_sizeof ();

/**
 * @param versions cast=(const GREVersionRange *)
 * @param properties cast=(const GREProperty *)
 * @param buffer cast=(char *)
 */
public static final native int _GRE_GetGREPathWithProperties (GREVersionRange versions, int versionsLength, int /*long*/ properties, int propertiesLength, int /*long*/ buffer, int buflen);
public static final int GRE_GetGREPathWithProperties (GREVersionRange versions, int versionsLength, int /*long*/ properties, int propertiesLength, int /*long*/ buffer, int buflen) {
	lock.lock();
	try {
		return _GRE_GetGREPathWithProperties(versions, versionsLength, properties, propertiesLength, buffer, buflen);
	} finally {
		lock.unlock();
	}
}
/** @param place cast=(const char *) */
public static final native int _XPCOMGlueStartup (byte[] place);
public static final int XPCOMGlueStartup (byte[] place) {
	lock.lock();
	try {
		return _XPCOMGlueStartup(place);
	} finally {
		lock.unlock();
	}
}
public static final native int _XPCOMGlueShutdown ();
public static final int XPCOMGlueShutdown () {
	lock.lock();
	try {
		return _XPCOMGlueShutdown();
	} finally {
		lock.unlock();
	}
}
}
