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
	static Control [] ControlTable = new Control [GrowSize];
	static {
		for (int i=0; i<GrowSize-1; i++) IndexTable [i] = i + 1;
		IndexTable [GrowSize - 1] = -1;
	}
	
public static synchronized Control get (int handle) {
	if (handle == 0) return null;
	int index = OS.GetControlReference (handle) - 1;
	if (0 <= index && index < ControlTable.length) return ControlTable [index];
	return null;
}

public synchronized static void put (int handle, Control control) {
	if (handle == 0) return;
	if (FreeSlot == -1) {
		int length = (FreeSlot = IndexTable.length) + GrowSize;
		int [] newIndexTable = new int [length];
		Control [] newControlTable = new Control [length];
		System.arraycopy (IndexTable, 0, newIndexTable, 0, FreeSlot);
		System.arraycopy (ControlTable, 0, newControlTable, 0, FreeSlot);
		for (int i=FreeSlot; i<length-1; i++) {
			newIndexTable [i] = i + 1;
		}
		newIndexTable [length - 1] = -1;
		IndexTable = newIndexTable;
		ControlTable = newControlTable;
	}
	OS.SetControlReference (handle, FreeSlot + 1);
	int oldSlot = FreeSlot;
	FreeSlot = IndexTable [oldSlot];
	IndexTable [oldSlot] = -2;
	ControlTable [oldSlot] = control;
}

public static synchronized Control remove (int handle) {
	if (handle == 0) return null;
	Control control = null;
	int index = OS.GetControlReference (handle) - 1;
	if (0 <= index && index < ControlTable.length) {
		control = ControlTable [index];
		ControlTable [index] = null;
		IndexTable [index] = FreeSlot;
		FreeSlot = index;
		OS.SetControlReference (handle, 0);	
	}
	return control;
}

public static synchronized Shell [] shells () {
      int count = 0;
      for (int i= 0; i < ControlTable.length; i++) {
            if (ControlTable[i] instanceof Shell) count++;
      }
      int index= 0;
      Shell [] result = new Shell [count];
      for (int i= 0; i < ControlTable.length; i++) {
            Control control = ControlTable [i];
            if (control != null && control instanceof Shell) {
                  result [index++]= (Shell) control;
            }
      }
      return result;
}

public static synchronized int size () {
	int length = 0;
	for (int i=0; i<ControlTable.length; i++) {
		if (ControlTable [i] != null) length++;
	}
	return length;
}
}
