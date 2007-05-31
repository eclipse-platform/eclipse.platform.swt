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

public class CHOOSEFONT {
	public int lStructSize;
	public int /*long*/ hwndOwner;
	public int /*long*/ hDC;
	public int /*long*/ lpLogFont;
	public int iPointSize;
	public int Flags;
	public int rgbColors;
	public int /*long*/ lCustData;
	public int /*long*/ lpfnHook;
	public int /*long*/ lpTemplateName;
	public int /*long*/ hInstance;
	public int /*long*/ lpszStyle;
	public short nFontType;
	public int nSizeMin;
	public int nSizeMax;
	public static final int sizeof = OS.CHOOSEFONT_sizeof ();
}
