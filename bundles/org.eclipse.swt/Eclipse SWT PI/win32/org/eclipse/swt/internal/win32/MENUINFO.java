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

public class MENUINFO {
	public int cbSize;
	public int fMask;
	public int dwStyle;
	public int cyMax;
	/** @field cast=(HBRUSH) */
	public int /*long*/ hbrBack;
	public int dwContextHelpID;
	public int /*long*/ dwMenuData;
	public static final int sizeof = OS.MENUINFO_sizeof ();
}
