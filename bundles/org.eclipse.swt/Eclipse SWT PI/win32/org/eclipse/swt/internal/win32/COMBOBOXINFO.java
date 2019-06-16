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

public class COMBOBOXINFO {
	public int cbSize;
	/** @field accessor=rcItem.left */
	public int itemLeft;
	/** @field accessor=rcItem.top */
	public int itemTop;
	/** @field accessor=rcItem.right */
	public int itemRight;
	/** @field accessor=rcItem.bottom */
	public int itemBottom;
	/** @field accessor=rcButton.left */
	public int buttonLeft;
	/** @field accessor=rcButton.top */
	public int buttonTop;
	/** @field accessor=rcButton.right */
	public int buttonRight;
	/** @field accessor=rcButton.bottom */
	public int buttonBottom;
	public int stateButton;
	/** @field cast=(HWND) */
	public long hwndCombo;
	/** @field cast=(HWND) */
	public long hwndItem;
	/** @field cast=(HWND) */
	public long hwndList;
	public static final int sizeof = OS.COMBOBOXINFO_sizeof ();
}
