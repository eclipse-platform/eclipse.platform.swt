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

import org.eclipse.swt.internal.mozilla.*;

class FilePicker_1_8 extends FilePicker {

void createCOMInterfaces () {
	/* Create each of the interfaces that this object implements */
	supports = new XPCOMObject (new int[] {2, 0, 0}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
	};

	filePicker = new XPCOMObject (new int[] {2, 0, 0, 3, 1, 2, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1}) {
		public long /*int*/ method0 (long /*int*/[] args) {return QueryInterface (args[0], args[1]);}
		public long /*int*/ method1 (long /*int*/[] args) {return AddRef ();}
		public long /*int*/ method2 (long /*int*/[] args) {return Release ();}
		public long /*int*/ method3 (long /*int*/[] args) {return Init (args[0], args[1], (short)args[2]);}
		public long /*int*/ method4 (long /*int*/[] args) {return AppendFilters ((int)/*64*/args[0]);}
		public long /*int*/ method5 (long /*int*/[] args) {return AppendFilter (args[0], args[1]);}
		public long /*int*/ method6 (long /*int*/[] args) {return GetDefaultString (args[0]);}
		public long /*int*/ method7 (long /*int*/[] args) {return SetDefaultString (args[0]);}
		public long /*int*/ method8 (long /*int*/[] args) {return GetDefaultExtension (args[0]);}
		public long /*int*/ method9 (long /*int*/[] args) {return SetDefaultExtension (args[0]);}
		public long /*int*/ method10 (long /*int*/[] args) {return GetFilterIndex (args[0]);}
		public long /*int*/ method11 (long /*int*/[] args) {return SetFilterIndex ((int)/*64*/args[0]);}
		public long /*int*/ method12 (long /*int*/[] args) {return GetDisplayDirectory (args[0]);}
		public long /*int*/ method13 (long /*int*/[] args) {return SetDisplayDirectory (args[0]);}
		public long /*int*/ method14 (long /*int*/[] args) {return GetFile (args[0]);}
		public long /*int*/ method15 (long /*int*/[] args) {return GetFileURL (args[0]);}
		public long /*int*/ method16 (long /*int*/[] args) {return GetFiles (args[0]);}
		public long /*int*/ method17 (long /*int*/[] args) {return Show (args[0]);}
	};
}

/*
 * As of Mozilla 1.8 some of nsIFilePicker's string arguments changed type.  This method
 * answers a java string based on the type of string that is appropriate for the Mozilla
 * version being used.
 */
String parseAString (long /*int*/ string) {
	if (string == 0) return null;
	int length = XPCOM.nsEmbedString_Length (string);
	long /*int*/ buffer = XPCOM.nsEmbedString_get (string);
	char[] chars = new char[length];
	XPCOM.memmove (chars, buffer, length * 2);
	return new String (chars);
}
}
