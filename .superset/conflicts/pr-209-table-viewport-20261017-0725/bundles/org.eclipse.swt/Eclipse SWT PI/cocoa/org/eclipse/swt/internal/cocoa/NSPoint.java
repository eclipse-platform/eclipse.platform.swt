/*******************************************************************************
 * Copyright (c) 2000, 2019 IBM Corporation and others.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *    IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class NSPoint {
	/** @field cast=(CGFloat) */
	public double x;
	/** @field cast=(CGFloat) */
	public double y;
	public static final int sizeof = OS.NSPoint_sizeof();

	@Override
	public String toString() {
		return "NSPoint{" + x + "," + y + "}";
	}
}
