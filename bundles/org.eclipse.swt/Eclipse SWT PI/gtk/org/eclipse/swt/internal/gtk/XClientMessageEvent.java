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

 
public class XClientMessageEvent {
	public int type;
	public int /*long*/ serial;
	public boolean send_event;
	public int /*long*/ display;
	public int /*long*/ window;
	public int /*long*/ message_type;
	public int format;
	public int /*long*/[] data = new int /*long*/[5];
	public static final int sizeof = OS.XClientMessageEvent_sizeof();
}
