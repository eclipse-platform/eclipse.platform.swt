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

public class BUTTON_IMAGELIST {
	/** @field cast=(HIMAGELIST) */
	public long /*int*/ himl; 
	/** @field accessor=margin.left,cast=(LONG) */
	public int margin_left;
	/** @field accessor=margin.top,cast=(LONG) */
	public int margin_top; 
	/** @field accessor=margin.right,cast=(LONG) */
	public int margin_right;
	/** @field accessor=margin.bottom,cast=(LONG) */
	public int margin_bottom; 
	/** @field cast=(UINT) */
	public int uAlign;
	public static final int sizeof = OS.BUTTON_IMAGELIST_sizeof ();
}
