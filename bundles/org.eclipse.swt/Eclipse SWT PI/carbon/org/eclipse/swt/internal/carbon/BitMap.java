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


public class BitMap {
	/** @field cast=(void *) */
	public int baseAddr;
	public short rowBytes;
	//Rect bounds;
	/** @field accessor=bounds.top */
	public short top;
	/** @field accessor=bounds.left */
	public short left;
	/** @field accessor=bounds.bottom */
	public short bottom;
	/** @field accessor=bounds.right */
	public short right;
	public static final int sizeof = 14;
}
