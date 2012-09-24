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

public abstract class NOTIFYICONDATA {
	public int cbSize;
	/** @field cast=(HWND) */
	public long /*int*/ hWnd;
	public int uID;
	public int uFlags;
	public int uCallbackMessage;
	/** @field cast=(HICON) */
	public long /*int*/ hIcon;
	/** @field flags=no_wince */
	public int dwState;
	/** @field flags=no_wince */
	public int dwStateMask;
	/** @field flags=no_wince */
	public int uVersion;
	/** @field flags=no_wince */
	public int dwInfoFlags;
	public static final int sizeof = OS.NOTIFYICONDATA_V2_SIZE;
}
