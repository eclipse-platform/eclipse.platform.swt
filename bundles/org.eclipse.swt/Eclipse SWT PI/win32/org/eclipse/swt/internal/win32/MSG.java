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

public class MSG {
	public int /*long*/ hwnd;
	public int message; 
	public int /*long*/ wParam; 
	public int /*long*/ lParam; 
	public int time; 
//	POINT pt;
	public int x, y;
	public static final int sizeof = OS.MSG_sizeof ();
}
