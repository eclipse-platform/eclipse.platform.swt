package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public final class TYPEATTR
{
	//public GUID guid 
	public int    guid_data1;
	public short  guid_data2;
	public short  guid_data3;
	public byte   guid_b0;
	public byte   guid_b1;
	public byte   guid_b2;
	public byte   guid_b3;
	public byte   guid_b4;
	public byte   guid_b5;
	public byte   guid_b6;
	public byte   guid_b7;
	
	public int    lcid;
	public int    dwReserved;
	public int    memidConstructor;
	public int    memidDestructor;
	public int    lpstrSchema;
	public int    cbSizeInstance;
	public int    typekind;
	public short  cFuncs;
	public short  cVars;
	public short  cImplTypes;
	public short  cbSizeVft;
	public short  cbAlignment;
	public short  wTypeFlags;
	public short  wMajorVerNum;
	public short  wMinorVerNum;

	// public TYPEDESC tdescAlias
	public int    tdescAlias_unionField;
	public short  tdescAlias_vt;
	// this filler field is required for proper byte alignment
	public short  filler;
	
	// public IDLDESC  idldesctype
	public int    idldescType_dwReserved;
	public short  idldescType_wIDLFlags;
	
	public static final int sizeof = 74;
}
