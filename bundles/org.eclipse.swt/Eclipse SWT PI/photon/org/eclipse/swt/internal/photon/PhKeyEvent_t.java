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
package org.eclipse.swt.internal.photon;


public class PhKeyEvent_t {
	public int key_mods;
	public int key_flags;
	public int key_cap;
	public int key_sym;
	public short key_scan;
	public short key_zero;
//	PhPoint_t pos;
	public short pos_x;
	public short pos_y;
	public short button_state;
	public static final int sizeof = 26;
}
