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

public class GUITHREADINFO {
	/** @field cast=(DWORD) */
	public int cbSize;
	/** @field cast=(DWORD) */
	public int flags;
	/** @field cast=(HWND) */
	public int /*long*/ hwndActive;
	/** @field cast=(HWND) */
	public int /*long*/ hwndFocus;
	/** @field cast=(HWND) */
	public int /*long*/ hwndCapture;
	/** @field cast=(HWND) */
	public int /*long*/ hwndMenuOwner;
	/** @field cast=(HWND) */
	public int /*long*/ hwndMoveSize; 
	/** @field cast=(HWND) */
	public int /*long*/ hwndCaret;
//	RECT rcCaret;
	/** @field accessor=rcCaret.left */
	public int left; 
	/** @field accessor=rcCaret.top */
	public int top; 
	/** @field accessor=rcCaret.right */
	public int right; 
	/** @field accessor=rcCaret.bottom */
	public int bottom;
	public static int sizeof = OS.GUITHREADINFO_sizeof ();
}
