package org.eclipse.swt.internal.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
