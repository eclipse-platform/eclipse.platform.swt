package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
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
	
}
