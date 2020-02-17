#include "webkitgtk_extension.h"

/*
 * To debug this extension:
 *  -ensure this is built with debug flags (look for '-g*' in make_linux, or 'SWT_LIB_DEBUG' macro)
 *  -connect to the WebKitWebProcess with the PID of this extension. It can be found as follows:
		g_print("Extension PID: %d\n", getpid());
 */

// Client address of the main process GDBusServer -- the extension connects to this
const gchar *swt_main_proc_client_address = NULL;

// Client address of the extension's GDBusServer -- SWT main process connects to this
const gchar *extension_server_address = NULL;

// See: WebKitGTK.java's 'TYPE NOTES'
guchar SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY = 101;
guchar SWT_DBUS_MAGIC_NUMBER_NULL = 48;

// This struct represents a BrowserFunction
typedef struct {
    guint64 page_id; // page ID
    gchar *function; // JS function
    gchar *url; // URL the function belongs to
} BrowserFunction;

// The list of BrowserFunctions registered to this extension
GSList *function_list = NULL;

// GDBusConnection to the SWT main process
GDBusConnection *connection_to_main_proc = NULL;

// This extension's GDBusServer and related resources
GDBusServer *server = NULL;
GDBusAuthObserver *auth_observer = NULL;
gchar *guid = NULL;

// GDBusConnection from SWT main process
GDBusConnection *connection_from_main_proc = NULL;

/**
 * Caller should free the returned GVariant
 */
GVariant *call_main_proc_sync(char * method_name, GVariant *parameters) {
	GError *error = NULL; // Some functions return errors through params
	GVariant *result;     // The value result from a call

	// Send a message
	result = g_dbus_connection_call_sync(connection_to_main_proc, WEBKIT_MAIN_PROCESS_DBUS_NAME,
			WEBKIT_MAIN_PROCESS_OBJECT_PATH, WEBKIT_MAIN_PROCESS_INTERFACE_NAME, method_name, parameters,
			NULL, G_DBUS_CALL_FLAGS_NONE, -1, NULL, &error);
	// Error checking.
	if (result == NULL) {
		if (error != NULL) {
			g_error("call_main_proc_sync failed because '%s.'\n", error->message);
		} else {
			g_error("call_main_proc_sync failed for an unknown reason.\n");
		}
		return NULL;
	}

	return result;
}

// +--------------------------------------------------+
// | JavaScriptCore to/from conversion GVariant logic |
// +--------------------------------------------------+

/** Return true if the given JSValueRef is one we can push over gdbus. False otherwise.
 *  We support basic types, nulls and arrays of basic types.*/
gboolean is_js_valid(JSContextRef context, JSValueRef value) {
	JSType type = JSValueGetType(context, value);
	if (type == kJSTypeBoolean
			|| type == kJSTypeNumber
			|| type == kJSTypeString
			|| type == kJSTypeNull
			|| type == kJSTypeUndefined) {
		return true;
	}
	if (type == kJSTypeObject && JSValueIsArray(context, value)) {
		JSStringRef propertyName = JSStringCreateWithUTF8CString("length");
		JSObjectRef object = JSValueToObject(context, value, NULL);
		JSValueRef valuePtr = JSObjectGetProperty(context, object, propertyName, NULL);
		JSStringRelease(propertyName);
		int length = (int) JSValueToNumber(context, valuePtr, NULL);
		int i;
		for (i = 0; i < length; i++) {
			const JSValueRef child = JSObjectGetPropertyAtIndex(context, object, i, NULL);
			if (!is_js_valid(context, child)) {
				return false;
			}
		}
		return true;
	}
	return false;
}

/*
 * Developer note:
 * JavaScriptCore defines a "Number" to be a double in general. It doesn't seem to be using "Int".
 */
