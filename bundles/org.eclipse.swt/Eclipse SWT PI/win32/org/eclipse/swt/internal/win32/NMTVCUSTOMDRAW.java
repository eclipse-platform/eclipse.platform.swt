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

public class NMTVCUSTOMDRAW extends NMCUSTOMDRAW {
	public int clrText;
	public int clrTextBk;
	/** @field flags=no_wince */
	public int iLevel; // the iLevel field does not appear on WinCE
	public static final int sizeof = OS.NMTVCUSTOMDRAW_sizeof ();
}
