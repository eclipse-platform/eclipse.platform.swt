/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;


public class VARDESC2 {
	public int memid;
	public int lpstrSchema;
	public int unionField;
	//ELEMDESC elemdescVar
	//TYPEDESC elemdescVar.tdesc
	public int elemdescVar_tdesc_union;
	public short elemdescVar_tdesc_vt;
	//IDLDESC elemdescFunc.idldesc
	public int elemdescFunc_idldesc_dwReserved;
	public short elemdescFunc_idldesc_wIDLFlags;
	public short wVarFlags;
	public int varkind;
	public static final int sizeof = 36;
}
