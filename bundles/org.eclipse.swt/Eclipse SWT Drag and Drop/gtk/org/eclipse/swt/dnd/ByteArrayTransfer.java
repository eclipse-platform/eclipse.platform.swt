/*******************************************************************************
 * Copyright (c) 2000, 2017 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.dnd.TransferData.*;
import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * The class <code>ByteArrayTransfer</code> provides a platform specific
 * mechanism for converting a java <code>byte[]</code> to a platform
 * specific representation of the byte array and vice versa.
 *
 * <p><code>ByteArrayTransfer</code> is never used directly but is sub-classed
 * by transfer agents that convert between data in a java format such as a
 * <code>String</code> and a platform specific byte array.
 *
 * <p>If the data you are converting <b>does not</b> map to a
 * <code>byte[]</code>, you should sub-class <code>Transfer</code> directly
 * and do your own mapping to a platform data type.</p>
 *
 * <p>The following snippet shows a subclass of ByteArrayTransfer that transfers
 * data defined by the class <code>MyType</code>.</p>
 *
 * <pre><code>
 * public class MyType {
 *	public String fileName;
 *	public long fileLength;
 *	public long lastModified;
 * }
 * </code></pre>
 *
 * <pre><code>
 * public class MyTypeTransfer extends ByteArrayTransfer {
 *
 *	private static final String MYTYPENAME = "my_type_name";
 *	private static final int MYTYPEID = registerType(MYTYPENAME);
 *	private static MyTypeTransfer _instance = new MyTypeTransfer();
 *
 * private MyTypeTransfer() {}
 *
 * public static MyTypeTransfer getInstance () {
 * 	return _instance;
 * }
 * public void javaToNative (Object object, TransferData transferData) {
 * 	if (object == null || !(object instanceof MyType[])) return;
 *
 * 	if (isSupportedType(transferData)) {
 * 		MyType[] myTypes = (MyType[]) object;
 * 		try {
 * 			// write data to a byte array and then ask super to convert to pMedium
 * 			ByteArrayOutputStream out = new ByteArrayOutputStream();
 * 			DataOutputStream writeOut = new DataOutputStream(out);
 * 			for (int i = 0, length = myTypes.length; i &lt; length;  i++){
 * 				byte[] buffer = myTypes[i].fileName.getBytes();
 * 				writeOut.writeInt(buffer.length);
 * 				writeOut.write(buffer);
 * 				writeOut.writeLong(myTypes[i].fileLength);
 * 				writeOut.writeLong(myTypes[i].lastModified);
 * 			}
 * 			byte[] buffer = out.toByteArray();
 * 			writeOut.close();
 *
 * 			super.javaToNative(buffer, transferData);
 *
 * 		} catch (IOException e) {
 * 		}
 * 	}
 * }
 * public Object nativeToJava(TransferData transferData){
 *
 * 	if (isSupportedType(transferData)) {
 *
 * 		byte[] buffer = (byte[])super.nativeToJava(transferData);
 * 		if (buffer == null) return null;
 *
 * 		MyType[] myData = new MyType[0];
 * 		try {
 * 			ByteArrayInputStream in = new ByteArrayInputStream(buffer);
 * 			DataInputStream readIn = new DataInputStream(in);
 * 			while(readIn.available() &gt; 20) {
 * 				MyType datum = new MyType();
 * 				int size = readIn.readInt();
 * 				byte[] name = new byte[size];
 * 				readIn.read(name);
 * 				datum.fileName = new String(name);
 * 				datum.fileLength = readIn.readLong();
 * 				datum.lastModified = readIn.readLong();
 * 				MyType[] newMyData = new MyType[myData.length + 1];
 * 				System.arraycopy(myData, 0, newMyData, 0, myData.length);
 * 				newMyData[myData.length] = datum;
 * 				myData = newMyData;
 * 			}
 * 			readIn.close();
 * 		} catch (IOException ex) {
 * 			return null;
 * 		}
 * 		return myData;
 * 	}
 *
 * 	return null;
 * }
 * protected String[] getTypeNames(){
 * 	return new String[]{MYTYPENAME};
 * }
 * protected int[] getTypeIds(){
 * 	return new int[] {MYTYPEID};
 * }
 * }
 * </code></pre>
 *
 * @see Transfer
 */
