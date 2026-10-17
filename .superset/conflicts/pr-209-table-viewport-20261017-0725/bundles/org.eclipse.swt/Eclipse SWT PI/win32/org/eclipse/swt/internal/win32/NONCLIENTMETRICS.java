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

public class NONCLIENTMETRICS {
	public int cbSize;
	public int iBorderWidth;
	public int iScrollWidth;
	public int iScrollHeight;
	public int iCaptionWidth;
	public int iCaptionHeight;
	public LOGFONT lfCaptionFont = new LOGFONT ();
	public int iSmCaptionWidth;
	public int iSmCaptionHeight;
	public LOGFONT lfSmCaptionFont = new LOGFONT ();
	public int iMenuWidth;
	public int iMenuHeight;
	public LOGFONT lfMenuFont = new LOGFONT ();
	public LOGFONT lfStatusFont = new LOGFONT ();
	public LOGFONT lfMessageFont = new LOGFONT ();
	public static final int sizeof = OS.NONCLIENTMETRICS_sizeof ();
}

