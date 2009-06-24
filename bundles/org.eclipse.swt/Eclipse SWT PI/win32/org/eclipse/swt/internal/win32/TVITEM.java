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

public class TVITEM {
	public int mask;
	/** @field cast=(HTREEITEM) */
	public int /*long*/ hItem;
	public int state;
	public int stateMask;
	/** @field cast=(LPTSTR) */
	public int /*long*/ pszText;
  	public int cchTextMax;
  	public int iImage;
  	public int iSelectedImage;
	public int cChildren;
	public int /*long*/ lParam;
	public static final int sizeof = OS.TVITEM_sizeof ();
}
