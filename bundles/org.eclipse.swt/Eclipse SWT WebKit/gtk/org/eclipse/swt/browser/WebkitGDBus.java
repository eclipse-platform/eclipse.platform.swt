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
import org.eclipse.swt.widgets.*;

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
	/*
	 * If any of the GDBus names/paths/interfaces need to be changed, then they also need to be changed
	 * in the extension (webkitgtk_extension.c).
	 */

	/* WEBKITGDBUS_DBUS_NAME isn't actually used but is here for informative purposes */
	@SuppressWarnings("unused")
	private static final String WEBKITGDBUS_DBUS_NAME = "org.eclipse.swt";
	private static final byte [] WEBKITGDBUS_OBJECT_PATH = Converter.javaStringToCString("/org/eclipse/swt/gdbus");
	private static final String WEBKITGDBUS_INTERFACE_NAME_JAVA = "org.eclipse.swt.gdbusInterface";
	private static final byte [] WEBKITGDBUS_INTERFACE_NAME = Converter.javaStringToCString("org.eclipse.swt.gdbusInterface");

	/* Extension connection details, in byte [] form */
	private static final byte [] EXTENSION_DBUS_NAME = Converter.javaStringToCString("org.eclipse.swt.webkitgtk_extension");
	private static final byte [] EXTENSION_OBJECT_PATH = Converter.javaStringToCString("/org/eclipse/swt/webkitgtk_extension/gdbus");
	private static final byte [] EXTENSION_INTERFACE_NAME = Converter.javaStringToCString("org.eclipse.swt.webkitgtk_extension.gdbusInterface");

	/** Extension GDBusServer client address */
	private static String EXTENSION_DBUS_SERVER_CLIENT_ADDRESS;

	/* Accepted GDBus methods */
	private static final String webkit2callJava = WebKit.WebKitExtension.getJavaScriptFunctionName();
	private static final String webkitWebExtensionIdentifier = WebKit.WebKitExtension.getWebExtensionIdentifier();

	/* Connections */
	/** GDBusConnection from the web extension */
	static long connectionFromExtension;
	/** GDBusConnection to the web extension */
	static long connectionToExtension;
	/** A field that is set to true if the proxy connection has been established, false otherwise. */
	static boolean connectionToExtensionCreated;

	/* Server related objects */
	/** GDBusServer for the main SWT process */
	private static long gDBusServer = 0;
	/** GDBusAuthObserver for the server */
	private static long authObserver = 0;
	/** GUID of the GDBusServer */
	private static long guid = 0;

	/** Display this GDBus class is "attached" to */
	static Display display;


	// BrowserFunction logic
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
	private static final byte [] WEBKITGDBUS_INTROSPECTION_XML = Converter.javaStringToCString(
			"<node>"
			+  "  <interface name='" + WEBKITGDBUS_INTERFACE_NAME_JAVA + "'>"
			+  "    <method name='" + webkit2callJava + "'>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRING + "' name='webViewPtr' direction='in'/>"
			+  "      <arg type='"+ OS.DBUS_TYPE_DOUBLE + "' name='index' direction='in'/>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRING + "' name='token' direction='in'/>"
			+  "      <arg type='" + OS.DBUS_TYPE_SINGLE_COMPLETE + "' name='arguments' direction='in'/>"
			+  "      <arg type='" + OS.DBUS_TYPE_SINGLE_COMPLETE + "' name='result' direction='out'/>"
			+  "    </method>"
			+  "	<method name='" + webkitWebExtensionIdentifier + "'>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRING + "' name='webExtensionServerAddress' direction='in'/>"
			+  "      <arg type='"+ OS.DBUS_TYPE_STRUCT_ARRAY_BROWSER_FUNCS + "' name='result' direction='out'/>"
			+  "    </method>"
			+  "  </interface>"
			+  "</node>");

	/**
	 * GDBus/DBus doesn't have a notion of Null.
	 * To get around this, we use magic numbers to represent special cases.
	 * Currently this is specific to Webkit to deal with Javascript data type conversions.
	 * @category gdbus */
	private static final byte SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY = 101;
	/** @category gdbus */
	private static final byte SWT_DBUS_MAGIC_NUMBER_NULL = 48;


	/** Callback for GDBus method handling */
	private static Callback handleMethodCB;
	/** Callback for incoming connections to WebkitGDBus' server */
	private static Callback newConnectionCB;
	/** Callback for creating a new connection to the web extension */
	private static Callback newConnectionToExtensionCB;
	/** Callback for authenticating connections to WebkitGDBus' server */
	private static Callback authenticatePeerCB;
	/** Callback for asynchronous proxy calls to the extension */
	private static Callback callExtensionAsyncCB;

	static {
		handleMethodCB = new Callback (WebkitGDBus.class, "handleMethodCB", 8); //$NON-NLS-1$
		callExtensionAsyncCB = new Callback (WebkitGDBus.class, "callExtensionAsyncCB", 3); //$NON-NLS-1$
		newConnectionCB = new Callback (WebkitGDBus.class, "newConnectionCB", 3); //$NON-NLS-1$
		newConnectionToExtensionCB = new Callback (WebkitGDBus.class, "newConnectionToExtensionCB", 3); //$NON-NLS-1$
		authenticatePeerCB = new Callback (WebkitGDBus.class, "authenticatePeerCB", 4); //$NON-NLS-1$
	}

	/** True iff the GDBusServer has been initialized */
	static boolean initialized;
	/** True iff this class has been attached to a Display */
	static boolean attachedToDisplay;

	/** This method is in an internal class and is not intended to be referenced by clients. */
	static long init () {
		if (initialized) {
			return gDBusServer;
		}
		initialized = true;

		// Generate address and GUID
		long address = construct_server_address();
		authObserver = OS.g_dbus_auth_observer_new();
		guid = OS.g_dbus_generate_guid();

		// Create server
		long [] error = new long [1];
		gDBusServer = OS.g_dbus_server_new_sync(address, OS.G_DBUS_SERVER_FLAGS_NONE, guid, authObserver, 0, error);

		// Connect authentication and incoming connections signals to the newly created server, and start it
		if (gDBusServer != 0) {
			OS.g_signal_connect(gDBusServer, OS.new_connection, newConnectionCB.getAddress(), 0);
			OS.g_signal_connect(authObserver, OS.authorize_authenticated_peer, authenticatePeerCB.getAddress(), 0);
			OS.g_dbus_server_start(gDBusServer);
		} else {
			System.err.println("SWT WebKitGDBus: error creating DBus server " + Display.extractFreeGError(error[0]));
		}
		return gDBusServer;
	}

	/**
	 * Sets the Display for this class. This allows WebkitGDBus to attach its servers and related objects
	 * to the provided Display. When the Display is disposed, it will will release the GDBus related
	 * resources.
	 *
	 * @param displayToSet the Display to set
	 */
	static void setDisplay (Display displayToSet) {
		if (attachedToDisplay) {
			return;
		} else {
			display = displayToSet;

			/*
			 * Add the GDBusServer, GDBusAuthObserver, and GUID to the Display.
			 * Note that we don't add the connections yet, because they likely
			 * don't exist. Those are added in callbacks as they come in.
			 */
			if (gDBusServer != 0) display.dBusServers.add(gDBusServer);
			if (authObserver != 0) display.dBusAuthObservers.add(authObserver);
			if (guid != 0) display.dBusGUIDS.add(guid);
			attachedToDisplay = true;
		}
	}

	/**
	 * Constructs an address at which the GDBus server for the SWT main process
	 * can be reached.
	 *
	 * @return a pointer to the address
	 */
	private static long construct_server_address () {
		byte [] swt = Converter.wcsToMbcs("SWT-GDBusServer-", true);
		long swtPtr = OS.g_malloc(swt.length);
		C.memmove(swtPtr, swt, swt.length);
		long user = OS.g_get_user_name();
		byte [] template = Converter.wcsToMbcs("-XXXXXX", true);
		long templatePtr = OS.g_malloc(template.length);
		C.memmove(templatePtr, template, template.length);
		long fileName = OS.g_strconcat(swtPtr, user, templatePtr, 0);
		long [] error = new long [1];
		long address = OS.g_dir_make_tmp(fileName, error);
		if (address == 0) {
			System.err.println("SWT WebkitGDBus: error creating temp folder " + Display.extractFreeGError(error[0]));
		}
		byte [] socket = Converter.wcsToMbcs("unix:tmpdir=", true);
		long socketPtr = OS.g_malloc(socket.length);
		C.memmove(socketPtr, socket, socket.length);
		long finalAddress = OS.g_strconcat(socketPtr, address, 0);

		// Clean up temporary string pointers and return result
		OS.g_free(swtPtr);
		OS.g_free(user);
		OS.g_free(templatePtr);
		OS.g_free(fileName);
		OS.g_free(socketPtr);
		return finalAddress;
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
	@SuppressWarnings("unused") // callback not directly called by SWT
	private static long handleMethodCB (
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
					result = WebKit.WebKitExtension.webkit2callJavaCallback(java_parameters);
				} catch (Exception e) {
					// gdbus should always return to prevent extension from hanging.
					result = (String) WebBrowser.CreateErrorString (e.getLocalizedMessage ());
					System.err.println("SWT WebkitGDBus: Exception occured in Webkit2 callback logic.");
				}
			} else if (java_method_name.equals(webkitWebExtensionIdentifier)) {
				Object [] serverAddress = (Object []) convertGVariantToJava(gvar_parameters);
				if (serverAddress[0] != null && serverAddress[0] instanceof String) {
					EXTENSION_DBUS_SERVER_CLIENT_ADDRESS = (String) serverAddress[0];
					// Connect to the extension's server by creating a connection asynchronously
					createConnectionToExtension();
					/*
					 * Return any pending BrowserFunctions that were created before WebkitGDBus
					 * was initialized.
					 */
					invokeReturnValueExtensionIdentifier(pendingBrowserFunctions, invocation);
				} else {
					System.err.println("SWT WebkitGDBus: error in web extension identification process."
							+ " BrowserFunction may not work.");
				}
				return 0;
			}
		} else {
			result = (String) "SWT WebkitGDBus: GDBus called an unknown method?";
			System.err.println("SWT WebkitGDBus: Received a call from an unknown method: " + java_method_name);
		}
		invokeReturnValue(result, invocation);
		return 0;
	}

	@SuppressWarnings("unused") // callback not directly called by SWT
	private static long callExtensionAsyncCB (long source_object, long result, long user_data) {
		long [] error = new long [1];
		long gVariantResult = OS.g_dbus_connection_call_finish (connectionToExtension, result, error);
		if (error[0] != 0) {
			String msg = Display.extractFreeGError(error[0]);
			System.err.println("SWT WebkitGDBus: there was an error executing something asynchronously with the extension (Java callback).");
			System.err.println("SWT WebkitGDBus: the error message provided is " + msg);
		}
		OS.g_variant_unref(gVariantResult);
		return 0;
	}

	@SuppressWarnings("unused") // callback not directly called by SWT
	private static long authenticatePeerCB (long observer, long stream, long credentials, long user_data) {
		boolean authorized = false;
		if (credentials != 0) {
			long error [] = new long [1];
			long ownCredentials = OS.g_credentials_new();
			authorized = OS.g_credentials_is_same_user(credentials, ownCredentials, error);
			if (error[0] != 0) {
				String msg = Display.extractFreeGError(error[0]);
				System.err.println("SWT WebkitGDBus: error authenticating client connection to server " + msg);
			}
			OS.g_object_unref(ownCredentials);
		}
		return authorized ? 1 : 0;
	}

	@SuppressWarnings("unused") // callback not directly called by SWT
	private static long newConnectionCB (long server, long connection, long user_data) {
		long gdBusNodeInfo;
		long [] error = new long [1];
		gdBusNodeInfo = OS.g_dbus_node_info_new_for_xml(WEBKITGDBUS_INTROSPECTION_XML, error);
		if (gdBusNodeInfo == 0 || error[0] != 0) {
			System.err.println("SWT WebkitGDBus: failed to get introspection data");
		}
		assert gdBusNodeInfo != 0 : "SWT WebKitGDBus: introspection data should not be 0";

		long interface_info = OS.g_dbus_node_info_lookup_interface(gdBusNodeInfo, WEBKITGDBUS_INTERFACE_NAME);
		long vtable [] = { handleMethodCB.getAddress(), 0, 0 };

		OS.g_dbus_connection_register_object(
				connection,
				WEBKITGDBUS_OBJECT_PATH,
				interface_info,
				vtable,
				0, // user_data
				0, // user_data_free_func
				error);

		if (error[0] != 0) {
			System.err.println("SWT WebKitGDBus: failed to register object: " + WEBKITGDBUS_OBJECT_PATH);
			return 0;
		}

		// Ref the connection and add it to the Display's list of connections
		connectionFromExtension = OS.g_object_ref(connection);
		if (attachedToDisplay && display != null) {
			if (!display.dBusConnections.contains(connection)) display.dBusConnections.add(connectionFromExtension);
		}
		return 1;
	}

	@SuppressWarnings("unused") // callback not directly called by SWT
	private static long newConnectionToExtensionCB (long sourceObject, long result, long user_data) {
		long [] error = new long [1];
		connectionToExtension = OS.g_dbus_connection_new_for_address_finish(result, error);
		if (error[0] != 0) {
			System.err.println("SWT WebKitGDBus: error finishing connection: " + Display.extractFreeGError(error[0]));
			return 0;
		} else {
			connectionToExtensionCreated = true;
		}

		// Add the connections to the Display's list of connections
		if (attachedToDisplay && display != null) {
			if (!display.dBusConnections.contains(connectionToExtension)) display.dBusConnections.add(connectionToExtension);
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
				System.err.println("SWT WebKitGDBus: error creating empty BrowserFunction GVariant tuple, skipping.");
			}
		} else {
			for (long id : map.keySet()) {
				ArrayList<ArrayList<String>> list = map.get(id);
				if (list != null) {
					for (ArrayList<String> stringList : list) {
						Object [] stringArray = stringList.toArray();
						if (stringArray.length > 2) {
							System.err.println("SWT WebKitGDBus: String array with BrowserFunction and URL should never have"
									+ "more than 2 Strings");
						}
						tupleArray[0] = id;
						System.arraycopy(stringArray, 0, tupleArray, 1, 2);
						long tupleGVariant = convertJavaToGVariant(tupleArray);
						if (tupleGVariant != 0) {
							OS.g_variant_builder_add_value(builder, tupleGVariant);
						} else {
							System.err.println("SWT WebKitGDBus: error creating BrowserFunction GVariant tuple, skipping.");
						}
					}
				}
			}
		}
		resultGVariant = OS.g_variant_builder_end(builder);
		String typeString = Converter.cCharPtrToJavaString(OS.g_variant_get_type_string(resultGVariant), false);
		if (!OS.DBUS_TYPE_STRUCT_ARRAY_BROWSER_FUNCS.equals(typeString)) {
			System.err.println("SWT WebKitGDBus: an error packaging the GVariant occurred: type mismatch.");
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
	 * Asynchronously initializes a GDBusConnection to the web extension. Connection process
	 * will be confirmed when the newConnectionToExtension callback is called.
	 */
	private static void createConnectionToExtension() {
		byte [] address = Converter.javaStringToCString(EXTENSION_DBUS_SERVER_CLIENT_ADDRESS);
		long [] error = new long [1];
		// Create connection asynchronously to avoid deadlock issues
		OS.g_dbus_connection_new_for_address (address, OS.G_DBUS_CONNECTION_FLAGS_AUTHENTICATION_CLIENT,
				0, 0, newConnectionToExtensionCB.getAddress(), 0);

		if (error[0] != 0) {
			System.err.println("SWT WebkitGDBus: error creating connection to the extension "
					+ Display.extractFreeGError(error[0]));
		}
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
		long [] error = new long [1]; // GError **
		long gVariant = OS.g_dbus_connection_call_sync(connectionToExtension, EXTENSION_DBUS_NAME, EXTENSION_OBJECT_PATH,
				EXTENSION_INTERFACE_NAME, Converter.javaStringToCString(methodName), params,
				0, OS.G_DBUS_CALL_FLAGS_NO_AUTO_START, 1000, 0, error);
		if (error[0] != 0) {
			String msg = Display.extractFreeGError(error[0]);
			/*
			 * Don't print console warnings for timeout errors, as we can handle these ourselves.
			 * Note, most timeout errors happen only when running test cases, not during "normal" use.
			 */
			if (msg != null && (!msg.contains("Timeout") && !msg.contains("timeout"))) {
				System.err.println("SWT WebKitGDBus: there was an error executing something synchronously with the extension.");
				System.err.println("SWT WebKitGDBus: the error message is: " + msg);
				return (Object) false;
			}
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
		long [] error = new long [1]; // GError **
		OS.g_dbus_connection_call(connectionToExtension, EXTENSION_DBUS_NAME, EXTENSION_OBJECT_PATH,
				EXTENSION_INTERFACE_NAME, Converter.javaStringToCString(methodName), params,
				0, OS.G_DBUS_CALL_FLAGS_NO_AUTO_START, 1000, 0, callExtensionAsyncCB.getAddress(), 0);
		if (error[0] != 0) {
			String msg = Display.extractFreeGError(error[0]);
			System.err.println("SWT WebKitGDBus: there was an error executing something asynchronously "
					+ "with the extension.");
			System.err.println("SWT WebKitGDBus: the error message is: " + msg);
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
	static Object convertGVariantToJava(long gVariant){

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
				System.err.println("SWT WebKitGDBus: received an unsupported byte type via GDBus: " + byteVal);
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
	static long convertJavaToGVariant(Object javaObject) throws SWTException {

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
		System.err.println("SWT WebKitGDBus: invalid object being returned to JavaScript: " + javaObject.toString() + "\n"
				+ "Only the following are supported: null, String, Boolean, Number(Long,Integer,Double...), Object[] of basic types");
		throw new SWTException(SWT.ERROR_INVALID_ARGUMENT, "Given object is not valid: " + javaObject.toString());
	}
}
