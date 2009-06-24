/**********************************************************************
 * Copyright (c) 2003, 2008 IBM Corp.
 * Portions Copyright (c) 1983-2002, Apple Computer, Inc.
 *
 * All rights reserved.  This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 **********************************************************************/
package org.eclipse.swt.internal.carbon;


public class CGRect {
	/** @field accessor=origin.x,cast=(float) */
	public float x;
	/** @field accessor=origin.y,cast=(float) */
	public float y;
	/** @field accessor=size.width,cast=(float) */
	public float width;
	/** @field accessor=size.height,cast=(float) */
	public float height;
	public static final int sizeof = 16;
}