static GVariant * convert_js_to_gvariant (JSContextRef context, JSValueRef value){
	g_assert(context != NULL);
	g_assert(value != NULL);
	JSType type = JSValueGetType(context, value);

	if (type == kJSTypeBoolean) {
		gboolean result = JSValueToNumber(context, value, NULL) != 0;
		return g_variant_new_boolean(result);
	}

	if (type == kJSTypeNumber) {
		double result = JSValueToNumber(context, value, NULL);
		return g_variant_new_double(result);
	}

	if (type == kJSTypeString) {
		JSStringRef stringRef = JSValueToStringCopy(context, value, NULL);
		size_t length = JSStringGetMaximumUTF8CStringSize(stringRef);
		char* string = (char*) malloc(length);
		JSStringGetUTF8CString(stringRef, string, length);
		GVariant *variant = g_variant_new_string(string);
		free(string);
		return variant;
	}

	if (type == kJSTypeNull || type == kJSTypeUndefined) {
		return g_variant_new_byte(SWT_DBUS_MAGIC_NUMBER_NULL);
	}

	if (type == kJSTypeObject) {
		JSStringRef propertyName = JSStringCreateWithUTF8CString("length");
		JSObjectRef object = JSValueToObject(context, value, NULL);
		JSValueRef valuePtr = JSObjectGetProperty(context, object, propertyName, NULL);
		JSStringRelease(propertyName);

		if (JSValueGetType(context, valuePtr) == kJSTypeNumber) {
			int length = (int) JSValueToNumber(context, valuePtr, NULL);

			if (length == 0) {
				return g_variant_new_byte(SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY);
			}
			GVariant **children = g_new(GVariant *, length);
			int i = 0;
			for (i = 0; i < length; i++) {
				const JSValueRef child = JSObjectGetPropertyAtIndex(context, object, i, NULL);
				children[i] = convert_js_to_gvariant(context, child);
			}
			GVariant* variant = g_variant_new_tuple(children, length);
			g_free(children);
			return variant;
		}
	}

	// Get type value string
	JSStringRef valueIString = JSValueToStringCopy(context, value, NULL);
	size_t valueUTF8Size = JSStringGetMaximumUTF8CStringSize(valueIString);
	char* valueUTF8 = (char*) malloc(valueUTF8Size);
	JSStringGetUTF8CString(valueIString, valueUTF8, valueUTF8Size);

	g_warning("Unhandled type %d value: %s \n", type, valueUTF8);
	free(valueUTF8);
	JSStringRelease(valueIString);

	return NULL;
}


static JSValueRef convert_gvariant_to_js (JSContextRef context, GVariant * value){
	g_assert(context != NULL);
	g_assert(value != NULL);

	if (g_variant_is_of_type(value, G_VARIANT_TYPE_BYTE)) {  // see: WebKitGTK.java 'TYPE NOTES'
		guchar magic_number = g_variant_get_byte(value);
		if (magic_number == SWT_DBUS_MAGIC_NUMBER_NULL) {
			// 'JSValueMakeUndefined' is used as oppose to 'JSValueMakeNull' (from what I gather) for legacy reasons.
			// I.e webkit1 used it, so we shall use it in webkit2 also.
			return JSValueMakeUndefined(context);
		} else if (magic_number == SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY) {
				return JSObjectMakeArray(context, 0, NULL, NULL); // The empty array with no children.
		} else {
			g_error("Java sent an unknown magic number: '%d' , this should never happen. \n", magic_number);
		}
	}

	if (g_variant_is_of_type(value, G_VARIANT_TYPE_BOOLEAN)) {
		return JSValueMakeBoolean(context, g_variant_get_boolean(value));
	}

	if (g_variant_is_of_type(value, G_VARIANT_TYPE_DOUBLE)) {
		return JSValueMakeNumber(context, g_variant_get_double(value));
	}

	if (g_variant_is_of_type(value, G_VARIANT_TYPE_STRING)) {
		JSStringRef stringRef = JSStringCreateWithUTF8CString(g_variant_get_string(value, NULL));
		JSValueRef result = JSValueMakeString(context, stringRef);
		JSStringRelease(stringRef);
		return result;
	}

	if (g_variant_is_of_type(value, G_VARIANT_TYPE_TUPLE)) {
		gsize length = g_variant_n_children(value);
		JSValueRef *children = g_new(JSValueRef, length);

		gsize i = 0;
		for (i = 0; i < length; i++) {
			children[i] = convert_gvariant_to_js(context, g_variant_get_child_value(value, i));
		}
		JSValueRef result = JSObjectMakeArray(context, length, children, NULL);
		g_free(children);
		return result;
	}
	g_error("Unhandled type %s \n", g_variant_get_type_string(value));
	return NULL;
}

