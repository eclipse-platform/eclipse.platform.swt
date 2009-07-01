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

public class MONITORINFO {
	public int cbSize;
//	RECT rcMonitor;
	/** @field accessor=rcMonitor.left */
	public int rcMonitor_left;
	/** @field accessor=rcMonitor.top */
	public int rcMonitor_top;
	/** @field accessor=rcMonitor.right */
	public int rcMonitor_right;
	/** @field accessor=rcMonitor.bottom */
	public int rcMonitor_bottom;
//	RECT rcWork;
	/** @field accessor=rcWork.left */
	public int rcWork_left;
	/** @field accessor=rcWork.top */
	public int rcWork_top;
	/** @field accessor=rcWork.right */
	public int rcWork_right;
	/** @field accessor=rcWork.bottom */
	public int rcWork_bottom;
	public int dwFlags;
	public static final int sizeof = OS.MONITORINFO_sizeof ();
}
