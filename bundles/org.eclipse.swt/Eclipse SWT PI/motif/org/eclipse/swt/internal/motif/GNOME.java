package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class GNOME extends OS {

public static final int GNOME_VFS_MIME_APPLICATION_ARGUMENT_TYPE_URIS = 0;

public static final synchronized native int g_list_next(int list);
public static final synchronized native int gnome_vfs_get_registered_mime_types();
public static final synchronized native void gnome_vfs_mime_application_free(int application);
public static final synchronized native void gnome_vfs_mime_extensions_list_free(int list);
public static final synchronized native int gnome_vfs_mime_get_default_application(byte[] mimeType);
public static final synchronized native int gnome_vfs_mime_get_extensions_list(int mime_type);
public static final synchronized native void gnome_vfs_mime_registered_mime_type_list_free(int list);
public static final synchronized native void memmove (GnomeVFSMimeApplication dest, int src, int count);

}
