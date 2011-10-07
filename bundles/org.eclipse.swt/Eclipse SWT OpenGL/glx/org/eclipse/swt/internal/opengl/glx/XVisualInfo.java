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
package org.eclipse.swt.internal.opengl.glx;

public class XVisualInfo {
	/** @field cast=(Visual *) */
	public int /*long*/ visual;
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
