/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class DRAWITEMSTRUCT {
	public int CtlType;
	public int CtlID;
	public int itemID;
	public int itemAction;
	public int itemState;
	public int hwndItem;
	public int hDC;
// 	RECT rcItem;
	public int left, top, bottom, right;
	public int itemData;
	public static final int sizeof = 48;
}
