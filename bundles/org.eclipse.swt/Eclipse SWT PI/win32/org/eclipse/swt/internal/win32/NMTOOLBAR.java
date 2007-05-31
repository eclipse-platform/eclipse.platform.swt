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

public class NMTOOLBAR extends NMHDR {
	public int iItem;
//	TBBUTTON tbButton;
	public int iBitmap;
	public int idCommand;
	public byte fsState;
	public byte fsStyle;
	public int /*long*/ dwData;
	public int /*long*/ iString;
	public int cchText;
	public int /*long*/ pszText;
//	RECT rcButton;
	public int left, top, right, bottom;
	/* Note in WinCE.  The field rcButton is not defined. */
	public static final int sizeof = OS.NMTOOLBAR_sizeof ();
}
