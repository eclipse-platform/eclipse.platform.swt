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


public class GdkEventButton extends GdkEvent {
	public int time;
	public long x;
	public long y;
	public long pressure;
	public long xtilt;
	public long ytilt;
	public int state;
	public int button;
	public int source;
	public int deviceid;
	public long x_root;
	public long y_root;
}
