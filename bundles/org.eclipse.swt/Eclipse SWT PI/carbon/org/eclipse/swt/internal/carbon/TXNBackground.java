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

 
public class TXNBackground {
	public int bgType;
	/** @field accessor=bg.color.red */
	public short bg_red;
	/** @field accessor=bg.color.green */
	public short bg_green;
	/** @field accessor=bg.color.blue */
	public short bg_blue;
	public static final int sizeof = 10;
}
