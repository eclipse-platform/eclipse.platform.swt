package org.eclipse.swt.internal.photon;

/*
 * (c) Copyright IBM Corp. 2000, 2001.
 * All Rights Reserved
 */

public class PgDisplaySettings_t {
	public int mode;
	public int xres;
	public int yres;
	public int refresh;
	public int flags;
	public int [] reserved = new int [22];
	public static final int sizeof = 108;
}
