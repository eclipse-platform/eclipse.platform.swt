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
package org.eclipse.swt.internal.ole.win32;

public final class OLEINPLACEFRAMEINFO {
	public int cb;
	public int fMDIApp;
	/** @field cast=(HWND) */
	public int /*long*/ hwndFrame;
	/** @field cast=(HACCEL) */
	public int /*long*/ haccel;
	public int cAccelEntries;
	public static final int sizeof = COM.OLEINPLACEFRAMEINFO_sizeof ();
}
