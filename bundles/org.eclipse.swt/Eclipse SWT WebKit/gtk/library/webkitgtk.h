/*******************************************************************************
 * Copyright (c) 2009, 2012 IBM Corporation and others. All rights reserved.
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
  
#ifndef INC_webkitgtk_H
#define INC_webkitgtk_H

#include <dlfcn.h>
#include <string.h>
#include <stdlib.h>
#include <glib-object.h>

#define WebKitGTK_LOAD_FUNCTION(var, name) \
	static int initialized = 0; \
	static void *var = NULL; \
	if (!initialized) { \
		void* handle = 0; \
		char *gtk3 = getenv("SWT_GTK3"); \
		if (gtk3 == NULL || strcmp(gtk3, "1") == 0) { \
			char *webkit2 = getenv("SWT_WEBKIT2"); \
			if (webkit2 != NULL && strcmp(webkit2, "1") == 0) { \
				handle = dlopen("libwebkit2gtk-3.0.so.25", LOAD_FLAGS); /* webkit2 */ \
			} else { \
				handle = dlopen("libwebkitgtk-3.0.so.0", LOAD_FLAGS); /* webkitgtk >= 3.x lib */ \
			} \
		} else { \
			handle = dlopen("libwebkit-1.0.so.2", LOAD_FLAGS); /* webkitgtk 1.2.x lib */ \
			if (!handle) { \
				handle = dlopen("libwebkitgtk-1.0.so.0", LOAD_FLAGS); /* webkitgtk >= 1.4.x lib */ \
			} \
		} \
		if (handle) { \
			var = dlsym(handle, #name); \
		} \
		CHECK_DLERROR \
		initialized = 1; \
	}

typedef struct {
    int version;
    int attributes;
    const char* className;
    void* parentClass;
    const void* staticValues;
    const void* staticFunctions;
    void* initialize;
    void* finalize;
    void* hasProperty;
    void* getProperty;
    void* setProperty;
    void* deleteProperty;
    void* getPropertyNames;
    void* callAsFunction;
    void* callAsConstructor;
    void* hasInstance;
    void* convertToType;
} JSClassDefinition;

typedef struct {
	char* name;
	char* value;
	char* domain;
	char* path;
	void* expires;
	gboolean secure;
	gboolean http_only;
} SoupCookie;

typedef struct {
	GObject parent;
	const char* method;
	guint status_code;
	char* reason_phrase;
	void* request_body;
	void* request_headers;
	void* response_body;
	void* response_headers;
} SoupMessage;

#endif /* INC_webkitgtk_H */
