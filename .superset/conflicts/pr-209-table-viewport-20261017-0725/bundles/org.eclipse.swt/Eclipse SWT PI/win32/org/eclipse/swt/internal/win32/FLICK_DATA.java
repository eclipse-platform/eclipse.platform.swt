/*******************************************************************************
 * Copyright (c) 2010, 2011 IBM Corporation and others.
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

public class FLICK_DATA {
	public int iFlickActionCommandCode;
	public byte iFlickDirection;
	public boolean fControlModifier;
	public boolean fMenuModifier;
	public boolean fAltGRModifier;
	public boolean fWinModifier;
	public boolean fShiftModifier;
	public int iReserved;
	public boolean fOnInkingSurface;
	public int iActionArgument;
	public static final int sizeof = OS.FLICK_DATA_sizeof();
}
