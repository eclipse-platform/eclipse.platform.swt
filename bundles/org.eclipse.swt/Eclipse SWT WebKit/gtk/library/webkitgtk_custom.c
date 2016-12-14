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
#include <stdbool.h>
#include <gio/gio.h>	       // For things like GAsyncResult

#include "swt.h"		       // For types like jintLong etc..

#include "webkitgtk.h"         // For WebKitGTK_LOAD_FUNCTION macro and custom struct definitions.
#include "webkitgtk_custom.h"

#include "webkitgtk_stats.h"   // for WebKitGTK_NATIVE_ENTER.
#include "webkitgtk_structs.h" // for struct setters/getters


/**
 * Flag to indicate if function pointers are cached or not.
 * 0 - not cached, need caching.
 * 1 - cached.
 * -1 - exception occured.
 */
int fps_cached = 0;

/** Dynamic Function pointer declarations */
dyn_WebKitJavascriptResult  (*fp_webkit_web_view_run_javascript_finish) 	 (dyn_WebKitWebView, GAsyncResult*, GError**);
dyn_JSGlobalContextRef 		(*fp_webkit_javascript_result_get_global_context)(dyn_WebKitJavascriptResult);
void 			(*fp_webkit_web_view_run_javascript) 	(dyn_WebKitWebView, const gchar * /*script*/, GCancellable*, GAsyncReadyCallback, gpointer /*user_data*/);
dyn_JSValueRef  (*fp_webkit_javascript_result_get_value)(dyn_WebKitJavascriptResult);

#define INIT_WEBKIT_FP(function) \
		{ \
			WebKitGTK_LOAD_FUNCTION(fp, function); \
			if (fp == NULL) { \
				goto fail; \
			} \
			fp_##function = fp; \
		}

void initFPs() {
	INIT_WEBKIT_FP(webkit_web_view_run_javascript_finish);
	INIT_WEBKIT_FP(webkit_javascript_result_get_global_context);
	INIT_WEBKIT_FP(webkit_web_view_run_javascript);
	INIT_WEBKIT_FP(webkit_javascript_result_get_value);

	fps_cached = 1;
	return;

	fail:
	fps_cached = -1;
	g_critical("SWT webkitgtk_custom.c: Failed to load webkit function pointer(s)");
}

/*
 Calling JS script and getting return value example copied and adapted to be dynamic from:
 https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#webkit-web-view-run-javascript-finish
 Type conversion occurs on Java side.
*/
static void
web_view_javascript_finished_callback (GObject      *object,
                              GAsyncResult *result,
                              gpointer      user_data)
{
	dyn_WebKitJavascriptResult js_result;
	dyn_JSGlobalContextRef context;
	dyn_JSValueRef value;
    GError                 *error = NULL;

	SWTJSreturnVal * swtjsreturnvalSTRUCT = (SWTJSreturnVal*) user_data;
    js_result = fp_webkit_web_view_run_javascript_finish ((jintLong) object, result, &error);
    if (!js_result) {
    	WEBKIT_DBG_MSG ("DEBUG: webkitgtk_custom.c: webkitgtk_custom.c: Error running javascript(1): %s", error->message);
        swtjsreturnvalSTRUCT->returnType = 50; //SWT.java:ERROR_FAILED_EVALUATE
        gchar *err_msg;
        gsize err_msg_len = strlen(error->message);
        err_msg = malloc(err_msg_len + 1);
        strcpy(err_msg, error->message);
        swtjsreturnvalSTRUCT->errorMsg = (jintLong) err_msg;
        g_error_free (error);
    } else {
    	WEBKIT_DBG_MSG ("DEBUG: webkitgtk_custom.c: webkitgtk_custom.c: Javascript execution yielded a value");
		context = fp_webkit_javascript_result_get_global_context (js_result);
		value = fp_webkit_javascript_result_get_value (js_result);
		swtjsreturnvalSTRUCT->context = (jintLong) context;
		swtjsreturnvalSTRUCT->value = (jintLong) value;
		swtjsreturnvalSTRUCT->returnType = 1; // Return is a value to be converted.
		swtjsreturnvalSTRUCT->jsResultPointer = (jintLong) js_result;
		// Note: js_result is free up in WebKit.java:evalute().
    }

    // Note about exit points: this function must unlock the spinlock prior to returning.
    // As such there should not be a 'return' in this function above the unlocking code
	swtjsreturnvalSTRUCT->JsCallFinished = true;
}


/**
 * Convert the async function webkit_web_view_run_javascript(..) into a synchronous one
 * by spinning until the callback is completed.
 */
JNIEXPORT void Java_org_eclipse_swt_internal_webkit_WebKitGTK__1swtWebkitEvaluateJavascript
	(JNIEnv *env, jclass that, jintLong webkit_handle, jbyteArray javascriptStringBytes, jobject swtjsreturnvalOBJ)
{
	WebKitGTK_NATIVE_ENTER(env, that, _1swtWebkitEvaluateJavascript_FUNC); // For native stats tool.
	if (fps_cached == 0)
		initFPs();
	if (fps_cached == -1)
		return;

	jbyte *cString = (*env)->GetByteArrayElements(env, javascriptStringBytes, NULL);
	if (cString == NULL)
		return; // Out of Memory

	WEBKIT_DBG_MSG("DEBUG: C: Executing Script: %s", cString);

	SWTJSreturnVal swtjsreturnvalSTRUCT;
	swtjsreturnvalSTRUCT.JsCallFinished = false;
	swtjsreturnvalSTRUCT.returnType = 0;
	swtjsreturnvalSTRUCT.errorMsg = 0;
	swtjsreturnvalSTRUCT.context = 0;
	swtjsreturnvalSTRUCT.value = 0;

	fp_webkit_web_view_run_javascript(webkit_handle,(const gchar *) cString, NULL, web_view_javascript_finished_callback, &swtjsreturnvalSTRUCT);

	while (swtjsreturnvalSTRUCT.JsCallFinished == false) {
		g_main_context_iteration(0, false);
	}

	setSWTJSreturnValFields(env,swtjsreturnvalOBJ, &swtjsreturnvalSTRUCT);
	(*env)->ReleaseByteArrayElements(env, javascriptStringBytes, cString, 0);

	WebKitGTK_NATIVE_EXIT(env, that, _1swtWebkitEvaluateJavascript_FUNC);
}
