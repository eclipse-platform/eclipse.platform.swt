/*******************************************************************************
 * Copyright (c) 20007 IBM Corporation and others.
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
 * The class <code>URLTransfer</code> provides a platform specific mechanism 
 * for converting text in URL format represented as a java <code>String</code> 
 * to a platform specific representation of the data and vice versa.  See 
 * <code>Transfer</code> for additional information. The string 
 * must contain a fully specified url.
 * 
 * <p>An example of a java <code>String</code> containing a URL is shown 
 * below:</p>
 * 
 * <code><pre>
 *     String urlData = "http://www.eclipse.org";
 * </code></pre>
 */
public class URLTransfer extends ByteArrayTransfer {

private URLTransfer() {}

/**
 * Returns the singleton instance of the URLTransfer class.
 *
 * @return the singleton instance of the URLTransfer class
 */
public static URLTransfer getInstance () {
	return null;
}

/**
 * This implementation of <code>javaToNative</code> converts a URL
 * represented by a java <code>String</code> to a platform specific representation.
 * For additional information see <code>Transfer#javaToNative</code>.
 * 
 * @param object a java <code>String</code> containing a URL
 * @param transferData an empty <code>TransferData</code> object; this
 *  object will be filled in on return with the platform specific format of the data
 */
public void javaToNative (Object object, TransferData transferData){
}

/**
 * This implementation of <code>nativeToJava</code> converts a platform specific 
 * representation of a URL to a java <code>String</code>.
 * For additional information see <code>Transfer#nativeToJava</code>.
 * 
 * @param transferData the platform specific representation of the data to be 
 * been converted
 * @return a java <code>String[]</code> containing a URL if the 
 * conversion was successful; otherwise null
 */
public Object nativeToJava(TransferData transferData){
	return null;
}

protected int[] getTypeIds(){
	return null;
}

protected String[] getTypeNames(){
	return null; 
}
}
