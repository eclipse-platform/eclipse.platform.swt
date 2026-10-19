/*******************************************************************************
 * Copyright (c) 2000, 2011 IBM Corporation and others.
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
package org.eclipse.swt.internal.opengl.win32;


public class PIXELFORMATDESCRIPTOR {
	public short nSize;
	public short nVersion;
	public int dwFlags;
	public byte iPixelType;
	public byte cColorBits;
	public byte cRedBits;
	public byte cRedShift;
	public byte cGreenBits;
	public byte cGreenShift;
	public byte cBlueBits;
	public byte cBlueShift;
	public byte cAlphaBits;
	public byte cAlphaShift;
	public byte cAccumBits;
	public byte cAccumRedBits;
	public byte cAccumGreenBits;
	public byte cAccumBlueBits;
	public byte cAccumAlphaBits;
	public byte cDepthBits;
	public byte cStencilBits;
	public byte cAuxBuffers;
	public byte iLayerType;
	public byte bReserved;
	public int dwLayerMask;
	public int dwVisibleMask;
	public int dwDamageMask;
	public static final int sizeof = 40;
}
