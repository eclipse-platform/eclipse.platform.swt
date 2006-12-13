/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public abstract class OSVERSIONINFOEX extends OSVERSIONINFO {
	public short wServicePackMajor;
	public short wServicePackMinor;
	public short wSuiteMask;
	public byte wProductType;
	public byte wReserved;
	public static /*final*/ int sizeof = OS.IsUnicode ? OS.OSVERSIONINFOEXW_sizeof () : OS.OSVERSIONINFOEXA_sizeof ();
}
