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


public class PhTile_t {
	//	PhRect_t rect;
	/** @field accessor=rect.ul.x */
	public short rect_ul_x;
	/** @field accessor=rect.ul.y */
	public short rect_ul_y;
	/** @field accessor=rect.lr.x */
	public short rect_lr_x;
	/** @field accessor=rect.lr.y */
	public short rect_lr_y;
	/** @field cast=(PhTile_t *) */
	public int next;
	public static final int sizeof = 12;
}
