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

import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.OS;
 
/**
 * The class <code>TextTransfer</code> provides a platform specific mechanism 
 * for converting plain text represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String</code> containing plain text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String textData = "Hello World";
 * </code></pre>
 */
public class TextTransfer extends ByteArrayTransfer {

	private static TextTransfer _instance = new TextTransfer();
	private static final String TEXT = "TEXT";
	private static final String UTEXT = "utxt";
	private static final int TEXTID = OS.kScrapFlavorTypeText;
	private static final int UTEXTID = OS.kScrapFlavorTypeUnicode;

private TextTransfer() {
}
/**
 * Returns the singleton instance of the TextTransfer class.
 *
 * @return the singleton instance of the TextTransfer class
 */
public static TextTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts plain text
 * represented by a java <code>String</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing text
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
	switch (transferData.type) {
		case TEXTID: {
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
			break;
		}
		case UTEXTID: {
			byte[] buffer = new byte[chars.length * 2];
			OS.memcpy(buffer, chars, buffer.length);
			super.javaToNative(buffer, transferData);
			break;
		}
	}
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of plain text to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	// get byte array from super
	byte[] buffer = (byte[])super.nativeToJava(transferData);
	if (buffer == null) return null;
	if (transferData.type == TEXTID) {
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
	if (transferData.type == UTEXTID) {
		char[] chars = new char[(transferData.data.length + 1) / 2];
		OS.memcpy(chars, transferData.data, transferData.data.length);
		return new String(chars);
	}
	return null;
}

protected String[] getTypeNames() {
	return new String[] {UTEXT, TEXT};
}

protected int[] getTypeIds() {
	return new int[] {UTEXTID, TEXTID};
}
}
