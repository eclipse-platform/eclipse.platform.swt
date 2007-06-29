/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;


import org.eclipse.swt.internal.*;

public class Win32 extends C {
	static {
		Library.loadLibrary ("swt-win32"); //$NON-NLS-1$
	}

	/** GDI+ constants */
	public static final int PixelFormat_Format24bppRgb = 137224;
	public static final int PixelFormat_Format32bppRgb = 139273;
	public static final int PixelFormat_Format32bppArgb = 2498570;
	public static final int PixelFormat_Format16bppRgb555 = 135173;
	public static final int PixelFormat_Format16bppRgb565 = 135174;
	public static final int PixelFormat_Format16bppArgb1555 = 397319;
	
	public static final int OIC_BANG = 0x7F03;
	public static final int OIC_HAND = 0x7F01;
	public static final int OIC_INFORMATION = 0x7F04;
	public static final int OIC_QUES = 0x7F02;
	public static final int OIC_WINLOGO = 0x7F05;
	public static final int IMAGE_ICON = 0x1;
	public static final int LR_SHARED = 0x8000;
	
	public static int HEAP_ZERO_MEMORY = 0x8;
	public static int SW_SHOW = 0x5;

/** OLE Natives */	
public static final native int OleInitialize(int reserved);
public static final native void OleUninitialize();
	
/** Win32 Natives */
public static final native boolean CloseHandle(int hObject);
public static final native int CreateCursor (int hInst, int xHotSpot, int yHotSpot, int nWidth, int nHeight, byte [] pvANDPlane, byte [] pvXORPlane);
public static final native boolean CreateProcessW(int lpApplicationName, char [] lpCommandLine, int lpProcessAttributes, int lpThreadAttributes, boolean bInheritHandles, int dwCreationFlags, int lpEnvironment, int lpCurrentDirectory, STARTUPINFOW lpStartupInfo, PROCESS_INFORMATION lpProcessInformation);
public static final native int CreateIconIndirect (ICONINFO lplf);
public static final native boolean DeleteObject (int hGdiObj);
public static final native boolean DestroyIcon (int hIcon);
public static final native void EnableWindow(int window, boolean enabled);
public static final native int ExtractIconExW(char[] lpszFile, int nIconIndex, int [] phiconLarge, int [] phiconSmall, int nIcons);
public static final native void GetCursorPos (POINT point);
public static final native boolean GetIconInfo (int hIcon, ICONINFO piconinfo);
public static final native boolean GetKeyboardState (byte [] lpKeyState);
public static final native int GetProcessHeap();
public static final native int GetModuleHandleW (char [] lpModuleName);
public static final native int HeapAlloc (int hHeap, int dwFlags, int dwBytes);
public static final native boolean HeapFree (int hHeap, int dwFlags, int lpMem);
public static final native int LoadImage (int hinst, int lpszName, int uType, int cxDesired, int cyDesired, int fuLoad);
public static final native int MapVirtualKeyW (int uCode, int uMapType);
public static final native void MoveMemory (int Destination, char [] Source, int Length);
public static final native int PROCESS_INFORMATION_sizeof();
public static final native int SetCursorPos (int x, int y);
public static final native boolean ShellExecuteExW(SHELLEXECUTEINFOW lpExecInfo);
public static final native int SHELLEXECUTEINFOW_sizeof();
public static final native int STARTUPINFOW_sizeof();
public static final native int ToUnicode(int wVirtKey, int wScanCode, byte [] lpKeyState, char [] pwszBuff, int cchBuff, int wFlags);


}
