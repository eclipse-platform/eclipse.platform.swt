package org.eclipse.swt.internal.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class FUNCDESC2 {
	public int memid;
	public int lprgscode;
	public int lprgelemdescParam;
	public int funckind;
	public int invkind;
	public int callconv;
	public short cParams;
	public short cParamsOpt;
	public short oVft;
	public short cScodes;
	//ELEMDESC elemdescFunc;
	//TYPEDESC elemdescFunc.tdesc
	public int elemdescFunc_tdesc_union;
	public short elemdescFunc_tdesc_vt;
	public short elemdescFunc_tdesc_filler;
	//IDLDESC elemdescFunc.idldesc
	public int elemdescFunc_idldesc_dwReserved;
	public short elemdescFunc_idldesc_wIDLFlags;
	public short elemdescFunc_idldesc_filler;
	public short wFuncFlags;
	
	public static final int sizeof = 50;

}
