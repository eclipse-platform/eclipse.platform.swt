/**********************************************************************
 * Copyright (c) 2003, 2005 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;

 
public class HIThemeTrackDrawInfo {
	public int version;
	public short kind;
	//HIRect bounds
	public float bounds_x;
	public float bounds_y;
	public float bounds_width;
	public float bounds_height;
	public int min;
	public int max;
	public int value;
	public int reserved;
	public short attributes;
	public byte enableState;
	public byte filler1;
	public ScrollBarTrackInfo scrollbar;
	public SliderTrackInfo slider;
	public ProgressTrackInfo progress;
	public static final int sizeof = 48;
}
