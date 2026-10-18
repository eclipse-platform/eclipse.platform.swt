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

public class NMLVFINDITEM extends NMHDR {
	public int iStart;
//	LVFINDINFO lvfi;
	/** @field accessor=lvfi.flags */
	public int flags;
	/** @field accessor=lvfi.psz,cast=(LPCTSTR) */
	public long psz;
	/** @field accessor=lvfi.lParam */
	public long lParam;
//	POINT pt;
	/** @field accessor=lvfi.pt.x */
	public int x;
	/** @field accessor=lvfi.pt.y */
	public int y;
	/** @field accessor=lvfi.vkDirection */
	public int vkDirection;
	public static final int sizeof = OS.NMLVFINDITEM_sizeof ();
}
