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

import org.eclipse.swt.internal.*;
import org.eclipse.swt.internal.gtk.*;
 
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

	private static RTFTransfer _instance = new RTFTransfer();
	private static final String TEXT_RTF = "text/rtf"; //$NON-NLS-1$
	private static final int TEXT_RTF_ID = registerType(TEXT_RTF);
	private static final String TEXT_RTF2 = "TEXT/RTF"; //$NON-NLS-1$
	private static final int TEXT_RTF2_ID = registerType(TEXT_RTF2);
	private static final String APPLICATION_RTF = "application/rtf"; //$NON-NLS-1$
	private static final int APPLICATION_RTF_ID = registerType(APPLICATION_RTF);

private RTFTransfer() {}

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
	transferData.result = 0;
	if (!checkRTF(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	byte [] buffer = Converter.wcsToMbcs (null, string, true);
	int /*long*/ pValue = OS.g_malloc(buffer.length);
	if (pValue == 0) return;
	OS.memmove(pValue, buffer, buffer.length);
	transferData.length = buffer.length - 1;
	transferData.format = 8;
	transferData.pValue = pValue;
	transferData.result = 1;
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
	if ( !isSupportedType(transferData) ||  transferData.pValue == 0 ) return null;
	int size = transferData.format * transferData.length / 8;
	if (size == 0) return null;
	byte[] buffer = new byte[size];
	OS.memmove(buffer, transferData.pValue, size);
	char [] chars = Converter.mbcsToWcs (null, buffer);
	String string = new String (chars);
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}

protected int[] getTypeIds() {
	return new int[] {TEXT_RTF_ID, TEXT_RTF2_ID, APPLICATION_RTF_ID};
}

protected String[] getTypeNames() {
	return new String[] {TEXT_RTF, TEXT_RTF2, APPLICATION_RTF};
}

boolean checkRTF(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkRTF(object);
}
}
