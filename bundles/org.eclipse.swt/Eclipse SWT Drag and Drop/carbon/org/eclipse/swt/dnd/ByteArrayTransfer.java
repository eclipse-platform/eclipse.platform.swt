package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

/**
 * The class <code>ByteArrayTransfer</code> provides a platform specific 
 * mechanism for converting a java <code>byte[]</code> to a platform 
 * specific representation of the byte array and vice versa.  See 
 * <code>Transfer</code> for additional information.
 *
 * <p><code>ByteArrayTransfer</code> is never used directly but is sub-classed 
 * by transfer agents that convert between data in a java format such as a
 * <code>String</code> and a platform specific byte array.
 * 
 * <p>If the data you are converting <b>does not</b> map to a 
 * <code>byte[]</code>, you should sub-class <code>Transfer</code> directly 
 * and do your own mapping to a platform data type.</p>
 */
public abstract class ByteArrayTransfer extends Transfer {
public TransferData[] getSupportedTypes(){
	return null;
}
public boolean isSupportedType(TransferData transferData){
	return false;
}

}
