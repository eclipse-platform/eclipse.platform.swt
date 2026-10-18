/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
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

public class LOGFONT {
	public int lfHeight;
	public int lfWidth;
	public int lfEscapement;
	public int lfOrientation;
	public int lfWeight;
	public byte lfItalic;
	public byte lfUnderline;
	public byte lfStrikeOut;
	public byte lfCharSet;
	public byte lfOutPrecision;
	public byte lfClipPrecision;
	public byte lfQuality;
	public byte lfPitchAndFamily;
	public char[] lfFaceName = new char[OS.LF_FACESIZE];
	public static final int sizeof = OS.LOGFONT_sizeof ();
}
