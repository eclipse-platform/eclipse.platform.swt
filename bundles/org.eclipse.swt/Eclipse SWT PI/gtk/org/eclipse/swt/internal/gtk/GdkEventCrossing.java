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


public class GdkEventCrossing extends GdkEvent {
	public int window;
	public byte send_event;
	public int subwindow;
	public int time;
	public double x;
	public double y;
	public double x_root;
	public double y_root;
	public int mode;
	public int detail;
	public boolean focus;
	public int state;
	public static final int sizeof = 68;
}
