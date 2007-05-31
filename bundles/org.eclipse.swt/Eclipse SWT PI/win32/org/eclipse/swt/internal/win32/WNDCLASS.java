/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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
	public int /*long*/ lpfnWndProc; 
	public int cbClsExtra; 
	public int cbWndExtra; 
	public int /*long*/ hInstance; 
	public int /*long*/ hIcon; 
	public int /*long*/ hCursor; 
	public int /*long*/ hbrBackground; 
	public int /*long*/ lpszMenuName; 
	public int /*long*/ lpszClassName; 
	public static final int sizeof = OS.WNDCLASS_sizeof ();
}
