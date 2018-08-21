/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class COSERVERINFO {
	public int dwReserved1;
	/** @field cast=(LPWSTR) */
	public long /*int*/ pwszName;
	/** @field cast=(COAUTHINFO *) */
	public long /*int*/ pAuthInfo;
	public int dwReserved2;
	public static final int sizeof = COM.COSERVERINFO_sizeof ();
}
