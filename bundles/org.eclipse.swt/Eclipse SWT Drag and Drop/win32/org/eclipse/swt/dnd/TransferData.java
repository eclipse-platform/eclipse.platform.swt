/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.dnd;

import org.eclipse.swt.internal.ole.win32.*;

/**
 * The <code>TransferData</code> class is a platform specific data structure for
 * describing the type and the contents of data being converted by a transfer agent.
 *
 * <p>As an application writer, you do not need to know the specifics of 
 * TransferData.  TransferData instances are passed to a subclass of Transfer 
 * and the Transfer object manages the platform specific issues.  
 * You can ask a Transfer subclass if it can handle this data by calling 
 * Transfer.isSupportedType(transferData).</p>
 *
 * <p>You should only need to become familiar with the fields in this class if you 
 * are implementing a Transfer subclass and you are unable to subclass the 
 * ByteArrayTransfer class.</p>
 */
public class TransferData {
	/**
	 * The type is a unique identifier of a system format or user defined format.
	 * (Warning: This field is platform dependent)
	 */
	public int type;
	
	/**
	 * The formatetc structure is a generalized data transfer format, enhanced to 
	 * encompass a target device, the aspect, or view of the data, and 
	 * a storage medium.
	 * (Warning: This field is platform dependent)
	 */
	public FORMATETC formatetc;
	
	/**
	 * The stgmedium structure is a generalized global memory handle used for 
	 * data transfer operations.
	 * (Warning: This field is platform dependent)
	 */
	public STGMEDIUM stgmedium;

	/**
	 * The result field contains the result of converting a java data type into a
	 * stgmedium value.
	 * (Warning: This field is platform dependent)
	 */
	public int result = COM.E_FAIL;

	/**
	 * The pIDataObject is the address of an IDataObject OLE Interface which 
	 * provides access to the data associated with the transfer.
	 * (Warning: This field is platform dependent)
	 */
	public int pIDataObject;
	
	static boolean sameType(TransferData data1, TransferData data2) {
		if (data1 == data2) return true;
		if (data1 == null || data2 == null) return false;
		return (data1.type == data2.type &&
				data1.formatetc.cfFormat == data2.formatetc.cfFormat &&
		    	data1.formatetc.dwAspect == data2.formatetc.dwAspect && 
		    	data1.formatetc.tymed == data2.formatetc.tymed);
	}
	
}
