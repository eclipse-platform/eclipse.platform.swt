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

public final class TYPEATTR {
//	GUID guid 
	/** @field accessor=guid.Data1 */
	public int guid_Data1;
	/** @field accessor=guid.Data2 */
	public short guid_Data2;
	/** @field accessor=guid.Data3 */
	public short guid_Data3;
	/** @field accessor=guid.Data4 */
	public byte[] guid_Data4 = new byte[8];
	public int lcid;
	public int dwReserved;
	public int memidConstructor;
	public int memidDestructor;
	/** @field cast=(OLECHAR FAR *) */
	public long /*int*/ lpstrSchema;
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
	/** @field accessor=tdescAlias.lptdesc,cast=(struct FARSTRUCT tagTYPEDESC FAR *) */
	public long /*int*/ tdescAlias_unionField;
	/** @field accessor=tdescAlias.vt */
	public short tdescAlias_vt;
//	IDLDESC idldesctype
	/** @field accessor=idldescType.dwReserved */
	public int idldescType_dwReserved;
	/** @field accessor=idldescType.wIDLFlags */
	public short idldescType_wIDLFlags;
	public static final int sizeof = COM.TYPEATTR_sizeof ();
}
