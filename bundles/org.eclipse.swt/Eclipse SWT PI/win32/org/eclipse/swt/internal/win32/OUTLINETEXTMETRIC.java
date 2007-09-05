/*******************************************************************************
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class OUTLINETEXTMETRIC {
	public int otmSize;
    public byte otmFiller;
    public byte otmPanoseNumber_bFamilyType;
    public byte otmPanoseNumber_bSerifStyle;
    public byte otmPanoseNumber_bWeight;
    public byte otmPanoseNumber_bProportion;
    public byte otmPanoseNumber_bContrast;
    public byte otmPanoseNumber_bStrokeVariation;
    public byte otmPanoseNumber_bArmStyle;
    public byte otmPanoseNumber_bLetterform;
    public byte otmPanoseNumber_bMidline;
    public byte otmPanoseNumber_bXHeight;
    public int otmfsSelection;
    public int otmfsType;
    public int otmsCharSlopeRise;
    public int otmsCharSlopeRun;
    public int otmItalicAngle;
    public int otmEMSquare;
    public int otmAscent;
    public int otmDescent;
    public int otmLineGap;
    public int otmsCapEmHeight;
    public int otmsXHeight;
	public RECT otmrcFontBox = new RECT();
    public int otmMacAscent;
    public int otmMacDescent;
    public int otmMacLineGap;
    public int otmusMinimumPPEM;
	public POINT otmptSubscriptSize = new POINT();
	public POINT otmptSubscriptOffset = new POINT();
	public POINT otmptSuperscriptSize = new POINT();
	public POINT otmptSuperscriptOffset = new POINT();
    public int otmsStrikeoutSize;
    public int otmsStrikeoutPosition;
    public int otmsUnderscoreSize;
    public int otmsUnderscorePosition;
    public int /*long*/ otmpFamilyName;
    public int /*long*/ otmpFaceName;
    public int /*long*/ otmpStyleName;
    public int /*long*/ otmpFullName;
    public static final int sizeof = OS.IsUnicode ? OS.OUTLINETEXTMETRICW_sizeof ():  OS.OUTLINETEXTMETRICA_sizeof ();
}
