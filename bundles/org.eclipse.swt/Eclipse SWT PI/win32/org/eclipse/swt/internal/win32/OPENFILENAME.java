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

public class OPENFILENAME {
	public int lStructSize;
	public int /*long*/ hwndOwner;
	public int /*long*/ hInstance;
	public int /*long*/ lpstrFilter;
	public int /*long*/ lpstrCustomFilter;
	public int nMaxCustFilter;
	public int nFilterIndex;
	public int /*long*/ lpstrFile;
	public int nMaxFile;
	public int /*long*/ lpstrFileTitle;
	public int nMaxFileTitle;
	public int /*long*/ lpstrInitialDir;
	public int /*long*/ lpstrTitle;
	public int Flags;
	public short nFileOffset;
	public short nFileExtension;
	public int /*long*/ lpstrDefExt;
	public int /*long*/ lCustData;
	public int /*long*/ lpfnHook;
	public int /*long*/ lpTemplateName;
	public int /*long*/ pvReserved;
	public int dwReserved;
	public int FlagsEx;   
	public static final int sizeof = !OS.IsWinCE && OS.WIN32_VERSION >= OS.VERSION (5, 0) ? OS.OPENFILENAME_sizeof () : 76;
}
