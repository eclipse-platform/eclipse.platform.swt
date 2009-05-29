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

 
public class HMHelpContentRec {
	public int version;
//	Rect absHotRect;
	/** @field accessor=absHotRect.top */
	public short absHotRect_top;
	/** @field accessor=absHotRect.left */
	public short absHotRect_left;
	/** @field accessor=absHotRect.bottom */
	public short absHotRect_bottom;
	/** @field accessor=absHotRect.right */
	public short absHotRect_right;
	public short tagSide;
//	HMHelpContent       content[2];
	/** @field accessor=content[0].contentType */
	public int content0_contentType;
	/** @field accessor=content[0].u.tagCFString,cast=(CFStringRef) */
	public int content0_tagCFString; 
	/** @field accessor=content[1].contentType */
	public int content1_contentType;
	/** @field accessor=content[1].u.tagCFString,cast=(CFStringRef) */
	public int content1_tagCFString;
	public static final int sizeof = 534;
}
