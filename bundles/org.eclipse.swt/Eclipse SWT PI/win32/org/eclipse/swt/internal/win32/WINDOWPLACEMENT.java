/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class WINDOWPLACEMENT {
	public int length;
	public int flags; 
	public int showCmd;
//	POINT ptMinPosition;
	public int ptMinPosition_x;
	public int ptMinPosition_y;
//	POINT ptMaxPosition;
	public int ptMaxPosition_x;
	public int ptMaxPosition_y;
//	RECT  rcNormalPosition; 
	public int left;
	public int top;
	public int right;
	public int bottom;
	public static final int sizeof = 44;
}
