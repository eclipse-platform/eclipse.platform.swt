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

public class COMBOBOXINFO {
	public int cbSize;
	public int itemLeft;
	public int itemTop;
	public int itemRight;
	public int itemBottom;
	public int buttonLeft;
	public int buttonTop;
	public int buttonRight;
	public int buttonBottom;
	public int stateButton;
	public int hwndCombo;
	public int hwndItem;
	public int hwndList;
	public static final int sizeof = 52;    
}
