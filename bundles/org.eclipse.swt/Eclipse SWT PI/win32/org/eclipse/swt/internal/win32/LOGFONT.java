/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public abstract class LOGFONT {
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
	public static final int sizeof = OS.IsUnicode ? 92 : 60;	
}
