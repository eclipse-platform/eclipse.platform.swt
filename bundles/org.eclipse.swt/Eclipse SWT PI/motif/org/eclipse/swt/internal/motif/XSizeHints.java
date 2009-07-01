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

 
public class XSizeHints {
	public int flags;
	public int x, y;
	public int width, height;
	public int min_width, min_height;
	public int max_width, max_height;
	public int width_inc, height_inc;
	/** @field accessor=min_aspect.x */
	public int aspect_x;
	/** @field accessor=min_aspect.y */
	public int aspect_y;
    public int base_width, base_height;
    public int win_gravity;
	public static final int sizeof = 72;
}
