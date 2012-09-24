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

 
public class XClientMessageEvent {
	public int type;
	public long /*int*/ serial;
	public boolean send_event;
	/** @field cast=(Display *) */
	public long /*int*/ display;
	/** @field cast=(Window) */
	public long /*int*/ window;
	/** @field cast=(Atom) */
	public long /*int*/ message_type;
	public int format;
	/** @field accessor=data.l,cast=(long *) */
	public long /*int*/[] data = new long /*int*/[5];
	public static final int sizeof = OS.XClientMessageEvent_sizeof();
}
