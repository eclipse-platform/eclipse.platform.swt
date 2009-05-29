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


public class PtFileSelectionInfo_t {
	public short ret;
	public byte [] path = new byte [(OS.PATH_MAX + OS.NAME_MAX + 4) & (~3)];
	public PhDim_t dim = new PhDim_t ();
	public PhPoint_t pos = new PhPoint_t ();
	public byte [] format = new byte [80];
	public byte [] fspec = new byte [80];
	/** @field cast=(void *) */
	public int user_data;
	/** @field cast=(void *) */
	public int confirm_display;
	/** @field cast=(void *) */
	public int confirm_selection;
	/** @field cast=(void *) */
	public int new_directory;
	/** @field cast=(char *) */
	public int btn1;
	/** @field cast=(char *) */
	public int btn2;
	public int num_args;
	/** @field cast=(void *) */
	public int args;
	/** @field cast=(PtFileSelectorInfo_t *) */
	public int minfo;
	/** @field cast=(long *) */
	public int [] spare = new int [3];
	public static final int sizeof = 1500;
}
