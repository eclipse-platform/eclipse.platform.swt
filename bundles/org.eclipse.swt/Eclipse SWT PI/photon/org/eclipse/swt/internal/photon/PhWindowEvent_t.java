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
package org.eclipse.swt.internal.photon;


public class PhWindowEvent_t {
	public int event_f;
	public int state_f;
	public int rid;
//	PhPoint_t pos;
	public short pos_x;
	public short pos_y;
//	PhDim_t size;
	public short size_w;
	public short size_h;
	public short event_state;
	public short input_group;
	public int rsvd0, rsvd1, rsvd2, rsvd3;
	public static final int sizeof = 40;
}
