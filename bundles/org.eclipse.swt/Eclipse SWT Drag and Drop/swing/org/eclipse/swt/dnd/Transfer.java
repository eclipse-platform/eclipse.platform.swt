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
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

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
	
abstract protected DataFlavor getDataFlavor();
  
/**
 * Returns a list of the platform specific data types that can be converted using 
 * this transfer agent.
 *
 * <p>Only the dataFlavor fields of the <code>TransferData</code> objects are filled 
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
 * @param transferData a platform specific description of a data type; only the
 * dataFlavor fields of the <code>TransferData</code> object need to be filled in.
 *
 * @return true if the transferData data type can be converted using this transfer 
 * agent
 */
public boolean isSupportedType(TransferData transferData) {
  TransferData[] types = getSupportedTypes();
  for(int i=0; i<types.length; i++) {
    if(types[i].dataFlavor.equals(transferData.dataFlavor)) {
      return true;
    }
  }
  return false;
}

/**
 * Returns the platform specfic ids of the  data types that can be converted using 
 * this transfer agent.
 * 
 * @return the platform specfic ids of the data types that can be converted using 
 * this transfer agent
 */
protected int[] getTypeIds() {
  TransferData[] supportedTyes = getSupportedTypes();
  int[] ids = new int[supportedTyes.length];
  for(int i=0; i<supportedTyes.length; i++) {
    ids[i] = getTypeID(supportedTyes[i].dataFlavor.getHumanPresentableName());
  }
  return ids;
}

/**
 * Returns the platform specfic names of the  data types that can be converted 
 * using this transfer agent.
 * 
 * @return the platform specfic names of the data types that can be converted 
 * using this transfer agent.
 */
protected String[] getTypeNames() {
  TransferData[] supportedTyes = getSupportedTypes();
  String[] names = new String[supportedTyes.length];
  for(int i=0; i<supportedTyes.length; i++) {
    names[i] = supportedTyes[i].dataFlavor.getHumanPresentableName();
  }
  return names;
}

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
protected Object nativeToJava(TransferData transferData){
  if (!isSupportedType(transferData) || transferData.transferable == null) return null;
  try {
    return transferData.transferable.getTransferData(getDataFlavor());
  } catch(UnsupportedFlavorException e) {
  } catch(IOException e) {}
  return null;
}

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
  return getTypeID(formatName);
}

static int getTypeID(String formatName) {
  // copied from the Carbon's port Transfer class
  int length = formatName.length();
  // TODO - hashcode may not be unique - need another way
  if (length > 4) return formatName.hashCode();
  int type = 0;
  if (length > 0) type |= (formatName.charAt(0) & 0xff) << 24;
  if (length > 1) type |= (formatName.charAt(1) & 0xff) << 16;
  if (length > 2) type |= (formatName.charAt(2) & 0xff) << 8;
  if (length > 3) type |= formatName.charAt(3) & 0xff;
  return type;
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
