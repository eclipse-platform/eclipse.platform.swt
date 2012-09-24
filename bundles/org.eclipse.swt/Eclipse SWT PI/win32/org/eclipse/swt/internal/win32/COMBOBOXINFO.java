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
	public long /*int*/ hwndCombo;
	/** @field cast=(HWND) */
	public long /*int*/ hwndItem;
	/** @field cast=(HWND) */
	public long /*int*/ hwndList;
	public static final int sizeof = OS.COMBOBOXINFO_sizeof ();
}
