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

public class GUITHREADINFO {
	public int cbSize;
	public int flags;
	public int /*long*/ hwndActive;
	public int /*long*/ hwndFocus;
	public int /*long*/ hwndCapture;
	public int /*long*/ hwndMenuOwner;
	public int /*long*/ hwndMoveSize; 
	public int /*long*/ hwndCaret;
//	RECT rcCaret;
	public int left, top, right, bottom;
	public static int sizeof = OS.GUITHREADINFO_sizeof ();
}
