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

public abstract class NONCLIENTMETRICS {
	public int cbSize;
	public int iBorderWidth;
	public int iScrollWidth;
	public int iScrollHeight;
	public int iCaptionWidth;
	public int iCaptionHeight;
	public int iSmCaptionWidth;
	public int iSmCaptionHeight;
	public int iMenuWidth;
	public int iMenuHeight;
	public static final int sizeof = OS.IsUnicode ? OS.NONCLIENTMETRICSW_sizeof () : OS.NONCLIENTMETRICSA_sizeof ();
}

