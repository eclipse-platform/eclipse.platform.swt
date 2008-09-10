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

public class COMPOSITIONFORM {
	public int dwStyle;
//	POINT ptCurrentPos;
	public int x;
	public int y;
//	RECT rcArea;  
	public int left;
	public int top;
	public int right;
	public int bottom;
	public static final int sizeof = OS.COMPOSITIONFORM_sizeof ();
}
