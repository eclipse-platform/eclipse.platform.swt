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

public class PANOSE {
	public byte bFamilyType;
	public byte bSerifStyle;
	public byte bWeight;
	public byte bProportion;
	public byte bContrast;
	public byte bStrokeVariation;
	public byte bArmStyle;
	public byte bLetterform;
	public byte bMidline;
	public byte bXHeight;
	public static final int sizeof = OS.PANOSE_sizeof ();
}
