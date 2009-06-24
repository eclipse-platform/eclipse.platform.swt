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
package org.eclipse.swt.internal.photon;


public class PhImage_t {
	public int type;
	public int image_tag;
	public int bpl;
	/** @field accessor=size.w */
	public short size_w;
	/** @field accessor=size.h */
	public short size_h;
	public int palette_tag;
	public int colors;
	/** @field cast=(PgAlpha_t *) */
	public int alpha;
	public int transparent;
	public byte format;
	public byte flags;
	public byte ghost_bpl;
	public byte spare1;
	/** @field cast=(char *) */
	public int ghost_bitmap;
	public int mask_bpl;
	/** @field cast=(char *) */
	public int mask_bm;
	/** @field cast=(PgColor_t *) */
	public int palette;
	/** @field cast=(char *) */
	public int image;
	public static final int sizeof = 56;
}
