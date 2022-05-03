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

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;

/**
 * The class <code>TextTransfer</code> provides a platform specific mechanism
 * for converting plain text represented as a java <code>String</code>
 * to a platform specific representation of the data and vice versa.
 *
 * <p>An example of a java <code>String</code> containing plain text is shown
 * below:</p>
 *
 * <pre><code>
 *     String textData = "Hello World";
 * </code></pre>
 *
 * <p>Note the <code>TextTransfer</code> does not change the content of the text
 * data. For a better integration with the platform, the application should convert
 * the line delimiters used in the text data to the standard line delimiter used by the
 * platform.
 * </p>
 *
 * @see Transfer
 */
public class TextTransfer extends ByteArrayTransfer {

	private static TextTransfer _instance = new TextTransfer();
	private static final String COMPOUND_TEXT = "COMPOUND_TEXT"; //$NON-NLS-1$
	private static final String UTF8_STRING = "UTF8_STRING"; //$NON-NLS-1$
	private static final String STRING = "STRING"; //$NON-NLS-1$
	private static final int COMPOUND_TEXT_ID = GTK.GTK4 ? 0 : registerType(COMPOUND_TEXT);
	private static final int UTF8_STRING_ID = GTK.GTK4 ? 0 : registerType(UTF8_STRING);
	private static final int STRING_ID = GTK.GTK4 ? 0 : registerType(STRING);

private TextTransfer() {}

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
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 *
 * @see Transfer#nativeToJava
 */
@Override
public void javaToNative (Object object, TransferData transferData) {
	transferData.result = 0;
	if (!checkText(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	byte[] utf8 = Converter.wcsToMbcs (string, true);
	if  (OS.isX11() && transferData.type ==  COMPOUND_TEXT_ID) {
		long [] encoding = new long [1];
		int[] format = new int[1];
		long [] ctext = new long [1];
		int[] length = new int[1];
		boolean result = GDK.gdk_x11_display_utf8_to_compound_text (GDK.gdk_display_get_default(), utf8, encoding, format, ctext, length);
		if (!result) return;
		transferData.type = encoding[0];
		transferData.format = format[0];
		transferData.length = length[0];
		transferData.pValue = ctext[0];
		transferData.result = 1;
	}
	if (transferData.type == UTF8_STRING_ID) {
		long pValue = OS.g_malloc(utf8.length);
		if (pValue ==  0) return;
		C.memmove(pValue, utf8, utf8.length);
		transferData.type = UTF8_STRING_ID;
		transferData.format = 8;
		transferData.length = utf8.length - 1;
		transferData.pValue = pValue;
		transferData.result = 1;
	}
	if (transferData.type == STRING_ID) {
		long string_target = GDK.gdk_utf8_to_string_target(utf8);
		if (string_target ==  0) return;
		transferData.type = STRING_ID;
		transferData.format = 8;
		transferData.length = C.strlen(string_target);
		transferData.pValue = string_target;
		transferData.result = 1;
	}
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific
 * representation of plain text to a java <code>String</code>.
 *
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing text if the conversion was successful; otherwise null
 *
 * @see Transfer#javaToNative
 */
@Override
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) ||  transferData.pValue == 0) return null;
	long [] list = new long [1];
	int count = GDK.gdk_text_property_to_utf8_list_for_display(GDK.gdk_display_get_default(), transferData.type, transferData.format, transferData.pValue, transferData.length, list);
	if (count == 0) return null;
	long [] ptr = new long [1];
	C.memmove(ptr, list[0], C.PTR_SIZEOF);
	int length = C.strlen(ptr[0]);
	byte[] utf8 = new byte[length];
	C.memmove(utf8, ptr[0], length);
	OS.g_strfreev(list[0]);
	// convert utf8 byte array to a unicode string
	char [] unicode = Converter.mbcsToWcs (utf8);
	String string = new String (unicode);
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}

@Override
protected int[] getTypeIds() {
	if (OS.isX11()) {
		return new int[] {UTF8_STRING_ID, COMPOUND_TEXT_ID, STRING_ID};
	}
	if(GTK.GTK4) {
		return new int[] {(int) OS.G_TYPE_STRING()};
	}
	return new int[] {UTF8_STRING_ID, STRING_ID};
}

@Override
protected String[] getTypeNames() {
	if (OS.isX11()) {
		return new String[] {UTF8_STRING, COMPOUND_TEXT, STRING};
	}
	if(GTK.GTK4) {
		return new String[] {STRING};
	}

	return new String[] {UTF8_STRING, STRING};
}

boolean checkText(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}

@Override
protected boolean validate(Object object) {
	return checkText(object);
}
}
