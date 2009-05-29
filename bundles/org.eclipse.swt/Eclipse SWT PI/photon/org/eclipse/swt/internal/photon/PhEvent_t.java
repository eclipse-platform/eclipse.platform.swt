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


public class PhEvent_t {
	public int type;
	public short subtype;
	public short processing_flags;
//	public PhEventRegion_t emitter;
	/** @field accessor=emitter.rid */
	public int emitter_rid;
	/** @field accessor=emitter.handle */
	public int emitter_handle;
//	public PhEventRegion_t collector;
	/** @field accessor=collector.rid */
	public int collector_rid;
	/** @field accessor=collector.handle */
	public int collector_handle;
	public short input_group;
	public short flags;
	public int timestamp;
//	public PhPoint_t translation;
	/** @field accessor=translation.x */
	public short translation_x;
	/** @field accessor=translation.y */
	public short translation_y;
	public short num_rects;
	public short data_len;
	public static final int sizeof = 40;
}
