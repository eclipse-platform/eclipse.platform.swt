package org.eclipse.swt.internal.motif;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */
 
public class GNOME extends OS {

public static final native int g_get_home_dir();
public static final native int gnome_datadir_file(byte[] fileName);
public static final native void gnome_desktop_entry_free(int entry);
public static final native int gnome_desktop_entry_load(byte[] fileName);
public static final native int gnome_mime_get_value(byte[] mimeType, byte[] key);
public static final native int gnome_mime_type(byte[] fileName);
public static final native int gnome_pixmap_file(byte[] fileName);

}