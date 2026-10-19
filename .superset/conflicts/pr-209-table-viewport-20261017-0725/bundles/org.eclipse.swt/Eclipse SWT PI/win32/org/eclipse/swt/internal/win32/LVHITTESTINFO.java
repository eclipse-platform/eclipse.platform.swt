/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
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

public class LVHITTESTINFO {
//	POINT pt;
	/** @field accessor=pt.x */
	public int x;
	/** @field accessor=pt.y */
	public int y;
	public int flags;
	public int iItem;
	public int iSubItem;
	public static int sizeof = OS.LVHITTESTINFO_sizeof ();
}
