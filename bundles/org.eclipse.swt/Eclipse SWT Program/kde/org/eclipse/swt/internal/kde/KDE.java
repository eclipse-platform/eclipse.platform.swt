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
 
public class KDE {

	/** Constants */
	public static final int KICON_SMALL = 0;
	public static final int SIGABRT = 6;
	public static final int SIGFPE = 8;
	public static final int SIGILL = 4;
	public static final int SIGSEGV = 11;
	public static final int XmUNSPECIFIED_PIXMAP = 0x2;

/** Natives */
public static final synchronized native int /*long*/ KApplication_new(int argc, int /*long*/ argv, int /*long*/ rAppName, boolean allowStyles, boolean GUIenabled);
public static final synchronized native int /*long*/ KGlobal_iconLoader();
public static final synchronized native int /*long*/ KIconLoader_iconPath(int /*long*/ loader, int /*long*/ name, int group_or_size, boolean canReturnNull);
public static final synchronized native void KMimeType_delete(int /*long*/ mimeType);
public static final synchronized native int /*long*/ KMimeType_mimeType(int /*long*/ _name);
public static final synchronized native int /*long*/ KMimeType_icon(int /*long*/ mimeType, int /*long*/ unused1, boolean unused2);
public static final synchronized native int /*long*/ KMimeType_name(int /*long*/ mimeType);
public static final synchronized native int /*long*/ KMimeType_patterns(int /*long*/ mimeType);
public static final synchronized native int /*long*/ KMimeType_offers(int /*long*/ _servicetype);
public static final synchronized native int /*long*/ KMimeType_allMimeTypes();
public static final synchronized native int /*long*/ KMimeTypeList_begin(int /*long*/ mimeTypeList);
public static final synchronized native void KMimeTypeList_delete(int /*long*/ mimeTypeList);
public static final synchronized native int /*long*/ KMimeTypeList_end(int /*long*/ mimeTypeList);
public static final synchronized native void KMimeTypeListIterator_delete(int /*long*/ iterator);
public static final synchronized native int /*long*/ KMimeTypeListIterator_dereference(int /*long*/ iterator);
public static final synchronized native boolean KMimeTypeListIterator_equals(int /*long*/ iterator, int /*long*/ iterator2);
public static final synchronized native void KMimeTypeListIterator_increment(int /*long*/ iterator);
public static final synchronized native int /*long*/ QStringList_begin(int /*long*/ qstringList);
public static final synchronized native void QStringList_delete(int /*long*/ qstringList);
public static final synchronized native int /*long*/ QStringList_end(int /*long*/ qstringList);
public static final synchronized native void QStringListIterator_delete(int /*long*/ iterator);
public static final synchronized native int /*long*/ QStringListIterator_dereference(int /*long*/ iterator);
public static final synchronized native boolean QStringListIterator_equals(int /*long*/ iterator, int /*long*/ iterator2);
public static final synchronized native void QStringListIterator_increment(int /*long*/ iterator);
public static final synchronized native int /*long*/ KURL_new(int /*long*/ qURLString);
public static final synchronized native void KURL_delete(int /*long*/ url);
public static final synchronized native int KRun_runURL(int /*long*/ url, int /*long*/ mimeTypeName);
public static final synchronized native void KServiceList_delete(int /*long*/ serviceList);
public static final synchronized native int /*long*/ QCString_data(int /*long*/ qcString);
public static final synchronized native void QCString_delete(int /*long*/ qcString);
public static final synchronized native int /*long*/ QCString_new(byte[] string);
public static final synchronized native void QString_delete(int /*long*/ qString);
public static final synchronized native boolean QString_equals(int /*long*/ qString, int /*long*/ qString2);
public static final synchronized native int /*long*/ QString_new(byte[] string);
public static final synchronized native int /*long*/ QString_utf8(int /*long*/ qString);
public static final synchronized native void free(int /*long*/ mem);
public static final synchronized native int /*long*/ malloc(int /*long*/ size);
public static final synchronized native int XpmReadFileToPixmap(int /*long*/ display, int /*long*/ drawable, byte[] filename, int /*long*/ [] pixmap_return, int /*long*/ [] mask_return, int /*long*/ attributes);
public static final synchronized native void XFreePixmap(int /*long*/ display, int /*long*/ pixmap);
public static final synchronized native int sigaction(int signum, byte[] act, byte[] oldact);
public static final synchronized native int sigaction_sizeof();

}
