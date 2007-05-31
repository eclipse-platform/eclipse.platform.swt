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

public class LVITEM {
	public int mask;
	public int iItem;
	public int iSubItem;
	public int state;
	public int stateMask;
	public int /*long*/ pszText;
	public int cchTextMax;
	public int iImage;
	public int /*long*/ lParam;
	public int iIndent;
	public int iGroupId;
	public int cColumns;
	public int /*long*/ puColumns;
	public static final int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 1) ? OS.LVITEM_sizeof () : 40;
}
