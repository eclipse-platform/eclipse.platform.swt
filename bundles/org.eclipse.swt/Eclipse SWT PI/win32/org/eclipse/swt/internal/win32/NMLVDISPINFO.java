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

public class NMLVDISPINFO extends NMHDR {
//	LVITEM item;
	/** @field accessor=item.mask */
	public int mask;
	/** @field accessor=item.iItem */
	public int iItem;
	/** @field accessor=item.iSubItem */
	public int iSubItem;
	/** @field accessor=item.state */
	public int state;
	/** @field accessor=item.stateMask */
	public int stateMask;
	/** @field accessor=item.pszText,cast=(LPTSTR) */
	public long /*int*/ pszText;
	/** @field accessor=item.cchTextMax */
	public int cchTextMax;
	/** @field accessor=item.iImage */
	public int iImage;
	/** @field accessor=item.lParam */
	public long /*int*/ lParam;
	/** @field accessor=item.iIndent */
	public int iIndent;
	/** @field accessor=item.iGroupId,flags=no_wince */
	public int iGroupId;
	/** @field accessor=item.cColumns,flags=no_wince */
	public int cColumns;
	/** @field accessor=item.puColumns,cast=(PUINT),flags=no_wince */
	public long /*int*/ puColumns;
	public static final int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1) ? OS.NMLVDISPINFO_sizeof () : 52;
}
