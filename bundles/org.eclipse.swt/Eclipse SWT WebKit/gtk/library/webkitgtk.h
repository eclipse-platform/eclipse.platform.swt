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

/* Manually written code */

#ifndef INC_webkitgtk_H
#define INC_webkitgtk_H

#include <dlfcn.h>
#include <string.h>
#include <stdlib.h>
#include <glib-object.h>

// For JNI bindings in webkitgtk.c to properly link to custom functions:
#include "webkitgtk_custom.h"

#include "swt.h"  // for jintlong used by custom struct below.

/**
 * Conceptually the macro does:
 * void *fp = dlsym(<libwebkit(3|4).so>, "name");  // Note, name is auto-wrapped into string literal.
 *
 * I.e, it declares and loads the function pointer from currently loaded webkitlib.
 * Preformance note: If webkit lib is already loaded, then it's not re-loaded.
 *   Instead, the lib pointer is simply acquired.
 */
#define WebKitGTK_LOAD_FUNCTION(var, name) \
	static int initialized = 0; \
	static void *var = NULL; \
	if (!initialized) { \
		void* handle = 0; \
		char *gtk3 = getenv("SWT_GTK3"); \
		if (gtk3 == NULL || strcmp(gtk3, "1") == 0) { \
			char *webkit2 = getenv("SWT_WEBKIT2"); \
			if (webkit2 != NULL && strcmp(webkit2, "1") == 0) { \
				handle = dlopen("libwebkit2gtk-4.0.so.37", LOAD_FLAGS); /* webkit2 */ \
			} else { \
				handle = dlopen("libwebkitgtk-3.0.so.0", LOAD_FLAGS); /* webkitgtk >= 3.x lib */ \
				if (!handle) { \
					handle = dlopen("libwebkit2gtk-4.0.so.37", LOAD_FLAGS); /* webkit2  as machine doesn't have webkitgtk 3.x*/ \
				} \
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




// Below 3 lines are useful to uncomment when developing, so that you get proper C/C++ indexing.
// To activate this macro for actual compilation, uncomment 'SWT_DEBUG' in make_linux.mak
//#ifndef WEBKIT_DEBUG
//#define WEBKIT_DEBUG
//#endif


// Debug print messages conditionally.
#ifdef WEBKIT_DEBUG
#define WEBKIT_DBG_MSG(args...) g_message(args)
#else
#define WEBKIT_DBG_MSG(args...)
#endif

#ifdef WEBKIT_DEBUG
// Note:
// In production code, do not include things like '<webkit2/webkit2.h>' '<JavaScriptCore/JavaScript.h>' directly as below.
// All webkit functions must be loaded dynamically.
// If you compile on a newer Linux that contains Webkit2, trying to run the compiled binary on older
// OS's without webkit2 will lead to a crash even when they are running in 'Webkit1' mode.
// See Bug 430538.
// However, during development and debugging it's useful to have those for prototyping and debugging.
#include <webkit2/webkit2.h>
#include <JavaScriptCore/JavaScript.h>
#else
/*
 * These custom structs override the native structs found in webkit/javascript/libsoup.
 * These are needed because invocation to those libraries is dynamic, and without redefining
 * these structs, the struct setting/getting functions in webkigtk_structs.c don't know how to access fields.
 *
 * NOTE:
 * - These are not one-to-one copies. Instead they strip out pointers to other structs and instead use void*.
 * - During webkit debugging/development, we use native includes for easier debugging.
 *     But this causes conflicting definitions during build with includes when debugging/developing,
 *     so these are only defined if not debugging.
 */
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
#endif /*WEBKIT_DEBUG*/


/**
 * Custom SWT Struct for passing return values from Javascript back to java.
 * Java: SWTJSReturnVal.java (class)
 * C:    SWTJSReturnVal (struct)
 * NOTE: If you need to modify fields, modify both the C part which is declared in webkitgtk.h and java part int SWTJSreturnVal.java
 * webkigtk_struct.c/h should be updated by auto-tools to add setters/getters.
 */
typedef struct {
	jintLong 	errorMsg;
	int 		returnType;
	jintLong	context;
	jintLong	value;
	jintLong 	jsResultPointer;

	jboolean 	JsCallFinished; // Custom field only used on C side. Not mirrored by Java.
} SWTJSreturnVal;

#endif /* INC_webkitgtk_H */
