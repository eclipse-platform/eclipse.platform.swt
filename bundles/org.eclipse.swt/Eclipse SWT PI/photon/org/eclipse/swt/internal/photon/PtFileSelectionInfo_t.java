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


public class PtFileSelectionInfo_t {
	public short ret;
	public byte [] path = new byte [(OS.PATH_MAX + OS.NAME_MAX + 4) & (~3)];
	public PhDim_t dim = new PhDim_t ();
	public PhPoint_t pos = new PhPoint_t ();
	public byte [] format = new byte [80];
	public byte [] fspec = new byte [80];
	public int user_data;
	public int confirm_display;
	public int confirm_selection;
	public int new_directory;
	public int btn1;
	public int btn2;
	public int num_args;
	public int args;
	public int minfo;
	public int [] spare = new int [3];
	public static final int sizeof = 1500;
}
