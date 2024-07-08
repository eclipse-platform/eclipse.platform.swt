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
import java.io.Reader;

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
 *     String htmlData = "<p>This is a paragraph of text.</p>";
 * </code></pre>
 */
public class HTMLTransfer extends Transfer {

private static HTMLTransfer _instance = new HTMLTransfer();
	
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
 * This implementation of <code>javaToNative</code> converts HTML-formatted text
 * represented by a java <code>String</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing HTML text
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
	if (!checkHTML(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
  transferData.transferable = new StringSelection((String)object);
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
  Object o = super.nativeToJava(transferData);
  if(o instanceof Reader) {
    StringBuilder sb = new StringBuilder();
    Reader reader = (Reader)o;
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
  return o;
}

boolean checkHTML(Object object) {
	return (object != null  && object instanceof String && ((String)object).length() > 0);
}

protected boolean validate(Object object) {
	return checkHTML(object);
}

static DataFlavor HTML_FLAVOR_1 = new DataFlavor("text/html; class=java.lang.String", "HTML Text");
//static DataFlavor HTML_FLAVOR_2 = new DataFlavor("text/html; class=java.io.Reader", "HTML Text");

public TransferData[] getSupportedTypes() {
  TransferData data1 = new TransferData();
  data1.dataFlavor = HTML_FLAVOR_1;
  return new TransferData[] {data1};
//  TransferData data2 = new TransferData();
//  data2.dataFlavor = HTML_FLAVOR_2;
//  return new TransferData[] {data1, data2};
}

protected DataFlavor getDataFlavor() {
  return HTML_FLAVOR_1;
}

}
