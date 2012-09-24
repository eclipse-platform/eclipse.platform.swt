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

class InputStream {
	XPCOMObject inputStream;
	int refCount = 0;

	byte[] buffer;
	int index = 0;
	
InputStream (byte[] buffer) {
	this.buffer = buffer;
	index = 0;
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	inputStream = new XPCOMObject (new int[] {2, 0, 0, 0, 1, 3, 4, 1}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		public long /*int*/ method3 (long /*int*/[] args) {return Close ();}
		public long /*int*/ method4 (long /*int*/[] args) {return Available (args[0]);}
		public long /*int*/ method5 (long /*int*/[] args) {return Read (args[0], (int)/*64*/args[1], args[2]);}
		public long /*int*/ method6 (long /*int*/[] args) {return ReadSegments (args[0], args[1], (int)/*64*/args[2], args[3]);}
		public long /*int*/ method7 (long /*int*/[] args) {return IsNonBlocking (args[0]);}
	};
}

void disposeCOMInterfaces () {
	if (inputStream != null) {
		inputStream.dispose ();
		inputStream = null;	
	}
}

long /*int*/ getAddress () {
	return inputStream.getAddress ();
}

int QueryInterface (long /*int*/ riid, long /*int*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	
	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {inputStream.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIInputStream.NS_IINPUTSTREAM_IID)) {
		XPCOM.memmove (ppvObject, new long /*int*/[] {inputStream.getAddress ()}, C.PTR_SIZEOF);
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
	
/* nsIInputStream implementation */

int Close () {
	buffer = null;
	index = 0;
	return XPCOM.NS_OK;
}

int Available (long /*int*/ _retval) {
	int available = buffer == null ? 0 : buffer.length - index;
	XPCOM.memmove (_retval, new int[] {available}, 4);
	return XPCOM.NS_OK;
}

int Read(long /*int*/ aBuf, int aCount, long /*int*/ _retval) {
	int max = Math.min (aCount, buffer == null ? 0 : buffer.length - index);
	if (max > 0) {
		byte[] src = new byte[max];
		System.arraycopy (buffer, index, src, 0, max);
		XPCOM.memmove (aBuf, src, max);
		index += max;
	}
	XPCOM.memmove(_retval, new int[] {max}, 4);
	return XPCOM.NS_OK;
}

int ReadSegments (long /*int*/ aWriter, long /*int*/ aClosure, int aCount, long /*int*/ _retval) {
	int max = buffer == null ? 0 : buffer.length - index;
	if (aCount != -1) {
		max = Math.min (max, aCount);
	}
	int cnt = max;
	while (cnt > 0) {
		int[] aWriteCount = new int[1];
		long /*int*/ rc = XPCOM.Call (aWriter, getAddress (), aClosure, buffer, index, cnt, aWriteCount);
		if (rc != XPCOM.NS_OK) break;
		index += aWriteCount[0];
		cnt -= aWriteCount[0];
	}
	XPCOM.memmove (_retval, new int[] {max - cnt}, 4);
	return XPCOM.NS_OK;
}

int IsNonBlocking (long /*int*/ _retval) {
	/* blocking */
	XPCOM.memmove (_retval, new boolean[] {false});
	return XPCOM.NS_OK;
}		
}
