package org.eclipse.swt.internal.photon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class FontQueryInfo {
	public byte[] font = new byte[OS.MAX_FONT_TAG];
	public byte[] desc = new byte[OS.MAX_DESC_LENGTH];
	public short size;
	public short style;
	public short ascender;
	public short descender;
	public short width;
	public int lochar;
	public int hichar;
	public static final int sizeof = 140;
}
