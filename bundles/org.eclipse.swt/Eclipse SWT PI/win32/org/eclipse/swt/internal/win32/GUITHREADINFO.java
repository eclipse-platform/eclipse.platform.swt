/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
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
	public int hwndActive;
	public int hwndFocus;
	public int hwndCapture;
	public int hwndMenuOwner;
	public int hwndMoveSize; 
	public int hwndCaret;
//	RECT rcCaret;
	public int left, top, right, bottom;
	public static int sizeof = 48;
}
