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

public class VARDESC {
	public int memid;
	/** @field cast=(OLECHAR FAR *) */
	public long lpstrSchema;
	public int oInst;
//	ELEMDESC elemdescVar
//	TYPEDESC elemdescVar.tdesc
	/** @field accessor=elemdescVar.tdesc.lptdesc,cast=(struct FARSTRUCT tagTYPEDESC FAR *) */
	public long elemdescVar_tdesc_union;
	/** @field accessor=elemdescVar.tdesc.vt */
	public short elemdescVar_tdesc_vt;
//	PARAMDESC elemdescFunc.paramdesc
	/** @field accessor=elemdescVar.paramdesc.pparamdescex,cast=(LPPARAMDESCEX) */
	public long elemdescVar_paramdesc_pparamdescex;
	/** @field accessor=elemdescVar.paramdesc.wParamFlags */
	public short elemdescVar_paramdesc_wParamFlags;
	public short wVarFlags;
	public int varkind;
	public static final int sizeof = COM.VARDESC_sizeof ();
}
