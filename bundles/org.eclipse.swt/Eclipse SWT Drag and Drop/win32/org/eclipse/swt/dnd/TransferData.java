package org.eclipse.swt.dnd;

/*
 * Licensed Materials - Property of IBM,
 * WebSphere Studio Workbench
 * (c) Copyright IBM Corp 2000
 */
import org.eclipse.swt.internal.ole.win32.*;

/**
 *
 * The <code>TransferData</code> class is a platform specific data structure for describing the type and the
 * contents of data being transferred in a Drag and Drop operation.
 *
 * <p>As an application writer, you do not need to know anything about the specifics of TransferData.  You
 * should just pass the TransferData instances to subclass of Transfer and let the Transfer objects deal 
 * with the platform specific issues.  You can ask a Transfer subclass if it can handle this data by calling 
 * TextTransfer.isSupportedType(transferData).  You can get a list of the types of TransferData supported by a 
 * Transfer object by calling TextTransfer.getSupportedTypes().</p>
 *
 * <p>You should only need to become familiar with the fields in this class if you are implementing
 * a Transfer subclass and you are unable to subclass the ByteArrayTransfer class.</p>
 *
 */
public class TransferData {
	/**
	 * Data Type - a pre-defined clipboard format <b>or</b> the unique identifier of a user defined format
	 */
	public int type;
	/**
	 * Data Type - a Windows format structure which describes additional aspects of the type
	 */
	public FORMATETC formatetc;
	
	/**
	 * Set Data - a data storage structure which you update to contain the data to be transferred in the 
	 *            Windows specific format
	 */
	public STGMEDIUM stgmedium;
	/**
	 * Set Data - the result of converting a Java object into an stgmedium value
	 */
	public int result = COM.E_FAIL;

	/**
	 * Get Data - the address of an IDataObject OLE Interface which contains the data that was transferred
	 */
	public int pIDataObject;
	
}
