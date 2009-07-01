/******************************************************************************* * Copyright (c) 20007 IBM Corporation and others. * All rights reserved. This program and the accompanying materials * are made available under the terms of the Eclipse Public License v1.0 * which accompanies this distribution, and is available at * http://www.eclipse.org/legal/epl-v10.html * * Contributors: *     IBM Corporation - initial API and implementation *******************************************************************************/package org.eclipse.swt.dnd;import org.eclipse.swt.internal.carbon.*;/**
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
 */public class URLTransfer extends ByteArrayTransfer {	static URLTransfer _instance = new URLTransfer();	static final String URL = "url "; //$NON-NLS-1$	static final int URL_ID = registerType(URL);	static final String URLN = "urln"; //$NON-NLS-1$	static final int URLN_ID = registerType(URLN);private URLTransfer() {}/**
 * Returns the singleton instance of the URLTransfer class.
 *
 * @return the singleton instance of the URLTransfer class
 */public static URLTransfer getInstance () {	return _instance;}/**
 * This implementation of <code>javaToNative</code> converts a URL
 * represented by a java <code>String</code> to a platform specific representation.
 * 
 * @param object a java <code>String</code> containing a URL
 * @param transferData an empty <code>TransferData</code> object that will
 *  	be filled in on return with the platform specific format of the data
 * 
 * @see Transfer#nativeToJava
 */public void javaToNative (Object object, TransferData transferData){	if (!checkURL(object) || !isSupportedType(transferData)) {		DND.error(DND.ERROR_INVALID_DATA);	}	transferData.result = -1;	String url = (String)object;	int count = url.length();	char[] chars = new char[count];	url.getChars(0, count, chars, 0);	int cfstring = OS.CFStringCreateWithCharacters(OS.kCFAllocatorDefault, chars, count);	if (cfstring == 0) return;	try {		CFRange range = new CFRange();		range.length = chars.length;		int encoding = OS.kCFStringEncodingUTF8;		int[] size = new int[1];		int numChars = OS.CFStringGetBytes(cfstring, range, encoding, (byte)'?', true, null, 0, size);		if (numChars == 0 || size[0] == 0) return;		byte[] buffer = new byte[size[0]];		numChars = OS.CFStringGetBytes(cfstring, range, encoding, (byte)'?', true, buffer, size [0], size);		if (numChars == 0) return;		transferData.data = new byte[][] {buffer};		transferData.result = 0;	} finally {		OS.CFRelease(cfstring);	}}/**
 * This implementation of <code>nativeToJava</code> converts a platform 
 * specific representation of a URL to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing a URL if the conversion was successful;
 * 		otherwise null
 * 
 * @see Transfer#javaToNative
 */public Object nativeToJava(TransferData transferData){	if (!isSupportedType(transferData) || transferData.data == null) return null;	if (transferData.data.length == 0) return null;	byte[] buffer = transferData.data[0];	int encoding = OS.kCFStringEncodingUTF8;	int cfstring = OS.CFStringCreateWithBytes(OS.kCFAllocatorDefault, buffer, buffer.length, encoding, true);	if (cfstring == 0) return null;	char[] unescapedChars = new char[] {'%'};	int unescapedStr = OS.CFStringCreateWithCharacters(0, unescapedChars, unescapedChars.length);	int str = OS.CFURLCreateStringByReplacingPercentEscapes(OS.kCFAllocatorDefault, cfstring, unescapedStr);	OS.CFRelease(unescapedStr);	OS.CFRelease(cfstring);	if (str == 0) return null;	try {		int length = OS.CFStringGetLength(str);		if (length == 0) return null;		char[] chars = new char[length];		CFRange range = new CFRange();		range.length = length;		OS.CFStringGetCharacters(str, range, chars);		return new String(chars);	} finally {		OS.CFRelease(str);	}}protected int[] getTypeIds(){	return new int[] {URL_ID, URLN_ID};}protected String[] getTypeNames(){	return new String[] {URL, URLN}; }boolean checkURL(Object object) {	return object != null && (object instanceof String) && ((String)object).length() > 0;}protected boolean validate(Object object) {	return checkURL(object);}}