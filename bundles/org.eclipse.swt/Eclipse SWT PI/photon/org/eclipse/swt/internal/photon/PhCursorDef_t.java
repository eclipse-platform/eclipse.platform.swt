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


public class PhCursorDef_t {
//	PhRegionDataHdr_t hdr;
	public short hdr_len;
	public short hdr_type;
//	PhPoint_t size1;
	public short size1_x;
	public short size1_y;
//	PhPoint_t offset1;
	public short offset1_x;
	public short offset1_y;
	public int color1;
	public byte bytesperline1;
//	byte zero1; // 3 bytes
//	PhPoint_t size2;
	public short size2_x;
	public short size2_y;
//	PhPoint_t offset2;
	public short offset2_x;
	public short offset2_y;
	public int color2;
	public byte bytesperline2;
//	byte Spare; // 14 bytes
//	byte images; // variable number of bytes
	public static final int sizeof = 47;
}
