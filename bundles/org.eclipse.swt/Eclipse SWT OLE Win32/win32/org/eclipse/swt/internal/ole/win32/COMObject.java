/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

import java.util.Hashtable;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;


public class COMObject {

	private int ppVtable;
	
	static private final int MAX_ARG_COUNT = 12;
	static private final int MAX_VTABLE_LENGTH = 80;
	static private Callback[][] Callbacks = new Callback[MAX_VTABLE_LENGTH][MAX_ARG_COUNT];
	static private Hashtable ObjectMap = new Hashtable();
	
public COMObject(int[] argCounts) {
	int[] callbackAddresses = new int[argCounts.length];
	for (int i = 0, length = argCounts.length; i < length; i++){
		if ((Callbacks[i][argCounts[i]]) == null) {
			Callbacks[i][argCounts[i]] = new Callback(this.getClass(), "callback"+i, argCounts[i] + 1, true, COM.E_FAIL); //$NON-NLS-1$
			if (Callbacks[i][argCounts[i]].getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
		}
		callbackAddresses[i] = Callbacks[i][argCounts[i]].getAddress();
	}

	int pVtable = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, 4 * argCounts.length);
	COM.MoveMemory(pVtable, callbackAddresses, 4 * argCounts.length);
	ppVtable = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, 4);
	COM.MoveMemory(ppVtable, new int[] {pVtable}, 4);
	ObjectMap.put(new Integer(ppVtable), this);
}

public static GUID IIDFromString(String lpsz) {
	// create a null terminated array of char
	char[] buffer = (lpsz +"\0").toCharArray();

	// invoke system method
	GUID lpiid = new GUID();
	if (COM.IIDFromString(buffer, lpiid) == COM.S_OK)
		return lpiid;
	return null;
}

