/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.kde;

import org.eclipse.swt.internal.*;
 
public class KDE extends Platform {
	static {
		Library.loadLibrary("swt-kde");
	}

	/** Constants */
	public static final int KICON_SMALL = 0;
	public static final int SIGABRT = 6;
	public static final int SIGFPE = 8;
	public static final int SIGILL = 4;
	public static final int SIGSEGV = 11;
	public static final int XmUNSPECIFIED_PIXMAP = 0x2;

/** Natives */
public static final native int /*long*/ _KApplication_new(int argc, int /*long*/ argv, int /*long*/ rAppName, boolean allowStyles, boolean GUIenabled);
public static final int /*long*/ KApplication_new(int argc, int /*long*/ argv, int /*long*/ rAppName, boolean allowStyles, boolean GUIenabled) {
	lock.lock();
	try {
		return _KApplication_new(argc, argv, rAppName, allowStyles, GUIenabled);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KGlobal_iconLoader();
public static final int /*long*/ KGlobal_iconLoader() {
	lock.lock();
	try {
		return _KGlobal_iconLoader();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KIconLoader_iconPath(int /*long*/ loader, int /*long*/ name, int group_or_size, boolean canReturnNull);
public static final int /*long*/ KIconLoader_iconPath(int /*long*/ loader, int /*long*/ name, int group_or_size, boolean canReturnNull) {
	lock.lock();
	try {
		return _KIconLoader_iconPath(loader, name, group_or_size, canReturnNull);
	} finally {
		lock.unlock();
	}
}
public static final native void _KMimeType_delete(int /*long*/ mimeType);
public static final void KMimeType_delete(int /*long*/ mimeType) {
	lock.lock();
	try {
		_KMimeType_delete(mimeType);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeType_mimeType(int /*long*/ _name);
public static final int /*long*/ KMimeType_mimeType(int /*long*/ _name) {
	lock.lock();
	try {
		return _KMimeType_mimeType(_name);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeType_icon(int /*long*/ mimeType, int /*long*/ unused1, boolean unused2);
public static final int /*long*/ KMimeType_icon(int /*long*/ mimeType, int /*long*/ unused1, boolean unused2) {
	lock.lock();
	try {
		return _KMimeType_icon(mimeType, unused1, unused2);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeType_name(int /*long*/ mimeType);
public static final int /*long*/ KMimeType_name(int /*long*/ mimeType) {
	lock.lock();
	try {
		return _KMimeType_name(mimeType);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeType_patterns(int /*long*/ mimeType);
public static final int /*long*/ KMimeType_patterns(int /*long*/ mimeType) {
	lock.lock();
	try {
		return _KMimeType_patterns(mimeType);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeType_offers(int /*long*/ _servicetype);
public static final int /*long*/ KMimeType_offers(int /*long*/ _servicetype) {
	lock.lock();
	try {
		return _KMimeType_offers(_servicetype);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeType_allMimeTypes();
public static final int /*long*/ KMimeType_allMimeTypes() {
	lock.lock();
	try {
		return _KMimeType_allMimeTypes();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeTypeList_begin(int /*long*/ mimeTypeList);
public static final int /*long*/ KMimeTypeList_begin(int /*long*/ mimeTypeList) {
	lock.lock();
	try {
		return _KMimeTypeList_begin(mimeTypeList);
	} finally {
		lock.unlock();
	}
}
public static final native void _KMimeTypeList_delete(int /*long*/ mimeTypeList);
public static final void KMimeTypeList_delete(int /*long*/ mimeTypeList) {
	lock.lock();
	try {
		_KMimeTypeList_delete(mimeTypeList);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeTypeList_end(int /*long*/ mimeTypeList);
public static final int /*long*/ KMimeTypeList_end(int /*long*/ mimeTypeList) {
	lock.lock();
	try {
		return _KMimeTypeList_end(mimeTypeList);
	} finally {
		lock.unlock();
	}
}
public static final native void _KMimeTypeListIterator_delete(int /*long*/ iterator);
public static final void KMimeTypeListIterator_delete(int /*long*/ iterator) {
	lock.lock();
	try {
		_KMimeTypeListIterator_delete(iterator);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KMimeTypeListIterator_dereference(int /*long*/ iterator);
public static final int /*long*/ KMimeTypeListIterator_dereference(int /*long*/ iterator) {
	lock.lock();
	try {
		return _KMimeTypeListIterator_dereference(iterator);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _KMimeTypeListIterator_equals(int /*long*/ iterator, int /*long*/ iterator2);
public static final boolean KMimeTypeListIterator_equals(int /*long*/ iterator, int /*long*/ iterator2) {
	lock.lock();
	try {
		return _KMimeTypeListIterator_equals(iterator, iterator2);
	} finally {
		lock.unlock();
	}
}
public static final native void _KMimeTypeListIterator_increment(int /*long*/ iterator);
public static final void KMimeTypeListIterator_increment(int /*long*/ iterator) {
	lock.lock();
	try {
		_KMimeTypeListIterator_increment(iterator);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _QStringList_begin(int /*long*/ qstringList);
public static final int /*long*/ QStringList_begin(int /*long*/ qstringList) {
	lock.lock();
	try {
		return _QStringList_begin(qstringList);
	} finally {
		lock.unlock();
	}
}
public static final native void _QStringList_delete(int /*long*/ qstringList);
public static final void QStringList_delete(int /*long*/ qstringList) {
	lock.lock();
	try {
		_QStringList_delete(qstringList);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _QStringList_end(int /*long*/ qstringList);
public static final int /*long*/ QStringList_end(int /*long*/ qstringList) {
	lock.lock();
	try {
		return _QStringList_end(qstringList);
	} finally {
		lock.unlock();
	}
}
public static final native void _QStringListIterator_delete(int /*long*/ iterator);
public static final void QStringListIterator_delete(int /*long*/ iterator) {
	lock.lock();
	try {
		_QStringListIterator_delete(iterator);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _QStringListIterator_dereference(int /*long*/ iterator);
public static final int /*long*/ QStringListIterator_dereference(int /*long*/ iterator) {
	lock.lock();
	try {
		return _QStringListIterator_dereference(iterator);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _QStringListIterator_equals(int /*long*/ iterator, int /*long*/ iterator2);
public static final boolean QStringListIterator_equals(int /*long*/ iterator, int /*long*/ iterator2) {
	lock.lock();
	try {
		return _QStringListIterator_equals(iterator, iterator2);
	} finally {
		lock.unlock();
	}
}
public static final native void _QStringListIterator_increment(int /*long*/ iterator);
public static final void QStringListIterator_increment(int /*long*/ iterator) {
	lock.lock();
	try {
		_QStringListIterator_increment(iterator);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _KURL_new(int /*long*/ qURLString);
public static final int /*long*/ KURL_new(int /*long*/ qURLString) {
	lock.lock();
	try {
		return _KURL_new(qURLString);
	} finally {
		lock.unlock();
	}
}
public static final native void _KURL_delete(int /*long*/ url);
public static final void KURL_delete(int /*long*/ url) {
	lock.lock();
	try {
		_KURL_delete(url);
	} finally {
		lock.unlock();
	}
}
public static final native int _KRun_runURL(int /*long*/ url, int /*long*/ mimeTypeName);
public static final int KRun_runURL(int /*long*/ url, int /*long*/ mimeTypeName) {
	lock.lock();
	try {
		return _KRun_runURL(url, mimeTypeName);
	} finally {
		lock.unlock();
	}
}
public static final native void _KServiceList_delete(int /*long*/ serviceList);
public static final void KServiceList_delete(int /*long*/ serviceList) {
	lock.lock();
	try {
		_KServiceList_delete(serviceList);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _QCString_data(int /*long*/ qcString);
public static final int /*long*/ QCString_data(int /*long*/ qcString) {
	lock.lock();
	try {
		return _QCString_data(qcString);
	} finally {
		lock.unlock();
	}
}
public static final native void _QCString_delete(int /*long*/ qcString);
public static final void QCString_delete(int /*long*/ qcString) {
	lock.lock();
	try {
		_QCString_delete(qcString);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _QCString_new(byte[] string);
public static final int /*long*/ QCString_new(byte[] string) {
	lock.lock();
	try {
		return _QCString_new(string);
	} finally {
		lock.unlock();
	}
}
public static final native void _QString_delete(int /*long*/ qString);
public static final void QString_delete(int /*long*/ qString) {
	lock.lock();
	try {
		_QString_delete(qString);
	} finally {
		lock.unlock();
	}
}
public static final native boolean _QString_equals(int /*long*/ qString, int /*long*/ qString2);
public static final boolean QString_equals(int /*long*/ qString, int /*long*/ qString2) {
	lock.lock();
	try {
		return _QString_equals(qString, qString2);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _QString_new(byte[] string);
public static final int /*long*/ QString_new(byte[] string) {
	lock.lock();
	try {
		return _QString_new(string);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _QString_utf8(int /*long*/ qString);
public static final int /*long*/ QString_utf8(int /*long*/ qString) {
	lock.lock();
	try {
		return _QString_utf8(qString);
	} finally {
		lock.unlock();
	}
}
public static final native void _free(int /*long*/ mem);
public static final void free(int /*long*/ mem) {
	lock.lock();
	try {
		_free(mem);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _malloc(int /*long*/ size);
public static final int /*long*/ malloc(int /*long*/ size) {
	lock.lock();
	try {
		return _malloc(size);
	} finally {
		lock.unlock();
	}
}
public static final native int _XpmReadFileToPixmap(int /*long*/ display, int /*long*/ drawable, byte[] filename, int /*long*/ [] pixmap_return, int /*long*/ [] mask_return, int /*long*/ attributes);
public static final int XpmReadFileToPixmap(int /*long*/ display, int /*long*/ drawable, byte[] filename, int /*long*/ [] pixmap_return, int /*long*/ [] mask_return, int /*long*/ attributes) {
	lock.lock();
	try {
		return _XpmReadFileToPixmap(display, drawable, filename, pixmap_return, mask_return, attributes);
	} finally {
		lock.unlock();
	}
}
public static final native void _XFreePixmap(int /*long*/ display, int /*long*/ pixmap);
public static final void XFreePixmap(int /*long*/ display, int /*long*/ pixmap) {
	lock.lock();
	try {
		_XFreePixmap(display, pixmap);
	} finally {
		lock.unlock();
	}
}
public static final native int _sigaction(int signum, byte[] act, byte[] oldact);
public static final int sigaction(int signum, byte[] act, byte[] oldact) {
	lock.lock();
	try {
		return _sigaction(signum, act, oldact);
	} finally {
		lock.unlock();
	}
}
public static final native int _sigaction_sizeof();
public static final int sigaction_sizeof() {
	lock.lock();
	try {
		return _sigaction_sizeof();
	} finally {
		lock.unlock();
	}
}

}
