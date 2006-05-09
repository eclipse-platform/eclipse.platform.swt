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
public class HTMLTransfer extends ByteArrayTransfer {

	private static HTMLTransfer _instance = new HTMLTransfer();
	private static final String TYPENAME1 = "text/html\0";
	private static final int TYPEID1 = registerType(TYPENAME1);
	private static final String TYPENAME2 = "TEXT/HTML\0";
	private static final int TYPEID2 = registerType(TYPENAME2);

private HTMLTransfer() {
}
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
	return null;
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME1, TYPENAME2};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID1, TYPEID2};
}
}
