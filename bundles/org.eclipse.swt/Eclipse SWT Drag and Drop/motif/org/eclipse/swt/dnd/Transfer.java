package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
 
import org.eclipse.swt.internal.Converter;
import org.eclipse.swt.internal.motif.OS;
import org.eclipse.swt.widgets.Display;

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
abstract protected String[] getTypeNames();
abstract protected int[] getTypeIds();
abstract protected void javaToNative (Object object, TransferData transferData);
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
 * @return the unique identifier associated with this data type
 */
public static int registerType(String formatName){
	// Use default display because we don't have a particular widget
	int xDisplay = Display.getDefault().xDisplay;
	// Use the character encoding for the default locale
	byte[] buffer = Converter.wcsToMbcs (null, formatName, true);
	return OS.XmInternAtom (xDisplay, buffer, false); 
}
}
