/*******************************************************************************
 * Copyright (c) 2010, 2012 IBM Corporation and others.
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

public class GESTUREINFO {
	public int cbSize;
	public int dwFlags;
	public int dwID;
	/** @field cast=(HWND) */
	public long hwndTarget;
	//	POINTS ptsLocation
	/** @field accessor=ptsLocation.x */
	public short x;
	/** @field accessor=ptsLocation.y */
	public short y;
	public int dwInstanceID;
	public int dwSequenceID;
	public long ullArguments;
	public int cbExtraArgs;
	public static final int sizeof = OS.GESTUREINFO_sizeof ();
}
