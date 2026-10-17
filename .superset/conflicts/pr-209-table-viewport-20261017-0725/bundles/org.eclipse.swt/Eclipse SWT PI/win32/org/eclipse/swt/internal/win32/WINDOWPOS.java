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

public class WINDOWPOS {
	/** @field cast=(HWND) */
	public long hwnd;
	/** @field cast=(HWND) */
	public long hwndInsertAfter;
	public int x;
	public int y;
	public int cx;
	public int cy;
	public int flags;
	public static final int sizeof = OS.WINDOWPOS_sizeof ();
}
