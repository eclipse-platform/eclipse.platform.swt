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
 
import org.eclipse.swt.internal.carbon.OS;

/**
 * The class <code>HTMLTransfer</code> provides a platform specific mechanism 
 * for converting text in HTML format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String</code> containing HTML text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String htmlData = "<b>Hello World</b>";
 * </code></pre>
 */
public class HTMLTransfer extends ByteArrayTransfer {

	private static HTMLTransfer _instance = new HTMLTransfer();
	private static final String HTML = "HTML"; //$NON-NLS-1$
	private static final int HTMLID = registerType(HTML);

private HTMLTransfer() {}

/**
 * Returns the singleton instance of the HTMLTransfer class.
 *
 * @return the singleton instance of the HTMLTransfer class
 */
public static HTMLTransfer getInstance () {
	return _instance;
}

/**
 * This implementation of <code>javaToNative</code> converts HTML text
 * represented by a java <code>String</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing HTML text
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
	if (!_validate(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String string = (String)object;
	int count = string.length();
	char[] chars = new char[count];
	string.getChars(0, count, chars, 0);
	byte[] buffer = new byte[chars.length * 2];
	OS.memcpy(buffer, chars, buffer.length);
	transferData.data = new byte[1][];
	transferData.data[0] = buffer;
	transferData.result = OS.noErr;
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of HTML text to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing HTML text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	if (!isSupportedType(transferData) || transferData.data == null) return null;
	if (transferData.data.length == 0 || transferData.data[0].length == 0) return null;
	byte[] buffer = transferData.data[0];
	char[] chars = new char[(buffer.length + 1) / 2];
	OS.memcpy(chars, buffer, buffer.length);
	return new String(chars);
}

protected int[] getTypeIds() {
	return new int[] {HTMLID};
}

protected String[] getTypeNames() {
	return new String[] {HTML};
}

boolean _validate(Object object) {
	return (object != null && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return _validate(object);
}
}
