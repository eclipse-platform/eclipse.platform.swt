package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
	public byte
		lfFaceName0,  lfFaceName1,  lfFaceName2,  lfFaceName3,
		lfFaceName4,  lfFaceName5,  lfFaceName6,  lfFaceName7,
		lfFaceName8,  lfFaceName9,  lfFaceName10, lfFaceName11,
		lfFaceName12, lfFaceName13, lfFaceName14, lfFaceName15,
		lfFaceName16, lfFaceName17, lfFaceName18, lfFaceName19,
		lfFaceName20, lfFaceName21, lfFaceName22, lfFaceName23,
		lfFaceName24, lfFaceName25, lfFaceName26, lfFaceName27,
		lfFaceName28, lfFaceName29, lfFaceName30, lfFaceName31;
	public static final int sizeof = 60;
}
