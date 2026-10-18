/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class HELPINFO {
	public int cbSize;
	public int iContextType;
	public int iCtrlId;
	/** @field cast=(HANDLE) */
	public long hItemHandle;
	public int dwContextId;
//	POINT MousePos
	/** @field accessor=MousePos.x */
	public int x;
	/** @field accessor=MousePos.y */
	public int y;
	public static final int sizeof = OS.HELPINFO_sizeof ();
}
