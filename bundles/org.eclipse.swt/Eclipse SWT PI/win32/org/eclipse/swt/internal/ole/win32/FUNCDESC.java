/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class FUNCDESC {
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
//	ELEMDESC elemdescFunc;
//	TYPEDESC elemdescFunc.tdesc
	public int elemdescFunc_tdesc_union;
	public short elemdescFunc_tdesc_vt;
//	PARAMDESC elemdescFunc.paramdesc
	public int elemdescFunc_paramdesc_pparamdescex;
	public short elemdescFunc_paramdesc_wParamFlags;
	public short wFuncFlags;
	public static final int sizeof = 50;
}
