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

public abstract class NMTTDISPINFO extends NMHDR {
	public int /*long*/ lpszText;
	public int /*long*/ hinst;   
	public int uFlags;
	public int /*long*/ lParam;
	public static final int sizeof = OS.IsUnicode ? OS.NMTTDISPINFOW_sizeof () : OS.NMTTDISPINFOA_sizeof ();
}
