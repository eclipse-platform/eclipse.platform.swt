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
	public int /*long*/ hMenu;
	/** @field cast=(HWND) */
	public int /*long*/ hwndMenu;
	public boolean fBarFocused;
	public boolean fFocused;
	public static final int sizeof = OS.MENUBARINFO_sizeof ();
}
