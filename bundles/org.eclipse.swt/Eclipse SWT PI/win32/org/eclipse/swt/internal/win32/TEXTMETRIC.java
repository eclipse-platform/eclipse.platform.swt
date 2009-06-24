/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public abstract class TEXTMETRIC {
	public int tmHeight;
	public int tmAscent; 
	public int tmDescent;
	public int tmInternalLeading;  
	public int tmExternalLeading;
	public int tmAveCharWidth;
	public int tmMaxCharWidth;
	public int tmWeight; 
	public int tmOverhang;
	public int tmDigitizedAspectX;
	public int tmDigitizedAspectY;
	public byte tmItalic;
	public byte tmUnderlined; 
	public byte tmStruckOut;
	public byte tmPitchAndFamily;
	public byte tmCharSet;
	public static final int sizeof = OS.IsUnicode ? OS.TEXTMETRICW_sizeof():  OS.TEXTMETRICA_sizeof ();
}
