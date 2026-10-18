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

public class LVCOLUMN {
	public int mask;
	public int fmt;
	public int cx;
	/** @field cast=(LPTSTR) */
	public long pszText;
	public int cchTextMax;
	public int iSubItem;
	public int iImage;
	public int iOrder;
	public static final int sizeof = OS.LVCOLUMN_sizeof ();
}
