/*******************************************************************************
 * Copyright (c) 2000, 2025 IBM Corporation and others.
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

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * The class <code>HTMLTransfer</code> provides a platform specific mechanism
 * for converting text in HTML format represented as a java <code>String</code>
 * to a platform specific representation of the data and vice versa.
 *
 * <p>An example of a java <code>String</code> containing HTML text is shown
 * below:</p>
 *
 * <pre><code>
 *     String htmlData = "&lt;p&gt;This is a paragraph of text.&lt;/p&gt;";
 * </code></pre>
 *
 * @see Transfer
 */
public class HTMLTransfer extends ByteArrayTransfer {

	private static HTMLTransfer _instance = new HTMLTransfer();
	private static final String TEXT_HTML = "text/html"; //$NON-NLS-1$
	private static final int TEXT_HTML_ID = registerType(TEXT_HTML);
	private static final String TEXT_HTML2 = "TEXT/HTML"; //$NON-NLS-1$
	private static final int TEXT_HTML2_ID = registerType(TEXT_HTML2);

private HTMLTransfer() {}

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
@Override
public void javaToNative (Object object, TransferData transferData){
	transferData.result = 0;
	if (!checkHTML(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	byte[] utf8 = Converter.wcsToMbcs(string, true);
	int byteCount = utf8.length;
	long pValue = OS.g_malloc(byteCount);
	if (pValue == 0) return;
	C.memmove(pValue, utf8, byteCount);
	transferData.length = byteCount;
	transferData.format = 8;
	transferData.pValue = pValue;
	transferData.result = 1;
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
@Override
public Object nativeToJava(TransferData transferData){
	if ( !isSupportedType(transferData) ||  transferData.pValue == 0 ) return null;

	int size = (transferData.format * transferData.length / 8);
	if (size <= 0) return null;
	char[] bom = new char[1]; // look for a Byte Order Mark
	if (size > 1) C.memmove (bom, transferData.pValue, 2);
	String string;
	if (bom[0] == '\ufeff' || bom[0] == '\ufffe') {
		// XXX Follow up to Bugs 376589 384381 this is almost
		// certainly wrong as it leaves the BOM as the first
		// character in the string. This probably should be
		//
		// byte[] bytes = new byte [size];
		// C.memmove (bytes, transferData.pValue, size);
		// string = new String(bytes, StandardCharset.UTF16);
		//
		// However I cannot find any current Linux program that
		// copies text/html to the clipboard in UTF16 anymore
		// so it is probably not a big issue
		/* Ensure byteCount is a multiple of 2 bytes */
		size = size / 2 * 2;
		char[] chars = new char [size/2];
		C.memmove (chars, transferData.pValue, size);
		string = new String (chars);
	} else {
		byte[] utf8 = new byte[size];
		C.memmove(utf8, transferData.pValue, size);
		// convert utf8 byte array to a unicode string
		char [] unicode = org.eclipse.swt.internal.Converter.mbcsToWcs (utf8);
		string = new String (unicode);
	}
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}
@Override
protected int[] getTypeIds() {
	return new int[] {TEXT_HTML_ID, TEXT_HTML2_ID};
}

@Override
protected String[] getTypeNames() {
	return new String[] {TEXT_HTML, TEXT_HTML2};
}

boolean checkHTML(Object object) {
	return (object instanceof String && !((String)object).isEmpty());
}

@Override
protected boolean validate(Object object) {
	return checkHTML(object);
}
}
