/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;

 
public class GNOME extends OS {

public static final int GNOME_VFS_MIME_APPLICATION_ARGUMENT_TYPE_URIS = 0;

public static final synchronized native int gnome_vfs_get_registered_mime_types();
public static final synchronized native boolean gnome_vfs_init();
public static final synchronized native void gnome_vfs_mime_application_free(int application);
public static final synchronized native void gnome_vfs_mime_extensions_list_free(int list);
public static final synchronized native int gnome_vfs_mime_get_default_application(byte[] mimeType);
public static final synchronized native int gnome_vfs_mime_get_extensions_list(int mime_type);
public static final synchronized native void gnome_vfs_mime_registered_mime_type_list_free(int list);
public static final synchronized native void memmove (GnomeVFSMimeApplication dest, int src, int count);

}
