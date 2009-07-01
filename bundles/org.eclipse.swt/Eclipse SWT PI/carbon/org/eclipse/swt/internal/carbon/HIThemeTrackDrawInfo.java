/**********************************************************************
 * Copyright (c) 2003, 2008 IBM Corp.
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
	/** @field cast=(ThemeTrackKind) */
	public short kind;
	//HIRect bounds
	/** @field accessor=bounds.origin.x */
	public float bounds_x;
	/** @field accessor=bounds.origin.y */
	public float bounds_y;
	/** @field accessor=bounds.size.width */
	public float bounds_width;
	/** @field accessor=bounds.size.height */
	public float bounds_height;
	public int min;
	public int max;
	public int value;
	public int reserved;
	/** @field cast=(ThemeTrackAttributes) */
	public short attributes;
	/** @field cast=(ThemeTrackEnableState) */
	public byte enableState;
	public byte filler1;
	/** @field accessor=trackInfo.scrollbar */
	public ScrollBarTrackInfo scrollbar;
	/** @field accessor=trackInfo.slider */
	public SliderTrackInfo slider;
	/** @field accessor=trackInfo.progress */
	public ProgressTrackInfo progress;
	public static final int sizeof = 48;
}
