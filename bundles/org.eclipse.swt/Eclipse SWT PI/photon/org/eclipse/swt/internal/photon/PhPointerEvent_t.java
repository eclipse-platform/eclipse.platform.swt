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


public class PhPointerEvent_t {
//	public PhPoint_t pos;
	/** @field accessor=pos.x */
	public short pos_x;
	/** @field accessor=pos.y */
	public short pos_y;
	public short buttons;
	public short button_state;	
	public byte click_count;	
	public byte flags;	
	public short z;	
	public int key_mods;
	public int zero;
	public static final int sizeof = 20;
}
