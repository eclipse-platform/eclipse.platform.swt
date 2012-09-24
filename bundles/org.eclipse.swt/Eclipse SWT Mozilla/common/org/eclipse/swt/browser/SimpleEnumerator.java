/*******************************************************************************
 * Copyright (c) 2003, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.internal.C;
import org.eclipse.swt.internal.mozilla.*;

class SimpleEnumerator {
	XPCOMObject supports;
	XPCOMObject simpleEnumerator;
	int refCount = 0;
	nsISupports[] values;
	int index = 0;

SimpleEnumerator (nsISupports[] values) {
	this.values = values;
	for (int i = 0; i < values.length; i++) {
		values[i].AddRef ();
	}
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
	};

	simpleEnumerator = new XPCOMObject (new int[] {2, 0, 0, 1, 1}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		public long /*int*/ method3 (long /*int*/[] args) {return HasMoreElements (args[0]);}
		public long /*int*/ method4 (long /*int*/[] args) {return GetNext (args[0]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (simpleEnumerator != null) {
		simpleEnumerator.dispose ();
		simpleEnumerator = null;	
	}
	if (values != null) {
		for (int i = 0; i < values.length; i++) {
			values[i].Release ();
		}
		values = null;
	}
}

long /*int*/ getAddress () {
	return simpleEnumerator.getAddress ();
}

int QueryInterface (long /*int*/ riid, long /*int*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);

	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsISimpleEnumerator.NS_ISIMPLEENUMERATOR_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {simpleEnumerator.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}

	XPCOM.memmove (ppvObject, new long /*int*/[] {0}, C.PTR_SIZEOF);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}

int Release () {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces ();
	return refCount;
}

int HasMoreElements (long /*int*/ _retval) {
	boolean more = values != null && index < values.length;
	XPCOM.memmove (_retval, new boolean[] {more});
	return XPCOM.NS_OK;
}	
	
int GetNext (long /*int*/ _retval) {
	if (values == null || index == values.length) return XPCOM.NS_ERROR_UNEXPECTED;
	nsISupports value = values[index++];
    value.AddRef ();
    XPCOM.memmove (_retval, new long /*int*/[] {value.getAddress ()}, C.PTR_SIZEOF);
    return XPCOM.NS_OK;
}		
}

