package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.gtk.OS;

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
	private static final String TYPENAME1 = "COMPOUND_TEXT";
	private static final int TYPEID1 = registerType(TYPENAME1);
	private static final String TYPENAME2 = "STRING";
	private static final int TYPEID2 = registerType(TYPENAME2);

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
	if (object == null || !(object instanceof String)) return;
	byte [] buffer = Converter.wcsToMbcs (null, (String)object, true);
	if  (transferData.type ==  TYPEID1) { // COMPOUND_TEXT
		int[] encoding = new int[1];
		int[] format = new int[1];
		int[] ctext = new int[1];
		int[] length = new int[1];
		boolean result = OS.gdk_utf8_to_compound_text(buffer, encoding, format, ctext, length);
		if (!result) {
			transferData.result = 0;
		} else {
			transferData.type = encoding[0];
			transferData.format = format[0];
			transferData.length = length[0];
			transferData.pValue = ctext[0];
			transferData.result = 1;
		}
	} else {
		super.javaToNative(buffer, transferData);
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
	byte[] buffer = null;
	if (transferData.type == TYPEID1) { // COMPOUND_TEXT
		int[] list = new int[1];
		int count = OS.gdk_text_property_to_utf8_list(transferData.type, transferData.format, transferData.pValue, transferData.length, list);
		if (count == 0) {
			transferData.result = 0;
		} else {
			int[] ptr = new int[1];
			OS.memmove(ptr, list[0], 4);
			int length = OS.g_utf8_strlen(ptr[0], -1) * 8;
			buffer = new byte[length];
			OS.memmove(buffer, ptr[0], length);
			OS.g_strfreev(list[0]);
		}
	} else {
		buffer = (byte[])super.nativeToJava(transferData);
	}
	if (buffer == null) return null;
	// convert byte array to a string
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	String string = new String (unicode);
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME1, TYPENAME2};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID1, TYPEID2};
}
}
