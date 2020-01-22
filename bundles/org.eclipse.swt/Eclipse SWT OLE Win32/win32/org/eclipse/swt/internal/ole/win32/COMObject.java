/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

import java.util.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.win32.*;

public class COMObject {

	public long ppVtable;

	static private final int MAX_ARG_COUNT = 12;
	static private final int MAX_VTABLE_LENGTH = 80;
	static private Callback[][] Callbacks = new Callback[MAX_VTABLE_LENGTH][MAX_ARG_COUNT];
	static private Map<LONG, COMObject> ObjectMap = new HashMap<>();

public COMObject(int[] argCounts) {
	long[] callbackAddresses = new long[argCounts.length];
	synchronized (Callbacks) {
		for (int i = 0, length = argCounts.length; i < length; i++){
			if ((Callbacks[i][argCounts[i]]) == null) {
				Callbacks[i][argCounts[i]] = new Callback(this.getClass(), "callback"+i, argCounts[i] + 1, true, COM.E_FAIL); //$NON-NLS-1$
			}
			callbackAddresses[i] = Callbacks[i][argCounts[i]].getAddress();
		}
	}

	long pVtable = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, C.PTR_SIZEOF * argCounts.length);
	OS.MoveMemory(pVtable, callbackAddresses, C.PTR_SIZEOF * argCounts.length);
	ppVtable = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, C.PTR_SIZEOF);
	OS.MoveMemory(ppVtable, new long[] {pVtable}, C.PTR_SIZEOF);
	ObjectMap.put(new LONG(ppVtable), this);
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

