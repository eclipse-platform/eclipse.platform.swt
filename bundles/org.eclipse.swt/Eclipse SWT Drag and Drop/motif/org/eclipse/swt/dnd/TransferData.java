package org.eclipse.swt.dnd;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
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
	
	/* attributes specific to set/get */
	int length;
	int format;
	int pValue;

	/**
	 * The result field contains the result of converting a java data type into a
	 * stgmedium value.
	 * (Warning: This field is platform dependent)
	 */
	int result;
	
}
