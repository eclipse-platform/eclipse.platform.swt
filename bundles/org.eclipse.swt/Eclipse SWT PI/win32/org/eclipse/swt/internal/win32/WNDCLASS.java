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

public class WNDCLASS {
	public int style; 
	/** @field cast=(WNDPROC) */
	public int /*long*/ lpfnWndProc; 
	public int cbClsExtra; 
	public int cbWndExtra; 
	/** @field cast=(HINSTANCE) */
	public int /*long*/ hInstance; 
	/** @field cast=(HICON) */
	public int /*long*/ hIcon; 
	/** @field cast=(HCURSOR) */
	public int /*long*/ hCursor; 
	/** @field cast=(HBRUSH) */
	public int /*long*/ hbrBackground; 
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpszMenuName; 
	/** @field cast=(LPCTSTR) */
	public int /*long*/ lpszClassName; 
	public static final int sizeof = OS.WNDCLASS_sizeof ();
}
