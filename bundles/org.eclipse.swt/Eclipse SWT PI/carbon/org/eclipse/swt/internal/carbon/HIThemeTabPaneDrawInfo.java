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

 
public class HIThemeTabPaneDrawInfo {
	public int version;
	public int state;
	public short direction;
	public int size;	
	public int kind;
	public int adornment;
	public static final int sizeof = OS.VERSION >= 0x1040 ? 22 : 14;
}
