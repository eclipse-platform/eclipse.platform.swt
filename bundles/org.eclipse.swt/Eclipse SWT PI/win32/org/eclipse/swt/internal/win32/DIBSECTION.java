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

public class DIBSECTION extends BITMAP {
	/** @field accessor=dsBmih.biSize */
	public int biSize;
	/** @field accessor=dsBmih.biWidth */
	public int biWidth;
	/** @field accessor=dsBmih.biHeight */
	public int biHeight;
	/** @field accessor=dsBmih.biPlanes */
	public short biPlanes;
	/** @field accessor=dsBmih.biBitCount */
	public short biBitCount;
	/** @field accessor=dsBmih.biCompression */
	public int biCompression;
	/** @field accessor=dsBmih.biSizeImage */
	public int biSizeImage;
	/** @field accessor=dsBmih.biXPelsPerMeter */
	public int biXPelsPerMeter;
	/** @field accessor=dsBmih.biYPelsPerMeter */
	public int biYPelsPerMeter;
	/** @field accessor=dsBmih.biClrUsed */
	public int biClrUsed;
	/** @field accessor=dsBmih.biClrImportant */
	public int biClrImportant;
	/** @field accessor=dsBitfields[0] */
	public int dsBitfields0;
	/** @field accessor=dsBitfields[1] */
	public int dsBitfields1;
	/** @field accessor=dsBitfields[2] */
	public int dsBitfields2;
	/** @field cast=(HANDLE) */
	public long /*int*/ dshSection;
	public int dsOffset;
	public static final int sizeof = OS.DIBSECTION_sizeof ();
}
