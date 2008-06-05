/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * to a platform specific representation of the data and vice versa.
 * 
 * <p>An example of a java <code>String</code> containing RTF text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String rtfData = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i Hello World}";
 * </code></pre>
 *
 * @see Transfer
 */
public class RTFTransfer extends ByteArrayTransfer {

	static RTFTransfer _instance = new RTFTransfer();
	static final String RTF = "RTF "; //$NON-NLS-1$
	static final int RTFID = registerType(RTF);

RTFTransfer() {}

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
 * 
 * @param object a java <code>String</code> containing RTF text
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkRTF(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	transferData.result = -1;
	String string = (String)object;
	int count = string.length();
	char[] chars = new char[count];
	string.getChars(0, count, chars, 0);
	int cfstring = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, count);
	if (cfstring == 0) return;
	try {
		CFRange range = new CFRange();
		range.length = chars.length;
		int encoding = OS.CFStringGetSystemEncoding();
		int[] size = new int[1];
		int numChars = OS.CFStringGetBytes(cfstring, range, encoding, (byte)'?', true, null, 0, size);
		if (numChars == 0 || size[0] == 0) return;
		byte[] buffer = new byte[size[0]];
		numChars = OS.CFStringGetBytes(cfstring, range, encoding, (byte)'?', true, buffer, size [0], size);
		if (numChars == 0) return;
		transferData.data = new byte[1][];
		transferData.data[0] = buffer;
		transferData.result = 0;
	} finally {
		OS.CFRelease(cfstring);
	}
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of RTF text to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing RTF text if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	if (transferData.data.length == 0 || transferData.data[0].length == 0) return null;
	byte[] buffer = transferData.data[0];
	int encoding = OS.CFStringGetSystemEncoding();
	int cfstring = OS.CFStringCreateWithBytes(OS.kCFAllocatorDefault, buffer, buffer.length, encoding, true);
	if (cfstring == 0) return null;
	try {
		int length = OS.CFStringGetLength(cfstring);
		if (length == 0) return null;
		char[] chars = new char[length];
		CFRange range = new CFRange();
		range.length = length;
		OS.CFStringGetCharacters(cfstring, range, chars);
		return new String(chars);
	} finally {
		OS.CFRelease(cfstring);
	}
}

protected int[] getTypeIds() {
	return new int[] {RTFID};
}

protected String[] getTypeNames() {
	return new String[] {RTF};
}

boolean checkRTF(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkRTF(object);
}
}
