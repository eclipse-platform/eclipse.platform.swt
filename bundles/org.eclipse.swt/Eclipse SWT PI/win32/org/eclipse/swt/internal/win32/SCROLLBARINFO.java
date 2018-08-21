/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
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

public class SCROLLBARINFO {
	public int cbSize;
	public RECT rcScrollBar = new RECT ();
	public int dxyLineButton;
	public int xyThumbTop;
	public int xyThumbBottom;
	public int reserved;
	public int [] rgstate = new int [OS.CCHILDREN_SCROLLBAR + 1];
	public static final int sizeof = OS.SCROLLBARINFO_sizeof ();
}
