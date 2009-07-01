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

 
public class HIThemeTabDrawInfo {
	public int version;
	public short style;
	public short direction;
	public int size;
	public int adornment;
	/** @field exclude=#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4 */
	public int kind;
	/** @field exclude=#if MAC_OS_X_VERSION_MAX_ALLOWED >= MAC_OS_X_VERSION_10_4 */
	public int position;
	public static final int sizeof = OS.VERSION >= 0x1040 ? 24 : 16;
}
