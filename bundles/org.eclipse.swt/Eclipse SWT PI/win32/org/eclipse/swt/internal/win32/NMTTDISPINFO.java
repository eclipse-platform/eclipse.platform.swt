/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMTTDISPINFO extends NMHDR {
	public int lpszText;
//  char szText[80];
//	public char [] szText = new char [80];
	public int hinst;   
	public int uFlags;
	public int lParam;
	public static final int sizeofW = 188;
	public static final int sizeofA = 108;
	public static final int sizeof = OS.IsUnicode ? sizeofW : sizeofA;
}
