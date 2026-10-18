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

public class BUTTON_IMAGELIST {
	/** @field cast=(HIMAGELIST) */
	public long himl;
	/** @field accessor=margin.left,cast=(LONG) */
	public int margin_left;
	/** @field accessor=margin.top,cast=(LONG) */
	public int margin_top;
	/** @field accessor=margin.right,cast=(LONG) */
	public int margin_right;
	/** @field accessor=margin.bottom,cast=(LONG) */
	public int margin_bottom;
	/** @field cast=(UINT) */
	public int uAlign;
	public static final int sizeof = OS.BUTTON_IMAGELIST_sizeof ();
}
