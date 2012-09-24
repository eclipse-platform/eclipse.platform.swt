/*******************************************************************************
 * Copyright (c) 2003, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.mozilla;

import java.util.*;
import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;

public class XPCOMObject {
	static boolean IsSolaris;
	static {
		String osName = System.getProperty ("os.name").toLowerCase (); //$NON-NLS-1$
		IsSolaris = osName.startsWith ("sunos") || osName.startsWith("solaris"); //$NON-NLS-1$
	}
	
	private long /*int*/ ppVtable;

	static private final int MAX_ARG_COUNT = 12;
	static private final int MAX_VTABLE_LENGTH = 80;
	static final int OS_OFFSET = IsSolaris ? 2 : 0;
	static private Callback[][] Callbacks = new Callback[MAX_VTABLE_LENGTH + OS_OFFSET][MAX_ARG_COUNT];
	static private Hashtable ObjectMap = new Hashtable ();
	
	
public XPCOMObject (int[] argCounts) {
	long /*int*/[] callbackAddresses = new long /*int*/[argCounts.length + OS_OFFSET];
	synchronized (Callbacks) {
		for (int i = 0, length = argCounts.length; i < length; i++) {
			if ((Callbacks[i + OS_OFFSET][argCounts[i]]) == null) {
				Callbacks[i + OS_OFFSET][argCounts[i]] = new Callback (getClass (), "callback"+i, argCounts[i] + 1, true, XPCOM.NS_ERROR_FAILURE); //$NON-NLS-1$
			}
			callbackAddresses[i + OS_OFFSET] = Callbacks[i + OS_OFFSET][argCounts[i]].getAddress ();
			if (callbackAddresses[i + OS_OFFSET] == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		}
	}

	long /*int*/ pVtable = C.malloc (C.PTR_SIZEOF * (argCounts.length + OS_OFFSET));
	XPCOM.memmove (pVtable, callbackAddresses, C.PTR_SIZEOF * (argCounts.length + OS_OFFSET));
	ppVtable = C.malloc (C.PTR_SIZEOF);
	XPCOM.memmove (ppVtable, new long /*int*/[] {pVtable}, C.PTR_SIZEOF);
	ObjectMap.put (new LONG (ppVtable), this);
}
	
static long /*int*/ callback0 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method0 (args);
}
static long /*int*/ callback1 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method1 (args);
}
static long /*int*/ callback10 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method10 (args);
}
static long /*int*/ callback11 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method11 (args);
}
static long /*int*/ callback12 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method12 (args);
}
static long /*int*/ callback13 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method13 (args);
}
static long /*int*/ callback14 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method14 (args);
}
static long /*int*/ callback15 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method15 (args);
}
static long /*int*/ callback16 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method16 (args);
}
static long /*int*/ callback17 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method17 (args);
}
static long /*int*/ callback18 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method18 (args);
}
static long /*int*/ callback19 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method19 (args);
}
static long /*int*/ callback2 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method2 (args);
}
static long /*int*/ callback20 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method20 (args);
}
static long /*int*/ callback21 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method21 (args);
}
static long /*int*/ callback22 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method22 (args);
}
static long /*int*/ callback23 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method23 (args);
}
static long /*int*/ callback24 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method24 (args);
}
static long /*int*/ callback25 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method25 (args);
}
static long /*int*/ callback26 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method26 (args);
}
static long /*int*/ callback27 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method27 (args);
}
static long /*int*/ callback28 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method28 (args);
}
static long /*int*/ callback29 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method29 (args);
}
static long /*int*/ callback3 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method3 (args);
}
static long /*int*/ callback30 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method30 (args);
}
static long /*int*/ callback31 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method31 (args);
}
static long /*int*/ callback32 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method32 (args);
}
static long /*int*/ callback33 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method33 (args);
}
static long /*int*/ callback34 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method34 (args);
}
static long /*int*/ callback35 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method35 (args);
}
static long /*int*/ callback36 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method36 (args);
}
static long /*int*/ callback37 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method37 (args);
}
static long /*int*/ callback38 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method38 (args);
}
static long /*int*/ callback39 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method39 (args);
}
static long /*int*/ callback4 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method4 (args);
}
static long /*int*/ callback40 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method40 (args);
}
static long /*int*/ callback41 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method41 (args);
}
static long /*int*/ callback42 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method42 (args);
}
static long /*int*/ callback43 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method43 (args);
}
static long /*int*/ callback44 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method44 (args);
}
static long /*int*/ callback45 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method45 (args);
}
static long /*int*/ callback46 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method46 (args);
}
static long /*int*/ callback47 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method47 (args);
}
static long /*int*/ callback48 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method48 (args);
}
static long /*int*/ callback49 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method49 (args);
}
static long /*int*/ callback5 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method5 (args);
}
static long /*int*/ callback50 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method50 (args);
}
static long /*int*/ callback51 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method51 (args);
}
static long /*int*/ callback52 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method52 (args);
}
static long /*int*/ callback53 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method53 (args);
}
static long /*int*/ callback54 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method54 (args);
}
static long /*int*/ callback55 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method55 (args);
}
static long /*int*/ callback56 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method56 (args);
}
static long /*int*/ callback57 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method57 (args);
}
static long /*int*/ callback58 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method58 (args);
}
static long /*int*/ callback59 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method59 (args);
}
static long /*int*/ callback6 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method6 (args);
}
static long /*int*/ callback60 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method60 (args);
}
static long /*int*/ callback61 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method61 (args);
}
static long /*int*/ callback62 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method62 (args);
}
static long /*int*/ callback63 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method63 (args);
}
static long /*int*/ callback64 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method64 (args);
}
static long /*int*/ callback65 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method65 (args);
}
static long /*int*/ callback66 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method66 (args);
}
static long /*int*/ callback67 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method67 (args);
}
static long /*int*/ callback68 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method68 (args);
}
static long /*int*/ callback69 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method69 (args);
}
static long /*int*/ callback7 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method7 (args);
}
static long /*int*/ callback70 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method70 (args);
}
static long /*int*/ callback71 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method71 (args);
}
static long /*int*/ callback72 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method72 (args);
}
static long /*int*/ callback73 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method73 (args);
}
static long /*int*/ callback74 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method74 (args);
}
static long /*int*/ callback75 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method75 (args);
}
static long /*int*/ callback76 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method76 (args);
}
static long /*int*/ callback77 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method77 (args);
}
static long /*int*/ callback78 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method78 (args);
}
static long /*int*/ callback79 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method79 (args);
}
static long /*int*/ callback8 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method8 (args);
}
static long /*int*/ callback9 (long /*int*/[] callbackArgs) {
	// find the object on which this call was invoked
	long /*int*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	long /*int*/[] args = new long /*int*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method9 (args);
}

public void dispose() {
	// free the memory for this reference
	long /*int*/[] pVtable = new long /*int*/[1];
	XPCOM.memmove (pVtable, ppVtable, C.PTR_SIZEOF);
	C.free (pVtable[0]);
	C.free (ppVtable);	

	// remove this ppVtable from the list
	ObjectMap.remove (new LONG (ppVtable));	

	ppVtable = 0;
}
	
public long /*int*/ getAddress () {
	return ppVtable;
}

public long /*int*/ method0 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method1 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method10 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method11 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method12 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method13 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method14 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method15 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method16 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method17 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method18 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method19 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method2 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method20 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method21 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method22 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method23 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method24 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method25 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method26 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method27 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method28 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method29 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method3 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method30 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method31 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method32 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method33 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method34 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method35 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method36 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method37 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method38 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method39 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method4 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method40 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method41 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method42 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method43 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method44 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method45 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method46 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method47 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method48 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method49 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method5 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method50 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method51 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method52 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method53 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method54 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method55 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method56 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method57 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method58 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method59 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method6 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method60 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method61 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method62 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method63 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method64 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method65 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method66 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method67 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method68 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method69 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method7 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method70 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method71 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method72 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method73 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method74 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method75 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method76 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method77 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method78 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method79 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method8 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public long /*int*/ method9 (long /*int*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}

