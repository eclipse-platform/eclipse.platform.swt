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


public class PixMap extends BitMap {
	public short pmVersion;
	public short packType;
	public int packSize;
	public int hRes;
	public int vRes;
	public short pixelType;
	public short pixelSize;
	public short cmpCount;
	public short cmpSize;
	public int pixelFormat;
	/** @field cast=(CTabHandle) */
	public int pmTable;
	/** @field cast=(void *) */
	public int pmExt;
	public static final int sizeof = 50;
}
