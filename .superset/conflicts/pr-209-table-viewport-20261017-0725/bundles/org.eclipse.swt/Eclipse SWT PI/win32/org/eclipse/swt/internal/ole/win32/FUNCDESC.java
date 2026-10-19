/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class FUNCDESC {
	/** @field cast=(MEMBERID) */
	public int memid;
	/** @field cast=(SCODE FAR *) */
	public long lprgscode;
	/** @field cast=(ELEMDESC FAR *) */
	public long lprgelemdescParam;
	/** @field cast=(FUNCKIND) */
	public int funckind;
	/** @field cast=(INVOKEKIND) */
	public int invkind;
	/** @field cast=(CALLCONV) */
	public int callconv;
	public short cParams;
	public short cParamsOpt;
	public short oVft;
	public short cScodes;
//	ELEMDESC elemdescFunc;
//	TYPEDESC elemdescFunc.tdesc
	/** @field accessor=elemdescFunc.tdesc.lptdesc,cast=(struct FARSTRUCT tagTYPEDESC FAR* ) */
	public long elemdescFunc_tdesc_union;
	/** @field accessor=elemdescFunc.tdesc.vt */
	public short elemdescFunc_tdesc_vt;
//	PARAMDESC elemdescFunc.paramdesc
	/** @field accessor=elemdescFunc.paramdesc.pparamdescex,cast=(LPPARAMDESCEX) */
	public long elemdescFunc_paramdesc_pparamdescex;
	/** @field accessor=elemdescFunc.paramdesc.wParamFlags */
	public short elemdescFunc_paramdesc_wParamFlags;
	public short wFuncFlags;
	public static final int sizeof = COM.FUNCDESC_sizeof ();
}
