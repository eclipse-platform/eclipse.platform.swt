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
	public float /*double*/ a;
	public float /*double*/ b;
	public float /*double*/ c;
	public float /*double*/ d;
	public float /*double*/ tx;
	public float /*double*/ ty;
	public static int sizeof = OS.CGAffineTransform_sizeof();
}
