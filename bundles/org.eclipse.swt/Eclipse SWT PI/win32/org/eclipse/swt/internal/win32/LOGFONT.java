package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class LOGFONT {
	public int lfHeight;    
	public int lfWidth;    
	public int lfEscapement; 
	public int lfOrientation;    
	public int lfWeight;    
	public byte lfItalic;    
	public byte lfUnderline; 
	public byte lfStrikeOut;    
	public byte lfCharSet;    
	public byte lfOutPrecision; 
	public byte lfClipPrecision;    
	public byte lfQuality;    
	public byte lfPitchAndFamily;
//	TCHAR lfFaceName[LF_FACESIZE]; 
	public char
		lfFaceName0,  lfFaceName1,  lfFaceName2,  lfFaceName3,
		lfFaceName4,  lfFaceName5,  lfFaceName6,  lfFaceName7,
		lfFaceName8,  lfFaceName9,  lfFaceName10, lfFaceName11,
		lfFaceName12, lfFaceName13, lfFaceName14, lfFaceName15,
		lfFaceName16, lfFaceName17, lfFaceName18, lfFaceName19,
		lfFaceName20, lfFaceName21, lfFaceName22, lfFaceName23,
		lfFaceName24, lfFaceName25, lfFaceName26, lfFaceName27,
		lfFaceName28, lfFaceName29, lfFaceName30, lfFaceName31;
	public static final int sizeof = OS.IsUnicode ? 92 : 60;
}
