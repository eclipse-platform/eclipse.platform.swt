/*******************************************************************************
 * Copyright (c) 2007, 2017 IBM Corporation and others.
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
 * The class <code>URLTransfer</code> provides a platform specific mechanism
 * for converting text in URL format represented as a java <code>String</code>
 * to a platform specific representation of the data and vice versa. The string
 * must contain a fully specified url.
 *
 * <p>An example of a java <code>String</code> containing a URL is shown below:</p>
 *
 * <pre><code>
 *     String url = "http://www.eclipse.org";
 * </code></pre>
 *
 * @see Transfer
 * @since 3.4
 */
public class URLTransfer extends ByteArrayTransfer {

	static URLTransfer _instance = new URLTransfer();
	private static final String TEXT_UNICODE = "text/unicode"; //$NON-NLS-1$
	private static final String TEXT_XMOZURL = "text/x-moz-url"; //$NON-NLS-1$
	private static final int TEXT_UNICODE_ID = registerType(TEXT_UNICODE);
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
@Override
public void javaToNative (Object object, TransferData transferData){
	transferData.gtk3().result = 0;
	if (!checkURL(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	int charCount = string.length();
	char [] chars = new char[charCount +1];
	string.getChars(0, charCount , chars, 0);
	int byteCount = chars.length*2;
	long pValue = OS.g_malloc(byteCount);
	if (pValue == 0) return;
	C.memmove(pValue, chars, byteCount);
	transferData.gtk3().length = byteCount;
	transferData.gtk3().format = 8;
	transferData.gtk3().pValue = pValue;
	transferData.gtk3().result = 1;
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
@Override
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) ||  transferData.gtk3().pValue == 0) return null;
	/* Ensure byteCount is a multiple of 2 bytes */
	int size = (transferData.gtk3().format * transferData.gtk3().length / 8) / 2 * 2;
	if (size <= 0) return null;
	char[] chars = new char [size/2];
	C.memmove (chars, transferData.gtk3().pValue, size);
	String string = new String (chars);
	int end = string.indexOf('\0');
	return (end == -1) ? string : string.substring(0, end);
}

@Override
protected int[] getTypeIds(){
	return new int[] {TEXT_XMOZURL_ID, TEXT_UNICODE_ID};
}

@Override
protected String[] getTypeNames(){
	return new String[] {TEXT_XMOZURL, TEXT_UNICODE};
}

boolean checkURL(Object object) {
	return object != null && (object instanceof String) && ((String)object).length() > 0;
}

@Override
protected boolean validate(Object object) {
	return checkURL(object);
}
}
