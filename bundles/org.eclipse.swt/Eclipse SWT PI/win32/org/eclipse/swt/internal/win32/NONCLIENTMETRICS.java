package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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

