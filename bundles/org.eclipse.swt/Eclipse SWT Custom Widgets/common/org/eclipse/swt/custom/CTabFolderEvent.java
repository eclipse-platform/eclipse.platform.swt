package org.eclipse.swt.custom;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2001  All Rights Reserved
 */

import org.eclipse.swt.events.TypedEvent;
import org.eclipse.swt.widgets.*;

public class CTabFolderEvent extends TypedEvent {
 	public Widget item;
 	public boolean doit;
 	
CTabFolderEvent(Widget w) {
	super(w);
}
}