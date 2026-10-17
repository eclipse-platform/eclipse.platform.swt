/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.opengl.glx;

public class XVisualInfo {
	/** @field cast=(Visual *) */
	public long visual;
	public int visualid;
	public int screen;
	public int depth;
	/** @field accessor=class */
	public int cclass;
	public int red_mask, green_mask, blue_mask;
	public int colormap_size;
	public int bits_per_rgb;
	public static final int sizeof = GLX.XVisualInfo_sizeof();
}
