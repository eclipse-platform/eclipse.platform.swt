/**********************************************************************
 * Copyright (c) 2003-2004 IBM Corp.
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
	public int gdITable;
	public short gdResPref;
	public int gdSearchProc;
	public int gdCompProc;
	public short gdFlags;
	public int gdPMap;
	public int gdRefCon;
	public int gdNextGD;
	//Rect gdRect;
	public short left;
	public short top;
	public short right;
	public short bottom;
	public int gdMode;
	public short gdCCBytes;
	public short gdCCDepth;
	public int gdCCXData;
	public int gdCCXMask;
	public int gdExt;
	public static final int sizeof = 62;
}
