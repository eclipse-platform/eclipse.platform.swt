/*******************************************************************************
 * Copyright (c) 2000, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class DOCHOSTUIINFO {
	public int cbSize;
	public int dwFlags;
	public int dwDoubleClick;
	/*
	 * TODO uncomment the following two fields when 32-bit swt starts
	 * compiling with a newer mssdk whose definition of DOCHOSTUIINFO
	 * includes these fields.
	 */
//	public int /*long*/ pchHostCss;
//	public int /*long*/ pchHostNS;
	public static final int sizeof = OS.DOCHOSTUIINFO_sizeof ();
}
