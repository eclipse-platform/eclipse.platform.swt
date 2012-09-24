/*******************************************************************************
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.cocoa;

public class CGAffineTransform {
	public double /*float*/ a;
	public double /*float*/ b;
	public double /*float*/ c;
	public double /*float*/ d;
	public double /*float*/ tx;
	public double /*float*/ ty;
	public static int sizeof = OS.CGAffineTransform_sizeof();
}
