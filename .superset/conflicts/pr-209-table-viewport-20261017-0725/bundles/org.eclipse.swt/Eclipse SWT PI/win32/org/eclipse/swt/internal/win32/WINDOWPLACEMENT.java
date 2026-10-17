/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
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
	/** @field accessor=ptMinPosition.x */
	public int ptMinPosition_x;
	/** @field accessor=ptMinPosition.y */
	public int ptMinPosition_y;
//	POINT ptMaxPosition;
	/** @field accessor=ptMaxPosition.x */
	public int ptMaxPosition_x;
	/** @field accessor=ptMaxPosition.y */
	public int ptMaxPosition_y;
//	RECT  rcNormalPosition;
	/** @field accessor=rcNormalPosition.left */
	public int left;
	/** @field accessor=rcNormalPosition.top */
	public int top;
	/** @field accessor=rcNormalPosition.right */
	public int right;
	/** @field accessor=rcNormalPosition.bottom */
	public int bottom;
	public static final int sizeof = OS.WINDOWPLACEMENT_sizeof ();
}
