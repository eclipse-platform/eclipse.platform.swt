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
 
import org.eclipse.swt.internal.carbon.CFRange;
import org.eclipse.swt.internal.carbon.OS;

/**
 * The class <code>HTMLTransfer</code> provides a platform specific mechanism 
 * for converting text in HTML format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.
 * 
 * <p>An example of a java <code>String</code> containing HTML text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String htmlData = "<p>This is a paragraph of text.</p>";
 * </code></pre>
 *
 * @see Transfer
 */
public class HTMLTransfer extends ByteArrayTransfer {

	static HTMLTransfer _instance = new HTMLTransfer();
	static final String HTML = "HTML"; //$NON-NLS-1$
	static final int HTMLID = registerType(HTML);

HTMLTransfer() {}

/**
 * Returns the singleton instance of the HTMLTransfer class.
 *
 * @return the singleton instance of the HTMLTransfer class
 */
public static HTMLTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts HTML-formatted text
 * represented by a java <code>String</code> to a platform specific representation.
 * 
 * @param object a java <code>String</code> containing HTML text
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkHTML(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	char[] chars = new char[string.length()];
	string.getChars (0, chars.length, chars, 0);
	transferData.result = -1;

	int encoding = OS.CFStringGetSystemEncoding();
	int cfstring = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, chars.length);
	if (cfstring == 0) return;
	byte[] buffer = null;
	try {
		CFRange range = new CFRange();
		range.length = chars.length;
		int[] size = new int[1];
		int numChars = OS.CFStringGetBytes(cfstring, range, encoding, (byte)'?', false, null, 0, size);
		if (numChars == 0) return;
		buffer = new byte[size[0]];
		numChars = OS.CFStringGetBytes(cfstring, range, encoding, (byte)'?', false, buffer, size [0], size);
		if (numChars == 0) return;
	} finally {
		OS.CFRelease(cfstring);
	}
	transferData.data = new byte[1][];
	transferData.data[0] = buffer;
	transferData.result = OS.noErr;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of HTML text to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing HTML text if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	if (transferData.data.length == 0 || transferData.data[0].length == 0) return null;
	byte[] buffer = transferData.data[0];
	int encoding = OS.CFStringGetSystemEncoding();
	int cfstring = OS.CFStringCreateWithBytes(OS.kCFAllocatorDefault, buffer, buffer.length, encoding, false);
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
	return new int[] {HTMLID};
}

protected String[] getTypeNames() {
	return new String[] {HTML};
}

boolean checkHTML(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkHTML(object);
}
}
