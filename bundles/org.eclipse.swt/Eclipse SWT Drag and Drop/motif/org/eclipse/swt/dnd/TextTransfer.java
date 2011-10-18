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

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.motif.OS;
import org.eclipse.swt.internal.motif.XTextProperty;

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
 * <p>Note the <code>TextTransfer</code> does not change the content of the text
 * data. For a better integration with the platform, the application should convert
 * the line delimiters used in the text data to the standard line delimiter used by the
 * platform.
 * </p>
 * 
 * @see Transfer
 */
public class TextTransfer extends ByteArrayTransfer {

	static TextTransfer _instance = new TextTransfer();
	static final String COMPOUND_TEXT = "COMPOUND_TEXT"; //$NON-NLS-1$
	static final String STRING = "STRING"; //$NON-NLS-1$
	static final int COMPOUND_TEXT_ID = registerType(COMPOUND_TEXT);
	static final int STRING_ID = registerType(STRING);

TextTransfer() {/*empty*/}

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
public void javaToNative (Object object, TransferData transferData) {
	transferData.result = 0;
	if (!checkText(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	byte[] buffer = Converter.wcsToMbcs (null, string, true);
	if (transferData.type ==  COMPOUND_TEXT_ID) {
		Display display = Display.getCurrent();
		if (display == null) return;
		int xDisplay = display.xDisplay;
		int pBuffer = OS.XtMalloc(buffer.length);
		if (pBuffer == 0) return;
		try {
			OS.memmove(pBuffer, buffer, buffer.length);
			int list = OS.XtMalloc(4);
			if (list == 0) return;
			OS.memmove(list, new int[] {pBuffer}, 4);
			XTextProperty text_prop_return = new XTextProperty();
			int result = OS.XmbTextListToTextProperty (xDisplay, list, 1, OS.XCompoundTextStyle, text_prop_return);
			OS.XtFree(list);
			if (result != 0)return;
			transferData.format = text_prop_return.format;
			transferData.length = text_prop_return.nitems;
			transferData.pValue = text_prop_return.value;
			transferData.type = text_prop_return.encoding;
			transferData.result = 1;
		} finally {
			OS.XtFree(pBuffer);
		}
	}
	if (transferData.type == STRING_ID) {
		int pValue = OS.XtMalloc(buffer.length);
		if (pValue ==  0) return;
		OS.memmove(pValue, buffer, buffer.length);
		transferData.type = STRING_ID;
		transferData.format = 8;
		transferData.length = buffer.length - 1;
		transferData.pValue = pValue;
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
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) ||  transferData.pValue == 0) return null;
	byte[] buffer = null;
	if (transferData.type == COMPOUND_TEXT_ID) {
		Display display = Display.getCurrent();
		if (display == null) return null;
		int xDisplay = display.xDisplay;
		XTextProperty text_prop = new XTextProperty();
		text_prop.encoding = transferData.type;
		text_prop.format = transferData.format;
		text_prop.nitems = transferData.length;
		text_prop.value = transferData.pValue;
		int[] list_return = new int[1];
		int[] count_return = new int[1];
		int result = OS.XmbTextPropertyToTextList (xDisplay, text_prop, list_return, count_return);
		if (result != 0 || list_return[0] == 0) return null;
		//Note: only handling the first string in list
		int[] ptr = new int[1];
		OS.memmove(ptr, list_return[0], 4);
		int length = OS.strlen(ptr[0]);
		buffer = new byte[length];
		OS.memmove(buffer, ptr[0], length);
		OS.XFreeStringList(list_return[0]);
	}
	if (transferData.type == STRING_ID) {
		int size = transferData.format * transferData.length / 8;
		if (size == 0) return null;
		buffer = new byte[size];
		OS.memmove(buffer, transferData.pValue, size);
	}
	if (buffer == null) return null;
	// convert byte array to a string
	char [] unicode = Converter.mbcsToWcs (null, buffer);
	String string = new String (unicode);
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}

protected int[] getTypeIds() {
	return new int[] {COMPOUND_TEXT_ID, STRING_ID};
}

protected String[] getTypeNames() {
	return new String[] {COMPOUND_TEXT, STRING};
}

boolean checkText(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkText(object);
}
}
