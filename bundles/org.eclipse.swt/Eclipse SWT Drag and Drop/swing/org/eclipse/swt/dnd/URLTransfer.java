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
import java.net.URL;

/**
 * The class <code>URLTransfer</code> provides a platform specific mechanism 
 * for converting text in URL format represented as a java <code>String[]</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information. The first string in the 
 * array is mandatory and must contain the fully specified url.  The second 
 * string in the array is optional and if present contains the title for the
 * page.
 * 
 * <p>An example of a java <code>String[]</code> containing a URL is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String[] urlData = new String[] {"http://www.eclipse.org", "Eclipse.org Main Page"};
 * </code></pre>
 */
public class URLTransfer extends Transfer {

private static URLTransfer _instance = new URLTransfer();
	
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
 * This implementation of <code>javaToNative</code> converts a URL and optionally a title
 * represented by a java <code>String[]</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String[]</code> containing a URL and optionally, a title
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData) {
	if (!checkURL(object) || !isSupportedType(transferData)) {
		DND.error(DND.ERROR_INVALID_DATA);
	}
	String s = ((String[])object)[0];
	if(!s.endsWith("\r\n")) {
	  if(s.endsWith("\n")) {
	    s = s.substring(0, s.length() - 1) + "\r\n";
	  } else if(s.endsWith("\r")) {
	    s += "\n";
	  }
	}
  transferData.transferable = new StringSelection(s);
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a URL and optionally, a title to a java <code>String[]</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String[]</code> containing a URL and optionally a title if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
  String s = (String)super.nativeToJava(transferData);
  if(s.endsWith("\n")) {
    s = s.substring(0, s.length() - 1);
  }
  if(s.endsWith("\r")) {
    s = s.substring(0, s.length() - 1);
  }
  return new String[] {s, null};
}

boolean checkURL(Object object) {
  if (object == null  || !(object instanceof String[]) || ((String[])object).length == 0) return false;
  String[] strings = (String[])object;
  if (strings[0] == null || strings[0].length() == 0) return false;
  try {
    new URL(strings[0]);
  } catch (java.net.MalformedURLException e) {
    return false;
  }
  return true;
}

static DataFlavor URL_FLAVOR = new DataFlavor("text/uri-list; class=java.lang.String", "URI list");

protected boolean validate(Object object) {
	return checkURL(object);
}

public TransferData[] getSupportedTypes() {
  TransferData data = new TransferData();
  data.dataFlavor = URL_FLAVOR;
  return new TransferData[] {data};
}

protected DataFlavor getDataFlavor() {
  return URL_FLAVOR;
}

}
