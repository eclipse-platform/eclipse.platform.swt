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

public final class TYPEATTR {
//	GUID guid 
	public int guid_Data1;
	public short guid_Data2;
	public short guid_Data3;
	public byte[] guid_Data4 = new byte[8];
	public int lcid;
	public int dwReserved;
	public int memidConstructor;
	public int memidDestructor;
	public int lpstrSchema;
	public int cbSizeInstance;
	public int typekind;
	public short cFuncs;
	public short cVars;
	public short cImplTypes;
	public short cbSizeVft;
	public short cbAlignment;
	public short wTypeFlags;
	public short wMajorVerNum;
	public short wMinorVerNum;
//	TYPEDESC tdescAlias
	public int tdescAlias_unionField;
	public short tdescAlias_vt;
//	IDLDESC idldesctype
	public int idldescType_dwReserved;
	public short idldescType_wIDLFlags;
	public static final int sizeof = 74;
}
