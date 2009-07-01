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


public class ATSTrapezoid {
	//FixedPoint upperLeft;
	/** @field accessor=upperLeft.x */
	public int upperLeft_x;
	/** @field accessor=upperLeft.y */
	public int upperLeft_y;
	//FixedPoint upperRight;
	/** @field accessor=upperRight.x */
	public int upperRight_x;
	/** @field accessor=upperRight.y */
	public int upperRight_y;
	//FixedPoint lowerRight;
	/** @field accessor=lowerRight.x */
	public int lowerRight_x;
	/** @field accessor=lowerRight.y */
	public int lowerRight_y;
	//FixedPoint lowerLeft;
	/** @field accessor=lowerLeft.x */
	public int lowerLeft_x;
	/** @field accessor=lowerLeft.y */
	public int lowerLeft_y;
	public static final int sizeof = 32;
}
