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

public class NMLVCUSTOMDRAW extends NMCUSTOMDRAW {
	public int clrText;
	public int clrTextBk;
	public int iSubItem;
	/** @field flags=no_wince */
	public int dwItemType;
	/** @field flags=no_wince */
	public int clrFace;
	/** @field flags=no_wince */
	public int iIconEffect;
	/** @field flags=no_wince */
	public int iIconPhase;
	/** @field flags=no_wince */
	public int iPartId;
	/** @field flags=no_wince */
	public int iStateId;
//	RECT rcText;
	/** @field accessor=rcText.left,flags=no_wince */
	public int rcText_left; 
	/** @field accessor=rcText.top,flags=no_wince */
	public int rcText_top; 
	/** @field accessor=rcText.right,flags=no_wince */
	public int rcText_right; 
	/** @field accessor=rcText.bottom,flags=no_wince */
	public int rcText_bottom;
	/** @field flags=no_wince */
	public int uAlign; 
	public static final int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1) ? OS.NMLVCUSTOMDRAW_sizeof () : 60;
}
