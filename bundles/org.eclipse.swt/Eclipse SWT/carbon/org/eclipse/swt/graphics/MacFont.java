/*
 * Copyright (c) 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *
 * Andre Weinand, OTI - Initial version
 */
package org.eclipse.swt.graphics;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.widgets.MacUtil;

public class MacFont {

	public short fID= 1;
	public short fSize= 12;
	public short fFace= 0;


	public MacFont() {
	}
	
	public MacFont(String name, int size, int face) {
	
		fFace= (short)OS.normal;
		if ((face & SWT.BOLD) !=  0)
			fFace |= OS.bold;
		if ((face & SWT.ITALIC) !=  0)
			fFace |= OS.italic;

		if ("Courier".equals(name)) {
			name= "Monaco";
		}
		
		if ("MS Sans Serif".equals(name)) {
			MacFont f= getThemeFont(OS.kThemeSystemFont);
			fID= f.fID;
			fSize= f.fSize;
			return;
		}
		
		if (size < 10)
			size= 10;

		short id= OS.FMGetFontFamilyFromName(MacUtil.Str255(name));
		//System.out.print("MacFont(" + name + ", " + size + ", " + face + "): ");
		if (id == OS.kInvalidFontFamily) {
			fID= (short) 1;
			//System.out.println("not found");
		} else {
			fID= id;
			//System.out.println(fID);
		}
		fSize= (short)size;
	}
	
	public static MacFont getThemeFont(int themeFontId) {
		byte[] fontName= new byte[256];
		short[] fontSize= new short[1];
		byte[] style= new byte[1];
		OS.GetThemeFont((short)themeFontId, (short)OS.smSystemScript, fontName, fontSize, style);
		return new MacFont(MacUtil.toString(fontName), fontSize[0], style[0]);
	}
	
	public MacFont(short ID, short size, short face) {
		fID= ID;
		fSize= size;
		fFace= (short)OS.normal;
		if ((face & SWT.BOLD) !=  0)
			fFace |= OS.bold;
		if ((face & SWT.ITALIC) !=  0)
			fFace |= OS.italic;
	}
	
	public MacFont(short ID) {
		fID= ID;
	}
	
	public String getName() {
		byte[] name= new byte[256];
		if (OS.FMGetFontFamilyName(fID, name) == OS.noErr)
			return MacUtil.toString(name);
		return "no name";
	}
	
	public short getSize() {
		return fSize;
	}
	
	public int getFace() {
		int face= 0;
		if ((fFace & OS.bold) != 0)
			face |= SWT.BOLD;
		if ((fFace & OS.italic) != 0)
			face |= SWT.ITALIC;
		return face;
	}
	
	public void installInGrafPort() {
		OS.TextFont(fID);
		OS.TextSize(fSize);
		OS.TextFace(fFace);
	}
	
	public boolean equals(Object object) {
		if (object == this) return true;
		if (!(object instanceof MacFont)) return false;
		MacFont font= (MacFont) object;
		return fID == font.fID && fSize == font.fSize && fFace == font.fFace;
	}
	
	public int hashCode() {
		return (fID << 16) | (fSize << 8) | fFace;
	}
	
	public String toString() {
		return fID + "," + fSize + "," + fFace;
	}
}