static long callback0(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method0(args);
}
static long callback1(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method1(args);
}
static long callback2(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method2(args);
}
static long callback3(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method3(args);
}
static long callback4(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method4(args);
}
static long callback5(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method5(args);
}
static long callback6(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method6(args);
}
static long callback7(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method7(args);
}
static long callback8(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method8(args);
}
static long callback9(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method9(args);
}
static long callback10(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method10(args);
}
static long callback11(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method11(args);
}
static long callback12(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method12(args);
}
static long callback13(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method13(args);
}
static long callback14(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method14(args);
}
static long callback15(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method15(args);
}
static long callback16(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method16(args);
}
static long callback17(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method17(args);
}
static long callback18(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method18(args);
}
static long callback19(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method19(args);
}
static long callback20(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method20(args);
}
static long callback21(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method21(args);
}
static long callback22(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method22(args);
}
static long callback23(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method23(args);
}
static long callback24(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method24(args);
}
static long callback25(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method25(args);
}
static long callback26(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method26(args);
}
static long callback27(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method27(args);
}
static long callback28(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method28(args);
}
static long callback29(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method29(args);
}
static long callback30(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method30(args);
}
static long callback31(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method31(args);
}
static long callback32(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method32(args);
}
static long callback33(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method33(args);
}
static long callback34(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method34(args);
}
static long callback35(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method35(args);
}
static long callback36(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method36(args);
}
static long callback37(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method37(args);
}
static long callback38(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method38(args);
}
static long callback39(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method39(args);
}
static long callback40(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method40(args);
}
static long callback41(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method41(args);
}
static long callback42(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method42(args);
}
static long callback43(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method43(args);
}
static long callback44(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method44(args);
}
static long callback45(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method45(args);
}
static long callback46(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method46(args);
}
static long callback47(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method47(args);
}
static long callback48(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method48(args);
}
static long callback49(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method49(args);
}
static long callback50(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method50(args);
}
static long callback51(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method51(args);
}
static long callback52(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method52(args);
}
static long callback53(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method53(args);
}
static long callback54(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method54(args);
}
static long callback55(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method55(args);
}
static long callback56(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method56(args);
}
static long callback57(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method57(args);
}
static long callback58(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method58(args);
}
static long callback59(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method59(args);
}
static long callback60(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method60(args);
}
static long callback61(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method61(args);
}
static long callback62(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method62(args);
}
static long callback63(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method63(args);
}
static long callback64(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method64(args);
}
static long callback65(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method65(args);
}
static long callback66(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method66(args);
}
static long callback67(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method67(args);
}
static long callback68(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method68(args);
}
static long callback69(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method69(args);
}
static long callback70(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method70(args);
}
static long callback71(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method71(args);
}
static long callback72(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method72(args);
}
static long callback73(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method73(args);
}
static long callback74(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method74(args);
}
static long callback75(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method75(args);
}
static long callback76(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method76(args);
}
static long callback77(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method77(args);
}
static long callback78(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method78(args);
}
static long callback79(long[] callbackArgs) {
	// find the object on which this call was invoked
	long address = callbackArgs[0];
	COMObject object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long[] args = new long[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return object.method79(args);
}
public void dispose() {
	// free the memory for this reference
	long[] pVtable = new long[1];
	OS.MoveMemory(pVtable, ppVtable, C.PTR_SIZEOF);
	OS.GlobalFree(pVtable[0]);
	OS.GlobalFree(ppVtable);

	// remove this ppVtable from the list
	ObjectMap.remove(new LONG(ppVtable));

	ppVtable = 0;
}
public long getAddress () {
	return ppVtable;
}
public long method0(long[] args) {
	return COM.E_NOTIMPL;
}
public long method1(long[] args) {
	return COM.E_NOTIMPL;
}
public long method2(long[] args) {
	return COM.E_NOTIMPL;
}
public long method3(long[] args) {
	return COM.E_NOTIMPL;
}
public long method4(long[] args) {
	return COM.E_NOTIMPL;
}
public long method5(long[] args) {
	return COM.E_NOTIMPL;
}
public long method6(long[] args) {
	return COM.E_NOTIMPL;
}
public long method7(long[] args) {
	return COM.E_NOTIMPL;
}
public long method8(long[] args) {
	return COM.E_NOTIMPL;
}
public long method9(long[] args) {
	return COM.E_NOTIMPL;
}
public long method10(long[] args) {
	return COM.E_NOTIMPL;
}
public long method11(long[] args) {
	return COM.E_NOTIMPL;
}
public long method12(long[] args) {
	return COM.E_NOTIMPL;
}
public long method13(long[] args) {
	return COM.E_NOTIMPL;
}
public long method14(long[] args) {
	return COM.E_NOTIMPL;
}
public long method15(long[] args) {
	return COM.E_NOTIMPL;
}
public long method16(long[] args) {
	return COM.E_NOTIMPL;
}
public long method17(long[] args) {
	return COM.E_NOTIMPL;
}
public long method18(long[] args) {
	return COM.E_NOTIMPL;
}
public long method19(long[] args) {
	return COM.E_NOTIMPL;
}
public long method20(long[] args) {
	return COM.E_NOTIMPL;
}
public long method21(long[] args) {
	return COM.E_NOTIMPL;
}
public long method22(long[] args) {
	return COM.E_NOTIMPL;
}
public long method23(long[] args) {
	return COM.E_NOTIMPL;
}
public long method24(long[] args) {
	return COM.E_NOTIMPL;
}
public long method25(long[] args) {
	return COM.E_NOTIMPL;
}
public long method26(long[] args) {
	return COM.E_NOTIMPL;
}
public long method27(long[] args) {
	return COM.E_NOTIMPL;
}
public long method28(long[] args) {
	return COM.E_NOTIMPL;
}
public long method29(long[] args) {
	return COM.E_NOTIMPL;
}
public long method30(long[] args) {
	return COM.E_NOTIMPL;
}
public long method31(long[] args) {
	return COM.E_NOTIMPL;
}
public long method32(long[] args) {
	return COM.E_NOTIMPL;
}
public long method33(long[] args) {
	return COM.E_NOTIMPL;
}
public long method34(long[] args) {
	return COM.E_NOTIMPL;
}
public long method35(long[] args) {
	return COM.E_NOTIMPL;
}
public long method36(long[] args) {
	return COM.E_NOTIMPL;
}
public long method37(long[] args) {
	return COM.E_NOTIMPL;
}
public long method38(long[] args) {
	return COM.E_NOTIMPL;
}
public long method39(long[] args) {
	return COM.E_NOTIMPL;
}
public long method40(long[] args) {
	return COM.E_NOTIMPL;
}
public long method41(long[] args) {
	return COM.E_NOTIMPL;
}
public long method42(long[] args) {
	return COM.E_NOTIMPL;
}
public long method43(long[] args) {
	return COM.E_NOTIMPL;
}
public long method44(long[] args) {
	return COM.E_NOTIMPL;
}
public long method45(long[] args) {
	return COM.E_NOTIMPL;
}
public long method46(long[] args) {
	return COM.E_NOTIMPL;
}
public long method47(long[] args) {
	return COM.E_NOTIMPL;
}
public long method48(long[] args) {
	return COM.E_NOTIMPL;
}
public long method49(long[] args) {
	return COM.E_NOTIMPL;
}
public long method50(long[] args) {
	return COM.E_NOTIMPL;
}
public long method51(long[] args) {
	return COM.E_NOTIMPL;
}
public long method52(long[] args) {
	return COM.E_NOTIMPL;
}
public long method53(long[] args) {
	return COM.E_NOTIMPL;
}
public long method54(long[] args) {
	return COM.E_NOTIMPL;
}
public long method55(long[] args) {
	return COM.E_NOTIMPL;
}
public long method56(long[] args) {
	return COM.E_NOTIMPL;
}
public long method57(long[] args) {
	return COM.E_NOTIMPL;
}
public long method58(long[] args) {
	return COM.E_NOTIMPL;
}
public long method59(long[] args) {
	return COM.E_NOTIMPL;
}
public long method60(long[] args) {
	return COM.E_NOTIMPL;
}
public long method61(long[] args) {
	return COM.E_NOTIMPL;
}
public long method62(long[] args) {
	return COM.E_NOTIMPL;
}
public long method63(long[] args) {
	return COM.E_NOTIMPL;
}
public long method64(long[] args) {
	return COM.E_NOTIMPL;
}
public long method65(long[] args) {
	return COM.E_NOTIMPL;
}
public long method66(long[] args) {
	return COM.E_NOTIMPL;
}
public long method67(long[] args) {
	return COM.E_NOTIMPL;
}
public long method68(long[] args) {
	return COM.E_NOTIMPL;
}
public long method69(long[] args) {
	return COM.E_NOTIMPL;
}
public long method70(long[] args) {
	return COM.E_NOTIMPL;
}
public long method71(long[] args) {
	return COM.E_NOTIMPL;
}
public long method72(long[] args) {
	return COM.E_NOTIMPL;
}
public long method73(long[] args) {
	return COM.E_NOTIMPL;
}
public long method74(long[] args) {
	return COM.E_NOTIMPL;
}
public long method75(long[] args) {
	return COM.E_NOTIMPL;
}
public long method76(long[] args) {
	return COM.E_NOTIMPL;
}
public long method77(long[] args) {
	return COM.E_NOTIMPL;
}
public long method78(long[] args) {
	return COM.E_NOTIMPL;
}
public long method79(long[] args) {
	return COM.E_NOTIMPL;
}

}
