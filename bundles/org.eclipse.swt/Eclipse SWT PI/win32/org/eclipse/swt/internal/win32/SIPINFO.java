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

public class SIPINFO {
	public int cbSize;
	public int fdwFlags;
//	RECT rcVisibleDesktop
	/** @field accessor=rcVisibleDesktop.left */
	public int rcVisibleDesktop_left;
	/** @field accessor=rcVisibleDesktop.top */
	public int rcVisibleDesktop_top;
	/** @field accessor=rcVisibleDesktop.right */
	public int rcVisibleDesktop_right;
	/** @field accessor=rcVisibleDesktop.bottom */
	public int rcVisibleDesktop_bottom;
//	RECT rcSipRect
	/** @field accessor=rcSipRect.left */
	public int rcSipRect_left;
	/** @field accessor=rcSipRect.top */
	public int rcSipRect_top;
	/** @field accessor=rcSipRect.right */
	public int rcSipRect_right;
	/** @field accessor=rcSipRect.bottom */
	public int rcSipRect_bottom;
	public int dwImDataSize;
	/** @field cast=(void *) */
	public long /*int*/ pvImData;
	public static final int sizeof = OS.SIPINFO_sizeof ();
}