public abstract class ByteArrayTransfer extends Transfer {

@Override
public TransferData[] getSupportedTypes() {
	int[] types = getTypeIds();
	TransferData[] data = new TransferData[types.length];
	for (int i = 0; i < types.length; i++) {
		data[i] = new TransferData();
		data[i].type = types[i];
	}
	return data;
}

@Override
public boolean isSupportedType(TransferData transferData){
	if (GTK.GTK4) return isSupportedTypeGTK4(transferData);
	if (transferData == null) return false;
	int[] types = getTypeIds();
	for (int i = 0; i < types.length; i++) {
		if (transferData.type == types[i]) return true;
	}
	return false;
}

private boolean isSupportedTypeGTK4(TransferData transferData) {
	if (transferData == null) return false;
	int[] types = getTypeIds();
	for (int i = 0; i < types.length; i++) {
		if (transferData.gtk4().type == types[i]) return true;
	}
	return false;
}

/**
 * This implementation of <code>javaToNative</code> converts a java
 * <code>byte[]</code> to a platform specific representation.
 *
 * @param object a java <code>byte[]</code> containing the data to be converted
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 *
 * @see Transfer#nativeToJava
 */
@Override
protected void javaToNative (Object object, TransferData transferData) {
	if (GTK.GTK4) {
		javaToNativeGTK4(object, transferData);
		return;
	}
	transferData.gtk3().result = 0;
	if (!checkByteArray(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	byte[] buffer = (byte[])object;
	if (buffer.length == 0) return;
	long pValue = OS.g_malloc(buffer.length);
	if (pValue == 0) return;
	C.memmove(pValue, buffer, buffer.length);
	transferData.gtk3().length = buffer.length;
	transferData.gtk3().format = 8;
	transferData.gtk3().pValue = pValue;
	transferData.gtk3().result = 1;
}

private void javaToNativeGTK4(Object object, TransferData transferData) {
	if (!checkByteArray(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	byte[] buffer = (byte[]) object;
	if (buffer.length == 0) {
		return;
	}
	TransferDataGTK4 data = transferData.gtk4();
	GdkContentSerializer serializer = data.serializer;

	long pValue = OS.g_malloc(buffer.length);
	if (pValue == 0) {
		return;
	}
	C.memmove(pValue, buffer, buffer.length);

// Sync version - this won't work if the other end of the stream is in our process (such as a Clipboard.getContents call).
//	long[] error = new long[1];
//	boolean finish = OS.g_output_stream_write_all(serializer.output_stream(), pValue, buffer.length, null,
//			serializer.cancellable(), error);
//	if (!finish) {
//		serializer.return_error(error[0]);
//	} else {
//		serializer.return_success();
//	}
// Async version:
	new AsyncFinishUtil().run(data.display, new AsyncReadyCallback() {
		@Override
		public void async(long result) {
			OS.g_output_stream_write_all_async(serializer.output_stream(), pValue, buffer.length, serializer.priority(),
					serializer.cancellable(), result, 0);
		}

		@Override
		public long await(long result) {
			long[] error = new long[1];
			boolean finish = OS.g_output_stream_write_all_finish(serializer.output_stream(), result, null, error);
			if (!finish) {
				serializer.return_error(error[0]);
			} else {
				serializer.return_success();
			}
			OS.g_free(pValue);
			return 0;
		}
	});
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific
 * representation of a byte array to a java <code>byte[]</code>.
 *
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>byte[]</code> containing the converted data if the conversion was
 * 		successful; otherwise null
 *
 * @see Transfer#javaToNative
 */
@Override
protected Object nativeToJava(TransferData transferData) {
	if (GTK.GTK4) return nativeToJavaGTK4(transferData);
	if ( !isSupportedType(transferData) || transferData.gtk3().pValue == 0) return null;
	int size = transferData.gtk3().format * transferData.gtk3().length / 8;
	if (size == 0) return null;
	byte[] buffer = new byte[size];
	C.memmove(buffer, transferData.gtk3().pValue, size);
	return buffer;
}

private Object nativeToJavaGTK4(TransferData transferData) {
	TransferDataGTK4 data = transferData.gtk4();
	if (!isSupportedType(transferData) || data.deserializer == null)
		return null;

	GdkContentDeserializer deserializer = data.deserializer;
	long memoryStream = OS.g_memory_output_stream_new_resizable();
	System.out.println("About to run g_output_stream_splice_async");
	boolean success = new SyncFinishUtil<Boolean>().run(data.display, new SyncFinishCallback<Boolean>() {
		@Override
		public void async(long result) {
			OS.g_output_stream_splice_async(memoryStream, deserializer.input_stream(),
					OS.G_OUTPUT_STREAM_SPLICE_CLOSE_SOURCE | OS.G_OUTPUT_STREAM_SPLICE_CLOSE_TARGET,
					deserializer.priority(), deserializer.cancellable(), result, 0);
		}

		@Override
		public Boolean await(long result) {
			long[] error = new long[1];
			long spliced = OS.g_output_stream_splice_finish(memoryStream, result, null);
			if (spliced < 0) {
				deserializer.return_error(error[0]);
				return false;
			} else {
				deserializer.return_success();
				return true;
			}
		}
	});

	if (success) {
		long data_size = OS.g_memory_output_stream_get_data_size(memoryStream);
		long dataPtr = OS.g_memory_output_stream_get_data(memoryStream);
		byte[] buffer = new byte[(int)data_size];
		C.memmove(buffer, dataPtr, data_size);
		return buffer;
	}

	return null;
}

boolean checkByteArray(Object object) {
	return (object instanceof byte[] && ((byte[])object).length > 0);
}
}
