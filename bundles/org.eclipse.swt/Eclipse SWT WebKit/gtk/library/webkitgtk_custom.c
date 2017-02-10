/*******************************************************************************
* Copyright (c) 2016 Red Hat, Inc. All rights reserved.
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
*     Red Hat, Inc
*******************************************************************************/

/* Manually written code */
//#include <stdbool.h>		   // for standard true/false
//#include <gio/gio.h>	       // For things like GAsyncResult

#include "swt.h"		       // For types like jintLong etc..

#include "webkitgtk.h"         // For WebKitGTK_LOAD_FUNCTION macro and custom struct definitions.
#include "webkitgtk_custom.h"

#include "webkitgtk_stats.h"   // for WebKitGTK_NATIVE_ENTER.
#include "webkitgtk_structs.h" // for struct setters/getters


/**
 * This file provides functionality to execute custom functions dynamically in case this is needed.
 * Some examples are provided. See dynamic type translation examples in webkitgtk_custom.h
 * Careful not to include any webkit includes. All webkit calls should be dynamic.
 */


/**
 * Flag to indicate if function pointers are cached or not.
 * 0 - not cached, need caching.
 * 1 - cached.
 * -1 - exception occured.
 */
int fps_cached = 0;

/** Dynamic Function pointer declarations */
// Example of a dynamic function declaration.
// dyn_WebKitJavascriptResult  (*fp_webkit_web_view_run_javascript_finish) 	 (dyn_WebKitWebView, GAsyncResult*, GError**);
// // example call of this function would be: fp_webkit_web_view_run_javascript_finish ((jintLong) object, result, &error);

#define INIT_WEBKIT_FP(function) \
		{ \
			WebKitGTK_LOAD_FUNCTION(fp, function); \
			if (fp == NULL) { \
				goto fail; \
			} \
			fp_##function = fp; \
		}

void initFPs() {
	// Example of a dynamic function initilization.
//	INIT_WEBKIT_FP(webkit_web_view_run_javascript_finish);

	fps_cached = 1;
	return;

//	fail: // uncomment this if you make use of this function.
	fps_cached = -1;
	g_critical("SWT webkitgtk_custom.c: Failed to load webkit function pointer(s)");
}

// Example of a function that makes dynamic calls.
//JNIEXPORT void Java_org_eclipse_swt_internal_webkit_WebKitGTK__1swtWebkitEvaluateJavascript
//	(JNIEnv *env, jclass that, jintLong webkit_handle, jbyteArray javascriptStringBytes, jobject swtjsreturnvalOBJ)
//{
//	WebKitGTK_NATIVE_ENTER(env, that, _1swtWebkitEvaluateJavascript_FUNC); // For native stats tool.
//// In your custom function, you should first ensure function pointers are initiated:
// if (fps_cached == 0)
//		initFPs();
//	if (fps_cached == -1)
//		return;
//
//
//	WebKitGTK_NATIVE_EXIT(env, that, _1swtWebkitEvaluateJavascript_FUNC);
//}
