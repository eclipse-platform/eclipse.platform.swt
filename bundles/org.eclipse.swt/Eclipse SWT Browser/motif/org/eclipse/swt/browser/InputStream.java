/*******************************************************************************
 * Copyright (c) 2003, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.browser;

import org.eclipse.swt.internal.mozilla.*;

class InputStream {
	XPCOMObject inputStream;
	int refCount = 0;

	byte[] buffer;
	int index = 0;
	
public InputStream(byte[] buffer) {
	this.buffer = buffer;
	index = 0;
	createCOMInterfaces();
}

int AddRef() {
	refCount++;
	return refCount;
}

void createCOMInterfaces() {
	/* Create each of the interfaces that this object implements */
	inputStream = new XPCOMObject(new int[]{2, 0, 0, 0, 1, 3, 4, 1}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return Close();}
		public int method4(int[] args) {return Available(args[0]);}
		public int method5(int[] args) {return Read(args[0], args[1], args[2]);}
		public int method6(int[] args) {return ReadSegments(args[0], args[1], args[2], args[3]);}
		public int method7(int[] args) {return IsNonBlocking(args[0]);}
	};
}

void disposeCOMInterfaces() {
	if (inputStream != null) {
		inputStream.dispose();
		inputStream = null;	
	}
}

int getAddress() {
	return inputStream.getAddress();
}

int queryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);
	
	if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove(ppvObject, new int[] {inputStream.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIInputStream.NS_IINPUTSTREAM_IID)) {
		XPCOM.memmove(ppvObject, new int[] {inputStream.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}	
	XPCOM.memmove(ppvObject, new int[] {0}, 4);
	return XPCOM.NS_ERROR_NO_INTERFACE;
}
        	
int Release() {
	refCount--;
	if (refCount == 0) disposeCOMInterfaces();
	return refCount;
}
	
/* nsIInputStream implementation */

int Close() {
	buffer = null;
	index = 0;
	return XPCOM.NS_OK;
}

int Available(int _retval) {
	int available = buffer == null ? 0 : buffer.length - index;
	XPCOM.memmove(_retval, new int[] {available}, 4);
	return XPCOM.NS_OK;
}

int Read(int aBuf, int aCount, int _retval) {
	int max = Math.min(aCount, buffer == null ? 0 : buffer.length - index);
	if (max > 0) {
		byte[] src = new byte[max];
		System.arraycopy(buffer, index, src, 0, max);
		XPCOM.memmove(aBuf, src, max);
		index += max;
	}
	XPCOM.memmove(_retval, new int[] {max}, 4);
	return XPCOM.NS_OK;
}

int ReadSegments(int aWriter, int aClosure, int aCount, int _retval) {
	int max = Math.min(aCount, buffer == null ? 0 : buffer.length - index);
	int cnt = max;
	while (cnt > 0) {
		int[] aWriteCount = new int[1];
		int rc = XPCOM.Call(aWriter, getAddress(), aClosure, buffer, index, cnt, aWriteCount);
		if (rc != XPCOM.NS_OK) break;
		index += aWriteCount[0];
		cnt -= aWriteCount[0];
	}
	XPCOM.memmove(_retval, new int[] {max - cnt}, 4);
	return XPCOM.NS_OK;
}

int IsNonBlocking(int _retval) {
	/* blocking */
	XPCOM.memmove(_retval, new int[] {0}, 4);
	return XPCOM.NS_OK;
}		
}
