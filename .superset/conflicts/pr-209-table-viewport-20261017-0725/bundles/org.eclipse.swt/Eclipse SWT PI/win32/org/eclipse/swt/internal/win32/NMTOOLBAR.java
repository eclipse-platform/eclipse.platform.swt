/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
	public long dwData;
	/** @field accessor=tbButton.iString */
	public long iString;
	public int cchText;
	/** @field cast=(LPTSTR) */
	public long pszText;
//	RECT rcButton;
	/** @field accessor=rcButton.left */
	public int left;
	/** @field accessor=rcButton.top */
	public int top;
	/** @field accessor=rcButton.right */
	public int right;
	/** @field accessor=rcButton.bottom */
	public int bottom;
	public static final int sizeof = OS.NMTOOLBAR_sizeof ();
}
