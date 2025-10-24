/*******************************************************************************
 * Copyright (c) 20007 IBM Corporation and others.
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

import org.eclipse.swt.internal.cocoa.*;

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
	// XXX: SWT uses the deprecated format name here,
	// the new name is "public.url" aka NSPasteboardTypeURL
	static final String URL = OS.NSURLPboardType.getString();
	static final String URL1 = OS.kUTTypeURL.getString();
	static final int URL_ID = registerType(URL);
	static final int URL_ID1 = registerType(URL1);

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
	if (!checkURL(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String url = (String)object;
	NSString nsString = NSString.stringWith(url);
	NSString escapedString = nsString.stringByAddingPercentEscapesUsingEncoding(OS.NSUTF8StringEncoding);
	transferData.data = NSURL.URLWithString(escapedString);
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
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	NSURL nsUrl = (NSURL) transferData.data;
	NSString nsString = nsUrl.absoluteString();
	nsString = nsString.stringByReplacingPercentEscapesUsingEncoding(OS.NSUTF8StringEncoding);
	return nsString.getString();
}

@Override
protected int[] getTypeIds(){
	return new int[] {URL_ID, URL_ID1};
}

@Override
protected String[] getTypeNames(){
	return new String[] {URL, URL1};
}

boolean checkURL(Object object) {
	return object != null && (object instanceof String) && ((String)object).length() > 0;
}

@Override
protected boolean validate(Object object) {
	return checkURL(object);
}
}
