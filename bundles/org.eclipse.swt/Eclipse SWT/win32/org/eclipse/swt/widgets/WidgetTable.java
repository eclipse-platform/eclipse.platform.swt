package org.eclipse.swt.widgets;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.internal.win32.*;

/**
 * This class contains static methods which provide a map from the
 * platform representation of a widget to the SWT control. 
 */

class WidgetTable {
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
	int index = OS.GetWindowLong (handle, OS.GWL_USERDATA) - 1;
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
		for (int i=FreeSlot; i<length-1; i++) newIndexTable [i] = i + 1;
		newIndexTable [length - 1] = -1;
		IndexTable = newIndexTable;
		ControlTable = newControlTable;
	}
	OS.SetWindowLong (handle, OS.GWL_USERDATA, FreeSlot + 1);
	int oldSlot = FreeSlot;
	FreeSlot = IndexTable [oldSlot];
	IndexTable [oldSlot] = -2;
	ControlTable [oldSlot] = control;
}

public static synchronized Control remove (int handle) {
	if (handle == 0) return null;
	Control control = null;
	int index = OS.GetWindowLong (handle, OS.GWL_USERDATA) - 1;
	if (0 <= index && index < ControlTable.length) {
		control = ControlTable [index];
		ControlTable [index] = null;
		IndexTable [index] = FreeSlot;
		FreeSlot = index;
		OS.SetWindowLong (handle, OS.GWL_USERDATA, 0);
	}
	return control;
}

public static synchronized Shell [] shells () {
	/*
	* This code is intentionally commented.
	* Bug in JVM 1.2.  For some reason, when the following code
	* is inlined in this method, the JVM issues this error error:
	*
	* 	A nonfatal internal JIT (3.00.072b(x)) error 'GetRegisterA' has occurred in :
  	* 	'org/eclipse/swt/widgets/WidgetTable.shells ()[Lorg/eclipse/swt/widgets/Shell;': Interpreting method.
	* 	Please report this error in detail to http://java.sun.com/cgi-bin/bugreport.cgi
	*
	* The fix is to move the code that would be inlined into another method.
	*/
//	int size = 0;
//	for (int i=0; i<WidgetTable.length; i++) {
//		Control control = WidgetTable [i];
//		if (control != null && control instanceof Shell) size++;
//	}

	int size = shellSize ();
	int index = 0;
	Shell [] result = new Shell [size];
	for (int i=0; i<ControlTable.length; i++) {
		Control control = ControlTable [i];
		if (control != null && control instanceof Shell) {
			result [index++] = (Shell) control;
		}
	}
	return result;
}

static int shellSize () {
	int length = 0;
	for (int i=0; i<ControlTable.length; i++) {
		Control control = ControlTable [i];
		if (control != null && control instanceof Shell) length++;
	}
	return length;
}

public static synchronized int size () {
	int length = 0;
	for (int i=0; i<ControlTable.length; i++) {
		if (ControlTable [i] != null) length++;
	}
	return length;
}

}
