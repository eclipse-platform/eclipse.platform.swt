/*******************************************************************************
 * Copyright (c) 2000, 2004 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;

 
public class XCrossingEvent extends XAnyEvent {
	public int /*long*/ root;
	public int /*long*/ subwindow;
	public int time;
	public int x;
	public int y;
	public int x_root;
	public int y_root;
	public int mode;
	public int detail;
	public boolean same_screen;
	public boolean focus;
	public int state;
	public static final int sizeof =  OS.XCrossingEvent_sizeof();
}
