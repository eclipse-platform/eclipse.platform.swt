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
package org.eclipse.swt.internal.motif;

 
public class XtWidgetGeometry {
	public int request_mode;
	public int x;
	public int y;
	public int width;
	public int height;
	public int border_width;
	/** @field cast=(Widget) */
	public int sibling;
	public int stack_mode; 
	public static final int sizeof = 24;
}
