/**********************************************************************
 * Copyright (c) 2003-2004 IBM Corp.
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


public class FontSelectionQDStyle {
	public int version;
	//FMFontFamilyInstance instance;
	public short instance_fontFamily;
	public short instance_fontStyle;
	public short size;
	public boolean hasColor;
	public byte reserved;
	//RGBColor color
	public short color_red;
	public short color_green;
	public short color_blue;
	public static final int sizeof = 18;
}
