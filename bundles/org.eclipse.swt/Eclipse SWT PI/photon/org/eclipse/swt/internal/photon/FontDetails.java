package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class FontDetails {
	public byte[] desc = new byte[OS.MAX_DESC_LENGTH];
	public byte[] stem = new byte[OS.MAX_FONT_TAG];
	public short losize;
	public short hisize;
	public short flags;
	public static final int sizeof = 128;
}
