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

 
public class HMHelpContentRec {
	public int version;
//	Rect absHotRect;
	public short absHotRect_top;
	public short absHotRect_left;
	public short absHotRect_bottom;
	public short absHotRect_right;
	public short tagSide;
//	HMHelpContent       content[2];
	public int content0_contentType;
	public int content0_tagCFString; 
	public int content1_contentType;
	public int content1_tagCFString;
	public static final int sizeof = 534;
}
