/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
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
	public short size_w;
	public short size_h;
	public int palette_tag;
	public int colors;
	public int alpha;
	public int transparent;
	public byte format;
	public byte flags;
	public byte ghost_bpl;
	public byte spare1;
	public int ghost_bitmap;
	public int mask_bpl;
	public int mask_bm;
	public int palette;
	public int image;
	public static final int sizeof = 56;
}
