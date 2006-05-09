/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others. All rights reserved.
 * The contents of this file are made available under the terms
 * of the GNU Lesser General Public License (LGPL) Version 2.1 that
 * accompanies this distribution (lgpl-v21.txt).  The LGPL is also
 * available at http://www.gnu.org/licenses/lgpl.html.  If the version
 * of the LGPL at http://www.gnu.org is different to the version of
 * the LGPL accompanying this distribution and there is any conflict
 * between the two license versions, the terms of the LGPL accompanying
 * this distribution shall govern.
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gnome;

import org.eclipse.swt.internal.*;

public class GNOME extends Platform {
	static {
		Library.loadLibrary("swt-gnome");
	}

public static final int GNOME_FILE_DOMAIN_PIXMAP = 4;
public static final int GNOME_ICON_LOOKUP_FLAGS_NONE = 0;
public static final int GNOME_PARAM_NONE = 0;
public static final int GNOME_VFS_MIME_APPLICATION_ARGUMENT_TYPE_URIS = 0;
public static final int GNOME_VFS_OK = 0;
public static final int GNOME_VFS_MAKE_URI_DIR_NONE = 0;
public static final int GNOME_VFS_MAKE_URI_DIR_HOMEDIR = 1<<0;
public static final int GNOME_VFS_MAKE_URI_DIR_CURRENT = 1<<1;

/** 64 bit */
public static final native int GnomeVFSMimeApplication_sizeof();

/** Natives */
public static final native void _g_free(int /*long*/ mem);
public static final void g_free(int /*long*/ mem) {
	lock.lock();
	try {
		_g_free(mem);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _g_list_append(int /*long*/ list, int /*long*/ data);
public static final int /*long*/ g_list_append(int /*long*/ list, int /*long*/ data) {
	lock.lock();
	try {
		return _g_list_append(list, data);
	} finally {
		lock.unlock();
	}
}
public static final native void _g_list_free(int /*long*/ list);
public static final void g_list_free(int /*long*/ list) {
	lock.lock();
	try {
		_g_list_free(list);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _g_list_next(int /*long*/ list);
public static final int /*long*/ g_list_next(int /*long*/ list) {
	lock.lock();
	try {
		return _g_list_next(list);
	} finally {
		lock.unlock();
	}
}
public static final native void _g_object_unref(int /*long*/ object);
public static final void g_object_unref(int /*long*/ object) {
	lock.lock();
	try {
		_g_object_unref(object);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_icon_lookup(int /*long*/ icon_theme, int /*long*/ thumbnail_factory, byte[] file_uri, byte[] custom_icon, int /*long*/ file_info, byte[] mime_type, int flags, int[] result);
public static final int /*long*/ gnome_icon_lookup(int /*long*/ icon_theme, int /*long*/ thumbnail_factory, byte[] file_uri, byte[] custom_icon, int /*long*/ file_info, byte[] mime_type, int flags, int[] result) {
	lock.lock();
	try {
		return _gnome_icon_lookup(icon_theme, thumbnail_factory, file_uri, custom_icon, file_info, mime_type, flags, result);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_icon_theme_lookup_icon(int /*long*/ theme, int /*long*/ icon_name, int size, int /*long*/[] icon_data, int[] base_size);
public static final int /*long*/ gnome_icon_theme_lookup_icon(int /*long*/ theme, int /*long*/ icon_name, int size, int /*long*/[] icon_data, int[] base_size) {
	lock.lock();
	try {
		return _gnome_icon_theme_lookup_icon(theme, icon_name, size, icon_data, base_size);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_icon_theme_new();
public static final int /*long*/ gnome_icon_theme_new() {
	lock.lock();
	try {
		return _gnome_icon_theme_new();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_vfs_get_registered_mime_types();
public static final int /*long*/ gnome_vfs_get_registered_mime_types() {
	lock.lock();
	try {
		return _gnome_vfs_get_registered_mime_types();
	} finally {
		lock.unlock();
	}
}
public static final native boolean _gnome_vfs_init();
public static final boolean gnome_vfs_init() {
	lock.lock();
	try {
		return _gnome_vfs_init();
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_vfs_make_uri_from_input(byte[] uri);
public static final int /*long*/ gnome_vfs_make_uri_from_input(byte[] uri) {
	lock.lock();
	try {
		return _gnome_vfs_make_uri_from_input(uri);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_vfs_make_uri_from_input_with_dirs(byte[] uri, int dirs);
public static final int /*long*/ gnome_vfs_make_uri_from_input_with_dirs(byte[] uri, int dirs) {
	lock.lock();
	try {
		return _gnome_vfs_make_uri_from_input_with_dirs(uri, dirs);
	} finally {
		lock.unlock();
	}
}
public static final native void _gnome_vfs_mime_application_free(int /*long*/ application);
public static final void gnome_vfs_mime_application_free(int /*long*/ application) {
	lock.lock();
	try {
		_gnome_vfs_mime_application_free(application);
	} finally {
		lock.unlock();
	}
}
public static final native int _gnome_vfs_mime_application_launch(int /*long*/ application, int /*long*/ uris);
public static final int gnome_vfs_mime_application_launch(int /*long*/ application, int /*long*/ uris) {	
	lock.lock();
	try {
		return _gnome_vfs_mime_application_launch(application, uris);
	} finally {
		lock.unlock();
	}
}
public static final native void _gnome_vfs_mime_extensions_list_free(int /*long*/ list);
public static final void gnome_vfs_mime_extensions_list_free(int /*long*/ list) {
	lock.lock();
	try {
		_gnome_vfs_mime_extensions_list_free(list);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_vfs_mime_get_default_application(byte[] mimeType);
public static final int /*long*/ gnome_vfs_mime_get_default_application(byte[] mimeType) {
	lock.lock();
	try {
		return _gnome_vfs_mime_get_default_application(mimeType);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_vfs_mime_get_extensions_list(int /*long*/ mime_type);
public static final int /*long*/ gnome_vfs_mime_get_extensions_list(int /*long*/ mime_type) {
	lock.lock();
	try {
		return _gnome_vfs_mime_get_extensions_list(mime_type);
	} finally {
		lock.unlock();
	}
}
public static final native void _gnome_vfs_mime_registered_mime_type_list_free(int /*long*/ list);
public static final void gnome_vfs_mime_registered_mime_type_list_free(int /*long*/ list) {
	lock.lock();
	try {
		_gnome_vfs_mime_registered_mime_type_list_free(list);
	} finally {
		lock.unlock();
	}
}
public static final native int /*long*/ _gnome_vfs_mime_type_from_name(byte[] file);
public static final int /*long*/ gnome_vfs_mime_type_from_name(byte[] file) {
	lock.lock();
	try {
		return _gnome_vfs_mime_type_from_name(file);
	} finally {
		lock.unlock();
	}
}
public static final native int _gnome_vfs_url_show(int /*long*/ url);
public static final int gnome_vfs_url_show(int /*long*/ url) {
	lock.lock();
	try {
		return _gnome_vfs_url_show(url);
	} finally {
		lock.unlock();
	}
}
public static final native void memmove (GnomeVFSMimeApplication dest, int /*long*/ src, int /*long*/ count);
}
