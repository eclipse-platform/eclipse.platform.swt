package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class PAINTSTRUCT {
	public int  hdc; 
	public boolean fErase; 
//	public RECT rcPaint;
	public int left, top, right, bottom;
	public boolean fRestore; 
	public boolean fIncUpdate; 
//  public byte rgbReserved[32];
	public int pad0, pad1, pad2, pad3, pad4, pad5, pad6, pad7;
	public static final int sizeof = 64;
}
