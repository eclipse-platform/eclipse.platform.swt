package org.eclipse.swt.dnd;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.*;

class DNDEvent extends Event {

	public TransferData dataType;
	public TransferData[] dataTypes;
	public int operations;
	public int feedback;
	
}
