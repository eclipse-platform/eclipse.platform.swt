/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;
 
import org.eclipse.swt.internal.carbon.OS;
import org.eclipse.swt.internal.carbon.CFRange;

/**
 * The class <code>RTFTransfer</code> provides a platform specific mechanism 
 * for converting text in RTF format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String</code> containing RTF text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String rtfData = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i Hello World}";
 * </code></pre>
 */
public class RTFTransfer extends ByteArrayTransfer {

	private static RTFTransfer _instance = new RTFTransfer();
	private static final String TYPENAME1 = "RTF ";
	
	private static final int TYPEID1 = registerType(TYPENAME1);

private RTFTransfer() {
}

/**
 * Returns the singleton instance of the RTFTransfer class.
 *
 * @return the singleton instance of the RTFTransfer class
 */
public static RTFTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts RTF-formatted text
 * represented by a java <code>String</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing RTF text
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
	if (object == null || !(object instanceof String)) {
		transferData.result = -1;
		return;
	}
	String string = (String)object;
	char[] chars = new char[string.length()];
	string.getChars (0, chars.length, chars, 0);
	int ptr = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
	if (ptr == 0) {
		transferData.result = -1;
		return;
	}
	CFRange range = new CFRange();
	range.length = chars.length;
	int encoding = OS.CFStringGetSystemEncoding();
	int[] size = new int[1];
	OS.CFStringGetBytes(ptr, range, encoding, (byte)'?', true, null, 0, size);
	byte[] buffer = new byte[size[0]];
	OS.CFStringGetBytes(ptr, range, encoding, (byte)'?', true, buffer, size [0], size);
	OS.CFRelease(ptr);
	super.javaToNative(buffer, transferData);
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of RTF text to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing RTF text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	byte[] buffer = (byte[])super.nativeToJava(transferData);
	if (buffer == null) return null;
	// convert byte array to a string
	int encoding = OS.CFStringGetSystemEncoding();
	int ptr = OS.CFStringCreateWithBytes(OS.kCFAllocatorDefault, buffer, buffer.length, encoding, true);
	int length = OS.CFStringGetLength(ptr);
	char[] chars = new char[length];
	CFRange range = new CFRange();
	range.length = length;
	OS.CFStringGetCharacters(ptr, range, chars);
	OS.CFRelease (ptr);
	return new String (chars);
}

protected String[] getTypeNames() {
	return new String[]{ TYPENAME1 };
}

protected int[] getTypeIds() {
	return new int[] { TYPEID1 };
}
}
