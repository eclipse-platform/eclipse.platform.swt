package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class VARDESC1 {
	public int memid;
	public int lpstrSchema;
	public int unionField;
	//ELEMDESC elemdescVar
	//TYPEDESC elemdescVar.tdesc
	public int elemdescVar_tdesc_union;
	public short elemdescVar_tdesc_vt;
	//PARAMDESC elemdescFunc.paramdesc
	public int elemdescVar_paramdesc_pparamdescex;
	public short elemdescVar_paramdesc_wParamFlags;
	public short wVarFlags;
	public int varkind;
	
	public static final int sizeof = 36;
}
