package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class NONCLIENTMETRICS {
	public int cbSize; 
	public int iBorderWidth; 
	public int iScrollWidth; 
	public int iScrollHeight; 
	public int iCaptionWidth; 
	public int iCaptionHeight; 
//  LOGFONT lfCaptionFont;
	public LOGFONT lfCaptionFont = new LOGFONT ();
	public int iSmCaptionWidth; 
	public int iSmCaptionHeight; 
//  LOGFONT lfSmCaptionFont;
	public LOGFONT lfSmCaptionFont = new LOGFONT (); 
	public int iMenuWidth; 
	public int iMenuHeight; 
//  LOGFONT lfMenuFont; 
	public LOGFONT lfMenuFont = new LOGFONT (); 
//  LOGFONT lfStatusFont; 
	public LOGFONT lfStatusFont = new LOGFONT (); 
//  LOGFONT lfMessageFont; 
	public LOGFONT lfMessageFont = new LOGFONT (); 
	public static final int sizeof = OS.IsUnicode ? 500 : 340;
}

