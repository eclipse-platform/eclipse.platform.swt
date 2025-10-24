/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

import org.eclipse.swt.internal.cocoa.*;


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
 * private MyTypeTransfer() {
 * }
 *
 * public static MyTypeTransfer getInstance() {
 * 	return _instance;
 * }
 *
 * &#64;Override
 * public void javaToNative(Object object, TransferData transferData) {
 * 	if (!checkMyType(object) || !isSupportedType(transferData)) {
 * 		DND.error(DND.ERROR_INVALID_DATA);
 * 	}
 *
 * 	MyType myType = (MyType) object;
 * 	// write data to a byte array and then ask super to convert to native
 * 	ByteArrayOutputStream out = new ByteArrayOutputStream();
 * 	try (DataOutputStream writeOut = new DataOutputStream(out)) {
 * 		byte[] fileNameBytes = myType.fileName.getBytes(StandardCharsets.UTF_8);
 * 		writeOut.writeInt(fileNameBytes.length);
 * 		writeOut.write(fileNameBytes);
 * 		writeOut.writeLong(myType.fileLength);
 * 		writeOut.writeLong(myType.lastModified);
 * 		super.javaToNative(out.toByteArray(), transferData);
 * 	} catch (IOException e) {
 * 	}
 * }
 *
 * &#64;Override
 * public Object nativeToJava(TransferData transferData) {
 * 	if (!isSupportedType(transferData)) {
 * 		return null;
 * 	}
 *
 * 	byte[] buffer = (byte[]) super.nativeToJava(transferData);
 * 	if (buffer == null) {
 * 		return null;
 * 	}
 *
 * 	ByteArrayInputStream in = new ByteArrayInputStream(buffer);
 * 	try (DataInputStream readIn = new DataInputStream(in)) {
 * 		MyType myType = new MyType();
 * 		int size = readIn.readInt();
 * 		byte[] name = new byte[size];
 * 		readIn.read(name);
 * 		myType.fileName = new String(name);
 * 		myType.fileLength = readIn.readLong();
 * 		myType.lastModified = readIn.readLong();
 * 		return myType;
 * 	} catch (IOException ex) {
 * 	}
 * 	return null;
 * }
 *
 * &#64;Override
 * protected String[] getTypeNames() {
 * 	return new String[] { MYTYPENAME };
 * }
 *
 * &#64;Override
 * protected int[] getTypeIds() {
 * 	return new int[] { MYTYPEID };
 * }
 *
 * private boolean checkMyType(Object object) {
 * 	return (object instanceof MyType);
 * }
 *
 * &#64;Override
 * protected boolean validate(Object object) {
 * 	return checkMyType(object);
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
	if (transferData == null) return false;
	int[] types = getTypeIds();
	for (int i = 0; i < types.length; i++) {
		if (transferData.type == types[i]) return true;
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
	if (!checkByteArray(object) && !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	byte[] orig = (byte[])object;
	NSData data = NSData.dataWithBytes(orig, orig.length);
	transferData.data = data;
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
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	if (transferData.data == null) return null;
	NSData data = (NSData) transferData.data;
	if (data.length() == 0) return null;
	byte[] bytes = new byte[(int)data.length()];
	data.getBytes(bytes);
	return bytes;
}
boolean checkByteArray(Object object) {
	return (object != null && object instanceof byte[] && ((byte[])object).length > 0);
}
}
