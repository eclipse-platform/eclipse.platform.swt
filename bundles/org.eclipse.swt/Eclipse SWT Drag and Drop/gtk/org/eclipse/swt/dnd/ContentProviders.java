/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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
import org.eclipse.swt.internal.gtk.*;
import org.eclipse.swt.internal.gtk4.*;
import org.eclipse.swt.widgets.*;

/**
 * Manages <a href=
 * "https://docs.gtk.org/gdk4/class.ContentProvider.html">GdkContentProviders</a>
 * and the (de)serializers they rely on
 */
class ContentProviders {
	public static enum CLIPBOARD_TYPE {
		CLIPBOARD(1), PRIMARYCLIPBOARD(2), DRAG(3);

		private final long sourceId;
		private final Map<String, Object> data = new HashMap<>();
		private Display display = null;

		CLIPBOARD_TYPE(long id) {
			this.sourceId = id;
		}

		/**
		 * Return the DESTINATION instance matching the clipboards using the constants
		 * {@link DND#CLIPBOARD} and {@link DND#SELECTION_CLIPBOARD}
		 */
		public static CLIPBOARD_TYPE fromDNDConstants(int clipboards) {
			if (clipboards == DND.CLIPBOARD)
				return CLIPBOARD;
			if (clipboards == DND.SELECTION_CLIPBOARD)
				return PRIMARYCLIPBOARD;
			// the clipboards should have been error checked for validity before entering
			// this method
			throw new RuntimeException("Error - invalid clipboards");
		}

		/**
		 * Returns the DESTINATION matching the given sourceId
		 */
		public static CLIPBOARD_TYPE fromSourceId(long sourceId) {
			CLIPBOARD_TYPE[] values = CLIPBOARD_TYPE.values();
			for (CLIPBOARD_TYPE destination : values) {
				if (destination.sourceId == sourceId) {
					return destination;
				}
			}
			return null;
		}
	}

	/**
	 * The life-time of the serializers in GTK4 (Gdk) are the life-time of the
	 * process so we use a singleton to store the mapping data that is needed.
	 */
	private static ContentProviders instance = new ContentProviders();

	/*
	 * The types registered with {@link #registerType(String)} on GTK4. Using
	 * TreeMap to make the maps sorted to ease debugging
	 */
	final private Map<Integer, String> ID_TO_CONTENTTYPE = new TreeMap<>();
	final private Map<String, Integer> CONTENTTYPE_TO_ID = new TreeMap<>();
	// start at 1 because 0 is the null formatName
	private int typeIndex = 1;

	// These normally need disposal, but since this class is a singleton it isn't disposed.
	Callback gdkContentSerializeFunc;
	Callback gdkContentDeserializeFunc;

	/**
	 * All transfers registered with the GTK serializers
	 *
	 * Key is the transfer obtained with {@link #transferKey(Transfer)}
	 */
	private Map<String, Transfer> registeredTransfers = new TreeMap<>();

	private Object lastDeserializedObject;

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

	public long createContentProviders(Object[] data, Transfer[] transfers, CLIPBOARD_TYPE id, Display display) {
		id.data.clear();
		id.display = display;
		long[] providers = new long[transfers.length];
		for (int i = 0; i < transfers.length; i++) {
			Transfer transfer = transfers[i];
			registerTransfer(transfer);

			// gvalue represents a data + transfer pair, with the
			// gtype we can extract the transfer type, and that
			// plus id can extract the data
			long gvalue = OS.create_gvalue(transfer.gtype, id.sourceId);
			// TODO free gvalue (probably when we free the union of providers, see TODO below)
			providers[i] = GTK4.gdk_content_provider_new_for_value(gvalue);
			id.data.put(transferKey(transfer), data[i]);
		}

		// TODO free union of providers (note that gdk_content_provider_new_union takes
		// ownership of providers array)
		return GTK4.gdk_content_provider_new_union(providers, providers.length);
	}

