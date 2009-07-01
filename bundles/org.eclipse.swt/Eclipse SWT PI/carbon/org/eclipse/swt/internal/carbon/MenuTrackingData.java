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

 
public class MenuTrackingData {
	/** @field cast=(MenuRef) */
	public int menu; 
	public short itemSelected; 
	public short itemUnderMouse; 
//	  Rect itemRect; 
	/** @field accessor=itemRect.top */
	public short top;
	/** @field accessor=itemRect.left */
	public short left;
	/** @field accessor=itemRect.bottom */
	public short bottom;
	/** @field accessor=itemRect.right */
	public short right;
	public int virtualMenuTop; 
	public int virtualMenuBottom;
	public static int sizeof = 24;
}
