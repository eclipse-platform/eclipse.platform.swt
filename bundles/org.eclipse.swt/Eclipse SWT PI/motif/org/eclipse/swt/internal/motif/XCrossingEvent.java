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

 
public class XCrossingEvent extends XAnyEvent {
	public int root;
	public int subwindow;
	public int time;
	public int x;
	public int y;
	public int x_root;
	public int y_root;
	public int mode;
	public int detail;
	public int same_screen;
	public int focus;
	public int state;
	public static final int sizeof = 68;
}
