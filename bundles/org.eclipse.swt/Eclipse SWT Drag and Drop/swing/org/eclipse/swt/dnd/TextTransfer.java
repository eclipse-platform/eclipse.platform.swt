/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;

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
 * @see Transfer
 */
public class TextTransfer extends Transfer {

private static TextTransfer _instance = new TextTransfer();
	
private TextTransfer() {}

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
 * @param transferData an empty <code>TransferData</code> object; this object
 *  will be filled in on return with the platform specific format of the data
 *  
 * @see Transfer#javaToNative
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkText(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
  transferData.transferable = new StringSelection((String)object);
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of plain text to a java <code>String</code>.
 * 
 * @param transferData the platform specific representation of the data to be converted
 * @return a java <code>String</code> containing text if the conversion was successful; otherwise null
 * 
 * @see Transfer#nativeToJava
 */
public Object nativeToJava(TransferData transferData){
  return super.nativeToJava(transferData);
}

boolean checkText(Object object) {
	return (object != null  && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkText(object);
}

public TransferData[] getSupportedTypes() {
  TransferData data = new TransferData();
  data.dataFlavor = DataFlavor.stringFlavor;
  return new TransferData[] {data};
}

protected DataFlavor getDataFlavor() {
  return DataFlavor.stringFlavor;
}

}
