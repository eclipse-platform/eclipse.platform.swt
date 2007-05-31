/*******************************************************************************
 * Copyright (c) 2000, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMLVFINDITEM extends NMHDR {
	public int iStart;
//	LVFINDINFO lvfi;
	public int flags;
	public int /*long*/ psz;
	public int /*long*/ lParam;
//	POINT pt;
	public int x;
	public int y;
	public int vkDirection;
	public static final int sizeof = OS.NMLVFINDITEM_sizeof ();
}
