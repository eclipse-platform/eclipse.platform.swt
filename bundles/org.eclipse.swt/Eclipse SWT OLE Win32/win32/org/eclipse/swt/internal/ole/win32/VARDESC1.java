package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class VARDESC1 {
	public int memid;
	public int lpstrSchema;
	public int unionField;
	//ELEMDESC elemdescVar
	//TYPEDESC elemdescVar.tdesc
	public int elemdescVar_tdesc_union;
	public short elemdescVar_tdesc_vt;
	public short elemdescVar_tdesc_filler;
	//PARAMDESC elemdescFunc.paramdesc
	public int elemdescVar_paramdesc_pparamdescex;
	public short elemdescVar_paramdesc_wParamFlags;
	public short elemdescVar_paramdesc_filler;
	public short wVarFlags;
	public short filler;
	public int varkind;
	
	public static final int sizeof = 36;
}
