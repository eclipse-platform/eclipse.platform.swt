package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class MenuTrackingData {
	public int menu; 
	public short itemSelected; 
	public short itemUnderMouse; 
//	  Rect itemRect; 
	public short top;
	public short left;
	public short bottom;
	public short right;
	public int virtualMenuTop; 
	public int virtualMenuBottom;
	public static int sizeof = 24;
}
