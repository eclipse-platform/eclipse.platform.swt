/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
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
	/** @field accessor=pos.x */
	public short pos_x;
	/** @field accessor=pos.y */
	public short pos_y;
	public short button_state;
	public static final int sizeof = OS.QNX_MAJOR * 100 + OS.QNX_MINOR * 10 + OS.QNX_MICRO >= 610 ? 28 : 26;
}