	void gdkContentSerializeFunc(long pSerializer) {
		GdkContentSerializer serializer = new GdkContentSerializer(pSerializer);

		Transfer transfer = getTransfer(serializer);
		CLIPBOARD_TYPE fromSourceId = CLIPBOARD_TYPE.fromSourceId(serializer.source_id());
		if (fromSourceId == null) {
			// TODO failure here - where did this data come from? How are we asked to
			// serialize data
			// we didn't start with
			// TODO make a GError like this??? or throw exception? Is this only a
			// programming error?
//			  GError *error = g_error_new (G_IO_ERROR,
//                    G_IO_ERROR_NOT_FOUND,
//                    "Could not convert data from %s to %s",
//                    g_type_name (gdk_content_serializer_get_gtype (serializer)),
//                    gdk_content_serializer_get_mime_type (serializer));
//			serializer.return_error(error);
			throw new RuntimeException("this should be unreachable??? - see TODOs above this line");
		}
		Object object = fromSourceId.data.get(transferKey(transfer));
		Display display = fromSourceId.display;
		String mime_type = serializer.mime_type();
		Integer typeObject = CONTENTTYPE_TO_ID.get(mime_type);
		int type = typeObject == null ? 0 : typeObject;
		// make a transfer data and call
		TransferData transferData = new TransferData();
		transferData.gtk4().display = display;
		transferData.gtk4().serializer = serializer;
		transferData.gtk4().type = type;
		transfer.javaToNative(object, transferData);
	}

	private Transfer getTransfer(GdkContentSerializer serializer) {
		long gtype = serializer.gtype();
		long namePtr = OS.g_type_name(gtype);
		String transferName = Converter.cCharPtrToJavaString(namePtr, false);
		Transfer transfer = registeredTransfers.get(transferName);
		if (transfer == null) {
// TODO make a GError like this??? or throw exception? Is this only a programming error?
//			  GError *error = g_error_new (G_IO_ERROR,
//                      G_IO_ERROR_NOT_FOUND,
//                      "Could not convert data from %s to %s",
//                      g_type_name (gdk_content_serializer_get_gtype (serializer)),
//                      gdk_content_serializer_get_mime_type (serializer));
//			  GTK4.gdk_content_serializer_return_error(serializer, namePtr);
			throw new RuntimeException("Could not convert data because transfer of id " + transferName + " missing");
		}
		return transfer;
	}

	void gdkContentDeserializeFunc(long pDeserializer) {
		System.out.println("gdkContentDeserializeFunc");
		GdkContentDeserializer deserializer = new GdkContentDeserializer(pDeserializer);

		Transfer transfer = getTransfer(deserializer);
		String mime_type = deserializer.mime_type();
		Integer typeObject = CONTENTTYPE_TO_ID.get(mime_type);
		int type = typeObject == null ? 0 : typeObject;
		// make a transfer data and call
		TransferData transferData = new TransferData();
		transferData.gtk4().display = Display.getCurrent(); // TODO where to get display from? Is this OK?
		transferData.gtk4().deserializer = deserializer;
		transferData.gtk4().type = type;
		Object object = transfer.nativeToJava(transferData);
		// TODO save object better (see TODO where lastDeserializedObject is read)
		this.lastDeserializedObject = object;
	}

	private Transfer getTransfer(GdkContentDeserializer deserializer) {
		long gtype = deserializer.gtype();
		long namePtr = OS.g_type_name(gtype);
		String transferName = Converter.cCharPtrToJavaString(namePtr, false);
		Transfer transfer = registeredTransfers.get(transferName);
		if (transfer == null) {
// TODO make a GError like this??? or throw exception? Is this only a programming error?
//			  GError *error = g_error_new (G_IO_ERROR,
//                      G_IO_ERROR_NOT_FOUND,
//                      "Could not convert data from %s to %s",
//                      g_type_name (gdk_content_serializer_get_gtype (serializer)),
//                      gdk_content_serializer_get_mime_type (serializer));
//			  GTK4.gdk_content_serializer_return_error(serializer, namePtr);
			throw new RuntimeException("Could not convert data because transfer of id " + transferName + " missing");
		}
		return transfer;
	}

