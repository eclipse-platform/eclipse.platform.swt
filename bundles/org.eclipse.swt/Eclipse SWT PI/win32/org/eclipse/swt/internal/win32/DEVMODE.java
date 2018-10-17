/*******************************************************************************
 * Copyright (c) 2000, 2009 IBM Corporation and others.
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

public class DEVMODE {
	public char[] dmDeviceName = new char[OS.CCHDEVICENAME];
	public short dmSpecVersion;
	public short dmDriverVersion;
	public short dmSize;
	public short dmDriverExtra;
	public int dmFields;
	public short dmOrientation;
	public short dmPaperSize;
	public short dmPaperLength;
	public short dmPaperWidth;
	public short dmScale;
	public short dmCopies;
	public short dmDefaultSource;
	public short dmPrintQuality;
	public short dmColor;
	public short dmDuplex;
	public short dmYResolution;
	public short dmTTOption;
	public short dmCollate;
	public char[] dmFormName = new char[OS.CCHFORMNAME];
	public short dmLogPixels;
	public int dmBitsPerPel;
	public int dmPelsWidth;
	public int dmPelsHeight;
	public int dmNup;
	public int dmDisplayFrequency;
	public int dmICMMethod;
	public int dmICMIntent;
	public int dmMediaType;
	public int dmDitherType;
	public int dmReserved1;
	public int dmReserved2;
	public int dmPanningWidth;
	public int dmPanningHeight;
	public static final int sizeof = OS.DEVMODE_sizeof ();
}
