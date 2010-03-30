/*******************************************************************************
 * Copyright (c) 2000, 2010 IBM Corporation and others. All rights reserved.
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

#include "swt.h"
#include "webkitgtk_stats.h"

#ifdef NATIVE_STATS

int WebKitGTK_nativeFunctionCount = 97;
int WebKitGTK_nativeFunctionCallCount[97];
char * WebKitGTK_nativeFunctionNames[] = {
	"JSClassDefinition_1sizeof",
	"_1JSClassCreate",
	"_1JSContextGetGlobalObject",
	"_1JSEvaluateScript",
	"_1JSObjectGetPrivate",
	"_1JSObjectGetProperty",
	"_1JSObjectGetPropertyAtIndex",
	"_1JSObjectMake",
	"_1JSObjectMakeArray",
	"_1JSObjectMakeFunctionWithCallback",
	"_1JSObjectSetProperty",
	"_1JSStringCreateWithUTF8CString",
	"_1JSStringGetLength",
	"_1JSStringGetMaximumUTF8CStringSize",
	"_1JSStringGetUTF8CString",
	"_1JSStringIsEqualToUTF8CString",
	"_1JSStringRelease",
	"_1JSValueGetType",
	"_1JSValueIsObjectOfClass",
	"_1JSValueMakeBoolean",
	"_1JSValueMakeNumber",
	"_1JSValueMakeString",
	"_1JSValueMakeUndefined",
	"_1JSValueToBoolean",
	"_1JSValueToNumber",
	"_1JSValueToStringCopy",
	"_1SOUP_1IS_1SESSION",
	"_1SoupCookie_1expires",
	"_1SoupMessage_1method",
	"_1SoupMessage_1request_1body",
	"_1SoupMessage_1request_1headers",
	"_1WEBKIT_1IS_1WEB_1FRAME",
	"_1soup_1auth_1authenticate",
	"_1soup_1auth_1get_1host",
	"_1soup_1auth_1get_1scheme_1name",
	"_1soup_1cookie_1jar_1add_1cookie",
	"_1soup_1cookie_1jar_1all_1cookies",
	"_1soup_1cookie_1jar_1delete_1cookie",
	"_1soup_1cookie_1jar_1get_1cookies",
	"_1soup_1cookie_1jar_1get_1type",
	"_1soup_1cookie_1parse",
	"_1soup_1message_1body_1append",
	"_1soup_1message_1body_1flatten",
	"_1soup_1message_1get_1uri",
	"_1soup_1message_1headers_1append",
	"_1soup_1session_1add_1feature_1by_1type",
	"_1soup_1session_1feature_1attach",
	"_1soup_1session_1feature_1detach",
	"_1soup_1session_1get_1feature",
	"_1soup_1uri_1free",
	"_1soup_1uri_1new",
	"_1soup_1uri_1to_1string",
	"_1webkit_1download_1cancel",
	"_1webkit_1download_1get_1current_1size",
	"_1webkit_1download_1get_1status",
	"_1webkit_1download_1get_1suggested_1filename",
	"_1webkit_1download_1get_1total_1size",
	"_1webkit_1download_1get_1uri",
	"_1webkit_1download_1set_1destination_1uri",
	"_1webkit_1get_1default_1session",
	"_1webkit_1major_1version",
	"_1webkit_1micro_1version",
	"_1webkit_1minor_1version",
	"_1webkit_1network_1request_1get_1message",
	"_1webkit_1network_1request_1get_1uri",
	"_1webkit_1network_1request_1new",
	"_1webkit_1soup_1auth_1dialog_1get_1type",
	"_1webkit_1web_1data_1source_1get_1data",
	"_1webkit_1web_1data_1source_1get_1encoding",
	"_1webkit_1web_1frame_1get_1data_1source",
	"_1webkit_1web_1frame_1get_1global_1context",
	"_1webkit_1web_1frame_1get_1load_1status",
	"_1webkit_1web_1frame_1get_1parent",
	"_1webkit_1web_1frame_1get_1title",
	"_1webkit_1web_1frame_1get_1uri",
	"_1webkit_1web_1frame_1get_1web_1view",
	"_1webkit_1web_1policy_1decision_1download",
	"_1webkit_1web_1policy_1decision_1ignore",
	"_1webkit_1web_1view_1can_1go_1back",
	"_1webkit_1web_1view_1can_1go_1forward",
	"_1webkit_1web_1view_1can_1show_1mime_1type",
	"_1webkit_1web_1view_1execute_1script",
	"_1webkit_1web_1view_1get_1load_1status",
	"_1webkit_1web_1view_1get_1main_1frame",
	"_1webkit_1web_1view_1get_1progress",
	"_1webkit_1web_1view_1get_1settings",
	"_1webkit_1web_1view_1get_1title",
	"_1webkit_1web_1view_1get_1uri",
	"_1webkit_1web_1view_1get_1window_1features",
	"_1webkit_1web_1view_1go_1back",
	"_1webkit_1web_1view_1go_1forward",
	"_1webkit_1web_1view_1load_1string",
	"_1webkit_1web_1view_1load_1uri",
	"_1webkit_1web_1view_1new",
	"_1webkit_1web_1view_1reload",
	"_1webkit_1web_1view_1stop_1loading",
	"memmove",
};

#define STATS_NATIVE(func) Java_org_eclipse_swt_tools_internal_NativeStats_##func

JNIEXPORT jint JNICALL STATS_NATIVE(WebKitGTK_1GetFunctionCount)
	(JNIEnv *env, jclass that)
{
	return WebKitGTK_nativeFunctionCount;
}

JNIEXPORT jstring JNICALL STATS_NATIVE(WebKitGTK_1GetFunctionName)
	(JNIEnv *env, jclass that, jint index)
{
	return (*env)->NewStringUTF(env, WebKitGTK_nativeFunctionNames[index]);
}

JNIEXPORT jint JNICALL STATS_NATIVE(WebKitGTK_1GetFunctionCallCount)
	(JNIEnv *env, jclass that, jint index)
{
	return WebKitGTK_nativeFunctionCallCount[index];
}

#endif
