/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	public int hSubMenu;
	public int hbmpChecked;
	public int hbmpUnchecked;
	public int dwItemData;
	public int dwTypeData;
	public int cch;
	public int hbmpItem;
	public static final int sizeof;

	/*
	* Feature in Windows.  The hbmpItem field requires Windows 4.10
	* or greater.  On Windows NT 4.0, passing in a larger struct size
	* in the cbSize field does nothing.  On Windows 95, the MENUITEMINFO
	* calls fail when the struct size is too large.  The fix is to ensure
	* that the correct struct size is used for the Windows platform.
	*/
	static {
		sizeof = OS.WIN32_VERSION < OS.VERSION(4, 10) ? 44 : 48;
	}
}
