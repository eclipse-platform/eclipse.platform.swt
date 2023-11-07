/*******************************************************************************
 * Copyright (c) 2018 Red Hat and others. All rights reserved.
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
package org.eclipse.swt.internal;

import java.util.*;
import java.util.function.*;

import org.eclipse.swt.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * General purpose DBus interface for SWT to interact with the operating system and vice versa.
 * (See also WebkitGDBus for the webkit specific gdbus interface).
 *
 * This implementation uses GDBus (Gnome DBus) to implement the DBus interface.
 *
 * It can be reached via:
 * 		gdbus call --session --dest org.eclipse.swt --object-path /org/eclipse/swt --method org.eclipse.swt.YOUR_METHOD YOUR_ARGS
 * where YOUR_ARGS can be something like  "MyString"  or  "['/tmp/myFile', '/tmp/myFile2']" etc..

 * For hygiene purposes, GVariant/GDBus native types/values should *never* leave this class. Convert on the way in/out.
 *
 * Technical notes:
 *  - Concurrent gdbus names can co-exist. (i.e, multiple session names in single proc).
 *    Meaning if you don't like org.eclipse.swt, you can add more session names.
 *  - This implementation is only a small subset of GDBus.
 *    E.g not all types are translated and not functionality implemented. Add them as you need them.
 *  - At time of writing (v 1.4), only handles incoming gdbus calls. But could be easily extended to
 *    handle outgoing gdbus calls.
 *
 * @category gdbus
 */
public class GDBus {

	final static String SWT_GDBUS_VERSION_INFO = "SWT_LIB GDbus firing up. Implementation v1.5";

	public static class GDBusMethod {
		final private String name;
		final private Function<Object[], Object[]> userFunction;
		final private String methodArgsXmlSignature;

		/**
		 * Create a method that GDBus will listen to.
		 *
		 * @param name of the method. It will be part of 'org.eclipse.swt.NAME'
		 * @param inputArgs   2D array pair of Strings in the format of:  (DBUS_TYPE_*, argument_name).
		 *                    Where argument_name is only so that it's seen by command line by user.
		 * @param outputArgs  Same as inputArgs, but for returning values.
		 * @param userFunction  A Function<Object[],Object[]>, that you would like to run when the user calls the method over gdbus.
		 *                      Note, input argument(s) are provided as an Object[] array. You need to cast items manually.
		 *                      Output must always be an Object[] array or null. (E.g Object[] with only 1 element in it).
		 *
		 * Here are examples that you can base your methods off of:
		 *      // Multiple input types and multiple output types.
		 *      // From command line, it can be called like this:
		 *      //   gdbus call --session --dest org.eclipse.swt --object-path /org/eclipse/swt --method org.eclipse.swt.typeTest true "world" 1234
		 *      // The call will return a tuple (struct) like: (true, 'world', 5678)
		 * 		GDBusMethod typeTest = new GDBusMethod(
		 * 		"typeTest",
		 * 		new String [][] {{OS.DBUS_TYPE_BOOLEAN, "boolean Test Val"}, {OS.DBUS_TYPE_STRING, "string Test Val"}, {OS.DBUS_TYPE_INT32, "int Test Val"}},
		 * 		new String [][] {{OS.DBUS_TYPE_BOOLEAN, "boolean Response"}, {OS.DBUS_TYPE_STRING, "string Test Response"}, {OS.DBUS_TYPE_INT32, "int Test Response"}},
		 * 		(args) -> {
		 * 			System.out.println(args[0] + " " + args[1] + " " + args[2]); // A
		 * 			return new Object[] {Boolean.valueOf(true), "world", Integer.valueOf(5678)} ;
		 * 		});
		 *
		 * 		// Single input and single output. Observe input is an array with one item. Output is an array with one item.
		 * 		// It can be called from cmd via:
		 * 		//    gdbus call --session --dest org.eclipse.swt --object-path /org/eclipse/swt --method org.eclipse.swt.singleValueTest 123
		 * 		// The return is a tuple with one element: (456,)
		 * 		GDBusMethod singleValTest = new GDBusMethod(
		 * 		"singleValueTest",
		 * 		new String[][] {{OS.DBUS_TYPE_INT32, "int val"}},
		 * 		new String[][] {{OS.DBUS_TYPE_INT32, "int ret"}},
		 * 		(arg) -> {
		 * 			System.out.println("Input int: " + arg[0]);
		 * 			return new Object[] {(Integer) 456};
		 * 		});
		 *
		 *      // A simple method for clients to check if org.eclipse.swt is up and running. (getting/parsing interface xml is too tedious).
		 *		// Reached via: gdbus call --session --dest org.eclipse.swt --object-path /org/eclipse/swt --method org.eclipse.swt.PingResponse
		 *		// return is: (true,)    #i.e (b)
		 * 		new GDBusMethod(
		 *		"PingResponse",
		 *		new String [0][0], // No input args.
		 *		new String [][] {{OS.DBUS_TYPE_BOOLEAN, "Ping boolean response"}}, // Single boolean res
		 *		(args) -> {
		 *			return new Object[] {true}; // You should 'g_variant_unref(result)' on client side.
		 *		})
		 */
		public GDBusMethod(String name, String [][] inputArgs, String [][] outputArgs, Function<Object[], Object[]> userFunction) {
			this.name = name;
			this.userFunction = userFunction;
			StringBuilder gdbBusArgsXml = new StringBuilder();
			for (String [] dataType : inputArgs) {
				gdbBusArgsXml.append("     <arg type='" + dataType[0] + "' name='" + dataType[1] + "' direction='in'/>\n");
			}
			for (String [] dataType : outputArgs) { // I haven't tested output args, but should work if types get converted properly.
				gdbBusArgsXml.append("     <arg type='" + dataType[0] + "' name='" + dataType[1] + "' direction='out'/>\n");
			}
			methodArgsXmlSignature = gdbBusArgsXml.toString();
		}

