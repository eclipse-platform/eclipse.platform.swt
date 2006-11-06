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

public class STARTUPINFO {
	public int cb;
	public int /*long*/ lpReserved;
	public int /*long*/ lpDesktop;
	public int /*long*/ lpTitle;
	public int dwX;
	public int dwY;
	public int dwXSize;
	public int dwYSize;
	public int dwXCountChars;
	public int dwYCountChars;
	public int dwFillAttribute;
	public int dwFlags;
	public short wShowWindow;
	public short cbReserved2;
	public int /*long*/ lpReserved2;
	public int /*long*/ hStdInput;
	public int /*long*/ hStdOutput;
	public int /*long*/ hStdError;
	public static int sizeof = OS.STARTUPINFO_sizeof ();
}
