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
	public double /*float*/ x;
	/** @field accessor=origin.y */
	public double /*float*/ y;
	/** @field accessor=size.width */
	public double /*float*/ width;
	/** @field accessor=size.height */
	public double /*float*/ height;
	public static final int sizeof = OS.NSRect_sizeof();

	public String toString() {
		return "NSRect{" + x + "," + y + "," + width + "," + height + "}"; 
	}
}
