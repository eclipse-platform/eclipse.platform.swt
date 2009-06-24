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

public class HELPINFO {
	public int cbSize;
	public int iContextType;
	public int iCtrlId;
	/** @field cast=(HANDLE) */
	public int /*long*/ hItemHandle;
	public int dwContextId;
//	POINT MousePos
	/** @field accessor=MousePos.x */
	public int x;
	/** @field accessor=MousePos.y */
	public int y;
	public static final int sizeof = OS.HELPINFO_sizeof ();
}
