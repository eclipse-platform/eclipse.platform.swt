package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

public class VARDESC2 {
	public int memid;
	public int lpstrSchema;
	public int unionField;
	//ELEMDESC elemdescVar
	//TYPEDESC elemdescVar.tdesc
	public int elemdescVar_tdesc_union;
	public short elemdescVar_tdesc_vt;
	public short elemdescVar_tdesc_filler;
	//IDLDESC elemdescFunc.idldesc
	public int elemdescFunc_idldesc_dwReserved;
	public short elemdescFunc_idldesc_wIDLFlags;
	public short elemdescFunc_idldesc_filler;
	public short wVarFlags;
	public short filler;
	public int varkind;
	
	public static final int sizeof = 36;
}