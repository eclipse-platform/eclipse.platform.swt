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

class FilePickerFactory {
	XPCOMObject supports;
	XPCOMObject factory;
	int refCount = 0;

FilePickerFactory () {
	createCOMInterfaces ();
}

int AddRef () {
	refCount++;
	return refCount;
}

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
	};
	
	factory = new XPCOMObject (new int[] {2, 0, 0, 3, 1}) {
		public int /*long*/ method0 (int /*long*/[] args) {return QueryInterface (args[0], args[1]);}
		public int /*long*/ method1 (int /*long*/[] args) {return AddRef ();}
		public int /*long*/ method2 (int /*long*/[] args) {return Release ();}
		public int /*long*/ method3 (int /*long*/[] args) {return CreateInstance (args[0], args[1], args[2]);}
		public int /*long*/ method4 (int /*long*/[] args) {return LockFactory ((int)/*64*/args[0]);}
	};
}

void disposeCOMInterfaces () {
	if (supports != null) {
		supports.dispose ();
		supports = null;
	}	
	if (factory != null) {
		factory.dispose ();
		factory = null;	
	}
}

int /*long*/ getAddress () {
	return factory.getAddress ();
}

int QueryInterface (int /*long*/ riid, int /*long*/ ppvObject) {
	if (riid == 0 || ppvObject == 0) return XPCOM.NS_ERROR_NO_INTERFACE;
	nsID guid = new nsID ();
	XPCOM.memmove (guid, riid, nsID.sizeof);
	
	if (guid.Equals (nsISupports.NS_ISUPPORTS_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {supports.getAddress ()}, C.PTR_SIZEOF);
		AddRef ();
		return XPCOM.NS_OK;
	}
	if (guid.Equals (nsIFactory.NS_IFACTORY_IID)) {
		XPCOM.memmove (ppvObject, new int /*long*/[] {factory.getAddress ()}, C.PTR_SIZEOF);
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
	
/* nsIFactory */

int CreateInstance (int /*long*/ aOuter, int /*long*/ iid, int /*long*/ result) {
	if (!Mozilla.IsPre_4) {
		FilePicker_10 picker = new FilePicker_10 ();
		picker.AddRef ();
		XPCOM.memmove (result, new int /*long*/[] {picker.getAddress ()}, C.PTR_SIZEOF);
	} else if (Mozilla.IsXULRunner) {
		FilePicker_1_8 picker = new FilePicker_1_8 ();
		picker.AddRef ();
		XPCOM.memmove (result, new int /*long*/[] {picker.getAddress ()}, C.PTR_SIZEOF);
	} else {
		FilePicker picker = new FilePicker ();
		picker.AddRef ();
		XPCOM.memmove (result, new int /*long*/[] {picker.getAddress ()}, C.PTR_SIZEOF);
	}
	return XPCOM.NS_OK;
}

int LockFactory (int lock) {
	return XPCOM.NS_OK;
}
}
