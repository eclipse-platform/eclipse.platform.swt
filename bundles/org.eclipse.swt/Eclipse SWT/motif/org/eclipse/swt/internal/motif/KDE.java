package org.eclipse.swt.internal.motif;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
public class KDE extends OS {

public static final int KICON_SMALL = 0;

public static final native int KApplication_new(int qcString);

public static final native int KGlobal_iconLoader();

public static final native int KIconLoader_iconPath(int receiver, int iconQString, int iconType, int canReturnNull);

public static final native int KMimeType_findByURL(int kurl);
public static final native int KMimeType_icon(int receiver, int unused1, int unused2);
public static final native int KMimeType_name(int receiver);

public static final native int KService_allServices();
public static final native int KService_exec(int receiver);
public static final native int KService_icon(int receiver);
public static final native int KService_name(int receiver);
public static final native int KService_serviceByName(int serviceName);
public static final native int KService_type(int receiver);

public static final native int KServiceTypeProfile_preferredService(int mimeTypeQString, int needApp);

public static final native void KURL_delete(int receiver);

public static final native int KURL_new(int qString);

public static final native int KServiceList_begin(int receiver);
public static final native int KServiceList_delete(int receiver);
public static final native int KServiceList_end(int receiver);

public static final native int QCString_data(int receiver);
public static final native int QCString_delete(int receiver);
public static final native int QCString_new(byte[] string);

public static final native int QString_delete(int receiver);
public static final native int QString_equals(int receiver, int object);
public static final native int QString_new(byte[] string);
public static final native int QString_utf8(int receiver);

public static final native int KServiceListIterator_delete(int receiver);
public static final native int KServiceListIterator_dereference(int receiver);
public static final native void KServiceListIterator_increment(int receiver);
public static final native int KServiceListIterator_new(int listBeginning);
public static final native int KServiceListIterator_equals(int receiver, int object);

}