// +--------------------+---------------------------------------------------------
// | WebExtension Logic |
// +--------------------+

// Reached by calling "webkit2callJava();" in javascript console.
// Some basic c function to be exposed to the javascript environment
static JSValueRef webkit2callJava (JSContextRef context,
                               JSObjectRef function,
                               JSObjectRef thisObject,
                               size_t argumentCount,
                               const JSValueRef arguments[], // [String webview, double index, String Token, Object[] args]
                               JSValueRef *exception) {
	g_assert (argumentCount == 4);
	GVariant *g_var_params;     // The parameters to a function call

	// Need to ensure user arguments won't break gdbus.
	if (!is_js_valid(context, arguments[3])) {
		g_warning("Arguments contain an invalid type (object). Only Number,Boolean,null,String and (mixed) arrays of basic types are supported");
		return 0;
	}

	g_var_params = g_variant_new ("(@s@d@s@*)",   // pointer to String, pointer to double, pointer to string, pointer to any type.
			convert_js_to_gvariant(context, arguments[0]), // String webView
			convert_js_to_gvariant(context, arguments[1]), // int index
			convert_js_to_gvariant(context, arguments[2]), // String Token
			convert_js_to_gvariant(context, arguments[3])  // js args
			);

	GVariant *g_var_result = call_main_proc_sync("webkit2callJava", g_var_params);
	if (g_var_result == NULL) {
		g_error("Java call returned NULL. This should never happpen\n");
		return 0;
	}

	// gdbus dynamic call always returns an array(tuple) with return types.
	// In our case, we return a single type or an array.
	// E.g  java:int -> gdbus:(i)   (array containing one int)
	// E.g java [int,str] -> gdbus:((is))   (array with array of (int+str).
	// So we always extract the first child, convert and pass to js.
	JSValueRef retVal = 0;
	if (g_variant_is_of_type(g_var_result, G_VARIANT_TYPE_TUPLE)) {
		if (g_variant_n_children(g_var_result) != 1) {
			g_error("Should only receive a single item in the tuple, but length is: %ud\n", (unsigned int) g_variant_n_children(g_var_result));
		}
		retVal = convert_gvariant_to_js(context, g_variant_get_child_value(g_var_result, 0));
	} else {
		g_error("Unsupported return type. Should be an array, but received a single type.\n");
	}

	g_variant_unref(g_var_result);
	return retVal;
}

static void web_page_created_callback(WebKitWebExtension *extension, WebKitWebPage *web_page, gpointer user_data) {
	// Observation. This seems to be called only once.
}

/**
 * Returns the main frame of the WebPage with the given ID
 */
static WebKitFrame *webkitgtk_extension_get_main_frame (const guint64 id) {
	WebKitWebPage *web_page = webkit_web_extension_get_page (this_extension, id);
	return webkit_web_page_get_main_frame (web_page);
}

/*
 * Execute the Javascript for the given page and URL.
 */
static gboolean webkitgtk_extension_execute_script (const guint64 page_id, gchar* script, gchar* url) {
	WebKitFrame *main_frame = webkitgtk_extension_get_main_frame (page_id);

	JSStringRef url_string = JSStringCreateWithUTF8CString (url);
	JSStringRef script_string = JSStringCreateWithUTF8CString (script);

	JSGlobalContextRef context = webkit_frame_get_javascript_global_context (main_frame);

	JSValueRef exception;
	JSValueRef result = JSEvaluateScript(context, script_string, NULL, url_string, 0,  &exception);
	if (!result) {
		JSStringRef exceptionIString = JSValueToStringCopy(context, exception, NULL);
		size_t exceptionUTF8Size = JSStringGetMaximumUTF8CStringSize(exceptionIString);
		char* exceptionUTF8 = (char*)malloc(exceptionUTF8Size);
		JSStringGetUTF8CString(exceptionIString, exceptionUTF8, exceptionUTF8Size);
		g_error("Failed to execute script exception: %s\n", exceptionUTF8);
		free(exceptionUTF8);
		JSStringRelease(exceptionIString);
	}

	JSStringRelease (url_string);
	JSStringRelease (script_string);

	return result != NULL;
}

