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

public class SHMENUBARINFO {
	public int cbSize;
	/** @field cast=(HWND) */
	public int /*long*/ hwndParent;
	public int dwFlags;
	public int nToolBarId;
	/** @field cast=(HINSTANCE) */
	public int /*long*/ hInstRes;
	public int nBmpId;
	public int cBmpImages;
	/** @field cast=(HWND) */
	public int /*long*/ hwndMB;
	public static final int sizeof = OS.IsSP ? 36 : OS.SHMENUBARINFO_sizeof ();
}
