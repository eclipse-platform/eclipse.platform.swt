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

 
import org.eclipse.swt.internal.win32.*;

/**
 * <code>Transfer</code> provides a mechanism for converting between a java 
 * representation of data and a platform specific representation of data and 
 * vice versa.  It is used in data transfer operations such as drag and drop and 
 * clipboard copy/paste.
 *
 * <p>You should only need to become familiar with this class if you are 
 * implementing a Transfer subclass and you are unable to subclass the 
 * ByteArrayTransfer class.</p>
 * 
 * @see ByteArrayTransfer
 */
public abstract class Transfer {
	
/**
 * Returns a list of the platform specific data types that can be converted using 
 * this transfer agent.
 *
 * <p>Only the data type fields of the <code>TransferData</code> objects are filled 
 * in.</p>
 *
 * @return a list of the data types that can be converted using this transfer agent
 */
abstract public TransferData[] getSupportedTypes();

/**
 * Returns true if the <code>TransferData</code> data type can be converted 
 * using this transfer agent, or false otherwise (including if transferData is
 * <code>null</code>).
 *
 * @param transferData a platform specific description of a data type; only the data
 *  type fields of the <code>TransferData</code> object need to be filled in
 *
 * @return true if the transferData data type can be converted using this transfer 
 * agent
 */
abstract public boolean isSupportedType(TransferData transferData);

/**
 * Returns the platform specfic ids of the  data types that can be converted using 
 * this transfer agent.
 * 
 * @return the platform specfic ids of the data types that can be converted using 
 * this transfer agent
 */
abstract protected int[] getTypeIds();

/**
 * Returns the platform specfic names of the  data types that can be converted 
 * using this transfer agent.
 * 
 * @return the platform specfic names of the data types that can be converted 
 * using this transfer agent.
 */
abstract protected String[] getTypeNames();

/**
 * Converts a java representation of data to a platform specific representation of 
 * the data. 
 *
 * <p>On a successful conversion, the transferData.result field will be set as follows:
 * <ul>
 * <li>Windows: COM.S_OK
 * <li>Motif: 1
 * <li>GTK: 1
 * <li>Photon: 1
 * </ul></p>
 * 
 * <p>If this transfer agent is unable to perform the conversion, the transferData.result 
 * field will be set to a failure value as follows:
 * <ul>
 * <li>Windows: COM.DV_E_TYMED or COM.E_FAIL
 * <li>Motif: 0
 * <li>GTK: 0
 * <li>Photon: 0
 * </ul></p>
 *
 * @param object a java representation of the data to be converted; the type of
 * Object that is passed in is dependant on the <code>Transfer</code> subclass.
 *
 * @param transferData an empty TransferData object; this object will be 
 * filled in on return with the platform specific representation of the data
 * 
 * @exception org.eclipse.swt.SWTException <ul>
 *    <li>ERROR_INVALID_DATA - if object does not contain data in a valid format or is <code>null</code></li>
 * </ul>
 */
abstract protected void javaToNative (Object object, TransferData transferData);

/**
 * Converts a platform specific representation of data to a java representation.
 * 
 * @param transferData the platform specific representation of the data to be 
 * converted
 *
 * @return a java representation of the converted data if the conversion was 
 * successful; otherwise null.  If transferData is <code>null</code> then
 * <code>null</code> is returned.  The type of Object that is returned is 
 * dependant on the <code>Transfer</code> subclass.
 */
abstract protected Object nativeToJava(TransferData transferData);

/**
 * Registers a name for a data type and returns the associated unique identifier.
 *
 * <p>You may register the same type more than once, the same unique identifier 
 * will be returned if the type has been previously registered.</p>
 *
 * <p>Note: On windows, do <b>not</b> call this method with pre-defined 
 * Clipboard Format types such as CF_TEXT or CF_BITMAP because the 
 * pre-defined identifier will not be returned</p>
 *
 * @param formatName the name of a data type
 *
 * @return the unique identifier associated with this data type
 */
public static int registerType(String formatName) {
	// Look name up in the registry
	// If name is not in registry, add it and return assigned value.
	// If name already exists in registry, return its assigned value
	TCHAR chFormatName = new TCHAR(0, formatName, true);
	return OS.RegisterClipboardFormat(chFormatName);
}

/**
 * Test that the object is of the correct format for this Transfer class.
 * 
 * @param object a java representation of the data to be converted
 * 
 * @return true if object is of the correct form for this transfer type
 * 
 * @since 3.1
 */
protected boolean validate(Object object) {
	return true;
}
}
