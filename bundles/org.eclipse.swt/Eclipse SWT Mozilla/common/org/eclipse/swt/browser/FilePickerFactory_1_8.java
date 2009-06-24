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

class FilePickerFactory_1_8 extends FilePickerFactory {

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

/* nsIFactory */

int CreateInstance (int /*long*/ aOuter, int /*long*/ iid, int /*long*/ result) {
	FilePicker_1_8 picker = new FilePicker_1_8 ();
	picker.AddRef ();
	XPCOM.memmove (result, new int /*long*/[] {picker.getAddress ()}, C.PTR_SIZEOF);
	return XPCOM.NS_OK;
}

}
