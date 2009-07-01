/*******************************************************************************
 * Copyright (c) 2007, 2008 IBM Corporation and others.
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
    /** @field accessor=otmPanoseNumber.bFamilyType */
	public byte otmPanoseNumber_bFamilyType;
    /** @field accessor=otmPanoseNumber.bSerifStyle */
	public byte otmPanoseNumber_bSerifStyle;
    /** @field accessor=otmPanoseNumber.bWeight */
	public byte otmPanoseNumber_bWeight;
    /** @field accessor=otmPanoseNumber.bProportion */
	public byte otmPanoseNumber_bProportion;
    /** @field accessor=otmPanoseNumber.bContrast */
	public byte otmPanoseNumber_bContrast;
    /** @field accessor=otmPanoseNumber.bStrokeVariation */
	public byte otmPanoseNumber_bStrokeVariation;
    /** @field accessor=otmPanoseNumber.bArmStyle */
	public byte otmPanoseNumber_bArmStyle;
    /** @field accessor=otmPanoseNumber.bLetterform */
	public byte otmPanoseNumber_bLetterform;
    /** @field accessor=otmPanoseNumber.bMidline */
	public byte otmPanoseNumber_bMidline;
    /** @field accessor=otmPanoseNumber.bXHeight */
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
    /** @field cast=(PSTR) */
	public int /*long*/ otmpFamilyName;
    /** @field cast=(PSTR) */
	public int /*long*/ otmpFaceName;
    /** @field cast=(PSTR) */
	public int /*long*/ otmpStyleName;
    /** @field cast=(PSTR) */
	public int /*long*/ otmpFullName;
    public static final int sizeof = OS.IsUnicode ? OS.OUTLINETEXTMETRICW_sizeof ():  OS.OUTLINETEXTMETRICA_sizeof ();
}
