/*******************************************************************************
 * Copyright (c) 2000, 2008 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.swt.internal.win32;

public class OFNOTIFY extends NMHDR {
	/** @field cast=(LPOPENFILENAME) */
	public long /*int*/ lpOFN;
	/** @field cast=(LPTSTR) */
	public long /*int*/ pszFile;
	public static int sizeof = OS.OFNOTIFY_sizeof ();
}
