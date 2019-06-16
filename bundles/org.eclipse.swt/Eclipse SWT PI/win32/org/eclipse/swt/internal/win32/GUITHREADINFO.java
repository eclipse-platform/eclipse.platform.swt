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

public class GUITHREADINFO {
	/** @field cast=(DWORD) */
	public int cbSize;
	/** @field cast=(DWORD) */
	public int flags;
	/** @field cast=(HWND) */
	public long hwndActive;
	/** @field cast=(HWND) */
	public long hwndFocus;
	/** @field cast=(HWND) */
	public long hwndCapture;
	/** @field cast=(HWND) */
	public long hwndMenuOwner;
	/** @field cast=(HWND) */
	public long hwndMoveSize;
	/** @field cast=(HWND) */
	public long hwndCaret;
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
