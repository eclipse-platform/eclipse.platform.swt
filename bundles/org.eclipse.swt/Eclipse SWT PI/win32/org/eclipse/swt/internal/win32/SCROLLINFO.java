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

public class SCROLLINFO {
	public int cbSize;
	public int fMask;
	public int nMin;
	public int nMax;
	public int nPage;
	public int nPos;
	public int nTrackPos;
	public static final int sizeof = OS.SCROLLINFO_sizeof ();
}
