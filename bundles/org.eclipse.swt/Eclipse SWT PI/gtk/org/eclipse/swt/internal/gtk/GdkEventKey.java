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


public class GdkEventKey extends GdkEvent {
	public int window;
	public byte send_event;
	public int time;
	public int state;
	public int keyval;
	public int length;
	public int string;
	public short hardware_keycode;
    public byte group;
   	public static final int sizeof = 36;
}
