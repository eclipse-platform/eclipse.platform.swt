/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
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

	public long /*int*/ ppVtable;
	
	static private final int MAX_ARG_COUNT = 12;
	static private final int MAX_VTABLE_LENGTH = 80;
	static private Callback[][] Callbacks = new Callback[MAX_VTABLE_LENGTH][MAX_ARG_COUNT];
	static private Hashtable ObjectMap = new Hashtable();
	
public COMObject(int[] argCounts) {
	long /*int*/[] callbackAddresses = new long /*int*/[argCounts.length];
	synchronized (Callbacks) {
		for (int i = 0, length = argCounts.length; i < length; i++){
			if ((Callbacks[i][argCounts[i]]) == null) {
				Callbacks[i][argCounts[i]] = new Callback(this.getClass(), "callback"+i, argCounts[i] + 1, true, COM.E_FAIL); //$NON-NLS-1$
				if (Callbacks[i][argCounts[i]].getAddress() == 0) SWT.error(SWT.ERROR_NO_MORE_CALLBACKS);
			}
			callbackAddresses[i] = Callbacks[i][argCounts[i]].getAddress();
		}
	}

	long /*int*/ pVtable = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, OS.PTR_SIZEOF * argCounts.length);
	COM.MoveMemory(pVtable, callbackAddresses, OS.PTR_SIZEOF * argCounts.length);
	ppVtable = OS.GlobalAlloc(COM.GMEM_FIXED | COM.GMEM_ZEROINIT, OS.PTR_SIZEOF);
	COM.MoveMemory(ppVtable, new long /*int*/[] {pVtable}, OS.PTR_SIZEOF);
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

static long /*int*/ callback0(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method0(args);
}
static long /*int*/ callback1(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method1(args);
}
static long /*int*/ callback2(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method2(args);
}
static long /*int*/ callback3(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method3(args);
}
static long /*int*/ callback4(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method4(args);
}
static long /*int*/ callback5(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method5(args);
}
static long /*int*/ callback6(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method6(args);
}
static long /*int*/ callback7(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method7(args);
}
static long /*int*/ callback8(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method8(args);
}
static long /*int*/ callback9(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method9(args);
}
static long /*int*/ callback10(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method10(args);
}
static long /*int*/ callback11(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method11(args);
}
static long /*int*/ callback12(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method12(args);
}
static long /*int*/ callback13(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method13(args);
}
static long /*int*/ callback14(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method14(args);
}
static long /*int*/ callback15(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method15(args);
}
static long /*int*/ callback16(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method16(args);
}
static long /*int*/ callback17(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method17(args);
}
static long /*int*/ callback18(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method18(args);
}
static long /*int*/ callback19(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method19(args);
}
static long /*int*/ callback20(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method20(args);
}
static long /*int*/ callback21(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method21(args);
}
static long /*int*/ callback22(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method22(args);
}
static long /*int*/ callback23(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method23(args);
}
static long /*int*/ callback24(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method24(args);
}
static long /*int*/ callback25(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method25(args);
}
static long /*int*/ callback26(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method26(args);
}
static long /*int*/ callback27(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method27(args);
}
static long /*int*/ callback28(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method28(args);
}
static long /*int*/ callback29(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method29(args);
}
static long /*int*/ callback30(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method30(args);
}
static long /*int*/ callback31(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method31(args);
}
static long /*int*/ callback32(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method32(args);
}
static long /*int*/ callback33(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method33(args);
}
static long /*int*/ callback34(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method34(args);
}
static long /*int*/ callback35(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method35(args);
}
static long /*int*/ callback36(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method36(args);
}
static long /*int*/ callback37(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method37(args);
}
static long /*int*/ callback38(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method38(args);
}
static long /*int*/ callback39(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method39(args);
}
static long /*int*/ callback40(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method40(args);
}
static long /*int*/ callback41(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method41(args);
}
static long /*int*/ callback42(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method42(args);
}
static long /*int*/ callback43(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method43(args);
}
static long /*int*/ callback44(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method44(args);
}
static long /*int*/ callback45(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method45(args);
}
static long /*int*/ callback46(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method46(args);
}
static long /*int*/ callback47(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method47(args);
}
static long /*int*/ callback48(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method48(args);
}
static long /*int*/ callback49(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method49(args);
}
static long /*int*/ callback50(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method50(args);
}
static long /*int*/ callback51(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method51(args);
}
static long /*int*/ callback52(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method52(args);
}
static long /*int*/ callback53(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method53(args);
}
static long /*int*/ callback54(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method54(args);
}
static long /*int*/ callback55(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method55(args);
}
static long /*int*/ callback56(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method56(args);
}
static long /*int*/ callback57(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method57(args);
}
static long /*int*/ callback58(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method58(args);
}
static long /*int*/ callback59(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method59(args);
}
static long /*int*/ callback60(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method60(args);
}
static long /*int*/ callback61(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method61(args);
}
static long /*int*/ callback62(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method62(args);
}
static long /*int*/ callback63(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method63(args);
}
static long /*int*/ callback64(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method64(args);
}
static long /*int*/ callback65(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method65(args);
}
static long /*int*/ callback66(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method66(args);
}
static long /*int*/ callback67(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method67(args);
}
static long /*int*/ callback68(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method68(args);
}
static long /*int*/ callback69(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method69(args);
}
static long /*int*/ callback70(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method70(args);
}
static long /*int*/ callback71(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method71(args);
}
static long /*int*/ callback72(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method72(args);
}
static long /*int*/ callback73(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method73(args);
}
static long /*int*/ callback74(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method74(args);
}
static long /*int*/ callback75(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method75(args);
}
static long /*int*/ callback76(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method76(args);
}
static long /*int*/ callback77(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method77(args);
}
static long /*int*/ callback78(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method78(args);
}
static long /*int*/ callback79(long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get(new LONG(address));
	if (object == null) return COM.E_FAIL;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((COMObject) object).method79(args);
}
public void dispose() {
	// free the memory for this reference
	long /*int*/[] pVtable = new long /*int*/[1];
	OS.MoveMemory(pVtable, ppVtable, OS.PTR_SIZEOF);
	OS.GlobalFree(pVtable[0]);
	OS.GlobalFree(ppVtable);

	// remove this ppVtable from the list
	ObjectMap.remove(new LONG(ppVtable));

	ppVtable = 0;
}
public long /*int*/ getAddress () {
	return ppVtable;
}
public long /*int*/ method0(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method1(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method2(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method3(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method4(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method5(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method6(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method7(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method8(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method9(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method10(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method11(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method12(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method13(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method14(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method15(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method16(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method17(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method18(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method19(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method20(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method21(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method22(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method23(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method24(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method25(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method26(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method27(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method28(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method29(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method30(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method31(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method32(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method33(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method34(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method35(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method36(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method37(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method38(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method39(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method40(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method41(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method42(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method43(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method44(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method45(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method46(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method47(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method48(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method49(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method50(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method51(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method52(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method53(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method54(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method55(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method56(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method57(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method58(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method59(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method60(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method61(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method62(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method63(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method64(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method65(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method66(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method67(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method68(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method69(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method70(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method71(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method72(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method73(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method74(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method75(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method76(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method77(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method78(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}
public long /*int*/ method79(long /*int*/[] args) {
	return COM.E_NOTIMPL;
}

}
