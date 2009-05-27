/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
 * The class <code>URLTransfer</code> provides a platform specific mechanism 
 * for converting text in URL format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa. The string
 * must contain a fully specified url.
 * 
 * <p>An example of a java <code>String</code> containing a URL is shown below:</p>
 * 
 * <code><pre>
 *     String url = "http://www.eclipse.org";
 * </code></pre>
 *
 * @see Transfer
 * @since 3.4
 */
public class URLTransfer extends ByteArrayTransfer {

	static URLTransfer _instance = new URLTransfer();
	static final String URL = "UniformResourceLocator"; //$NON-NLS-1$
	static final int URL_ID = registerType(URL);

private URLTransfer() {}

/**
 * Returns the singleton instance of the URLTransfer class.
 *
 * @return the singleton instance of the URLTransfer class
 */
public static URLTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts a URL
 * represented by a java <code>String</code> to a platform specific representation.
 * 
 * @param object a java <code>String</code> containing a URL
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkURL(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object + '\0';
	byte[] buffer = string.getBytes();
	if (buffer.length == 0) return;
	int typeid = OS.Byte_typeid();
	int pBytes = OS.Array_CreateInstance(typeid, buffer.length);
	OS.GCHandle_Free(typeid);
	if (pBytes == 0) return;
	OS.memcpy(pBytes, buffer, buffer.length);
	int pStream = OS.gcnew_MemoryStream();
	OS.MemoryStream_Write(pStream, pBytes, 0, buffer.length);
	OS.GCHandle_Free(pBytes);
	transferData.pValue = pStream;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform 
 * specific representation of a URL to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing a URL if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.pValue == 0) return null;

	int byteArray = 	OS.MemoryStream_ToArray(transferData.pValue);
	int bLength = OS.Array_GetLength(byteArray, 0);
	byte[] buffer = new byte[bLength];
	if (bLength == 0) return "";
	OS.memcpy(buffer, byteArray, bLength);
	OS.GCHandle_Free(byteArray);

	String string = new String(buffer);
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}

protected int[] getTypeIds(){
	return new int[] {URL_ID};
}

protected String[] getTypeNames(){
	return new String[] {URL}; 
}

boolean checkURL(Object object) {
	return object != null && (object instanceof String) && ((String)object).length() > 0;
}

protected boolean validate(Object object) {
	return checkURL(object);
}
}