		private String getName() {
			return name;
		}

		private Function<Object[], Object[]> getUserFunction() {
			return userFunction;
		}

		private String getMethodArgsXmlSignature() {
			return methodArgsXmlSignature;
		}
	}

	/**
	 * Instantiate GDBus for use by SWT.
	 * Note, a new SWT instance that runs this "Steals" the session bus,
	 * but upon termination it returns the session back to the previous owner.
	 *
	 * To make this more flexible we append appName (derived from the
	 * application executable but can be set with the command-line argument
	 * -name) to the session name.
	 *
	 * @param methods GDBus methods that we should handle.
	 * @param appName appName to append to GDBus object name if not null
	 */
	public static void init (GDBusMethod[] methods, String appName) {
		String serviceName = DBUS_SERVICE_NAME;

		if (!initialized)
			initialized = true;
		else
			return;

		if (methods == null || methods.length == 0) {
			System.err.println("SWT Error, no gdbus methods to initialize.");
			return;
		}

		if (appName != null) {
			// GDBus allows alphanumeric characters, underscores and hyphens.
			// https://gitlab.gnome.org/GNOME/glib/blob/master/gio/gdbusutils.c
			// Replace invalid GDBus characters with hyphens.
			appName = appName.replaceAll("[^0-9A-Za-z_.\\-]", "-");
			serviceName += "." + appName;
		}

		gdbusMethods = Arrays.asList(methods);

		int owner_id = OS.g_bus_own_name(OS.G_BUS_TYPE_SESSION,
				Converter.javaStringToCString(serviceName),
				OS.G_BUS_NAME_OWNER_FLAGS_REPLACE | OS.G_BUS_NAME_OWNER_FLAGS_ALLOW_REPLACEMENT, // Allow name to be used by other eclipse instances.
				onBusAcquired.getAddress(),
				onNameAcquired.getAddress(), // name_acquired_handler
				onNameLost.getAddress(), // name_lost_handler
				0, // user_data
				0); // user_data_free_func

		if (owner_id == 0) {
			System.err.println("SWT GDBus: Failed to aquire bus name: " + serviceName);
		}
	}

	private static boolean initialized;
	private static List<GDBusMethod> gdbusMethods;

	private static final String DBUS_SERVICE_NAME = "org.eclipse.swt";
	private static final String DBUS_OBJECT_NAME  = "/org/eclipse/swt";
	private static final String INTERFACE_NAME    = "org.eclipse.swt";

	private static Callback onBusAcquired;
	private static Callback onNameAcquired;
	private static Callback onNameLost;
	private static Callback handleMethod;

	static {
		onBusAcquired = new Callback (GDBus.class, "onBusAcquired", 3); //$NON-NLS-1$
		onNameAcquired = new Callback (GDBus.class, "onNameAcquired", 3); //$NON-NLS-1$
		onNameLost = new Callback (GDBus.class, "onNameLost", 3); //$NON-NLS-1$
		handleMethod = new Callback (GDBus.class, "handleMethod", 8); //$NON-NLS-1$

		String swt_lib_versions = OS.getEnvironmentalVariable (OS.SWT_LIB_VERSIONS); // Note, this is read in multiple places.
		if (swt_lib_versions != null && swt_lib_versions.equals("1")) {
			System.out.println(SWT_GDBUS_VERSION_INFO);
		}
	}

	static void teardown_gdbus() {
		// Currently GDBus is persistent.
		// If ever needed, gdbus can be disposed via:
		// g_bus_unown_name (owner_id);					// owner_id would need to be made global
		// g_dbus_node_info_unref (gdBusNodeInfo); // introspection_data Would need to be made global
	}

