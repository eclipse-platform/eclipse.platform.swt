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
package org.eclipse.swt.internal.motif;

 
public class Visual {
	/** @field cast=(XExtData *) */
	public int ext_data;
	public int visualid;
	/** @field accessor=class */
	public int c_class;
	public int red_mask;
	public int green_mask;
	public int blue_mask;
	public int bits_per_rgb;
	public int map_entries;
	public static final int sizeof = 32;
}
