/*******************************************************************************
 * Copyright (c) 2000, 2003 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.motif;

 
public class XConfigureEvent extends XEvent {
	public int serial;
	public int send_event;
	public int display;
	public int event;
	public int window;
	public int x;
	public int y;
	public int width;
	public int height;
	public int border_width;
	public int above;
	public int override_redirect;
	public static final int sizeof = 52;
}
