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

public class TVINSERTSTRUCT {
	/** @field cast=(HTREEITEM) */
	public long hParent;
	/** @field cast=(HTREEITEM) */
	public long hInsertAfter;
//	public TVITEMEX item;
	/** @field accessor=item.mask */
	public int mask;
	/** @field accessor=item.hItem,cast=(HTREEITEM) */
	public long hItem;
	/** @field accessor=item.state */
	public int state;
	/** @field accessor=item.stateMask */
	public int stateMask;
	/** @field accessor=item.pszText,cast=(LPTSTR) */
	public long pszText;
	/** @field accessor=item.cchTextMax */
	public int cchTextMax;
	/** @field accessor=item.iImage */
	public int iImage;
	/** @field accessor=item.iSelectedImage */
	public int iSelectedImage;
	/** @field accessor=item.cChildren */
	public int cChildren;
	/** @field accessor=item.lParam */
	public long lParam;
	/** @field accessor=itemex.iIntegral */
	public int iIntegral;
	public static final int sizeof = OS.TVINSERTSTRUCT_sizeof ();
}
