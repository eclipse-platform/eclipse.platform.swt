/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.ole.win32;

public final class GUID
{
	public int    data1;
	public short  data2;
	public short  data3;
	public byte   b0;
	public byte   b1;
	public byte   b2;
	public byte   b3;
	public byte   b4;
	public byte   b5;
	public byte   b6;
	public byte   b7;
	
	public static final int sizeof = 16;
}
