package org.eclipse.swt.dnd;

import org.eclipse.swt.widgets.*;

class DNDEvent extends Event {

	public TransferData dataType;
	public TransferData[] dataTypes;
	public int operations;
	public int feedback;
	
}