void execute_browser_functions(gconstpointer item, gpointer page) {
	BrowserFunction *function = (BrowserFunction *) item;
	if (function != NULL && function->page_id == GPOINTER_TO_UINT(page)) {
		webkitgtk_extension_execute_script(function->page_id, function->function, function->url);
	}
	return;
}

gint find_browser_function (gconstpointer item, gconstpointer target) {
	BrowserFunction *element = (BrowserFunction *) item;
	BrowserFunction *remove = (BrowserFunction *) target;
	if (element->page_id == remove->page_id && g_strcmp0(element->function, remove->function) == 0 &&
			g_strcmp0(element->url, remove->url) == 0) {
		return 0;
	}
	return 1;
}

void add_browser_function(guint64 page_id, const gchar *function, const gchar *url) {
	BrowserFunction *func = g_slice_new0(BrowserFunction);
	func->page_id = page_id;
	func->function = g_strdup(function);
	func->url = g_strdup(url);
	function_list = g_slist_append(function_list, func);
}

void remove_browser_function(guint64 page_id, const gchar *function, const gchar *url) {
	BrowserFunction *func = g_slice_new0(BrowserFunction);
	func->page_id = page_id;
	func->function = g_strdup(function);
	func->url = g_strdup(url);
	GSList *to_remove = g_slist_find_custom(function_list, func, find_browser_function);
	if (to_remove != NULL) {
		BrowserFunction *delete_func = to_remove->data;
		g_free(delete_func->function);
		g_free(delete_func->url);
		function_list = g_slist_delete_link(function_list, to_remove);
	}
	g_free(func->function);
	g_free(func->url);
	g_slice_free(BrowserFunction, func);
}

void unpack_browser_function_array(GVariant *array) {
	GVariantIter iter;
	GVariant *child;

	g_variant_iter_init (&iter, array);
	while ((child = g_variant_iter_next_value (&iter))) {
	    gsize length = (int)g_variant_n_children (child);
	    if (length > 3) {
	    	// If the length is longer than three, something went wrong and this tuple should be skipped
	    	g_warning("There was an error unpacking the GVariant tuple for a BrowserFunction in the web extension.\n");
	    	continue;
	    }
	    guint64 page = g_variant_get_uint64(g_variant_get_child_value(child, 0));
	    if (page == -1UL) {
	    	// Empty or malformed BrowserFunction, skip this one
	    	continue;
	    } else {
	    	const gchar *function = g_variant_get_string(g_variant_get_child_value(child, 1), NULL);
	    	const gchar *url = g_variant_get_string(g_variant_get_child_value(child, 2), NULL);
	    	if (function != NULL && url != NULL) {
	    		add_browser_function(page, function, url);
	    	} else {
	    		g_warning("There was an error unpacking the function string or URL.\n");
	    	}
	    }
	 	g_variant_unref (child);
	}
}

/*
 * Every time a webpage is loaded, we should re-register the 'webkit2callJava' function.
 * Additionally, we re-register all BrowserFunctions that are stored in the function_list
 * GSList.
 */
static void window_object_cleared_callback (WebKitScriptWorld *world, WebKitWebPage *web_page,
                                WebKitFrame       *frame,
                                gpointer           user_data) {
	// Observation: This is called every time a webpage is loaded.
	JSGlobalContextRef jsContext;
    JSObjectRef        globalObject;
    JSValueRef exception = 0;

    jsContext = webkit_frame_get_javascript_context_for_script_world (frame, world);
    globalObject = JSContextGetGlobalObject (jsContext);

    JSStringRef function_name = JSStringCreateWithUTF8CString("webkit2callJava"); // Func reference by javascript
    JSObjectRef jsFunction = JSObjectMakeFunctionWithCallback(jsContext, function_name, webkit2callJava); // C reference to func
    JSObjectSetProperty(jsContext, globalObject, function_name, jsFunction,
    		kJSPropertyAttributeDontDelete | kJSPropertyAttributeReadOnly, &exception);

    if (exception) {
    	g_print("OJSObjectSetProperty exception occurred");
    }

    /*
     * Iterate over the list of BrowserFunctions and execute each one of them for the current page.
     * This ensures that BrowserFunctions are not lost on page reloads. See bug 536141.
     */
    if (function_list != NULL) {
    	guint64 page_id = webkit_web_page_get_id (web_page);
    	if (page_id != -1UL) {
    		g_slist_foreach(function_list, (GFunc)execute_browser_functions, GUINT_TO_POINTER(page_id));
    	} else {
    		g_warning("There was an error fetching the page ID in the object_cleared callback.\n");
    	}
    }
}

