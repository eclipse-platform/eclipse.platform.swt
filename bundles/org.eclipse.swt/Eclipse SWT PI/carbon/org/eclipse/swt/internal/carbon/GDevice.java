/**********************************************************************
 * Copyright (c) 2003, 2008 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;


public class GDevice {
	public short gdRefNum;
	public short gdID;
	public short gdType;
	/** @field cast=(ITabHandle) */
	public int gdITable;
	public short gdResPref;
	/** @field cast=(SProcHndl) */
	public int gdSearchProc;
	/** @field cast=(CProcHndl) */
	public int gdCompProc;
	public short gdFlags;
	/** @field cast=(PixMapHandle) */
	public int gdPMap;
	public int gdRefCon;
	/** @field cast=(GDHandle) */
	public int gdNextGD;
	//Rect gdRect;
	/** @field accessor=gdRect.left */
	public short left;
	/** @field accessor=gdRect.top */
	public short top;
	/** @field accessor=gdRect.right */
	public short right;
	/** @field accessor=gdRect.bottom */
	public short bottom;
	public int gdMode;
	public short gdCCBytes;
	public short gdCCDepth;
	/** @field cast=(Handle) */
	public int gdCCXData;
	/** @field cast=(Handle) */
	public int gdCCXMask;
	/** @field cast=(Handle) */
	public int gdExt;
	public static final int sizeof = 62;
}
