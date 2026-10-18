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
