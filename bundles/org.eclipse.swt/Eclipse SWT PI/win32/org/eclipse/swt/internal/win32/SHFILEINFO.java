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
package org.eclipse.swt.internal.win32;

public class SHFILEINFO {
	/** @field cast=(HICON) */
	public long hIcon;
	public int iIcon;
	public int dwAttributes;
	public char [] szDisplayName = new char [OS.MAX_PATH];
	public char [] szTypeName = new char [80];
	public static int sizeof = OS.SHFILEINFO_sizeof ();
}
