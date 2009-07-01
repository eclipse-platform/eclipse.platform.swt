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


public class PhWindowEvent_t {
	public int event_f;
	public int state_f;
	public int rid;
//	PhPoint_t pos;
	/** @field accessor=pos.x */
	public short pos_x;
	/** @field accessor=pos.y */
	public short pos_y;
//	PhDim_t size;
	/** @field accessor=size.w */
	public short size_w;
	/** @field accessor=size.h */
	public short size_h;
	public short event_state;
	public short input_group;
	/** @field accessor=rsvd[0] */
	public int rsvd0;
	/** @field accessor=rsvd[1] */
	public int rsvd1;
	/** @field accessor=rsvd[2] */
	public int rsvd2;
	/** @field accessor=rsvd[3] */
	public int rsvd3;
	public static final int sizeof = 40;
}