static void
webkitgtk_extension_handle_method_call (GDBusConnection *connection, const gchar *sender,
                    const gchar           *object_path,
                    const gchar           *interface_name,
                    const gchar           *method_name,
                    GVariant              *parameters,
                    GDBusMethodInvocation *invocation,
                    gpointer               user_data) {
	gboolean result = FALSE;
	const gchar *script;
	const gchar *url;
	guint64 page_id;
	// Check method names
	if (g_strcmp0(method_name, "webkitgtk_extension_register_function") == 0) {
		g_variant_get(parameters, "(t&s&s)", &page_id, &script, &url);
		if (page_id != -1UL) {
			result = TRUE;
			// Return before processing the linked list, to prevent DBus from hanging
			g_dbus_method_invocation_return_value(invocation, g_variant_new("(b)", result));
			add_browser_function(page_id, script, url);
			return;
		}
		g_dbus_method_invocation_return_value(invocation, g_variant_new("(b)", result));
		return;
	}
	if (g_strcmp0(method_name, "webkitgtk_extension_deregister_function") == 0) {
		g_variant_get(parameters, "(t&s&s)", &page_id, &script, &url);
		if (page_id != -1UL) {
			result = TRUE;
			// Return before processing the linked list, to prevent DBus from hanging
			g_dbus_method_invocation_return_value(invocation, g_variant_new("(b)", result));
			remove_browser_function(page_id, script, url);
			return;
		}
		g_dbus_method_invocation_return_value(invocation, g_variant_new("(b)", result));
		return;
	}
	g_error ("Unknown method %s\n", method_name);
}

static const GDBusInterfaceVTable interface_vtable = {.method_call = webkitgtk_extension_handle_method_call};

static void connection_closed_cb (GDBusConnection *connection, gboolean remote_peer_vanished, GError *error,
		gpointer user_data) {
	/*
	 * If this connection is closed we can shut the server down. NOTE: all connections are freed in the main
	 * SWT process, including connections created here in this extension. This is done for the sake of
	 * consistency to avoid double frees.
	 */
	if (connection == connection_from_main_proc) {
		// Free server and its objects
		g_dbus_server_stop(server);
		g_object_unref(auth_observer);
		g_object_unref(server);
		g_free(guid);
	}
}

static gboolean new_connection_cb (GDBusServer *server, GDBusConnection *connection, gpointer user_data) {
	// Create introspection XML
	dbus_introspection_xml = g_new (gchar, strlen(dbus_introspection_xml_template) +
			strlen(WEBKITGTK_EXTENSION_INTERFACE_NAME) + 1);
	g_sprintf (dbus_introspection_xml, dbus_introspection_xml_template, WEBKITGTK_EXTENSION_INTERFACE_NAME);

	// Create DBus node
	dbus_node = g_dbus_node_info_new_for_xml (dbus_introspection_xml, NULL);
	g_assert (dbus_node != NULL);

	// Register node on the connection that was just created
	dbus_interface = g_dbus_node_info_lookup_interface(dbus_node, WEBKITGTK_EXTENSION_INTERFACE_NAME);
	guint registration_id = g_dbus_connection_register_object(connection, WEBKITGTK_EXTENSION_OBJECT_PATH,
		      dbus_interface,
		      &interface_vtable, NULL, /* user_data */
		      NULL, /* user_data_free_func */
		      NULL); /* GError** */
	g_assert(registration_id > 0);

	// This connection will be the one from the main SWT process
	connection_from_main_proc = g_object_ref(connection);

	// Listen to the "close" signal on this connection so we can free resources in the extension when
	// the time comes.
	g_signal_connect(connection_from_main_proc, "closed", G_CALLBACK (connection_closed_cb), NULL);

	return 1;
}

