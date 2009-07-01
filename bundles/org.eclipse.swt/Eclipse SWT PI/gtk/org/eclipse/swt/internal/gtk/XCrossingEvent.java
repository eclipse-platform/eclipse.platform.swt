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
package org.eclipse.swt.internal.gtk;

 
public class XCrossingEvent extends XAnyEvent {
	/** @field cast=(Window) */
	public int /*long*/ root;
	/** @field cast=(Window) */
	public int /*long*/ subwindow;
	/** @field cast=(Time) */
	public int time;
	public int x;
	public int y;
	public int x_root;
	public int y_root;
	public int mode;
	public int detail;
	/** @field cast=(Bool) */
	public boolean same_screen;
	/** @field cast=(Bool) */
	public boolean focus;
	/** @field cast=(unsigned int) */
	public int state;
	public static final int sizeof =  OS.XCrossingEvent_sizeof();
}
