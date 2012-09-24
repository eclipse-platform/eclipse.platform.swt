/*******************************************************************************
 * Copyright (c) 2011, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

	
public class DTTOPTS {
	public int dwSize;
	public int dwFlags;
	public int crText;
	public int crBorder;
	public int crShadow;
	public int iTextShadowType;
	public POINT ptShadowOffset;
	public int iBorderSize;
	public int iFontPropId;
	public int iColorPropId;
	public int iStateId;
	public boolean fApplyOverlay;
	public int iGlowSize;
	/** @field cast=(DTT_CALLBACK_PROC) */
	public long /*int*/ pfnDrawTextCallback;
	public long /*int*/ lParam;
	public static final int sizeof = OS.DTTOPTS_sizeof ();
}