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

public class TOUCHINPUT {
	public int /*long*/ x;
	public int /*long*/ y;
	/** @field cast=(HWND) */
	public int /*long*/    hSource;
	public int dwID;
	public int dwFlags;
	public int dwMask;
	public int dwTime;
	public int /*long*/ dwExtraInfo;
	public int cxContact;
	public int cyContact;
	public static final int sizeof = OS.TOUCHINPUT_sizeof();
}
