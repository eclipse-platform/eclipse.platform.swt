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

public class HDITEM {
	public int mask;
	public int cxy;
	/** @field cast=(LPTSTR) */
	public int /*long*/ pszText;
	/** @field cast=(HBITMAP) */
	public int /*long*/ hbm;
	public int cchTextMax;
	public int fmt;
	public int /*long*/ lParam; 
	public int iImage;
	public int iOrder;
	/** @field flags=no_wince */
	public int type;
	/** @field cast=(void *),flags=no_wince */
	public int /*long*/ pvFilter; 
	public static int sizeof = OS.HDITEM_sizeof ();
}
