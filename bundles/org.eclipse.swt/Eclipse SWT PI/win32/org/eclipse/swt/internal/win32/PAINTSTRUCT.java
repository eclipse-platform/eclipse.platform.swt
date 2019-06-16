/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
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

public class PAINTSTRUCT {
	/** @field cast=(HDC) */
	public long  hdc;
	public boolean fErase;
//	public RECT rcPaint;
	/** @field accessor=rcPaint.left */
	public int left;
	/** @field accessor=rcPaint.top */
	public int top;
	/** @field accessor=rcPaint.right */
	public int right;
	/** @field accessor=rcPaint.bottom */
	public int bottom;
	public boolean fRestore;
	public boolean fIncUpdate;
	public byte[] rgbReserved = new byte[32];
	public static final int sizeof = OS.PAINTSTRUCT_sizeof ();
}
