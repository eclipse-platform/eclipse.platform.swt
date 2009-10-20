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
