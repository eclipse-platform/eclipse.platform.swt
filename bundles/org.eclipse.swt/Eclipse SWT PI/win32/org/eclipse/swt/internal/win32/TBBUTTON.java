package org.eclipse.swt.internal.win32;

/*
 * Copyright (c) 2000, 2002 IBM Corp.  All rights reserved.
 * This file is made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 */
public class TBBUTTON {
	public int iBitmap;
	public int idCommand;
	public byte fsState;
	public byte fsStyle;
	public int dwData;
	public int iString;
	public static final int sizeof = 20;
}
