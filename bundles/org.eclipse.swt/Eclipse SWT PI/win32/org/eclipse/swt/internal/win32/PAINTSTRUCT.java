package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
