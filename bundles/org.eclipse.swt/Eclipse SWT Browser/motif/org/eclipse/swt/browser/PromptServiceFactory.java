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

class PromptServiceFactory {
	XPCOMObject supports;
	XPCOMObject factory;
	int refCount = 0;

public PromptServiceFactory() {
	createCOMInterfaces();
}

int AddRef() {
	refCount++;
	return refCount;
}

void createCOMInterfaces() {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject(new int[]{2, 0, 0}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
	};
	
	factory = new XPCOMObject(new int[]{2, 0, 0, 3, 1}){
		public int method0(int[] args) {return queryInterface(args[0], args[1]);}
		public int method1(int[] args) {return AddRef();}
		public int method2(int[] args) {return Release();}
		public int method3(int[] args) {return CreateInstance(args[0], args[1], args[2]);}
		public int method4(int[] args) {return LockFactory(args[0]);}
	};
}

void disposeCOMInterfaces() {
	if (supports != null) {
		supports.dispose();
		supports = null;
	}	
	if (factory != null) {
		factory.dispose();
		factory = null;	
	}
}

int getAddress() {
	return factory.getAddress();
}

int queryInterface(int riid, int ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID();
	XPCOM.memmove(guid, riid, nsID.sizeof);
	
	if (guid.Equals(nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove(ppvObject, new int[] {supports.getAddress()}, 4);
		AddRef();
		return XPCOM.NS_OK;
	}
	if (guid.Equals(nsIFactory.NS_IFACTORY_IID)) {
		XPCOM.memmove(ppvObject, new int[] {factory.getAddress()}, 4);
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
	
/* nsIFactory */

public int CreateInstance(int aOuter, int iid, int result) {
	PromptService promptService = new PromptService();
	promptService.AddRef();
	XPCOM.memmove(result, new int[] {promptService.getAddress()}, 4);
	return XPCOM.NS_OK;
}

public int LockFactory(int lock) {
	return XPCOM.NS_OK;
}
}
