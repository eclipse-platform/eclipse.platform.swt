package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class NMTOOLBAR extends NMHDR {
	public int iItem;
//	TBBUTTON tbButton;
	public int iBitmap;
	public int idCommand;
	public byte fsState;
	public byte fsStyle;
	public int dwData;
	public int iString;
	public int cchText;
	public int pszText;
//	RECT rcButton;
	public int left, top, right, bottom;
	/* Note in WinCE.  The field rcButton is not defined. */
	public static final int sizeof = OS.IsWinCE ? 44 : 60;
}
