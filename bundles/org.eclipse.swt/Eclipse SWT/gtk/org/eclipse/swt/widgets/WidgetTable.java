package org.eclipse.swt.widgets;

/*
 * (c) Copyright IBM Corp. 2000, 2001, 2002.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.gtk.*;

class WidgetTable {
	static int FreeSlot = 0;
	static int GrowSize = 1024;
	static int [] IndexTable = new int [GrowSize];
	static Widget [] WidgetTable = new Widget [GrowSize];
	static {
		for (int i=0; i<GrowSize-1; i++) IndexTable [i] = i + 1;
		IndexTable [GrowSize - 1] = -1;
	}
	
public static synchronized Widget get (int handle) {
	if (handle == 0) return null;
	int index = OS.gtk_object_get_user_data (handle) - 1;
	if (0 <= index && index < WidgetTable.length) return WidgetTable [index];
	return null;
}

public synchronized static void put(int handle, Widget widget) {
	if (handle == 0) return;
	if (FreeSlot == -1) {
		int length = (FreeSlot = IndexTable.length) + GrowSize;
		int[] newIndexTable = new int[length];
		Widget[] newWidgetTable = new Widget [length];
		System.arraycopy (IndexTable, 0, newIndexTable, 0, FreeSlot);
		System.arraycopy (WidgetTable, 0, newWidgetTable, 0, FreeSlot);
		for (int i = FreeSlot; i < length - 1; i++) {
			newIndexTable[i] = i + 1;
		}
		newIndexTable[length - 1] = -1;
		IndexTable = newIndexTable;
		WidgetTable = newWidgetTable;
	}
	int index = FreeSlot + 1;
	OS.gtk_object_set_user_data (handle, index);
	int oldSlot = FreeSlot;
	FreeSlot = IndexTable[oldSlot];
	IndexTable [oldSlot] = -2;
	WidgetTable [oldSlot] = widget;
}

public static synchronized Widget remove (int handle) {
	if (handle == 0) return null;
	Widget widget = null;
	int index = OS.gtk_object_get_user_data (handle) - 1;
	if (0 <= index && index < WidgetTable.length) {
		widget = WidgetTable [index];
		WidgetTable [index] = null;
		IndexTable [index] = FreeSlot;
		FreeSlot = index;
		OS.gtk_object_set_user_data (handle, 0);
	}
	return widget;
}

public static synchronized Shell [] shells () {
	int length = 0;
	for (int i=0; i<WidgetTable.length; i++) {
		Widget widget = WidgetTable [i];
		if (widget != null && widget instanceof Shell) length++;
	}
	int index = 0;
	Shell [] result = new Shell [length];
	for (int i=0; i<WidgetTable.length; i++) {
		Widget widget = WidgetTable [i];
		if (widget != null && widget instanceof Shell) {
			int j = 0;
			while (j < index) {
				if (result [j] == widget) break;
				j++;
			}
			if (j == index)	result [index++] = (Shell) widget;
		}
	}
	if (index == length) return result;
	Shell [] newResult = new Shell [index];
	System.arraycopy (result, 0, newResult, 0, index);
	return newResult;
}

public static synchronized int size () {
	int size = 0;
	for (int i=0; i<WidgetTable.length; i++) {
		if (WidgetTable [i] != null) size++;
	}
	return size;
}

}
