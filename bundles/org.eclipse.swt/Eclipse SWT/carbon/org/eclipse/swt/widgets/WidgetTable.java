package org.eclipse.swt.widgets;

import org.eclipse.swt.internal.carbon.*;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

public class WidgetTable {
	static int FreeSlot = 0;
	static int GrowSize = 1024;
	static int [] IndexTable = new int [GrowSize];
	static Widget [] WidgetTable = new Widget [GrowSize];
	static final int SWT0 = ('s'<<24) + ('w'<<16) + ('t'<<8) + '0';
	static int [] Property = new int [1];
	static {
		for (int i=0; i<GrowSize-1; i++) IndexTable [i] = i + 1;
		IndexTable [GrowSize - 1] = -1;
	}
	
public static synchronized Widget get (int handle) {
	if (handle == 0) return null;
	Property [0] = 0;
	OS.GetControlProperty (handle, SWT0, SWT0, 4, null, Property);
	int index = Property [0] - 1;
	if (0 <= index && index < WidgetTable.length) return WidgetTable [index];
	return null;
}

public synchronized static void put (int handle, Widget widget) {
	if (handle == 0) return;
	if (FreeSlot == -1) {
		int length = (FreeSlot = IndexTable.length) + GrowSize;
		int [] newIndexTable = new int [length];
		Widget [] newWidgetTable = new Widget [length];
		System.arraycopy (IndexTable, 0, newIndexTable, 0, FreeSlot);
		System.arraycopy (WidgetTable, 0, newWidgetTable, 0, FreeSlot);
		for (int i=FreeSlot; i<length-1; i++) {
			newIndexTable [i] = i + 1;
		}
		newIndexTable [length - 1] = -1;
		IndexTable = newIndexTable;
		WidgetTable = newWidgetTable;
	}
	Property [0] = FreeSlot + 1;
	OS.SetControlProperty (handle, SWT0, SWT0, 4, Property);
	int oldSlot = FreeSlot;
	FreeSlot = IndexTable [oldSlot];
	IndexTable [oldSlot] = -2;
	WidgetTable [oldSlot] = widget;
}

public static synchronized Widget remove (int handle) {
	if (handle == 0) return null;
	Widget widget = null;
	Property [0] = 0;
	OS.GetControlProperty (handle, SWT0, SWT0, 4, null, Property);
	int index = Property [0] - 1;
	if (0 <= index && index < WidgetTable.length) {
		widget = WidgetTable [index];
		WidgetTable [index] = null;
		IndexTable [index] = FreeSlot;
		FreeSlot = index;
		OS.RemoveControlProperty (handle, SWT0, SWT0);

	}
	return widget;
}

public static synchronized Shell [] shells () {
      int count = 0;
      for (int i= 0; i < WidgetTable.length; i++) {
            if (WidgetTable[i] instanceof Shell) count++;
      }
      int index= 0;
      Shell [] result = new Shell [count];
      for (int i= 0; i < WidgetTable.length; i++) {
            Widget widget = WidgetTable [i];
            if (widget != null && widget instanceof Shell) {
                  result [index++]= (Shell) widget;
            }
      }
      return result;
}

public static synchronized int size () {
	int length = 0;
	for (int i=0; i<WidgetTable.length; i++) {
		if (WidgetTable [i] != null) length++;
	}
	return length;
}
}
