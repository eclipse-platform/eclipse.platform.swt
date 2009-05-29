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


public class PhCursorDef_t {
//	PhRegionDataHdr_t hdr;
	/** @field accessor=hdr.len */
	public short hdr_len;
	/** @field accessor=hdr.type */
	public short hdr_type;
//	PhPoint_t size1;
	/** @field accessor=size1.x */
	public short size1_x;
	/** @field accessor=size1.y */
	public short size1_y;
//	PhPoint_t offset1;
	/** @field accessor=offset1.x */
	public short offset1_x;
	/** @field accessor=offset1.y */
	public short offset1_y;
	public int color1;
	public byte bytesperline1;
//	byte zero1; // 3 bytes
//	PhPoint_t size2;
	/** @field accessor=size2.x */
	public short size2_x;
	/** @field accessor=size2.y */
	public short size2_y;
//	PhPoint_t offset2;
	/** @field accessor=offset2.x */
	public short offset2_x;
	/** @field accessor=offset2.y */
	public short offset2_y;
	public int color2;
	public byte bytesperline2;
//	byte Spare; // 14 bytes
//	byte images; // variable number of bytes
	public static final int sizeof = 47;
}
