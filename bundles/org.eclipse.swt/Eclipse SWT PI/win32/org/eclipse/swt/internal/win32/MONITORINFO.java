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

public class MONITORINFO {
	public int cbSize;
//	RECT rcMonitor;
	public int rcMonitor_left;
	public int rcMonitor_top;
	public int rcMonitor_right;
	public int rcMonitor_bottom;
//	RECT rcWork;
	public int rcWork_left;
	public int rcWork_top;
	public int rcWork_right;
	public int rcWork_bottom;
	public int dwFlags;
	public static final int sizeof = 40;
}
