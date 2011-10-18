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


public class NSPoint {
	public float /*double*/ x;
	public float /*double*/ y;
	public static final int sizeof = OS.NSPoint_sizeof();

	public String toString() {
		return "NSPoint{" + x + "," + y + "}"; 
	}
}
