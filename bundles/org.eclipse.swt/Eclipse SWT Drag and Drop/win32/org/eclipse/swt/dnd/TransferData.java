package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */
import org.eclipse.swt.internal.ole.win32.*;

/**
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
 */
public class TransferData {
	/**
	 * Data Type - a pre-defined clipboard format <b>or</b> the unique identifier of a user defined format
	 * (Warning: This field is platform dependent)
	 */
	public int type;
	
	/* Not Javadoc
	 * Data Type - a Windows format structure which describes additional aspects of the type
	 * (Warning: This field is platform dependent)
	 */
	public FORMATETC formatetc;
	
	/* Not Javadoc
	 * Set Data - a data storage structure which you update to contain the data to be transferred in the 
	 * (Warning: This field is platform dependent)
	 */
	public STGMEDIUM stgmedium;

	/* Not Javadoc
	 * Set Data - the result of converting a Java object into an stgmedium value
	 * (Warning: This field is platform dependent)
	 */
	public int result = COM.E_FAIL;

	/* Not Javadoc
	 * Get Data - the address of an IDataObject OLE Interface which contains the data that was transferred
	 * (Warning: This field is platform dependent)
	 */
	public int pIDataObject;
	
}
