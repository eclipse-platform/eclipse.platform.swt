/*******************************************************************************
 * Copyright (c) 2008, 2012 IBM Corporation and others.
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
package org.eclipse.swt.internal.cocoa;


public class NSRect {
	/** @field accessor=origin.x */
	public double x;
	/** @field accessor=origin.y */
	public double y;
	/** @field accessor=size.width */
	public double width;
	/** @field accessor=size.height */
	public double height;
	public static final int sizeof = OS.NSRect_sizeof();

	@Override
	public String toString() {
		return "NSRect{" + x + "," + y + "," + width + "," + height + "}";
	}
}
