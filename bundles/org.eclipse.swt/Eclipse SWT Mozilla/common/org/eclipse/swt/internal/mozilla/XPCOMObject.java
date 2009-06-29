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
	
	private int /*long*/ ppVtable;

	static private final int MAX_ARG_COUNT = 12;
	static private final int MAX_VTABLE_LENGTH = 80;
	static final int OS_OFFSET = IsSolaris ? 2 : 0;
	static private Callback[][] Callbacks = new Callback[MAX_VTABLE_LENGTH + OS_OFFSET][MAX_ARG_COUNT];
	static private Hashtable ObjectMap = new Hashtable ();
	
	
public XPCOMObject (int[] argCounts) {
	int /*long*/[] callbackAddresses = new int /*long*/[argCounts.length + OS_OFFSET];
	synchronized (Callbacks) {
		for (int i = 0, length = argCounts.length; i < length; i++) {
			if ((Callbacks[i + OS_OFFSET][argCounts[i]]) == null) {
				Callbacks[i + OS_OFFSET][argCounts[i]] = new Callback (getClass (), "callback"+i, argCounts[i] + 1, true, XPCOM.NS_ERROR_FAILURE); //$NON-NLS-1$
			}
			callbackAddresses[i + OS_OFFSET] = Callbacks[i + OS_OFFSET][argCounts[i]].getAddress ();
			if (callbackAddresses[i + OS_OFFSET] == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
		}
	}

	int /*long*/ pVtable = C.malloc (C.PTR_SIZEOF * (argCounts.length + OS_OFFSET));
	XPCOM.memmove (pVtable, callbackAddresses, C.PTR_SIZEOF * (argCounts.length + OS_OFFSET));
	ppVtable = C.malloc (C.PTR_SIZEOF);
	XPCOM.memmove (ppVtable, new int /*long*/[] {pVtable}, C.PTR_SIZEOF);
	ObjectMap.put (new LONG (ppVtable), this);
}
	
static int /*long*/ callback0 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method0 (args);
}
static int /*long*/ callback1 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method1 (args);
}
static int /*long*/ callback10 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method10 (args);
}
static int /*long*/ callback11 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method11 (args);
}
static int /*long*/ callback12 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method12 (args);
}
static int /*long*/ callback13 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method13 (args);
}
static int /*long*/ callback14 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method14 (args);
}
static int /*long*/ callback15 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method15 (args);
}
static int /*long*/ callback16 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method16 (args);
}
static int /*long*/ callback17 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method17 (args);
}
static int /*long*/ callback18 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method18 (args);
}
static int /*long*/ callback19 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method19 (args);
}
static int /*long*/ callback2 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method2 (args);
}
static int /*long*/ callback20 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method20 (args);
}
static int /*long*/ callback21 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method21 (args);
}
static int /*long*/ callback22 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method22 (args);
}
static int /*long*/ callback23 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method23 (args);
}
static int /*long*/ callback24 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method24 (args);
}
static int /*long*/ callback25 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method25 (args);
}
static int /*long*/ callback26 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method26 (args);
}
static int /*long*/ callback27 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method27 (args);
}
static int /*long*/ callback28 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method28 (args);
}
static int /*long*/ callback29 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method29 (args);
}
static int /*long*/ callback3 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method3 (args);
}
static int /*long*/ callback30 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method30 (args);
}
static int /*long*/ callback31 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method31 (args);
}
static int /*long*/ callback32 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method32 (args);
}
static int /*long*/ callback33 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method33 (args);
}
static int /*long*/ callback34 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method34 (args);
}
static int /*long*/ callback35 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method35 (args);
}
static int /*long*/ callback36 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method36 (args);
}
static int /*long*/ callback37 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method37 (args);
}
static int /*long*/ callback38 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method38 (args);
}
static int /*long*/ callback39 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method39 (args);
}
static int /*long*/ callback4 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method4 (args);
}
static int /*long*/ callback40 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method40 (args);
}
static int /*long*/ callback41 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method41 (args);
}
static int /*long*/ callback42 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method42 (args);
}
static int /*long*/ callback43 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method43 (args);
}
static int /*long*/ callback44 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method44 (args);
}
static int /*long*/ callback45 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method45 (args);
}
static int /*long*/ callback46 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method46 (args);
}
static int /*long*/ callback47 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method47 (args);
}
static int /*long*/ callback48 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method48 (args);
}
static int /*long*/ callback49 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method49 (args);
}
static int /*long*/ callback5 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method5 (args);
}
static int /*long*/ callback50 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method50 (args);
}
static int /*long*/ callback51 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method51 (args);
}
static int /*long*/ callback52 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method52 (args);
}
static int /*long*/ callback53 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method53 (args);
}
static int /*long*/ callback54 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method54 (args);
}
static int /*long*/ callback55 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method55 (args);
}
static int /*long*/ callback56 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method56 (args);
}
static int /*long*/ callback57 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method57 (args);
}
static int /*long*/ callback58 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method58 (args);
}
static int /*long*/ callback59 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method59 (args);
}
static int /*long*/ callback6 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method6 (args);
}
static int /*long*/ callback60 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method60 (args);
}
static int /*long*/ callback61 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method61 (args);
}
static int /*long*/ callback62 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method62 (args);
}
static int /*long*/ callback63 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method63 (args);
}
static int /*long*/ callback64 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method64 (args);
}
static int /*long*/ callback65 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method65 (args);
}
static int /*long*/ callback66 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method66 (args);
}
static int /*long*/ callback67 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method67 (args);
}
static int /*long*/ callback68 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method68 (args);
}
static int /*long*/ callback69 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method69 (args);
}
static int /*long*/ callback7 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method7 (args);
}
static int /*long*/ callback70 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method70 (args);
}
static int /*long*/ callback71 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method71 (args);
}
static int /*long*/ callback72 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method72 (args);
}
static int /*long*/ callback73 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method73 (args);
}
static int /*long*/ callback74 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method74 (args);
}
static int /*long*/ callback75 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method75 (args);
}
static int /*long*/ callback76 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method76 (args);
}
static int /*long*/ callback77 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method77 (args);
}
static int /*long*/ callback78 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method78 (args);
}
static int /*long*/ callback79 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method79 (args);
}
static int /*long*/ callback8 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method8 (args);
}
static int /*long*/ callback9 (int /*long*/[] callbackArgs) {
	// find the object on which this call was invoked
	int /*long*/ address = callbackArgs[0];
	Object object = ObjectMap.get (new LONG (address));
	if (object == null) return XPCOM.NS_ERROR_FAILURE;
	int /*long*/[] args = new int /*long*/[callbackArgs.length - 1];
	System.arraycopy (callbackArgs, 1, args, 0, args.length);
	return ((XPCOMObject) object).method9 (args);
}

