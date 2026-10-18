/*******************************************************************************
 * Copyright (c) 2000, 2012 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.gtk;


public abstract class XAnyEvent extends XEvent {
	public long serial;
	public int send_event;
	/** @field cast=(Display *) */
	public long display;
	/** @field cast=(Window) */
	public long window;
	public static final int sizeof = OS.XAnyEvent_sizeof();
}
