package org.eclipse.swt.internal.ole.win32;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

public class FUNCDESC1 {
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
	//PARAMDESC elemdescFunc.paramdesc
	public int elemdescFunc_paramdesc_pparamdescex;
	public short elemdescFunc_paramdesc_wParamFlags;
	public short elemdescFunc_paramdesc_filler;
	public short wFuncFlags;
	
	public static final int sizeof = 50;

}