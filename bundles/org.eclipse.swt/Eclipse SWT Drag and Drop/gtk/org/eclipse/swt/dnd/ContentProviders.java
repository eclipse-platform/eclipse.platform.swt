/*******************************************************************************
 * Copyright (c) 2025 Kichwa Coders Canada, Inc.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *******************************************************************************/
package org.eclipse.swt.dnd;

import java.lang.reflect.*;
import java.util.*;

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.GAsyncReadyCallbackHelper.*;
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk4.*;

/**
 * Manages <a href=
 * "https://docs.gtk.org/gdk4/class.ContentProvider.html">GdkContentProviders</a>
 * and the (de)serializers they rely on
 *
 * As the process only has a single collection of
 * serializers/deserializers/content providers the life-cycle of this class is
 * that of the process, which means this class is a singleton.
 *
 * The challenge of handling content providers in GTK4 is that it is all based
 * around serializing/deserializing GValue and registering GTypes as being
 * transferable. However we want to keep the Java objects in the Java space
 * until we are ready to transfer them.
 *
 * Therefore, we register GTypes associated with each {@link Transfer}, those
 * GTypes are boxed GTypes which box a simple long value. That long value we use
 * as an index to connect back to Java object.
 */
class ContentProviders {
	/**
	 * There are multiple clipboards in GTK, each with its own current contents
	 * (providers). We use this enum to store our view of the state of each of these
	 * clipboard types.
	 */
	public static enum CLIPBOARD_DATA {
		CLIPBOARD(1), PRIMARYCLIPBOARD(2), DRAG(3);

		/**
		 * Used to map the data passed through the serializers (from
		 * gdk_content_serializer_get_value) back to the clipboard data that value is
		 * referring to.
		 */
		private final long sourceId;

		/**
		 * This contains the collection of data for each type of clipboard.
		 *
		 * Map of transferKey() -> associated data
		 */
		private final Map<String, Object> data = new HashMap<>();
		/**
		 * The collection of GdkContentProviders that this clipboard currently has
		 * assigned to it.
		 */
		private long providers = 0;

		CLIPBOARD_DATA(long id) {
			this.sourceId = id;
		}

		/**
		 * Return the DESTINATION instance matching the clipboards using the constants
		 * {@link DND#CLIPBOARD} and {@link DND#SELECTION_CLIPBOARD}
		 */
		public static CLIPBOARD_DATA fromDNDConstants(int clipboards) {
			if (clipboards == DND.CLIPBOARD)
				return CLIPBOARD;
			if (clipboards == DND.SELECTION_CLIPBOARD)
				return PRIMARYCLIPBOARD;
			// the clipboards should have been error checked for validity before entering
			// this method
			throw new UnsupportedOperationException("Error - invalid clipboards");
		}

		/**
		 * Returns the clipboard data matching the given sourceId
		 */
		public static CLIPBOARD_DATA fromSourceId(long sourceId) {
			CLIPBOARD_DATA[] values = CLIPBOARD_DATA.values();
			for (CLIPBOARD_DATA d : values) {
				if (d.sourceId == sourceId) {
					return d;
				}
			}
			return null;
		}

		/**
		 * At the beginning of a set contents operation clear out any old data we have
		 * copied to the clipboard.
		 *
		 * TODO call clear when we lose ownership of this clipboard by registering with
		 * https://docs.gtk.org/gdk4/signal.Clipboard.changed.html and then maybe
		 * checking https://docs.gtk.org/gdk4/method.Clipboard.is_local.html and if not
		 * local anymore then we should clear the data we placed.
		 */
		void clear() {
			this.data.clear();
			if (this.providers != 0) {
				OS.g_object_unref(this.providers);
				this.providers = 0;
			}

		}
	}

	/**
	 * The life-time of the serializers in GTK4 (Gdk) are the life-time of the
	 * process so we use a singleton to store the mapping data that is needed.
	 */
	private static ContentProviders instance = new ContentProviders();

	/**
	 * The types registered with {@link #registerType(String)} on GTK4.
	 *
	 * Using TreeMap to make the maps sorted to ease debugging.
	 *
	 * Map of Transfer.id type -> Transfer's type name.
	 */
	final private Map<Integer, String> ID_TO_CONTENTTYPE = new TreeMap<>();

	/**
	 * The types registered with {@link #registerType(String)} on GTK4.
	 *
	 * Using TreeMap to make the maps sorted to ease debugging.
	 *
	 * Map of Transfer's type name -> Transfer.id type
	 */
	final private Map<String, Integer> CONTENTTYPE_TO_ID = new TreeMap<>();

