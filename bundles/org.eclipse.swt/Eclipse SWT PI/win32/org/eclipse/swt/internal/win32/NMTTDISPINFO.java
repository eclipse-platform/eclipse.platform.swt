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

public abstract class NMTTDISPINFO extends NMHDR {
	/** @field cast=(void *) */
	public int /*long*/ lpszText;
	/** @field cast=(HINSTANCE) */
	public int /*long*/ hinst;   
	public int uFlags;
	public int /*long*/ lParam;
	public static final int sizeof = OS.IsUnicode ? OS.NMTTDISPINFOW_sizeof () : OS.NMTTDISPINFOA_sizeof ();
}
