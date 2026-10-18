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

public class DRAWITEMSTRUCT {
	public int CtlType;
	public int CtlID;
	public int itemID;
	public int itemAction;
	public int itemState;
	/** @field cast=(HWND) */
	public long hwndItem;
	/** @field cast=(HDC) */
	public long hDC;
// 	RECT rcItem;
	/** @field accessor=rcItem.left */
	public int left;
	/** @field accessor=rcItem.top */
	public int top;
	/** @field accessor=rcItem.bottom */
	public int bottom;
	/** @field accessor=rcItem.right */
	public int right;
	public long itemData;
	public static final int sizeof = OS.DRAWITEMSTRUCT_sizeof ();
}
