package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
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
