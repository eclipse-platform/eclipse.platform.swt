/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public abstract class NOTIFYICONDATA {
	static {
		/* Get the Shell32.DLL version */
		DLLVERSIONINFO dvi = new DLLVERSIONINFO ();
		dvi.cbSize = DLLVERSIONINFO.sizeof;
		dvi.dwMajorVersion = 4;
		TCHAR lpLibFileName = new TCHAR (0, "Shell32.dll", true); //$NON-NLS-1$
		int hModule = OS.LoadLibrary (lpLibFileName);
		if (hModule != 0) {
			String name = "DllGetVersion\0"; //$NON-NLS-1$
			byte [] lpProcName = new byte [name.length ()];
			for (int i=0; i<lpProcName.length; i++) {
				lpProcName [i] = (byte) name.charAt (i);
			}
			int DllGetVersion = OS.GetProcAddress (hModule, lpProcName);
			if (DllGetVersion != 0) OS.Call (DllGetVersion, dvi);
			OS.FreeLibrary (hModule);
		}
		if (dvi.dwMajorVersion < 5) {
			sizeof = OS.NOTIFYICONDATA_V1_SIZE;
		} else if (dvi.dwMajorVersion == 5) {
			sizeof = OS.NOTIFYICONDATA_V2_SIZE;
		} else {
			sizeof = OS.NOTIFYICONDATA_sizeof ();
		}
	}
	
	public int cbSize;
	public int hWnd;
	public int uID;
	public int uFlags;
	public int uCallbackMessage;
	public int hIcon;
	public int dwState;
	public int dwStateMask;
	public int uVersion;
	public int dwInfoFlags;
	public static final int sizeof;
}