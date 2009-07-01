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
package org.eclipse.swt.internal.win32;

public class NMREBARCHILDSIZE extends NMHDR {
	public int uBand;
	public int wID;
//  RECT rcChild;
	/** @field accessor=rcChild.left */
	public int rcChild_left;
	/** @field accessor=rcChild.top */
	public int rcChild_top;
	/** @field accessor=rcChild.right */
	public int rcChild_right;
	/** @field accessor=rcChild.bottom */
	public int rcChild_bottom;
//  RECT rcBand;
	/** @field accessor=rcBand.left */
	public int rcBand_left;
	/** @field accessor=rcBand.top */
	public int rcBand_top;
	/** @field accessor=rcBand.right */
	public int rcBand_right;
	/** @field accessor=rcBand.bottom */
	public int rcBand_bottom;
	public static final int sizeof = OS.NMREBARCHILDSIZE_sizeof ();
}
