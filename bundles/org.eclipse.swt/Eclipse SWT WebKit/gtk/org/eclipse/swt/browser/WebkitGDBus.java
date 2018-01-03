package org.eclipse.swt.browser;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.webkit.*;

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
	private static final String DBUS_OBJECT_NAME = "/org/eclipse/swt/gdbus";
	private static final String INTERFACE_NAME = "org.eclipse.swt.gdbusInterface";

	/** Accepted methods over gdbus */
	private static final String webkit2callJava = WebKit.Webkit2Extension.getJavaScriptFunctionName();


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
			+  "  </interface>"
			+  "</node>";

	/**
	 * GDBus/DBus doesn't have a notion of Null.
	 * To get around this, we use magic numbers to represent special cases.
	 * Currently this is specific to Webkit to deal with Javascript data type conversions.
	 * @category gdbus */
	public static final byte SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY = 101;
	/** @category gdbus */
	public static final byte SWT_DBUS_MAGIC_NUMBER_NULL = 48;


	/** GDBusNodeInfo */
	private static Callback onBusAcquiredCallback;
	private static Callback onNameAcquiredCallback;
	private static Callback onNameLostCallback;
	private static Callback handleMethodCallback;

	static {
		onBusAcquiredCallback = new Callback (WebkitGDBus.class, "onBusAcquiredCallback", 3); //$NON-NLS-1$
		if (onBusAcquiredCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		onNameAcquiredCallback = new Callback (WebkitGDBus.class, "onNameAcquiredCallback", 3); //$NON-NLS-1$
		if (onNameAcquiredCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		onNameLostCallback = new Callback (WebkitGDBus.class, "onNameLostCallback", 3); //$NON-NLS-1$
		if (onNameLostCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);

		handleMethodCallback = new Callback (WebkitGDBus.class, "handleMethodCallback", 8); //$NON-NLS-1$
		if (handleMethodCallback.getAddress () == 0) SWT.error (SWT.ERROR_NO_MORE_CALLBACKS);
	}

	static private boolean initialized;
	static void init(String uniqueId) {
		if (initialized)
			return;
		initialized = true;
		DBUS_SERVICE_NAME = "org.eclipse.swt" + uniqueId;
		int owner_id = WebKitGTK.g_bus_own_name(OS.G_BUS_TYPE_SESSION,
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

	static void teardown_gdbus() {
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
	private static long /*int*/ onBusAcquiredCallback (long /*int*/ connection, long /*int*/ name, long /*int*/ user_data) {
		long /*int*/ gdBusNodeInfo;

		// Parse XML
		{
			long /*int*/ [] error = new long /*int*/ [1];
			gdBusNodeInfo = WebKitGTK.g_dbus_node_info_new_for_xml(Converter.javaStringToCString(dbus_introspection_xml), error);
			if (gdBusNodeInfo == 0 || error[0] != 0) {
				System.err.println("SWT WebkitGDBus: Failed to get introspection data");
			}
			assert gdBusNodeInfo != 0 : "SWT WebkitGDBus: introspection data should not be 0";
		}

		// Register object
		{
			long /*int*/ [] error = new long /*int*/ [1];
			long /*int*/ interface_info = WebKitGTK.g_dbus_node_info_lookup_interface(gdBusNodeInfo, Converter.javaStringToCString(INTERFACE_NAME));
			long /*int*/ vtable [] = { handleMethodCallback.getAddress(), 0, 0 };
			// SWT Dev Note: SWT Tool's "32/64 bit" checking mechanism sometimes get's confused by this method signature and shows an incorrect warning.
			// Other times it validates it fine. We ignore for now as 32bit will be dropped anyway.
			WebKitGTK.g_dbus_connection_register_object(
					connection,
					Converter.javaStringToCString(DBUS_OBJECT_NAME),
					interface_info,
					vtable,
					0, // user_data
					0, // user_data_free_func
					error);

			if (error[0] != 0) {
				System.err.println("SWT WebkitGDBus: Failed to register object: " + DBUS_OBJECT_NAME);
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
	private static long /*int*/ onNameAcquiredCallback (long /*int*/ connection, long /*int*/ name, long /*int*/ user_data) {
		// Currently not used, but can be used if acquring the gdbus name should trigger something to load.
		return 0;
	}


	@SuppressWarnings("unused") // Callback Only called directly by JNI.
	private static long /*int*/ onNameLostCallback (long /*int*/ connection, long /*int*/ name, long /*int*/ user_data) {
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
	private static long /*int*/ handleMethodCallback (
			long /*int*/ connection, long /*int*/ sender,
			long /*int*/ object_path, long /*int*/ interface_name,
			long /*int*/ method_name, long /*int*/ gvar_parameters,
			long /*int*/ invocation, long /*int*/ user_data) {

		String java_method_name = Converter.cCharPtrToJavaString(method_name, false);
		Object result = null;
		if (java_method_name != null && java_method_name.equals(webkit2callJava)) {
			try {
				Object [] java_parameters = (Object []) convertGVariantToJava(gvar_parameters);
				result = WebKit.Webkit2Extension.webkit2callJavaCallback(java_parameters);
			} catch (Exception e) {
				// gdbus should always return to prevent extension from hanging.
				result = (String) WebBrowser.CreateErrorString (e.getLocalizedMessage ());
				System.err.println("SWT Webkit: Exception occured in Webkit2 callback logic. Bug?");
			}
		} else {
			result = (String) "SWT Webkit: Gdbus called an unknown method?";
			System.err.println("SWT WebkitGDBus: Received a call from an unknown method: " + java_method_name);
		}
		long /*int*/ resultGVariant = 0;
		try {
			resultGVariant = convertJavaToGVariant(new Object [] {result}); // Result has to be a tuple.
		} catch (SWTException e) {
			// gdbus should always return to prevent extension from hanging.
			String errMsg = (String) WebBrowser.CreateErrorString (e.getLocalizedMessage ());
			resultGVariant = convertJavaToGVariant(new Object [] {errMsg});
		}
		WebKitGTK.g_dbus_method_invocation_return_value(invocation, resultGVariant);
		return 0; // void return value.
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
	private static Object convertGVariantToJava(long /*int*/ gVariant){

		if (WebKitGTK.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_BOOLEAN)){
			return new Boolean(WebKitGTK.g_variant_get_boolean(gVariant));
		}

		// see: WebKitGTK.java 'TYPE NOTES'
		if (WebKitGTK.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_BYTE)) {
			byte byteVal = WebKitGTK.g_variant_get_byte(gVariant);

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

		if (WebKitGTK.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_DOUBLE)){
			return new Double(WebKitGTK.g_variant_get_double(gVariant));
		}

		if (WebKitGTK.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_STRING)){
			return Converter.cCharPtrToJavaString(WebKitGTK.g_variant_get_string(gVariant, null), false);
		}

		if (WebKitGTK.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_TUPLE)){
			int length = (int)WebKitGTK.g_variant_n_children (gVariant);
			Object[] result = new Object[length];
			for (int i = 0; i < length; i++) {
				result[i] = convertGVariantToJava (WebKitGTK.g_variant_get_child_value(gVariant, i));
			}
			return result;
		}

		String typeString = Converter.cCharPtrToJavaString(WebKitGTK.g_variant_get_type_string(gVariant), false);
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
	private static long /*int*/ convertJavaToGVariant(Object javaObject) throws SWTException {

		if (javaObject == null) {
			return WebKitGTK.g_variant_new_byte(WebkitGDBus.SWT_DBUS_MAGIC_NUMBER_NULL);  // see: WebKitGTK.java 'TYPE NOTES'
		}

		if (javaObject instanceof String) {
			return WebKitGTK.g_variant_new_string (Converter.javaStringToCString((String) javaObject));
		}

		if (javaObject instanceof Boolean) {
			return WebKitGTK.g_variant_new_boolean((Boolean) javaObject);
		}

		// We treat Integer, Long, Double, Short as a 'double' because in Javascript these are all 'double'.
		// Note,  they all extend 'Number' java type, so they are an instance of it.
		if (javaObject instanceof Number) {  // see: WebKitGTK.java 'TYPE NOTES'
			return WebKitGTK.g_variant_new_double (((Number) javaObject).doubleValue());
		}

		if (javaObject instanceof Object[]) {
			Object[] arrayValue = (Object[]) javaObject;
			int length = arrayValue.length;

			if (length == 0) {
				return WebKitGTK.g_variant_new_byte(WebkitGDBus.SWT_DBUS_MAGIC_NUMBER_EMPTY_ARRAY);  // see: WebKitGTK.java 'TYPE NOTES'
			}

			long /*int*/ variants[] = new long /*int*/[length];

			for (int i = 0; i < length; i++) {
				variants[i] = convertJavaToGVariant(arrayValue[i]);
			}

			return WebKitGTK.g_variant_new_tuple(variants, length);
		}
		System.err.println("SWT Webkit: Invalid object being returned to javascript: " + javaObject.toString() + "\n"
				+ "Only the following are supported: null, String, Boolean, Number(Long,Integer,Double...), Object[] of basic types");
		throw new SWTException(SWT.ERROR_INVALID_ARGUMENT, "Given object is not valid: " + javaObject.toString());
	}
}
