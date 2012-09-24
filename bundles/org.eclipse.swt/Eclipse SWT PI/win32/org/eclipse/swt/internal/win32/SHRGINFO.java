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

public class SHRGINFO {
	public int cbSize;
	/** @field cast=(HWND) */
	public long /*int*/ hwndClient;
//	POINT ptDown
	/** @field accessor=ptDown.x */
	public int ptDown_x;
	/** @field accessor=ptDown.y */
	public int ptDown_y;
	public int dwFlags;
	public static final int sizeof = OS.SHRGINFO_sizeof ();
}
