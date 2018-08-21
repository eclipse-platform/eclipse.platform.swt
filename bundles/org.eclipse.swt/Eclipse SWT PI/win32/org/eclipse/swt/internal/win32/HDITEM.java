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

public class HDITEM {
	public int mask;
	public int cxy;
	/** @field cast=(LPTSTR) */
	public long /*int*/ pszText;
	/** @field cast=(HBITMAP) */
	public long /*int*/ hbm;
	public int cchTextMax;
	public int fmt;
	public long /*int*/ lParam;
	public int iImage;
	public int iOrder;
	public int type;
	/** @field cast=(void *) */
	public long /*int*/ pvFilter;
	public static int sizeof = OS.HDITEM_sizeof ();
}