public void dispose() {
	// free the memory for this reference
	int /*long*/[] pVtable = new int /*long*/[1];
	XPCOM.memmove (pVtable, ppVtable, C.PTR_SIZEOF);
	C.free (pVtable[0]);
	C.free (ppVtable);	

	// remove this ppVtable from the list
	ObjectMap.remove (new LONG (ppVtable));	

	ppVtable = 0;
}
	
public int /*long*/ getAddress () {
	return ppVtable;
}

public int /*long*/ method0 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method1 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method10 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method11 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method12 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method13 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method14 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method15 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method16 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method17 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method18 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method19 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method2 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method20 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method21 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method22 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method23 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method24 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method25 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method26 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method27 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method28 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method29 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method3 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method30 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method31 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method32 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method33 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method34 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method35 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method36 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method37 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method38 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method39 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method4 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method40 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method41 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method42 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method43 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method44 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method45 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method46 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method47 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method48 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method49 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method5 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method50 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method51 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method52 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method53 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method54 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method55 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method56 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method57 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method58 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method59 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method6 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method60 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method61 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method62 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method63 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method64 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method65 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method66 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method67 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method68 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method69 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method7 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method70 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method71 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method72 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method73 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method74 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method75 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method76 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method77 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method78 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method79 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method8 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
public int /*long*/ method9 (int /*long*/[] args) {
	return XPCOM.NS_ERROR_NOT_IMPLEMENTED;
}
}

