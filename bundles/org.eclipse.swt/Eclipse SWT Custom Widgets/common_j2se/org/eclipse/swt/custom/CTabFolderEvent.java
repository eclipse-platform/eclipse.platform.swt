package org.eclipse.swt.custom;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
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
