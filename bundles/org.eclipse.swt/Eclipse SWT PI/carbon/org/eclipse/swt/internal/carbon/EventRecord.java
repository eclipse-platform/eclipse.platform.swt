package org.eclipse.swt.internal.carbon;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class EventRecord {
	public short what;
	public int message;
	public int when;
	//Point where;
	public short where_v;
	public short where_h;
	public short modifiers;
	public static final int sizeof = 16;
}
