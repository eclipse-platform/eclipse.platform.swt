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

 
public class GNOME extends OS  {

public static final int GNOME_FILE_DOMAIN_PIXMAP = 4;
public static final int GNOME_ICON_LOOKUP_FLAGS_NONE = 0;
public static final int GNOME_PARAM_NONE = 0;
public static final int GNOME_VFS_MIME_APPLICATION_ARGUMENT_TYPE_URIS = 0;

public static final synchronized native int LIBGNOME_MODULE();
public static final synchronized native int gnome_icon_lookup(int icon_theme, int thumbnail_factory, byte[] file_uri, byte[] custom_icon, int file_info, byte[] mime_type, int flags, int[] result);
public static final synchronized native int gnome_icon_theme_lookup_icon(int theme, int icon_name, int size, int[] icon_data, int[] base_size);
public static final synchronized native int gnome_icon_theme_new();
public static final synchronized native int gnome_program_init(byte[] app_id, byte[] app_version, int module_info, int argc, int[] argv, int first_property_name);
public static final synchronized native int gnome_program_locate_file(int program, int domain, int file_name, boolean only_if_exists, int ret_location);
public static final synchronized native int gnome_vfs_get_registered_mime_types();
public static final synchronized native boolean gnome_vfs_init();
public static final synchronized native void gnome_vfs_mime_application_free(int application);
public static final synchronized native void gnome_vfs_mime_extensions_list_free(int list);
public static final synchronized native int gnome_vfs_mime_get_default_application(byte[] mimeType);
public static final synchronized native int gnome_vfs_mime_get_extensions_list(int mime_type);
public static final synchronized native int gnome_vfs_mime_get_icon(byte[] mime_type);
public static final synchronized native void gnome_vfs_mime_registered_mime_type_list_free(int list);
public static final synchronized native void memmove (GnomeVFSMimeApplication dest, int src, int count);
}