	/**
	 * Next index to use for {@link #registerType(String)}
	 */
	private int typeIndex = 1;

	/**
	 * This the data that has been deserialized and associates source id (which is a
	 * number greater than those in {@link CLIPBOARD_DATA} so they can be
	 * differentied)
	 *
	 * It is expected that the map is normally empty and only has data while a
	 * gdk_clipboard_read_value_async (or similar) is active.
	 *
	 * Map of sourceId -> associated data
	 */
	private final Map<Long, Object> deserializedData = new HashMap<>();
	private long nextDeserializedDataId = 100;

	// These normally need disposal, but since this class is a singleton it isn't
	// disposed.
	private Callback gdkContentSerializeFunc;
	private Callback gdkContentDeserializeFunc;

	/**
	 * All transfers registered with the GTK serializers
	 *
	 * Key is the transfer obtained with {@link #transferKey(Transfer)}
	 */
	private Map<String, Transfer> registeredTransfers = new TreeMap<>();
	/**
	 * All transfer IDs registered with the GTK serializers
	 *
	 * Key is the transfer obtained with {@link #transferKey(Transfer)} Value is the
	 * Gtype of of the transfer
	 */
	private Map<String, Long> registeredTransferGtypes = new TreeMap<>();

	private ContentProviders() {
		gdkContentSerializeFunc = new Callback(this, "gdkContentSerializeFunc", void.class, new Type[] { long.class }); //$NON-NLS-1$
		gdkContentDeserializeFunc = new Callback(this, "gdkContentDeserializeFunc", void.class, //$NON-NLS-1$
				new Type[] { long.class });
	}

	public static ContentProviders getInstance() {
		return instance;
	}

	/**
	 * Called by {@link Transfer#registerType(String)} only
	 */
	public int registerType(String formatName) {
		if (formatName == null) {
			return 0;
		}
		Integer id = CONTENTTYPE_TO_ID.get(formatName);
		if (id == null) {
			id = typeIndex++;
			CONTENTTYPE_TO_ID.put(formatName, id);
			ID_TO_CONTENTTYPE.put(id, formatName);
		}
		return id;
	}

	/**
	 * Create a set of content providers (GdkContentProviders) for the given SWT
	 * Transfer Types and data, assigned to the given clipboard.
	 *
	 * @return the content providers. The data is owned by the called function.
	 */
	public long createContentProviders(Object[] data, Transfer[] transfers, CLIPBOARD_DATA id) {
		id.clear();
		long[] providerList = new long[transfers.length];
		for (int i = 0; i < transfers.length; i++) {
			Transfer transfer = transfers[i];
			long gType = getGType(transfer);

			// gvalue represents a data + transfer pair, with the
			// gtype we can extract the transfer type, and that
			// plus id can extract the data
			long gvalue = OS.content_providers_create_gvalue(gType, id.sourceId);
			providerList[i] = GTK4.gdk_content_provider_new_for_value(gvalue);
			OS.g_value_unset(gvalue);
			OS.g_free(gvalue);

			id.data.put(transferKey(transfer), data[i]);
		}

		long providers = GTK4.gdk_content_provider_new_union(providerList, providerList.length);
		id.providers = providers;
		return providers;
	}

	/**
	 * Convert the Java object to data with transfer.javaToNative and then write all
	 * the data to the output stream.
	 *
	 * Use the gvalue of the serializer to identify which Transfer and object to
	 * complete the transfer with.
	 *
	 * Referenced by Callback {@link #gdkContentSerializeFunc}
	 */
	@SuppressWarnings("unused")
	private void gdkContentSerializeFunc(long pSerializer) {
		GdkContentSerializer serializer = new GdkContentSerializer(pSerializer);

		Transfer transfer = getTransfer(serializer.gtype());
		TransferData transferData = new TransferData();
		transferData.type = getTypeId(serializer.mime_type());
		long sourceId = OS.g_value_get_boxed(serializer.gvalue());
		CLIPBOARD_DATA clipboardData = CLIPBOARD_DATA.fromSourceId(sourceId);
		if (transferData.type == 0 || transfer == null || clipboardData == null) {
			/*
			 * This should be impossible - the serializer should not be called if we haven't
			 * declared this as being able to convert the data
			 */
			serializer.return_error(
					OS.g_error_new_literal(OS.g_io_error_quark(), OS.G_IO_ERROR_FAILED, "Could not convert data"));
			return;
		}

		Object object = clipboardData.data.get(transferKey(transfer));

		transfer.javaToNative(object, transferData);
		if (transferData.result != 1) {
			serializer.return_error(OS.g_error_new_literal(OS.g_io_error_quark(), OS.G_IO_ERROR_FAILED,
					"Transfer object could not convert data"));
			return;
		}

		GAsyncReadyCallbackHelper.run(new Async() {
			@Override
			public void async(long callback) {
				OS.g_output_stream_write_all_async(serializer.output_stream(), transferData.pValue, transferData.length,
						serializer.priority(), serializer.cancellable(), callback, 0);
			}

			@Override
			public void callback(long result) {
				long[] error = new long[1];
				boolean finish = OS.g_output_stream_write_all_finish(serializer.output_stream(), result, null, error);
				if (!finish) {
					serializer.return_error(error[0]);
				} else {
					serializer.return_success();
				}
				OS.g_free(transferData.pValue);
			}
		});
	}

