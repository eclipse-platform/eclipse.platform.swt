package org.eclipse.swt.ole.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */

import org.eclipse.swt.widgets.Widget;

public class OleEvent {
	public int type;
	public Widget widget;
	public int detail;
	public boolean doit = true;
	public Variant[] arguments;
}
