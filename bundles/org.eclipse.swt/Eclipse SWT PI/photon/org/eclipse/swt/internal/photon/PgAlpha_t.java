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


public class PgAlpha_t {
	public int alpha_op;
	// PgMap_t src_alpha_map;
	/** @field accessor=src_alpha_map.dim.w */
	public short src_alpha_map_dim_w;
	/** @field accessor=src_alpha_map.dim.h */
	public short src_alpha_map_dim_h;
	/** @field accessor=src_alpha_map.bpl */
	public short src_alpha_map_bpl;
	/** @field accessor=src_alpha_map.bpp */
	public short src_alpha_map_bpp;
	/** @field accessor=src_alpha_map.map,cast=(char *) */
	public int src_alpha_map_map;
	// PgGradient_t src_alpha_gradient; *** unused
	// PgMap_t dest_alpha_map; *** unused
	// PgGradient_t dest_alpha_gradient; *** unused
	public byte src_global_alpha;
	public byte dest_global_alpha;	
	// char spare[2]; *** unused
	public static final int sizeof = 104;
}
