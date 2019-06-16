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

public class MENUBARINFO {
	public int cbSize;
//	RECT  rcBar;
	/** @field accessor=rcBar.left */
	public int left;
	/** @field accessor=rcBar.top */
	public int top;
	/** @field accessor=rcBar.right */
	public int right;
	/** @field accessor=rcBar.bottom */
	public int bottom;
	/** @field cast=(HMENU) */
	public long hMenu;
	/** @field cast=(HWND) */
	public long hwndMenu;
	public boolean fBarFocused;
	public boolean fFocused;
	public static final int sizeof = OS.MENUBARINFO_sizeof ();
}
