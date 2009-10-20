/*******************************************************************************
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.motif.*;
 
/**
 * The class <code>URLTransfer</code> provides a platform specific mechanism 
 * for converting text in URL format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa. The string
 * must contain a fully specified url.
 * 
 * <p>An example of a java <code>String</code> containing a URL is shown below:</p>
 * 
 * <code><pre>
 *     String url = "http://www.eclipse.org";
 * </code></pre>
 *
 * @see Transfer
 * @since 3.4
 */
public class URLTransfer extends ByteArrayTransfer {

	static URLTransfer _instance = new URLTransfer();
	
	static final String TEXT_UNICODE = "text/unicode"; //$NON-NLS-1$
	private static final String TEXT_XMOZURL = "text/x-moz-url"; //$NON-NLS-1$
	static final int TEXT_UNICODE_ID = registerType(TEXT_UNICODE);
	private static final int TEXT_XMOZURL_ID = registerType(TEXT_XMOZURL);	

private URLTransfer() {}

/**
 * Returns the singleton instance of the URLTransfer class.
 *
 * @return the singleton instance of the URLTransfer class
 */
public static URLTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts a URL
 * represented by a java <code>String</code> to a platform specific representation.
 * 
 * @param object a java <code>String</code> containing a URL
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */
public void javaToNative (Object object, TransferData transferData){
	transferData.result = 0;
	if (!checkURL(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	int charCount = string.length();
	char [] chars = new char[charCount +1];
	string.getChars(0, charCount , chars, 0);
	int byteCount = chars.length*2;
	int pValue = OS.XtMalloc(byteCount);
	if (pValue == 0) return;
	OS.memmove(pValue, chars, byteCount);
	transferData.length = byteCount;
	transferData.format = 8;
	transferData.pValue = pValue;
	transferData.result = 1;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform 
 * specific representation of a URL to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing a URL if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */
public Object nativeToJava(TransferData transferData){
	if ( !isSupportedType(transferData) ||  transferData.pValue == 0 ) return null;
	/* Ensure byteCount is a multiple of 2 bytes */
	int size = (transferData.format * transferData.length / 8) / 2 * 2;
	if (size <= 0) return null;			
	char[] chars = new char [size/2];
	OS.memmove (chars, transferData.pValue, size);
	String string = new String (chars);
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}

protected int[] getTypeIds(){
	return new int[] {TEXT_XMOZURL_ID, TEXT_UNICODE_ID};
}

protected String[] getTypeNames(){
	return new String[] {TEXT_XMOZURL, TEXT_UNICODE};
}

boolean checkURL(Object object) {
	return object != null && (object instanceof String) && ((String)object).length() > 0;
}

protected boolean validate(Object object) {
	return checkURL(object);
}
}
