/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class SHACTIVATEINFO {
	public int cbSize;
	/** @field cast=(HWND) */
	public int /*long*/ hwndLastFocus;
	public int fSipUp; // :1
	public int fSipOnDeactivation; // :1
	public int fActive; // :1
	public int fReserved; // :29
	public static final int sizeof = OS.SHACTIVATEINFO_sizeof ();
}
