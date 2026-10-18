/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

public class BITMAPINFOHEADER {
	public int biSize;
	public int biWidth;
	public int biHeight;
	public short biPlanes;
	public short biBitCount;
	public int biCompression;
	public int biSizeImage;
	public int biXPelsPerMeter;
	public int biYPelsPerMeter;
	public int biClrUsed;
	public int biClrImportant;
	public static final int sizeof = OS.BITMAPINFOHEADER_sizeof ();
}