static int callback0(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method0(args);
}
static int callback1(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method1(args);
}
static int callback2(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method2(args);
}
static int callback3(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method3(args);
}
static int callback4(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method4(args);
}
static int callback5(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method5(args);
}
static int callback6(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method6(args);
}
static int callback7(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method7(args);
}
static int callback8(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method8(args);
}
static int callback9(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method9(args);
}
static int callback10(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method10(args);
}
static int callback11(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method11(args);
}
static int callback12(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method12(args);
}
static int callback13(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method13(args);
}
static int callback14(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method14(args);
}
static int callback15(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method15(args);
}
static int callback16(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method16(args);
}
static int callback17(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method17(args);
}
static int callback18(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method18(args);
}
static int callback19(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method19(args);
}
static int callback20(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method20(args);
}
static int callback21(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method21(args);
}
static int callback22(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method22(args);
}
static int callback23(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method23(args);
}
static int callback24(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method24(args);
}
static int callback25(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method25(args);
}
static int callback26(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method26(args);
}
static int callback27(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method27(args);
}
static int callback28(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method28(args);
}
static int callback29(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method29(args);
}
static int callback30(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method30(args);
}
static int callback31(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method31(args);
}
static int callback32(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method32(args);
}
static int callback33(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method33(args);
}
static int callback34(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method34(args);
}
static int callback35(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method35(args);
}
static int callback36(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method36(args);
}
static int callback37(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method37(args);
}
static int callback38(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method38(args);
}
static int callback39(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method39(args);
}
static int callback40(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method40(args);
}
static int callback41(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method41(args);
}
static int callback42(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method42(args);
}
static int callback43(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method43(args);
}
static int callback44(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method44(args);
}
static int callback45(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method45(args);
}
static int callback46(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method46(args);
}
static int callback47(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method47(args);
}
static int callback48(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method48(args);
}
static int callback49(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method49(args);
}
static int callback50(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method50(args);
}
static int callback51(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method51(args);
}
static int callback52(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method52(args);
}
static int callback53(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method53(args);
}
static int callback54(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method54(args);
}
static int callback55(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method55(args);
}
static int callback56(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method56(args);
}
static int callback57(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method57(args);
}
static int callback58(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method58(args);
}
static int callback59(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method59(args);
}
static int callback60(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method60(args);
}
static int callback61(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method61(args);
}
static int callback62(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method62(args);
}
static int callback63(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method63(args);
}
static int callback64(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method64(args);
}
static int callback65(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method65(args);
}
static int callback66(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method66(args);
}
static int callback67(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method67(args);
}
static int callback68(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method68(args);
}
static int callback69(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method69(args);
}
static int callback70(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method70(args);
}
static int callback71(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method71(args);
}
static int callback72(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method72(args);
}
static int callback73(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method73(args);
}
static int callback74(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method74(args);
}
static int callback75(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method75(args);
}
static int callback76(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method76(args);
}
static int callback77(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method77(args);
}
static int callback78(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method78(args);
}
static int callback79(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return COM.E_FAIL;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method79(args);
}
public void dispose() {
	// free the memory for this reference
	int[] pVtable = new int[1];
	OS.MoveMemory(pVtable, ppVtable, 4);
	OS.GlobalFree(pVtable[0]);
	OS.GlobalFree(ppVtable);

	// remove this ppVtable from the list
	ObjectMap.remove(new Integer(ppVtable));

	ppVtable = 0;
}
public int getAddress () {
	return ppVtable;
}
public int method0(int[] args) {
	return COM.E_NOTIMPL;
}
public int method1(int[] args) {
	return COM.E_NOTIMPL;
}
public int method2(int[] args) {
	return COM.E_NOTIMPL;
}
public int method3(int[] args) {
	return COM.E_NOTIMPL;
}
public int method4(int[] args) {
	return COM.E_NOTIMPL;
}
public int method5(int[] args) {
	return COM.E_NOTIMPL;
}
public int method6(int[] args) {
	return COM.E_NOTIMPL;
}
public int method7(int[] args) {
	return COM.E_NOTIMPL;
}
public int method8(int[] args) {
	return COM.E_NOTIMPL;
}
public int method9(int[] args) {
	return COM.E_NOTIMPL;
}
public int method10(int[] args) {
	return COM.E_NOTIMPL;
}
public int method11(int[] args) {
	return COM.E_NOTIMPL;
}
public int method12(int[] args) {
	return COM.E_NOTIMPL;
}
public int method13(int[] args) {
	return COM.E_NOTIMPL;
}
public int method14(int[] args) {
	return COM.E_NOTIMPL;
}
public int method15(int[] args) {
	return COM.E_NOTIMPL;
}
public int method16(int[] args) {
	return COM.E_NOTIMPL;
}
public int method17(int[] args) {
	return COM.E_NOTIMPL;
}
public int method18(int[] args) {
	return COM.E_NOTIMPL;
}
public int method19(int[] args) {
	return COM.E_NOTIMPL;
}
public int method20(int[] args) {
	return COM.E_NOTIMPL;
}
public int method21(int[] args) {
	return COM.E_NOTIMPL;
}
public int method22(int[] args) {
	return COM.E_NOTIMPL;
}
public int method23(int[] args) {
	return COM.E_NOTIMPL;
}
public int method24(int[] args) {
	return COM.E_NOTIMPL;
}
public int method25(int[] args) {
	return COM.E_NOTIMPL;
}
public int method26(int[] args) {
	return COM.E_NOTIMPL;
}
public int method27(int[] args) {
	return COM.E_NOTIMPL;
}
public int method28(int[] args) {
	return COM.E_NOTIMPL;
}
public int method29(int[] args) {
	return COM.E_NOTIMPL;
}
public int method30(int[] args) {
	return COM.E_NOTIMPL;
}
public int method31(int[] args) {
	return COM.E_NOTIMPL;
}
public int method32(int[] args) {
	return COM.E_NOTIMPL;
}
public int method33(int[] args) {
	return COM.E_NOTIMPL;
}
public int method34(int[] args) {
	return COM.E_NOTIMPL;
}
public int method35(int[] args) {
	return COM.E_NOTIMPL;
}
public int method36(int[] args) {
	return COM.E_NOTIMPL;
}
public int method37(int[] args) {
	return COM.E_NOTIMPL;
}
public int method38(int[] args) {
	return COM.E_NOTIMPL;
}
public int method39(int[] args) {
	return COM.E_NOTIMPL;
}
public int method40(int[] args) {
	return COM.E_NOTIMPL;
}
public int method41(int[] args) {
	return COM.E_NOTIMPL;
}
public int method42(int[] args) {
	return COM.E_NOTIMPL;
}
public int method43(int[] args) {
	return COM.E_NOTIMPL;
}
public int method44(int[] args) {
	return COM.E_NOTIMPL;
}
public int method45(int[] args) {
	return COM.E_NOTIMPL;
}
public int method46(int[] args) {
	return COM.E_NOTIMPL;
}
public int method47(int[] args) {
	return COM.E_NOTIMPL;
}
public int method48(int[] args) {
	return COM.E_NOTIMPL;
}
public int method49(int[] args) {
	return COM.E_NOTIMPL;
}
public int method50(int[] args) {
	return COM.E_NOTIMPL;
}
public int method51(int[] args) {
	return COM.E_NOTIMPL;
}
public int method52(int[] args) {
	return COM.E_NOTIMPL;
}
public int method53(int[] args) {
	return COM.E_NOTIMPL;
}
public int method54(int[] args) {
	return COM.E_NOTIMPL;
}
public int method55(int[] args) {
	return COM.E_NOTIMPL;
}
public int method56(int[] args) {
	return COM.E_NOTIMPL;
}
public int method57(int[] args) {
	return COM.E_NOTIMPL;
}
public int method58(int[] args) {
	return COM.E_NOTIMPL;
}
public int method59(int[] args) {
	return COM.E_NOTIMPL;
}
public int method60(int[] args) {
	return COM.E_NOTIMPL;
}
public int method61(int[] args) {
	return COM.E_NOTIMPL;
}
public int method62(int[] args) {
	return COM.E_NOTIMPL;
}
public int method63(int[] args) {
	return COM.E_NOTIMPL;
}
public int method64(int[] args) {
	return COM.E_NOTIMPL;
}
public int method65(int[] args) {
	return COM.E_NOTIMPL;
}
public int method66(int[] args) {
	return COM.E_NOTIMPL;
}
public int method67(int[] args) {
	return COM.E_NOTIMPL;
}
public int method68(int[] args) {
	return COM.E_NOTIMPL;
}
public int method69(int[] args) {
	return COM.E_NOTIMPL;
}
public int method70(int[] args) {
	return COM.E_NOTIMPL;
}
public int method71(int[] args) {
	return COM.E_NOTIMPL;
}
public int method72(int[] args) {
	return COM.E_NOTIMPL;
}
public int method73(int[] args) {
	return COM.E_NOTIMPL;
}
public int method74(int[] args) {
	return COM.E_NOTIMPL;
}
public int method75(int[] args) {
	return COM.E_NOTIMPL;
}
public int method76(int[] args) {
	return COM.E_NOTIMPL;
}
public int method77(int[] args) {
	return COM.E_NOTIMPL;
}
public int method78(int[] args) {
	return COM.E_NOTIMPL;
}
public int method79(int[] args) {
	return COM.E_NOTIMPL;
}

}
