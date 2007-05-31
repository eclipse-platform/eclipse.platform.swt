/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class TVINSERTSTRUCT {
	public int /*long*/ hParent;
	public int /*long*/ hInsertAfter;
//	public TVITEMEX item;
	public int mask;
	public int /*long*/ hItem;
	public int state;
	public int stateMask;
	public int /*long*/ pszText;
  	public int cchTextMax;
  	public int iImage;
  	public int iSelectedImage;
	public int cChildren;
	public int /*long*/ lParam;
	public int iIntegral;
	public static final int sizeof = OS.TVINSERTSTRUCT_sizeof ();
}
