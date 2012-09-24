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

public class NMLVFINDITEM extends NMHDR {
	public int iStart;
//	LVFINDINFO lvfi;
	/** @field accessor=lvfi.flags */
	public int flags;
	/** @field accessor=lvfi.psz,cast=(LPCTSTR) */
	public long /*int*/ psz;
	/** @field accessor=lvfi.lParam */
	public long /*int*/ lParam;
//	POINT pt;
	/** @field accessor=lvfi.pt.x */
	public int x;
	/** @field accessor=lvfi.pt.y */
	public int y;
	/** @field accessor=lvfi.vkDirection */
	public int vkDirection;
	public static final int sizeof = OS.NMLVFINDITEM_sizeof ();
}
