package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.ole.win32.COM;
import org.eclipse.swt.internal.win32.TCHAR;

/**
 * The class <code>Transfer</code> provides a mechanism for converting a Java object to a 
 * platform specific format that can be passed around in a Drag and Drop operation and vice versa.
 *
 * <p>You should only need to become familiar with this class if you are implementing
 * a Transfer subclass and you are unable to subclass the ByteArrayTransfer class.</p>
 */
public abstract class Transfer {
	
/**
 * Returns a list of the data types that can be transferred using this Transfer agent.
 *
 * <p>Only the data type fields of the TransferData Object are filled in.</p>
 *
 * @return a list of the data types that can be transferred using this Transfer agent
 */
abstract public TransferData[] getSupportedTypes();
/**
 * Returns true if the transferData data type can be transferred using this Transfer agent.
 *
 * @param transferData a platform specific description of a data type; only the data type fields 
 *                         of the TransferData Object need to be filled in
 *
 * @return true if the transferData data type can be transferred using this Transfer agent
 */
abstract public boolean isSupportedType(TransferData transferData);

abstract protected int[] getTypeIds();
abstract protected String[] getTypeNames();

/**
 * Converts a Java Object to a platform specific representation of the data. 
 * <p>
 * On a successful conversion, the transferData.result field will be set as follows:
 * <ul>
 * <li>Windows: OLE.S_OK
 * <li>Motif: 0
 * </ul>
 * If this transfer agent is unable to perform the conversion,
 * the transferData.result field will be set to a failure value as follows:
 * <ul>
 * <li>Windows: OLE.DV_E_TYMED
 * <li>Motif: 1
 * </ul></p>
 *
 * @param object a Java Object containing the data to be transferred
 *
 * @param transferData an empty TransferData object; this object will be filled in on return
 *        with the platform specific representation of the data
 */
 abstract protected void javaToNative (Object object, TransferData transferData);     

/**
 * Converts a platform specific representation of data to a Java Object.
 *
 * @param transferData the platform specific representation of the data that has been transferred
 *
 * @return a Java Object containing the transferred data if the conversion was successful;
 *         otherwise null
 */
 abstract protected Object nativeToJava(TransferData transferData); 

/**
 * Registers a name for a data type and returns the associated unique identifier.
 *
 * <p>You may register the same type more than once, the same unique identifier will be returned if the
 * type has been previously registered.</p>
 *
 * <p>Note: Do <b>not</b> call this method with pre-defined Clipboard Format types such as CF_TEXT 
 * or CF_BITMAP because the pre-defined value will not be returned</p>
 *
 * @param formatName the name of a data type
 *
 * @return the unique identifier associated with htis data type
 */
public static int registerType(String formatName){
	// Look name up in the registry
	// If name is not in registry, add it and return assigned value.
	// If name already exists in registry, return its assigned value
	TCHAR chFormatName = new TCHAR(0, formatName, true);
	return COM.RegisterClipboardFormat(chFormatName);
}
}
