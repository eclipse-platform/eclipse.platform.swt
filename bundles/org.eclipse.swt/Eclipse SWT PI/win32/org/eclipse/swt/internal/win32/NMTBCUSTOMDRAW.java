/*******************************************************************************
 * Copyright (c) 2017, 2017 Conrad Groth and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Conrad Groth - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMTBCUSTOMDRAW extends NMCUSTOMDRAW {
	/** @field cast=(HBRUSH) */
	public long hbrMonoDither;
	/** @field cast=(HBRUSH) */
	public long hbrLines;
	/** @field cast=(HPEN) */
	public long hpenLines;
	public int clrText;
	public int clrMark;
	public int clrTextHighlight;
	public int clrBtnFace;
	public int clrBtnHighlight;
	public int clrHighlightHotTrack;
	// RECT rcText;
	/** @field accessor=rcText.left */
	public int rcText_left;
	/** @field accessor=rcText.top */
	public int rcText_top;
	/** @field accessor=rcText.right */
	public int rcText_right;
	/** @field accessor=rcText.bottom */
	public int rcText_bottom;
	public int nStringBkMode;
	public int nHLStringBkMode;
	public int iListGap;
	public static final int sizeof = OS.NMTBCUSTOMDRAW_sizeof ();
}
