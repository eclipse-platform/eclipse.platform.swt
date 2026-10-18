/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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
	/** @field accessor=rcText.left */
	public int rcText_left;
	/** @field accessor=rcText.top */
	public int rcText_top;
	/** @field accessor=rcText.right */
	public int rcText_right;
	/** @field accessor=rcText.bottom */
	public int rcText_bottom;
	public int uAlign;
	public static final int sizeof = OS.NMLVCUSTOMDRAW_sizeof ();
}
