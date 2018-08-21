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
package org.eclipse.swt.internal.ole.win32;

public final class DVTARGETDEVICE {
	public int tdSize;
	public short tdDriverNameOffset;
	public short tdDeviceNameOffset;
	public short tdPortNameOffset;
	public short tdExtDevmodeOffset;
	public byte[] tdData = new byte[1];
	public static final int sizeof = COM.DVTARGETDEVICE_sizeof ();
}
