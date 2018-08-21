/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public class MCHITTESTINFO {
	public int cbSize;
	public POINT pt = new POINT ();
	public int uHit;
	public SYSTEMTIME st = new SYSTEMTIME ();
//	public RECT rc = new RECT ();
//	public int iOffset;
//	public int iRow;
//	public int iCol;
	public static final int sizeof = OS.MCHITTESTINFO_sizeof ();
}