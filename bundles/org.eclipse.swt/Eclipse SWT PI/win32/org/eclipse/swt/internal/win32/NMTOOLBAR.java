/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMTOOLBAR extends NMHDR {
	public int iItem;
//	TBBUTTON tbButton;
	/** @field accessor=tbButton.iBitmap */
	public int iBitmap;
	/** @field accessor=tbButton.idCommand */
	public int idCommand;
	/** @field accessor=tbButton.fsState */
	public byte fsState;
	/** @field accessor=tbButton.fsStyle */
	public byte fsStyle;
	/** @field accessor=tbButton.dwData */
	public long /*int*/ dwData;
	/** @field accessor=tbButton.iString */
	public long /*int*/ iString;
	public int cchText;
	/** @field cast=(LPTSTR) */
	public long /*int*/ pszText;
//	RECT rcButton;
	/** @field accessor=rcButton.left,flags=no_wince */
	public int left; 
	/** @field accessor=rcButton.top,flags=no_wince */
	public int top; 
	/** @field accessor=rcButton.right,flags=no_wince */
	public int right; 
	/** @field accessor=rcButton.bottom,flags=no_wince */
	public int bottom;
	/* Note in WinCE.  The field rcButton is not defined. */
	public static final int sizeof = OS.NMTOOLBAR_sizeof ();
}
