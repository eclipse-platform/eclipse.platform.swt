package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XmListCallbackStruct {
	public int reason;
	public int event;
	public int item;
	public int item_length;
	public int item_position;
	public int selected_items;
	public int selected_item_count;
	public int selected_item_positions;
	public byte selection_type;
	public byte auto_selection_type;
	public static final int sizeof = 34;
}
