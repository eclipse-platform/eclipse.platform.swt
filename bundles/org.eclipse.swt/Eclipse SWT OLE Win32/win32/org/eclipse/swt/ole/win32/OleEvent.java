package org.eclipse.swt.ole.win32;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

import org.eclipse.swt.widgets.Widget;

public class OleEvent {
	public int type;
	public Widget widget;
	public int detail;
	public boolean doit = true;
	public Variant[] arguments;
}
