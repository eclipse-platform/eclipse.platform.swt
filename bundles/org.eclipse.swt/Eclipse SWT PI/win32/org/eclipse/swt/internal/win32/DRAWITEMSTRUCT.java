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

public class DRAWITEMSTRUCT {
	public int CtlType;
	public int CtlID;
	public int itemID;
	public int itemAction;
	public int itemState;
	/** @field cast=(HWND) */
	public long /*int*/ hwndItem;
	/** @field cast=(HDC) */
	public long /*int*/ hDC;
// 	RECT rcItem;
	/** @field accessor=rcItem.left */
	public int left;
	/** @field accessor=rcItem.top */
	public int top;
	/** @field accessor=rcItem.bottom */
	public int bottom;
	/** @field accessor=rcItem.right */
	public int right;
	public long /*int*/ itemData;
	public static final int sizeof = OS.DRAWITEMSTRUCT_sizeof ();
}
