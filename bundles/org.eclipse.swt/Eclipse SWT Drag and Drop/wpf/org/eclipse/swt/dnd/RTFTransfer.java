/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.wpf.OS;

 
/**
 * The class <code>RTFTransfer</code> provides a platform specific mechanism 
 * for converting text in RTF format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String</code> containing RTF text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String rtfData = "{\\rtf1{\\colortbl;\\red255\\green0\\blue0;}\\uc1\\b\\i Hello World}";
 * </code></pre>
 */
public class RTFTransfer extends ByteArrayTransfer {

	private static RTFTransfer _instance = new RTFTransfer();

private RTFTransfer() {
}

/**
 * This implementation of <code>javaToNative</code> converts a java 
 * <code>byte[]</code> to a platform specific representation.  For additional
 * information see <code>Transfer#javaToNative</code>.
 * 
 * @see Transfer#javaToNative
 * 
 * @param object a java <code>byte[]</code> containing the data to be converted
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
protected void javaToNative (Object object, TransferData transferData) {
	if (!checkText(object)) DND.error(DND.ERROR_INVALID_DATA);
	transferData.pValue = createDotNetString((String)object);
	transferData.format = OS.DataFormats_Rtf;
}

/**
 * Returns the singleton instance of the RTFTransfer class.
 *
 * @return the singleton instance of the RTFTransfer class
 */
public static RTFTransfer getInstance () {
	return _instance;
}

protected int[] getTypeIds(){
	return new int[]{OS.DataFormats_Rtf};
}
}