	/**
	 * @return void. (No return value actually returned to OS. Just SWT mechanism dicates 'long' is returned.
	 */
	@SuppressWarnings("unused") // Callback only called directly by JNI.
	private static long onBusAcquired (long gDBusConnection, long const_gchar_name, long user_data) {
		long gdBusNodeInfo;

		{ // Generate and parse DBus XML interface.
			StringBuilder dbus_introspection_xml = new StringBuilder();
			dbus_introspection_xml.append("<node><interface name='" + INTERFACE_NAME + "'>\n");
			for (GDBusMethod method : gdbusMethods) {

				dbus_introspection_xml.append("  <method name='" + method.name + "'>\n");
				dbus_introspection_xml.append("   " + method.getMethodArgsXmlSignature() + "\n");
				dbus_introspection_xml.append("  </method>\n");
			}
			dbus_introspection_xml.append("</interface></node>");

			long [] error = new long [1];
			gdBusNodeInfo = OS.g_dbus_node_info_new_for_xml(Converter.javaStringToCString(dbus_introspection_xml.toString()), error);
			if (gdBusNodeInfo == 0 || error[0] != 0) {
				System.err.println("SWT GDBus: Failed to get introspection data");
			}
			assert gdBusNodeInfo != 0 : "SWT GDBus: introspection data should not be 0";
		}

		{ // Register object
			long [] error = new long [1];
			long interface_info = OS.g_dbus_node_info_lookup_interface(gdBusNodeInfo, Converter.javaStringToCString(INTERFACE_NAME));
			long vtable [] = { handleMethod.getAddress(), 0, 0 };
			OS.g_dbus_connection_register_object(
					gDBusConnection,
					Converter.javaStringToCString(DBUS_OBJECT_NAME),
					interface_info,
					vtable,
					0, // user_data
					0, // user_data_free_func
					error);
			if (error[0] != 0) {
				System.err.println("SWT GDBus: Failed to register object: " + DBUS_OBJECT_NAME);
				return 0;
			}
		}

		// Developer note:
		// To verify that a gdbus interface is regisetered on the gdbus, you can use the 'gdbus' utility.
		// e.g:
		// gdbus introspect --session --dest org.eclipse  <Press TAB KEY>    // it should expand to something like:  (uniqueID might be appended at the end).
		// gdbus introspect --session --dest org.eclipse.swt     			 // you can then get object info like:
		// gdbus introspect --session --dest org.eclipse.swt --object-path /org/eclipse/swt ... etc

		return 0; // Actual callback is void.
	}


	@SuppressWarnings("unused") // Callback Only called directly by JNI.
	private static long onNameAcquired (long connection, long name, long user_data) {
		// Currently not used, but can be used if acquring the gdbus name should trigger something to load.
		return 0;
	}

	@SuppressWarnings("unused") // Callback Only called directly by JNI.
	private static long onNameLost (long connection, long name, long user_data) {
		// Currently not used, but can be used if losing the gdbus name should trigger something.
		// As a note, if another instance steals the name, upon it's terminate the session is returned to it's former owner.
		return 0;
	}


	/**
	 * This is called when a client calls one of the GDBus methods.
	 *
	 * Developer note:
	 * This method can be reached directly from GDBus cmd utility:
	 * 	gdbus call --session --dest org.eclipse.swt --object-path /org/eclipse/swt --method org.eclipse.swt.<your method>
	 * Tip: Use tab-completion.
	 *
	 * @param connection 	GDBusConnection
	 * @param sender 		const gchar
	 * @param object_path	const gchar
	 * @param interface_name const gchar
	 * @param method_name    const gchar
	 * @param gvar_parameters	 GVariant
	 * @param invocation     GDBusMethodInvocation
	 * @param user_data      gpointer
	 */
	@SuppressWarnings("unused") // Callback only called directly by JNI.
	private static long handleMethod (
			long connection, long sender,
			long object_path, long interface_name,
			long method_name, long gvar_parameters,
			long invocation, long user_data) {

		long resultGVariant = 0;
		try {
			String java_method_name = Converter.cCharPtrToJavaString(method_name, false);
			for (GDBusMethod gdbusMethod : gdbusMethods) {
				if (gdbusMethod.getName().equals(java_method_name)) {
					Object [] args = convertGVariantToJava(gvar_parameters);
					Object [] returnVal = gdbusMethod.getUserFunction().apply(args);
					if (returnVal == null || (returnVal instanceof Object [])) {
						resultGVariant = convertJavaToGVariant(returnVal);
					} else {
						System.err.println("SWT GDBus error processing user return value: " + returnVal.toString() + ". Return value must be an Object[] or null.");
					}
					break;
				}
			}
		} catch (Exception e) {
			System.err.println("SWT GDBUS ERROR: Error in handling method: \n" + e.getMessage());
		} finally {
			// - GDBus method should always return to prevent caller from hanging.
			// - Note, result must be a tuple. (or null (0)..).
			OS.g_dbus_method_invocation_return_value(invocation, resultGVariant);
		}
		return 0; // void return value.
	}

