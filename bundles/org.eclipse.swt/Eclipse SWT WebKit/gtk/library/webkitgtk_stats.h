/*******************************************************************************
 * Copyright (c) 2009, 2011 IBM Corporation and others. All rights reserved.
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

#ifdef NATIVE_STATS
extern int WebKitGTK_nativeFunctionCount;
extern int WebKitGTK_nativeFunctionCallCount[];
extern char* WebKitGTK_nativeFunctionNames[];
#define WebKitGTK_NATIVE_ENTER(env, that, func) WebKitGTK_nativeFunctionCallCount[func]++;
#define WebKitGTK_NATIVE_EXIT(env, that, func) 
#else
#ifndef WebKitGTK_NATIVE_ENTER
#define WebKitGTK_NATIVE_ENTER(env, that, func) 
#endif
#ifndef WebKitGTK_NATIVE_EXIT
#define WebKitGTK_NATIVE_EXIT(env, that, func) 
#endif
#endif

typedef enum {
	JSClassDefinition_1sizeof_FUNC,
	_1JSClassCreate_FUNC,
	_1JSContextGetGlobalObject_FUNC,
	_1JSEvaluateScript_FUNC,
	_1JSObjectGetPrivate_FUNC,
	_1JSObjectGetProperty_FUNC,
	_1JSObjectGetPropertyAtIndex_FUNC,
	_1JSObjectMake_FUNC,
	_1JSObjectMakeArray_FUNC,
	_1JSObjectMakeFunctionWithCallback_FUNC,
	_1JSObjectSetProperty_FUNC,
	_1JSStringCreateWithUTF8CString_FUNC,
	_1JSStringGetLength_FUNC,
	_1JSStringGetMaximumUTF8CStringSize_FUNC,
	_1JSStringGetUTF8CString_FUNC,
	_1JSStringIsEqualToUTF8CString_FUNC,
	_1JSStringRelease_FUNC,
	_1JSValueGetType_FUNC,
	_1JSValueIsObjectOfClass_FUNC,
	_1JSValueMakeBoolean_FUNC,
	_1JSValueMakeNumber_FUNC,
	_1JSValueMakeString_FUNC,
	_1JSValueMakeUndefined_FUNC,
	_1JSValueToBoolean_FUNC,
	_1JSValueToNumber_FUNC,
	_1JSValueToStringCopy_FUNC,
	_1SoupCookie_1expires_FUNC,
	_1SoupMessage_1method_FUNC,
	_1SoupMessage_1request_1body_FUNC,
	_1SoupMessage_1request_1headers_FUNC,
	_1soup_1auth_1authenticate_FUNC,
	_1soup_1auth_1get_1host_FUNC,
	_1soup_1auth_1get_1scheme_1name_FUNC,
	_1soup_1cookie_1jar_1add_1cookie_FUNC,
	_1soup_1cookie_1jar_1all_1cookies_FUNC,
	_1soup_1cookie_1jar_1delete_1cookie_FUNC,
	_1soup_1cookie_1jar_1get_1cookies_FUNC,
	_1soup_1cookie_1jar_1get_1type_FUNC,
	_1soup_1cookie_1parse_FUNC,
	_1soup_1message_1body_1append_FUNC,
	_1soup_1message_1body_1flatten_FUNC,
	_1soup_1message_1get_1uri_FUNC,
	_1soup_1message_1headers_1append_FUNC,
	_1soup_1session_1add_1feature_1by_1type_FUNC,
	_1soup_1session_1feature_1attach_FUNC,
	_1soup_1session_1feature_1detach_FUNC,
	_1soup_1session_1get_1feature_FUNC,
	_1soup_1session_1get_1type_FUNC,
	_1soup_1uri_1free_FUNC,
	_1soup_1uri_1new_FUNC,
	_1soup_1uri_1to_1string_FUNC,
	_1webkit_1download_1cancel_FUNC,
	_1webkit_1download_1get_1current_1size_FUNC,
	_1webkit_1download_1get_1status_FUNC,
	_1webkit_1download_1get_1suggested_1filename_FUNC,
	_1webkit_1download_1get_1total_1size_FUNC,
	_1webkit_1download_1get_1uri_FUNC,
	_1webkit_1download_1set_1destination_1uri_FUNC,
	_1webkit_1get_1default_1session_FUNC,
	_1webkit_1major_1version_FUNC,
	_1webkit_1micro_1version_FUNC,
	_1webkit_1minor_1version_FUNC,
	_1webkit_1network_1request_1get_1message_FUNC,
	_1webkit_1network_1request_1get_1uri_FUNC,
	_1webkit_1network_1request_1new_FUNC,
	_1webkit_1soup_1auth_1dialog_1get_1type_FUNC,
	_1webkit_1web_1data_1source_1get_1data_FUNC,
	_1webkit_1web_1data_1source_1get_1encoding_FUNC,
	_1webkit_1web_1frame_1get_1data_1source_FUNC,
	_1webkit_1web_1frame_1get_1global_1context_FUNC,
	_1webkit_1web_1frame_1get_1load_1status_FUNC,
	_1webkit_1web_1frame_1get_1parent_FUNC,
	_1webkit_1web_1frame_1get_1title_FUNC,
	_1webkit_1web_1frame_1get_1type_FUNC,
	_1webkit_1web_1frame_1get_1uri_FUNC,
	_1webkit_1web_1frame_1get_1web_1view_FUNC,
	_1webkit_1web_1policy_1decision_1download_FUNC,
	_1webkit_1web_1policy_1decision_1ignore_FUNC,
	_1webkit_1web_1view_1can_1go_1back_FUNC,
	_1webkit_1web_1view_1can_1go_1forward_FUNC,
	_1webkit_1web_1view_1can_1show_1mime_1type_FUNC,
	_1webkit_1web_1view_1execute_1script_FUNC,
	_1webkit_1web_1view_1get_1load_1status_FUNC,
	_1webkit_1web_1view_1get_1main_1frame_FUNC,
	_1webkit_1web_1view_1get_1progress_FUNC,
	_1webkit_1web_1view_1get_1settings_FUNC,
	_1webkit_1web_1view_1get_1title_FUNC,
	_1webkit_1web_1view_1get_1uri_FUNC,
	_1webkit_1web_1view_1get_1window_1features_FUNC,
	_1webkit_1web_1view_1go_1back_FUNC,
	_1webkit_1web_1view_1go_1forward_FUNC,
	_1webkit_1web_1view_1load_1string_FUNC,
	_1webkit_1web_1view_1load_1uri_FUNC,
	_1webkit_1web_1view_1new_FUNC,
	_1webkit_1web_1view_1reload_FUNC,
	_1webkit_1web_1view_1stop_1loading_FUNC,
	memmove_FUNC,
} WebKitGTK_FUNCS;
