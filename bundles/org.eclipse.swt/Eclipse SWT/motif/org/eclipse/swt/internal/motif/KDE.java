package org.eclipse.swt.internal.motif;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class KDE extends OS {

public static final int KICON_SMALL = 0;

public static final native int  KApplication_new(int qcString);

public static final native int  KGlobal_iconLoader();

public static final native int  KIconLoader_iconPath(int loader, int iconQString, int iconType, int canReturnNull);

public static final native int  KMimeType_mimeType(int mimeTypeName);
public static final native int  KMimeType_icon(int mimeType, int unused1, int unused2);
public static final native int  KMimeType_name(int mimeType);
public static final native int  KMimeType_patterns(int mimeType);
public static final native int  KMimeType_offers(int mimeTypeName);
public static final native int  KMimeType_allMimeTypes();

public static final native int  KMimeTypeList_begin(int mimeTypeList);
public static final native int  KMimeTypeList_delete(int mimeTypeList);
public static final native int  KMimeTypeList_end(int mimeTypeList);

public static final native int  KMimeTypeListIterator_delete(int iterator);
public static final native int  KMimeTypeListIterator_dereference(int iterator);
public static final native int  KMimeTypeListIterator_equals(int iterator, int iterator2);
public static final native void KMimeTypeListIterator_increment(int iterator);

public static final native int  QStringList_begin(int qstringList);
public static final native int  QStringList_delete(int qstringList);
public static final native int  QStringList_end(int qstringList);

public static final native int  QStringListIterator_delete(int iterator);
public static final native int  QStringListIterator_dereference(int iterator);
public static final native int  QStringListIterator_equals(int iterator, int iterator2);
public static final native void QStringListIterator_increment(int iterator);

public static final native int  KURL_new( int qURLString );
public static final native void KURL_delete( int url );
public static final native int  KRun_runURL( int url, int mimeTypeName );

public static final native int  KServiceList_delete(int serviceList);

public static final native int  QCString_data(int qcString);
public static final native int  QCString_delete(int qcString);
public static final native int  QCString_new(byte[] string);

public static final native int  QString_delete(int qString);
public static final native int  QString_equals(int qString, int qString2);
public static final native int  QString_new(byte[] string);
public static final native int  QString_utf8(int qString);

}
