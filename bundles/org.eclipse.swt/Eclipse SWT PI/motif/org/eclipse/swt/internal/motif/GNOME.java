package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
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
