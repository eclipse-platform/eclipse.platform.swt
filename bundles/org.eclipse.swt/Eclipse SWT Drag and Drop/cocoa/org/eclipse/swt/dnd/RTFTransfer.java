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

import org.eclipse.swt.internal.cocoa.*;

/**
 * The class <code>RTFTransfer</code> provides a platform specific mechanism
 * for converting text in RTF format represented as a java <code>String</code>
 * to a platform specific representation of the data and vice versa.
 *
 * <p>An example of a java <code>String</code> containing RTF text is shown
 * below:</p>
 *
 * <pre><code>
 *     String rtfData = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i Hello World}";
 * </code></pre>
 *
 * @see Transfer
 */
public class RTFTransfer extends ByteArrayTransfer {

	static RTFTransfer _instance = new RTFTransfer();
	static final String RTF = OS.NSPasteboardTypeRTF.getString();
	static final int RTFID = registerType(RTF);

RTFTransfer() {}

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
@Override
public void javaToNative (Object object, TransferData transferData){
	if (!checkRTF(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	transferData.data = NSString.stringWith((String) object);
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
@Override
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	NSString string = (NSString) transferData.data;
	return string.getString();
}

@Override
protected int[] getTypeIds() {
	return new int[] {RTFID};
}

@Override
protected String[] getTypeNames() {
	return new String[] {RTF};
}

boolean checkRTF(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}

@Override
protected boolean validate(Object object) {
	return checkRTF(object);
}
}
