/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class TOOLINFO {
	public int cbSize; 
	public int uFlags;
	public int hwnd; 
	public int uId; 
//	public RECT rect;
	public int left, top, right, bottom;
	public int hinst; 
	public int lpszText;
	public int lParam;
	public static int sizeof = 44;
}
