/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public abstract class NOTIFYICONDATA {
	public int cbSize;
	public int hWnd;
	public int uID;
	public int uFlags;
	public int uCallbackMessage;
	public int hIcon;
	public int dwState;
	public int dwStateMask;
	public int uVersion;
	public int dwInfoFlags;
	public static final int sizeof = OS.NOTIFYICONDATA_V2_SIZE;
}
