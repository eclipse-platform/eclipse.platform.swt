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

public class MENUINFO {
	public int cbSize;
	public int fMask;
	public int dwStyle;
	public int cyMax;
	/** @field cast=(HBRUSH) */
	public long hbrBack;
	public int dwContextHelpID;
	public long dwMenuData;
	public static final int sizeof = OS.MENUINFO_sizeof ();
}
