/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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