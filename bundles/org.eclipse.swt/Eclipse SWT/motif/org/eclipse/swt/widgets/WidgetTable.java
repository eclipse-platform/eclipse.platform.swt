/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.widgets;

 
import org.eclipse.swt.internal.motif.*;

class WidgetTable {
	static int FreeSlot = 0;
	static int GrowSize = 1024;
	static int [] IndexTable = new int [GrowSize];
	static Shell [] Shells = new Shell [GrowSize / 8];
	static Widget [] WidgetTable = new Widget [GrowSize];
	static int [] ArgList = {OS.XmNuserData, 0};
	static {
		for (int i=0; i<GrowSize-1; i++) IndexTable [i] = i + 1;
		IndexTable [GrowSize - 1] = -1;	
	}
public static synchronized Widget get (int handle) {
	if (handle == 0) return null;
	if (OS.XtIsSubclass (handle, OS.shellWidgetClass ())) {
		for (int i=0; i<Shells.length; i++) {
			Widget shell = Shells [i];
			if ((shell != null) && (shell.topHandle () == handle)) return shell;
		}
		return null;
	}
	ArgList [1] = 0;
	OS.XtGetValues (handle, ArgList, ArgList.length / 2);
	if (ArgList [1] == 0) return null;
	int index = ArgList [1] - 1;
	if (0 <= index && index < WidgetTable.length) return WidgetTable [index];
	return null;
}
public synchronized static void put (int handle, Widget widget) {
	if (handle == 0) return;
	if (OS.XtIsSubclass (handle, OS.shellWidgetClass ())) {
		for (int i=0; i<Shells.length; i++) {
			if (Shells [i] == null) {
				Shells [i] = (Shell) widget;
				return;
			}
		}
		Shell [] newShells = new Shell [Shells.length + GrowSize / 8];
		System.arraycopy (Shells, 0, newShells, 0, Shells.length);
		newShells [Shells.length] = (Shell) widget;
		Shells = newShells;
		return;
	}
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
	ArgList [1] = FreeSlot + 1;
	OS.XtSetValues (handle, ArgList, ArgList.length / 2);
	int oldSlot = FreeSlot;
	FreeSlot = IndexTable [oldSlot];
	IndexTable [oldSlot] = -2;
	WidgetTable [oldSlot] = widget;
}
public static synchronized Widget remove (int handle) {
	if (handle == 0) return null;
	if (OS.XtIsSubclass (handle, OS.shellWidgetClass ())) {
		for (int i=0; i<Shells.length; i++) {
			Widget shell = Shells [i];
			if ((shell != null) && (shell.topHandle () == handle)) {
				Shells [i] = null;
				return shell;
			}
		}
		return null;
	}
	ArgList [1] = 0;
	Widget widget = null;
	OS.XtGetValues (handle, ArgList, ArgList.length / 2);
	int index = ArgList [1] - 1;
	if (0 <= index && index < WidgetTable.length) {
		widget = WidgetTable [index];
		WidgetTable [index] = null;
		IndexTable [index] = FreeSlot;
		FreeSlot = index;
		ArgList [1] = 0;
		OS.XtSetValues (handle, ArgList, ArgList.length / 2);
	}
	return widget;
}
public static synchronized Shell [] shells () {
	int length = 0;
	for (int i=0; i<Shells.length; i++) {
		if (Shells [i] != null) length++;
	}
	int index = 0;
	Shell [] result = new Shell [length];
	for (int i=0; i<Shells.length; i++) {
		Shell widget = Shells [i];
		if (widget != null) result [index++] = widget;
	}
	return result;
}
public static synchronized int size () {
	int length = 0;
	for (int i=0; i<Shells.length; i++) {
		if (Shells [i] != null) length++;
	}
	for (int i=0; i<WidgetTable.length; i++) {
		if (WidgetTable [i] != null) length++;
	}
	return length;
}
}
