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


import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.motif.OS;
import org.eclipse.swt.internal.motif.XTextProperty;
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
	byte[] buffer = Converter.wcsToMbcs (null, (String)object, true);
	if (transferData.type == TYPEID1) { // COMPOUND_TEXT
		Display display = Display.getCurrent();
		if (display == null) {
			transferData.result = 0;
			return;
		}
		int xDisplay = display.xDisplay;
		int pBuffer = OS.XtMalloc(buffer.length);
		OS.memmove(pBuffer, buffer, buffer.length);
		int list = OS.XtMalloc(4);
		OS.memmove(list, new int[] {pBuffer}, 4);
		XTextProperty text_prop_return = new XTextProperty();
		int result = OS.XmbTextListToTextProperty (xDisplay, list, 1, OS.XCompoundTextStyle, text_prop_return);
		OS.XtFree(pBuffer);
		OS.XtFree(list);
		if (result != 0){
			transferData.result = 0;
		} else {	
			transferData.format = text_prop_return.format;
			transferData.length = text_prop_return.nitems;
			transferData.pValue = text_prop_return.value;
			transferData.type = text_prop_return.encoding;
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
	// get byte array from super
	byte[] buffer = null;
	if (transferData.type == TYPEID1){ //COMPOUND_TEXT
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
		OS.memmove(buffer, ptr[0], buffer.length);
		OS.XFreeStringList(list_return[0]);
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
	return new String[]{TYPENAME1, TYPENAME2,};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID1, TYPEID2,};
}
}
