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

package org.eclipse.swt.browser;

import java.util.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * Logic for Webkit to interact with it's Webkit extension via GDBus.
 *
 * While this class supports quite a bit of GDBus and gvariant support, it is by no means a complete
 * implementation and it's tailored to support Java to Javascript conversion. (E.g all Numbers are converted to Double).
 * If this is ever to be used outside of Webkit, then care must be taken to deal with
 * cases that are not currently implemented/used. See: WebKitGTK.java 'TYPE NOTES'
 *
 * For hygiene purposes, GVariant types should not be leaving this class. Convert on the way in/out.
 *
 * @category gdbus
 */
class WebkitGDBus {
	private static String DBUS_SERVICE_NAME;
	private static final String DBUS_OBJECT_PATH = "/org/eclipse/swt/gdbus";
	private static final String INTERFACE_NAME = "org.eclipse.swt.gdbusInterface";
	private static final String EXTENSION_INTERFACE_NAME = "org.eclipse.swt.webkitgtk_extension.gdbusInterface";
	private static String EXTENSION_DBUS_NAME;
	private static String EXTENSION_DBUS_PATH;

	/** Accepted methods over gdbus */
	private static final String webkit2callJava = WebKit.Webkit2Extension.getJavaScriptFunctionName();
	private static final String webkitWebExtensionIdentifier = WebKit.Webkit2Extension.getWebExtensionIdentifer();

	/** Proxy connection to the web extension.*/
	static long proxy;
	/** A field that is set to true if the proxy connection has been established, false otherwise */
	static boolean proxyToExtension;
	/** Set to true if there are <code>BrowserFunction</code> objects waiting to be registered with the web extension.*/
	static boolean functionsPending;
	/**
	 * HashMap that stores any BrowserFunctions which have been created but not yet registered with the web extension.
	 * These functions will be registered with the web extension as soon as the proxy to the extension is set up.
	 *
	 * The format of the HashMap is (page ID, list of function string and URL).
	 */
	static HashMap<Long, ArrayList<ArrayList<String>>> pendingBrowserFunctions = new HashMap<>();


	/**
	 * Interface is read/parsed at run time. No compilation with gdbus-code-gen necessary.
	 *
	 * Note,
	 * - When calling a method via g_dbus_proxy_call_sync(..g_variant params..),
	 *   the g_variant that describes parameters should only mirror incoming parameters.
	 *   Each type is a separate argument.
	 *   e.g:
	 *    g_variant                 xml:
	 *   "(si)", "string", 42 		   .. arg type='s'
	 *   								   .. arg type='i'
	 *
	 * - Nested parameters need to have a 2nd bracket around them.
	 *   e.g:
	 *    g_variant                    xml:
	 *    "((r)i)", *gvariant, 42			.. arg type='r'
	 *    									.. arg type='i'
	 *
	 * - '@' is a pointer to a gvariant. so '@r' is a pointer to nested type, i.e *gvariant
	 *
	 * To understand the mappings, it's good to understand DBus and GVariant's syntax:
	 * https://dbus.freedesktop.org/doc/dbus-specification.html#idm423
	 * https://developer.gnome.org/glib/stable/glib-GVariantType.html
	 *
	 * Be mindful about only using supported DBUS_TYPE_* , as convert* methods might fail otherwise.
	 * Alternatively, modify convert* methods.
	 */
	private static final String dbus_introspection_xml =
			"<node>"
			+  "  <interface name='" + INTERFACE_NAME + "'>"
			+  "    <method name='" + webkit2callJava + "'>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRING + "' name='webViewPtr' direction='in'/>"
			+  "      <arg type='"+ OS.DBUS_TYPE_DOUBLE + "' name='index' direction='in'/>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRING + "' name='token' direction='in'/>"
			+  "      <arg type='" + OS.DBUS_TYPE_SINGLE_COMPLETE + "' name='arguments' direction='in'/>"
			+  "      <arg type='" + OS.DBUS_TYPE_SINGLE_COMPLETE + "' name='result' direction='out'/>"
			+  "    </method>"
			+  "	<method name='" + webkitWebExtensionIdentifier + "'>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRING + "' name='webExtensionDbusName' direction='in'/>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRING + "' name='webExtensionDbusPath' direction='in'/>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRUCT_ARRAY_BROWSER_FUNCS + "' name='result' direction='out'/>"
			+  "    </method>"
			+  "  </interface>"
			+  "</node>";

