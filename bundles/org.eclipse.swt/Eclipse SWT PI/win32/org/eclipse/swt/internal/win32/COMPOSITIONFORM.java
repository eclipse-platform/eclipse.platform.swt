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

public class COMPOSITIONFORM {
	public int dwStyle;
//	POINT ptCurrentPos;
	/** @field accessor=ptCurrentPos.x */
	public int x;
	/** @field accessor=ptCurrentPos.y */
	public int y;
//	RECT rcArea;  
	/** @field accessor=rcArea.left */
	public int left;
	/** @field accessor=rcArea.top */
	public int top;
	/** @field accessor=rcArea.right */
	public int right;
	/** @field accessor=rcArea.bottom */
	public int bottom;
	public static final int sizeof = OS.COMPOSITIONFORM_sizeof ();
}
