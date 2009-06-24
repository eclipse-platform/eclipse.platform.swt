/**********************************************************************
 * Copyright (c) 2003, 2006 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;


public class ATSFontMetrics {
	public int version;
	public float ascent;
	public float descent;
	public float leading;
	public float avgAdvanceWidth;
	public float maxAdvanceWidth;
	public float minLeftSideBearing;
	public float minRightSideBearing;
	public float stemWidth;
	public float stemHeight;
	public float capHeight;
	public float xHeight;
	public float italicAngle;
	public float underlinePosition;
	public float underlineThickness;
	public static final int sizeof = 60;
}
