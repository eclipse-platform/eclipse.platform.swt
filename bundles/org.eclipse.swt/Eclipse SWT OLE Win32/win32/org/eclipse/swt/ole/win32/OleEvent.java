package org.eclipse.swt.ole.win32;

import org.eclipse.swt.widgets.Widget;

/*
 * Licensed Materials - Property of IBM,
 * (c) Copyright IBM Corp. 1998, 2000  All Rights Reserved
 */

public class OleEvent {
	public int type;
	public Widget widget;
	public int detail;
	public boolean doit = true;
	public Variant[] arguments;
}
