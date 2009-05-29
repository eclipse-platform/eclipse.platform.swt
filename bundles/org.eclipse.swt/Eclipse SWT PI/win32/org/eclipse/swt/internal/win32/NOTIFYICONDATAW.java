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

public class NOTIFYICONDATAW extends NOTIFYICONDATA {
	/** @field cast=(TCHAR) */
	public char szTip[] = new char [128];
	/** @field cast=(TCHAR),flags=no_wince */
	public char szInfo[] = new char [256];
	/** @field cast=(TCHAR),flags=no_wince */
	public char szInfoTitle[] = new char [64];
	public static final int sizeof = OS.NOTIFYICONDATAW_V2_SIZE;
}