	/**
	 * Use the gtype + mime_type of the deserializer to identify which Transfer to
	 * complete the transfer with.
	 *
	 * Read all the data from the source (clipboard or drag source) into this memory
	 * stream and then, on success, convert the data to Java object with
	 * transfer.nativeToJava.
	 *
	 * Then place the converted object deserializedData and associate the gvalue
	 * with the deserializedData by using nextDeserializedDataId
	 *
	 * Referenced by Callback {@link #gdkContentSerializeFunc}
	 */
	@SuppressWarnings("unused")
	private void gdkContentDeserializeFunc(long pDeserializer) {
		GdkContentDeserializer deserializer = new GdkContentDeserializer(pDeserializer);

		Transfer transfer = getTransfer(deserializer.gtype());
		TransferData transferData = new TransferData();
		transferData.type = getTypeId(deserializer.mime_type());
		if (transferData.type == 0 || transfer == null) {
			/*
			 * This should be impossible - the serializer should not be called if we haven't
			 * declared this as being able to convert the data
			 */
			deserializer.return_error(
					OS.g_error_new_literal(OS.g_io_error_quark(), OS.G_IO_ERROR_FAILED, "Could not convert data"));
		}

		long memoryStream = OS.g_memory_output_stream_new_resizable();
		GAsyncReadyCallbackHelper.run(new Async() {
			@Override
			public void async(long callback) {
				OS.g_output_stream_splice_async(memoryStream, deserializer.input_stream(),
						OS.G_OUTPUT_STREAM_SPLICE_CLOSE_SOURCE | OS.G_OUTPUT_STREAM_SPLICE_CLOSE_TARGET,
						deserializer.priority(), deserializer.cancellable(), callback, 0);
			}

			@Override
			public void callback(long result) {
				long[] error = new long[1];
				long size = OS.g_output_stream_splice_finish(memoryStream, result, null);
				if (size < 0) {
					deserializer.return_error(error[0]);
					return;
				} else if (size > Integer.MAX_VALUE) {
					/*
					 * the size of source object is > largest object allowed in Java.
					 *
					 * Is this a real-world problem? If so, we should change
					 * g_memory_output_stream_new_resizable to use a limited size realloc function
					 * that fails at the point of trying to write too many bytes instead of waiting
					 * until here to error.
					 */
					deserializer
							.return_error(OS.g_error_new_literal(OS.g_io_error_quark(), OS.G_IO_ERROR_MESSAGE_TOO_LARGE,
									"Could not convert data because transfer size was too big " + size));
					return;
				}

				transferData.length = (int) size;
				transferData.pValue = OS.g_memory_output_stream_get_data(memoryStream);
				transferData.format = 8;
				Object data = transfer.nativeToJava(transferData);
				OS.g_object_unref(memoryStream);

				// Associate the transfer data to the specific gvalue
				// by using this source id
				long gvalue = deserializer.get_value();
				long deserializedDataId = nextDeserializedDataId++;
				OS.g_value_take_boxed(gvalue, deserializedDataId);

				deserializedData.put(deserializedDataId, data);
				deserializer.return_success();
			}
		});

	}

	/**
	 * Return the type for the given mime type
	 *
	 * Returns 0 on error (unknown mime_type). Valid type ids are >= 1. See
	 * {@link #typeIndex}
	 */
	private int getTypeId(String mime_type) {
		Integer typeObject = CONTENTTYPE_TO_ID.get(mime_type);
		return typeObject == null ? 0 : typeObject;
	}

	/**
	 * Return the transfer associated with the given gtype
	 *
	 * Return null on error (unknown gtype)
	 */
	private Transfer getTransfer(long gtype) {
		long g_type_name = OS.g_type_name(gtype);
		if (g_type_name == 0) {
			return null;
		}
		String transferKey = Converter.cCharPtrToJavaString(g_type_name, false);
		return registeredTransfers.get(transferKey);
	}

