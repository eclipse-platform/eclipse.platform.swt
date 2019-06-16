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

public class WNDCLASS {
	public int style;
	/** @field cast=(WNDPROC) */
	public long lpfnWndProc;
	public int cbClsExtra;
	public int cbWndExtra;
	/** @field cast=(HINSTANCE) */
	public long hInstance;
	/** @field cast=(HICON) */
	public long hIcon;
	/** @field cast=(HCURSOR) */
	public long hCursor;
	/** @field cast=(HBRUSH) */
	public long hbrBackground;
	/** @field cast=(LPCTSTR) */
	public long lpszMenuName;
	/** @field cast=(LPCTSTR) */
	public long lpszClassName;
	public static final int sizeof = OS.WNDCLASS_sizeof ();
}
