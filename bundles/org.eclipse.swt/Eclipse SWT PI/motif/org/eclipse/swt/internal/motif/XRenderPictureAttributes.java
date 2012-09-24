/*******************************************************************************
 * Copyright (c) 2000, 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.motif;

 
public class XRenderPictureAttributes {
	public boolean repeat;
	public long /*int*/ alpha_map;
	public int alpha_x_origin;
	public int alpha_y_origin;
	public int clip_x_origin;
	public int clip_y_origin;
	public long /*int*/ clip_mask;
	public boolean graphics_exposures;
	public int subwindow_mode;
	public int poly_edge;
	public int poly_mode;
	public long /*int*/ dither;
	public boolean component_alpha;
	public static final int sizeof = OS.XRenderPictureAttributes_sizeof();
}
