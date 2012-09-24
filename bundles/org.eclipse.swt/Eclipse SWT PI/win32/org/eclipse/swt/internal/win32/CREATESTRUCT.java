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

public class CREATESTRUCT {
	/** @field cast=(LPVOID) */
	public long /*int*/ lpCreateParams; 
	/** @field cast=(HINSTANCE) */
	public long /*int*/ hInstance; 
	/** @field cast=(HMENU) */
	public long /*int*/ hMenu; 
	/** @field cast=(HWND) */
	public long /*int*/ hwndParent; 
	public int cy; 
	public int cx; 
	public int y; 
	public int x; 
	public int style; 
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpszName; 
	/** @field cast=(LPCTSTR) */
	public long /*int*/ lpszClass; 
	public int dwExStyle;
	public static final int sizeof = OS.CREATESTRUCT_sizeof ();
}