	/**
	 * Create a string key for the transfer that can be used as a key for Map and as
	 * a GType
	 */
	private String transferKey(Transfer transfer) {
		// The actual gtype name here is used to help debugging the code.
		// The value does not matter beyond that, as long is it is a unique string
		// which is achieved by appending transfer.id
		// In case a transfer has non ASCII characters in its name we also
		// remove them
		String lowerCaseSafeClassName = transfer.getClass().getSimpleName().replaceAll("[^A-Za-z0-9_]", "_");
		return "SwtTransfer_" + lowerCaseSafeClassName + "_" + transfer.id;
	}

	/**
	 * Register the transfer in the GTK land if not registered already.
	 *
	 * See class comment for overall design and connection between different parts.
	 */
	private void registerTransfer(Transfer transfer) {
		String name = transferKey(transfer);
		if (registeredTransfers.containsKey(name)) {
			return;
		}
		registeredTransfers.put(name, transfer);
		long gtype = OS.content_providers_create_gtype(name);
		registeredTransferGtypes.put(name, gtype);
		String[] mimeTypes = transfer.getTypeNames();
		for (int i = 0; i < mimeTypes.length; i++) {
			String mimeType = mimeTypes[i];
			GTK4.gdk_content_register_serializer(gtype, mimeType, gdkContentSerializeFunc.getAddress(), 0, 0);
			GTK4.gdk_content_register_deserializer(mimeType, gtype, gdkContentDeserializeFunc.getAddress(), 0, 0);
		}
	}

	/**
	 * Get the GType for the given transfer, and {@link #registerTransfer(Transfer)}
	 * if needed
	 *
	 * The gtype can then be used to request data from GTK with methods such as
	 * gdk_clipboard_read_value_async
	 */
	public long getGType(Transfer transfer) {
		registerTransfer(transfer);
		return registeredTransferGtypes.get(transferKey(transfer));
	}

	/**
	 * Return the Java object associated with the given GValue
	 */
	public Object getObject(long gvalue) {
		if (gvalue == 0) {
			return null;
		}

		long namePtr = OS.G_VALUE_TYPE_NAME(gvalue);
		String transferName = Converter.cCharPtrToJavaString(namePtr, false);
		Transfer transfer = registeredTransfers.get(transferName);
		if (transfer != null) {
			long g_value_get_boxed = OS.g_value_get_boxed(gvalue);
			CLIPBOARD_DATA fromSourceId = CLIPBOARD_DATA.fromSourceId(g_value_get_boxed);
			if (fromSourceId != null) {
				// Object is from within app, so get contents from clipboard data
				// Do not remove our reference to the object yet as it may be copied
				// multiple times
				Object object = fromSourceId.data.get(transferKey(transfer));
				return clone(transfer, object);
			} else {
				// Object is from outside app or otherwise needed deserializing
				// Remove our reference to it and return it
				return deserializedData.remove(g_value_get_boxed);

			}
		}

		// The transfer failed - this should be unreachable as
		// gdk_clipboard_read_value_finish and equivalents
		// should have returned 0 in this case
		return null;
	}

	/**
	 * When calling gdk_clipboard_read_value_async and related functions, if the
	 * clipboard is owned locally it returns the instance that was placed on the
	 * clipboard and avoid the serialization and deserialization.
	 *
	 * This presents a problem for SWT because SWT has always returned a copy
	 * (javaToNative/nativeToJava) of the object between the setContents and the
	 * getContents.
	 *
	 * For some objects, like String it may be ok to return the same instance since
	 * String is immutable, but for others, such as ImageData or custom types a copy
	 * definitely needs to be made to preserve the pre-existing behavior. However,
	 * to maintain compatibility with the pre-existing implementations for even
	 * String type Transfers that have returned a copy, we copy String types too in
	 * this implementation.
	 */
	private Object clone(Transfer transfer, Object object) {
		TransferData td = new TransferData();
		td.result = 0;
		int[] typeIds = transfer.getTypeIds();
		if (typeIds.length == 0 || typeIds[0] == 0) {
			return null;
		}
		// Which type id we use doesn't matter, as long as it is the same in both
		// directions
		td.type = typeIds[0];
		transfer.javaToNative(object, td);
		if (td.result == 0 || td.pValue == 0 || td.length == 0) {
			return null;
		}
		Object clonedObject = transfer.nativeToJava(td);
		OS.g_free(td.pValue);
		return clonedObject;
	}
}
