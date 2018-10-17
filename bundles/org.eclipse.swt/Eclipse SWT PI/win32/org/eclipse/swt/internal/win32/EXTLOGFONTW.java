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

public class EXTLOGFONTW {
	public LOGFONT elfLogFont = new LOGFONT();
	public char[] elfFullName = new char[OS.LF_FULLFACESIZE];
	public char[] elfStyle = new char[OS.LF_FACESIZE];
	public int elfVersion;
	public int elfStyleSize;
	public int elfMatch;
	public int elfReserved;
	public byte[] elfVendorId = new byte[OS.ELF_VENDOR_SIZE];
	public int elfCulture;
	public PANOSE elfPanose = new PANOSE();
	public static final int sizeof = OS.EXTLOGFONTW_sizeof ();
}
