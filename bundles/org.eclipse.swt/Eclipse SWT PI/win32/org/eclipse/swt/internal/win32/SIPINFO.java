/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class SIPINFO {
	public int cbSize;
	public int fdwFlags;
//	RECT rcVisibleDesktop
	public int rcVisibleDesktop_left;
	public int rcVisibleDesktop_top;
	public int rcVisibleDesktop_right;
	public int rcVisibleDesktop_bottom;
//	RECT rcSipRect
	public int rcSipRect_left;
	public int rcSipRect_top;
	public int rcSipRect_right;
	public int rcSipRect_bottom;
	public int dwImDataSize;
	public int pvImData;
	public static final int sizeof = 48;
}
