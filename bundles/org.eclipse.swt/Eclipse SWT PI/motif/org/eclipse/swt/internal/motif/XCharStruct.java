package org.eclipse.swt.internal.motif;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
 
public class XCharStruct {
	public short lbearing;
	public short rbearing;
	public short width;
	public short ascent;
	public short descent;
	public short attributes;
	public static final int sizeof = 12;
}