static gboolean extension_authorize_peer (GDBusAuthObserver *observer, GIOStream *stream, GCredentials *credentials,
		gpointer user_data) {
	g_autoptr (GError) error = NULL;
	gboolean authorized = FALSE;
	if (credentials != NULL) {
		  GCredentials *own_credentials;
		  own_credentials = g_credentials_new ();
		  if (g_credentials_is_same_user (credentials, own_credentials, &error)) {
			  authorized = TRUE;
	      }
		  g_object_unref (own_credentials);
	}
	if (error) {
		g_warning ("Error authenticating client connection: %s", error->message);
	}
	return authorized;
}

// Returns a valid GDBusServer address -- this will be the address used to connect to the extension's
// GDBusServer
gchar *construct_server_address () {
	g_autoptr (GError) error = NULL;
	gchar *partial_name = g_strconcat ("SWT-WebExtensionGDBusServer-", g_get_user_name (), "-XXXXXX", NULL);
	gchar *temp_path = g_dir_make_tmp (partial_name, &error);
	if (error) {
		g_error("There was an error creating the server address: %s\n", error->message);
	}
	g_free (partial_name);
	return g_strdup_printf ("unix:tmpdir=%s", temp_path);
}

// Creates the GDBusServer for this web extension
static void create_server () {
	g_autoptr (GError) error = NULL;
	extension_server_address = construct_server_address();
	auth_observer = g_dbus_auth_observer_new();

	guid = g_dbus_generate_guid();
	server = g_dbus_server_new_sync(extension_server_address, G_DBUS_SERVER_FLAGS_NONE, guid,
			auth_observer, NULL, &error);

	if (error) {
		g_error ("Failed to create server: %s", error->message);
	}

	if (server) {
		g_signal_connect(server, "new-connection", G_CALLBACK(new_connection_cb), NULL);
		g_dbus_server_start(server);
		g_signal_connect (auth_observer, "authorize-authenticated-peer", G_CALLBACK(extension_authorize_peer), NULL);
	}
}

// Identifies this web extension to the main SWT process by sending its
// server address over DBus.
static void identify_extension_to_main_proc () {
	const gchar *client_address_for_extension_server = g_dbus_server_get_client_address(server);
	GVariant *result = call_main_proc_sync("webkitWebExtensionIdentifier", g_variant_new ("(s)",
			client_address_for_extension_server));

	// Process and register any pending BrowserFunctions from the main SWT process
	if (g_variant_is_of_type(result, G_VARIANT_TYPE_TUPLE)) {
		  unpack_browser_function_array(g_variant_get_child_value(result, 0));
	} else {
		g_warning("webkitWebExtensionIdentifier return value from SWT was an unexpected type""(not a tuple).\n");
	}
}

// Creates a GDBusConnection to the main SWT process
static void connect_to_main_proc (GVariant *user_data) {
	swt_main_proc_client_address = g_variant_get_string(user_data, NULL);

	g_autoptr (GError) error = NULL;
	connection_to_main_proc = g_dbus_connection_new_for_address_sync (swt_main_proc_client_address,
			G_DBUS_CONNECTION_FLAGS_AUTHENTICATION_CLIENT, NULL, NULL, &error);

	if (error) {
		g_error ("Failed to create connection: %s", error->message);
	}

	if (connection_to_main_proc && g_dbus_connection_is_closed(connection_to_main_proc)) {
			g_error ("Failed to created connection: connection is closed");
	}
}

G_MODULE_EXPORT void webkit_web_extension_initialize_with_user_data(WebKitWebExtension *extension, GVariant *user_data) {
	this_extension = extension;

	// Connect to the main SWT process
	connect_to_main_proc(user_data);

	// Create this extension's GDBusServer
	create_server();

	// Identify this extension's server address to the main process
	identify_extension_to_main_proc();

	// WebKit callbacks
    g_signal_connect(extension, "page-created",  G_CALLBACK(web_page_created_callback), NULL);
    g_signal_connect (webkit_script_world_get_default (), "window-object-cleared", G_CALLBACK (window_object_cleared_callback), NULL);
}
