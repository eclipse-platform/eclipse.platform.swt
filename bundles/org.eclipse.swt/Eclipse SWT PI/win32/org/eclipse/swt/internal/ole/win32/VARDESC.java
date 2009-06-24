/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public class VARDESC {
	public int memid;
	/** @field cast=(OLECHAR FAR *) */
	public int /*long*/ lpstrSchema;
	public int oInst;
//	ELEMDESC elemdescVar
//	TYPEDESC elemdescVar.tdesc
	/** @field accessor=elemdescVar.tdesc.lptdesc,cast=(struct FARSTRUCT tagTYPEDESC FAR *) */
	public int /*long*/ elemdescVar_tdesc_union;
	/** @field accessor=elemdescVar.tdesc.vt */
	public short elemdescVar_tdesc_vt;
//	PARAMDESC elemdescFunc.paramdesc
	/** @field accessor=elemdescVar.paramdesc.pparamdescex,cast=(LPPARAMDESCEX) */
	public int /*long*/ elemdescVar_paramdesc_pparamdescex;
	/** @field accessor=elemdescVar.paramdesc.wParamFlags */
	public short elemdescVar_paramdesc_wParamFlags;
	public short wVarFlags;
	public int varkind;
	public static final int sizeof = COM.VARDESC_sizeof ();
}
