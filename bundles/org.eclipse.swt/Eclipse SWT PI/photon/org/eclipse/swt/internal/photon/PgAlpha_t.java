package org.eclipse.swt.internal.photon;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class PgAlpha_t {
	
	public int alpha_op;
	// PgMap_t src_alpha_map;
	public short src_alpha_map_dim_w;
	public short src_alpha_map_dim_h;
	public short src_alpha_map_bpl;
	public short src_alpha_map_bpp;
	public int src_alpha_map_map;
	// PgGradient_t src_alpha_gradient; *** unused
	// PgMap_t dest_alpha_map; *** unused
	// PgGradient_t dest_alpha_gradient; *** unused
	public byte src_global_alpha;
	public byte dest_global_alpha;	
	// char spare[2]; *** unused
	public static final int sizeof = 104;
}
