/*******************************************************************************
 * Copyright (c) 2003, 2007 IBM Corporation and others.
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
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return Close ();}
		public int /*long*/ method4 (int /*long*/[] args) {return Available (args[0]);}
		public int /*long*/ method5 (int /*long*/[] args) {return Read (args[0], (int)/*64*/args[1], args[2]);}
		public int /*long*/ method6 (int /*long*/[] args) {return ReadSegments (args[0], args[1], (int)/*64*/args[2], args[3]);}
		public int /*long*/ method7 (int /*long*/[] args) {return IsNonBlocking (args[0]);}
	};
}

void disposeCOMInterfaces () {
	if (inputStream != null) {
		inputStream.dispose ();
		inputStream = null;	
	}
}

int /*long*/ getAddress () {
	return inputStream.getAddress ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	
	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {inputStream.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIInputStream.NS_IINPUTSTREAM_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {inputStream.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}	
	XPCOM.memmove (ppvObject, new int /*long*/[] {0}, C.PTR_SIZEOF);
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

int Available (int /*long*/ _retval) {
	int available = buffer == null ? 0 : buffer.length - index;
	XPCOM.memmove (_retval, new int[] {available}, 4);
	return XPCOM.NS_OK;
}

int Read(int /*long*/ aBuf, int aCount, int /*long*/ _retval) {
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

int ReadSegments (int /*long*/ aWriter, int /*long*/ aClosure, int aCount, int /*long*/ _retval) {
	int max = buffer == null ? 0 : buffer.length - index;
	if (aCount != -1) {
		max = Math.min (max, aCount);
	}
	int cnt = max;
	while (cnt > 0) {
		int[] aWriteCount = new int[1];
		int /*long*/ rc = XPCOM.Call (aWriter, getAddress (), aClosure, buffer, index, cnt, aWriteCount);
		if (rc != XPCOM.NS_OK) break;
		index += aWriteCount[0];
		cnt -= aWriteCount[0];
	}
	XPCOM.memmove (_retval, new int[] {max - cnt}, 4);
	return XPCOM.NS_OK;
}

int IsNonBlocking (int /*long*/ _retval) {
	/* blocking */
	XPCOM.memmove (_retval, new int[] {0}, 4);
	return XPCOM.NS_OK;
}		
}
