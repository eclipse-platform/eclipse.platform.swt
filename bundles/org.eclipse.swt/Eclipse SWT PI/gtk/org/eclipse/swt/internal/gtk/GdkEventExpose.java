/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;


public class GdkEventExpose extends GdkEvent {
	public int window;
	public byte send_event;
	public int area_x;
	public int area_y;
	public int area_width;
	public int area_height;
	public int region;
	public int count;
	public static final int sizeof = 36;
}

