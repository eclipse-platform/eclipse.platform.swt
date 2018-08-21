/*******************************************************************************
 * Copyright (c) 2009, 2012 IBM Corporation and others.
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

public class CGAffineTransform {
	public double /*float*/ a;
	public double /*float*/ b;
	public double /*float*/ c;
	public double /*float*/ d;
	public double /*float*/ tx;
	public double /*float*/ ty;
	public static int sizeof = OS.CGAffineTransform_sizeof();
}
