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


public class PhTile_t {
	//	PhRect_t rect;
	public short rect_ul_x;
	public short rect_ul_y;
	public short rect_lr_x;
	public short rect_lr_y;
	public int next;
	public static final int sizeof = 12;
}