	/**
	 * Converts the given GVariant(s) to Java Object(s).
	 * If GVariant is not an array, this returns an Object[] array with one element.
	 *
	 * Only subset of types is currently supported, add type(s) as/when you need them.
	 * For inspiration, see WebkitGDBus.java:convert..()
	 *
	 * @param gVariant a pointer to the native GVariant
	 */
	public static Object[] convertGVariantToJava(long gVariant) {
		Object retVal = convertGVariantToJavaHelper(gVariant);
		if (retVal instanceof Object[]) {
			return (Object[]) retVal;
		} else {
			System.err.println("SWT GDBus Error converting arguments : Expecting object array, received Object.");
			return null;
		}
	}

	private static Object convertGVariantToJavaHelper(long gVariant){
		// - Developer note:
		//   When instantiating GDBus dynamically (as we do),
		//   GDBus's 'Parameters' is _always_ a tuple of stuff.
		//   E.g If you pass it only a single argument, then you will have a tuple with one object (s).
		//   As such, '1) convert specific types' is only reached, at the earliest
		//   on the 2nd invocation of this recursive function.
		// - Note, tuples '()' are not the same as arrays 'a'. They're treated in different ways.

		// 1) Convert specific types. (non-array).
		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_STRING)){
			return Converter.cCharPtrToJavaString(OS.g_variant_get_string(gVariant, null), false);
		}

		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_BOOLEAN)) {
			return Boolean.valueOf(OS.g_variant_get_boolean(gVariant));
		}

		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_IN32)) {
			return Integer.valueOf(OS.g_variant_get_int32(gVariant));
		}

		// <You can add more primitive types here>


		//2) Handle struct of defined types. (Sort of like arrays, but note, tuples!=array).
		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_TUPLE)) {
			int length = (int)OS.g_variant_n_children (gVariant);
			Object[] result = new Object[length];
			for (int i = 0; i < length; i++) {
				result[i] = convertGVariantToJavaHelper (OS.g_variant_get_child_value(gVariant, i));
			}
			return result;
		}

		// 2 b) Handle an array of Strings. Same as above, except type is String.
		if (OS.g_variant_is_of_type(gVariant, OS.G_VARIANT_TYPE_STRING_ARRAY)) {
			int length = (int)OS.g_variant_n_children (gVariant);
			String[] result = new String[length];
			for (int i = 0; i < length; i++) {
				result[i] = (String) convertGVariantToJavaHelper (OS.g_variant_get_child_value(gVariant, i));
			}
			return result;
		}

		String typeString = Converter.cCharPtrToJavaString(OS.g_variant_get_type_string(gVariant), false);
		System.err.println("SWT GDBus: Error. Unhandled variant type (i.e DBus type):  " + typeString +
				"     You probably need to update  (SWT) GDBus.java:convertGVariantToJavaHelper() to support this type.");
		SWT.error (SWT.ERROR_INVALID_ARGUMENT);
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
	public static long convertJavaToGVariant(Object javaObject) throws SWTException {
		if (javaObject == null) {
			return 0;
		}

		if (javaObject instanceof String) {
			return OS.g_variant_new_string (Converter.javaStringToCString((String) javaObject));
		}

		if (javaObject instanceof Boolean) {
			return OS.g_variant_new_boolean((Boolean) javaObject);
		}

		if (javaObject instanceof Integer) {
			return OS.g_variant_new_int32((Integer) javaObject);
		}

		// <You can add more primitive types here>


		// Dangers:
		// Null values and empty arrays can cause problems.
		//   - DBus doesn't have notion of 'null'.
		//   - DBus doesn't support empty arrays.
		// If needed, see workaround implemented in WebkitGDBus.java
		if (javaObject instanceof Object[]) {
			Object[] arrayValue = (Object[]) javaObject;
			int length = arrayValue.length;

			long variants[] = new long [length];
			for (int i = 0; i < length; i++) {
				variants[i] = convertJavaToGVariant(arrayValue[i]);
			}
			return OS.g_variant_new_tuple(variants, length);
		}

		System.err.println("SWT GDbus: Invalid object being returned to caller: " + javaObject.toString() + "   You probably need to update (SWT) GDBus.java:convertJavaToGVariant()");
		throw new SWTException(SWT.ERROR_INVALID_ARGUMENT);
	}


}