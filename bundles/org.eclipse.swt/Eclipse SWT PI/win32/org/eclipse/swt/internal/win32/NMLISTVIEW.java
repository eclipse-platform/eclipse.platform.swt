/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class NMLISTVIEW extends NMHDR {
	public int iItem;
	public int iSubItem;
	public int uNewState;
	public int uOldState;
	public int uChanged;
//	POINT ptAction;
	public int x, y;
	public int lParam;
	public static int sizeof = 44;
}
