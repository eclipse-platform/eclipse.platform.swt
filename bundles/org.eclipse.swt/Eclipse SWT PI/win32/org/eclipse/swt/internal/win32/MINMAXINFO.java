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

public class MINMAXINFO {
//	POINT ptReserved;
	/** @field accessor=ptReserved.x */
	public int ptReserved_x;
	/** @field accessor=ptReserved.y */
	public int ptReserved_y;
//	POINT ptMaxSize;
	/** @field accessor=ptMaxSize.x */
	public int ptMaxSize_x;
	/** @field accessor=ptMaxSize.y */
	public int ptMaxSize_y;
//	POINT ptMaxPosition;
	/** @field accessor=ptMaxPosition.x */
	public int ptMaxPosition_x;
	/** @field accessor=ptMaxPosition.y */
	public int ptMaxPosition_y;
//	POINT ptMinTrackSize;
	/** @field accessor=ptMinTrackSize.x */
	public int ptMinTrackSize_x;
	/** @field accessor=ptMinTrackSize.y */
	public int ptMinTrackSize_y;
//	POINT ptMaxTrackSize;
	/** @field accessor=ptMaxTrackSize.x */
	public int ptMaxTrackSize_x;
	/** @field accessor=ptMaxTrackSize.y */
	public int ptMaxTrackSize_y;
	public static final int sizeof = OS.MINMAXINFO_sizeof ();
}
