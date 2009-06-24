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


public class PgVideoModeInfo_t {
	public short width;
	public short height;
	public short bits_per_pixel;
	public short bytes_per_scanline;
	public int type;
	public int mode_capabilities1;
	public int mode_capabilities2;
	public int mode_capabilities3;
	public int mode_capabilities4;
	public int mode_capabilities5;
	public int mode_capabilities6;
	/** @field cast=(char *) */
	public byte [] refresh_rates = new byte [20];
	public static final int sizeof = 56;
}