	private void registerTransfer(Transfer transfer) {
		String name = transferKey(transfer);
		if (registeredTransfers.containsKey(name)) {
			return;
		}
		registeredTransfers.put(name, transfer);
		transfer.gtype = OS.register_gtype_for_name(name);
		String[] mimeTypes = transfer.getTypeNames();
		for (int i = 0; i < mimeTypes.length; i++) {
			String mimeType = mimeTypes[i];
			GTK4.gdk_content_register_serializer(transfer.gtype, mimeType, gdkContentSerializeFunc.getAddress(), 0, 0);
			GTK4.gdk_content_register_deserializer(mimeType, transfer.gtype, gdkContentDeserializeFunc.getAddress(), 0,
					0);
		}
	}

	private String transferKey(Transfer transfer) {
		// The value of the name here is used to help debugging the code.
		// The only important thing is that the name is unique which is achieved by
		// appending transfer.id
		return "SWTTransfer_" + transfer.getClass().getSimpleName() + "_" + transfer.id;
	}

	public long getGType(Transfer transfer) {
		registerTransfer(transfer);
		return transfer.gtype;
	}

	public Object getObject(long gvalue) {

		long namePtr = OS.G_VALUE_TYPE_NAME(gvalue);
		String transferName = Converter.cCharPtrToJavaString(namePtr, false);
		Transfer transfer = registeredTransfers.get(transferName);
		if (transfer != null) {
			long g_value_get_boxed = OS.g_value_get_boxed(gvalue);
			CLIPBOARD_TYPE fromSourceId = CLIPBOARD_TYPE.fromSourceId(g_value_get_boxed);
			if (fromSourceId != null) {
				Object object = fromSourceId.data.get(transferKey(transfer));
				return object;
			}
		}

		// TODO support multiple deserialized objects - we should really be able to
		// align a
		// gvalue in GTK memory to a specific Java object here. For now return the last
		// thing deserialized
		return lastDeserializedObject;
	}

//	private long setProviderFromType(String string, Object data) {
//		long provider = 0;
//
//		if (data == null)
//			SWT.error(SWT.ERROR_NULL_ARGUMENT);
//		else {
//			if (string.equals("text/plain") || string.equals("text/rtf")) {
//				long value = OS.g_malloc(OS.GValue_sizeof());
//				C.memset(value, 0, OS.GValue_sizeof());
//				OS.g_value_init(value, OS.G_TYPE_STRING());
//				OS.g_value_set_string(value, Converter.javaStringToCString((String) data));
//				provider = GTK4.gdk_content_provider_new_for_value(value);
//			}
//			if (string.equals("PIXBUF")) {
//				if (!(data instanceof ImageData))
//					DND.error(DND.ERROR_INVALID_DATA);
//				ImageData imgData = (ImageData) data;
//				Image image = new Image(Display.getCurrent(), imgData);
//				long pixbuf = ImageList.createPixbuf(image);
//				if (pixbuf != 0) {
//					provider = GTK4.gdk_content_provider_new_typed(GDK.GDK_TYPE_PIXBUF(), pixbuf);
//				}
//				image.dispose();
//			}
//			if (string.equals("text/html")) {
//				long value = OS.g_malloc(OS.GValue_sizeof());
//				C.memset(value, 0, OS.GValue_sizeof());
//				OS.g_value_init(value, OS.G_TYPE_STRING());
//				OS.g_value_set_string(value, Converter.javaStringToCString((String) data));
//				provider = GTK4.gdk_content_provider_new_for_value(value);
//			}
//
//		}
//		return provider;
//	}

}
