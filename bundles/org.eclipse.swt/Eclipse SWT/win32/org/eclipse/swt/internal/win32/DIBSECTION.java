package org.eclipse.swt.internal.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
public class DIBSECTION extends BITMAP {
	public int biSize;
	public int biWidth;
	public int biHeight;
	public short biPlanes;
	public short biBitCount;
	public int biCompression;
	public int biSizeImage;
	public int biXPelsPerMeter;
	public int biYPelsPerMeter;
	public int biClrUsed;
	public int biClrImportant;
	public int dsBitfields0;
	public int dsBitfields1;
	public int dsBitfields2;
	public int dshSection;
	public int dsOffset;
	public static final int sizeof = 84;
}
