/*******************************************************************************
 * Copyright (c) 2011 IBM Corporation and others.
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
	public int /*long*/ crText;
	public int /*long*/ crBorder;
	public int /*long*/ crShadow;
	public int iTextShadowType;
	public POINT ptShadowOffset;
	public int iBorderSize;
	public int iFontPropId;
	public int iColorPropId;
	public int iStateId;
	public boolean fApplyOverlay;
	public int iGlowSize;
	/** @field cast=(DTT_CALLBACK_PROC) */
	public int /*long*/ pfnDrawTextCallback;
	public int /*long*/ lParam;
	public static final int sizeof = OS.DTTOPTS_sizeof ();
}