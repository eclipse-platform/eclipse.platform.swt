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

public class NMREBARCHEVRON extends NMHDR {
	public int uBand;
	public int wID;
	public long /*int*/ lParam;
//	RECT rc;
	/** @field accessor=rc.left */
	public int left; 
	/** @field accessor=rc.top */
	public int top; 
	/** @field accessor=rc.right */
	public int right; 
	/** @field accessor=rc.bottom */
	public int bottom;
	public long /*int*/ lParamNM;
	public static int sizeof = OS.NMREBARCHEVRON_sizeof ();
}
