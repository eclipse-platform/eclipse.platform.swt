/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.wpf.OS;


/**
 * The class <code>ByteArrayTransfer</code> provides a platform specific 
 * mechanism for converting a java <code>byte[]</code> to a platform 
 * specific representation of the byte array and vice versa.  See 
 * <code>Transfer</code> for additional information.
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
 * 			while(readIn.available() > 20) {
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
 */
public abstract class ByteArrayTransfer extends Transfer {


int createDotNetString (String string) {
	if (string == null) return 0;
	int length = string.length();
	char[] buffer = new char[length + 1];
	string.getChars(0, length, buffer, 0);
	return OS.gcnew_String(buffer);
}

String createJavaString (int ptr) {
	int charArray = OS.String_ToCharArray(ptr);
	char[] chars = new char[OS.String_Length(ptr)];
	OS.memcpy(chars, charArray, chars.length * 2);
	OS.GCHandle_Free(charArray);
	return new String(chars);
}
	
public TransferData[] getSupportedTypes(){
	int[] ids = getTypeIds();
	TransferData[] result = new TransferData[ids.length];
	for (int i = 0; i < result.length; i++) {
		result[i] = new TransferData();
		result[i].format = ids[i];
	}
	return result;
}


protected String[] getTypeNames(){
	int[] ids = getTypeIds();
	String[] result = new String[ids.length];
	for (int i = 0; i < result.length; i++) {
		result[i] = createJavaString(ids[i]);
	}
	return result;
}


public boolean isSupportedType(TransferData transferData){
	if (transferData == null) return false;
	int[] ids = getTypeIds();
	for (int i = 0; i < ids.length; i++) {
		if (transferData.format == ids[i]) return true;
	}
	return false;
}

boolean checkText(Object object) {
	return object != null && object instanceof String && ((String)object).length() > 0;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a byte array to a java <code>byte[]</code>.   
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @see Transfer#nativeToJava
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>byte[]</code> containing the converted data if the 
 * conversion was successful; otherwise null
 */
protected Object nativeToJava(TransferData transferData) {
	return createJavaString(transferData.pValue);
}

/**
 * This implementation of <code>javaToNative</code> converts a java 
 * <code>byte[]</code> to a platform specific representation.  For additional
 * information see <code>Transfer#javaToNative</code>.
 * 
 * @see Transfer#javaToNative
 * 
 * @param object a java <code>byte[]</code> containing the data to be converted
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
protected void javaToNative (Object object, TransferData transferData) {
	//TEMPORARY CODE FAIL WITHOUT EXCEPTION
	if (!checkText(object)) return;
	//if (!checkText(object)) DND.error(DND.ERROR_INVALID_DATA);
	
	if (isSupportedType(transferData)) {
		transferData.pValue = createDotNetString((String)object);
	}
}

}
