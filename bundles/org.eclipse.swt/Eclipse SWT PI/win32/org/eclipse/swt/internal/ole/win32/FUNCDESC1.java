package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
	//PARAMDESC elemdescFunc.paramdesc
	public int elemdescFunc_paramdesc_pparamdescex;
	public short elemdescFunc_paramdesc_wParamFlags;
	public short wFuncFlags;
	
	public static final int sizeof = 50;

}
