/*******************************************************************************
 * Copyright (c) 2008, 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;


public class NSRect {
	/** @field accessor=origin.x */
	public float /*double*/ x;
	/** @field accessor=origin.y */
	public float /*double*/ y;
	/** @field accessor=size.width */
	public float /*double*/ width;
	/** @field accessor=size.height */
	public float /*double*/ height;
	public static final int sizeof = OS.NSRect_sizeof();

	public String toString() {
		return "NSRect{" + x + "," + y + "," + width + "," + height + "}"; 
	}
}
