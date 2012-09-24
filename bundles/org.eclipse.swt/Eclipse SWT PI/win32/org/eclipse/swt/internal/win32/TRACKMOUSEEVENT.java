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

public class TRACKMOUSEEVENT {
	public int cbSize;
	public int dwFlags;
	/** @field cast=(HWND) */
	public long /*int*/ hwndTrack;
	public int dwHoverTime;
	public static final int sizeof = OS.TRACKMOUSEEVENT_sizeof ();
}
