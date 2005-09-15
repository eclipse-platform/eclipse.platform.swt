/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
 * to a platform specific representation of the data and vice versa.
 * 
 * <p>An example of a java <code>String</code> containing plain text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String textData = "Hello World";
 * </code></pre>
 * 
 * @see Transfer
 */
public class TextTransfer extends ByteArrayTransfer {

	static TextTransfer _instance = new TextTransfer();
	static final String TEXT = "TEXT"; //$NON-NLS-1$
	static final String UTEXT = "utxt"; //$NON-NLS-1$
	static final int TEXTID = OS.kScrapFlavorTypeText;
	static final int UTEXTID = OS.kScrapFlavorTypeUnicode;

TextTransfer() {}

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
 * 
 * @param object a java <code>String</code> containing text
 * @param transferData an empty <code>TransferData</code> object; this object
 *  will be filled in on return with the platform specific format of the data
 *  
 * @see Transfer#javaToNative
 */
public void javaToNative (Object object, TransferData transferData) {
	if (!checkText(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	char[] chars = new char[string.length()];
	string.getChars (0, chars.length, chars, 0);
	transferData.result = -1;
	switch (transferData.type) {
		case TEXTID: {
			int cfstring = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
			if (cfstring == 0) return;
			byte[] buffer = null;
			try {
				CFRange range = new CFRange();
				range.length = chars.length;
				int encoding = OS.CFStringGetSystemEncoding();
				int[] size = new int[1];
				int numChars = OS.CFStringGetBytes(cfstring, range, encoding, (byte)'?', true, null, 0, size);
				if (numChars == 0) return;
				buffer = new byte[size[0]];
				numChars = OS.CFStringGetBytes(cfstring, range, encoding, (byte)'?', true, buffer, size [0], size);
				if (numChars == 0) return;
			} finally {
				OS.CFRelease(cfstring);
			}
			transferData.data = new byte[1][];
			transferData.data[0] = buffer;
			transferData.result = OS.noErr;
			break;
		}
		case UTEXTID: {
			byte[] buffer = new byte[chars.length * 2];
			OS.memcpy(buffer, chars, buffer.length);
			transferData.data = new byte[1][];
			transferData.data[0] = buffer;
			transferData.result = OS.noErr;
			break;
		}
	}
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of plain text to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing text if the conversion was successful; otherwise null
 * 
 * @see Transfer#nativeToJava
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	if (transferData.data.length == 0 || transferData.data[0].length == 0) return null;
	byte[] buffer = transferData.data[0];
	switch (transferData.type) {
		case TEXTID: {
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
		case UTEXTID: {
			char[] chars = new char[(buffer.length + 1) / 2];
			OS.memcpy(chars, buffer, buffer.length);
			return new String(chars);
		}
	}
	return null;
}

protected int[] getTypeIds() {
	return new int[] {UTEXTID, TEXTID};
}

protected String[] getTypeNames() {
	return new String[] {UTEXT, TEXT};
}

boolean checkText(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}
protected boolean validate(Object object) {
	return checkText(object);
}
}
