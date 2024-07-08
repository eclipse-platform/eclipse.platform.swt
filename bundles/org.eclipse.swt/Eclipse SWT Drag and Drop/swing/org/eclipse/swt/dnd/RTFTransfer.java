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
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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
public class RTFTransfer extends Transfer {

private static RTFTransfer _instance = new RTFTransfer();
	
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
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing RTF text
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (final Object object, TransferData transferData){
	if (!checkRTF(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
  transferData.transferable = new Transferable() {
    public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
      if(!isDataFlavorSupported(flavor)) {
        throw new UnsupportedFlavorException(flavor);
      }
      return new ByteArrayInputStream(((String)object).getBytes());
    }
    public boolean isDataFlavorSupported(DataFlavor flavor) {
      return getDataFlavor().equals(flavor);
    }
    public DataFlavor[] getTransferDataFlavors() {
      return new DataFlavor[] {getDataFlavor()};
    }
  };
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of RTF text to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing RTF text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
  if (!isSupportedType(transferData) || transferData.transferable == null) return null;
  InputStreamReader reader = new InputStreamReader((ByteArrayInputStream)super.nativeToJava(transferData));
  StringBuilder sb = new StringBuilder();
  char[] chars = new char[128];
  try {
    for(int i; (i=reader.read(chars)) != -1; ) {
      sb.append(chars, 0, i);
    }
  } catch(Exception e) {
    e.printStackTrace();
  }
  return sb.toString();
}

boolean checkRTF(Object object) {
	return (object != null  && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkRTF(object);
}

static DataFlavor RTF_FLAVOR = new DataFlavor("text/rtf", "RTF Text");

public TransferData[] getSupportedTypes() {
  TransferData data = new TransferData();
  data.dataFlavor = RTF_FLAVOR;
  return new TransferData[] {data};
}

protected DataFlavor getDataFlavor() {
  return RTF_FLAVOR;
}

}
