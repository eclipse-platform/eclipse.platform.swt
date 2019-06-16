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

public class MENUITEMINFO {
	public int cbSize;
	public int fMask;
	public int fType;
	public int fState;
	public int wID;
	/** @field cast=(HMENU) */
	public long hSubMenu;
	/** @field cast=(HBITMAP) */
	public long hbmpChecked;
	/** @field cast=(HBITMAP) */
	public long hbmpUnchecked;
	public long dwItemData;
	/** @field cast=(LPTSTR) */
	public long dwTypeData;
	public int cch;
	/** @field cast=(HBITMAP) */
	public long hbmpItem;
	public static final int sizeof = OS.MENUITEMINFO_sizeof ();
}
