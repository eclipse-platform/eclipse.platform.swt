	/*******************************************************************************
 * Copyright (c) 2017 Red Hat and others. All rights reserved.
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
 *     Red Hat - initial API and implementation
 *******************************************************************************/
#ifndef INC_webkit_extension_H
#define INC_webkit_extension_H

#include <string.h>

#include <glib.h>
#include <glib/gprintf.h>

#include <gio/gio.h>
#include <stdlib.h>

#include <unistd.h>
#include <stdio.h>

// These 2 are only for getpid();
#include <sys/types.h>
#include <unistd.h>

#include <webkit2/webkit-web-extension.h>

#include <JavaScriptCore/JavaScript.h>
#include <JavaScriptCore/JSContextRef.h>
#include <JavaScriptCore/JSObjectRef.h>
#include <JavaScriptCore/JSStringRef.h>

#define WEBKITGTK_EXTENSION_DBUS_NAME_PREFIX "org.eclipse.swt.webkitgtk_extension"
#define WEBKITGTK_EXTENSION_DBUS_PATH_PREFIX "/org/eclipse/swt/webkitgtk_extension/gdbus/"
#define WEBKITGTK_EXTENSION_DBUS_INTERFACE "org.eclipse.swt.webkitgtk_extension.gdbusInterface"

#define WEBKIT_MAIN_PROCESS_DBUS_NAME_PREFIX "org.eclipse.swt"
#define WEBKIT_MAIN_PROCESS_DBUS_PATH_PREFIX "/org/eclipse/swt/gdbus/"

static gchar* webkitgtk_extension_dbus_name;
static gchar* webkitgtk_extension_dbus_path;

static WebKitWebExtension *this_extension;

static GDBusNodeInfo *dbus_node;
static GDBusInterfaceInfo *dbus_interface;
static gchar* dbus_introspection_xml;
static gchar* dbus_introspection_xml_template =
"<node>"
  "<interface name='%s'>"

	"<method name='webkitgtk_extension_register_function'>"
	  "<arg type='t' name='page_id' direction='in'/>"
	  "<arg type='s' name='script' direction='in'/>"
	  "<arg type='s' name='url' direction='in'/>"
	  "<arg type='b' name='result' direction='out'/>"
	 "</method>"

	"<method name='webkitgtk_extension_deregister_function'>"
	  "<arg type='t' name='page_id' direction='in'/>"
	  "<arg type='s' name='script' direction='in'/>"
	  "<arg type='s' name='url' direction='in'/>"
	  "<arg type='b' name='result' direction='out'/>"
	"</method>"

  "</interface>"
"</node>";

#endif /*INC_webkit_extension_H*/
