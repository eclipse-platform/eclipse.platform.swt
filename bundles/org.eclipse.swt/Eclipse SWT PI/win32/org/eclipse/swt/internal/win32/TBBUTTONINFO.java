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

public class TBBUTTONINFO {
	public int cbSize;
	public int dwMask;
	public int idCommand;
	public int iImage;
	public byte fsState;
	public byte fsStyle;
	public short cx;
	public int /*long*/ lParam;
	/** @field cast=(LPTSTR) */
	public int /*long*/ pszText;
	public int cchText;
	public static final int sizeof = OS.TBBUTTONINFO_sizeof ();
}
