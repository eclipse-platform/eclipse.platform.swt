package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

/**
 * The class <code>ByteArrayTransfer</code> provides a platform specific mechanism for transforming
 * a Java array of bytes into a format that can be passed around in a Drag and Drop operation and vice
 * versa.
 *
 * <p>This abstract class can be subclassed to provided utilities for transforming Java data types
 * into the byte array based platform specific drag and drop data types.  See TextTransfer and 
 * FileTransfer for examples.  If the data you are transferring <b>does not</b> map to a byte array, 
 * you should sub-class Transfer directly and do your own mapping to the platform data types.</p>
 */ 
public abstract class ByteArrayTransfer extends Transfer {
public TransferData[] getSupportedTypes(){
	return null;
}
public boolean isSupportedType(TransferData transferData){
	return false;
}

}
