package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
/**
 * The class <code>TextTransfer</code> provides a platform specific mechanism 
 * for converting plain text represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information.
 * 
 * <p>An example of a java <code>String</code> containing plain text is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String textData = "Hello World";
 * </code></pre>
 */
public class TextTransfer extends ByteArrayTransfer {

	private static TextTransfer _instance = new TextTransfer();
	private static final String TYPENAME1 = "STRING\0";
	private static final int TYPEID1 = registerType(TYPENAME1);
	private static final String TYPENAME2 = "text/plain\0";
	private static final int TYPEID2 = registerType(TYPENAME2);
	private static final String TYPENAME3 = "text/text\0";
	private static final int TYPEID3 = registerType(TYPENAME3);

private TextTransfer() {
}
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
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing text
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
}
/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of plain text to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String</code> containing text if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	return null;
}
protected String[] getTypeNames(){
	return new String[]{TYPENAME1, TYPENAME2, TYPENAME3};
}
protected int[] getTypeIds(){
	return new int[]{TYPEID1, TYPEID2, TYPEID3};
}
}
