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

public class NMLVCUSTOMDRAW extends NMCUSTOMDRAW {
	public int clrText;
	public int clrTextBk;
	public int iSubItem;
	public int dwItemType;
	public int clrFace;
	public int iIconEffect;
	public int iIconPhase;
	public int iPartId;
	public int iStateId;
//	RECT rcText;
	public int rcText_left, rcText_top, rcText_right, rcText_bottom;
	public int uAlign; 
	public static final int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1) ? OS.NMLVCUSTOMDRAW_sizeof () : 60;
}
