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

public class CHOOSEFONT {
	public int lStructSize;     
	public int hwndOwner; 
	public int hDC;     
	public int lpLogFont;     
	public int iPointSize; 
	public int Flags;     
	public int rgbColors;     
	public int lCustData; 
	public int lpfnHook;     
	public int lpTemplateName; 
	public int hInstance;     
	public int lpszStyle; 
	public short nFontType;
	public int nSizeMin;     
	public int nSizeMax; 
	public static final int sizeof = 60;
}
