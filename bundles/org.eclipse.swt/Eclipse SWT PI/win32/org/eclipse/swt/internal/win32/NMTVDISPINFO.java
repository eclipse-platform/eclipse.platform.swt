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

public class NMTVDISPINFO extends NMHDR {
//	TVITEM item;
	/** @field accessor=item.mask */
	public int mask;
	/** @field accessor=item.hItem,cast=(HTREEITEM) */
	public int /*long*/ hItem;
	/** @field accessor=item.state */
	public int state;
	/** @field accessor=item.stateMask */
	public int stateMask;
	/** @field accessor=item.pszText,cast=(LPTSTR) */
	public int /*long*/ pszText;
  	/** @field accessor=item.cchTextMax */
	public int cchTextMax;
  	/** @field accessor=item.iImage */
	public int iImage;
  	/** @field accessor=item.iSelectedImage */
	public int iSelectedImage;
	/** @field accessor=item.cChildren */
	public int cChildren;
	/** @field accessor=item.lParam */
	public int /*long*/ lParam;
	public static final int sizeof = OS.NMTVDISPINFO_sizeof ();
}
