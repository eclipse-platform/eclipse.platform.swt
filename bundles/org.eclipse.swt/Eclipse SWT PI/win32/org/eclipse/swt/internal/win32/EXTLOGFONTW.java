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

public class EXTLOGFONTW {
	public LOGFONTW elfLogFont = new LOGFONTW();
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