	/**
	 * GDBus/DBus doesn't have a notion of Null.
	 * To get around this, we use magic numbers to represent special cases.
	 * Currently this is specific to Webkit to deal with Javascript data type conversions.
	 * @category gdbus */
	private static final byte SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY = 101;
	/** @category gdbus */
	private static final byte SWT_DBUS_MAGIC_NUMBER_NULL = 48;


	/** GDBusNodeInfo */
	private static Callback onBusAcquiredCallback;
	private static Callback onNameAcquiredCallback;
	private static Callback onNameLostCallback;
	private static Callback handleMethodCallback;

	/** Callback for asynchronous proxy calls to the extension */
	private static Callback callExtensionAsyncCallback;

	static {
		onBusAcquiredCallback = new Callback (WebkitGDBus.class, "onBusAcquiredCallback", 3); //$NON-NLS-1$
		if (onBusAcquiredCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		onNameAcquiredCallback = new Callback (WebkitGDBus.class, "onNameAcquiredCallback", 3); //$NON-NLS-1$
		if (onNameAcquiredCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		onNameLostCallback = new Callback (WebkitGDBus.class, "onNameLostCallback", 3); //$NON-NLS-1$
		if (onNameLostCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		handleMethodCallback = new Callback (WebkitGDBus.class, "handleMethodCallback", 8); //$NON-NLS-1$
		if (handleMethodCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		callExtensionAsyncCallback = new Callback (WebkitGDBus.class, "callExtensionAsyncCallback", 3); //$NON-NLS-1$
		if (callExtensionAsyncCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	}

	static boolean initialized;

	/** This method is not intended to be referenced by clients. Internal class. */
	static void init(String uniqueId) {
		if (initialized)
			return;
		initialized = true;
		DBUS_SERVICE_NAME = "org.eclipse.swt" + uniqueId;
		int owner_id = OS.g_bus_own_name(OS.G_BUS_TYPE_SESSION,
				Converter.javaStringToCString(DBUS_SERVICE_NAME),
				OS.G_BUS_NAME_OWNER_FLAGS_REPLACE | OS.G_BUS_NAME_OWNER_FLAGS_ALLOW_REPLACEMENT,
				onBusAcquiredCallback.getAddress(),
				onNameAcquiredCallback.getAddress(), // name_acquired_handler
				onNameLostCallback.getAddress(), // name_lost_handler
				0, // user_data
				0); // user_data_free_func

		if (owner_id == 0) {
			System.err.println("SWT WebkitGDBus: Failed to aquire bus name: " + DBUS_SERVICE_NAME);
		}
	}

	@SuppressWarnings("unused")
	private static void teardown_gdbus() {
		// Currently GDBus is persistent across browser instances.
		// If ever needed, gdbus can be disposed via:
		// g_bus_unown_name (owner_id);					// owner_id would need to be made global
		// g_dbus_node_info_unref (gdBusNodeInfo); // introspection_data Would need to be made global
	}

	/**
	 * @param connection  GDBusConnection *
	 * @param name		  const gchar *
	 * @param user_data   gpointer
	 * @return	void.
	 */
	@SuppressWarnings("unused") // Callback Only called directly by JNI.
	private static long onBusAcquiredCallback (long connection, long name, long user_data) {
		long gdBusNodeInfo;

		// Parse XML
		{
			long [] error = new long [1];
			gdBusNodeInfo = OS.g_dbus_node_info_new_for_xml(Converter.javaStringToCString(dbus_introspection_xml), error);
			if (gdBusNodeInfo == 0 || error[0] != 0) {
				System.err.println("SWT WebkitGDBus: Failed to get introspection data");
			}
			assert gdBusNodeInfo != 0 : "SWT WebkitGDBus: introspection data should not be 0";
		}

		// Register object
		{
			long [] error = new long [1];
			long interface_info = OS.g_dbus_node_info_lookup_interface(gdBusNodeInfo, Converter.javaStringToCString(INTERFACE_NAME));
			long vtable [] = { handleMethodCallback.getAddress(), 0, 0 };
			// SWT Dev Note: SWT Tool's "32/64 bit" checking mechanism sometimes get's confused by this method signature and shows an incorrect warning.
			// Other times it validates it fine. We ignore for now as 32bit will be dropped anyway.
			OS.g_dbus_connection_register_object(
					connection,
					Converter.javaStringToCString(DBUS_OBJECT_PATH),
					interface_info,
					vtable,
					0, // user_data
					0, // user_data_free_func
					error);

			if (error[0] != 0) {
				System.err.println("SWT WebkitGDBus: Failed to register object: " + DBUS_OBJECT_PATH);
				return 0;
			}
		}

		// Developer note:
		// To verify that a gdbus interface is regisetered on the gdbus, you can use the 'gdbus' utility.
		// e.g:
		// gdbus introspect --session --dest org.eclipse  <Press TAB KEY>    // it should expand to something like:  (uniqueID might be appended at the end).
		// gdbus introspect --session --dest org.eclipse.swt     			 // you can then get object info like:
		// gdbus introspect --session --dest org.eclipse.swt --object-path /org/eclipse/swt/gdbus

		return 0; // Actual callback is void.
	}


	@SuppressWarnings("unused") // Callback Only called directly by JNI.
	private static long onNameAcquiredCallback (long connection, long name, long user_data) {
		// Currently not used, but can be used if acquring the gdbus name should trigger something to load.
		return 0;
	}


	@SuppressWarnings("unused") // Callback Only called directly by JNI.
	private static long onNameLostCallback (long connection, long name, long user_data) {
		assert false : "This code should never have executed";
		System.err.println("SWT WebkitGDBus.java: Lost GDBus name. This should never occur");
		return 0;
	}



	/**
	 * This is called when a client call one of the GDBus methods.
	 *
	 * Developer note:
	 * This method can be reached directly from GDBus cmd utility:
	 * 	gdbus call --session --dest org.eclipse.swt<UNIQUE_ID> --object-path /org/eclipse/swt/gdbus --method org.eclipse.swt.gdbusInterface.HelloWorld
	 * where as you tab complete, you append the UNIQUE_ID.
	 *
	 * @param connection 	GDBusConnection
	 * @param sender 		const gchar
	 * @param object_path	const gchar
	 * @param interface_name const gchar
	 * @param method_name    const gchar
	 * @param gvar_parameters	 GVariant
	 * @param invocation     GDBusMethodInvocation
	 * @param user_data      gpointer
	 * @return
	 */
	@SuppressWarnings("unused") // Callback only called directly by JNI.
	private static long handleMethodCallback (
			long connection, long sender,
			long object_path, long interface_name,
			long method_name, long gvar_parameters,
			long invocation, long user_data) {

		String java_method_name = Converter.cCharPtrToJavaString(method_name, false);
		Object result = null;
		if (java_method_name != null) {
			if (java_method_name.equals(webkit2callJava)) {
				try {
					Object [] java_parameters = (Object []) convertGVariantToJava(gvar_parameters);
					result = WebKit.Webkit2Extension.webkit2callJavaCallback(java_parameters);
				} catch (Exception e) {
					// gdbus should always return to prevent extension from hanging.
					result = (String) WebBrowser.CreateErrorString (e.getLocalizedMessage ());
					System.err.println("SWT Webkit: Exception occured in Webkit2 callback logic. Bug?");
				}
			} else if (java_method_name.equals(webkitWebExtensionIdentifier)) {
				Object [] nameArray = (Object []) convertGVariantToJava(gvar_parameters);
				if (nameArray [0] != null && nameArray[0] instanceof String) EXTENSION_DBUS_NAME = (String) nameArray[0];
				if (nameArray [1] != null && nameArray[1] instanceof String) EXTENSION_DBUS_PATH = (String) nameArray[1];
				proxyToExtension = proxyToExtensionInit();
				if (proxyToExtension) {
					invokeReturnValueExtensionIdentifier(pendingBrowserFunctions, invocation);
				} else {
					invokeReturnValueExtensionIdentifier(null, invocation);
					System.err.println("SWT webkit: proxy to web extension failed to load, BrowserFunction may not work.");
				}
				return 0;
			}
		} else {
			result = (String) "SWT webkit: GDBus called an unknown method?";
			System.err.println("SWT webkit: Received a call from an unknown method: " + java_method_name);
		}
		invokeReturnValue(result, invocation);
		return 0;
	}

	@SuppressWarnings("unused")
	private static long callExtensionAsyncCallback (long source_object, long res, long user_data) {
		long [] gerror = new long [1];
		long result = OS.g_dbus_proxy_call_finish (proxy, res, gerror);
		if (gerror[0] != 0){
			long errMsg = OS.g_error_get_message(gerror[0]);
			String msg = Converter.cCharPtrToJavaString(errMsg, false);
			System.err.println("SWT webkit: There was an error executing something asynchronously with the extension (Java callback).");
			System.err.println("SWT webkit: the error message provided is " + msg);
			OS.g_error_free(gerror[0]);
		}
		return 0;
	}

	/**
	 * Returns a GVariant to the DBus invocation of the extension identifier method. When the extension
	 * is initialized it sends a DBus message to the SWT webkit instance. As a return value, the SWT webkit
	 * instance sends any BrowserFunctions that have been registered. If no functions have been registered,
	 * an "empty" function with a page ID of -1 is sent.
	 *
	 * @param map the HashMap of BrowserFunctions waiting to be registered in the extension, or null
	 * if you'd like to explicitly send an empty function signature
	 * @param invocation the GDBus invocation to return the value on
	 */
	private static void invokeReturnValueExtensionIdentifier (HashMap<Long, ArrayList<ArrayList<String>>> map,
			long invocation) {
		long resultGVariant;
		long builder;
		long type = OS.g_variant_type_new(OS.G_VARIANT_TYPE_ARRAY_BROWSER_FUNCS);
		builder = OS.g_variant_builder_new(type);
		if (builder == 0) return;
		Object [] tupleArray = new Object[3];
		boolean sendEmptyFunction;
		if (map == null) {
			sendEmptyFunction = true;
		} else {
			sendEmptyFunction = map.isEmpty() && !functionsPending;
		}
		/*
		 * No functions to register, send a page ID of -1 and empty strings.
		 */
		if (sendEmptyFunction) {
			tupleArray[0] = (long)-1;
			tupleArray[1] = "";
			tupleArray[2] = "";
			long tupleGVariant = convertJavaToGVariant(tupleArray);
			if (tupleGVariant != 0) {
				OS.g_variant_builder_add_value(builder, tupleGVariant);
			} else {
				System.err.println("SWT webkit: error creating empty BrowserFunction GVariant tuple, skipping.");
			}
		} else {
			for (long id : map.keySet()) {
				ArrayList<ArrayList<String>> list = map.get(id);
				if (list != null) {
					for (ArrayList<String> stringList : list) {
						Object [] stringArray = stringList.toArray();
						if (stringArray.length > 2) {
							System.err.println("SWT webkit: String array with BrowserFunction and URL should never have"
									+ "more than 2 Strings");
						}
						tupleArray[0] = id;
						System.arraycopy(stringArray, 0, tupleArray, 1, 2);
						long tupleGVariant = convertJavaToGVariant(tupleArray);
						if (tupleGVariant != 0) {
							OS.g_variant_builder_add_value(builder, tupleGVariant);
						} else {
							System.err.println("SWT webkit: error creating BrowserFunction GVariant tuple, skipping.");
						}
					}
				}
			}
		}
		resultGVariant = OS.g_variant_builder_end(builder);
		String typeString = Converter.cCharPtrToJavaString(OS.g_variant_get_type_string(resultGVariant), false);
		if (!OS.DBUS_TYPE_STRUCT_ARRAY_BROWSER_FUNCS.equals(typeString)) {
			System.err.println("An error packaging the GVariant occurred: type mismatch.");
		}
		long [] variants = {resultGVariant};
		long finalGVariant = OS.g_variant_new_tuple(variants, 1);
		OS.g_dbus_method_invocation_return_value(invocation, finalGVariant);
		OS.g_variant_builder_unref(builder);
		OS.g_variant_type_free(type);
		return;
	}

	private static void invokeReturnValue (Object result, long invocation) {
		long resultGVariant = 0;
		try {
			resultGVariant = convertJavaToGVariant(new Object [] {result}); // Result has to be a tuple.
		} catch (SWTException e) {
			// gdbus should always return to prevent extension from hanging.
			String errMsg = (String) WebBrowser.CreateErrorString (e.getLocalizedMessage ());
			resultGVariant = convertJavaToGVariant(new Object [] {errMsg});
		}
		OS.g_dbus_method_invocation_return_value(invocation, resultGVariant);
		return; // void return value.
	}

	/**
	 * Initializes the proxy connection to the web extension.
	 *
	 * @return true if establishing the proxy connections succeeded,
	 * false otherwise
	 */
	private static boolean proxyToExtensionInit() {
		if (proxy != 0) {
			return true;
		} else {
			if (EXTENSION_DBUS_NAME != null && EXTENSION_DBUS_PATH != null) {
				long [] error = new long [1];
				byte [] name = Converter.javaStringToCString(EXTENSION_DBUS_NAME);
				byte [] path = Converter.javaStringToCString(EXTENSION_DBUS_PATH);
				byte [] interfaceName = Converter.javaStringToCString(EXTENSION_INTERFACE_NAME);
				proxy = OS.g_dbus_proxy_new_for_bus_sync(OS.G_BUS_TYPE_SESSION, OS.G_DBUS_PROXY_FLAGS_NONE, 0, name, path, interfaceName, 0, error);
				if (error[0] != 0) {
					long errMsg = OS.g_error_get_message(error[0]);
					String msg = Converter.cCharPtrToJavaString(errMsg, false);
					OS.g_error_free(error[0]);
					System.err.println("SWT webkit: there was an error establishing the proxy connection to the extension. " +
							" The error is " + msg);
					return false;
				} else {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Calls the web extension synchronously. Returns true if the operation succeeded, and false
	 * otherwise (or if the operation times out).
	 *
	 * @param params a pointer to the GVariant containing the parameters
	 * @param methodName a String representing the DBus method name in the extension
	 * @return an Object representing the return value from DBus in boolean form
	 */
	static Object callExtensionSync (long params, String methodName) {
		long [] gerror = new long [1]; // GError **
		long gVariant = OS.g_dbus_proxy_call_sync(proxy, Converter.javaStringToCString(methodName),
				params, OS.G_DBUS_CALL_FLAGS_NO_AUTO_START, 1000, 0, gerror);
		if (gerror[0] != 0) {
			long errMsg = OS.g_error_get_message(gerror[0]);
			String msg = Converter.cCharPtrToJavaString(errMsg, false);
			/*
			 * Don't print console warnings for timeout errors, as we can handle these ourselves.
			 * Note, most timeout errors happen only when running test cases, not during "normal" use.
			 */
			if (msg != null && (!msg.contains("Timeout") && !msg.contains("timeout"))) {
				System.err.println("SWT webkit: There was an error executing something synchronously with the extension.");
				System.err.println("SWT webkit: The error message is: " + msg);
				return (Object) false;
			}
			OS.g_error_free(gerror[0]);
			return (Object) "timeout";
		}
		Object resultObject = gVariant != 0 ? convertGVariantToJava(gVariant) : (Object) false;
		// Sometimes we get back tuples from GDBus, which get converted into Object arrays. In this case
		// we only care about the first value, since the extension never returns anything more than that.
		if (resultObject instanceof Object[]) {
			return ((Object []) resultObject)[0];
		}
		return resultObject;
	}

	/**
	 * Calls the web extension asynchronously. Note, this method returning true does not
	 * guarantee the operation's success, it only means no errors occurred.
	 *
	 * @param params a pointer to the GVariant containing the parameters
	 * @param methodName a String representing the DBus method name in the extension
	 * @return true if the extension was called without errors, false otherwise
	 */
	static boolean callExtensionAsync (long params, String methodName) {
		long [] gerror = new long [1]; // GError **
		OS.g_dbus_proxy_call(proxy, Converter.javaStringToCString(methodName),
				params, OS.G_DBUS_CALL_FLAGS_NO_AUTO_START, 1000, 0, callExtensionAsyncCallback.getAddress(), gerror);
		if (gerror[0] != 0) {
			long errMsg = OS.g_error_get_message(gerror[0]);
			String msg = Converter.cCharPtrToJavaString(errMsg, false);
			System.err.println("SWT webkit: There was an error executing something asynchronously with the extension.");
			System.err.println("SWT webkit: The error message is: " + msg);
			OS.g_error_free(gerror[0]);
			return false;
		}
		return true;
	}

	/* TYPE NOTES
	 *
	 * GDBus doesn't support all the types that we need. I used encoded 'byte' to translate some types.
	 *
	 * - 'null' is not supported. I thought to potentially use  'maybe' types, but they imply a possible NULL of a certain type, but not null itself.
	 *   so I use 'byte=48' (meaning '0' in ASCII) to denote null.
	 *
	 * - Empty arrays/structs are not supported by gdbus.
	 *    "Container types ... Empty structures are not allowed; there must be at least one type code between the parentheses"
	 *	   src: https://dbus.freedesktop.org/doc/dbus-specification.html
	 *	I used byte=101  (meaning 'e' in ASCII) to denote empty array.
	 *
	 * In Javascript all Number types seem to be 'double', (int/float/double/short  -> Double). So we convert everything into double accordingly.
	 *
	 * DBus Type info:  https://dbus.freedesktop.org/doc/dbus-specification.html#idm423
	 * GDBus Type info: https://developer.gnome.org/glib/stable/glib-GVariantType.html
	 */

	/**
	 * Converts the given GVariant to a Java object.
	 * (Only subset of types is currently supported).
	 *
	 * We assume that the given gvariant does not contain errors. (checked by webextension first).
	 *
	 * @param gVariant a pointer to the native GVariant
	 */
	private static Object convertGVariantToJava(long gVariant){

		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_BOOLEAN)){
			return OS.g_variant_get_boolean(gVariant);
		}

		// see: WebKitGTK.java 'TYPE NOTES'
		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_BYTE)) {
			byte byteVal = OS.g_variant_get_byte(gVariant);

			switch (byteVal) {
			case WebkitGDBus.SWT_DBUS_MAGIC_NUMBER_NULL:
				return null;
			case WebkitGDBus.SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY:
				return new Object [0];
			default:
				System.err.println("SWT Error, received unsupported byte type via gdbus: " + byteVal);
				break;
			}
		}

		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_DOUBLE)){
			return OS.g_variant_get_double(gVariant);
		}

		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_UINT64)){
			return OS.g_variant_get_uint64(gVariant);
		}

		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_STRING)){
			return Converter.cCharPtrToJavaString(OS.g_variant_get_string(gVariant, null), false);
		}

		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_TUPLE)){
			int length = (int)OS.g_variant_n_children (gVariant);
			Object[] result = new Object[length];
			for (int i = 0; i < length; i++) {
				result[i] = convertGVariantToJava (OS.g_variant_get_child_value(gVariant, i));
			}
			return result;
		}

		String typeString = Converter.cCharPtrToJavaString(OS.g_variant_get_type_string(gVariant), false);
		SWT.error (SWT.ERROR_INVALID_ARGUMENT, new Throwable("Unhandled variant type " + typeString ));
		return null;
	}

	/**
	 * Converts the given Java Object to a GVariant * representation.
	 * (Only subset of types is currently supported).
	 *
	 * We assume that input Object may contain invalid types.
	 *
	 * @return pointer GVariant *
	 */
	private static long convertJavaToGVariant(Object javaObject) throws SWTException {

		if (javaObject == null) {
			return OS.g_variant_new_byte(WebkitGDBus.SWT_DBUS_MAGIC_NUMBER_NULL);  // see: WebKitGTK.java 'TYPE NOTES'
		}

		if (javaObject instanceof Long) {
			return OS.g_variant_new_uint64((Long) javaObject);
		}

		if (javaObject instanceof String) {
			return OS.g_variant_new_string (Converter.javaStringToCString((String) javaObject));
		}

		if (javaObject instanceof Boolean) {
			return OS.g_variant_new_boolean((Boolean) javaObject);
		}

		// We treat Integer, Long, Double, Short as a 'double' because in Javascript these are all 'double'.
		// Note,  they all extend 'Number' java type, so they are an instance of it.
		if (javaObject instanceof Number) {  // see: WebKitGTK.java 'TYPE NOTES'
			return OS.g_variant_new_double (((Number) javaObject).doubleValue());
		}

		if (javaObject instanceof Object[]) {
			Object[] arrayValue = (Object[]) javaObject;
			int length = arrayValue.length;

			if (length == 0) {
				return OS.g_variant_new_byte(WebkitGDBus.SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY);  // see: WebKitGTK.java 'TYPE NOTES'
			}

			long variants[] = new long [length];

			for (int i = 0; i < length; i++) {
				variants[i] = convertJavaToGVariant(arrayValue[i]);
			}

			return OS.g_variant_new_tuple(variants, length);
		}
		System.err.println("SWT Webkit: Invalid object being returned to javascript: " + javaObject.toString() + "\n"
				+ "Only the following are supported: null, String, Boolean, Number(Long,Integer,Double...), Object[] of basic types");
		throw new SWTException(SWT.ERROR_INVALID_ARGUMENT, "Given object is not valid: " + javaObject.toString());
	}
}
