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

public class MINMAXINFO {
//	POINT ptReserved;
	public int ptReserved_x;
	public int ptReserved_y;
//	POINT ptMaxSize;
	public int ptMaxSize_x;
	public int ptMaxSize_y;
//	POINT ptMaxPosition;
	public int ptMaxPosition_x;
	public int ptMaxPosition_y;
//	POINT ptMinTrackSize;
	public int ptMinTrackSize_x;
	public int ptMinTrackSize_y;
//	POINT ptMaxTrackSize;
	public int ptMaxTrackSize_x;
	public int ptMaxTrackSize_y;
	public static final int sizeof = 40;
}
