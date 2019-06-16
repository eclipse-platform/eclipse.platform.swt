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

public class BITMAP {
	public int bmType;
	public int bmWidth;
	public int bmHeight;
	public int bmWidthBytes;
	public short bmPlanes;
	public short bmBitsPixel;
	/** @field cast=(LPVOID) */
	public long bmBits;
	public static final int sizeof = OS.BITMAP_sizeof ();
}
