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


public class PhRegion_t {
	public int rid;
	public int handle;
	public int owner;
	public int flags;
	public short state;
//	public short zero1;
	public int events_sense;
	public int events_opaque;
	/** @field accessor=origin.x */
	public short origin_x;
	/** @field accessor=origin.y */
	public short origin_y;
	public int parent;
	public int child;
	public int bro_in_front;
	public int bro_behind;
	public int cursor_color;
	public short input_group;
	public short data_len;
//	public int zero21;
//	public int zero22;
	public short cursor_type;
//	public short zero3;
	public static final int sizeof = 68;
}
