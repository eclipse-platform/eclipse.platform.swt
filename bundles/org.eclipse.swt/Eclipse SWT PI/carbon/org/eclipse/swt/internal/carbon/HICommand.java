package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class HICommand {
	public int attributes;
	public int commandID;
	public int menu_menuRef;
	public short menu_menuItemIndex;
	
	public static final int sizeof = 14;
}
