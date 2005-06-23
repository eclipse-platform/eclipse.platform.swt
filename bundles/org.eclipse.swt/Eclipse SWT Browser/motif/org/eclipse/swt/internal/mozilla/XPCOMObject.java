/*******************************************************************************
 * Copyright (c) 2003, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.mozilla;

import java.util.Hashtable;
import org.eclipse.swt.internal.Callback;
import org.eclipse.swt.SWT;

public class XPCOMObject {

	private int ppVtable;
	
	static private final int MAX_ARG_COUNT = 12;
	static private final int MAX_VTABLE_LENGTH = 80;
	static private Callback[][] Callbacks = new Callback[MAX_VTABLE_LENGTH][MAX_ARG_COUNT];
	static private Hashtable ObjectMap = new Hashtable();
	
public XPCOMObject(int[] argCounts) {
	int[] callbackAddresses = new int[argCounts.length];
	for (int i = 0, length = argCounts.length; i < length; i++){
		if ((Callbacks[i][argCounts[i]]) == null) {
			Callbacks[i][argCounts[i]] = new Callback(this.getClass(), "callback"+i, argCounts[i] + 1, true); //$NON-NLS-1$
		}
		callbackAddresses[i] = Callbacks[i][argCounts[i]].getAddress();
		if (callbackAddresses[i] == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	}	

	int pVtable = XPCOM.PR_Malloc(4 * argCounts.length);
	XPCOM.memmove(pVtable, callbackAddresses, 4 * argCounts.length);
	ppVtable = XPCOM.PR_Malloc(4);
	XPCOM.memmove(ppVtable, new int[] {pVtable}, 4);
	ObjectMap.put(new Integer(ppVtable), this);
}
	
static int callback0(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method0(args);
}
static int callback1(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method1(args);
}
static int callback10(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method10(args);
}
static int callback11(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method11(args);
}
static int callback12(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method12(args);
}
static int callback13(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method13(args);
}
static int callback14(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method14(args);
}
static int callback15(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method15(args);
}
static int callback16(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method16(args);
}
static int callback17(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method17(args);
}
static int callback18(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method18(args);
}
static int callback19(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method19(args);
}
static int callback2(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method2(args);
}
static int callback20(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method20(args);
}
static int callback21(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method21(args);
}
static int callback22(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method22(args);
}
static int callback23(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method23(args);
}
static int callback24(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method24(args);
}
static int callback25(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method25(args);
}
static int callback26(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method26(args);
}
static int callback27(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method27(args);
}
static int callback28(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method28(args);
}
static int callback29(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method29(args);
}
static int callback3(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method3(args);
}
static int callback30(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method30(args);
}
static int callback31(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method31(args);
}
static int callback32(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method32(args);
}
static int callback33(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method33(args);
}
static int callback34(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method34(args);
}
static int callback35(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method35(args);
}
static int callback36(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method36(args);
}
static int callback37(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method37(args);
}
static int callback38(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method38(args);
}
static int callback39(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method39(args);
}
static int callback4(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method4(args);
}
static int callback40(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method40(args);
}
static int callback41(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method41(args);
}
static int callback42(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method42(args);
}
static int callback43(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method43(args);
}
static int callback44(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method44(args);
}
static int callback45(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method45(args);
}
static int callback46(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method46(args);
}
static int callback47(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method47(args);
}
static int callback48(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method48(args);
}
static int callback49(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method49(args);
}
static int callback5(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method5(args);
}
static int callback50(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method50(args);
}
static int callback51(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method51(args);
}
static int callback52(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method52(args);
}
static int callback53(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method53(args);
}
static int callback54(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method54(args);
}
static int callback55(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method55(args);
}
static int callback56(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method56(args);
}
static int callback57(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method57(args);
}
static int callback58(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method58(args);
}
static int callback59(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method59(args);
}
static int callback6(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method6(args);
}
static int callback60(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method60(args);
}
static int callback61(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method61(args);
}
static int callback62(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method62(args);
}
static int callback63(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method63(args);
}
static int callback64(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method64(args);
}
static int callback65(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method65(args);
}
static int callback66(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method66(args);
}
static int callback67(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method67(args);
}
static int callback68(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method68(args);
}
static int callback69(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method69(args);
}
static int callback7(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method7(args);
}
static int callback70(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method70(args);
}
static int callback71(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method71(args);
}
static int callback72(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method72(args);
}
static int callback73(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method73(args);
}
static int callback74(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method74(args);
}
static int callback75(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method75(args);
}
static int callback76(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method76(args);
}
static int callback77(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method77(args);
}
static int callback78(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method78(args);
}
static int callback79(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method79(args);
}
static int callback8(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method8(args);
}
static int callback9(int[] callbackArgs) {
	// find the object on which this call was invoked
	int address = callbackArgs[0];
	Object object = ObjectMap.get(new Integer(address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int[] args = new int[callbackArgs.length - 1];
	System.arraycopy(callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method9(args);
}

public void dispose() {
	// free the memory for this reference
	int[] pVtable = new int[1];
	XPCOM.memmove(pVtable, ppVtable, 4);
	XPCOM.PR_Free(pVtable[0]);
	XPCOM.PR_Free(ppVtable);	

	// remove this ppVtable from the list
	ObjectMap.remove(new Integer(ppVtable));	

	ppVtable = 0;
}
	
public int getAddress () {
	return ppVtable;
}

public int method0(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method1(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method10(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method11(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method12(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method13(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method14(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method15(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method16(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method17(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method18(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method19(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method2(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method20(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method21(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method22(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method23(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method24(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method25(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method26(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method27(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method28(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method29(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method3(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method30(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method31(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method32(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method33(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method34(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method35(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method36(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method37(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method38(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method39(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method4(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method40(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method41(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method42(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method43(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method44(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method45(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method46(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method47(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method48(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method49(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method5(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method50(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method51(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method52(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method53(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method54(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method55(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method56(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method57(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method58(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method59(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method6(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method60(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method61(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method62(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method63(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method64(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method65(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method66(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method67(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method68(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method69(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method7(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method70(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method71(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method72(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method73(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method74(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method75(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method76(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method77(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method78(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method79(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method8(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int method9(int[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}

