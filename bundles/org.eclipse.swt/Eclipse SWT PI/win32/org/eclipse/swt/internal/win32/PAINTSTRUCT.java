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

public class PAINTSTRUCT {
	/** @field cast=(HDC) */
	public int /*long*/  hdc; 
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
