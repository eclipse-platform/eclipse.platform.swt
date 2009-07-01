/**********************************************************************
 * Copyright (c) 2003, 2008 IBM Corp.
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


public class ControlFontStyleRec {
	public short flags;
	public short font;
	public short size;
	public short style;
	public short mode;
	public short just;
//	RGBColor foreColor;
	/** @field accessor=foreColor.red */
	public short foreColor_red;
	/** @field accessor=foreColor.green */
	public short foreColor_green;
	/** @field accessor=foreColor.blue */
	public short foreColor_blue;
//	RGBColor backColor;
	/** @field accessor=backColor.red */
	public short backColor_red;
	/** @field accessor=backColor.green */
	public short backColor_green;
	/** @field accessor=backColor.blue */
	public short backColor_blue;
	public static final int sizeof = 24;
}
