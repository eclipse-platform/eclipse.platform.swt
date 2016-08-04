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

#include "webkitgtk_custom.h"
#include "webkitgtk.h"  // For WebKitGTK_LOAD_FUNCTION macro
#include "swt.h"		// For types like jintLong etc..

#include <gio/gio.h>	// For things like GAsyncResult


// Note about webkit includes:
// Do not include things like '<webkit2/webkit2.h>' '<JavaScriptCore/JavaScript.h>' directly.
// All webkit functions must be loaded dynamically.
// If you compile on a newer Linux that contains Webkit2, trying to run the compiled binary on older
// OS's without webkit2 will lead to a crash even when they are running in 'Webkit1' mode.
// See Bug 430538.


/*
 Calling JS script and getting return value example copied and adapted from:
 https://webkitgtk.org/reference/webkit2gtk/stable/WebKitWebView.html#webkit-web-view-run-javascript-finish
*/
static void
web_view_javascript_finished_callback (GObject      *object,
                              GAsyncResult *result,
                              gpointer      user_data)
{

	//// TODO handling of return value to be implemented in Stage 2.
	//// All webkit functions will need to be called dynamically.
//    WebKitJavascriptResult *js_result;
//    JSValueRef              value;
//    JSGlobalContextRef      context;
//    GError                 *error = NULL;
//
//
//    js_result = webkit_web_view_run_javascript_finish (WEBKIT_WEB_VIEW (object), result, &error);
//    if (!js_result) {
//    	// Recoverable runtime error:
//        g_warning ("webkitgtk_custom.c: Error running javascript(1): %s", error->message);
//        g_error_free (error);
//    } else {
//		context = webkit_javascript_result_get_global_context (js_result);
//		value = webkit_javascript_result_get_value (js_result);
//
//
////		// TODO WEBKIT2 - handle various return value(s). Starter code below.
//		//		Idea: create struct with pointer to return value and lock;
//		//		then pass it along via user_data. NOTE *malloc it*
//
//	    // Supress compiler warnings till return types implemented.
//	    (void) value;
//	    (void) context;
//
////		if (JSValueIsString (context, value)) {
////			JSStringRef js_str_value;
////			gchar      *str_value;
////			gsize       str_length;
////
////			js_str_value = JSValueToStringCopy (context, value, NULL);
////			str_length = JSStringGetMaximumUTF8CStringSize (js_str_value);
////			str_value = (gchar *)g_malloc (str_length);
////			JSStringGetUTF8CString (js_str_value, str_value, str_length);
////			JSStringRelease (js_str_value);
////			g_print ("Script result: %s\n", str_value);
////			g_free (str_value);
////
////		// else if JSValueIsBoolean
////		// else if JSValueIsNumber
////		// else if JSValueIsObject
////		// else if JSValueIsNull
////		// else if JSValueIsArray
////		// else if JSValueIsUndefined (?)
////		// else if JSValueIsDate (?)
////		} else {
////			g_warning ("webkitgtk_custom.c: Error running javascript(2): unexpected return value");
////		}
//
//		webkit_javascript_result_unref (js_result);
//    }
//
//    // Note about exit points: this function must unlock the spinlock prior to returning.
//    // As such there should not be a 'return' in this function above the unlocking code
    gboolean *JsCallFinished = (gboolean* ) user_data;
    *JsCallFinished = TRUE;
}



/*
 * Type notes:
 * [Java]     ->  [native]
 * byte []    -> signed char *str
 * long /int/ -> long
*/
/**
 * Convert the async function webkit_web_view_run_javascript(..) into a synchronous one
 * by spinning until the callback is completed.
 */
long swt_webkit_web_view_run_javascript (long webkit_handle, signed char *script) {

	// TODO - WEBKIT2 port this will eventually be a struct that will hold the return value.
	gboolean * JsCallFinished = g_new(gboolean, 1); //allocate 1 unit of gboolean (not assigning '1' to it).
	*JsCallFinished = FALSE;

	// Macro usage copied and adjusted from webkitgtk.c's 'NO__1webkit_1web_1view_1run_1javascript' wrapper.
	WebKitGTK_LOAD_FUNCTION(fp, webkit_web_view_run_javascript)
	if (fp) {
		((void (CALLING_CONVENTION*)(jintLong, jbyte *, jintLong, jintLong, jintLong))fp)((jintLong) webkit_handle, script, (jintLong) 0, (jintLong) web_view_javascript_finished_callback, (jintLong) JsCallFinished);
	}

	// Spin till callback completes. Note this spin is needed for 'callback' event to be called, otherwise we have a hang.
	while (*JsCallFinished == FALSE) {
		g_main_context_iteration(0, FALSE);
	}
	g_free(JsCallFinished);

	return 1;
}
